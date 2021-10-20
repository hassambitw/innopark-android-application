package com.autobots.innopark.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.adapter.MenuRecyclerViewAdapter;
import com.autobots.innopark.data.MenuItemList;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MenuListFragment extends Fragment implements MenuRecyclerViewAdapter.OnMenuClickListener {

    Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<MenuItemList> menuItem;
    AlertDialog signoutDialog;
    Button signOutBtn;
    TextView toolbar_title;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;


    public MenuListFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_list, container, false);
        signOutBtn = view.findViewById(R.id.id_sign_out_btn);
        Fragment menuFragment = new MenuFragment();
        //firebaseAuth = FirebaseAuth.getInstance();

//        toolbar = ((AppCompatActivity) getActivity()).findViewById(R.id.id_menu_toolbar);
//        toolbar_title = toolbar.findViewById(R.id.id_toolbar_title);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        toolbar_title.setText("Menu");

        setupToolbar(view);
        addMenuItems();
        setupRecyclerView(view);
        setupSignOutDialog(view);

        return view;
    }


    private void addMenuItems()
    {
        menuItem = new ArrayList<>();

        menuItem.add(new MenuItemList(R.drawable.menu_account, "Profile"));
        menuItem.add(new MenuItemList(R.drawable.menu_car, "Vehicles"));
        menuItem.add(new MenuItemList(R.drawable.menu_customer_service, "Customer Service"));
        menuItem.add(new MenuItemList(R.drawable.menu_faq, "FAQ"));
        menuItem.add(new MenuItemList(R.drawable.menu_admin_panel, "Admin Panel"));
    }

    private void setupRecyclerView(View view)
    {
        mRecyclerView = view.findViewById(R.id.id_menu_recycler_view);
        mRecyclerView.setHasFixedSize(true);
       // mLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mAdapter = new MenuRecyclerViewAdapter(menuItem, getActivity(), this);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupSignOutDialog(View view)
    {
        signOutBtn.setOnClickListener((v) -> {
            signoutDialog = new AlertDialog.Builder(getActivity())
                    .setMessage("Are you sure you want to sign out?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            //firebaseAuth.signOut();
                            Toast.makeText(getActivity(), "User signed out", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .setCancelable(false)
                    .show();
        });
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

    @Override
    public void onMenuClick(int position)
    {
        Log.d("TAG", "onMenuClick: " + position);
        Toast.makeText(getActivity(), "Clicked " + position, Toast.LENGTH_SHORT).show();
        Fragment selectedFragment = null;


        switch (position)
        {
            case 0:
            {
                selectedFragment = new ProfileFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                        .addToBackStack(null)
                        .replace(R.id.id_child_fragment_container_view, selectedFragment)
                        .commit();
                break;
            }
            case 1:
            {
                selectedFragment = new VehicleListFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                        .addToBackStack(null)
                        .replace(R.id.id_child_fragment_container_view, selectedFragment)
                        .commit();
                break;
            }
            case 2:
            {
                selectedFragment = new CustomerServiceFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                        .addToBackStack(null)
                        .replace(R.id.id_child_fragment_container_view, selectedFragment)
                        .commit();
                break;
            }
            case 3:
            {
                selectedFragment = new FAQFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                        .addToBackStack(null)
                        .replace(R.id.id_child_fragment_container_view, selectedFragment)
                        .commit();
                break;
            }
            case 4:
            {
                selectedFragment = new AdminPanelFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                        .addToBackStack(null)
                        .replace(R.id.id_child_fragment_container_view, selectedFragment)
                        .commit();
                break;
            }


        }
    }

}