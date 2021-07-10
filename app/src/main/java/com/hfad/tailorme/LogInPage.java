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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button logInButton;
    TextInputEditText emailId, passwordId;
    String email;
    String password;
    TextView signUpLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getBaseContext());

        setContentView(R.layout.activity_log_in_page);
        logInButton = findViewById(R.id.logIn);
        signUpLink = findViewById(R.id.signUpLink);
        emailId = findViewById(R.id.email);
        passwordId = findViewById(R.id.password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailId.getText().toString();
                password = passwordId.getText().toString();

               // try {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LogInPage.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("SUCCESSFUL", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        // update UI
                                        Intent intent = new Intent(LogInPage.this, HomePage.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("FAILED", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LogInPage.this, "Log In failed.",
                                                Toast.LENGTH_SHORT).show();
                                        // update ui
                                    }
                                }
                            });
               // }catch(Exception e){
                    Toast.makeText(LogInPage.this,"Log In Failed",Toast.LENGTH_LONG)
                            .show();
               // }

            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInPage.this, SignUpPage.class);
                startActivity(intent);
            }
        });




    }

    @Override
    public void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // startMain Activity
            Intent intent = new Intent(LogInPage.this, HomePage.class);
            startActivity(intent);
        }
    }


}