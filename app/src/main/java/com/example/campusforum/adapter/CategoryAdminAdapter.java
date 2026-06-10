package com.example.campusforum.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusforum.R;
import com.example.campusforum.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdminAdapter extends RecyclerView.Adapter<CategoryAdminAdapter.CategoryViewHolder> {

    public interface OnCategoryActionListener {
        void onDeleteCategory(Category category);
    }

    private final List<Category> categories = new ArrayList<>();
    private final OnCategoryActionListener actionListener;

    public CategoryAdminAdapter(OnCategoryActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void updateCategories(List<Category> updatedCategories) {
        categories.clear();
        if (updatedCategories != null) {
            categories.addAll(updatedCategories);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position), actionListener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameText;
        private final TextView descriptionText;
        private final Button deleteButton;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.item_admin_category_name);
            descriptionText = itemView.findViewById(R.id.item_admin_category_description);
            deleteButton = itemView.findViewById(R.id.item_admin_category_delete_button);
        }

        void bind(Category category, OnCategoryActionListener actionListener) {
            nameText.setText(category.getName());
            String description = category.getDescription();
            boolean hasDescription = description != null && !description.trim().isEmpty();
            descriptionText.setText(hasDescription ? description : "");
            descriptionText.setVisibility(hasDescription ? View.VISIBLE : View.GONE);
            deleteButton.setOnClickListener(view -> {
                if (actionListener != null) {
                    actionListener.onDeleteCategory(category);
                }
            });
        }
    }
}
