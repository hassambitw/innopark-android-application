package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.adapter.DriverRecyclerView;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.Driver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddVehicleFragment extends Fragment {

    Toolbar toolbar;
    TextView toolbarTitle;
    Button addDriver;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Driver> driverList;

    Button addLicense;
    EditText licenseET;

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
        View view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);

        addLicense = view.findViewById(R.id.id_add_vehicle_add);
        licenseET = view.findViewById(R.id.id_add_vehicle_license_num);


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






}