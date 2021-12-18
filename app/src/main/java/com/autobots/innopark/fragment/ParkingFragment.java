package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autobots.innopark.activity.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.adapter.ParkingFragmentPagerAdapter;
import com.autobots.innopark.util.DatabaseUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class ParkingFragment extends Fragment {

    Toolbar toolbar;
    TextView toolbarTitle;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    String value;
    private static final String TAG = "Parking Fragment";

    ViewPager viewPager;
    TabLayout tabLayout;

    //firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    public ParkingFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parking, container, false);
        viewPager = view.findViewById(R.id.id_view_pager);
        tabLayout = view.findViewById(R.id.id_tabs);
        Log.d("TAG", "onCreateView: ");

        setupToolbar(view);
        setupViewPager();

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().getSupportFragmentManager().setFragmentResultListener("From Dispute Fine", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.d(TAG, "onFragmentResult: Inside");
                value = result.getString("FinesDefault");
                setupViewPager();
            }
        });
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

    private void setupViewPager()
    {
        PagerAdapter pagerAdapter = new ParkingFragmentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
//        tabLayout.selectTab(T);
        if (value == "1") {
            tabLayout.selectTab(tabLayout.getTabAt(1));
            Log.d(TAG, "setupViewPager: Inside");
        }
        tabLayout.setupWithViewPager(viewPager);

    }
}