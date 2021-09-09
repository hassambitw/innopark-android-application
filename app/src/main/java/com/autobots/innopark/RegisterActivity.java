package com.autobots.innopark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

   //widgets
    private Button register_btn;
    private RadioGroup radio_grp_vehicle;
    private RadioButton radio_btn_yes;
    private RadioButton radio_btn_no;
    private EditText license_plate_et;
    private EditText email_et;
    private EditText username_et;
    private EditText password_et;
    private EditText id_et;
    private EditText phoneNum_et;
    private EditText ccName_et;
    private EditText ccNum_et;
    private EditText ccExpiry_et;
    private EditText ccCVV_et;

    //Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

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
        username_et = findViewById(R.id.id_register_username);
        password_et = findViewById(R.id.id_register_password);
        id_et = findViewById(R.id.id_register_id);
        phoneNum_et = findViewById(R.id.id_register_phone);
        ccName_et = findViewById(R.id.id_register_ccname);
        ccNum_et = findViewById(R.id.id_register_ccnum);
        ccExpiry_et = findViewById(R.id.id_register_ccexpiry);
        ccCVV_et = findViewById(R.id.id_register_cc_cvv);


        firebaseAuth = FirebaseAuth.getInstance();


        register_btn.setOnClickListener(view -> {
            String email = email_et.getText().toString().trim();
            String username = username_et.getText().toString().trim();
            String password  = password_et.getText().toString().trim();
            long id = Long.parseLong(id_et.getText().toString().trim()); // string to long
            String phoneNumber = phoneNum_et.getText().toString().trim();
            String ccName = ccName_et.getText().toString().trim();
            long ccNum = Long.parseLong(ccNum_et.getText().toString().trim());
            String ccExpiry = ccExpiry_et.getText().toString().trim();
            int ccCVV = Integer.parseInt(ccCVV_et.getText().toString().trim());
            String licenseNum = license_plate_et.getText().toString().trim();


            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(String.valueOf(id)) && !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(phoneNumber)
                && !TextUtils.isEmpty(ccName) && !TextUtils.isEmpty(String.valueOf(ccNum)) && !TextUtils.isEmpty(ccExpiry) && !TextUtils.isEmpty(String.valueOf(ccCVV)))
            {
                createUserEmailAccount(email, username, password, id, phoneNumber, ccName, ccNum, ccExpiry, ccCVV, ownVehicle);
                Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "All fields must be filled!", Toast.LENGTH_SHORT).show();
            }
        });

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
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        //firebaseAuth.addAuthStateListener(authStateListener);

    }

    private void createUserEmailAccount(String email, String username, String password, long id, String phoneNumber, String ccName, long ccNum, String ccExpiry, int ccCVV, boolean ownVehicle)
    {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(String.valueOf(id)) && !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(ccName)
            && !TextUtils.isEmpty(String.valueOf(ccNum)) && !TextUtils.isEmpty(ccExpiry) && !TextUtils.isEmpty(String.valueOf(ccCVV)))
        {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // take user to loginActivity

                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                String currentUserID = currentUser.getUid();

                                //create user map so we can add a user to user collection in firestore
                                Map<String, String> userObject = new HashMap<>();
                                userObject.put("UserID", currentUserID);
                                userObject.put("Username", username);
                                userObject.put("Email", email);
                                userObject.put("Password", password);
                                userObject.put("Phone Number", phoneNumber);
                                userObject.put("Credit Card Name", ccName);
                                userObject.put("Credit Card Number", String.valueOf(ccNum));
                                userObject.put("Credit Card Expiry", ccExpiry);
                                userObject.put("Credit Card CVV", String.valueOf(ccCVV));
                                userObject.put("Own Vehicle", String.valueOf(ownVehicle));
                                //if (ownVehicle) userObject.put("License Number", licenseNum);


                                //save to firestore db
                                collectionReference.add(userObject)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (Objects.requireNonNull(task).getResult().exists()) {
                                                                    String name = task.getResult()
                                                                            .getString("username");

                                                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                                    intent.putExtra("username", name);
                                                                    intent.putExtra("userId", currentUserID);

                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Adding to DB Error: " +  e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Registration Error: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else
        {

        }
    }
}