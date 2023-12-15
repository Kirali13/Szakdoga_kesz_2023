package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Tr_Retrieve extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    private RecyclerView recyclerView;
    private Tr_Recycle_Adapter adapter;

    Button filterbtn;
    Button filterbtn1;
    String userID;
    String type;
    ImageView plus;
    ImageView receipt;
    ImageView chart;
    ImageView logout;
    private FirebaseAuth firebaseAuth;

    DocumentReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tr_recycle);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (currentUser != null) {
            userID = currentUser.getUid();
            // Use userID to load data specifically for this user from Firestore
            loadDataFromFirestore(userID);
        } else {
            // Handle the case where the user is not signed in
        }


        recyclerView = findViewById(R.id.recyclerView);
        filterbtn = findViewById(R.id.filterButton);
        filterbtn1 = findViewById(R.id.filterButton1);
        plus = findViewById(R.id.plus);
        receipt = findViewById(R.id.receipt);
        chart = findViewById(R.id.chart);
        logout = findViewById(R.id.logout);
        filterbtn.setTag(1);
        filterbtn1.setTag(1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Tr_Recycle_Adapter(this); // Create your adapter instance
        recyclerView.setAdapter(adapter);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tr_Retrieve.this, Tr_Create.class);
                startActivity(intent);
            }
        });

        receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tr_Retrieve.this, Tr_Receipt_Retrieve.class);
                startActivity(intent);
            }
        });

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tr_Retrieve.this, Tr_Stats.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent intent = new Intent(Tr_Retrieve.this, MainActivity.class);
                startActivity(intent);
                firebaseAuth.signOut();
            }
        });

        filterbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int status =(Integer) view.getTag();
                type = "KÉSZPÉNZES";
                if (status == 1){
                    adapter.deleteData();
                    filter(userID, type);
                    filterbtn1.setText("Összes");
                    view.setTag(0);
                } else if (status == 0) {
                    adapter.deleteData();
                    loadDataFromFirestore(userID);
                    filterbtn1.setText("Készpénzes");
                    view.setTag(1);
                }
            }
        });

        // Load data from Firestore or elsewhere and set it to your adapter
    }

    private void loadDataFromFirestore(String userID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tr10")
                .whereEqualTo("userID", userID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Tr_Object object = document.toObject(Tr_Object.class);
                        if (object.getKey() != null){
                            adapter.addData(object);
                        }
                    }
                    adapter.sort();
                })
                .addOnFailureListener(e -> {
                    Log.d(LOG_TAG, "Something is not working");
                    // Handle any errors while fetching data
                });
    }

    private void filter(String userID, String type){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tr10")
                .whereEqualTo("userID", userID)
                .whereEqualTo("type", type)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Handle the retrieved data
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert each document to your object model and add it to your adapter
                        Tr_Object object = document.toObject(Tr_Object.class);
                        Log.d("RetrievedObject", object.toString());
                        if (object.getKey() != null){
                            adapter.addData(object);
                            // Method to add data to your adapter
                        }
                    }
                    adapter.sort();
                });
    }

    public void filter(View view) {
        final int status =(Integer) view.getTag();
        type = "KÁRTYÁS";
        if (status == 1){
            adapter.deleteData();
            filter(userID, type);
            filterbtn.setText("Összes");
            view.setTag(0);
        } else if (status == 0) {
            adapter.deleteData();
            loadDataFromFirestore(userID);
            filterbtn.setText("Kártyás");
            view.setTag(1);
        }
    }
}