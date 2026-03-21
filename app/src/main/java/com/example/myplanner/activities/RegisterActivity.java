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
            // 1. Kriter: Exception Handling (İnternet Kontrolü)
            if (!internetVarMi()) {
                internetYokEkraniGoster(); // Pembe ünlemli ekran
                return;
            }

            // Boş alan kontrolü (Kullanıcı deneyimi için önemli)
            if (kadi.getText().toString().isEmpty() || sifre.getText().toString().isEmpty()) {
                mesajGoster("Kullanıcı adı ve şifre boş bırakılamaz bebüş!");
                return;
            }

            // 2. Kriter: Model Kullanımı (User nesnesi oluşturma)
            // Not: User constructor'ındaki parametre sırasına dikkat et kanka!
            User yeniUser = new User(
                    ad.getText().toString(),
                    soyad.getText().toString(),
                    email.getText().toString(),
                    okul.getText().toString(),
                    sifre.getText().toString(),
                    kadi.getText().toString());

            AuthManager authManager = new AuthManager();

            // 3. Kriter: Asenkron Veri Yazma (Firebase Kayıt)
            authManager.kullaniciKaydet(yeniUser, new AuthCallback() {
                @Override
                public void onSuccess(String mesaj) {
                    bilgi.setText("Kayıt Başarılı!");
                    bilgi.setVisibility(View.VISIBLE);
                    giris.setVisibility(View.VISIBLE); // Giriş butonunu göster
                    mesajGoster(mesaj);
                }

                @Override
                public void onFailure(String hata) {
                    // Kriter: Toast Message kullanımı
                    Toast.makeText(RegisterActivity.this, "Hata: " + hata, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Giriş Ekranına Dön Butonu
        giris.setOnClickListener(v -> {
            finish(); // Bu aktiviteyi kapatır ve bir önceki (Login) ekrana döner
        });
    }
}