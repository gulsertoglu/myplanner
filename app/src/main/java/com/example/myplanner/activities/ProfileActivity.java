package com.example.myplanner.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myplanner.R;
import com.example.myplanner.models.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

// OOP KRİTERİ: BaseActivity'den türeterek kalıtımı ve ortak menü yeteneklerini GERÇEKTEN miras alıyoruz kız!
public class ProfileActivity extends BaseActivity {

    private TextView tvName, tvEmail, tvOkul;
    private FirebaseFirestore db;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Yeni tasarımla birebir eşledik kız

        //XML'e eklediğimiz ID'lerle menüyü buluyoruz
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_profile);
        NavigationView navigationView = findViewById(R.id.nav_view_profile);
        ImageButton btnOpenMenu = findViewById(R.id.btn_open_menu_profile);

        //BaseActivity'deki o muazzam ortak menü motorunu ateşliyoruz!
        menuyuHazirla(drawerLayout, navigationView, btnOpenMenu);

        // XML bileşenlerini bağlıyoruz
        tvName = findViewById(R.id.tv_profile_full_name);
        tvOkul = findViewById(R.id.tv_profile_okul);
        tvEmail = findViewById(R.id.tv_profile_email);

        db = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(this);

        String currentUsername = sessionManager.getUsername();

        if (currentUsername != null) {
            loadUserProfile(currentUsername);
        } else {
            mesajGoster("Oturum bilgisi bulunamadı!");
        }
    }

    // Orijinal canavar gibi işleyen Firebase profil yükleme metodun aynen korunuyor kanka!
    private void loadUserProfile(String username) {
        android.util.Log.d("PROFIL_TEST", "Aranan Kullanıcı Adı: " + username);

        db.collection("Users").document(username)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        android.util.Log.d("PROFIL_TEST", "Döküman Bulundu! Veriler: " + documentSnapshot.getData());

                        // Firebase'deki isimleri buradakilerle birebir eşle
                        String ad = documentSnapshot.getString("ad");
                        String soyad = documentSnapshot.getString("soyad");
                        String email = documentSnapshot.getString("email");
                        String okul = documentSnapshot.getString("okulIs");

                        String tamAd = (ad != null ? ad : "") + " " + (soyad != null ? soyad : "");

                        // UI'ya basarken hata olup olmadığını anlamak için:
                        if(tvName != null) tvName.setText(tamAd.trim().isEmpty() ? username : tamAd);
                        if(tvEmail != null) tvEmail.setText(email);
                        if(tvOkul != null) tvOkul.setText(okul);

                    } else {
                        android.util.Log.e("PROFIL_TEST", "Döküman bulunamadı! Veritabanında '" + username + "' isimli bir döküman yok.");
                        mesajGoster("Veritabanında kayıt yok: " + username);
                    }
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("PROFIL_TEST", "Firebase Hatası: " + e.getMessage());
                    hataMesajiGoster("Profil Yükleme", e);
                });
    }
}