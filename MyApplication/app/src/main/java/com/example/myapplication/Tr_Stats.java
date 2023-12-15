package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tr_Stats extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    List<String> typeList = new ArrayList<>();
    List<String> barList = new ArrayList<>();

    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tr_stats);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        home = findViewById(R.id.myImageView);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tr_Stats.this, Tr_Retrieve.class);
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
    }

    private void loadDataFromFirestore(String userID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("tr10")
                .whereEqualTo("userID", userID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Handle the retrieved data
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert each document to your object model and add it to your adapter
                        String type = document.getString("group");
                        String bartype = document.getString("tr_date");
                        if (type != null && bartype != null) {
                            typeList.add(type);
                            barList.add(bartype);// Add 'type' value to the list
                        }
                    }
                    loadChart();
                    loadChart1();
                })
                .addOnFailureListener(e -> {
                    Log.d(LOG_TAG, "Valami szar");
                    // Handle any errors while fetching data
                });
    }

    private void loadChart() {
        Map<String, Integer> typeCountMap = new HashMap<>();
        for (String type : typeList) {
            if (typeCountMap.containsKey(type)) {
                int count = typeCountMap.get(type);
                typeCountMap.put(type, count + 1);
            } else {
                typeCountMap.put(type, 1);
            }
        }

// Prepare data for the pie chart
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : typeCountMap.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

// Create a dataset for the pie chart
        PieDataSet dataSet = new PieDataSet(entries, "Kategóriák");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);

// Create a pie data object from dataset
        PieData pieData = new PieData(dataSet);

// Get a reference to your PieChart view
        PieChart pieChart = findViewById(R.id.pieChart);

// Set the pie data into the pie chart
        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh the chart
    }

    private void loadChart1() {
        Map<String, Integer> typeCountMap = new HashMap<>();
        for (String currency : barList) {
            if (typeCountMap.containsKey(currency)) {
                int count = typeCountMap.get(currency);
                typeCountMap.put(currency, count + 1);
            } else {
                typeCountMap.put(currency, 1);
            }
        }

// Prepare data for the pie chart
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : typeCountMap.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

// Create a dataset for the pie chart
        PieDataSet dataSet = new PieDataSet(entries, "Tranzakciók gyakorisága");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setValueTextSize(12f);


// Create a pie data object from dataset
        PieData pieData = new PieData(dataSet);

// Get a reference to your PieChart view
        PieChart pieChart = findViewById(R.id.barChart);
        pieChart.setDrawHoleEnabled(false);

// Set the pie data into the pie chart
        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh the chart
    }

}