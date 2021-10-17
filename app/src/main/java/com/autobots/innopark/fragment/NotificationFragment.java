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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autobots.innopark.R;
import com.autobots.innopark.adapter.MenuRecyclerViewAdapter;
import com.autobots.innopark.adapter.NotificationsRecyclerViewAdapter;
import com.autobots.innopark.data.NotificationData;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    ArrayList<NotificationData> notifs = new ArrayList<>();
    Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView toolbar_title;

    public NotificationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);

        setupToolbar(view);
        setupData();
        setupRecyclerView(view);

        return view;
    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_notifications_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title = toolbar.findViewById(R.id.id_toolbar_title);
        toolbar_title.setText("Notification");
        //toolbar.setTitle("Menu");
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupData()
    {
        notifs.add(new NotificationData(1, "You have parked at spot B1"));
        notifs.add(new NotificationData(2, "You have parked at spot B2"));
        notifs.add(new NotificationData(3, "You have parked at spot B3"));
        notifs.add(new NotificationData(4, "You have parked at spot B4"));

    }

    private void setupRecyclerView(View view)
    {
        mRecyclerView = view.findViewById(R.id.id_notifications_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new NotificationsRecyclerViewAdapter(getActivity(), notifs);
        mRecyclerView.setAdapter(mAdapter);
    }
}