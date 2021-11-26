package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaidTariffFragment extends Fragment implements TariffInactiveSessionRecyclerViewAdapter.OnTariffClickListener
{

    private static final String TAG = "PaidTariffFragment";
    Toolbar toolbar;
    TextView toolbar_title;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Session> paidTariffItems;
    private List<String> vehiclesCombined;
    TextView emptyView;

    Bundle args2;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    //firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("avenues");

    Session tariff;

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
        View view = inflater.inflate(R.layout.fragment_paid_tariff, container, false);

        paidTariffItems = new ArrayList<>();
        vehiclesCombined = new ArrayList<>();

        mRecyclerView = view.findViewById(R.id.id_paid_tariff_recycler_view);
        emptyView = view.findViewById(R.id.id_paid_tariff_empty_view);

        args2 = new Bundle();

        setupToolbar(view);
        //populateTariffs();
        setupPaidRecyclerView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadPaidData();
    }

    private void loadPaidData()
    {
        UserApi userApi = UserApi.getInstance();
        vehiclesCombined = userApi.getVehiclesCombined();

        if (!vehiclesCombined.isEmpty()) {
            db.collectionGroup("sessions_info")
                    .whereNotEqualTo("end_datetime", null)
                    .whereEqualTo("is_paid", true)
                    .whereIn("vehicle", vehiclesCombined)
                    .orderBy("end_datetime", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Log.d(TAG, "onSuccess: Inside");
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot snapshot : snapshotList) {
                                    try {
                                        Log.d(TAG, "onSuccess: " + snapshot.getId());
                                        tariff = snapshot.toObject(Session.class);
                                        paidTariffItems.add(tariff);

                                    } catch (Exception e) {
                                        Log.d(TAG, "onSuccess: " + e.getMessage());
                                    }

                                    String parent_id = String.valueOf(snapshot.getReference().getParent().getParent().getId()); // parent doc id
                                    Log.d(TAG, "onSuccess: " + parent_id);

                                }
                                setupPaidRecyclerView();

                            } else {
                                Log.d(TAG, "onSuccess: Query document snapshots empty");
                                emptyView.setVisibility(View.VISIBLE);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    });
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }


    private void setupPaidRecyclerView()
    {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TariffInactiveSessionRecyclerViewAdapter(paidTariffItems, getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title = toolbar.findViewById(R.id.id_toolbar_title);
        toolbar_title.setText("Paid Tariff");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onInactiveTariffClick(int position)
    {

        Session tariff_item = paidTariffItems.get(position);
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

        if (start_time != null) args2.putSerializable("start_time5", start_time);
        if (end_time != null) args2.putSerializable("end_time5", end_time);
        if (avenue_name != null) args2.putString("avenue_name5", avenue_name);
        args2.putString("vehicle_num5", vehicleNum);
        args2.putString("parking_spot5", parking_spot);
        args2.putChar("parking_level5", parking_level);
        args2.putDouble("tariff5", tariff_amt);

        getActivity().getSupportFragmentManager().setFragmentResult("requestKeyFromPaidTariff", args2);

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
