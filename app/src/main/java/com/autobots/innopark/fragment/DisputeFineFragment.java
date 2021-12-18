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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.autobots.innopark.activity.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.util.DatabaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class DisputeFineFragment extends Fragment
{

    Toolbar toolbar;
    TextView toolbarTitle;

    EditText fineTypeET;
    String fineType;
    EditText fineAmountET;
    double fineAmount;
    String fineId;
    String license;
    EditText disputeET;

    Map<String, Object> disputeFineMap;
    Map<String, Object> finesMap;

    private static final String TAG = "DisputeFineFragment";

    Button submitBtn;
    String parentId;

    Bundle args;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;
    ProgressBar progressBar;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_dispute_fine, container, false);

        fineAmountET = view.findViewById(R.id.id_dispute_fine_fine_amount);
        fineTypeET = view.findViewById(R.id.id_dispute_fine_fine_type);
        submitBtn = view.findViewById(R.id.id_dispute_fine_submit);
        disputeET = view.findViewById(R.id.id_dispute_fine_message);
        progressBar = view.findViewById(R.id.id_dispute_fine_progress_bar);

        disputeFineMap = new HashMap<>();
        finesMap = new HashMap<>();

        setupToolbar(view);

        args = new Bundle();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().getSupportFragmentManager().setFragmentResultListener("From Fine Fragment", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                fineType = result.getString("fineType");
                fineTypeET.setText(fineType);
                fineAmount = result.getDouble("fineAmount");
                fineAmountET.setText(fineAmount + "");
                fineId = result.getString("fineId");
                license = result.getString("license");
                parentId = result.getString("parentId");

            }
        });

        submitBtn.setOnClickListener((v) -> {
            disputeFine();
        });

    }

    private void disputeFine()
    {
        //creates a disputed fine document in the subcollection
        //it has fine ID, created datetime, is_accepted, is_reviewed, vehicle,
        progressBar.setVisibility(View.VISIBLE);

        String disputeMessage = disputeET.getText().toString().trim();
        FieldValue currentTime = FieldValue.serverTimestamp();

        if (!TextUtils.isEmpty(disputeMessage)) {
            disputeFineMap.put("created_datetime", currentTime);
            disputeFineMap.put("dispute_description", disputeMessage);
            disputeFineMap.put("is_accepted", false);
            disputeFineMap.put("is_reviewed", false);
            disputeFineMap.put("fine_id", fineId);
            disputeFineMap.put("vehicle", license);

            DocumentReference newDisputedFineRef = db.collection("cities").document();

            db.collection("avenues")
                    .document(parentId)
                    .collection("disputed_fines")
                    .add(disputeFineMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            updateFinesSubcollection();
                            Log.d(TAG, "onSuccess: Added data");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "onFailure: Failed query: " + e.getMessage());
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Please input your dispute message.", Toast.LENGTH_SHORT).show();
        }



    }

    private void updateFinesSubcollection()
    {
        finesMap.put("is_disputed", true);

        db.collection("avenues")
                .document(parentId)
                .collection("fines_info")
                .document(fineId)
                .set(finesMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Your dispute has been recorded", Toast.LENGTH_SHORT).show();
                        goToFinesTab();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onFailure: Failed to update fines document: " + e.getMessage());
                    }
                });
    }

    private void goToFinesTab()
    {
        Fragment fragment = new ParkingFragment();

        args.putString("FinesDefault", "1");

        getActivity().getSupportFragmentManager().setFragmentResult("From Dispute Fine", args);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Dispute Fine");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
