package com.example.myplanner.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myplanner.R;
import com.example.myplanner.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    EditText kullanici,sifre;
    Button btn_giris,kayitol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        kullanici = findViewById(R.id.edit_kadi);
        sifre = findViewById(R.id.edit_sifre);
        btn_giris = findViewById(R.id.giris);
        kayitol = findViewById(R.id.kayit);

        db = FirebaseFirestore.getInstance();

        btn_giris.setOnClickListener(v -> {
            String usernameInput = kullanici.getText().toString();
            String passwordInput = sifre.getText().toString();

            try {
                db.collection("Users")
                        .whereEqualTo("username", usernameInput)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                User girisYapanKullanici = doc.toObject(User.class);

                                if (girisYapanKullanici != null) {
                                    if (girisYapanKullanici.getPassword().equals(passwordInput)) {
                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        intent.putExtra("kisiAd", girisYapanKullanici.getAd() + " " + girisYapanKullanici.getSoyad());
                                        intent.putExtra("kisiOkul", girisYapanKullanici.getOkulIs());
                                        intent.putExtra("username", girisYapanKullanici.getUsername());

                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Şifre Yanlış!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Kullanıcı Bulunamadı!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                Log.e("LoginError", "Giriş sırasında hata oluştu: " + e.getMessage());
            }
        });
        kayitol.setOnClickListener( v ->{
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}