import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ProfanityFilter {
    // Küfürlü kelimeleri saklayan bir Set
    private static final Set<String> PROFANITY_LIST = new HashSet<>();

    // Statik blok, sınıf yüklendiğinde küfürlü kelimeleri yükler
    static {
        loadProfanityList();
    }

    // Küfürlü kelimeleri dosyadan yükler
    private static void loadProfanityList() {
        try (BufferedReader reader = new BufferedReader(new FileReader("karaliste.txt"))) {
            String line;
            // Dosyadaki her satırı okuyup Set'e ekler
            while ((line = reader.readLine()) != null) {
                PROFANITY_LIST.add(line.trim().toLowerCase()); // Küfürlü kelimeler küçük harfe çevrilir ve boşluklar temizlenir
            }
        } catch (IOException e) {
            // Dosya okuma hatası varsa hata mesajı yazdırılır
            e.printStackTrace();
        }
    }

    // Verilen mesajı küfürlü kelimelerden arındırır
    public static String filterProfanity(String message) {
        // Küfürlü kelimeler listesindeki her kelime için mesajı kontrol eder
        for (String profanity : PROFANITY_LIST) {
            // Küfür tespit edilirse kelimeyi "***" ile değiştirir (case-insensitive arama yapılır)
            message = message.replaceAll("(?i)\\b" + profanity + "\\b", "***");
        }
        // Filtrelenmiş mesaj geri döndürülür
        return message;
    }
}
