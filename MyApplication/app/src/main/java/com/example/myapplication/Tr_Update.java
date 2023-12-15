package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Tr_Update extends AppCompatActivity {

    private Spinner groupSpinner;
    private Spinner currencySpinner;
    private Spinner typeSpinner;

    private Tr_Object.Group selectedGroup;
    private Tr_Object.Currency selectedCurrency;
    private Tr_Object.Type selectedType;
    FirebaseUser user;

    FirebaseFirestore db;
    EditText edit_name;
    EditText edit_amount;
    EditText edit_date;
    Tr_Object tr_edit;
    ImageView home;

    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tr_update);

        edit_name = findViewById(R.id.edit_name);
        edit_amount = findViewById(R.id.edit_amount);
        edit_date = findViewById(R.id.edit_date);
        groupSpinner = findViewById(R.id.groupSpinner);
        currencySpinner = findViewById(R.id.currencySpinner);
        typeSpinner = findViewById(R.id.typeSpinner);
        home = findViewById(R.id.myImageView);

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

        tr_edit = (Tr_Object) getIntent().getSerializableExtra("SZERKESZTÉS");
        if (tr_edit != null) {
            String eamount = Integer.toString(tr_edit.getTr_amount());
            edit_name.setText(tr_edit.getTr_name());
            edit_amount.setText(eamount);
            edit_date.setText(tr_edit.getTr_date());
        } else {
        }

    }

    public void update_Tr(View view) {
        //String userID, String tr_name, int tr_amount, Currency currency, Type type, String tr_date, Group group
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        Tr_Object tr = new Tr_Object(userId, edit_name.getText().toString(), Integer.parseInt(edit_amount.getText().toString()), selectedCurrency, selectedType, edit_date.getText().toString(), selectedGroup);
        if (tr_edit == null) {
            db.collection("tr10").add(tr).addOnSuccessListener(suc ->
            {
                Toast.makeText(this, "Record is inserted", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(er ->
            {
                Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            selectedGroup = (Tr_Object.Group) groupSpinner.getSelectedItem();
            selectedCurrency = (Tr_Object.Currency) currencySpinner.getSelectedItem();
            selectedType = (Tr_Object.Type) typeSpinner.getSelectedItem();
            String key = tr_edit.getKey();
            DocumentReference documentReference;
            documentReference = db.collection("tr10").document(key);
            documentReference.update("tr_name",edit_name.getText().toString(),"tr_amount",
                    Integer.parseInt(edit_amount.getText().toString()),"tr_date",edit_date.getText().toString(),"currency",
                    selectedCurrency,"type",selectedType,"group",selectedGroup).addOnSuccessListener(suc ->
            {
                Toast.makeText(this, "Record is updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,Tr_Retrieve.class);
                this.startActivity(intent);
            }).addOnFailureListener(er ->
            {
                Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    public void home(View view) {
        Intent intent = new Intent(this, Tr_Retrieve.class);
        startActivity(intent);
    }
}
