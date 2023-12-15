package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends Activity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();

    EditText EmailEditText;
    EditText EmailConfirmedEditText;
    EditText passwordEditText;
    EditText passwordConfirmEditText;

    private FirebaseAuth mAuth;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        mAuth = FirebaseAuth.getInstance();

        EmailEditText = findViewById(R.id.reg_email);
        EmailConfirmedEditText = findViewById(R.id.editText_reg_email2);
        passwordEditText = findViewById(R.id.editText_reg_pw);
        passwordConfirmEditText = findViewById(R.id.editText_reg_pw2);



        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        String email = preferences.getString("email", "");
        String password = preferences.getString("password", "");

        EmailEditText.setText(email);
        EmailConfirmedEditText.setText(email);
        passwordEditText.setText(password);
        passwordConfirmEditText.setText(password);

        ImageView imageView = (ImageView) findViewById(R.id.myImageView);

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }


    public void register(View view) {
        String email = EmailEditText.getText().toString();
        String emailConfirm = EmailConfirmedEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();

        if (!password.equals(passwordConfirm)) {
            Log.e(LOG_TAG, "Nem egyenlő a jelszó és a megerősítése.");
            return;
        } else if (!email.equals(emailConfirm)) {
            Log.e(LOG_TAG, "Nem egyenlő az email cím és a megerősítése.");
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    Log.d(LOG_TAG, "User created successfully");
                } else {
                    Log.d(LOG_TAG, "User was't created successfully:", task.getException());
                    Toast.makeText(RegisterActivity.this, "User wasn't created successfully:", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
