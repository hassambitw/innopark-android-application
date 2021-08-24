package com.autobots.innopark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    TextView register_tv;
    Button login_btn;
    TextView forgot_password_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register_tv = findViewById(R.id.id_register_text);
        login_btn = findViewById(R.id.id_login_btn);
        forgot_password_tv = findViewById(R.id.id_forgot_password_txt);

        register_tv.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        login_btn.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        });

        forgot_password_tv.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
        });
    }

}