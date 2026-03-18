package com.example.myplanner.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myplanner.R;
import com.example.myplanner.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText kadi,sifre,ad,soyad,email,okul;
    Button giris,kayit;
    TextView bilgi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        kadi=findViewById(R.id.edit_kadi);
        sifre=findViewById(R.id.edit_sifre);
        ad=findViewById(R.id.edit_ad);
        soyad=findViewById(R.id.edit_soyad);
        email=findViewById(R.id.edit_email);
        okul=findViewById(R.id.edit_okulis);
        kayit=findViewById(R.id.btn_kayitol);
        giris=findViewById(R.id.btn_giris);
        bilgi=findViewById(R.id.text_bilgi);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Kaydet butonuna basıldığında...
        kayit.setOnClickListener(v -> {
            // 1. Kutulardaki verileri al
            String username = kadi.getText().toString();
            String password = sifre.getText().toString();
            String name = ad.getText().toString();
            String surname = soyad.getText().toString();
            String mail = email.getText().toString();
            String school = okul.getText().toString();


            User yeniKullanici = new User(name, surname, mail, school, password, username);

            db.collection("Users")
                    .document(username)
                    .set(yeniKullanici)
                    .addOnSuccessListener(aVoid -> {
                        bilgi.setVisibility(View.VISIBLE);
                        bilgi.setText("Kayıt Başarılı, " + yeniKullanici.getAd() + "!");
                        giris.setVisibility(View.VISIBLE);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        giris.setOnClickListener(v -> {
            finish();
        });
    }
}