package com.letz.firebaseexample;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class main_menu extends AppCompatActivity
{
    ImageView profilePic;
    ProgressBar progressBar;

    Button upload;
    Button signOut;
    Uri imageUri;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        storageReference = FirebaseStorage.getInstance().getReference(user.getEmail());

        signOut = findViewById(R.id.buttonSignOut);
        profilePic = findViewById(R.id.imageViewProfilePic);
        upload = findViewById(R.id.buttonUploadProfilePicture);
        progressBar = findViewById(R.id.progressBarProfilePic);

        progressBar.setVisibility(View.INVISIBLE);



        profilePic.setOnClickListener(v -> {

            fileChooser();

        });

        upload.setOnClickListener(v -> {

            uploadPhoto();

        });

        signOut = findViewById(R.id.buttonSignOut);

        signOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(main_menu.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    public void fileChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        //디바이스의 파일 시스템 열기
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchChooser.launch(i);
    }

    ActivityResultLauncher<Intent> launchChooser = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Picasso.get().load(imageUri).into(profilePic);
                    }
                }
            }
    );

    public void uploadPhoto() {
        upload.setClickable(false);
        progressBar.setVisibility(View.VISIBLE);

        StorageReference reference = storageReference.child("user");
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(main_menu.this, "Upload successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(main_menu.this, "Sorry, there is a problem", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
            }
        });

    }
}
