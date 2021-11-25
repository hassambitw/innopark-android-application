package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.data.Callbacks.HashmapCallback;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.Tags;
import com.autobots.innopark.data.User;
import com.autobots.innopark.data.UserApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class ProfileFragment extends Fragment
{

    private static final String TAG = "ProfileFragment";
    Toolbar toolbar;
    TextView toolbar_title;
    TextView editProfile;
    EditText emailET;
    EditText usernameET;
    EditText passwordET;
    EditText firstNameET;
    EditText lastNameET;
    EditText cardET;
    Button deleteAccount;
    AlertDialog deleteDialog;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

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


        emailET = view.findViewById(R.id.id_profile_email);
        usernameET = view.findViewById(R.id.id_profile_username);
        passwordET = view.findViewById(R.id.id_profile_password);
        firstNameET = view.findViewById(R.id.id_profile_first_name);
        lastNameET = view.findViewById(R.id.id_profile_last_name);
        cardET = view.findViewById(R.id.id_profile_card_id);
        deleteAccount = view.findViewById(R.id.id_profile_delete);
        progressBar = view.findViewById(R.id.id_profile_progress_bar);

        editProfile.setOnClickListener((v) -> {
            editProfileFragment();
        });

        setupToolbar(view);
        setupDeleteAccount(view);

        return view;
    }

    private void setupDeleteAccount(View view)
    {
       deleteAccount.setOnClickListener((v) -> {
           deleteDialog = new AlertDialog.Builder(getActivity())
                   .setTitle("Are you sure?")
                   .setMessage("Deleting this account will result in completely removing your profile from the system " +
                                "and you won't be able to access it again.")
                   .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           progressBar.setVisibility(View.VISIBLE);
                            currentUser.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful())
                                                Log.d(TAG, "onComplete: " + " User Deleted Successfully");
                                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            Log.d(TAG, "onFailure: " + " User not deleted due to exception " + e.getMessage());
                                        }
                                    });
                       }
                   })
                   .setNegativeButton("Cancel", null)
                   .setCancelable(false)
                   .show();
       });
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
                if (!result.isEmpty()){
                    // DO SOMETHING WITH THE USER INFO
                    String email = (String) result.get("email_address");
                    String username = (String) result.get("username");
                    String first_name = (String) result.get("first_name");
                    String last_name = (String) result.get("last_name");
                    String cardId = (String) result.get("id_card_number");

                    emailET.setText(email);
                    usernameET.setText(username);
                    firstNameET.setText(first_name);
                    lastNameET.setText(last_name);
                    cardET.setText(cardId);

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