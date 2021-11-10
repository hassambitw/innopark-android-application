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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autobots.innopark.Config;
import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.adapter.MenuRecyclerViewAdapter;
//import com.autobots.innopark.adapter.NotificationsRecyclerView2;
import com.autobots.innopark.adapter.NotificationsRecyclerViewAdapter;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.NotificationData;
import com.autobots.innopark.data.Session;
import com.autobots.innopark.data.Tags;
import com.autobots.innopark.data.UsersTokens;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationFragment extends Fragment {

    private static final String TAG = "NotificationFragment";
    ArrayList<NotificationData> notifs = new ArrayList<>();
    Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView toolbar_title;
    private ArrayList<UsersTokens> data;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    UsersTokens users_tokens;
    ArrayList<String> notif_titles;
    ArrayList<String> notif_bodies;

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

        data = new ArrayList<>();

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
        db.collection("users_tokens").whereEqualTo("token_id", Config.current_user_token)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(Tags.SUCCESS.name(), "Successfully loading notification");
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot snapshot : snapshotList) {
                        try {
                            users_tokens = snapshot.toObject(UsersTokens.class);
                            //unpaidTariffItems.add(tariff);
                            data.add(users_tokens);
                            Log.d(TAG, "onSuccess: Inside loop");

                            notif_titles = users_tokens.getNotif_title();
                            notif_bodies = users_tokens.getNotif_body();

                            Log.d(TAG, "onSuccess: " + notif_bodies + " " + notif_titles);

//                            Log.d(Tags.SUCCESS.name(), "Notification: DATA FETCHED"+ " "+notif_bodies);


                        } catch (Exception e) {
                            Log.d(Tags.SUCCESS.name(), "Notification: Failed to get users_tokens object " + e.getMessage());
                        }

                    }
                    setupRecyclerView();

                } else {
                    Log.d(Tags.FAILURE.name(), "Notifications: no such user token exists in db");
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
        mAdapter = new NotificationsRecyclerViewAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}