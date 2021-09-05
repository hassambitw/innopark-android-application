package com.autobots.innopark.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autobots.innopark.R;
import com.autobots.innopark.adapter.FinesRecyclerViewAdapter;
import com.autobots.innopark.adapter.TariffRecyclerViewAdapter;
import com.autobots.innopark.data.Fine;
import com.autobots.innopark.data.Tariff;

import java.util.ArrayList;

public class FinesListFragment extends Fragment implements FinesRecyclerViewAdapter.OnFineClickListener
{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView paidFines;
    private ArrayList<Fine> fineItems;

    public FinesListFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fines_list, container, false);
        paidFines = view.findViewById(R.id.id_fine_view_previous_fine);

        paidFines.setOnClickListener((v) -> {
            startPaidFinesFragment();
        });

        populateFines();
        setupRecyclerView(view);

        return view;
    }

    private void setupRecyclerView(View view)
    {
        mRecyclerView = view.findViewById(R.id.id_recycler_view_fines);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FinesRecyclerViewAdapter(fineItems, getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void populateFines()
    {
        fineItems = new ArrayList<>();

        fineItems.add(new Fine(1, 100));
        fineItems.add(new Fine(2, 200));
        fineItems.add(new Fine(3, 300));
        fineItems.add(new Fine(4, 150));
        fineItems.add(new Fine(5, 350));

    }

    private void startPaidFinesFragment()
    {
        Fragment fragment = new PaidFinesFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    @Override
    public void onFineClick(int position)
    {
        Fragment selectedFragment = new FineFragment();

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .addToBackStack(null)
                .replace(R.id.id_fragment_container_view, selectedFragment)
                .commit();
    }
}