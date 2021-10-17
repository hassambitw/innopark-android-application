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

import com.autobots.innopark.R;

public class AddDriverFragment extends Fragment {

    Toolbar toolbar;
    TextView toolbarTitle;
    TextView driverName;
    TextView driverAge;
    TextView driverDOB;
    TextView driverNationality;


    public AddDriverFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_driver, container, false);
        driverName = view.findViewById(R.id.id_add_drivers_name);
        driverAge = view.findViewById(R.id.id_add_drivers_age);
        driverDOB = view.findViewById(R.id.id_add_drivers_dob);
        driverNationality = view.findViewById(R.id.id_add_drivers_nationality);

        setupToolbar(view);

        return view;
    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Add Driver");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
