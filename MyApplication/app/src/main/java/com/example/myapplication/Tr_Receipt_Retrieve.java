package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Tr_Receipt_Retrieve extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final int READ_EXTERNAL_STORAGE_REQUEST = 100;

    private RecyclerView recyclerView;
    private Tr_Receipt_Adapter adapter;
    private ArrayList<Tr_Receipt> list;

    ImageView plus;
    ImageView home;

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tr_receipt_retrieve);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        plus = findViewById(R.id.plus);
        home = findViewById(R.id.myImageView);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tr_Receipt_Retrieve.this, Tr_Create_Receipt.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tr_Receipt_Retrieve.this, Tr_Retrieve.class);
                startActivity(intent);
            }
        });

        if (currentUser != null) {
            String userID = currentUser.getUid();
            // Use userID to load data specifically for this user from Firestore
            loadDataFromFirestore(userID);

        } else {
            // Handle the case where the user is not signed in
        }


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        adapter = new Tr_Receipt_Adapter(this, list); // Create your adapter instance
        recyclerView.setAdapter(adapter);
    }

    private void loadDataFromFirestore(String userID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("tr_receipt")
                .whereEqualTo("userID", userID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Handle the retrieved data
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert each document to your object model and add it to your adapter
                        Tr_Receipt object = document.toObject(Tr_Receipt.class);
                        Log.d("RetrievedObject", object.toString());
                        adapter.addData(object); // Method to add data to your adapter
                    }
                    adapter.sort();
                })
                .addOnFailureListener(e -> {
                    Log.d(LOG_TAG, "Valami szar");
                    // Handle any errors while fetching data
                });
    }
}