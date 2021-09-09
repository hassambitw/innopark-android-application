package com.autobots.innopark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.autobots.innopark.fragment.HomeFragment;
import com.autobots.innopark.fragment.MenuFragment;
import com.autobots.innopark.fragment.NotificationFragment;
import com.autobots.innopark.fragment.PaymentFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DashboardActivity extends AppCompatActivity {

    private NavigationBarView bottom_nav;
    final FragmentManager fm = getSupportFragmentManager();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottom_nav = findViewById(R.id.id_bottom_nav_menu);
        bottom_nav.setOnItemSelectedListener(navigationItemSelectedListener);
        firebaseAuth = FirebaseAuth.getInstance();

        if (savedInstanceState == null)
        {
            bottom_nav.setSelectedItemId(R.id.id_bottom_nav_home);
        }

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
                case R.id.id_bottom_nav_payment:
                    selected_fragment = new PaymentFragment();
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