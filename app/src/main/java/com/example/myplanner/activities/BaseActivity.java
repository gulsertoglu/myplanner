package com.example.myplanner.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myplanner.R;
import com.example.myplanner.models.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

// OOP KRİTERİ: Abstract üst sınıf (Superclass). Kalıtım ve merkezi yönetim şablonumuz kız!
public abstract class BaseActivity extends AppCompatActivity {

    protected FirebaseFirestore db = FirebaseFirestore.getInstance();

    // 🎯 YENİ (ORTAK MENÜ MOTORU): Kod tekrarını sıfıra indiren asil mimarimiz kız!
    // Menü olmasını istediğin her aktivite (Home, Settings, Calendar vb.) onCreate içinde bu metodu tek satırla çağıracak.
    protected void menuyuHazirla(DrawerLayout drawerLayout, NavigationView navigationView, ImageButton btnOpenMenu) {

        // 1. Üst bardaki Hamburger ikonuna basınca sol menüyü kaydırarak açar
        if (btnOpenMenu != null && drawerLayout != null) {
            btnOpenMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }

        // 2. Menü elemanlarına tıklanınca hangi sayfaya gidileceğini tek merkezden yönetiyoruz
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                Intent intent = null;

                // Sonsuz döngüyü engellemek için şu an açık olan aktivitenin adını alıyoruz kız
                String mevcutSayfa = this.getClass().getSimpleName();

                if (id == R.id.nav_profile && !mevcutSayfa.equals("ProfileActivity")) {
                    intent = new Intent(this, ProfileActivity.class);
                }
                else if (id == R.id.nav_trash && !mevcutSayfa.equals("TrashActivity")) {
                    intent = new Intent(this, TrashActivity.class);
                }
                else if (id == R.id.nav_calendar && !mevcutSayfa.equals("CalendarActivity")) {
                    intent = new Intent(this, CalendarActivity.class);
                }
                else if (id == R.id.nav_notes && !mevcutSayfa.equals("NoteActivity")) {
                    intent = new Intent(this, NoteActivity.class);
                }
                else if (id == R.id.nav_settings && !mevcutSayfa.equals("SettingsActivity")) {
                    intent = new Intent(this, SettingsActivity.class);
                }
                else if (id == R.id.nav_add_plan && !mevcutSayfa.equals("AddPlanActivity")) {
                    intent = new Intent(this, AddPlanActivity.class);
                }
                else if (id == R.id.nav_logout) {
                    SessionManager sessionManager = new SessionManager(this);
                    sessionManager.logoutUser();
                    intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }

                // Eğer yeni bir sayfaya yönlendirme yapıldıysa tetikle kız
                if (intent != null) {
                    startActivity(intent);
                    // Ana sayfa hariç diğer sayfaları kapatalım ki geri tuşuna basınca arkada yığılma olmasın
                    if (!mevcutSayfa.equals("HomeActivity")) {
                        finish();
                    }
                }

                // İşlem bittikten sonra menüyü otomatik olarak kaydırarak kapatıyoruz
                if (drawerLayout != null) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            });
        }
    }

    public void mesajGoster(String mesaj) {
        Toast.makeText(this, mesaj, Toast.LENGTH_SHORT).show();
    }

    // TEKNİK UYGULAMA KRİTERİ: Ortak Exception Handling metodumuz aynen korunuyor kanka.
    public void hataMesajiGoster(String kaynak, Exception e) {
        String detayliHata = kaynak + " işleminde hata oluştu: " + e.getLocalizedMessage();
        Toast.makeText(this, detayliHata, Toast.LENGTH_LONG).show();
    }

    public boolean internetVarMi() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public void internetYokEkraniGoster() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_no_internet);
        dialog.setCancelable(false);

        Button btnRetry = dialog.findViewById(R.id.btn_retry_internet);
        Button btnOffline = dialog.findViewById(R.id.btn_offline_mode);

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
    }
}