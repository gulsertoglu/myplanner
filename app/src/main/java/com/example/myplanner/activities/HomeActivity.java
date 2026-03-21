package com.example.myplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplanner.R;
import com.example.myplanner.adapters.PlanAdapter;
import com.example.myplanner.models.Plan;
import com.example.myplanner.models.SessionManager;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private RecyclerView rvPlans;
    private PlanAdapter adapter;
    private List<Plan> planListesi = new ArrayList<>();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        rvPlans = findViewById(R.id.rv_plans);
        sessionManager = new SessionManager(this);

        rvPlans.setLayoutManager(new LinearLayoutManager(this));

        String aktifKullanici = sessionManager.getUsername();
        if (aktifKullanici != null) {
            planlariGetir(aktifKullanici);
        }
        ImageButton btnLogout = findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            finish();

            mesajGoster("Başarıyla çıkış yapıldı. Yine bekleriz!");
        });
    }
    private void planlariGetir(String username) {
        db.collection("Planlar")
                .whereEqualTo("sahibi", username)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        mesajGoster("Hata: " + error.getMessage());
                        return;
                    }

                    planListesi.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Plan plan = doc.toObject(Plan.class);
                        if (plan != null) {
                            plan.setId(doc.getId());
                            planListesi.add(plan);
                        }
                    }

                    adapter = new PlanAdapter(planListesi);
                    rvPlans.setAdapter(adapter);
                });
        LinearLayout layoutEmpty = findViewById(R.id.layout_empty);
        if (planListesi.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvPlans.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvPlans.setVisibility(View.VISIBLE);
        }
    }
}