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

    // List to hold logged weight data
    private List<LoggedWeight> loggedWeights;
    // Position of the selected item in the RecyclerView
    private int selectedPosition = RecyclerView.NO_POSITION;

    // Constructor to initialize the adapter with a list of logged weights
    public LoggedWeightAdapter(List<LoggedWeight> loggedWeights) {
        this.loggedWeights = loggedWeights != null ? loggedWeights : new ArrayList<>();
    }

    // Inflate the item layout and create the ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logged_weight_item, parent, false);
        return new ViewHolder(view);
    }

    // Bind data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoggedWeight loggedWeight = loggedWeights.get(position);
        holder.dateTextView.setText(loggedWeight.date);
        holder.weightTextView.setText(String.valueOf(loggedWeight.weight));
        holder.goalMetTextView.setText(loggedWeight.goalMet ? "Yes" : "No");

        // Highlight the selected item
        holder.itemView.setSelected(selectedPosition == position);
        holder.itemView.setOnClickListener(v -> {
            notifyItemChanged(selectedPosition);
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(selectedPosition);
        });
    }

    // Return the total number of items
    @Override
    public int getItemCount() {
        return loggedWeights.size();
    }

    // Get the currently selected logged weight
    public LoggedWeight getSelectedLoggedWeight() {
        return selectedPosition != RecyclerView.NO_POSITION ? loggedWeights.get(selectedPosition) : null;
    }

    // Update the data in the adapter and refresh the view
    public void updateData(List<LoggedWeight> newLoggedWeights) {
        this.loggedWeights = newLoggedWeights != null ? newLoggedWeights : new ArrayList<>();
        notifyDataSetChanged();
    }

    // ViewHolder class to hold references to the views in each item
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