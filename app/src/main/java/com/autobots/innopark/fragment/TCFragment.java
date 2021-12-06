package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.UserApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.LogDescriptor;
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

public class TCFragment extends Fragment
{
    Toolbar toolbar;
    TextView toolbarTitle;

    private static final String TAG = "TCFragment";

    EditText tcInputET;
    ProgressBar progressBar;
    Button verifyButton;
    String license;
    String tcNum;
    Map<String, Object> userMap;
    Map<String, Object> vehicleMap;
    String tc;

    boolean tracker;
    Bundle bundle;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        View view = inflater.inflate(R.layout.fragment_tc, container, false);

        tcInputET = view.findViewById(R.id.id_tc_number_input);
        verifyButton = view.findViewById(R.id.id_tc_number_verify);
        progressBar = view.findViewById(R.id.id_tc_progress_bar);

        userMap = new HashMap<>();
        vehicleMap = new HashMap<>();

        bundle = new Bundle();

        setupToolbar(view);

        getActivity().getSupportFragmentManager().setFragmentResultListener("requestKeyFromAddVehicle", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                license = result.getString("license");
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


        verifyButton.setOnClickListener((v) -> {
            verifyTCNumber();
        });
    }

    private void verifyTCNumber()
    {
        UserApi userApi = UserApi.getInstance();
        String userId = userApi.getUserId();
        tcNum = userApi.getTcNum();

        tc = tcInputET.getText().toString().trim();


        if (tcNum != null){
            Log.d(TAG, "verifyTCNumber: " + tcNum);
            Log.d(TAG, "verifyTCNumber: " + currentUser.getEmail());
            progressBar.setVisibility(View.VISIBLE);
            db.collection("users")
                    .whereEqualTo("email_address", currentUser.getEmail())
                    .whereEqualTo("traffic_code", tc)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                checkVehicleInGovtCollection();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "This traffic code number does not belong to you or is invalid. Please check again.", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onSuccess: TC not found in user profile");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "onFailure: User query failed: " + e.getMessage());
                        }
                    });

        } else {
            //user is first time adding a vehicle
            //jump straight to see if vehicle in govt collection
            checkVehicleInGovtCollection();
        }
    }

    private void checkVehicleInGovtCollection()
    {
        db.collection("government-registered-drivers")
                .whereEqualTo("trafficCodeNo", tc)
                .whereArrayContains("licenseNumber", license)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            //found license in govt collection, input is valid, so add to user collection and update vehicle collection
                            updateUserCollection();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Your TC number does not associate with the license number that you've added. Please double check the license number.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onSuccess: User's TC number does not contain vehicle.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed govt-vehicle query: " + e.getMessage());
                    }
                });
    }

    private void updateUserCollection()
    {

//        userMap.put("vehicles_owned", tc);
        UserApi userApi = UserApi.getInstance();


        db.collection("users")
                .document(currentUser.getUid())
                .update("vehicles_owned", FieldValue.arrayUnion(license))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //user doc updated, next check vehicle collection
                        checkVehicleCollectionForAddedLicense();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onFailure: Failed to update user document: " + e.getMessage());
                    }
                });
    }

    private void checkVehicleCollectionForAddedLicense()
    {

        db.collection("vehicles").document(license)
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
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onFailure: Vehicle collection query failed: " + e.getMessage());
                    }
                });
    }

    private void createVehicleDocument()
    {
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
                .document(license)
                .set(vehicleMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onSuccess: Created new vehicle document for license: " + license);
                        bundle.putString("TC", "Added car");
                        getActivity().getSupportFragmentManager().setFragmentResult("requestKeyFromTC", bundle);
                        loadVehicleListFragment();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onFailure: Failed to create vehicle document: " + e.getMessage());

                    }
                });
    }

    private void updateVehicleDocument()
    {
        vehicleMap.put("owned_by", currentUser.getEmail());

        db.collection("vehicles")
                .document(license)
                .set(vehicleMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onSuccess: Vehicle document updated");
                        bundle.putString("TC", "Added car");
                        getActivity().getSupportFragmentManager().setFragmentResult("requestKeyFromTC", bundle);
                        loadVehicleListFragment();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onFailure: Failed to update vehicle document: " + e.getMessage());
                    }
                });
    }

    private void loadVehicleListFragment()
    {
        Fragment selectedFragment = new VehicleListFragment();

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
        toolbarTitle.setText("Traffic Code");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

}
