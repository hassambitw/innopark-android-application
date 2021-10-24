package com.autobots.innopark.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.R;
import com.autobots.innopark.adapter.TariffInactiveSessionRecyclerViewAdapter;
import com.autobots.innopark.data.Tariff;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UnpaidTariffFragment extends Fragment implements TariffInactiveSessionRecyclerViewAdapter.OnTariffClickListener
{

    Toolbar toolbar;
    TextView toolbarTitle;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Tariff> tariffItems;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_unpaid_tariff, container, false);

        setupToolbar(view);
        populateTariffs();
        setupUnpaidRecyclerView(view);


        return view;

    }

    private void populateTariffs()
    {
        tariffItems = new ArrayList<>();

        tariffItems.add(new Tariff(1, 40.0, "1hr : 30 min", "UOWD", "B1", "Space A", "Spot 23", Boolean.FALSE));
        tariffItems.add(new Tariff(1, 40.0, "1hr : 30 min", "UOWD", "B1", "Space A", "Spot 23", Boolean.FALSE));
        tariffItems.add(new Tariff(1, 40.0, "1hr : 30 min", "UOWD", "B1", "Space A", "Spot 23", Boolean.FALSE));
        tariffItems.add(new Tariff(1, 40.0, "1hr : 30 min", "UOWD", "B1", "Space A", "Spot 23", Boolean.FALSE));
        tariffItems.add(new Tariff(1, 40.0, "2hr : 30 min", "UOWD", "B1", "Space A", "Spot 23", Boolean.FALSE));
        tariffItems.add(new Tariff(1, 40.0, "-", "UOWD", "B1", "Space A", "Spot 23", Boolean.FALSE));

    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_unpaid_sessions_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Unpaid Sessions");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setupUnpaidRecyclerView(View view)
    {
        mRecyclerView = view.findViewById(R.id.id_unpaid_sessions_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TariffInactiveSessionRecyclerViewAdapter(tariffItems, getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onTariffClick(int position)
    {
//        Fragment selectedFragment = new TariffFragment();
//
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .setReorderingAllowed(true)
//                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
//                .addToBackStack(null)
//                .replace(R.id.id_fragment_container_view, selectedFragment)
//                .commit();

        Fragment fragment = new CurrentSessionFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

}
