package com.example.myplanner.activities;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myplanner.R;
import com.example.myplanner.adapters.PlanAdapter;
import com.example.myplanner.models.Plan;
import com.example.myplanner.models.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// OOP KRİTERİ: BaseActivity kalıtımını ve Firebase entegrasyonunu eksiksiz koruyoruz kız!
public class CalendarActivity extends BaseActivity {

    private CalendarView calendarView;
    private RecyclerView rvCalendarPlans;
    private TextView tvSelectedDateTitle;
    private PlanAdapter adapter;
    private List<Plan> gunlukPlanListesi = new ArrayList<>();
    private SessionManager sessionManager;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar); // Tasarım dosyasını bağlıyoruz

        db = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(this);

        // 🎯 ORTAK MENÜ BAĞLANTISI: XML'deki ID'lerle menü bileşenlerini buluyoruz kız
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_calendar);
        NavigationView navigationView = findViewById(R.id.nav_view_calendar);
        ImageButton btnOpenMenu = findViewById(R.id.btn_open_menu_calendar);

        // 🔥 TEK SATIRLIK SİHİR: BaseActivity'deki o devasa ortak menü motorunu ateşliyoruz!
        menuyuHazirla(drawerLayout, navigationView, btnOpenMenu);

        calendarView = findViewById(R.id.calendar_view);
        rvCalendarPlans = findViewById(R.id.rv_calendar_plans);
        tvSelectedDateTitle = findViewById(R.id.tv_selected_date_title);

        rvCalendarPlans.setLayoutManager(new LinearLayoutManager(this));

        // Zaten yazdığımız o efsane çift butonlu adapter'ı burada da aynen kullanıyoruz kız (Kod tekrarı yok!)
        adapter = new PlanAdapter(gunlukPlanListesi);
        rvCalendarPlans.setAdapter(adapter);

        // İlk açılışta bugünün planlarını getir kanka
        Calendar bugun = Calendar.getInstance();
        tariheGorePlanlariGetir(bugun.get(Calendar.YEAR), bugun.get(Calendar.MONTH), bugun.get(Calendar.DAY_OF_MONTH));

        // 🎯 TAKVİMDEN GÜN SEÇİLME DİNLEYİCİSİ
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Kullanıcı hangi güne tıklarsa başlığı güncelle ve o günün planlarını çek kız
            tvSelectedDateTitle.setText(dayOfMonth + "/" + (month + 1) + "/" + year + " Tarihli Planlar");
            tariheGorePlanlariGetir(year, month, dayOfMonth);
        });
    }

    private void tariheGorePlanlariGetir(int year, int month, int dayOfMonth) {
        String aktifKullanici = sessionManager.getUsername();
        if (aktifKullanici == null) return;

        // Seçilen günün başlangıç (00:00) ve bitiş (23:59) zaman damgalarını hesaplıyoruz kız
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth, 0, 0, 0);
        Date baslangicTarihi = cal.getTime();

        cal.set(year, month, dayOfMonth, 23, 59, 59);
        Date bitisTarihi = cal.getTime();

        Timestamp startTimestamp = new Timestamp(baslangicTarihi);
        Timestamp endTimestamp = new Timestamp(bitisTarihi);

        // 🔥 FİREBASE SORGUSU: Sadece o güne ait, silinmemiş planları cımbızla çekiyoruz
        db.collection("Planlar")
                .whereEqualTo("sahibi", aktifKullanici)
                .whereEqualTo("silindi", false)
                .whereGreaterThanOrEqualTo("tarih", startTimestamp)
                .whereLessThanOrEqualTo("tarih", endTimestamp)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        android.util.Log.e("FirebaseTakvimHata", error.getMessage());
                        return;
                    }

                    gunlukPlanListesi.clear();
                    if (value != null) {
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Plan plan = doc.toObject(Plan.class);
                            if (plan != null) {
                                plan.setId(doc.getId());
                                gunlukPlanListesi.add(plan);
                            }
                        }
                    }

                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }

                    if (gunlukPlanListesi.isEmpty()) {
                        mesajGoster("Bu tarihe mühürlenmiş bir plan yok kanka! 🕊️");
                    }
                });
    }
}