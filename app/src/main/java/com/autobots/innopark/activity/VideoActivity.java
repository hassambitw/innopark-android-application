package com.autobots.innopark.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.autobots.innopark.R;
import com.autobots.innopark.fragment.FinesListFragment;

public class VideoActivity extends AppCompatActivity
{

    VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoView = findViewById(R.id.videoView);

//        launchVideo();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = getIntent();
        String videoPath = i.getStringExtra("footage");
        launchVideo(videoPath);

//        this.get
//
//        String videoPath = "android.resource://" + this.getPackageName() + "/" + R.raw.leg_2;
//        launchVideo(videoPath);
    }

    private void launchVideo(String videoPath)
    {

        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            super.onBackPressed(); //replaced
        }
    }


//    @Override
//    public void sendString(String s) {
//        Fragment frag = getFragmentManager().findFragmentById(R.id.id_fragment_container_view);
//        launchVideo(s);
//    }
}


