package com.example.myplanner.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myplanner.R;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity {

    protected FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void mesajGoster(String mesaj) {
        Toast.makeText(this, mesaj, Toast.LENGTH_SHORT).show();
    }

    public boolean internetVarMi() {
        android.net.ConnectivityManager cm = (android.net.ConnectivityManager) getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public void internetYokEkraniGoster() {
        android.app.Dialog dialog = new android.app.Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_no_internet);
        dialog.setCancelable(false);

        android.widget.Button btnRetry = dialog.findViewById(R.id.btn_retry_internet);
        android.widget.Button btnOffline = dialog.findViewById(R.id.btn_offline_mode);

        btnRetry.setOnClickListener(v -> {
            if (internetVarMi()) {
                dialog.dismiss();
                mesajGoster("Harika! Bağlantı sağlandı.");
            } else {
                mesajGoster("Hala internet yok, pembe ünlem boşuna durmuyor orada!");
            }
        });

        btnOffline.setOnClickListener(v -> {
            dialog.dismiss();
            mesajGoster("Çevrimdışı mod: Sadece kayıtlı planlar görünür.");
        });

        dialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_base);


    }
}