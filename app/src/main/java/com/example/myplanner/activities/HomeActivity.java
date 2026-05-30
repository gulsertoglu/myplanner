package com.example.myplanner.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplanner.R;
import com.example.myplanner.adapters.PlanAdapter;
import com.example.myplanner.models.Plan;
import com.example.myplanner.models.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends BaseActivity implements IPlanSirala {

    private RecyclerView rvPlans;
    private PlanAdapter adapter;
    private List<Plan> planListesi = new ArrayList<>();
    private SessionManager sessionManager;

    private String secilenRenkStr = "#4CAF50";
    private FirebaseFirestore db;
    private Calendar dialogTakvim = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(this);
        rvPlans = findViewById(R.id.rv_plans);
        rvPlans.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PlanAdapter(planListesi);
        rvPlans.setAdapter(adapter);

        ImageButton btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> cikisYap());

        // Tasarımdaki menü elemanlarını tek seferde nizami buluyoruz
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ImageButton btnOpenMenu = findViewById(R.id.btn_open_menu);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // BaseActivity'deki ortak menü motorunu tek satırla ateşliyoruz!
        menuyuHazirla(drawerLayout, navigationView, btnOpenMenu);

        String aktifKullanici = sessionManager.getUsername();
        if (aktifKullanici != null) {
            planlariGetir(aktifKullanici);
        }

        FloatingActionButton fabAdd = findViewById(R.id.fab_add_plan);
        fabAdd.setOnClickListener(v -> planEklemePenceresiniAc());
    }

    private void cikisYap() {
        sessionManager.logoutUser();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void planlariGetir(String username) {
        db.collection("Planlar")
                .whereEqualTo("sahibi", username)
                .whereEqualTo("silindi", false)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        android.util.Log.e("FirebaseHata", error.getMessage());
                        return;
                    }

                    planListesi.clear();
                    if (value != null) {
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Plan plan = doc.toObject(Plan.class);
                            if (plan != null) {
                                plan.setId(doc.getId());
                                planListesi.add(plan);
                            }
                        }
                    }

                    // İKİ BUTONUN DİNLEYİCİLERİ BURADA BAĞLANIYOR
                    if (adapter != null) {
                        // 1. Çöp butonuna basınca sorarak çöpe atar
                        adapter.setOnPlanSilmeListener(selectedPlan -> {
                            planiniCopeGonderSorusu(selectedPlan);
                        });

                        // 2. Tik butonuna basınca sorarak tamamlandı yapar
                        adapter.setOnPlanTamamlaListener(selectedPlan -> {
                            planiniTamamlaSorusu(selectedPlan);
                        });
                    }

                    planlariOnceligeGoreSirala(planListesi);
                });
    }

    // ÇÖP KUTUSU SORUSU
    private void planiniCopeGonderSorusu(Plan plan) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Planı Kaldır");
        builder.setMessage("Bu planı çöp kutusuna göndermek istediğinden emin misin?");

        builder.setPositiveButton("Evet, Gönder", (dialog, which) -> {
            try {
                db.collection("Planlar").document(plan.getId())
                        .update("silindi", true)
                        .addOnSuccessListener(aVoid -> mesajGoster("Plan çöp kutusuna atıldı! ️"))
                        .addOnFailureListener(e -> hataMesajiGoster("Çöpe Gönderme", e));
            } catch (Exception e) {
                hataMesajiGoster("Sistem Atık Motoru", e);
            }
        });

        builder.setNegativeButton("Vazgeç", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // PLAN TAMAMLANDI (TİK) SORUSU
    private void planiniTamamlaSorusu(Plan plan) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Görevi Mühürle");
        builder.setMessage("Bu planı başarıyla bitirdin mi?");

        builder.setPositiveButton("Evet, Bitirdim ", (dialog, which) -> {
            try {
                db.collection("Planlar").document(plan.getId())
                        .update("tamamlandi", true)
                        .addOnSuccessListener(aVoid -> mesajGoster("Harika! Görev başarıyla tamamlandı "))
                        .addOnFailureListener(e -> hataMesajiGoster("Plan Tamamlama", e));
            } catch (Exception e) {
                hataMesajiGoster("Sistem Onay Motoru", e);
            }
        });

        builder.setNegativeButton("Hayır, Henüz Değil", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void planlariOnceligeGoreSirala(List<Plan> liste) {
        java.util.Collections.sort(liste, (p1, p2) -> {
            return Integer.compare(getRenkOnceligi(p1.getRenk()), getRenkOnceligi(p2.getRenk()));
        });

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private int getRenkOnceligi(String renkKodu) {
        if (renkKodu == null) return 3;
        String temizRenk = renkKodu.trim().toUpperCase();
        switch (temizRenk) {
            case "#F44336": return 1;
            case "#FFEB3B": return 2;
            case "#4CAF50": return 3;
            default:
                if (temizRenk.equals("#FF0000") || temizRenk.contains("RED")) return 1;
                if (temizRenk.equals("#FFFF00") || temizRenk.contains("YELLOW")) return 2;
                if (temizRenk.equals("#00FF00") || temizRenk.contains("GREEN")) return 3;
                return 3;
        }
    }

    private void planEklemePenceresiniAc() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_plan_ekle);
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText etBaslik = dialog.findViewById(R.id.edit_plan_baslik);
        EditText etDetay = dialog.findViewById(R.id.edit_plan_detay);
        Button btnTarihSec = dialog.findViewById(R.id.btn_select_date);
        Button btnKaydet = dialog.findViewById(R.id.btn_plan_kaydet);

        View vRed = dialog.findViewById(R.id.view_color_red);
        View vYellow = dialog.findViewById(R.id.view_color_yellow);
        View vGreen = dialog.findViewById(R.id.view_color_green);

        secilenRenkStr = "#4CAF50";
        dialogTakvim = Calendar.getInstance();

        if (vRed != null) vRed.setOnClickListener(v -> dialogRenkEfekti(dialog, vRed, vYellow, vGreen, "#F44336"));
        if (vYellow != null) vYellow.setOnClickListener(v -> dialogRenkEfekti(dialog, vYellow, vRed, vGreen, "#FFEB3B"));
        if (vGreen != null) vGreen.setOnClickListener(v -> dialogRenkEfekti(dialog, vGreen, vRed, vYellow, "#4CAF50"));

        if (btnTarihSec != null) {
            btnTarihSec.setOnClickListener(view -> {
                new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
                    dialogTakvim.set(Calendar.YEAR, year);
                    dialogTakvim.set(Calendar.MONTH, month);
                    dialogTakvim.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    new TimePickerDialog(this, (view2, hourOfDay, minute) -> {
                        dialogTakvim.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        dialogTakvim.set(Calendar.MINUTE, minute);
                        Toast.makeText(this, "Zaman tanımlandı.", Toast.LENGTH_SHORT).show();
                    }, dialogTakvim.get(Calendar.HOUR_OF_DAY), dialogTakvim.get(Calendar.MINUTE), true).show();

                }, dialogTakvim.get(Calendar.YEAR), dialogTakvim.get(Calendar.MONTH), dialogTakvim.get(Calendar.DAY_OF_MONTH)).show();
            });
        }

        btnKaydet.setOnClickListener(view -> {
            String baslik = etBaslik.getText().toString().trim();
            if (!baslik.isEmpty()) {
                try {
                    com.google.firebase.Timestamp planaAtananZaman = new com.google.firebase.Timestamp(dialogTakvim.getTime());

                    Plan yeniPlan = new Plan(
                            baslik,
                            etDetay.getText().toString().trim(),
                            secilenRenkStr,
                            sessionManager.getUsername(),
                            planaAtananZaman
                    );

                    db.collection("Planlar").add(yeniPlan).addOnSuccessListener(doc -> {
                        Toast.makeText(this, "Plan başarıyla tanımlandı.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

                } catch (Exception e) {
                    Toast.makeText(this, "Sistem Hatası: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Başlığı boş bırakma!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void dialogRenkEfekti(Dialog dialog, View secilenView, View diger1, View diger2, String renkKodu) {
        secilenRenkStr = renkKodu;

        secilenView.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).start();
        secilenView.setBackgroundResource(R.drawable.color_circle_selected);
        secilenView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(renkKodu)));

        diger1.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
        dialogOrijinalRengiGeriYukle(dialog, diger1);
        diger1.setBackgroundTintList(null);

        diger2.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
        dialogOrijinalRengiGeriYukle(dialog, diger2);
        diger2.setBackgroundTintList(null);
    }

    private void dialogOrijinalRengiGeriYukle(Dialog dialog, View view) {
        if (view.getId() == R.id.view_color_red) {
            view.setBackgroundResource(R.drawable.color_circle_red);
        } else if (view.getId() == R.id.view_color_yellow) {
            view.setBackgroundResource(R.drawable.color_circle_yellow);
        } else if (view.getId() == R.id.view_color_green) {
            view.setBackgroundResource(R.drawable.color_circle_green);
        }
    }
}