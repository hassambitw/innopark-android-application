package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.autobots.innopark.activity.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.util.DatabaseUtils;
import com.autobots.innopark.data.api.UserApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddDriverFragment extends Fragment {

    Toolbar toolbar;
    TextView toolbarTitle;
    EditText driverEmailET;
    Button addBtn;
    String driverEmail;
    String license;
    String userDocumentID;
    String userEmail;
    ArrayList<String> alreadyDriving;
    boolean alreadyDrives = false;
    Bundle args;

    private static final String TAG = "AddDriverFragment";

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        super.onAttach(context);

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    public AddDriverFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_driver, container, false);
        driverEmailET = view.findViewById(R.id.id_add_driver_email);
        addBtn = view.findViewById(R.id.id_add_drivers_add);

        alreadyDriving = new ArrayList<>();
        args = new Bundle();

        setupToolbar(view);

//        UserApi userApi = UserApi.getInstance();
//        license = userApi.getIndividualLicense();
//        args.putString("license", license);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        UserApi userApi = UserApi.getInstance();
        license = userApi.getIndividualLicense();

        args.putString("license2", license);
        getActivity().getSupportFragmentManager().setFragmentResult("From Add Driver", args);

        addBtn.setOnClickListener((v) -> {
            addDriver();
        });
    }

    private void addDriver()
    {
        driverEmail = driverEmailET.getText().toString().trim().toLowerCase();

        UserApi userApi = UserApi.getInstance();
        license = userApi.getIndividualLicense();

        //check if user input exists in user db
        if (!TextUtils.isEmpty(driverEmail)) {
            db.collection("users")
                    .whereEqualTo("email_address", driverEmail)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                Log.d(TAG, "onSuccess: Found user");
                                //found user, add license of car to driven_by field
                                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot snapshot : snapshotList) {
                                    //for each individual user, update driven_by field
                                    userDocumentID = snapshot.getId();
                                    userEmail = snapshot.get("email_address").toString();
                                    alreadyDriving = (ArrayList<String>) snapshot.get("vehicles_driven");
                                    for (String s : alreadyDriving) {
                                        Log.d(TAG, "onSuccess: s: " + s + " License: " + license);
                                        if (s.equals(license)) {
                                            Log.d(TAG, "onSuccess: User already drives this.");
                                            Toast.makeText(getActivity(), "This user is already a driver.", Toast.LENGTH_SHORT).show();
                                            alreadyDrives = true;
                                        }
                                    }
                                    if (alreadyDrives == false) {
                                        updateUserCollection(userDocumentID);
                                        updateVehicleCollection(userEmail);
                                    }
                                }
                            } else {
                                Toast.makeText(getActivity(), "The email you input is either invalid or does not exist in the system.", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onSuccess: User not found");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Failed user query: " + e.getMessage());
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Please input a driver email.", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUserCollection(String userDocumentID)
    {
        UserApi userApi = UserApi.getInstance();
        license = userApi.getIndividualLicense();

        db.collection("users")
                .document(userDocumentID)
                .update("vehicles_driven", FieldValue.arrayUnion(license))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //user vehicles driven array updated
                        Log.d(TAG, "onSuccess: User collection updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: User update query failed: " + e.getMessage());
                    }
                });
    }

    private void updateVehicleCollection(String userEmail)
    {
        UserApi userApi = UserApi.getInstance();
        license = userApi.getIndividualLicense();

        db.collection("vehicles")
                .document(license)
                .update("driven_by", FieldValue.arrayUnion(userEmail))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Vehicle collection updated");
                        Toast.makeText(getActivity(), "Driver added.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Vehicle update query failed: " + e.getMessage());
                    }
                });
    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Add Driver");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });
    }
}
