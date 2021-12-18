package com.autobots.innopark.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.autobots.innopark.R;
import com.autobots.innopark.util.DatabaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordActivity extends AppCompatActivity
{
    public static final String TAG = "ForgotPassword";
    Button change_password;
    Button cancel;
    EditText email;
    ProgressBar progressBar;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        change_password = findViewById(R.id.id_forgot_password_btn);
        cancel = findViewById(R.id.id_forgot_password_cancel);
        email = findViewById(R.id.id_forgot_password_email);
        progressBar = findViewById(R.id.id_forgot_password_progress_bar);

        setupChangePassword();

        cancel.setOnClickListener(view -> {
            finish();
        });
    }

    private void setupChangePassword()
    {
        change_password.setOnClickListener((V) -> {
            progressBar.setVisibility(View.VISIBLE);
            String userEmail = email.getText().toString().trim();
            firebaseAuth.sendPasswordResetEmail(userEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful())
                            {
                                Log.d(TAG, "onComplete: " + "Password sent to email");
                                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: due to exception " + e.getMessage());
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        } );
    }

}