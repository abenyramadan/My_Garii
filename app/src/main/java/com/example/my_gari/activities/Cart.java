package com.example.my_gari.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_gari.adapters.CartAdapter;
import com.example.my_gari.databinding.ActivityCartBinding;
import com.example.my_gari.model.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart extends AppCompatActivity {

    ActivityCartBinding binding;
    CartAdapter adapter;
    RecyclerView recyclerView;
    TextView totalPriceAmount;
    List<MyCartModel> cartModelList;
    FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize the RecyclerView
        recyclerView = binding.cartList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list to hold cart items
        cartModelList = new ArrayList<>();

        // Initialize the adapter
        adapter = new CartAdapter(this, cartModelList);
        recyclerView.setAdapter(adapter);

        // Register the broadcast receiver to update total amount
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));

        // Initialize the total price amount TextView using View Binding
        totalPriceAmount = binding.textView25;


        // Fetch cart items from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("AddToCart").document(auth.getCurrentUser().getUid()).collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                MyCartModel myCartModel = document.toObject(MyCartModel.class);
                                cartModelList.add(myCartModel);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        // Set onClickListener for the continue button
        binding.continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieve the total amount from the TextView
                String totalPriceString = totalPriceAmount.getText().toString();
                // Extract the numeric value from the string
                float totalBill = extractTotalBill(totalPriceString);

                // Storing totalBill in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("totalBill", totalBill);
                editor.apply();
              Intent intent = new Intent(Cart.this, address.class);
              startActivity(intent);
            }
        });

        // Enable back button in ActionBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private float extractTotalBill(String totalPriceString) {

        // Extract the numeric value from the string (assuming "Total Amount: X Ksh" format)
        String totalAmount = totalPriceString.replaceAll("[^\\d.]", "");
        return Float.parseFloat(totalAmount);

    }

    // Broadcast receiver to receive total amount from adapter
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totalBill = intent.getIntExtra("totalAmount", 0);
            totalPriceAmount.setText("Total Amount: " + totalBill + "Ksh");
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
