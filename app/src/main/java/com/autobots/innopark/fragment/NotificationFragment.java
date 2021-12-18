package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autobots.innopark.util.Config;
import com.autobots.innopark.activity.LoginActivity;
import com.autobots.innopark.R;
//import com.autobots.innopark.adapter.NotificationsRecyclerView2;
import com.autobots.innopark.adapter.NotificationsRecyclerViewAdapter;
import com.autobots.innopark.util.DatabaseUtils;
import com.autobots.innopark.data.models.NotificationData;
import com.autobots.innopark.data.models.UserToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NotificationFragment extends Fragment {

    private static final String TAG = "NotificationFragment";
    ArrayList<NotificationData> notifs = new ArrayList<>();
    Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView toolbar_title;
    private ArrayList<Map<String, Object>> notifs_array;
//    private ArrayList<Map<String, Object>> reversed_notifs_array;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    UserToken user_token;

    //firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    public NotificationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);

        mRecyclerView = view.findViewById(R.id.id_notifications_recycler_view);

        notifs_array = new ArrayList<>();
//        reversed_notifs_array = new ArrayList<>();

        setupToolbar(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadNotifications();
    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_notifications_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title = toolbar.findViewById(R.id.id_toolbar_title);
        toolbar_title.setText("Notification");
        //toolbar.setTitle("Menu");
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadNotifications()
    {
        db.collection("users_tokens")
                .whereEqualTo("email_address", Config.current_user_email)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                Log.d(Tags.SUCCESS.name(), "Successfully loading notification");
                Log.d(TAG, "onSuccess: Inside on Success" );
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot snapshot : snapshotList) {
                        try {
                            user_token = snapshot.toObject(UserToken.class);

                            notifs_array = user_token.getNotif();
                            Collections.reverse(notifs_array);
                            Log.d(TAG, "onSuccess: Inside loop");

//                            Log.d(Tags.SUCCESS.name(), "Notification: DATA FETCHED: "+ notifs_array);
                            Log.d(TAG, "onSuccess: " + "Notification data fetched");


                        } catch (Exception e) {
//                            Log.d(Tags.SUCCESS.name(), "Notification: Failed to get users_tokens object " + e.getMessage());
                            Log.d(TAG, "onSuccess: " + "Notification: failed to get users_tokens object" + e.getMessage());
                        }

                    }
                    setupRecyclerView();

                } else {
//                    Log.d(Tags.FAILURE.name(), "Notifications: no such user token exists in db");
                    Log.d(TAG, "Notifications: no such user token exists in db");
                }
            }
        });
    }



    private void setupRecyclerView()
    {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new NotificationsRecyclerViewAdapter(getActivity(), notifs_array);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}