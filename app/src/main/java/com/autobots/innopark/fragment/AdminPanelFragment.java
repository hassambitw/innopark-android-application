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
import com.autobots.innopark.adapter.AdminPanelRecyclerViewAdapter;
import com.autobots.innopark.data.MenuItemList;

import java.util.ArrayList;

public class AdminPanelFragment extends Fragment implements AdminPanelRecyclerViewAdapter.OnAdminPanelClickListener {

    Toolbar toolbar;
    TextView toolbar_title;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> adminPanelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_panel, container, false);

        setupToolbar();
        populateAdminPanel();
        setupRecyclerView(view);

        return view;
    }

    private void populateAdminPanel()
    {
        adminPanelList = new ArrayList<>();

        adminPanelList.add("Ban Account");
        adminPanelList.add("Unban Account");
        adminPanelList.add("Import Blueprints");
        adminPanelList.add("View Parking Violations");
        adminPanelList.add("View Data Analytics");
        adminPanelList.add("Backup System");
        adminPanelList.add("Restore System");

    }

    private void setupRecyclerView(View view)
    {
        mRecyclerView = view.findViewById(R.id.id_admin_panel_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new AdminPanelRecyclerViewAdapter(adminPanelList, getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupToolbar()
    {
        toolbar = getActivity().findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title = toolbar.findViewById(R.id.id_toolbar_title);
        toolbar_title.setText("Admin Panel");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onAdminPanelClick(int position)
    {

    }
}