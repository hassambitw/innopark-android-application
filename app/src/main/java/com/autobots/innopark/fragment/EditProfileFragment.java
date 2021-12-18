package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.autobots.innopark.activity.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.util.DatabaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class EditProfileFragment extends Fragment
{

   Toolbar toolbar;
   TextView toolbarTitle;
   EditText emailET;
   EditText firstNameET;
   EditText lastNameET;
   EditText phoneNumET;
   Button saveChanges;
   ProgressBar progressBar;

   String firstName;
   String lastName;
   String email;
   String phoneNum;

    private static final String TAG = "EditProfileFragment";


    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    //firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("user");

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

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
        progressBar = view.findViewById(R.id.id_edit_profile_progress_bar);
        firstNameET = view.findViewById(R.id.id_edit_profile_first_name);
        lastNameET = view.findViewById(R.id.id_edit_profile_last_name);
        phoneNumET = view.findViewById(R.id.id_edit_profile_card_phone_num);
        saveChanges = view.findViewById(R.id.id_edit_profile_save_changes);

        saveChanges.setOnClickListener((v) -> {
            editProfile();
        });



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


    private void editProfile()
    {
        progressBar.setVisibility(View.VISIBLE);
        firstName = firstNameET.getText().toString();
        lastName = lastNameET.getText().toString();
        phoneNum = phoneNumET.getText().toString();

//        email = emailET.getText().toString();
        DocumentReference documentReference = db.collection("users").document(currentUser.getUid());

        Map<String, Object> map = new HashMap<>();

        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(phoneNum)) {
            map.put("first_name", firstName);
            map.put("last_name", lastName);
            map.put("phone_number", phoneNum);

            //set adds a new document with the fields in the map, if no such field exists, it removes it.
            //To preserve the fields, use SetOptions.merge() (eg: .set(map, SetOptions.merge()) ...

            documentReference.update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "onSuccess: Updated document");
                            getActivity().onBackPressed();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "onFailure: Failed to update profile: " + e.getMessage());
                            Toast.makeText(getActivity(), "Failed to update profile!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "All fields must be filled!", Toast.LENGTH_SHORT).show();
        }



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