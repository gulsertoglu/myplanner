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
        holder.tvTitle.setText(plan.getBaslik());
        holder.tvContent.setText(plan.getIcerik());
        if (plan.getTarih() != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
            String formatliTarih = sdf.format(plan.getTarih().toDate());
            holder.tvDate.setText(formatliTarih);
        } else {
            holder.tvDate.setText("Tarih belirtilmedi");
        }

        if (plan.isTamamlandi()) {
            holder.itemView.setAlpha(0.5f);
        }
        if (plan.getRenk() != null && !plan.getRenk().isEmpty()) {
            // holder içindeki o renkli kutucuğun ID'si neyse onu kullan (Mesela viewPriority)
            holder.viewPriority.setBackgroundColor(Color.parseColor(plan.getRenk()));
        }
        holder.deletebutton.setOnClickListener(v -> {
            if (listener != null) {
                // listener aracılığıyla HomeActivity'ye "Bu plana tıklandı!" haberi gönderiyoruz
                listener.onPlanLongClick(plan);
            }
        });
    }

    public interface OnPlanLongClickListener {
        void onPlanLongClick(Plan plan);
    }

    private OnPlanLongClickListener listener;

    public void setOnPlanLongClickListener(OnPlanLongClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return planListesi.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvDate;
        View viewPriority;
        ImageButton deletebutton;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvContent = itemView.findViewById(R.id.tv_item_content);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            deletebutton=itemView.findViewById(R.id.btn_delete_plan);
            viewPriority = itemView.findViewById(R.id.view_priority_indicator);
        }
    }
}