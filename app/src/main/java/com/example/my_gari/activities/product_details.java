
package com.example.my_gari.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.my_gari.R;
import com.example.my_gari.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hishd.tinycart.util.TinyCartHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class product_details extends AppCompatActivity {

    ImageView productImage;
    ImageView add_items,remove_items;
    TextView name,price, productdescription, quantity;
    Button addToCart, buyNow;
    int totalQuantity =1;
    int totalPrice = 0;

    Product product = null;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    Product currentProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productImage = findViewById(R.id.productImage);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        productdescription = findViewById(R.id.productdescription);
        quantity = findViewById(R.id.quantity);
        addToCart = findViewById(R.id.addToCart);
        add_items = findViewById(R.id.add_items);
        remove_items = findViewById(R.id.remove_items);
         buyNow = findViewById(R.id.buyNowBtn);



        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object obj = getIntent().getSerializableExtra("product");
        if (obj instanceof Product) {
            product = (Product) obj;
            currentProduct = (Product) obj; // Initialize currentProduct with the received product
        }

        if (product != null) {
            Glide.with(getApplicationContext())
                    .load(product.getImage())
                    .into(productImage);
            price.setText(product.getPrice() + "ksh");
            name.setText(product.getName());
            productdescription.setText(product.getDescription());

            totalPrice = product.getPrice() * totalQuantity;


            getSupportActionBar().setTitle(name.getText());

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        }

        addToCart.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });

        add_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalQuantity<10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                }
                if (product != null){
                    totalPrice = product.getPrice() * totalQuantity;
                }

            }
        });
        remove_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }

                if (product != null){
                    totalPrice = product.getPrice() * totalQuantity;
                }
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(product_details.this, address.class);

                if(product!=null){
                    intent.putExtra("item",product);
                }
                startActivity(intent);

            }
        });

    }


    private void addToCart() {
            String saveCurrentTime, saveCurrentDate;
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy", Locale.getDefault());
            saveCurrentDate = currentDate.format(calForDate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a", Locale.getDefault());
            saveCurrentTime = currentTime.format(calForDate.getTime());

            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("name", name.getText().toString());
            cartMap.put("price", price.getText().toString());
            cartMap.put("currentTime", saveCurrentTime); // Store currentTime instead of the format
            cartMap.put("currentDate", saveCurrentDate); // Store date with the correct format
            cartMap.put("totalQuantity", quantity.getText().toString()); // Assuming product has a getQuantity() method
            cartMap.put("totalPrice",totalPrice);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("AddToCart").document(auth.getCurrentUser().getUid()).collection("User")
                    .add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(product_details.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(product_details.this, Cart.class));
                        }
                    });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cart) {
            startActivity(new Intent(this, Cart.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}