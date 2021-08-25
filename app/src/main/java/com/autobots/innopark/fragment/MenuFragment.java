package com.autobots.innopark.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.adapter.MenuRecyclerViewAdapter;
import com.autobots.innopark.data.MenuItemList;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MenuItemList> menuItem;
    AlertDialog signoutDialog;
    Button signOutBtn;


    public MenuFragment()
    {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);
        signOutBtn = view.findViewById(R.id.id_sign_out_btn);


        setupToolbar(view);
        addMenuItems();
        setupRecyclerView(view);
        setupSignOutDialog(view);

        return view;
    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setTitle("Menu");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addMenuItems()
    {
        menuItem = new ArrayList<>();

        menuItem.add(new MenuItemList(R.drawable.ic_baseline_profile_24, "Profile"));
        menuItem.add(new MenuItemList(R.drawable.ic_baseline_directions_vehicle_24, "Vehicles"));
        menuItem.add(new MenuItemList(R.drawable.ic_baseline_customer_service_two_24, "Customer Service"));
        menuItem.add(new MenuItemList(R.drawable.ic_baseline_faq_24, "FAQ"));
        menuItem.add(new MenuItemList(R.drawable.ic_baseline_admin_panel_settings_24, "Admin Panel"));
    }

    private void setupRecyclerView(View view)
    {
        mRecyclerView = view.findViewById(R.id.id_menu_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new MenuRecyclerViewAdapter(menuItem, getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupSignOutDialog(View view)
    {
        signOutBtn.setOnClickListener((v) -> {
            signoutDialog = new AlertDialog.Builder(getActivity())
                    .setMessage("Are you sure you want to sign out?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .setCancelable(false)
                    .show();
        });
    }

}