import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable {

    // Tüm istemcilerin tutulduğu liste
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    // İstemci ile olan bağlantı ve mesaj iletişimini sağlayacak değişkenler
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername; // İstemci adı
    private String clientColor;    // İstemciye atanacak rastgele renk

    // ANSI renk kodları (terminalde kullanılacak)
    private static final String[] COLORS = {
        "\033[0;31m", // Kırmızı
        "\033[0;32m", // Yeşil
        "\033[0;33m", // Sarı
        "\033[0;34m", // Mavi
        "\033[0;35m", // Mor
        "\033[0;36m", // Camgöbeği
        "\033[0;37m"  // Beyaz
    };
    private static final String RESET_COLOR = "\033[0m"; // Renk sıfırlama kodu (mesajın sonunda kullanılır)

    // Spam kontrolü için gerekli değişkenler
    private static final int MESSAGE_LIMIT = 5; // Belirli bir süre içinde izin verilen maksimum mesaj sayısı
    private static final long TIME_WINDOW_MS = TimeUnit.MINUTES.toMillis(1); // Spam kontrol süresi (örneğin, 1 dakika)
    private static final Map<String, MessageCounter> messageCounters = new HashMap<>(); // Kullanıcı bazında mesaj sayacı

    // Yapıcı metod, istemci bağlandığında çalışır
    public ClientHandler(Socket socket) {
        try {
            // İstemci bağlantısını ve okuma/yazma akışlarını başlatır
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // İstemci adını alır
            this.clientUsername = bufferedReader.readLine();

            // İstemciye rastgele bir renk atar
            this.clientColor = getRandomColor();

            // İstemciyi diğer istemciler arasına ekler
            clientHandlers.add(this);

            // Yeni bir sunucu başlatıldığında önceki sohbet geçmişini temizler
            clearChatHistory();

            // Tüm istemcilere yeni bir kullanıcının katıldığını bildirir
            broadcastMessage("Sunucu: " + clientUsername + " sohbete katıldı!");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        // İstemci bağlı olduğu sürece mesaj dinler
        while (socket.isConnected()) {
            try {
                // İstemciden gelen mesajı okur
                messageFromClient = bufferedReader.readLine();

                // Özel mesaj kontrolü ("/w" ile başlar)
                if (messageFromClient.startsWith("/w ")) {
                    sendPrivateMessage(messageFromClient);
                }
                // Aktif kullanıcı listesini isteme komutu ("/listele")
                else if (messageFromClient.equals("/listele")) {
                    sendUserList();
                }
                // Spam kontrolü
                else if (isSpam(clientUsername)) {
                    // Spam davranışı tespit edilirse istemciye uyarı mesajı gönderilir
                    bufferedWriter.write("Sunucu: Spam davranışı tespit edildi! Mesaj gönderme sıklığınızı azaltın.");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                else {
                    // Küfür filtreleme (opsiyonel bir filtre kullanılabilir)
                    messageFromClient = ProfanityFilter.filterProfanity(messageFromClient);

                    // Mesajı tüm istemcilere yayınlar
                    broadcastMessage(messageFromClient);

                    // Mesajı kaydeder
                    logMessage(messageFromClient);
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    // Mesajı tüm kullanıcılara yayınlama metodu
    public void broadcastMessage(String messageToSend) {
        String timestamp = getTimestamp(); // Zaman damgası
        String formattedMessage = formatMessage(messageToSend); // Mesaj formatı (renkli)

        // Tüm istemcilere mesajı iletir
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                // Mesajın gönderildiği istemci kendi mesajını renkli ve zaman damgalı alır
                if (clientHandler.clientUsername.equals(this.clientUsername)) {
                    clientHandler.bufferedWriter.write(timestamp + " " + formattedMessage);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
                // Diğer istemcilere renkli mesaj iletilir
                else {
                    clientHandler.bufferedWriter.write(timestamp + " " + formattedMessage);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    // Özel mesaj gönderme metodu
    public void sendPrivateMessage(String message) {
        try {
            String[] messageParts = message.split(" ", 3); // Mesaj parçalanır ("/w", alıcı, mesaj)
            String recipientUsername = messageParts[1]; // Alıcı adı
            String privateMessage = getTimestamp() + " " + formatMessage("Özel mesaj " + clientUsername + ": " + messageParts[2]);

            // Mesajı alıcıya iletir
            for (ClientHandler clientHandler : clientHandlers) {
                if (clientHandler.clientUsername.equals(recipientUsername)) {
                    clientHandler.bufferedWriter.write(privateMessage);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                    break;
                }
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // Aktif kullanıcı listesini istemciye gönderir
    public void sendUserList() {
        try {
            bufferedWriter.write("Aktif kullanıcılar:");
            bufferedWriter.newLine();

            // Her bir kullanıcı adı listeye eklenir
            for (ClientHandler clientHandler : clientHandlers) {
                bufferedWriter.write(clientHandler.clientUsername);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // Mesajı dosyaya kaydeder
    public void logMessage(String message) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("chat_history.txt", true))) {
            // Zaman damgası ile birlikte mesaj kaydedilir
            fileWriter.write(getTimestamp() + " " + message);
            fileWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // İstemci ayrıldığında yapılacaklar
    public void removeClientHandler() {
        clientHandlers.remove(this); // İstemci listeden çıkarılır
        broadcastMessage("Sunucu: " + clientUsername + " sohbetten ayrıldı!"); // Diğer istemcilere bildirilir
    }

    // Kaynakları kapatma metodu
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler(); // İstemci listeden çıkarılır
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Zaman damgası elde etmek için yardımcı metod
    private String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    // Rastgele bir renk döndüren metod
    private String getRandomColor() {
        Random random = new Random();
        int index = random.nextInt(COLORS.length); // Rastgele renk seçimi
        return COLORS[index];
    }

    // Mesajı formatlayan metod (renkli ve sıfırlamalı)
    private String formatMessage(String message) {
        return clientColor + message + RESET_COLOR;
    }

    // Sohbet geçmişini temizleme metodu
    private void clearChatHistory() {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("chat_history.txt"))) {
            fileWriter.write(""); // Dosyayı temizler
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Spam kontrolü yapan metod
    private boolean isSpam(String username) {
        // Kullanıcı mesaj sayacı alınır veya oluşturulur
        MessageCounter counter = messageCounters.getOrDefault(username, new MessageCounter());
        long currentTime = System.currentTimeMillis();

        // Zaman kontrolü yapılır, eğer zaman penceresi dolmuşsa sıfırlanır
        if (currentTime - counter.getStartTime() > TIME_WINDOW_MS) {
            counter.reset(currentTime);
        }

        // Mesaj limitine ulaşıp ulaşmadığı kontrol edilir
        boolean isSpam = counter.incrementAndCheckLimit(MESSAGE_LIMIT);
        messageCounters.put(username, counter);
        return isSpam;
    }

    // Mesaj sayacını tutan iç sınıf
    private static class MessageCounter {
        private int count; // Mesaj sayısı
        private long startTime; // Zaman başlangıcı

        public MessageCounter() {
            this.count = 0;
            this.startTime = System.currentTimeMillis();
        }

        public long getStartTime() {
            return startTime;
        }

        public void reset(long newStartTime) {
            this.count = 0; // Sayaç sıfırlanır
            this.startTime = newStartTime;
        }

        public boolean incrementAndCheckLimit(int limit) {
            count++; // Mesaj sayısı artırılır
            return count > limit; // Limit aşılmışsa true döner
        }
    }
}
