package com.example.my_gari.adapters;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_gari.R;
import com.example.my_gari.databinding.ItemCartBinding;
import com.example.my_gari.databinding.ItemCategoriesBinding;
import com.example.my_gari.databinding.QuantityDialogBinding;
import com.example.my_gari.model.MyCartModel;
import com.example.my_gari.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    Context context;
    List<MyCartModel> cartModelList;
    int totalAmount = 0;

    public  CartAdapter(Context context,List<MyCartModel> cartModelList){
        this.context = context;
        this.cartModelList = cartModelList;

    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {

        holder.product_name.setText(cartModelList.get(position).getName());
        holder.product_price.setText(cartModelList.get(position).getPrice()+"Ksh");
        holder.current_date.setText(cartModelList.get(position).getCurrentDate());
        holder.current_time.setText(cartModelList.get(position).getCurrentTime());
        holder.total_quantity.setText(cartModelList.get(position).getTotalQuantity());
        holder.total_prices.setText(String.valueOf(cartModelList.get(position).getTotalPrice()));


        // Total Amount pass to Cart Activity
        totalAmount = totalAmount + cartModelList.get(position).getTotalPrice();
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("totalAmount", totalAmount);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


    }

        @Override
    public int getItemCount() {
        return cartModelList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView product_name, product_price, current_date, current_time, total_quantity, total_prices;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price=itemView.findViewById(R.id.product_price);
            current_date= itemView.findViewById(R.id.current_date);
            current_time=itemView.findViewById(R.id.current_time);
            total_quantity=itemView.findViewById(R.id.total_quantity);
            total_prices = itemView.findViewById(R.id.total_prices);
        }

    }
}

