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

        addMenuListFragment();


        return view;
    }

    private void addMenuListFragment()
    {
        Fragment fragment = new MenuListFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.id_child_fragment_container_view, fragment)
                .commit();

    }





}