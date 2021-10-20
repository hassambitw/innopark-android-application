package com.autobots.innopark;

import com.autobots.innopark.data.DatabaseUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.autobots.innopark.data.Driver;
import com.autobots.innopark.data.Tags;
import com.autobots.innopark.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity
{

    //widgets
    private TextView register_tv;
    private Button login_btn;
    private TextView forgot_password_tv;
    private EditText email_et;
    private EditText password_et;

    //Firebase Auth
    private FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //firestore connection
    private FirebaseFirestore db = DatabaseUtils.db;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register_tv = findViewById(R.id.id_register_text);
        login_btn = findViewById(R.id.id_login_btn);
        forgot_password_tv = findViewById(R.id.id_forgot_password_txt);
        email_et = findViewById(R.id.id_email);
        password_et = findViewById(R.id.id_password);

        register_tv.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        login_btn.setOnClickListener(view -> {
           //loginUser();
//            DatabaseUtils.db.collection("Users")
//                    .whereEqualTo("Email", "rama@gmail.com")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.passStringResult()) {
//                                    Log.d(Tags.SUCCESS.name(), document.getId() + " => " + document.getData());
//                                }
//                            } else {
//                                Log.d(Tags.FAILURE.name(), "Error getting documents: ", task.getException());
//                            }
//                        }
//                    });
            //User.getEmail("0FvlIc1YgEcen0hxtXiT08hFsxq1");
            //User.getUserUsingEmail("rama6@gmail.com");
            Driver.getDriversListDOB("J71612");
//            DocumentReference docRef = db.collection("Users").document("0FvlIc1YgEcen0hxtXiT08hFsxq1");
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.passStringResult();
//                        if (document != null && document.exists()) {
//                            Log.d("SUCCESS", document.getString("Email")); //Print the name
//                        } else {
//                            Log.d("SUCCESS", "No such document");
//                        }
//                    } else {
//                        Log.d("SUCCESS", "get failed with ", task.getException());
//                    }
//                }
//            });
//            List<Type>mArrayList = null;
//            DatabaseUtils.db.collection("users").get()
//                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot documentSnapshots) {
//                            if (documentSnapshots.isEmpty()) {
//                                Log.d(Tags.SUCCESS.name(), "onSuccess: LIST EMPTY");
//                                return;
//                            } else {
//                                // Convert the whole Query Snapshot to a list
//                                // of objects directly! No need to fetch each
//                                // document.
////                                List<Type> types = documentSnapshots.toObjects(Type.class);
////
////                                // Add all to your list
////                                mArrayList.addAll(types);
//                                Log.d(Tags.SUCCESS.name(), "onSuccess: " + mArrayList);
//                            }
//                        }
//                    })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
//                            }
//                        });
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        });

        forgot_password_tv.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
        });
    }

//    private void loginUser()
//    {
//        String email = email_et.getText().toString().trim();
//        String password = password_et.getText().toString().trim();
//
//        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
//        {
//            firebaseAuth.signInWithEmailAndPassword(email,password)
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(getApplicationContext(), "User logged in successfully", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Login error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        } else {
//            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
//        }
//    }

}