package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.autobots.innopark.activity.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.adapter.DriverRecyclerView;
import com.autobots.innopark.util.DatabaseUtils;
import com.autobots.innopark.data.models.Driver;
import com.autobots.innopark.data.api.UserApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class VehicleFragment extends Fragment {

    Toolbar toolbar;
    TextView toolbarTitle;
    Button addDriver;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Driver> driverList;

    boolean hasDrivers = false;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    String license;
    String model;

    private static final String TAG = "VehicleFragment";

    EditText licenseNum;
    EditText carModel;
    TextView emptyView;
    ProgressBar progressBar;

    ArrayList<String> driverEmails;

    //firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    public VehicleFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle, container, false);

        addDriver = view.findViewById(R.id.id_add_drivers_button);
        licenseNum = view.findViewById(R.id.id_vehicle_details_license_num);
        carModel = view.findViewById(R.id.id_vehicle_details_car_model);
        emptyView = view.findViewById(R.id.id_vehicle_details_empty_view);
        progressBar = view.findViewById(R.id.id_vehicle_details_progress_bar);
        mRecyclerView = view.findViewById(R.id.id_vehicle_drivers_recycler_view);

//        driverEmails = new ArrayList<>();

        driverList = new ArrayList<>();

        addDriver.setOnClickListener((v) -> {
            addDriverFragment();
        });



        setupToolbar(view);
//        setupRecyclerView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        UserApi userApi = UserApi.getInstance();

        getActivity().getSupportFragmentManager().setFragmentResultListener("From Vehicle List", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.d(TAG, "onFragmentResult: Inside from Vehicle List");
                license = result.getString("license");
                userApi.setIndividualLicense(license);
                licenseNum.setText(license);
                if (result.getString("model") != null) {
                    model = result.getString("model");
                    carModel.setText(model);
                } else {
                    carModel.setText("-");
                }
                loadDrivers();
            }
        });


        getActivity().getSupportFragmentManager().setFragmentResultListener("From Add Driver", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.d(TAG, "onFragmentResult: Inside from Add Vehicle");
                license = result.getString("license2");
//                userApi.setIndividualLicense(license);
                licenseNum.setText(license);
                if (result.getString("model") != null) {
                    model = result.getString("model");
                    carModel.setText(model);
                } else {
                    carModel.setText("-");
                }
                loadDrivers();
            }
        });
//        loadDrivers();
    }

    private void loadDrivers()
    {
        progressBar.setVisibility(View.VISIBLE);
        //get vehicle drivers from vehicle collection first
        db.collection("vehicles")
                .document(license)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "onSuccess: " + documentSnapshot.get("driven_by"));
                            ArrayList<Object> driverObjs = new ArrayList<>();
                            driverObjs = (ArrayList<Object>) documentSnapshot.get("driven_by");
                            if (!driverObjs.isEmpty()) {
                                Log.d(TAG, "onSuccess: Inside documentsnapshot");
                                driverEmails = (ArrayList<String>) documentSnapshot.get("driven_by");
                                //use driver email to get user info
                                for (String driverEmail : driverEmails) {
                                    Log.d(TAG, "onSuccess: Single driver email? " + driverEmail);
                                    db.collection("users")
                                            .whereEqualTo("email_address", driverEmail)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    Log.d(TAG, "onSuccess: Inside on success" + " Driver email: " + driverEmail);
                                                    if (!queryDocumentSnapshots.isEmpty()) {
                                                        progressBar.setVisibility(View.GONE);
                                                        //found user/driver document
                                                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                                        for (DocumentSnapshot snapshot : snapshotList) {
                                                            Driver driver = snapshot.toObject(Driver.class);

                                                            driverList.add(driver);
                                                            Log.d(TAG, "onSuccess: Inside for loop: " + driver.getEmail_address());
                                                        }
                                                    } else {
                                                        progressBar.setVisibility(View.GONE);
                                                        Log.d(TAG, "onSuccess: No user document found for the driver email");
                                                    }
                                                    setupRecyclerView();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressBar.setVisibility(View.GONE);
                                                    Log.d(TAG, "onFailure: User query to fetch driver info failed");
                                                }
                                            });
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Log.d(TAG, "onSuccess: Vehicle has no drivers");
                                emptyView.setVisibility(View.VISIBLE);
                            }

                        } else {
                            //vehicle document does not exist
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "onSuccess: Vehicle document does not exist");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onFailure: Vehicle query failed: " + e.getMessage());
                    }
                });
    }

    private void addDriverFragment()
    {
        AddDriverFragment fragment = new AddDriverFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_child_fragment_container_view, fragment)
                .commit();
    }


    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Vehicle Details");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });
    }

    private void setupRecyclerView()
    {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DriverRecyclerView(driverList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }
}