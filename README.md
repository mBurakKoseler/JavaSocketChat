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


![image](https://github.com/user-attachments/assets/24289893-0d43-44df-9b39-afe38898c88a)

[ Genel mesaj özelliklerinin ve sansür özelliğinin uygulanması  ]

![image](https://github.com/user-attachments/assets/ee14f13f-c0e2-4fb3-a588-663e5260860f)

[ Özel mesaj gönderme ve kullanıcı listeleme özelliğinin kullanımı ]

![image](https://github.com/user-attachments/assets/7cc2ef0e-0baf-45ff-9715-8e8fb95b4914)

[ Spam tespiti yapılması ve kısıtlama uygulanması ]






