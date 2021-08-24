package com.autobots.innopark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button change_password;
    Button cancel_password_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        change_password = findViewById(R.id.id_change_password_btn);
        cancel_password_reset = findViewById(R.id.id_cancel_password_reset_forgot);

       change_password.setOnClickListener(view -> {
           startActivity(new Intent(getApplicationContext(), LoginActivity.class));
       } );

        cancel_password_reset.setOnClickListener(view -> {
            finish();
        });
    }
}