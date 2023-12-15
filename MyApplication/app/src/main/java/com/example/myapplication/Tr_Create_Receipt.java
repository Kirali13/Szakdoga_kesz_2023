package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tr_Create_Receipt extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "YourActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private static final String LOG_TAG = MainActivity.class.getName();


    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private ImageView capturedImageView;
    private EditText editText;
    private EditText editText2;
    String formattedDate;

    Uri imageUri;
    StorageReference storageReference;

    String imageUrl;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tr_create_receipt);

        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        capturedImageView = findViewById(R.id.imageView);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    private void uploadImage() {
        String fileName = editText.getText().toString();
        String supplier = editText2.getText().toString();
        userID = null;
        imageUrl = imageUri.toString();

        if (currentUser != null) {
            userID = currentUser.getUid(); // Retrieve userID if user is signed in
        }
        storageReference = FirebaseStorage.getInstance().getReference("Tr_Receipts/"+fileName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {

                                Date creationDate = new Date(storageMetadata.getCreationTimeMillis());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                formattedDate = sdf.format(creationDate); // Format the date

                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Tr_Receipt receipt = new Tr_Receipt(userID, fileName, supplier, formattedDate ,uri.toString());
                                        db.collection("tr_receipt") // Replace "images" with your desired collection name
                                                .add(receipt)
                                                .addOnSuccessListener(documentReference -> {
                                                    // Document added successfully
                                                    String documentId = documentReference.getId();
                                                    // Handle success if needed (e.g., display a success message)
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle failure (e.g., show an error message)
                                                });
                                        Toast.makeText(Tr_Create_Receipt.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Tr_Create_Receipt.this, Tr_Receipt_Retrieve.class);
                                        startActivity(intent);

                                    }
                                });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Fos");
                    }
                });

    }
    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            capturedImageView.setImageURI(imageUri);
        }
    }

    public void opencamera(View view) {
        selectImage();
    }

    public void uploadToFirestorm(View view) {
        uploadImage();
    }
}