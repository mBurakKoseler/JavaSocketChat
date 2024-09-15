import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    // İstemci tarafında kullanılan soket, okuyucu ve yazıcı nesneleri
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username; // Kullanıcının adı

    // Yapıcı metod, soket ve kullanıcı adı ile istemci oluşturulur
    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            // Sunucuya mesaj göndermek için BufferedWriter
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // Sunucudan gelen mesajları almak için BufferedReader
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            // Hata oluştuğunda tüm kaynaklar kapatılır
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // İstemciden sunucuya mesaj gönderme metodu
    public void sendMessage() {
        try {
            // İlk olarak kullanıcı adını sunucuya gönder
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            // Kullanıcıdan mesaj girişi almak için Scanner
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();

                // Terminalde geçici olarak mesajı gösterme
                System.out.print("\033[1A"); // Satırı yukarı hareket ettir
                System.out.print("\033[K");  // Satırı temizle

                // Mesaj türüne göre işlem yap
                if (messageToSend.startsWith("/w ")) {
                    // Özel mesaj ise direkt sunucuya gönder
                    bufferedWriter.write(messageToSend);
                } else if (messageToSend.equals("/listele")) {
                    // Kullanıcı listesi komutu gönderiliyor
                    bufferedWriter.write(messageToSend);
                } else {
                    // Normal mesaj ise kullanıcı adı ile birlikte gönder
                    bufferedWriter.write(username + ": " + messageToSend);
                }
                bufferedWriter.newLine();
                bufferedWriter.flush(); // Yazılanları hemen gönder
            }
        } catch (IOException e) {
            // Hata oluşursa kaynaklar kapatılır
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // Sunucudan gelen mesajları dinleme metodu
    public void ListenForMessage() {
        // Yeni bir thread başlatılarak mesajlar dinlenir
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                // Soket bağlantısı açık olduğu sürece mesaj dinle
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine(); // Sunucudan gelen mesaj
                        System.out.println(msgFromGroupChat); // Zaman damgası ile birlikte mesaj yazdırılır
                    } catch (IOException e) {
                        // Hata oluşursa kaynaklar kapatılır
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start(); // Thread başlatılır
    }

    // Soket, okuyucu ve yazıcıyı kapatma metodu
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close(); // Okuyucu kapatılır
            }
            if (bufferedWriter != null) {
                bufferedWriter.close(); // Yazıcı kapatılır
            }
            if (socket != null) {
                socket.close(); // Soket kapatılır
            }
        } catch (IOException e) {
            e.printStackTrace(); // Hata detaylarını yazdır
        }
    }

    // Ana metod, programın başlangıç noktası
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Kullanıcıdan grup sohbeti için kullanıcı adı alınır
        System.out.println("Grup sohbeti için kullanıcı adı giriniz: ");
        String username = scanner.nextLine();

        // Sunucuya bağlanmak için soket oluşturulur
        Socket socket = new Socket("localhost", 1234);

        // İstemci oluşturulur ve kullanıcı adı ile sunucuya bağlanılır
        Client client = new Client(socket, username);

        // Sunucudan gelen mesajları dinle
        client.ListenForMessage();
        // Sunucuya mesaj gönder
        client.sendMessage();
    }
}
