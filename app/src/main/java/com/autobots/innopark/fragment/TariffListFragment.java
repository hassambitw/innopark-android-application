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


import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.adapter.Avenue;
import com.autobots.innopark.adapter.TariffActiveSessionRecyclerViewAdapter;
import com.autobots.innopark.adapter.TariffInactiveSessionRecyclerViewAdapter;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.Session;
import com.autobots.innopark.data.UserApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TariffListFragment extends Fragment implements TariffActiveSessionRecyclerViewAdapter.OnTariffClickListener, TariffInactiveSessionRecyclerViewAdapter.OnTariffClickListener {

    private static final String TAG = "TariffListFragment";
    private RecyclerView mRecyclerViewInactive;
    private RecyclerView mRecyclerViewActive;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView paidTariff;
    private TextView unpaidTariff;
    private ArrayList<Session> unpaidTariffItems;
    private ArrayList<Session> activeTariffItems;
    ArrayList<Avenue> avenueItems;
    private ArrayList<Session> tariffItems2;
    private ArrayList<Session> finalTariff;

    private List<String> vehiclesCombined;

    String parent_id;

    Bundle args2;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    //firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("avenues");

    Session tariff;

    public TariffListFragment()
    {
    }

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
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tariff_list, container, false);

        paidTariff = view.findViewById(R.id.id_tariff_view_previous_tariff);
        unpaidTariff = view.findViewById(R.id.id_tariff_list_view_unpaid_sessions);

        mRecyclerViewActive = view.findViewById(R.id.id_recycler_view_tariff);
        mRecyclerViewInactive = view.findViewById(R.id.id_recycler_view_tariff_2);

       args2 = new Bundle();

        vehiclesCombined = new ArrayList<>();
        unpaidTariffItems = new ArrayList<>();
        activeTariffItems = new ArrayList<>();
        tariffItems2 = new ArrayList<>();
        //finalTariff = new ArrayList<>();

        paidTariff.setOnClickListener(v -> {
            startPaidTariffFragment();
        });

        unpaidTariff.setOnClickListener((v) -> {
            startUnpaidTariffFragment();
        });


        //populateTariffs();
        //setupActiveRecyclerView();
        //setupInactiveRecyclerView();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadActiveData();
        loadUnpaidData();
    }

    private void loadActiveData()
    {
        UserApi userApi = UserApi.getInstance();
        vehiclesCombined = userApi.getVehiclesCombined();

        db.collectionGroup("sessions_info")
                .whereEqualTo("end_datetime", null)
                .whereIn("vehicle", vehiclesCombined)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "onSuccess: Inside");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {
                                try {
                                    Log.d(TAG, "onSuccess: Document ID of Active: " + snapshot.getId());
                                    tariff = snapshot.toObject(Session.class);
                                    activeTariffItems.add(tariff);

                                } catch (Exception e) {
                                    Log.d(TAG, "onSuccess: " + e.getMessage());
                                }

                                parent_id = String.valueOf(snapshot.getReference().getParent().getParent().getId()); // parent doc id
                                Log.d(TAG, "onSuccess: Parent Doc ID of Active: " + parent_id);

                            }
                            setupActiveRecyclerView();

                        } else {
                            Log.d(TAG, "onSuccess: Query document snapshots empty");
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

    private void loadUnpaidData()
    {

        UserApi userApi = UserApi.getInstance();
        String email = userApi.getUserEmail();
        String currentUserUid = currentUser.getUid();
        vehiclesCombined = userApi.getVehiclesCombined();

        db.collectionGroup("sessions_info")
                .whereNotEqualTo("end_datetime", null)
                .whereEqualTo("is_paid", false)
                .whereIn("vehicle", vehiclesCombined)
                .limit(3)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "onSuccess: Inside");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {
                                try {
                                    Log.d(TAG, "onSuccess: Document ID of Unpaid: " + snapshot.getId());
                                    tariff = snapshot.toObject(Session.class);
                                    unpaidTariffItems.add(tariff);

                                } catch (Exception e) {
                                    Log.d(TAG, "onSuccess: " + e.getMessage());
                                }

                                parent_id = String.valueOf(snapshot.getReference().getParent().getParent().getId()); // parent doc id
                                Log.d(TAG, "onSuccess: Parent Doc ID of Unpaid: " + parent_id);

                            }
                            setupInactiveRecyclerView();

                        } else {
                            Log.d(TAG, "onSuccess: Query document snapshots empty");
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

    private void startUnpaidTariffFragment()
    {
        Fragment fragment = new UnpaidTariffFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    private void startCurrentSessionFragment()
    {
        Fragment fragment = new CurrentSessionFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    private void setupActiveRecyclerView()
    {
//        mRecyclerView = view.findViewById(R.id.id_recycler_view_tariff);
        mRecyclerViewActive.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewActive.setLayoutManager(mLayoutManager);
        mAdapter = new TariffActiveSessionRecyclerViewAdapter(activeTariffItems, getActivity(), this);
        mRecyclerViewActive.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void setupInactiveRecyclerView()
    {
//        mRecyclerViewInactive = view.findViewById(R.id.id_recycler_view_tariff_2);
        mRecyclerViewInactive.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewInactive.setLayoutManager(mLayoutManager);
//        ArrayList<Session> tempList = new ArrayList<>();
//        if (tariffItems.size() >= 3) {
//            tempList.addAll(tariffItems.subList(0, 3));
//        } else {
//            tempList.addAll(tariffItems);
//        }
        mAdapter = new TariffInactiveSessionRecyclerViewAdapter(unpaidTariffItems, getActivity(), this);
        mRecyclerViewInactive.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private void startPaidTariffFragment()
    {
        Fragment fragment = new PaidTariffFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    @Override
    public void onInactiveTariffClick(int position)
    {
        Session tariff_item = unpaidTariffItems.get(position);
        Date start_time = tariff_item.getStart_datetime();
        //Log.d(TAG, "onSuccess: " + start_time);
        Date end_time = tariff_item.getEnd_datetime();
        //Log.d(TAG, "onSuccess: " + end_time);
        String vehicleNum = tariff_item.getVehicle();
        //Log.d(TAG, "onSuccess: " + vehicleNum);
        String parking_spot = tariff_item.getParking_id();
        //Log.d(TAG, "onSuccess: " + parking_spot);
        char parking_level = tariff_item.getParking_id().charAt(0);
        //Log.d(TAG, "onSuccess: " + parking_level);
        double tariff_amt = tariff_item.getTariff_amount();
        String avenue_name = tariff_item.getAvenue_name();

        if (start_time != null) args2.putSerializable("start_time2", start_time);
        if (end_time != null) args2.putSerializable("end_time2", end_time);
        if (avenue_name != null) args2.putString("avenue_name2", avenue_name);
        args2.putString("vehicle_num2", vehicleNum);
        args2.putString("parking_spot2", parking_spot);
        args2.putChar("parking_level2", parking_level);
        args2.putDouble("tariff2", tariff_amt);

        getActivity().getSupportFragmentManager().setFragmentResult("requestKeyFromUnpaidTariffList", args2);

        Fragment fragment = new CurrentSessionFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    @Override
    public void onActiveTariffClick(int position)
    {

        Session tariff_item = activeTariffItems.get(position);
        Date start_time = tariff_item.getStart_datetime();
        //Log.d(TAG, "onSuccess: " + start_time);
        Date end_time = tariff_item.getEnd_datetime();
        //Log.d(TAG, "onSuccess: " + end_time);
        String vehicleNum = tariff_item.getVehicle();
        //Log.d(TAG, "onSuccess: " + vehicleNum);
        String parking_spot = tariff_item.getParking_id();
        //Log.d(TAG, "onSuccess: " + parking_spot);
        char parking_level = tariff_item.getParking_id().charAt(0);
        //Log.d(TAG, "onSuccess: " + parking_level);
        double tariff_amt = tariff_item.getTariff_amount();
        String avenue_name = tariff_item.getAvenue_name();

        if (start_time != null) args2.putSerializable("start_time3", start_time);
        if (end_time != null) args2.putSerializable("end_time3", end_time);
        if (avenue_name != null) args2.putString("avenue_name3", avenue_name);
        args2.putString("vehicle_num3", vehicleNum);
        args2.putString("parking_spot3", parking_spot);
        args2.putChar("parking_level3", parking_level);
        args2.putDouble("tariff3", tariff_amt);

        getActivity().getSupportFragmentManager().setFragmentResult("requestKeyFromActiveTariffList", args2);


        Fragment fragment = new CurrentSessionFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();

    }
}