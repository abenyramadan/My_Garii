package com.example.my_gari.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my_gari.R;

public class PaymentActivity extends AppCompatActivity {
    TextView subtotal, discount, shipping, total;
    Button paymentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Retrieve the subtotal amount from the intent extras
        double amount = getIntent().getDoubleExtra("amount", 0.0);

        subtotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView14);
        shipping = findViewById(R.id.textView15);
        total = findViewById(R.id.textView19);
        paymentBtn = findViewById(R.id.pay_btn);

        // Set the subtotal amount to the TextView
        subtotal.setText(amount + "Ksh");

        // Retrieve totalBill from SharedPreferences
        float totalBill = retrieveTotalBillFromSharedPreferences();

        // Set the total amount to the TextView
        subtotal.setText(String.format("%.2f Ksh", totalBill));


        // Set onClickListener for the payment button
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve totalBill from SharedPreferences
                float totalBill = retrieveTotalBillFromSharedPreferences();

                // Start the CheckOutActivity and pass both subtotal and totalBill amounts
                Intent intent = new Intent(PaymentActivity.this, CheckOutActivity.class);
                intent.putExtra("amount", amount); // Pass the subtotal amount
                intent.putExtra("TotalAmount", totalBill); // Pass the totalBill amount
                startActivity(intent);
            }
        });

        // Enable the back button in the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle the back button press in the ActionBar
        finish();
        return super.onSupportNavigateUp();
    }

    private float retrieveTotalBillFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getFloat("totalBill", 0.0f);
    }
}
