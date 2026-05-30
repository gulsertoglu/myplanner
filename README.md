# dijital_ajanda
#  My Planner - Mobil Kişisel Planlayıcı Uygulaması

My Planner; modern yaşamın yoğun temposunda kullanıcıların zaman yönetimi süreçlerini kolaylaştırmak, günlük planlarını organize etmek ve serbest düşüncelerini mühürlemek amacıyla geliştirilmiş bulut tabanlı bir Android mobil uygulamasıdır. Tasarım dilinde Dark Academia ve Cottagecore estetik disiplinlerinden ilham alan mor-pembe neon bütünlüğü hakimdir.

---

##  Kullanılan Teknolojiler ve Altyapı
- **Programlama Dili:** Java (Nesne Yönelimli Programlama - OOP)
- **Geliştirme Ortamı (IDE):** Android Studio
- **Veritabanı Katmanı:** Google Firebase Firestore (NoSQL Real-time Database)
- **Tercih Motoru:** SharedPreferences (Yerel Hafıza Yönetimi)
- **Arayüz Standartları:** Google Material Design (DrawerLayout, NavigationView, CardView, FloatingActionButton)

---

##  Öne Çıkan Özellikler
- **Dinamik Zaman Akışı (Takvim):** Seçilen güne ait zaman damgalarını asenkron hesaplayarak sadece o tarihe ait planları Firestore'dan süzen sorgu motoru.
- **Günlük Düşünceler (Notlar):** Kullanıcının zihninden geçenleri mühürlediği ve açılışta eski verileri anlık geri yükleyen kişisel alan.
- **Geçmişin İzleri (Çöp Kutusu):** Silinen planların sığındığı, edebi ve felsefi diyalog pencereleriyle kalıcı imha veya canlandırma sunan modül.
- **Dinamik Öncelik Sıralaması:** Planların önem derecelerine (kırmızı, sarı, yeşil) göre arayüzde otomatik öncelik sıralaması yapan Java koleksiyon algoritması.
- **Ortak Navigasyon Yönetimi:** Tüm pencerelerin ortak bir üst sınıftan (`BaseActivity`) türetilerek kod tekrarının (DRY) engellendiği kalıtım mimarisi.

---

##  Kurulum ve Çalıştırma Talimatları

### 1. Ön Gereksinimler
- Bilgisayarınızda **Android Studio** (Dolphin veya üzeri güncel bir sürüm) kurulu olmalıdır.
- Cihazda veya emülatörde aktif bir internet bağlantısı bulunmalıdır (Firebase Firestore senkronizasyonu için).

### 2. Projenin Çalıştırılması
1. Bu proje klasörünü bilgisayarınıza indirin.
2. Android Studio uygulamasını açın ve **"Open an Existing Project"** seçeneği ile bu klasörü seçin.
3. Gradle senkronizasyonunun ve bağımlılıkların (`Google Services`) yüklenmesini bekleyin.
4. Üst barda yer alan **"Run App" (Yeşil Oynat Butonu)** vasıtasıyla projeyi bir emülatörde (Pixel vb.) veya gerçek bir Android cihazda başlatın.

---

##  Proje Sahibi Bilgileri
- **Adı Soyadı:** Ümmügülsüm Sertoğlu  
- **Okul / Bölüm:** Marmara Üniversitesi - Bilgisayar Programcılığı  
- **Proje Türü:** Nesne Yönelimli Programlama ve Mobil Programlama Dersi Final Projesi  
- **Geliştirme Yılı:** © 2026 gulsum.visible
