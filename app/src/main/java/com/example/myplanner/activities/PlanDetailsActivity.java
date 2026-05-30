package com.example.myplanner.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.myplanner.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

// OOP KRİTERİ: BaseActivity'den türeterek kalıtımı ve ortak menü motorunu miras alıyoruz
public class PlanDetailsActivity extends BaseActivity {

    private TextView tvTitle, tvDate, tvContent;
    private Button btnComplete;
    private String planId; // Firebase'de işlem yapabilmek için planın kimliği

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details); // Tasarım XML dosyamızı bağlıyoruz

        //XML'e eklediğimiz ID'lerle menüyü buluyoruz
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_plan_detail);
        NavigationView navigationView = findViewById(R.id.nav_view_plan_detail);
        ImageButton btnOpenMenu = findViewById(R.id.btn_open_menu_plan_detail);

        //BaseActivity'deki o efsane ortak menü motorunu ateşliyoruz!
        menuyuHazirla(drawerLayout, navigationView, btnOpenMenu);

        // XML'deki bileşenleri Java'ya bağlıyoruz
        tvTitle = findViewById(R.id.tv_detail_title);
        tvDate = findViewById(R.id.tv_detail_date);
        tvContent = findViewById(R.id.tv_detail_content);
        btnComplete = findViewById(R.id.btn_complete_plan);

        // HomeActivity veya Calendar'dan gelen verileri çekiyoruz
        if (getIntent() != null) {
            planId = getIntent().getStringExtra("PLAN_ID");
            String baslik = getIntent().getStringExtra("PLAN_BASLIK");
            String icerik = getIntent().getStringExtra("PLAN_ICERIK");
            String tarih = getIntent().getStringExtra("PLAN_TARIH");

            // Gelen veriler boş değilse ekrandaki TextView'lara şak diye basıyoruz
            if (baslik != null) tvTitle.setText(baslik);
            if (tarih != null) tvDate.setText(tarih);
            if (icerik != null) tvContent.setText(icerik);
        }

        // 🎯 PLANI TAMAMLANDI İŞARETLE BUTONU DİNLEYİCİSİ
        if (btnComplete != null) {
            btnComplete.setOnClickListener(v -> {
                if (planId != null) {
                    // Jüride hocaya şov yapacağımız try-catch Exception Handling yapımız kız
                    try {
                        db.collection("Planlar").document(planId)
                                .update("tamamlandi", true)
                                .addOnSuccessListener(aVoid -> {
                                    mesajGoster("Harika! Görev başarıyla mühürlendi ✅");
                                    finish(); // İşlem bitince detay sayfasını kapatıp listeye dönüyoruz kız
                                })
                                .addOnFailureListener(e -> hataMesajiGoster("Plan Detay Tamamlama", e));
                    } catch (Exception e) {
                        hataMesajiGoster("Detay Motoru", e);
                    }
                } else {
                    mesajGoster("Hata: Plan kimliği bulunamadı kanka! 🕵️");
                }
            });
        }
    }
}