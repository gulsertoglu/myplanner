package com.example.myplanner.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.myplanner.R;
import com.google.android.material.navigation.NavigationView;

// OOP KRİTERİ: BaseActivity kalıtımını koruyoruz, miras alıyoruz kız!
public class SettingsActivity extends BaseActivity {

    private SwitchCompat switchDarkMode;
    private SwitchCompat switchNotifications;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Senin yazdığın o harika mor-pembe tasarım aynen yükleniyor!
        setContentView(R.layout.activity_settings);

        // 🎯 ORTAK MENÜ BAĞLANTISI: XML'e birazdan ekleyeceğimiz ID'lerle menüyü buluyoruz kız
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_settings);
        NavigationView navigationView = findViewById(R.id.nav_view_settings);
        ImageButton btnOpenMenu = findViewById(R.id.btn_open_menu_settings);

        // 🔥 TEK SATIRLIK SİHİR: BaseActivity'deki o efsane ortak menü motorunu ateşliyoruz!
        menuyuHazirla(drawerLayout, navigationView, btnOpenMenu);

        // Ayarların durumunu kalıcı olarak kaydetmek için SharedPreferences motorunu başlatıyoruz
        sharedPreferences = getSharedPreferences("MyPlannerAyarlar", Context.MODE_PRIVATE);

        // XML'indeki gerçek ID'lerle elemanları bağlıyoruz kız:
        switchDarkMode = findViewById(R.id.switch_dark_mode);
        switchNotifications = findViewById(R.id.switch_notifications);

        // Telefonun hafızasında kayıtlı eski ayarlar varsa onları geri yüklüyoruz:
        ayarlariHafizadanYukle();

        // 1. Karanlık Mod Switch Dinleyicisi
        if (switchDarkMode != null) {
            switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("darkMode", isChecked);
                editor.apply(); // Arka planda hemen kaydet

                if (isChecked) {
                    mesajGoster("Karanlık mod mühürlendi!");
                } else {
                    mesajGoster("Aydınlık mod seçildi! ");
                }
            });
        }

        // 2. Bildirim İzin Ver Switch Dinleyicisi
        if (switchNotifications != null) {
            switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("notifications", isChecked);
                editor.apply();

                if (isChecked) {
                    mesajGoster("Bildirim büyüleri aktif edildi!");
                } else {
                    mesajGoster("Bildirimler sessizliğe gömüldü.");
                }
            });
        }
    }

    // Uygulama her açıldığında switch'lerin eski durumunu hatırlayan profesyonel metodumuz
    private void ayarlariHafizadanYukle() {
        try {
            // Karanlık mod varsayılan olarak XML'inde true (checked="true") olduğu için default değerini true yaptık kız
            boolean darkModeDurum = sharedPreferences.getBoolean("darkMode", true);
            boolean bildirimDurum = sharedPreferences.getBoolean("notifications", false);

            if (switchDarkMode != null) {
                switchDarkMode.setChecked(darkModeDurum);
            }
            if (switchNotifications != null) {
                switchNotifications.setChecked(bildirimDurum);
            }
        } catch (Exception e) {
            hataMesajiGoster("Ayarlar Yükleme", e);
        }
    }
}