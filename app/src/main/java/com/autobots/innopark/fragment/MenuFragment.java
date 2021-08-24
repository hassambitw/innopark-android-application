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

import com.autobots.innopark.R;
import com.autobots.innopark.adapter.RecyclerViewAdapter;
import com.autobots.innopark.data.MenuItemList;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    Toolbar toolbar;
    private RecyclerView m_recycler_view;
    private RecyclerView.Adapter m_adapter;
    private RecyclerView.LayoutManager m_layout_manager;
    private ArrayList<MenuItemList> menuItem;

    public MenuFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);

        setupToolbar(view);
        addMenuItems();
        setupRecyclerView(view);

        return view;
    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        m_recycler_view = view.findViewById(R.id.id_menu_recycler_view);
        m_recycler_view.setHasFixedSize(true);
        m_layout_manager = new LinearLayoutManager(getActivity());
        m_adapter = new RecyclerViewAdapter(menuItem, getActivity());
        m_recycler_view.setLayoutManager(m_layout_manager);
        m_recycler_view.setAdapter(m_adapter);
    }
}