package com.autobots.innopark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;


import com.autobots.innopark.adapter.RecyclerViewAdapter;
import com.autobots.innopark.data.MenuItemList;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    Toolbar toolbar;
    private RecyclerView m_recycler_view;
    private RecyclerView.Adapter m_adapter;
    private RecyclerView.LayoutManager m_layout_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        toolbar = findViewById(R.id.id_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<MenuItemList> menuItem = new ArrayList<>();

        menuItem.add(new MenuItemList(R.drawable.ic_baseline_profile_24, "Profile"));
        menuItem.add(new MenuItemList(R.drawable.ic_baseline_directions_vehicle_24, "Vehicles"));
        menuItem.add(new MenuItemList(R.drawable.ic_baseline_customer_service_two_24, "Customer Service"));
        menuItem.add(new MenuItemList(R.drawable.ic_baseline_faq_24, "FAQ"));
        menuItem.add(new MenuItemList(R.drawable.ic_baseline_admin_panel_settings_24, "Admin Panel"));


        m_recycler_view = findViewById(R.id.id_menu_recycler_view);
        m_recycler_view.setHasFixedSize(true);
        m_layout_manager = new LinearLayoutManager(this);
        m_adapter = new RecyclerViewAdapter(menuItem, this);

        m_recycler_view.setLayoutManager(m_layout_manager);
        m_recycler_view.setAdapter(m_adapter);




    }
}