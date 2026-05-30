package com.example.myplanner.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myplanner.R;
import com.example.myplanner.models.Plan;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private List<Plan> planListesi;

    // İki buton için iki ayrı dinleyici tanımlıyoruz kanka
    private OnPlanSilmeListener silmeDinleyicisi;
    private OnPlanTamamlaListener tamamlaDinleyicisi;

    public interface OnPlanSilmeListener {
        void onPlanSilClick(Plan plan);
    }

    public interface OnPlanTamamlaListener {
        void onPlanTamamlaClick(Plan plan);
    }

    public void setOnPlanSilmeListener(OnPlanSilmeListener listener) {
        this.silmeDinleyicisi = listener;
    }

    public void setOnPlanTamamlaListener(OnPlanTamamlaListener listener) {
        this.tamamlaDinleyicisi = listener;
    }

    public PlanAdapter(List<Plan> planListesi) {
        this.planListesi = planListesi;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan = planListesi.get(position);

        // Eğer plan tamamlandıysa başına tik koyup kartı hafif şeffaf yapıyoruz
        if (plan.isTamamlandi()) {
            holder.tvTitle.setText("✅ " + plan.getBaslik());
            holder.itemView.setAlpha(0.5f);
            if (holder.btnComplete != null) holder.btnComplete.setVisibility(View.GONE); // Zaten bitmişse tik butonunu gizle
        } else {
            holder.tvTitle.setText(plan.getBaslik());
            holder.itemView.setAlpha(1.0f);
            if (holder.btnComplete != null) holder.btnComplete.setVisibility(View.VISIBLE);
        }

        holder.tvContent.setText(plan.getIcerik());

        if (plan.getTarih() != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
            String formatliTarih = sdf.format(plan.getTarih().toDate());
            holder.tvDate.setText(formatliTarih);
        } else {
            holder.tvDate.setText("Tarih belirtilmedi");
        }

        if (plan.getRenk() != null && !plan.getRenk().isEmpty()) {
            holder.viewPriority.setBackgroundColor(Color.parseColor(plan.getRenk()));
        }

        // 🔥 1. Çöp Kutusu İkonuna Basıldığında
        holder.deletebutton.setOnClickListener(v -> {
            if (silmeDinleyicisi != null) {
                silmeDinleyicisi.onPlanSilClick(plan);
            }
        });

        // 🔥 2. Tik (Onay) İkonuna Basıldığında
        if (holder.btnComplete != null) {
            holder.btnComplete.setOnClickListener(v -> {
                if (tamamlaDinleyicisi != null) {
                    tamamlaDinleyicisi.onPlanTamamlaClick(plan);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return planListesi.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvDate;
        View viewPriority;
        ImageButton deletebutton, btnComplete; // btnComplete: Tasarımdaki tik butonu kanka

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvContent = itemView.findViewById(R.id.tv_item_content);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            deletebutton = itemView.findViewById(R.id.btn_delete_plan);
            viewPriority = itemView.findViewById(R.id.view_priority_indicator);

            btnComplete = itemView.findViewById(R.id.btn_complete_plan);
        }
    }
}