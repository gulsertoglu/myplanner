package com.example.myplanner.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myplanner.R;
import com.example.myplanner.models.AuthCallback;
import com.example.myplanner.models.AuthManager;
import com.example.myplanner.models.SessionManager;
import com.example.myplanner.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends BaseActivity {
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

        SessionManager sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("username", sessionManager.getUsername());
            startActivity(intent);
            finish();
        }

        btn_giris.setOnClickListener(v -> {
            if (!internetVarMi()) {
                internetYokEkraniGoster();
                return;
            }

            AuthManager authManager = new AuthManager();
                String username = kullanici.getText().toString();
                String password = sifre.getText().toString();

                authManager.kullaniciGiris(username, password, new AuthCallback() {
                    @Override
                    public void onSuccess(String mesaj) {
                        SessionManager sessionManager = new SessionManager(MainActivity.this);
                        sessionManager.createLoginSession(username);

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(String hata) {
                        Toast.makeText(MainActivity.this, hata, Toast.LENGTH_SHORT).show();
                    }
                });
        });
        kayitol.setOnClickListener( v ->{
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}