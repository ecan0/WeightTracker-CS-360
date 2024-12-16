package com.example.weighttracker.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weighttracker.Data.LoggedWeight;
import com.example.weighttracker.R;
import java.util.ArrayList;
import java.util.List;

public class LoggedWeightAdapter extends RecyclerView.Adapter<LoggedWeightAdapter.ViewHolder> {

    private List<LoggedWeight> loggedWeights;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public LoggedWeightAdapter(List<LoggedWeight> loggedWeights) {
        this.loggedWeights = loggedWeights != null ? loggedWeights : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logged_weight_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoggedWeight loggedWeight = loggedWeights.get(position);
        holder.dateTextView.setText(loggedWeight.date);
        holder.weightTextView.setText(String.valueOf(loggedWeight.weight));
        holder.goalMetTextView.setText(loggedWeight.goalMet ? "Yes" : "No");

        holder.itemView.setSelected(selectedPosition == position);
        holder.itemView.setOnClickListener(v -> {
            notifyItemChanged(selectedPosition);
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return loggedWeights.size();
    }

    public LoggedWeight getSelectedLoggedWeight() {
        return selectedPosition != RecyclerView.NO_POSITION ? loggedWeights.get(selectedPosition) : null;
    }

    public void updateData(List<LoggedWeight> newLoggedWeights) {
        this.loggedWeights = newLoggedWeights != null ? newLoggedWeights : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, weightTextView, goalMetTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            weightTextView = itemView.findViewById(R.id.weightTextView);
            goalMetTextView = itemView.findViewById(R.id.goalMetTextView);
        }
    }
}