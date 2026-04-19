package com.example.myplanner.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.example.myplanner.R;
import com.example.myplanner.models.AuthCallback;
import com.example.myplanner.models.AuthManager;
import com.example.myplanner.models.User;

public class RegisterActivity extends BaseActivity {
    // Kriter: Encapsulation (Değişken tanımlamaları)
    EditText kadi, sifre, ad, soyad, email, okul;
    Button giris, kayit;
    TextView bilgi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // XML Bağlantıları
        kadi = findViewById(R.id.edit_kadi);
        sifre = findViewById(R.id.edit_sifre);
        ad = findViewById(R.id.edit_ad);
        soyad = findViewById(R.id.edit_soyad);
        email = findViewById(R.id.edit_email);
        okul = findViewById(R.id.edit_okulis);
        kayit = findViewById(R.id.btn_kayitol);
        giris = findViewById(R.id.btn_giris);
        bilgi = findViewById(R.id.text_bilgi);

        // Kayıt Ol Butonu
        kayit.setOnClickListener(v -> {
            // İnternet Kontrolü
            if (!internetVarMi()) {
                internetYokEkraniGoster();
                return;
            }

            // Değerleri String'e çevirip alalım
            String strKadi = kadi.getText().toString().trim();
            String strSifre = sifre.getText().toString().trim();
            String strAd = ad.getText().toString().trim();
            String strSoyad = soyad.getText().toString().trim();
            String strEmail = email.getText().toString().trim();
            String strOkul = okul.getText().toString().trim();

            // Boş alan kontrolü
            if (strKadi.isEmpty() || strSifre.isEmpty() || strAd.isEmpty() || strEmail.isEmpty()) {
                mesajGoster("Gerekli alanları boş bırakamazsın bebüş!");
                return;
            }

            // 2. Kriter: Model Kullanımı
            // User nesnesi oluştururken kadi'yi de içine veriyoruz
            User yeniUser = new User(strAd, strSoyad, strEmail, strOkul, strSifre, strKadi);

            AuthManager authManager = new AuthManager();

            // 🚀 KRİTİK DEĞİŞİKLİK:
            // authManager içindeki kullaniciKaydet metodu arka planda artık
            // .add() yerine .document(yeniUser.getKadi()).set() kullanmalı!
            authManager.kullaniciKaydet(yeniUser, new AuthCallback() {
                @Override
                public void onSuccess(String mesaj) {
                    bilgi.setText("Kayıt Başarılı! Hoş geldin " + strKadi);
                    bilgi.setVisibility(View.VISIBLE);
                    giris.setVisibility(View.VISIBLE);
                    mesajGoster(mesaj);

                }

                @Override
                public void onFailure(String hata) {
                    Toast.makeText(RegisterActivity.this, "Kayıt Hatası: " + hata, Toast.LENGTH_SHORT).show();
                }
            });
        });

        giris.setOnClickListener(v -> finish());
    }
}