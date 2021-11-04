package com.autobots.innopark.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.autobots.innopark.R;
import com.autobots.innopark.data.User;
import com.autobots.innopark.data.UserApi;


public class EditProfileFragment extends Fragment
{

   Toolbar toolbar;
   TextView toolbarTitle;
   EditText usernameET;
   EditText passwordET;
   EditText cardET;
   String username;
   long cardId;

    public EditProfileFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        usernameET = view.findViewById(R.id.id_edit_profile_username);
        passwordET = view.findViewById(R.id.id_edit_profile_password);
        cardET = view.findViewById(R.id.id_edit_profile_card_id);

//        Bundle bundle = getActivity().getIntent().getExtras();
//
//        if (bundle != null)
//        {
//            cardId = bundle.getLong("cardId");
//        }
//
//        UserApi userApi = UserApi.getInstance();
//        username = userApi.getUsername();
//
//        usernameET.setText(username);
//        cardET.setText(cardId + "");



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
        toolbarTitle.setText("Edit Profile");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}