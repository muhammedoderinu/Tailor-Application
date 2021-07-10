package com.hfad.tailorme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpPage extends AppCompatActivity {
    TextInputEditText emailText, passwordText;
    String email, password;
    Button signUpButton;
    private FirebaseAuth mAuth;
    TextView logInLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        logInLink = findViewById(R.id.logInLink);
        signUpButton = findViewById(R.id.SignUp);
        emailText = findViewById(R.id.signUpEmail);
        passwordText = findViewById(R.id.signUpPassword);

        logInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpPage.this, LogInPage.class);
                startActivity(intent);
            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailText.getText().toString();
                password = passwordText.getText().toString();
                try {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpPage.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Successfull", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(SignUpPage.this, HomePage.class);
                                        startActivity(intent);
                                        //UpdateUi
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Failed", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpPage.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        // updateUI
                                    }
                                }
                            });
                }catch(Exception e){
                    Toast.makeText(SignUpPage.this,"Authentication Failed",Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(SignUpPage.this, HomePage.class);
            startActivity(intent);
        }
    }
}