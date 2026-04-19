package com.example.myplanner.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends BaseActivity {

    private RecyclerView rvPlans;
    private PlanAdapter adapter;
    private List<Plan> planListesi = new ArrayList<>();
    private SessionManager sessionManager;
    private String secilenRenkStr = "#4CAF50";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Değişkenleri Başlat
        db = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(this);
        rvPlans = findViewById(R.id.rv_plans);
        rvPlans.setLayoutManager(new LinearLayoutManager(this));

        ImageButton btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // 2. Sol Menü (Drawer) Ayarları
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ImageButton btnOpenMenu = findViewById(R.id.btn_open_menu);
        NavigationView navigationView = findViewById(R.id.nav_view);

        btnOpenMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // MENÜ TIKLAMALARI
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) startActivity(new Intent(this, ProfileActivity.class));
            else if (id == R.id.nav_trash) startActivity(new Intent(this, TrashActivity.class));
            else if (id == R.id.nav_settings) startActivity(new Intent(this, SettingsActivity.class));
            else if (id == R.id.nav_about) startActivity(new Intent(this, AboutActivity.class));
            else if (id == R.id.nav_notes) startActivity(new Intent(this, NoteActivity.class));
            else if (id == R.id.nav_add_plan) planEklemePenceresiniAc();

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // 3. Planları Listele
        String aktifKullanici = sessionManager.getUsername();
        if (aktifKullanici != null) {
            planlariGetir(aktifKullanici);
        }

        // 4. Floating Action Button (Artı Butonu)
        FloatingActionButton fabAdd = findViewById(R.id.fab_add_plan);
        fabAdd.setOnClickListener(v -> planEklemePenceresiniAc());
    }

    private void planlariGetir(String username) {
        db.collection("Planlar")
                .whereEqualTo("sahibi", username)
                .whereEqualTo("silindi", false)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
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

                    if (adapter == null) {
                        adapter = new PlanAdapter(planListesi);
                        rvPlans.setAdapter(adapter);
                        // Plana tıklayınca detay sayfasını aç
                        adapter.setOnPlanLongClickListener(selectedPlan -> {
                            Intent intent = new Intent(HomeActivity.this, PlanDetailsActivity.class);
                            intent.putExtra("baslik", selectedPlan.getBaslik());
                            intent.putExtra("detay", selectedPlan.getIcerik());
                            startActivity(intent);
                        });
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void planEklemePenceresiniAc() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_plan_ekle);
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText etBaslik = dialog.findViewById(R.id.edit_plan_baslik);
        EditText etDetay = dialog.findViewById(R.id.edit_plan_detay);
        Button btnKaydet = dialog.findViewById(R.id.btn_plan_kaydet);

        btnKaydet.setOnClickListener(view -> {
            String baslik = etBaslik.getText().toString().trim();
            if (!baslik.isEmpty()) {
                Map<String, Object> plan = new HashMap<>();
                plan.put("baslik", baslik);
                plan.put("detay", etDetay.getText().toString().trim());
                plan.put("renk", secilenRenkStr);
                plan.put("sahibi", sessionManager.getUsername());
                plan.put("tamamlandi", false);
                plan.put("tarih", com.google.firebase.Timestamp.now());
                plan.put("silindi", false);

                db.collection("Planlar").add(plan).addOnSuccessListener(doc -> dialog.dismiss());
            }
        });
        dialog.show();
    }

    public void mesajGoster(String mesaj) {
        Toast.makeText(this, mesaj, Toast.LENGTH_SHORT).show();
    }
}