package com.autobots.innopark;

import static android.content.ContentValues.TAG;

import com.autobots.innopark.data.Callbacks.HashmapCallback;
import com.autobots.innopark.data.DatabaseUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.autobots.innopark.data.Tags;
import com.autobots.innopark.data.User;
import com.autobots.innopark.data.UserApi;
import com.autobots.innopark.data.UserSessionManager;
import com.autobots.innopark.fragment.HomeFragment;
import com.autobots.innopark.fragment.MenuFragment;
import com.autobots.innopark.fragment.NotificationFragment;
import com.autobots.innopark.fragment.ParkingFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;


public class DashboardActivity extends AppCompatActivity {

    private NavigationBarView bottom_nav;
    final FragmentManager fm = getSupportFragmentManager();
    private FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    private FirebaseUser currentUser;
    private String currentUserUid;
    private View decorView;

    //shared preference for retrieving info from login state
    SharedPreferences userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setStatusBarGradiant(this);

        decorView = getWindow().getDecorView();
        bottom_nav = findViewById(R.id.id_bottom_nav_menu);
        bottom_nav.setOnItemSelectedListener(navigationItemSelectedListener);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

       // hideNavigationBar();

        if (savedInstanceState == null)
        {
            bottom_nav.setSelectedItemId(R.id.id_bottom_nav_home);
        }
    }

    public static void setStatusBarGradiant(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.gradient_primary_reverse);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    public void hideNavigationBar()
    {
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if (i == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus)
//    {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus)
//        {
//            decorView.setSystemUiVisibility(hideSystemBars());
//        }
//    }

    private int hideSystemBars()
    {
        return View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent (getApplicationContext(), LoginActivity.class));
        }

    }



    private NavigationBarView.OnItemSelectedListener navigationItemSelectedListener
            = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected_fragment = null;

            switch(item.getItemId()) {
                case R.id.id_bottom_nav_home:
                    selected_fragment = new HomeFragment();
                    fm.beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.id_fragment_container_view, selected_fragment)
                            .commit();
                    break;
                case R.id.id_bottom_nav_parking:
                    selected_fragment = new ParkingFragment();
                    fm.beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.id_fragment_container_view, selected_fragment)
                            .commit();
                    break;
                case R.id.id_bottom_nav_notifications:
                    selected_fragment = new NotificationFragment();
                    fm.beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.id_fragment_container_view, selected_fragment)
                            .commit();
                    break;
                case R.id.id_bottom_nav_menu:
                    //startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                    selected_fragment = new MenuFragment();
                    fm.beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.id_fragment_container_view, selected_fragment)
                            .commit();
                    break;
            }
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}