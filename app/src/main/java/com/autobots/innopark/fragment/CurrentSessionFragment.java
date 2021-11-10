package com.autobots.innopark.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.autobots.innopark.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentSessionFragment extends Fragment
{
    private static final String TAG = "Current Session";
    Toolbar toolbar;
    TextView toolbarTitle;
    TextView locationTV;
    String location;
    TextView startTimeTV;
    Date startTime;
    TextView endTimeTV;
    Date endTime;
    TextView vehicleTV;
    String vehicle;
    TextView parkingSpotTV;
    String parkingSpot;
    char parkingLevel;
    TextView tariffTV;
    double tariff;
    String formatted_date;
    SimpleDateFormat formatter;

    public static CurrentSessionFragment newInstance(Date startTime, Date endTime, String vehicle, String parkingSpot, char parking_level, String avenue_name, double tariff) {
        final Bundle creationArgs = new Bundle();
        creationArgs.putSerializable("start_time", startTime);
        creationArgs.putSerializable("end_time", endTime);
        creationArgs.putString("vehicle_num", vehicle);
        creationArgs.putString("avenue_name", avenue_name);
        creationArgs.putString("parking_spot", parkingSpot);
        creationArgs.putChar("parking_level", parking_level);
        creationArgs.putDouble("tariff", tariff);

        final CurrentSessionFragment instance = new CurrentSessionFragment();
        instance.setArguments(creationArgs);

        return instance;
    }


    public interface CurrentSessionFragmentListener {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_current_session, container, false);

        locationTV = v.findViewById(R.id.id_session_location);
        startTimeTV = v.findViewById(R.id.id_session_start_time);
        endTimeTV = v.findViewById(R.id.id_session_end_time);
        vehicleTV = v.findViewById(R.id.id_session_vehicle);
        parkingSpotTV = v.findViewById(R.id.id_session_parking_spot);
        tariffTV = v.findViewById(R.id.id_session_tariff_amt);

        getActivity().getSupportFragmentManager().setFragmentResultListener("requestKeyFromActive_Home", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {

                startTime = (Date) bundle.getSerializable("start_time");
                if ((Date) bundle.getSerializable("end_time") != null ) {
                    endTime = (Date) bundle.getSerializable("end_time");
                    formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                    formatted_date = formatter.format(endTime);
                    endTimeTV.setText(formatted_date);
                } else {
                    endTimeTV.setText("-");
                }
                location = bundle.getString("avenue_name");
                parkingSpot = bundle.getString("parking_spot");
                parkingLevel = bundle.getChar("parking_level");
                vehicle = bundle.getString("vehicle_num");
                tariff = bundle.getDouble("tariff");



                startTime = (Date) bundle.getSerializable("start_time");
                formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                formatted_date = formatter.format(startTime);
                startTimeTV.setText(formatted_date);
                if (location != null) locationTV.setText("To " + location.trim());
                parkingSpotTV.setText(parkingSpot.trim() + " (Level " + parkingLevel + ")");
                vehicleTV.setText(vehicle);
                tariffTV.setText(Double.toString(tariff));

            }
        });

        getActivity().getSupportFragmentManager().setFragmentResultListener("requestKeyFromUnpaidTariffList", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {

                startTime = (Date) bundle.getSerializable("start_time2");
                if ((Date) bundle.getSerializable("end_time2") != null ) {
                    endTime = (Date) bundle.getSerializable("end_time2");
                    formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                    formatted_date = formatter.format(endTime);
                    endTimeTV.setText(formatted_date);
                } else {
                    endTimeTV.setText("-");
                }
                location = bundle.getString("avenue_name2");
                parkingSpot = bundle.getString("parking_spot2");
                parkingLevel = bundle.getChar("parking_level2");
                vehicle = bundle.getString("vehicle_num2");
                tariff = bundle.getDouble("tariff2");



                startTime = (Date) bundle.getSerializable("start_time2");
                formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                formatted_date = formatter.format(startTime);
                startTimeTV.setText(formatted_date);
                if (location != null) locationTV.setText("To " + location.trim());
                parkingSpotTV.setText(parkingSpot.trim() + " (Level " + parkingLevel + ")");
                vehicleTV.setText(vehicle);
                tariffTV.setText(Double.toString(tariff));


            }
        });

        getActivity().getSupportFragmentManager().setFragmentResultListener("requestKeyFromActiveTariffList", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {

                startTime = (Date) bundle.getSerializable("start_time3");
                if ((Date) bundle.getSerializable("end_time3") != null ) {
                    endTime = (Date) bundle.getSerializable("end_time3");
                    formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                    formatted_date = formatter.format(endTime);
                    endTimeTV.setText(formatted_date);
                } else {
                    endTimeTV.setText("-");
                }
                location = bundle.getString("avenue_name3");
                parkingSpot = bundle.getString("parking_spot3");
                parkingLevel = bundle.getChar("parking_level3");
                vehicle = bundle.getString("vehicle_num3");
                tariff = bundle.getDouble("tariff3");



                startTime = (Date) bundle.getSerializable("start_time3");
                formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                formatted_date = formatter.format(startTime);
                startTimeTV.setText(formatted_date);
                if (location != null) locationTV.setText("To " + location.trim());
                parkingSpotTV.setText(parkingSpot.trim() + " (Level " + parkingLevel + ")");
                vehicleTV.setText(vehicle);
                tariffTV.setText(Double.toString(tariff));
            }
        });

        getActivity().getSupportFragmentManager().setFragmentResultListener("requestKeyFromUnpaid", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {

                startTime = (Date) bundle.getSerializable("start_time4");
                if ((Date) bundle.getSerializable("end_time4") != null ) {
                    endTime = (Date) bundle.getSerializable("end_time4");
                    formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                    formatted_date = formatter.format(endTime);
                    endTimeTV.setText(formatted_date);
                } else {
                    endTimeTV.setText("-");
                }
                location = bundle.getString("avenue_name4");
                parkingSpot = bundle.getString("parking_spot4");
                parkingLevel = bundle.getChar("parking_level4");
                vehicle = bundle.getString("vehicle_num4");
                tariff = bundle.getDouble("tariff4");



                startTime = (Date) bundle.getSerializable("start_time4");
                formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                formatted_date = formatter.format(startTime);
                startTimeTV.setText(formatted_date);
                if (location != null) locationTV.setText("To " + location.trim());
                parkingSpotTV.setText(parkingSpot.trim() + " (Level " + parkingLevel + ")");
                vehicleTV.setText(vehicle);
                tariffTV.setText(Double.toString(tariff));


            }
        });

        getActivity().getSupportFragmentManager().setFragmentResultListener("requestKeyFromPaidTariff", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {

                startTime = (Date) bundle.getSerializable("start_time5");
                if ((Date) bundle.getSerializable("end_time5") != null ) {
                    endTime = (Date) bundle.getSerializable("end_time5");
                    formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                    formatted_date = formatter.format(endTime);
                    endTimeTV.setText(formatted_date);
                } else {
                    endTimeTV.setText("-");
                }
                location = bundle.getString("avenue_name5");
                parkingSpot = bundle.getString("parking_spot5");
                parkingLevel = bundle.getChar("parking_level5");
                vehicle = bundle.getString("vehicle_num5");
                tariff = bundle.getDouble("tariff5");



                startTime = (Date) bundle.getSerializable("start_time5");
                formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                formatted_date = formatter.format(startTime);
                startTimeTV.setText(formatted_date);
                if (location != null) locationTV.setText("To " + location.trim());
                parkingSpotTV.setText(parkingSpot.trim() + " (Level " + parkingLevel + ")");
                vehicleTV.setText(vehicle);
                tariffTV.setText(Double.toString(tariff));


            }
        });

        setupToolbar(v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Session Info");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
