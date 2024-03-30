package com.example.my_gari.adapters;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_gari.R;
import com.example.my_gari.activities.CategoryActivity;
import com.example.my_gari.databinding.ItemCategoriesBinding;
import com.example.my_gari.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    Context context;
    ArrayList<Category> categories;
    public CategoryAdapter(Context context, ArrayList<Category> categories){
        this.context = context;
        this.categories = categories;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.catName.setText(category.getName());
        Glide.with(context)
                .load(category.getIcon())
                .into(holder.catImg);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView catImg;
        TextView catName;
        public CategoryViewHolder(@NonNull View itemView){
            super(itemView);
            catImg = itemView.findViewById(R.id.catImg);
            catName = itemView.findViewById(R.id.catName);
        }
    }
}
