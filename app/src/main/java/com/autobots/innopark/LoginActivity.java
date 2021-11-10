package com.autobots.innopark;

import com.autobots.innopark.data.Callbacks.ArraylistCallback;
import com.autobots.innopark.data.Callbacks.HashmapCallback;
import com.autobots.innopark.data.Callbacks.StringCallback;
import com.autobots.innopark.data.DatabaseUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.autobots.innopark.data.Driver;
import com.autobots.innopark.data.Tags;
import com.autobots.innopark.data.Tariff;
import com.autobots.innopark.data.User;
import com.autobots.innopark.data.UserApi;
import com.autobots.innopark.fragment.NotificationService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity
{

    //widgets
    private TextView register_tv;
    private Button login_btn;
    private TextView forgot_password_tv;
    private EditText email_et;
    private EditText password_et;
    private String currentUserName;
    private String currentUserId;

    //Firebase Auth
    private FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //firestore connection
    private FirebaseFirestore db = DatabaseUtils.db;

    //firebase messaging
    private static final String channel_id = Config.channel_id;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createNotificationChannel();
        getRegistrationToken();

        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic("welcome_to_innopark");

        register_tv = findViewById(R.id.id_register_text);
        login_btn = findViewById(R.id.id_login_btn);
        forgot_password_tv = findViewById(R.id.id_forgot_password_txt);
        email_et = findViewById(R.id.id_email);
        password_et = findViewById(R.id.id_password);

        register_tv.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        login_btn.setOnClickListener(view -> {
            loginUser();
            //startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        });

        forgot_password_tv.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
        });


    }

    private void getRegistrationToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Log.w("Get Token", "onComplete: Failed to get registration token of device");
                    return;
                }


                String token = task.getResult();

                if(Config.current_user_token == null) Config.current_user_token = token;

                Log.w("Get Token", "onComplete: "+ token);
            }
        });
    }

    private void createNotificationChannel() {
        String channel_name = "innopark_notification_channel";
        String channel_description = "receive test notification";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channel_name;
            String description = channel_description;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void loginUser()
    {
        String email = email_et.getText().toString().toLowerCase().trim();
        String password = password_et.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (Config.new_token_status) {
                                    NotificationService.sendRegistrationTokenToServer(Config.current_user_token);
                                    Config.new_token_status = false;
                                }

                                UserApi userApi = UserApi.getInstance();
                                userApi.setUserEmail(email);

                                if (Config.current_user_email == null){
                                    Config.current_user_email = email;

                                    DatabaseUtils.db.collection("users_tokens")
                                            .whereEqualTo("token_id", Config.current_user_token)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d(Tags.SUCCESS.name(), document.getId() + " => " + document.getData());

                                                            DatabaseUtils.updateData("users_tokens",
                                                                    document.getId(), "email_address", email);

                                                        }
                                                    } else {
                                                        Log.d(Tags.FAILURE.name(), "LOGIN ACTIVITY: Token_id doesn't exist: ", task.getException());
                                                    }
                                                }
                                            });
                                }

                                Log.d("TAG", "onComplete: " + currentUserName);

                                Toast.makeText(getApplicationContext(), "User logged in successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "Login error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

}