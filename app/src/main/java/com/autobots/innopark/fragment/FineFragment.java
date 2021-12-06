package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.VideoActivity;
import com.autobots.innopark.data.DatabaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FineFragment extends Fragment
{

    Toolbar toolbar;
    TextView toolbar_title;
    VideoView videoView;
    Button payFine;
    Button disputeFine;
    ArrayList<String> vehiclesOwned;
    Button fullscreenBtn;
    TextView emptyView;
    TextView launchVideo;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("avenues");

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_fine, container, false);
//        videoView = view.findViewById(R.id.id_pending_fine_video_footage);
        payFine = view.findViewById(R.id.id_pending_fine_pay);
        disputeFine = view.findViewById(R.id.id_pending_fine_dispute_fine);
        emptyView = view.findViewById(R.id.id_pending_fine_empty_view);
        launchVideo = view.findViewById(R.id.id_pending_fine_message);

        launchVideo.setOnClickListener((v) -> {
            startActivity(new Intent(getActivity(), VideoActivity.class));
        });

//        fullscreenBtn = view.findViewById(R.id.fullscreen);
//        vehiclesOwned = new ArrayList<>();

        disputeFine.setOnClickListener((v) -> {
            startDisputeFineFragment();
        });


        setupToolbar(view);
//        setupVideoView(view);

        return view;
    }

    private void startDisputeFineFragment()
    {
        Fragment fragment = new DisputeFineFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    private void setupVideoView(View view)
    {
//        String videoPath = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.leg_2;
//        Uri uri = Uri.parse(videoPath);
//        videoView.setVideoURI(uri);
//
//        MediaController mediaController = new MediaController(getActivity());
//        videoView.setMediaController(mediaController);
//        mediaController.setAnchorView(videoView);
//        videoView.start();

//        fullscreenBtn.setOnClickListener((v) -> {
//            enterFullScreen();
//            fullscreenBtn.setVisibility(View.GONE);
//        });
    }

    private void enterFullScreen()
    {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置横屏
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title = toolbar.findViewById(R.id.id_toolbar_title);
        toolbar_title.setText("Pending Fine");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
