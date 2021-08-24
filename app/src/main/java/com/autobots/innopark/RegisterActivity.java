package com.autobots.innopark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    Button register_btn;
    RadioGroup radio_grp_vehicle;
    RadioButton radio_btn_yes;
    RadioButton radio_btn_no;
    EditText license_plate_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_btn = findViewById(R.id.id_register_btn);
        radio_grp_vehicle = findViewById(R.id.id_radio_grp_vehicle_register);
        radio_btn_yes = findViewById(R.id.id_radio_own_vehicle_yes_register);
        radio_btn_no = findViewById(R.id.id_radio_own_vehicle_no_register);
        license_plate_et = findViewById(R.id.id_license_plate_register);


        register_btn.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

        radio_grp_vehicle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.id_radio_own_vehicle_yes_register) {
                    license_plate_et.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.id_radio_own_vehicle_no_register) {
                    license_plate_et.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}