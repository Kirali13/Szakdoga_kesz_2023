package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tr_Create extends AppCompatActivity {

    private EditText tr_name;
    private EditText tr_amount;
    private DatePicker datePicker;
    /*private String tr_date;*/
    private Spinner groupSpinner;
    private Spinner currencySpinner;
    private Spinner typeSpinner;
    private Tr_Object.Group selectedGroup;
    private Tr_Object.Currency selectedCurrency;
    private Tr_Object.Type selectedType;
    private FirebaseFirestore db;
    FirebaseUser user;
    DocumentReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_transaction);

        tr_name = findViewById(R.id.editText_tr_name);
        tr_amount = findViewById(R.id.editText_tr_amount);
        groupSpinner = findViewById(R.id.groupSpinner);
        currencySpinner = findViewById(R.id.currencySpinner);
        typeSpinner = findViewById(R.id.typeSpinner);
        datePicker = findViewById(R.id.datePicker);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();


        ArrayAdapter<Tr_Object.Group> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Tr_Object.Group.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        groupSpinner.setAdapter(adapter);

        selectedGroup = (Tr_Object.Group) groupSpinner.getSelectedItem();

        ArrayAdapter<Tr_Object.Currency> adapter1 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Tr_Object.Currency.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        currencySpinner.setAdapter(adapter1);

        selectedCurrency = (Tr_Object.Currency) currencySpinner.getSelectedItem();

        ArrayAdapter<Tr_Object.Type> adapter2 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Tr_Object.Type.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        typeSpinner.setAdapter(adapter2);

        selectedType = (Tr_Object.Type) typeSpinner.getSelectedItem();

    }

    public void create_tr(View view) {
        ref = db.collection("tr10").document();
        String id = (ref.getId());

        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

        // Convert the selected date to a Date object
        Date selectedDate = calendar.getTime();

        String tr_date = DateFormat.getDateInstance().format(selectedDate);

        String userID = null;

        if (user != null) {
            userID = user.getUid(); // Retrieve userID if user is signed in
        }

        if(userID != null){
        String tr_nev = tr_name.getText().toString();
        int tr_osszeg = Integer.parseInt(tr_amount.getText().toString());

        selectedGroup = (Tr_Object.Group) groupSpinner.getSelectedItem();
        selectedCurrency = (Tr_Object.Currency) currencySpinner.getSelectedItem();
        selectedType = (Tr_Object.Type) typeSpinner.getSelectedItem();



            // Create a MyObject instance with the retrieved values
        Tr_Object myObject = new Tr_Object();
        myObject.setUserID(userID);
        myObject.setTr_name(tr_nev);
        myObject.setTr_amount(tr_osszeg);
        myObject.setTr_date(tr_date);
        myObject.setCurrency(selectedCurrency);
        myObject.setType(selectedType);
        myObject.setGroup(selectedGroup);

        // Add the MyObject instance to Firestore
        addMyObjectToFirestore(myObject);
        }else{

        }
    }

    private void addMyObjectToFirestore(Tr_Object myObject) {
        // Specify the name of the collection where you want to store the objects
        String collectionName = "tr10"; // Replace with your actual collection name

        // Add the MyObject instance to the Firestore collection
        db.collection(collectionName)
                .add(myObject)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String ref = documentReference.getId();
                        documentReference.update("key", ref);
                        Intent intent = new Intent(Tr_Create.this, Tr_Retrieve.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Tr_Create.this, "Object wasn't created successfully!", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
