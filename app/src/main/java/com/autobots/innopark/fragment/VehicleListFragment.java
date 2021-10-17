package com.autobots.innopark.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autobots.innopark.R;
import com.autobots.innopark.adapter.VehicleRecyclerViewAdapter;
import com.autobots.innopark.data.Vehicle;

import java.util.ArrayList;

public class VehicleListFragment extends Fragment implements VehicleRecyclerViewAdapter.OnVehicleClickListener {

    Toolbar toolbar;
    TextView toolbarTitle;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Vehicle> vehicleList;
    private TextView addVehicle;


    public VehicleListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_list, container, false);

        addVehicle = view.findViewById(R.id.id_vehicles_add_vehicle);

        addVehicle.setOnClickListener((v) -> {
            addVehicleFragment();
        });

        setupToolbar(view);
        populateVehicles();
        setupRecyclerView(view);

        return view;
    }

    private void populateVehicles()
    {
        vehicleList = new ArrayList<>();

        vehicleList.add(new Vehicle("B12345", new String[]{"Hassam Shaukat"}, "Ferrari F50"));
        vehicleList.add(new Vehicle("C4521", new String[]{"Pritish Agarwal, Wahdan Hasan"}, "Lamborghini Aventador"));
    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Customer Support");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setupRecyclerView(View view)
    {
        mRecyclerView = view.findViewById(R.id.id_vehicles_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new VehicleRecyclerViewAdapter(vehicleList, getActivity(), this);
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