package com.autobots.innopark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardActivity extends AppCompatActivity {

    NavigationBarView bottom_nav;

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
                    //selected_fragment = new HomeFragment();
                    break;
                case R.id.id_bottom_nav_payment:
                    startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
                    break;
            }

            return true;
        }
    };
}