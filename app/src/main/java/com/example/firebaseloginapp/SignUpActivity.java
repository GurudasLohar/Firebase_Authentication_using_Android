package com.example.firebaseloginapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtSignUpEmail,txtSignUpPassword;
    TextView txtSignIn;
    Button btnSignUp;
    ProgressBar progressSignUp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign Up");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        txtSignUpEmail = findViewById(R.id.txtSignUpEmail);
        txtSignUpPassword = findViewById(R.id.txtSignUpPassword);
        txtSignIn = findViewById(R.id.txtSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        progressSignUp = findViewById(R.id.progressSignUp);

        txtSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnSignUp:
                userRegistration();
                break;

            case R.id.txtSignIn:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void userRegistration() {

        String email = txtSignUpEmail.getText().toString().trim();
        String password = txtSignUpPassword.getText().toString().trim();

        if(email.isEmpty()){
            txtSignUpEmail.setError("Please,enter email address");
            txtSignUpEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtSignUpEmail.setError("Please,enter valid email address");
            txtSignUpEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            txtSignUpPassword.setError("Please,enter password");
            txtSignUpPassword.requestFocus();
            return;
        }
        if(password.length()<5){
            txtSignUpPassword.setError("Minimum length of password shuold be 5");
            txtSignUpPassword.requestFocus();
            return;
        }
        progressSignUp.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressSignUp.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(SignUpActivity.this, "User is already Registered", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SignUpActivity.this, "Error :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
