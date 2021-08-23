package com.autobots.innopark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.autobots.innopark.fragment.HomeFragment;
import com.autobots.innopark.fragment.NotificationFragment;
import com.autobots.innopark.fragment.PaymentFragment;
import com.google.android.material.navigation.NavigationBarView;


public class DashboardActivity extends AppCompatActivity {

    private NavigationBarView bottom_nav;
    final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottom_nav = findViewById(R.id.id_bottom_nav_menu);
        bottom_nav.setOnItemSelectedListener(navigationItemSelectedListener);

    }


    private NavigationBarView.OnItemSelectedListener navigationItemSelectedListener
            = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected_fragment = null;

            switch(item.getItemId()) {
                case R.id.id_bottom_nav_home:
                    selected_fragment = new HomeFragment();
                    fm.beginTransaction().replace(R.id.id_fragment_container_view, selected_fragment).commit();
                    break;
                case R.id.id_bottom_nav_payment:
                    selected_fragment = new PaymentFragment();
                    fm.beginTransaction().replace(R.id.id_fragment_container_view, selected_fragment).commit();
                    break;
                case R.id.id_bottom_nav_notifications:
                    selected_fragment = new NotificationFragment();
                    fm.beginTransaction().replace(R.id.id_fragment_container_view, selected_fragment).commit();
                    break;
                case R.id.id_bottom_nav_menu:
                    //selected_fragment = new MenuFragment();
                    //fm.beginTransaction().replace(R.id.id_fragment_container_view, selected_fragment).commit();
                    break;
            }
            return true;
        }
    };
}