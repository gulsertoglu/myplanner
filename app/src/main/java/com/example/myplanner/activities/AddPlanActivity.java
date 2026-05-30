package com.example.myplanner.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.myplanner.R;
import com.example.myplanner.models.Plan;
import com.example.myplanner.models.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;

// OOP KRİTERİ: BaseActivity'den türeterek Inheritance (Kalıtım) yapısını GERÇEKTEN mühürlüyoruz kız!
public class AddPlanActivity extends BaseActivity {

    private Button btnKaydet, btnTarihSec;
    private EditText etBaslik, etDetay;
    private SessionManager sessionManager;
    private Calendar secilenTakvim = Calendar.getInstance();

    // Varsayılan rengi yeşil (#4CAF50) yapıyoruz kanka
    private String secilenRenkStr = "#4CAF50";

    // Renk butonlarımızı (View'larımızı) tanımlıyoruz
    private View vRed, vYellow, vGreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan); // Tasarım dosyanı bağlıyoruz

        sessionManager = new SessionManager(this);

        // 🎯 ORTAK MENÜ BAĞLANTISI: XML'e birazdan ekleyeceğimiz ID'lerle menüyü buluyoruz kız
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_add_plan);
        NavigationView navigationView = findViewById(R.id.nav_view_add_plan);
        ImageButton btnOpenMenu = findViewById(R.id.btn_open_menu_add_plan);

        // 🔥 TEK SATIRLIK SİHİR: BaseActivity'deki o ortak menü motorunu ateşliyoruz!
        menuyuHazirla(drawerLayout, navigationView, btnOpenMenu);

        btnKaydet = findViewById(R.id.btn_save_plan);
        etBaslik = findViewById(R.id.et_plan_title);
        etDetay = findViewById(R.id.et_plan_detail);
        btnTarihSec = findViewById(R.id.btn_select_date);

        // XML'deki 3 daire ID'sini buraya bağlıyoruz kanka
        vRed = findViewById(R.id.view_color_red);
        vYellow = findViewById(R.id.view_color_yellow);
        vGreen = findViewById(R.id.view_color_green);

        // Dairelere tıklandığında o büyüme ve siyah border (kenarlık) efektini tetikliyoruz
        if (vRed != null) vRed.setOnClickListener(v -> renkSecildiEfekti(vRed, vYellow, vGreen, "#F44336", "Yüksek Öncelik 🔴"));
        if (vYellow != null) vYellow.setOnClickListener(v -> renkSecildiEfekti(vYellow, vRed, vGreen, "#FFEB3B", "Orta Öncelik 🟡"));
        if (vGreen != null) vGreen.setOnClickListener(v -> renkSecildiEfekti(vGreen, vRed, vYellow, "#4CAF50", "Düşük Öncelik 🟢"));

        btnKaydet.setOnClickListener(v -> {
            String baslik = etBaslik.getText().toString().trim();
            String detay = etDetay.getText().toString().trim();
            String sahibi = sessionManager.getUsername();

            if (!baslik.isEmpty()) {
                // BÜYÜK OOP VE TEKNİK KRİTER: Exception Handling (try-catch) korundu!
                try {
                    com.google.firebase.Timestamp planaAtananZaman = new com.google.firebase.Timestamp(secilenTakvim.getTime());

                    Plan yeniPlan = new Plan(baslik, detay, secilenRenkStr, sahibi, planaAtananZaman);

                    db.collection("Planlar")
                            .add(yeniPlan)
                            .addOnSuccessListener(documentReference -> {
                                yeniPlan.setId(documentReference.getId());
                                mesajGoster("Plan başarıyla tanımlandı.");
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                hataMesajiGoster("Firebase Plan Ekleme", e);
                            });

                } catch (Exception e) {
                    hataMesajiGoster("Sistem Planlama Motoru", e);
                }
            } else {
                mesajGoster("Plan başlığını boş bırakamazsın bebüş! 📝");
            }
        });

        btnTarihSec.setOnClickListener(v -> tarihSaatSec());
    }

    // Seçilen daireyi 1.2 kat büyütüp siyah border verir, diğerlerini küçültür
    private void renkSecildiEfekti(View secilenView, View diger1, View diger2, String renkKodu, String mesaj) {
        secilenRenkStr = renkKodu;
        mesajGoster(mesaj);

        secilenView.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).start();
        secilenView.setBackgroundResource(R.drawable.color_circle_selected);
        secilenView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(renkKodu)));

        diger1.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
        orijinalArkaplaniGeriYukle(diger1);
        diger1.setBackgroundTintList(null);

        diger2.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
        orijinalArkaplaniGeriYukle(diger2);
        diger2.setBackgroundTintList(null);
    }

    private void orijinalArkaplaniGeriYukle(View view) {
        if (view.getId() == R.id.view_color_red) {
            view.setBackgroundResource(R.drawable.color_circle_red);
        } else if (view.getId() == R.id.view_color_yellow) {
            view.setBackgroundResource(R.drawable.color_circle_yellow);
        } else if (view.getId() == R.id.view_color_green) {
            view.setBackgroundResource(R.drawable.color_circle_green);
        }
    }

    private void tarihSaatSec() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            secilenTakvim.set(Calendar.YEAR, year);
            secilenTakvim.set(Calendar.MONTH, month);
            secilenTakvim.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
                secilenTakvim.set(Calendar.HOUR_OF_DAY, hourOfDay);
                secilenTakvim.set(Calendar.MINUTE, minute);
                mesajGoster("Zaman başarıyla tanımlandı.");
            }, secilenTakvim.get(Calendar.HOUR_OF_DAY), secilenTakvim.get(Calendar.MINUTE), true).show();

        }, secilenTakvim.get(Calendar.YEAR), secilenTakvim.get(Calendar.MONTH), secilenTakvim.get(Calendar.DAY_OF_MONTH)).show();
    }
}