package com.autobots.innopark.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autobots.innopark.R;
import com.autobots.innopark.adapter.ParkingFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class ParkingFragment extends Fragment {

    Toolbar toolbar;
    TextView toolbarTitle;

    public ParkingFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parking, container, false);
        Log.d("TAG", "onCreateView: ");

        setupToolbar(view);
        setupViewPager(view);

        return view;

    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.main_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Parking");
    }

    private void setupViewPager(View view)
    {
        ViewPager viewPager = view.findViewById(R.id.id_view_pager);
        PagerAdapter pagerAdapter = new ParkingFragmentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.id_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}