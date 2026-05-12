package com.example.beasy.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beasy.R;
import com.example.beasy.data.model.Professional;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ProfessionalAdapter extends RecyclerView.Adapter<ProfessionalAdapter.ProfessionalViewHolder> {

    public interface OnBookClickListener {
        void onBookClick(Professional professional);
    }

    private final List<Professional>  professionals;
    private final OnBookClickListener listener;

    public ProfessionalAdapter(List<Professional> professionals, OnBookClickListener listener) {
        this.professionals = new ArrayList<>(professionals);
        this.listener      = listener;
    }

    /**
     * Replaces the current list with filtered results and refreshes the RecyclerView.
     */
    public void updateList(List<Professional> newList) {
        professionals.clear();
        professionals.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProfessionalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_professional, parent, false);
        return new ProfessionalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessionalViewHolder holder, int position) {
        Professional pro = professionals.get(position);
        holder.tvName.setText(pro.getName());
        holder.tvCategory.setText(pro.getCategory() + " · ⭐ " + pro.getRating() + " (" + pro.getReviewCount() + " reviews)");
        holder.tvPrice.setText(pro.getPrice());
        holder.tvRating.setText("⭐ " + pro.getRating());
        holder.btnBook.setOnClickListener(v -> listener.onBookClick(pro));
    }

    @Override
    public int getItemCount() { return professionals.size(); }

    static class ProfessionalViewHolder extends RecyclerView.ViewHolder {
        TextView     tvName, tvCategory, tvPrice, tvRating;
        MaterialButton btnBook;

        ProfessionalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName     = itemView.findViewById(R.id.tvProName);
            tvCategory = itemView.findViewById(R.id.tvProCategory);
            tvPrice    = itemView.findViewById(R.id.tvProPrice);
            tvRating   = itemView.findViewById(R.id.tvProRating);
            btnBook    = itemView.findViewById(R.id.btnBook);
        }
    }
}