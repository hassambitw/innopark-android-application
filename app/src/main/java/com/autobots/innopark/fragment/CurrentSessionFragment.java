package com.autobots.innopark.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.autobots.innopark.activity.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.util.DatabaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    String paymentLink;

    Button payBtn;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    //firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("avenues");

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

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

        payBtn = v.findViewById(R.id.id_sessions_link_paypal);



        setupToolbar(v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

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
                paymentLink = bundle.getString("paymentLink");
                vehicle = bundle.getString("vehicle_num");
                tariff = bundle.getDouble("tariff");
                if (!bundle.getString("paymentLink").isEmpty()) paymentLink = bundle.getString("paymentLink");



                startTime = (Date) bundle.getSerializable("start_time");
                formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                formatted_date = formatter.format(startTime);
                startTimeTV.setText(formatted_date);
                if (location != null) locationTV.setText("To " + location.trim());
                parkingSpotTV.setText(parkingSpot.trim() + " (Level " + parkingLevel + ")");
                vehicleTV.setText(vehicle);
                tariffTV.setText(Double.toString(tariff));

                payBtn.setOnClickListener((v) -> {
                    loadNoPaymentLinkForActive();
                });

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
                if (!StringUtils.isEmpty("paymentLink")) paymentLink = bundle.getString("paymentLink");



                startTime = (Date) bundle.getSerializable("start_time2");
                formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                formatted_date = formatter.format(startTime);
                startTimeTV.setText(formatted_date);
                if (location != null) locationTV.setText("To " + location.trim());
                parkingSpotTV.setText(parkingSpot.trim() + " (Level " + parkingLevel + ")");
                vehicleTV.setText(vehicle);
                tariffTV.setText(Double.toString(tariff));

                payBtn.setOnClickListener((v) -> {
                    loadPaymentLink();
                });


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
//                if (!bundle.getString("paymentLink").isEmpty()) paymentLink = bundle.getString("paymentLink");



                startTime = (Date) bundle.getSerializable("start_time3");
                formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                formatted_date = formatter.format(startTime);
                startTimeTV.setText(formatted_date);
                if (location != null) locationTV.setText("To " + location.trim());
                parkingSpotTV.setText(parkingSpot.trim() + " (Level " + parkingLevel + ")");
                vehicleTV.setText(vehicle);
                tariffTV.setText(Double.toString(tariff));

                payBtn.setOnClickListener((v) -> {
                    loadNoPaymentLinkForActive();
                });
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
                if (!StringUtils.isEmpty("paymentLink")) paymentLink = bundle.getString("paymentLink");



                startTime = (Date) bundle.getSerializable("start_time4");
                formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                formatted_date = formatter.format(startTime);
                startTimeTV.setText(formatted_date);
                if (location != null) locationTV.setText("To " + location.trim());
                parkingSpotTV.setText(parkingSpot.trim() + " (Level " + parkingLevel + ")");
                vehicleTV.setText(vehicle);
                tariffTV.setText(Double.toString(tariff));

                payBtn.setOnClickListener((v) -> {
                    loadPaymentLink();
                });


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
//                if (!bundle.getString("paymentLink").isEmpty()) paymentLink = bundle.getString("paymentLink");



                startTime = (Date) bundle.getSerializable("start_time5");
                formatter = new SimpleDateFormat("E, dd MMM, HH:mm aa");
                formatted_date = formatter.format(startTime);
                startTimeTV.setText(formatted_date);
                if (location != null) locationTV.setText("To " + location.trim());
                parkingSpotTV.setText(parkingSpot.trim() + " (Level " + parkingLevel + ")");
                vehicleTV.setText(vehicle);
                tariffTV.setText(Double.toString(tariff));

                payBtn.setOnClickListener((v) -> {
                    loadPaidMessage();
                });

            }
        });



    }

    private void loadPaidMessage()
    {
        Toast.makeText(getActivity(), "You have already paid for this session!", Toast.LENGTH_SHORT).show();
        payBtn.setEnabled(false);
    }

    private void loadNoPaymentLinkForActive()
    {
        Toast.makeText(getActivity(), "You can't pay till this session is over!", Toast.LENGTH_SHORT).show();
    }

    private void loadPaymentLink()
    {
        Log.d(TAG, "loadPaymentLink: From either of the tariffs");
        db.collectionGroup("sessions_info")
                .whereNotEqualTo("end_datetime", null)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            //found documents
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {
                                if (!StringUtils.isEmpty(snapshot.getString("payment_link"))) {
//                                    Log.d(TAG, "onSuccess: Inside " + snapshot.getString("payment_link"));
                                    String URL = snapshot.getString("payment_link");
                                    Log.d(TAG, "onSuccess: Outside loop: " + URL);
                                    if (URL == null) {
                                        Log.d(TAG, "onSuccess: NULL: " + URL);
                                        Toast.makeText(getActivity(), "Link not generated yet!", Toast.LENGTH_SHORT).show();
                                        continue;
                                    }
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                                        startActivity(browserIntent);
//                                } else {
//                                        Toast.makeText(getActivity(), "Link not generated yet!", Toast.LENGTH_SHORT).show();
//                                    }
                                }
                            }
                        } else {
                            Log.d(TAG, "onSuccess: No subcollection documents found");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Sessions query failed due to: " + e.getMessage());
                    }
                });

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
