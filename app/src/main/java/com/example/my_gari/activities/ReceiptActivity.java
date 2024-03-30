package com.example.my_gari.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.my_gari.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReceiptActivity extends AppCompatActivity {

    Button done;
    TextView amountTotal, orderId;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        done = findViewById(R.id.doneBtn);
        amountTotal = findViewById(R.id.amount);
        orderId = findViewById(R.id.order);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Retrieve totalBill and orderId from intent extras
        float totalBill = getIntent().getFloatExtra("TotalAmount", 0.0f);
        String orderIdValue = getIntent().getStringExtra("OrderId");

        // Display the retrieved values in TextViews
        amountTotal.setText(String.format("%.2f Ksh", totalBill));
        orderId.setText(orderIdValue);

        // Call the saveReceipt method to save the receipt details to Firestore
        saveReceipt(orderIdValue, totalBill);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiptActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    void saveReceipt(String orderId, float totalBill) {
        // Create a new document with a generated ID
        Map<String, Object> receipt = new HashMap<>();
        receipt.put("orderId", orderId);
        receipt.put("totalBill", totalBill);

        // Add a new document with a generated ID
        db.collection("Orders").document(auth.getCurrentUser().getUid()).collection("User")
                .add(receipt)
                .addOnSuccessListener(documentReference -> {
                    // Log success message if document is added successfully
                    System.out.println("Receipt added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    // Log error message if document addition fails
                    System.out.println("Error adding receipt: " + e.getMessage());
                });
    }
}
