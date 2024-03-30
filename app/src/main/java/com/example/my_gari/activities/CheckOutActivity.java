package com.example.my_gari.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.my_gari.R;
import com.example.my_gari.model.Cards;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import java.util.UUID;

public class CheckOutActivity extends AppCompatActivity {

    private TextInputLayout mCardNumber;
    private TextInputLayout mCardExpiry;
    private TextInputLayout mCardCVV;
    private Button payNow;
    FirebaseFirestore db;

    private static final String TAG = "CheckOutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        payNow = findViewById(R.id.PayNowBtn);

        initializeForwardVariables();

        db = FirebaseFirestore.getInstance();

        // Set title of the action bar
        getSupportActionBar().setTitle("Check Out");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initializeForwardVariables() {
        mCardNumber = findViewById(R.id.CardNumber);
        mCardExpiry = findViewById(R.id.CardExpiry);
        mCardCVV = findViewById(R.id.CardCVV);

        Objects.requireNonNull(mCardExpiry.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (input.length() == 2 && !s.toString().contains("/")) {
                    s.append("/");
                }
            }
        });

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performDummyCharge();
            }
        });
    }

    private void performDummyCharge() {
        // Fetch card details from Firestore
        String cardNumber = Objects.requireNonNull(mCardNumber.getEditText()).getText().toString();

        db.collection("Cards")
                .whereEqualTo("cardNumber", cardNumber)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Card found
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                // Extract card details and perform payment simulation
                                Cards card = document.toObject(Cards.class);
                                Log.d(TAG, "CVV from Firestore: " + card.getCVV());
                                Log.d(TAG, "Expiry date from Firestore: " + card.getCardExpiryDate());
                                simulatePayment(isCardDetailsCorrect(card.getCardNumber(), card.getCardExpiryDate(), card.getCVV()));
                            }
                        } else {
                            // Card not found
                            toastMsg("Incorrect Card Details, Card not Found");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting cards.", e);
                        toastMsg("Error getting card details");
                    }
                });
    }

    private boolean isCardDetailsCorrect(String cardNumber, String cardExpiry, String cvv) {
        // You can implement your validation logic here
        // For simplicity, let's assume the card is correct if it has 16 digits, valid expiry, and 3-digit CVV
        return cardNumber.length() == 16 && cardExpiry.matches("\\d{2}/\\d{2}") && cvv.length() == 3;
    }


    private void simulatePayment(boolean cardDetailsCorrect) {
        if (cardDetailsCorrect) {

            // Generate a new order ID
            String orderId = OrderIdGenerator.generateOrderId();
            float totalBill = retrieveTotalBillFromSharedPreferences();
            // Simulate a successful payment
            toastMsg("Successful Payment");
            Intent intent = new Intent(CheckOutActivity.this, ReceiptActivity.class);
            intent.putExtra("TotalAmount", totalBill); // Pass the total amount to ReceiptActivity
            intent.putExtra("OrderId", orderId); // Pass the order ID

            startActivity(intent);
        } else {
            // Simulate a failed payment due to incorrect card details
            toastMsg("Incorrect Card Details");
        }
    }

    private float retrieveTotalBillFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getFloat("totalBill", 0.0f);
    }

    private void parseResponse(String transactionReference) {
        String message = "Payment Successful - " + transactionReference;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    private void toastMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static class OrderIdGenerator {

        public static String generateOrderId() {
            // Generate a random UUID as the order ID
            return UUID.randomUUID().toString();
        }
    }
}
