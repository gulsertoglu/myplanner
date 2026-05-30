package com.example.myplanner.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.myplanner.R;
import com.google.android.material.navigation.NavigationView;

// OOP KRİTERİ: BaseActivity'den türeterek kalıtımı ve ortak menü yeteneklerini miras alıyoruz kız!
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about); // Yeni tasarımla eşledik kız

        // XML'e eklediğimiz ID'lerle elemanları pencereden çağırıyoruz
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_about);
        NavigationView navigationView = findViewById(R.id.nav_view_about);
        ImageButton btnOpenMenu = findViewById(R.id.btn_open_menu_about);

        // BaseActivity'deki o devasa ortak menü motorunu ateşliyoruz!
        menuyuHazirla(drawerLayout, navigationView, btnOpenMenu);
    }
}