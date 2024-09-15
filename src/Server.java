import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // Sunucu için ServerSocket tanımlanıyor. Bu soket istemcilerden gelen bağlantıları dinleyecek.
    private ServerSocket serverSocket;

    // Yapıcı metot, ServerSocket nesnesini parametre olarak alıyor.
    public Server(ServerSocket serverSocket) {
        // Parametre olarak gelen serverSocket, sınıfın serverSocket değişkenine atanıyor.
        this.serverSocket = serverSocket;
    }

    // Sunucuyu başlatan metod.
    public void startServer() {
        try {
            // Sunucu soketi kapatılmadığı sürece bağlantıları kabul etmeye devam edecek.
            while (!serverSocket.isClosed()) {
                // Yeni bir istemci bağlantısı kabul ediliyor. Bu işlem bloklayıcıdır; bir bağlantı gelene kadar bekler.
                Socket socket = serverSocket.accept();
                // Yeni bir istemci bağlandığında ekrana bir mesaj yazdırılıyor.
                System.out.println("Yeni bir kullanıcı sohbete katıldı!");

                // İstemci ile sunucu arasındaki iletişimi yönetecek ClientHandler nesnesi oluşturuluyor.
                ClientHandler clientHandler = new ClientHandler(socket);

                // Her istemci için ayrı bir iş parçacığı (thread) başlatılıyor. Böylece birden fazla istemci aynı anda bağlanabilir.
                Thread thread = new Thread(clientHandler);
                thread.start(); // İş parçacığı başlatılıyor.
            }
        } catch (IOException e) {
            // IO hatası durumunda bu blok çalışacak.
            e.printStackTrace();
        }
    }

    // Sunucu soketini kapatan metod.
    public void closeServerSocket() {
        try {
            // Eğer sunucu soketi null değilse, kapatılıyor.
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            // Sunucu soketi kapatılırken bir hata oluşursa yakalanır ve hata mesajı ekrana yazdırılır.
            e.printStackTrace();
        }
    }

    // Ana metod, programın başlangıç noktası.
    public static void main(String[] args) throws IOException {
        // 1234 numaralı portta yeni bir ServerSocket oluşturuluyor.
        ServerSocket serverSocket = new ServerSocket(1234);
        // Server sınıfından yeni bir nesne oluşturuluyor ve serverSocket parametre olarak gönderiliyor.
        Server server = new Server(serverSocket);
        // Sunucu başlatılıyor.
        server.startServer();
    }
}
