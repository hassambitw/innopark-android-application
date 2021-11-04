package com.autobots.innopark.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.autobots.innopark.R;
import com.autobots.innopark.data.Callbacks.HashmapCallback;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.Tags;
import com.autobots.innopark.data.User;
import com.autobots.innopark.data.UserApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class ProfileFragment extends Fragment
{

    Toolbar toolbar;
    TextView toolbar_title;
    TextView editProfile;
    EditText emailET;
    EditText usernameET;
    EditText passwordET;
    EditText firstNameET;
    EditText lastNameET;
    EditText cardET;
    FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProfileFragment()
    {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        editProfile = view.findViewById(R.id.id_profile_editprofile);

        //edittexts
        emailET = view.findViewById(R.id.id_profile_email);
        usernameET = view.findViewById(R.id.id_profile_username);
        passwordET = view.findViewById(R.id.id_profile_password);
        firstNameET = view.findViewById(R.id.id_profile_first_name);
        lastNameET = view.findViewById(R.id.id_profile_last_name);
        cardET = view.findViewById(R.id.id_profile_card_id);

        editProfile.setOnClickListener((v) -> {
            editProfileFragment();
        });

        setupToolbar(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        UserApi userApi = UserApi.getInstance();
        String userEmail = userApi.getUserEmail();
        String username = userApi.getUsername();

        User.getUser(userEmail, new HashmapCallback() {
            @Override
            public void passHashmapResult(HashMap<String, Object> result) {
                if(!result.isEmpty()){
                    // DO SOMETHING WITH THE USER INFO
                    String email = (String) result.get("email_address");
                    String username = (String) result.get("username");
                    String first_name = (String) result.get("first_name");
                    String last_name = (String) result.get("last_name");
                    long cardId = (long) result.get("id_card_number");

                    emailET.setText(email);
                    usernameET.setText(username);
                    firstNameET.setText(first_name);
                    lastNameET.setText(last_name);
                    cardET.setText(cardId +"");

//                    Bundle bundle = new Bundle();
//                    bundle.putLong("cardId", cardId);

                    Log.w(Tags.SUCCESS.name(), result.toString());

                }else Log.w(Tags.FAILURE.name(), "ERROR: USER NOT FOUND");
            }
        });
    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title = toolbar.findViewById(R.id.id_toolbar_title);
        toolbar_title.setText("Profile");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void editProfileFragment()
    {
        Fragment selectedFragment = new EditProfileFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .addToBackStack(null)
                .replace(R.id.id_fragment_container_view, selectedFragment)
                .commit();

    }




}