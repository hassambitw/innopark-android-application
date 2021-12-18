package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autobots.innopark.activity.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.adapter.FinesRecyclerViewAdapter;
import com.autobots.innopark.util.DatabaseUtils;
import com.autobots.innopark.data.models.Fine;
import com.autobots.innopark.data.api.UserApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FinesListFragment extends Fragment implements FinesRecyclerViewAdapter.OnFineClickListener
{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView paidFines;
    private ArrayList<Fine> fineItems;
    ArrayList<String> vehiclesOwned;
    TextView emptyView;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;
    //firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "FinesListFragment";
    Bundle args;
    String license;
    Double fineAmount;
    boolean status;
    String violationType;
    String footage;
    Date dueDate;
    String paymentLink;

    String fineID;
    String parentDocumentId;

//    OnPassData onPassData;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

//        try {
//            onPassData = (OnPassData) context;
//        } catch(ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
//        }
    }

    public FinesListFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fines_list, container, false);
        paidFines = view.findViewById(R.id.id_fine_view_previous_fine);
        mRecyclerView = view.findViewById(R.id.id_recycler_view_unpaid_fines);
        emptyView = view.findViewById(R.id.id_unpaid_fines_empty_view);

        args = new Bundle();

        fineItems = new ArrayList<>();
        vehiclesOwned = new ArrayList<>();

        paidFines.setOnClickListener((v) -> {
            startPaidFinesFragment();
        });

//        setupRecyclerView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadUnpaidFines();
    }

    private void loadUnpaidFines()
    {

        UserApi userApi = UserApi.getInstance();
        vehiclesOwned = (ArrayList<String>) userApi.getVehiclesOwned();

        if (!vehiclesOwned.isEmpty()) {
            db.collectionGroup("fines_info")
                    .whereEqualTo("is_accepted", true)
                    .whereIn("vehicle", vehiclesOwned)
                    .orderBy("created_datetime", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                Log.d(TAG, "onSuccess: Found fines");
                                //found fines
                                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot snapshot : snapshotList) {
                                    Fine fine = snapshot.toObject(Fine.class);
                                    fineID = snapshot.getId();
                                    Log.d(TAG, "onSuccess: " + fineID + " ");
                                    parentDocumentId = String.valueOf(snapshot.getReference().getParent().getParent().getId());
                                    fine.setParentDocumentId(parentDocumentId);
                                    fine.setFineID(fineID);
//                                    Log.d(TAG, "onSuccess: Fine due date: " + fine.getDue_datetime() + " Fine avenue: " + fine.getAvenue_name() + " Fine amount: " + fine.getFine_amount());
                                    fineItems.add(fine);


                                }
                                setupRecyclerView();
                            } else {
                                Log.d(TAG, "onSuccess: No fines");
                                emptyView.setVisibility(View.VISIBLE);

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Failed to load fines: " + e.getMessage());
                            emptyView.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            Log.d(TAG, "loadUnpaidFines: No vehicle owned");
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView()
    {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FinesRecyclerViewAdapter(fineItems, getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
    }


    private void startPaidFinesFragment()
    {
        Fragment fragment = new PaidFinesFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    @Override
    public void onFineClick(int position)
    {
        Fragment selectedFragment = new FineFragment();

        Fine fines = fineItems.get(position);

        license = fines.getVehicle();
        fineAmount = fines.getFine_amount();
        status = fines.isIs_paid();
        violationType = fines.getFine_type();
        if (fines.getFootage().isEmpty()) {
            footage = "Null";
        } else {
            footage = fines.getFootage();
//            onPassData.sendString(footage);
        }
        String fineId = fines.getFineID();
        String fineDescription = fines.getFine_description();
        String parentDocumentId = fines.getParentDocumentId();

        paymentLink = fines.getPayment_link();


        Log.d(TAG, "onSuccess: " + footage);
        dueDate = fines.getDue_datetime();
//                                    Log.d(TAG, "onSuccess: " + dueDate);

        args.putString("license", license);
        args.putDouble("fineAmount", fineAmount);
        args.putBoolean("status", status);
        args.putString("violationType", violationType);
        args.putString("footage", footage);
        args.putSerializable("dueDate", dueDate);
        args.putString("fineId", fineId);
        args.putString("fineDescription", fineDescription);
        args.putString("parentId", parentDocumentId);

        args.putString("paymentLink", paymentLink);

        getActivity().getSupportFragmentManager().setFragmentResult("From Fine List", args);


        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .addToBackStack(null)
                .replace(R.id.id_fragment_container_view, selectedFragment)
                .commit();
    }

//    public interface OnPassData {
//        public void sendString(String s);
//    }
}