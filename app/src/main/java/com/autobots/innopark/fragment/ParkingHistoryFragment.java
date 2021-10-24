package com.autobots.innopark.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autobots.innopark.R;
import com.autobots.innopark.adapter.FineHistoryRecyclerViewAdapter;
import com.autobots.innopark.adapter.ParkingHistoryRecyclerViewAdapter;
import com.autobots.innopark.adapter.VehicleRecyclerViewAdapter;
import com.autobots.innopark.data.ParkingHistoryData;

import java.util.ArrayList;


public class ParkingHistoryFragment extends Fragment {

    Toolbar toolbar;
    TextView toolbarTitle;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ParkingHistoryData> parkingHistoryDataList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        String strMessage = getArguments().getString("Message");

        View view = inflater.inflate(R.layout.fragment_parking_history, container, false);
        parkingHistoryDataList = new ArrayList<ParkingHistoryData>();

        setupData();

        if (strMessage.toLowerCase().contains("from parking history card")) {
            setupPHToolbar(view);
            setupRecyclerView(view);
        }
        else if (strMessage.toLowerCase().contains("from fine history card")) {
            setupFHToolbar(view);
            setupFineRecyclerView(view);
        }


        return view;
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

    private void setupData()
    {
        parkingHistoryDataList.add(new ParkingHistoryData("UOWD", "02/09/2021", "B1", new String[]{"Hassam Shaukat"}, "A12345", "Toyota Corolla", "1 Hour", Boolean.TRUE, "Paid", 50, 100, Boolean.TRUE));
        parkingHistoryDataList.add(new ParkingHistoryData("UOWD", "02/09/2021", "B1", new String[]{"Hassam Shaukat"}, "A12345", "Toyota Corolla", "1 Hour", Boolean.FALSE, "Unpaid", 50, 100, Boolean.FALSE));
        parkingHistoryDataList.add(new ParkingHistoryData("UOWD", "02/09/2021", "B1", new String[]{"Hassam Shaukat"}, "A12345", "Toyota Corolla", "1 Hour", Boolean.TRUE, "Paid", 50, 100, Boolean.FALSE));
        parkingHistoryDataList.add(new ParkingHistoryData("UOWD", "02/09/2021", "B1", new String[]{"Hassam Shaukat"}, "A12345", "Toyota Corolla", "1 Hour", Boolean.FALSE, "Unpaid", 50, 100, Boolean.TRUE));
        parkingHistoryDataList.add(new ParkingHistoryData("UOWD", "02/09/2021", "B1", new String[]{"Hassam Shaukat"}, "A12345", "Toyota Corolla", "1 Hour", Boolean.FALSE, "Unpaid", 50, 100, Boolean.TRUE));
    }


    private void setupRecyclerView(View view)
    {
        mRecyclerView = view.findViewById(R.id.id_parking_history_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new ParkingHistoryRecyclerViewAdapter(parkingHistoryDataList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupFineRecyclerView(View view)
    {
        mRecyclerView = view.findViewById(R.id.id_parking_history_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new FineHistoryRecyclerViewAdapter(parkingHistoryDataList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

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