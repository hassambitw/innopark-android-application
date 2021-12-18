package com.autobots.innopark.activity;

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

import com.autobots.innopark.R;
import com.autobots.innopark.util.DatabaseUtils;
import com.autobots.innopark.data.Callbacks.StringCallback;
import com.autobots.innopark.data.models.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

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

    String licenseNum;
    String email;
    String firstName;
    String lastName;
    String password;
    String id;
    String phoneNum;

    public static final String TAG = "RegisterActivity";
    ProgressBar progressBar;
    boolean add = true;
    List<String> vehicles_owned;
    String tcNum;

    boolean sameID = false;

    //Firebase Auth

    private FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //firestore connection
    private FirebaseFirestore db = DatabaseUtils.db;
    private String collection = "users";

    Map<String, Object> user;
    Map<String, Object> vehicleMap;

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

        user = new HashMap<>();
        vehicleMap = new HashMap<>();

        setupRegisterAction();

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
//            List<String> vehicles_driven = new ArrayList<String>();
            Log.d(TAG, "setupRegisterAction: Before adding vehicle: " + vehicles_owned);
            checkIfValidID();
        });
    }

    private void checkIfValidID()
    {
        //in both cases where the user has or does not have a vehicle, need to check if the ID is valid
        id = id_et.getText().toString().trim();

        db.collection("users")
                .whereEqualTo("id_card_number", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.getResult().isEmpty()) {
                            //ID exists
                            Toast.makeText(getApplicationContext(), "This ID is already in use.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: Same ID exists");
                            id_et.setError("Already exists");
                        } else {
                            Log.d(TAG, "onComplete: Same ID doesn't exist");
                            if (ownVehicle) checkIfLicenseInUserCollection();
                            else getLicenseInGovtCollection();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: ID Query failed (with vehicle owned): " + e.getMessage());
            }
        });
    }

    private void createUserAccount()
    {
        email = email_et.getText().toString().trim();
        id = id_et.getText().toString().trim();
        firstName = first_name_et.getText().toString().trim();
        lastName = last_name_et.getText().toString().trim();
        phoneNum = phoneNum_et.getText().toString().trim();
        password = password_et.getText().toString().trim();

        if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(id) || !TextUtils.isEmpty(firstName) || !TextUtils.isEmpty(firstName)
                || !TextUtils.isEmpty(lastName) || !TextUtils.isEmpty(phoneNum) || !TextUtils.isEmpty(password)) {
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

                                                    ArrayList<String> vehiclesDriven = new ArrayList<>();

                                                        user.put("email_address", email);
                                                        user.put("first_name", firstName);
                                                        user.put("id_card_number", id);
                                                        user.put("is_banned", false);
                                                        user.put("last_name", lastName);
                                                        user.put("phone_number", phoneNum);
                                                        user.put("vehicles_owned", vehicles_owned);
                                                        user.put("vehicles_driven", vehiclesDriven);
                                                        user.put("traffic_code", tcNum);


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

                                                    //after adding data to user collection, check vehicle collection
                                                checkVehicleCollectionForAddedLicense();
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
        } else {
            Toast.makeText(getApplicationContext(), "Please input all fields.", Toast.LENGTH_SHORT).show();
        }


    }

    private void checkVehicleCollectionForAddedLicense()
    {
        licenseNum = license_plate_et.getText().toString().trim();

        db.collection("vehicles").document(licenseNum)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            updateVehicleDocument();
                        } else {
                            createVehicleDocument();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Vehicle collection query failed: " + e.getMessage());
                    }
                });
    }

    private void updateVehicleDocument()
    {
        licenseNum = license_plate_et.getText().toString().trim();

        vehicleMap.put("owned_by", currentUser.getEmail());

        db.collection("vehicles")
                .document(licenseNum)
                .set(vehicleMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Vehicle document updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed to update vehicle document: " + e.getMessage());
                    }
                });
    }

    private void createVehicleDocument()
    {
        licenseNum = license_plate_et.getText().toString().trim();

        FieldValue currentTime = FieldValue.serverTimestamp();

        List<String> drivenBy = new ArrayList<>();

        vehicleMap.put("added_on", currentTime);
        vehicleMap.put("city_of_registration", null);
        vehicleMap.put("driven_by", drivenBy);
        vehicleMap.put("manufacturer", null);
        vehicleMap.put("model", null);
        vehicleMap.put("owned_by", currentUser.getEmail());
        vehicleMap.put("year", null);

        db.collection("vehicles")
                .document(licenseNum)
                .set(vehicleMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Created new vehicle document for license: " + licenseNum);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed to create vehicle document: " + e.getMessage());
                    }
                });
    }

    private void checkIfLicenseInUserCollection()
    {
        licenseNum = license_plate_et.getText().toString().trim();

            vehicles_owned.add(licenseNum);
            if (vehicles_owned.size() > 1) {
                vehicles_owned.clear();
                vehicles_owned.add(licenseNum);
            }
        currentUser = firebaseAuth.getCurrentUser();
        if (ownVehicle) {
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
//                                    vehicles_owned.clear();
                                    Log.d(TAG, "onComplete: After clearing vehicles " + vehicles_owned);
                                } else {
                                    // license not found in user collection, so check if it exists in govt collection next
                                    getLicenseInGovtCollection();
                                    Log.d(TAG, "onFailure: " + "No document exists where the same vehicle is owned.");
                                }
                            }
                        }
                    });
        }


    }

    private void getLicenseInGovtCollection()
    {
        //get matching ID, then check if vehicles match

        id = id_et.getText().toString().trim();
        licenseNum = license_plate_et.getText().toString().trim();

        db.collection("government-registered-drivers")
                .whereEqualTo("id", id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot : snapshotList) {
                                tcNum = documentSnapshot.get("trafficCodeNo").toString();
                                if (!ownVehicle) createUserAccount();
                                else {
//                                String license = licensesObj.toString();
                                    ArrayList<ArrayList<String>> licenses = new ArrayList<>();
                                    licenses.add((ArrayList<String>) documentSnapshot.get("licenseNumber"));

                                    Log.d(TAG, "onSuccess: Printing licenses in govt DB: " + licenses);
                                    for (ArrayList<String> innerList : licenses) {
                                        Log.d(TAG, "onSuccess: Printing individual licenses: " + innerList);
                                        Log.d(TAG, "onSuccess: Printing license input: " + licenseNum);
                                        if (innerList.contains(licenseNum)) {
                                              createUserAccount();
//                                            checkVehicleCollectionForAddedLicense();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "You do not own this vehicle. Please input the license number of the vehicle you own.", Toast.LENGTH_SHORT).show();
                                            license_plate_et.setError("Incorrect license number");
                                        }
                                    }
                                }
                            }
                        } else {
                            if (!ownVehicle) createUserAccount();
                            else {
                                Toast.makeText(getApplicationContext(), "This ID does not belong to anybody. Please pick a valid ID.", Toast.LENGTH_SHORT).show();
                                id_et.setError("Invalid ID");
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
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
}