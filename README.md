**Gerçek Zamanlı Java Sohbet Uygulaması**

Bu proje, Java ve soket programlaması kullanılarak geliştirilmiş bir gerçek zamanlı sohbet uygulamasıdır. Uygulama, bir sunucuya bağlı birden fazla kullanıcının aynı anda mesaj gönderip almasına olanak tanır. Kullanıcılar hem grup sohbeti yapabilir hem de belirli kullanıcılara özel mesajlar gönderebilirler. Uygulama, kullanıcı deneyimini iyileştirmek amacıyla mesaj zaman damgaları, küfür filtresi, spam tespiti ve mesaj kaydı gibi çeşitli özelliklerle donatılmıştır.

**Amaç**

Bu projenin amacı, soket programlaması ve çoklu kullanıcı desteği ile çalışan bir sohbet uygulaması geliştirmektir. Bu uygulama, hem ağ programlama temellerini öğrenmek isteyenler için hem de daha geniş kapsamlı sohbet sistemleri oluşturmak isteyenler için örnek bir yapı sunmaktadır.

**Teknolojiler**

Java: Uygulamanın ana programlama dili.

Socket Programlama: Sunucu ve istemci arasındaki iletişimi sağlamak için kullanılır.

Java I/O: Mesaj kaydı ve kullanıcıların mesajları göndermesi/alması için veri giriş/çıkışı sağlanır.

**Mimarisi**

Sunucu: Sunucu tarafı, istemcilerin bağlantı taleplerini kabul eder ve istemciler arasında mesaj alışverişini sağlar. Sunucu tüm istemcilerle sürekli bağlantıda kalarak, her bir istemciden gelen mesajları ilgili kişilere iletir.

İstemci: Her istemci, sunucuya bağlanır ve diğer istemcilerle etkileşime geçebilir. Kullanıcılar, grup sohbeti içinde mesaj gönderip alabilir veya /w komutu ile özel mesajlar gönderebilir.

**Özellikler**

1. Gerçek Zamanlı Grup Sohbeti
Açıklama: Kullanıcılar, sunucuya bağlandıktan sonra gerçek zamanlı olarak mesaj gönderip alabilirler. Tüm kullanıcıların bulunduğu genel bir sohbet odası vardır.
Nasıl Çalışır?: Kullanıcı tarafından gönderilen her mesaj, sunucu üzerinden tüm aktif kullanıcılara iletilir.

2. Özel Mesajlaşma
Açıklama: Kullanıcılar, diğer kullanıcılara özel mesaj gönderebilirler. Bu özellik, genel sohbette görünmemesini istediğiniz mesajlar için kullanılır.
Kullanımı: /w [kullanıcıadı] [mesaj] komutuyla belirli bir kullanıcıya özel mesaj gönderilir.

3. Kullanıcı Listesi Görüntüleme
Açıklama: Sohbetteki tüm aktif kullanıcıları listeleme imkanı sunar.
Kullanımı: /listele komutunu kullanarak, o an bağlı olan tüm kullanıcıları görebilirsiniz.

4. Mesaj Zaman Damgaları
Açıklama: Gönderilen her mesaj, mesajın ne zaman gönderildiğini belirten bir zaman damgası ile birlikte gelir. Bu sayede, mesajların hangi zaman aralığında gönderildiği izlenebilir.
Özellik: Sunucu her mesajın zamanını otomatik olarak ekler ve istemci tarafında kullanıcıya görüntülenir.

5. Küfür Filtresi
Açıklama: Sohbette saldırgan veya uygunsuz dil kullanımını engellemek amacıyla küfür filtresi uygulanmıştır. Küfür içeren kelimeler, tanımlanmış bir kelime listesine göre otomatik olarak filtrelenir.
Kaynak: Filtreleme, https://github.com/ooguz/turkce-kufur-karaliste kaynağındaki veri tabanına dayanmaktadır. Bu listeyi düzenleyerek filtrelemenin hassasiyetini artırabilirsiniz.

6. Spam Tespiti
Açıklama: Kullanıcıların belirli bir süre içerisinde çok sayıda mesaj göndermesi engellenir. Bu sayede spam niteliğindeki mesajların önüne geçilir.
Nasıl Çalışır?: Belirli bir eşik değer belirlenir. Bu eşik aşıldığında kullanıcıya uyarı mesajı gösterilir ve mesajları gönderemez hale gelir.

7. Mesaj Kaydı
Açıklama: Tüm sohbet oturumu boyunca gönderilen mesajlar, oturum bazlı olarak bir dosyaya kaydedilir. Bu özellik, ileride mesaj geçmişini incelemek veya sorunları tespit etmek için kullanılabilir.
Kaydetme Yeri: Mesajlar, sunucu tarafında belirlenen bir dosyaya kayıt edilir. Her oturum için ayrı bir kayıt dosyası oluşturulur.

![image](https://github.com/user-attachments/assets/24289893-0d43-44df-9b39-afe38898c88a)

[ Genel mesaj özelliklerinin ve sansür özelliğinin uygulanması  ]

![image](https://github.com/user-attachments/assets/ee14f13f-c0e2-4fb3-a588-663e5260860f)

[ Özel mesaj gönderme ve kullanıcı listeleme özelliğinin kullanımı ]

![image](https://github.com/user-attachments/assets/7cc2ef0e-0baf-45ff-9715-8e8fb95b4914)

[ Spam tespiti yapılması ve kısıtlama uygulanması ]






