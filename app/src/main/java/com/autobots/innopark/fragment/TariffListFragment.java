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
import com.autobots.innopark.adapter.TariffRecyclerViewAdapter;
import com.autobots.innopark.data.Tariff;

import java.util.ArrayList;


public class TariffListFragment extends Fragment implements TariffRecyclerViewAdapter.OnTariffClickListener
{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView paidTariff;
    private ArrayList<Tariff> tariffItems;

    public TariffListFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tariff_list, container, false);
        paidTariff = view.findViewById(R.id.id_tariff_view_previous_tariff);

        paidTariff.setOnClickListener((v) -> {
            startPaidTariffFragment();
        });

        populateTariffs();
        setupRecyclerView(view);


        return view;
    }

    private void setupRecyclerView(View view)
    {
        mRecyclerView = view.findViewById(R.id.id_recycler_view_tariff);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TariffRecyclerViewAdapter(tariffItems, getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void populateTariffs()
    {
        tariffItems = new ArrayList<>();

        tariffItems.add(new Tariff(1, 40.0, "1hr : 30 min", "UOWD", "B1", "Space A", "Spot 23", Boolean.FALSE));

    }

    private void startPaidTariffFragment()
    {
        Fragment fragment = new PaidTariffFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    @Override
    public void onTariffClick(int position)
    {
        Fragment selectedFragment = new TariffFragment();

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .addToBackStack(null)
                .replace(R.id.id_fragment_container_view, selectedFragment)
                .commit();
    }
}