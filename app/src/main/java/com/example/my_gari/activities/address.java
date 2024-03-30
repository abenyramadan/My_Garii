package com.example.my_gari.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_gari.R;
import com.example.my_gari.adapters.AddressAdapter;
import com.example.my_gari.model.AddressModel;
import com.example.my_gari.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class address extends AppCompatActivity implements AddressAdapter.SelectedAddress{

    Button addAddress;
    RecyclerView recyclerView;
    List<AddressModel> addressModelList;
    AddressAdapter addressAdapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Button addAddressBtn, paymentBtn, continueToPayment;
    String mAddress = "";
    double totalBill = 0.0; // Initialize totalBill variable




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get data from product detail
        Object obj = getIntent().getSerializableExtra("item");


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.address_recycler);
        addAddressBtn = findViewById(R.id.add_address_btn);
        paymentBtn = findViewById(R.id.payment_btn);
        continueToPayment = findViewById(R.id.payment_btn);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addressModelList = new ArrayList<>();
        addressAdapter = new AddressAdapter(getApplicationContext(),addressModelList,this);
        recyclerView.setAdapter(addressAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("Address").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    AddressModel addressModel = doc.toObject(AddressModel.class);
                    addressModelList.add(addressModel);
                }
                // Notify adapter after all data is added
                addressAdapter.notifyDataSetChanged();
            } else {
                // Handle the error if the task is not successful
                // For example, show a toast message or log the error
            }
        });




        // Retrieve total bill from intent extras
        Intent intent = getIntent();
        totalBill = intent.getDoubleExtra("TotalAmount", 0.0);

        paymentBtn.setOnClickListener(v -> {

            double amount = 0.0;
            if (obj instanceof Product){
                Product product =(Product) obj;
                amount = product.getPrice();
            }

            Intent paymentIntent = new Intent(address.this, PaymentActivity.class);

            paymentIntent.putExtra("amount", amount);

            // Pass the total bill to PaymentActivity
            paymentIntent.putExtra("TotalAmount", totalBill);

            startActivity(paymentIntent);
        });


        addAddress = findViewById(R.id.add_address_btn);


        addAddress.setOnClickListener(v -> startActivity(new Intent(address.this, AddAddress.class)));

    }




    @Override
    public void setAddress(String address) {

        mAddress = address;
    }
}
