package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.adapter.FineHistoryRecyclerViewAdapter;
import com.autobots.innopark.adapter.ParkingHistoryRecyclerViewAdapter;
import com.autobots.innopark.adapter.VehicleRecyclerViewAdapter;
import com.autobots.innopark.data.Callbacks.HashmapCallback;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.ParkingHistoryData;
import com.autobots.innopark.data.Session;
import com.autobots.innopark.data.User;
import com.autobots.innopark.data.UserApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ParkingHistoryFragment extends Fragment {

    private static final String TAG = "ParkingHistoryFragment";
    Toolbar toolbar;
    TextView toolbarTitle;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ParkingHistoryData> parkingHistoryDataList;
    private List<String> vehiclesCombined;
    private ArrayList<Session> parkingList;

    ParkingHistoryData data;
    Session data2;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    //firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("avenues");

    Session tariff;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
{
        // Inflate the layout for this fragment

        String strMessage = getArguments().getString("Message");

        View view = inflater.inflate(R.layout.fragment_parking_history, container, false);
        parkingHistoryDataList = new ArrayList<ParkingHistoryData>();
        vehiclesCombined = new ArrayList<>();
        parkingList = new ArrayList<>();

        mRecyclerView = view.findViewById(R.id.id_parking_history_recycler_view);

//        setupData();

        if (strMessage.toLowerCase().contains("from parking history card")) {
            setupPHToolbar(view);
            setupRecyclerView();
        }
        else if (strMessage.toLowerCase().contains("from fine history card")) {
            setupFHToolbar(view);
            //setupFineRecyclerView(view);
        }


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadParkingHistoryData();
    }

    private void loadParkingHistoryData()
    {

        UserApi userApi = UserApi.getInstance();
        String email = userApi.getUserEmail();
        String currentUserUid = currentUser.getUid();
        vehiclesCombined = userApi.getVehiclesCombined();

//        User.getUser(email, new HashmapCallback() {
//            @Override
//            public void passHashmapResult(HashMap<String, Object> result) {
//                if (!result.isEmpty()) {
//
//                }
//            }
//        });


        db.collectionGroup("sessions_info")
                .whereNotEqualTo("end_datetime", null)
                .whereIn("vehicle", vehiclesCombined)
                .orderBy("end_datetime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: Inside");
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {
                                try {
                                    Log.d(TAG, "onSuccess: inside document loop");
                                    data2 = snapshot.toObject(Session.class);
                                    parkingList.add(data2);
                                } catch (Exception e) {
                                    Log.d(TAG, "onSuccess: " + e.getMessage());
                                }
                            }
                            setupRecyclerView();
                        } else {
                            Log.d(TAG, "onSuccess: Query document snapshots empty");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });

//        Task userQuery =
//                db.collection("vehicles")
//                .whereEqualTo("owned_by", email)
//                .get();
//
//        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(subCollectionQuery, userQuery);
//
//        allTasks.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
//            @Override
//            public void onSuccess(List<QuerySnapshot> querySnapshots) {
//                for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
//                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                        data = documentSnapshot.toObject(ParkingHistoryData.class);
//                        data.setModel(documentSnapshot.getString("model"));
//                        Log.d(TAG, "onSuccess: " + data.getModel() + " " + data.getAvenue_name());
//                    }
//                    parkingHistoryDataList.add(data);
//                }
//                setupRecyclerView();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "onFailure: " + e.getMessage());
//            }
//        });
    }

    private void setupFHToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_parking_history_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Fine History");

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
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new ParkingHistoryRecyclerViewAdapter(parkingList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

//    private void setupFineRecyclerView(View view)
//    {
//        mRecyclerView = view.findViewById(R.id.id_parking_history_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
//        mAdapter = new FineHistoryRecyclerViewAdapter(parkingHistoryDataList, getActivity());
//        mRecyclerView.setAdapter(mAdapter);
//    }

    private void setupPHToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_parking_history_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Parking History");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}