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
import android.widget.TextView;

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.adapter.VehicleRecyclerViewAdapter;
import com.autobots.innopark.data.Callbacks.HashmapCallback;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.User;
import com.autobots.innopark.data.UserApi;
import com.autobots.innopark.data.Vehicle;
import com.autobots.innopark.data.Vehicle2;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class VehicleListFragment extends Fragment implements VehicleRecyclerViewAdapter.OnVehicleClickListener {

    Toolbar toolbar;
    TextView toolbarTitle;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final String TAG = "VehicleListFragment";

    private ArrayList<String> vehiclesOwned;
    private TextView addVehicle;
    TextView emptyView;
    ArrayList<String> driverEmails;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    Vehicle vehicle;

    ArrayList<Vehicle> vehicleList;

    Vehicle2 vehicle2;

    ArrayList<Vehicle2> vehicleList2;

    boolean innerQuery = false;

    String strMessage;

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

    public VehicleListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_list, container, false);

        addVehicle = view.findViewById(R.id.id_vehicles_add_vehicle);
        emptyView = view.findViewById(R.id.id_vehicle_list_empty_view);
        mRecyclerView = view.findViewById(R.id.id_vehicles_recycler_view);


        vehiclesOwned = new ArrayList<>();
        vehicleList = new ArrayList<>();
        vehicleList2 = new ArrayList<>();
        driverEmails = new ArrayList<String>();

        addVehicle.setOnClickListener((v) -> {
            addVehicleFragment();
        });

        getActivity().getSupportFragmentManager().setFragmentResultListener("requestKeyFromTC", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.d(TAG, "onFragmentResult: Inside");
                strMessage = result.getString("TC");

                setupToolbar(view);
            }
        });

        setupNormalToolbar(view);
//        setupRecyclerView(view);

        return view;
    }

    private void setupNormalToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Vehicles");
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: " + strMessage);
        getCurrentVehiclesOwned();
//        loadVehicles();
    }

    private void getCurrentVehiclesOwned()
    {
        User.getUser(currentUser.getEmail(), new HashmapCallback() {
            @Override
            public void passHashmapResult(HashMap<String, Object> result) {
                UserApi userApi = UserApi.getInstance();
                ArrayList<String> currentVehiclesOwned = (ArrayList<String>) result.get("vehicles_owned");
                userApi.setVehiclesOwned(currentVehiclesOwned);
                loadVehicles();

            }
        });
    }

    private void loadVehicles()
    {
        UserApi userApi = UserApi.getInstance();
        vehiclesOwned = (ArrayList<String>) userApi.getVehiclesOwned();
        Log.d(TAG, "loadVehicles: Vehicles owned: " + vehiclesOwned);

        if (!vehiclesOwned.isEmpty()) {
            db.collection("vehicles")
                    .whereIn(FieldPath.documentId(), vehiclesOwned)
                    .whereEqualTo("owned_by", currentUser.getEmail())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot snapshot : snapshotList) {
                                    Log.d(TAG, "onSuccess: " + snapshot.getId());
                                    vehicle2 = snapshot.toObject(Vehicle2.class);

                                    String documentId = snapshot.getId();
                                    vehicle2.setDocumentId(documentId);

                                    vehicleList2.add(vehicle2);

                                    Log.d(TAG, "onSuccess: " + vehicle2.getDriven_by().toString());


//                                    db.collection("users")
//                                            .whereIn("email_address", vehicle2.getDriven_by())
//                                            .get()
//                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                                @Override
//                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                                    if (!queryDocumentSnapshots.isEmpty()) {
//                                                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
//                                                        for (DocumentSnapshot snapshot : snapshotList) {
//                                                            String driverFirstName = snapshot.get("first_name").toString();
//                                                            String driverLastName = snapshot.get("last_name").toString();
//                                                            String driverName = driverFirstName + " " + driverLastName;
//
//                                                            driverEmails.add(driverName);
////                                                            setupRecyclerView();
//                                                            Log.d(TAG, "onSuccess: Inside loop: " + driverEmails);
//
////
//                                                        }
//                                                        setupRecyclerView();
//                                                    } else {
//                                                        Log.d(TAG, "No drivers with that email found!");
//                                                    }
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Log.d(TAG, "onFailure: Driven by query failed: " + e.getMessage());
//                                                }
//                                            });

//                                    vehicle2.setDriverName(driverEmails.get(0));
//                                    Log.d(TAG, "onSuccess: Outside loop: " + driverEmails);

//                                   if (!driverEmails.isEmpty())

//                                    Log.d(TAG, "onSuccess: Driver emails: " + driverEmails);


//                                    String drivers = vehicle2.getDriven_by().toString();
//                                    driverEmails.add(drivers);
                                }
//                                Log.d(TAG, "onSuccess: DriverEmails: " + driverEmails);
//                                String[] arr = (String[]) driverEmails.toArray();
//                                Log.d(TAG, "onSuccess: " + arr);

                            } else {
                                Log.d(TAG, "onSuccess: No documents found");
                                emptyView.setVisibility(View.VISIBLE);
                            }
                            setupRecyclerView();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Query failed: " + e.getMessage());
                        }
                    });
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }





    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Vehicles");

    }

    private void setupRecyclerView()
    {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new VehicleRecyclerViewAdapter(vehicleList2, driverEmails, getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onVehicleClick(int position)
    {
        Fragment selectedFragment = new VehicleFragment();

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .addToBackStack(null)
                .replace(R.id.id_child_fragment_container_view, selectedFragment)
                .commit();
    }

    private void addVehicleFragment()
    {
        Fragment selectedFragment = new AddVehicleFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .addToBackStack(null)
                .replace(R.id.id_child_fragment_container_view, selectedFragment)
                .commit();
    }
}