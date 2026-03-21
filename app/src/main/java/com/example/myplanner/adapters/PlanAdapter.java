package com.example.myplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.tvDate.setText(plan.getTarih());

        if (plan.isTamamlandi()) {
            holder.itemView.setAlpha(0.5f);
        }
    }

    @Override
    public int getItemCount() {
        return planListesi.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvDate;
        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvContent = itemView.findViewById(R.id.tv_item_content);
            tvDate = itemView.findViewById(R.id.tv_item_date);
        }
    }
}