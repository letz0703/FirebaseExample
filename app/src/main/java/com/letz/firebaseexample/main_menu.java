package com.letz.firebaseexample;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class main_menu extends AppCompatActivity
{
    ImageView profilePic;
    ProgressBar loading;
    Button upload;
    Button signOut;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        profilePic = findViewById(R.id.imageViewProfilePic);
        loading = findViewById(R.id.progressBarProfilePic);
        upload = findViewById(R.id.buttonUploadProfilePicture);

        profilePic.setOnClickListener(v -> {

        });

        upload.setOnClickListener(v->{

        });

        signOut = findViewById(R.id.button);

        signOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(main_menu.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    public void fileChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        //디바이스의 파일 시스템 열기
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchChooser.launch(i);
    }

    ActivityResultLauncher< Intent > launchChooser = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                    }
                }
            }
    );
}