package com.example.myeventapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {


    EditText textUsername, textEmail, textPassword, textPasswordConfirm;
    Button registerBtn;

    private FirebaseAuth mAuth;

    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(RegisterActivity.this, EventActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView signUpTxt = findViewById(R.id.haveAnAccount);
        //If user already has an account
        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        textUsername = findViewById(R.id.registerUsername);
        textEmail = findViewById(R.id.registerEmail);
        textPassword = findViewById(R.id.registerPassword);
        textPasswordConfirm = findViewById(R.id.registerConfirmPassword);
        registerBtn = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.progessBar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //ClickListener after user click register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String username, email, password, passwordConf;
                username = textUsername.getText().toString();
                email = textEmail.getText().toString();
                password = textPassword.getText().toString();
                passwordConf = textPasswordConfirm.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Please enter an email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(passwordConf)) {
                    Toast.makeText(RegisterActivity.this, "Please enter a password confirmation", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals(passwordConf)){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        //Log.d(TAG, "createUserWithEmail:success");
                                        Toast.makeText(RegisterActivity.this, "Account Created.",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, EventActivity.class));
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Please enter the same password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}