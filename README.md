# 💬 Java Socket Chat Application

**Gerçek zamanlı, çok kullanıcılı bir sohbet uygulaması**

Bu proje, Java ve soket programlaması kullanılarak geliştirilmiş, birden fazla istemcinin aynı anda bağlanarak mesajlaşabildiği bir **sunucu-tabanlı sohbet sistemi** sunar. Uygulama hem grup sohbeti hem de özel mesajlaşmayı destekler ve kullanıcı deneyimini artırmak için çeşitli güvenlik ve denetim özellikleri içerir.

---

## 🚀 Özellikler

- ✅ Gerçek zamanlı mesajlaşma  
- ✅ Grup sohbeti ve özel mesaj desteği (`/w kullanıcı mesaj`)  
- ✅ Spam tespiti ve küfür filtresi  
- ✅ Mesaj geçmişi kaydı (`chat_history.txt`)  
- ✅ Kara liste kontrolü (`karaliste.txt`)  
- ✅ Zaman damgalı mesajlar  
- ✅ Basit ve anlaşılır istemci-sunucu mimarisi  

---

## 🧱 Kullanılan Teknolojiler

- **Java** – Temel programlama dili  
- **Socket Programlama (TCP)** – Sunucu/istemci bağlantısı için  
- **Java I/O** – Dosya okuma/yazma, mesaj loglama  
- **Çoklu Threading** – Aynı anda birden fazla kullanıcı desteği  

---

## 🧠 Mimarisi

```text
+----------+       +----------+       +----------+
| Client 1 | <---> |  Server  | <---> | Client N |
+----------+       +----------+       +----------+
```

- `Server.java`: İstemcileri dinler, bağlantı kabul eder ve mesajları yönlendirir.  
- `ClientHandler.java`: Her bir istemci için ayrı bir iş parçacığı (thread) oluşturur.  
- `Client.java`: Kullanıcı arayüzü; sunucuya bağlanır, mesaj gönderir/alır.  

---

## ▶️ Nasıl Çalıştırılır?

1. Proje klasörüne gidin ve `.java` dosyalarını derleyin:

```bash
javac Server.java
javac Client.java
```

2. Sunucuyu başlatın:

```bash
java Server
```

3. Ardından her istemci için ayrı bir terminalde şunu çalıştırın:

```bash
java Client
```

> Not: Her istemci çalıştığında sunucuya bağlanır ve diğer kullanıcılarla sohbet edebilir.

---

## 📄 Dosya Yapısı

```
JavaSocketChat/
├── Server.java             # Sunucu kodu
├── Client.java             # İstemci kodu
├── ClientHandler.java      # Sunucu tarafında istemcileri yöneten sınıf
├── chat_history.txt        # Mesaj geçmişi
├── karaliste.txt           # Kara liste (küfür filtresi)
├── .gitignore
└── README.md               # Bu dosya
```

---

## 📦 Özelleştirme

- `karaliste.txt`: Küfür filtresine yeni kelimeler ekleyebilirsiniz.  
- `chat_history.txt`: Tüm mesaj geçmişi burada saklanır.  
- GUI arayüzü istenirse JavaFX veya Swing ile genişletilebilir.  

---

## 📜 Lisans

Bu proje açık kaynaklıdır ve MIT Lisansı ile lisanslanmıştır.

---

🔌 **Gerçek zamanlı iletişim, Java ile mümkün!**



![image](https://github.com/user-attachments/assets/24289893-0d43-44df-9b39-afe38898c88a)

[ Genel mesaj özelliklerinin ve sansür özelliğinin uygulanması  ]

![image](https://github.com/user-attachments/assets/ee14f13f-c0e2-4fb3-a588-663e5260860f)

[ Özel mesaj gönderme ve kullanıcı listeleme özelliğinin kullanımı ]







