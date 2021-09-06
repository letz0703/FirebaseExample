package com.letz.firebaseexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class phoneSignInActivity extends AppCompatActivity
{
    EditText phoneNumber;
    Button SMSRequest;
    EditText code;
    Button signIn;
    String sentCode;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_sign_in);

        phoneNumber = findViewById(R.id.editTextPhone);
        SMSRequest = findViewById(R.id.buttonSendSMS);
        code = findViewById(R.id.editTextNumberPassword);
        signIn = findViewById(R.id.buttonSignPhone);

        SMSRequest.setOnClickListener(v -> {
            String userPhoneNumber = phoneNumber.getText().toString();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(userPhoneNumber
                    , 60, TimeUnit.SECONDS, phoneSignInActivity.this, mCallBack);
        });

        signIn.setOnClickListener(v -> {
            signInWithPhoneCode();
        });
    }

    public void signInWithPhoneCode() {
        //get the code that user entered
        //compare it w/ the sentCode
        String enterUserCode = code.getText().toString();
        PhoneAuthCredential credential
                = PhoneAuthProvider.getCredential(sentCode, enterUserCode);
        signInWithPhoneAuthCredential(credential);
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent i = new Intent(phoneSignInActivity.this, main_menu.class);
                            startActivity(i);
                            finish();

                        } else {
                            Toast.makeText(phoneSignInActivity.this, "Incorrect code", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack
            = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            sentCode = s;
        }
    };
}











