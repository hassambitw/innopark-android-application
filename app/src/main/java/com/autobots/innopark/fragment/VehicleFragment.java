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
import android.widget.Button;
import android.widget.TextView;

import com.autobots.innopark.R;
import com.autobots.innopark.adapter.DriverRecyclerView;
import com.autobots.innopark.adapter.VehicleRecyclerViewAdapter;
import com.autobots.innopark.data.Driver;

import java.util.ArrayList;


public class VehicleFragment extends Fragment {

    Toolbar toolbar;
    TextView toolbar_title;
    Button addDriver;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Driver> driverList;

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

        addDriver.setOnClickListener((v) -> {
            addDriverFragment();
        });

        setupToolbar();
        populateDrivers();
        setupRecyclerView(view);

        return view;
    }

    private void addDriverFragment()
    {
        Fragment fragment = new AddDriverFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_child_fragment_container_view, fragment)
                .commit();
    }

    private void populateDrivers()
    {
        driverList = new ArrayList<>();

        driverList.add(new Driver("Hassam Shaukat", "23", "05/12/1997", "Pakistan"));
        driverList.add(new Driver("Wahdan Hasan", "21", "15/02/1999", "Pakistan"));
        driverList.add(new Driver("Rama Al Sbeinaty", "21", "05/12/1999", "Syria"));
        driverList.add(new Driver("Pritish Agarwal", "21", "05/12/1999", "India"));
    }

    private void setupToolbar()
    {
        toolbar = getActivity().findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title = toolbar.findViewById(R.id.id_toolbar_title);
        toolbar_title.setText("Vehicle Details");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setupRecyclerView(View view)
    {
        mRecyclerView = view.findViewById(R.id.id_vehicle_drivers_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DriverRecyclerView(driverList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }
}