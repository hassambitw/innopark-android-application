package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.autobots.innopark.activity.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.util.DatabaseUtils;
import com.autobots.innopark.data.models.Driver;
import com.autobots.innopark.data.api.UserApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddVehicleFragment extends Fragment {

    Toolbar toolbar;
    TextView toolbarTitle;
    Button addDriver;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Driver> driverList;

    private static final String TAG = "AddVehicleFragment";

    Button addLicense;
    EditText licenseET;
    ProgressBar progressBar;
    Bundle args;

    String newLicense;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    boolean tracker;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);

        addLicense = view.findViewById(R.id.id_add_vehicle_add);
        licenseET = view.findViewById(R.id.id_add_vehicle_license_num);
        progressBar = view.findViewById(R.id.id_add_vehicle_progress_bar);

        args = new Bundle();


        setupToolbar(view);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        addLicense.setOnClickListener((v) -> {
            processLicenseInput();
        });
    }

    private void processLicenseInput()
    {
        progressBar.setVisibility(View.VISIBLE);
        UserApi userApi = UserApi.getInstance();
        newLicense = licenseET.getText().toString().toUpperCase().trim();

        // need to check if license exists in user collection or not

        db.collection("users")
                .whereArrayContains("vehicles_owned", newLicense)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        progressBar.setVisibility(View.GONE);
                        if (!queryDocumentSnapshots.isEmpty()) {
                            //car found in someone's document
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "This vehicle is already owned by someone else.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onSuccess: Vehicle already exists in user collection");
                        } else {
                              checkLicenseInGovtCollection();
//                            startTCFragment();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onFailure: Query failed: " + e.getMessage());     
                    }
                });
    }

    private void checkLicenseInGovtCollection()
    {

        db.collection("government-registered-drivers")
                .whereArrayContains("licenseNumber", newLicense)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.GONE);
                        if (!queryDocumentSnapshots.isEmpty()) {
                            //license exists in govt db, proceed to tc frag
                            args.putString("license", newLicense);
                            getActivity().getSupportFragmentManager().setFragmentResult("requestKeyFromAddVehicle", args);
                            tracker = true;
                            startTCFragment();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "This license plate does not belong to anyone. Please input a valid plate!", Toast.LENGTH_SHORT).show();
                            licenseET.setError("Invalid license");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onFailure: License query failed: " + e.getMessage());
                    }
                });
    }

    private void startTCFragment() 
    {
        Fragment selectedFragment = new TCFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .addToBackStack(null)
                .replace(R.id.id_child_fragment_container_view, selectedFragment)
                .commit();
    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Add Vehicle");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }






}