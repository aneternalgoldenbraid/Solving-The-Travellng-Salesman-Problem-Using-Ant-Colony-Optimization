# Karınca Kolonisi Optimizasyonu ile Gezgin Satıcı Problemi Çözümü

Çalışma kapsamında hazırlanan KarincaAdam-2.0aGUI programı, Java programlama dili kullanılarak NetBeans IDE üzerinde geliştirilmiştir. Uygulamanın kaynak kodu ve export edilmiş zip dosyası Code klasöründe yer almaktadır. NetBeans üzerinde  File > Import Project > From ZIP yolu takip edilerek import edilebilir.

Uygulamanın çalıştırabilir hali olan jar uzantılı dosyanın çalıştırılabilmesi için bilgisayaranızda Java Development Kit (JDK) veya Java Runtime Environment (JRE)’in kurulu olması gerekmektedir. Unix temelli MacOs işletim sistemi üzerinde jar dosyasına çift tıklandığında uygulamanın Arayüz içeren versiyonu çalışacaktır. Aynı şekilde JRE kurulu Windows işletim sistemi üzerinde de uygulamanın çift tıklanarak çalıştırılabildiği test edilmiştir. 

Uygulama arayüzü haricinde Terminal üzerinde (Windows temelli bilgisayarlarda DOS) de çalıştırılmak üzere tasarlanmıştır. Terminalde, cd komutu ile jar dosyasının bulunduğu klasöre girdikten sonra, java -jar KarincaAdam-2.0aGUI.jar berlin52.tsp gibi bir kod girilirse 52 şehirden oluşan harita bilgisini içeren berlin52.tsp dosyası uygulama üzerinde çalıştırılacak ve sonuçları yine terminal üzerinde listelenecektir. (Bu noktada, tsp dosyasının jar uzantılı uygulama ile aynı klasörde bulunması gerekir.) java -jar KarincaAdam-1.9aGUI.jar şeklinde bir kod girilirse uygulamanın arayüz içeren hali çalıştırılacaktır. 

Uygulamada giriş dosyası olarak Heidelberg Üniversitesi'ndeki Ayrık ve Kombinatoryal Optimizasyon grubunun, TSPLIB kütüphanesindeki TSP dosyaları kullanılmaktadır. TSPLIB'de, bazı gerçek ülke ve şehir haritaları bulunduğu gibi rassal üretilmiş haritalar ve elektronik devrelerde kullanılmak üzere geliştirilen delikli kartlar da yer almaktadır. Dosya ismi üzerindeki sayı, haritanın kaç düğüm içerdiğini temsil etmektedir. Örneğin turkiye81.tsp dosyası, Türkiye'deki 81 ilin bilgisini tutan haritadır. Uygulamanın test edilme aşamasında sık kullanılan tsp dosyaları Database klasöründe yer almaktadır.

KarincaAdam-2.0aGUI programının minimum sistem gereksinimlerini tespit edebilmek için JProfiler 9.2.1 ve VisualVM 1.3.9 analiz programları kullanılmıştır. JProfiler, performans darboğazlarının çözümlenmesine, bellek sızıntılarının azaltılmasına ve iş parçacığı sorunlarının anlamlandırılmasına yardımcı olan bir test aracıdır. Yine aynı şekilde VisualVM ise Java Sanal Makinesi üzerinde çalışırken, Java tabanlı uygulamalar hakkında ayrıntılı bilgi edinmek ve performans analizi yapmak için kullanılan, görsel bir arayüz sağlayan bir araçtır. 
