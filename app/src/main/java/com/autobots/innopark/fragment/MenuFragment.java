package com.autobots.innopark.fragment;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.autobots.innopark.R;


public class MenuFragment extends Fragment
{

    Toolbar toolbar;
    Button signOutBtn;
    TextView toolbar_title;


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
        addMenuListFragment();


//        addMenuItems();
//        setupRecyclerView(view);
//        setupSignOutDialog(view);

        return view;
    }

    private void addMenuListFragment()
    {
        Fragment fragment = new MenuListFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.id_child_fragment_container_view, fragment)
                .commit();

    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title = toolbar.findViewById(R.id.id_toolbar_title);
        toolbar_title.setText("Menu");
        //toolbar.setTitle("Menu");
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



}