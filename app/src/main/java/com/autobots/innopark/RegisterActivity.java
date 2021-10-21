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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.Callbacks.StringCallback;
import com.autobots.innopark.data.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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
    private EditText ccName_et;
    private EditText ccNum_et;
    private EditText ccExpiry_et;
    private EditText ccCVV_et;

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
        first_name_et = findViewById(R.id.id_register_first_name);
        last_name_et = findViewById(R.id.id_register_last_name);
        password_et = findViewById(R.id.id_register_password);
        id_et = findViewById(R.id.id_register_id);
        phoneNum_et = findViewById(R.id.id_register_phone);


        register_btn.setOnClickListener(view -> {
            String email = email_et.getText().toString().trim();
            String first_name = first_name_et.getText().toString().trim();
            String last_name = last_name_et.getText().toString().trim();
            String password  = password_et.getText().toString().trim();
            String id = id_et.getText().toString().trim(); // string to long
            String phoneNumber = phoneNum_et.getText().toString().trim();
            String licenseNum = license_plate_et.getText().toString().trim();


            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(first_name) && !TextUtils.isEmpty(last_name)&& !TextUtils.isEmpty(password) && !TextUtils.isEmpty(String.valueOf(id))
                    && !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(phoneNumber))
            {
                createUserEmailAccount(email, first_name, last_name, password, id, phoneNumber, ownVehicle);
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

    private void createUserEmailAccount(String email, String first_name, String last_name, String password, String id, String phoneNumber, boolean ownVehicle)
    {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(first_name) && !TextUtils.isEmpty(last_name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(String.valueOf(id)) && !TextUtils.isEmpty(phoneNumber))
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

//                                String vehicle = "11222333";
//                                User user = new User(currentUserID, email, password, first_name,
//                                        last_name, id, phoneNumber, vehicle);
//
//                                user.addUser();

                                //create user map so we can add a user to user collection in firestore
                                List<String> vehicles_driven=new ArrayList<String>();
                                List<String> vehicles_owned=new ArrayList<String>();

                                //change this to license number entered by user if he owns a vehicle
                                vehicles_owned.add("12345");

                                Map<String, Object> user = new HashMap<>();
                                user.put("email_address", email);
                                user.put("first_name", first_name);
                                user.put("id_card_number", id);
                                user.put("is_banned", false);
                                user.put("last_name", last_name);
                                user.put("password", password);
                                user.put("phone_number", phoneNumber);
                                user.put("vehicles_driven", vehicles_driven);
                                user.put("vehicles_owned", vehicles_owned);
                                //if (ownVehicle) userObject.put("License Number", licenseNum);

                                DatabaseUtils.addData(collection, currentUserID, user, new StringCallback(){
                                        public void passStringResult(String result){
                                                if(result.equals(Tags.SUCCESS.name())){
                                                    Log.w(Tags.SUCCESS.name(), "ADDED DATA");

                                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                    intent.putExtra("first_name", first_name);
                                                    intent.putExtra("userId", currentUserID);

                                                    startActivity(intent);
                                                }else{
                                                    Log.w(Tags.FAILURE.name(), "FAILED TO ADD DATA");
                                                }
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