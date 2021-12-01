package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.UserApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordFragment extends Fragment
{
    private static final String TAG = "ChangePasswordFragment";
    Toolbar toolbar;
    TextView toolbar_title;

    EditText currentPassET;
    EditText newPassET;
    Button save;
    ProgressBar progressBar;

    String currentPass;
    String newPass;

    FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        currentPassET = view.findViewById(R.id.id_change_password_current_pass);
        newPassET = view.findViewById(R.id.id_change_password_new_pass);
        save = view.findViewById(R.id.id_change_password_save);
        progressBar = view.findViewById(R.id.id_change_password_progressbar);

        save.setOnClickListener((v)-> {
            changePassword();
        });

        setupToolbar(view);

        return view;
    }

    private void changePassword()
    {
        progressBar.setVisibility(View.VISIBLE);
        currentPass = currentPassET.getText().toString();
        newPass = newPassET.getText().toString();

        String email = currentUser.getEmail();

        if (!TextUtils.isEmpty(currentPass) && !TextUtils.isEmpty(newPass))
        {
            AuthCredential authCredential = EmailAuthProvider.getCredential(email, currentPass);
            currentUser.reauthenticate(authCredential)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: User successfully authenticated.");
                            currentUser.updatePassword(newPass)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressBar.setVisibility(View.GONE);
                                            Log.d(TAG, "onSuccess: Password successfully changed");
                                            getActivity().onBackPressed();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getActivity(), "Failed to change password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "onFailure: Failed to change password: " + e.getMessage());
                                        }
                                    });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "onFailure: Failed to reauthenticate user: " + e.getMessage());
                            Toast.makeText(getActivity(), "Failed to authenticate user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
        {
            Toast.makeText(getActivity(), "Please fill in the password fields.", Toast.LENGTH_SHORT).show();
        }

    }

    private void setupToolbar(View view) {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title = toolbar.findViewById(R.id.id_toolbar_title);
        toolbar_title.setText("Change Email");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

}
