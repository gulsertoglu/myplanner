package com.example.myplanner.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myplanner.R;
import com.example.myplanner.adapters.PlanAdapter;
import com.example.myplanner.models.Plan;
import com.example.myplanner.models.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

// OOP KRİTERİ: BaseActivity'den türeterek kalıtımı ve ortak menü yeteneklerini koruyoruz Gülsüm!
public class TrashActivity extends BaseActivity {

    private RecyclerView rvTrash;
    private PlanAdapter adapter;
    private List<Plan> silinenPlanListesi = new ArrayList<>();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash); // Tasarım dosyamızı bağlıyoruz

        sessionManager = new SessionManager(this);

        // 🎯 ORTAK MENÜ BAĞLANTISI: XML'e eklediğimiz ID'lerle menü elemanlarını tek seferde nizami buluyoruz kız
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_trash);
        NavigationView navigationView = findViewById(R.id.nav_view_trash);
        ImageButton btnOpenMenu = findViewById(R.id.btn_open_menu_trash);

        // 🔥 TEK SATIRLIK SİHİR: BaseActivity'deki ortak menü motorunu ateşliyoruz!
        menuyuHazirla(drawerLayout, navigationView, btnOpenMenu);

        rvTrash = findViewById(R.id.rv_trash);
        rvTrash.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PlanAdapter(silinenPlanListesi);
        rvTrash.setAdapter(adapter);

        // İki butonlu sistemin dinleyicileri ortak menü altında da tıkır tıkır çalışıyor kanka
        if (adapter != null) {
            // 1. Çöp kutusu butonuna (deletebutton) basıldığında: Kökten silme sorusu sorar
            adapter.setOnPlanSilmeListener(selectedPlan -> {
                copKutusuSecenekleriniGoster(selectedPlan, true);
            });

            // 2. Kartın kendisine veya tik butonuna basıldığında: Geri yükleme sorusu sorar
            adapter.setOnPlanTamamlaListener(selectedPlan -> {
                copKutusuSecenekleriniGoster(selectedPlan, false);
            });
        }

        String aktifKullanici = sessionManager.getUsername();
        if (aktifKullanici != null) {
            silinenPlanlariGetir(aktifKullanici);
        }
    }

    private void silinenPlanlariGetir(String username) {
        db.collection("Planlar")
                .whereEqualTo("sahibi", username)
                .whereEqualTo("silindi", true) // Sadece silinenleri getiriyoruz kız
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        hataMesajiGoster("Çöp Kutusu Listeleme", error);
                        return;
                    }
                    silinenPlanListesi.clear();
                    if (value != null) {
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Plan plan = doc.toObject(Plan.class);
                            if (plan != null) {
                                plan.setId(doc.getId());
                                silinenPlanListesi.add(plan);
                            }
                        }
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    // ÇÖP KUTUSU DİYALOG MOTORU: Orijinal edebi ve teknik yapısı milimetrik korundu kız!
    private void copKutusuSecenekleriniGoster(Plan plan, boolean koktenSilMe) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Geçmişin İzleri");

        if (koktenSilMe) {
            builder.setMessage("Bu planı evrenden tamamen silmek istediğinden emin misin? Geri dönüşü yok!");
            builder.setPositiveButton("Evet, Kökten Sil 💥", (dialog, which) -> {
                try {
                    db.collection("Planlar").document(plan.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> mesajGoster("Plan sonsuzluğa uğurlandı! 🕊️"))
                            .addOnFailureListener(e -> hataMesajiGoster("Kökten Silme", e));
                } catch (Exception e) {
                    hataMesajiGoster("Sistem İmha Motoru", e);
                }
            });
        } else {
            builder.setMessage("Bu planı ana ekrana geri çağırmak ister misin?");
            builder.setPositiveButton("Evet, Geri Yükle 🌟", (dialog, which) -> {
                try {
                    db.collection("Planlar").document(plan.getId())
                            .update("silindi", false)
                            .addOnSuccessListener(aVoid -> mesajGoster("Plan başarıyla canlandırıldı! 🎉"))
                            .addOnFailureListener(e -> hataMesajiGoster("Geri Yükleme", e));
                } catch (Exception e) {
                    hataMesajiGoster("Sistem Kurtarma Motoru", e);
                }
            });
        }

        builder.setNegativeButton("Vazgeç", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}