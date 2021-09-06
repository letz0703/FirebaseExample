package com.letz.firebaseexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;

public class ForgetActivity extends AppCompatActivity
{
    EditText email;
    Button reset;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        email = findViewById(R.id.editTextReset);
        reset = findViewById(R.id.buttonReset);

        reset.setOnClickListener(v->{
            String userEmail = email.getText().toString();

            auth.sendPasswordResetEmail(userEmail)
            .addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ForgetActivity.this
                                , "We sent an email for reset pw"
                                , Toast.LENGTH_SHORT).show();
                    }
                }
            });

            finish();

        });
    }
}