package com.autobots.innopark.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.adapter.MenuRecyclerViewAdapter;
import com.autobots.innopark.data.MenuItemList;

import java.util.ArrayList;

public class MenuFragment extends Fragment
{

    Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MenuItemList> menuItem;
    AlertDialog signoutDialog;
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


//
//    private void addMenuItems()
//    {
//        menuItem = new ArrayList<>();
//
//        menuItem.add(new MenuItemList(R.drawable.ic_baseline_profile_24, "Profile"));
//        menuItem.add(new MenuItemList(R.drawable.ic_baseline_directions_vehicle_24, "Vehicles"));
//        menuItem.add(new MenuItemList(R.drawable.ic_baseline_customer_service_two_24, "Customer Service"));
//        menuItem.add(new MenuItemList(R.drawable.ic_baseline_faq_24, "FAQ"));
//        menuItem.add(new MenuItemList(R.drawable.ic_baseline_admin_panel_settings_24, "Admin Panel"));
//    }
//
//    private void setupRecyclerView(View view)
//    {
//        mRecyclerView = view.findViewById(R.id.id_menu_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mAdapter = new MenuRecyclerViewAdapter(menuItem, getActivity(), this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//    }
//
//    private void setupSignOutDialog(View view)
//    {
//        signOutBtn.setOnClickListener((v) -> {
//            signoutDialog = new AlertDialog.Builder(getActivity())
//                    .setMessage("Are you sure you want to sign out?")
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i)
//                        {
//                            startActivity(new Intent(getActivity(), LoginActivity.class));
//                        }
//                    })
//                    .setNegativeButton("Cancel", null)
//                    .setCancelable(false)
//                    .show();
//        });
//    }
//
//    @Override
//    public void onMenuClick(int position)
//    {
//        Log.d("TAG", "onMenuClick: " + position);
//        Toast.makeText(getActivity(), "Clicked " + position, Toast.LENGTH_SHORT).show();
//        Fragment childFragment = null;
//        Fragment parentFragment;
//
//
//        switch (position)
//        {
//            case 0:
//            {
//                childFragment = new ProfileFragment();
//                parentFragment = new MenuFragment();
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
//                        .remove(parentFragment)
//                        .replace(R.id.id_fragment_container_view, childFragment)
//                        .addToBackStack(null)
//                        .commit();
//                break;
//            }
//
//
//        }
//    }
}