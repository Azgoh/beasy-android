package com.example.beasy.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beasy.R;
import com.example.beasy.data.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    // Use a mutable ArrayList so updateList() can replace contents
    private final List<Category>          categories;
    private final OnCategoryClickListener listener;

    public CategoryAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = new ArrayList<>(categories);
        this.listener   = listener;
    }

    /**
     * Replaces the current list with a new one and refreshes the grid.
     * Called by HomeActivity whenever the search query changes.
     */
    public void updateList(List<Category> newList) {
        categories.clear();
        categories.addAll(newList);
        notifyDataSetChanged(); // tells RecyclerView to redraw all items
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvEmoji.setText(category.getEmoji());
        holder.tvName.setText(category.getName());
        // Tapping a category chip fills the search box
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category));
    }

    @Override
    public int getItemCount() { return categories.size(); }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvName;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmoji = itemView.findViewById(R.id.tvCategoryEmoji);
            tvName  = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}