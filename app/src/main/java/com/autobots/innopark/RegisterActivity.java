package com.autobots.innopark;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.Callbacks.StringCallback;
import com.autobots.innopark.data.Tags;
import com.autobots.innopark.data.UserApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

   //widgets
    private Button register_btn;
    private RadioGroup radio_grp_vehicle;
    private RadioButton radio_btn_yes;
    private RadioButton radio_btn_no;
    private EditText license_plate_et;
    private EditText email_et;
    private EditText first_name_et;
    private EditText last_name_et;
    private EditText password_et;
    private EditText id_et;
    private EditText phoneNum_et;
    private EditText username_et;
    public static final String TAG = "RegisterActivity";
    ProgressBar progressBar;
    boolean add = true;
    List<String> vehicles_owned;

    //Firebase Auth

    private FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //firestore connection
    private FirebaseFirestore db = DatabaseUtils.db;
    private String collection = "users";

    private boolean ownVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_btn = findViewById(R.id.id_register_btn);
        radio_grp_vehicle = findViewById(R.id.id_radio_grp_vehicle_register);
        radio_btn_yes = findViewById(R.id.id_register_own_vehicle_yes);
        radio_btn_no = findViewById(R.id.id_register_own_vehicle_no);
        license_plate_et = findViewById(R.id.id_license_plate_register);
        email_et = findViewById(R.id.id_register_email);
        first_name_et = findViewById(R.id.id_register_firstname);
        last_name_et = findViewById(R.id.id_register_last_name);
        password_et = findViewById(R.id.id_register_password);
        id_et = findViewById(R.id.id_register_card_id);
        phoneNum_et = findViewById(R.id.id_register_phone);
        progressBar = findViewById(R.id.id_register_progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();

        setupRegisterAction();


//        register_btn.setOnClickListener(view -> {
//            String email = email_et.getText().toString().trim();
//            String first_name = first_name_et.getText().toString().trim();
//            String last_name = last_name_et.getText().toString().trim();
//            String password  = password_et.getText().toString().trim();
//            String id = id_et.getText().toString().trim();
//            String phoneNumber = phoneNum_et.getText().toString().trim();
//            String licenseNum = license_plate_et.getText().toString().trim();
//
//
//            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(first_name) && !TextUtils.isEmpty(last_name)&& !TextUtils.isEmpty(password) && !TextUtils.isEmpty(String.valueOf(id))
//                    && !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(phoneNumber))
//            {
//                createUserEmailAccount(email, first_name, last_name, password, id, phoneNumber, ownVehicle, licenseNum);
//                Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getApplicationContext(), "All fields must be filled!", Toast.LENGTH_SHORT).show();
//            }
//        });

        vehicles_owned = new ArrayList<>();

        radio_grp_vehicle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.id_register_own_vehicle_yes)
                {
                    ownVehicle = true;
                    license_plate_et.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.id_register_own_vehicle_no)
                {
                    ownVehicle = false;
                    license_plate_et.setVisibility(View.INVISIBLE);
                }
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null)
                {
                    //user is already logged in
                }
                else
                    {
                    //no user yet
                }
            }
        };
    }

    private void setupRegisterAction()
    {
        register_btn.setOnClickListener((v) -> {

            String email = email_et.getText().toString().trim();
            String password  = password_et.getText().toString().trim();
            String first_name = first_name_et.getText().toString().trim();
            String last_name = last_name_et.getText().toString().trim();
            String id = id_et.getText().toString().trim();
            String phoneNumber = phoneNum_et.getText().toString().trim();
            String licenseNum = license_plate_et.getText().toString().trim();
//            List<String> vehicles_driven = new ArrayList<String>();

            Log.d(TAG, "setupRegisterAction: Before adding vehicle: " + vehicles_owned);

            if (ownVehicle) vehicles_owned.add(licenseNum);

            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
//            String currentUserID = currentUser.getUid();
        if (ownVehicle) {
            Log.d(TAG, "setupRegisterAction: After adding vehicle " + vehicles_owned);
            db.collection("users")
                    .whereArrayContainsAny("vehicles_owned", vehicles_owned)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.d(TAG, "onSuccess: " + " Inside On Complete");
                            Log.d(TAG, "onComplete: On Complete 2" + vehicles_owned);
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onSuccess: Inside task successful");
                                Log.d(TAG, "onComplete: " + (QuerySnapshot) task.getResult());
                                if (!task.getResult().isEmpty()) {
//                                        Log.d(TAG, "onComplete: Result: " + document.getId());
                                        Log.d(TAG, "onComplete: " + "This vehicle is already owned by someone.");
                                        Toast.makeText(getApplicationContext(), "This vehicle is already owned. Please select a different one YOU own.", Toast.LENGTH_SHORT).show();
                                        license_plate_et.setError("Already exists");
                                        vehicles_owned.clear();
                                        Log.d(TAG, "onComplete: After clearing vehicles " + vehicles_owned);
                                } else {
                                    Log.d(TAG, "onFailure: " + "No document exists where the same vehicle is owned.");
                                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    progressBar.setVisibility(View.VISIBLE);
                                                    if (task.isSuccessful()) {

                                                        currentUser.sendEmailVerification()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        if (task.isSuccessful()) {
                                                                            //create user map so we can add a user to user collection in firestore
                                                                            //change this to license number entered by user if he owns a vehicle

                                                                            Map<String, Object> user = new HashMap<>();
                                                                            user.put("email_address", email);
                                                                            user.put("first_name", first_name);
                                                                            user.put("id_card_number", id);
                                                                            user.put("is_banned", false);
                                                                            user.put("last_name", last_name);
                                                                            user.put("phone_number", phoneNumber);
                                                                            user.put("vehicles_owned", vehicles_owned);

                                                                            DatabaseUtils.addData(collection, currentUser.getUid(), user, new StringCallback() {
                                                                                public void passStringResult(String result) {
                                                                                    if (result.equals(Tags.SUCCESS.name())) {
                                                                                        Log.w(Tags.SUCCESS.name(), "ADDED DATA");
                                                                                        Toast.makeText(getApplicationContext(), "User Registration link sent!", Toast.LENGTH_SHORT).show();
                                                                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                                                    } else {
                                                                                        Toast.makeText(getApplicationContext(), "Failure to add data!", Toast.LENGTH_SHORT).show();
                                                                                        Log.w(Tags.FAILURE.name(), "FAILED TO ADD DATA");
                                                                                    }
                                                                                }
                                                                            });

//                                                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                                                                    Toast.makeText(getApplicationContext(), "User Registration link sent!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        Log.d(TAG, "onFailure: User email not sent due to exception " + e.getMessage());
                                                                    }
                                                                });
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(getApplicationContext(), "Registration Error: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

//                                add = false;
                            }
                        }
                    });
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d(TAG, "onFailure: " + "No document exists where the same vehicle is owned.");
//
//
//                        }
//                    });
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.VISIBLE);
                            if (task.isSuccessful()) {

                                currentUser.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressBar.setVisibility(View.GONE);
                                                if (task.isSuccessful()) {
                                                    //create user map so we can add a user to user collection in firestore
                                                    //change this to license number entered by user if he owns a vehicle

                                                    Map<String, Object> user = new HashMap<>();
                                                    user.put("email_address", email);
                                                    user.put("first_name", first_name);
                                                    user.put("id_card_number", id);
                                                    user.put("is_banned", false);
                                                    user.put("last_name", last_name);
                                                    user.put("phone_number", phoneNumber);
                                                    user.put("vehicles_driven", null);
                                                    user.put("vehicles_owned", null);

                                                    DatabaseUtils.addData(collection, currentUser.getUid(), user, new StringCallback() {
                                                        public void passStringResult(String result) {
                                                            if (result.equals(Tags.SUCCESS.name())) {
                                                                Log.w(Tags.SUCCESS.name(), "ADDED DATA");
                                                                Toast.makeText(getApplicationContext(), "User Registration link sent!", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Failure to add data!", Toast.LENGTH_SHORT).show();
                                                                Log.w(Tags.FAILURE.name(), "FAILED TO ADD DATA");
                                                            }
                                                        }
                                                    });

//                                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                                                    Toast.makeText(getApplicationContext(), "User Registration link sent!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressBar.setVisibility(View.GONE);
                                                Log.d(TAG, "onFailure: User email not sent due to exception " + e.getMessage());
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Registration Error: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        });
    }


    @Override
    protected void onStart()
    {
        //check if user is already logged in
        super.onStart();
  
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

//    private void createUserEmailAccount(String email, String first_name, String last_name, String password, String id, String phoneNumber, boolean ownVehicle, String licenseNum)
//    {
//        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(first_name) && !TextUtils.isEmpty(last_name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(String.valueOf(id)) && !TextUtils.isEmpty(phoneNumber))
//        {
//            firebaseAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            progressBar.setVisibility(View.VISIBLE);
//                            if (task.isSuccessful()) {
//                                currentUser = firebaseAuth.getCurrentUser();
//                                assert currentUser != null;
//                                String currentUserID = currentUser.getUid();
//
//                                currentUser.sendEmailVerification()
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                progressBar.setVisibility(View.GONE);
//                                                if (task.isSuccessful()) {
//                                                    //create user map so we can add a user to user collection in firestore
//                                                    List<String> vehicles_driven = new ArrayList<String>();
//                                                    List<String> vehicles_owned = new ArrayList<String>();
//
//                                                    //change this to license number entered by user if he owns a vehicle
//                                                    if (ownVehicle) vehicles_owned.add(licenseNum);
//
//                                                    Map<String, Object> user = new HashMap<>();
//                                                    user.put("email_address", email);
//                                                    user.put("first_name", first_name);
//                                                    user.put("id_card_number", id);
//                                                    user.put("is_banned", false);
//                                                    user.put("last_name", last_name);
//                                                    user.put("phone_number", phoneNumber);
//
//                                                    db.collection("users")
//                                                            .whereIn("vehicles_owned", vehicles_owned)
//                                                            .get()
//                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                                @Override
//                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                                    if (task.isSuccessful()) {
//                                                                        Log.d(TAG, "onComplete: " + "This vehicle is already owned by someone.");
//                                                                        Toast.makeText(getApplicationContext(), "This vehicle is already owned. Please select a different one YOU own.", Toast.LENGTH_SHORT).show();
//                                                                        license_plate_et.setError("Already exists");
//                                                                        add = false;
//                                                                    }
//                                                                }
//                                                            })
//                                                            .addOnFailureListener(new OnFailureListener() {
//                                                                @Override
//                                                                public void onFailure(@NonNull Exception e) {
//                                                                    Log.d(TAG, "onFailure: " + "No document exists where the same vehicle is owned.");
//                                                                    user.put("vehicles_owned", vehicles_owned);
//
////                                                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//
//                                                                    DatabaseUtils.addData(collection, currentUserID, user, new StringCallback() {
//                                                                        public void passStringResult(String result) {
//                                                                            if (result.equals(Tags.SUCCESS.name())) {
//                                                                                Log.w(Tags.SUCCESS.name(), "ADDED DATA");
//                                                                                Toast.makeText(getApplicationContext(), "User registered!", Toast.LENGTH_SHORT).show();
//                            //                                                    UserApi userApi = UserApi.getInstance();
//                            //                                                    userApi.setUserId(currentUserID);
//                            //                                                    userApi.setUsername(first_name);
//                            //                                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                            //                                                    intent.putExtra("first_name", first_name);
//                            //                                                    intent.putExtra("userId", currentUserID);
//                                                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                                                                            } else {
//                                                                                Toast.makeText(getApplicationContext(), "Failure to add data!", Toast.LENGTH_SHORT).show();
//                                                                                Log.w(Tags.FAILURE.name(), "FAILED TO ADD DATA");
//                                                                            }
//                                                                        }
//                                                                    });
//                                                                }
//                                                            });
//                                                }
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                progressBar.setVisibility(View.GONE);
//                                                Log.d(TAG, "onFailure: User email not sent due to exception " + e.getMessage());
//                                            }
//                                        });
//                            }
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressBar.setVisibility(View.GONE);
//                            Toast.makeText(getApplicationContext(), "Registration Error: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        } else
//        {
//
//        }
//    }
}