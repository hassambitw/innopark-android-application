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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.VideoActivity;
import com.autobots.innopark.data.DatabaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    EditText licenseET;
    String license;
    EditText fineET;
    double fineAmount;
    EditText statusET;
    boolean status;
    EditText violationTypeET;
    String violationType;

    Bundle args;

    EditText dueDateET;
    Date dueDate;
    SimpleDateFormat formatter;
    String formatted_date;
    ProgressBar progressBar;

    String fineId;
    String fineDescription;
    String parentId;
    TextView fineDescriptionTV;

    String paymentLink;

    private static final String TAG = "FineFragment";

    String footage;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("avenues");

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    Button payBtn;

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
        licenseET = view.findViewById(R.id.id_pending_fine_license);
        fineET = view.findViewById(R.id.id_pending_fine_amount);
        statusET = view.findViewById(R.id.id_pending_fine_status);
        violationTypeET = view.findViewById(R.id.id_pending_fine_violation_type);
        dueDateET = view.findViewById(R.id.id_pending_due_date);
        progressBar = view.findViewById(R.id.id_pending_fine_progress_bar);
        fineDescriptionTV = view.findViewById(R.id.id_pending_fine_description);
        payBtn = view.findViewById(R.id.id_pending_fine_pay);


        args = new Bundle();

//        fullscreenBtn = view.findViewById(R.id.fullscreen);
//        vehiclesOwned = new ArrayList<>();

        disputeFine.setOnClickListener((v) -> {
            startDisputeFineFragment();
        });

//        launchVideo.setOnClickListener((v) -> {
//            startActivity(new Intent(getActivity(), VideoActivity.class));
//        });


        setupToolbar(view);
//        setupVideoView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().getSupportFragmentManager().setFragmentResultListener("From Fine List", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                license = result.getString("license");
                fineAmount = result.getDouble("fineAmount");
                status = result.getBoolean("status");
                violationType = result.getString("violationType");
                footage = result.getString("footage");
                Log.d(TAG, "onFragmentResult: " + footage);
                dueDate = (Date) result.getSerializable("dueDate");
                fineId = result.getString("fineId");
                fineDescription = result.getString("fineDescription");
                parentId = result.getString("parentId");
                fineDescriptionTV.setText(fineDescription);

                paymentLink = result.getString("paymentLink");
//                Log.d(TAG, "onFragmentResult: Due Date: " + dueDate);

                licenseET.setText(license);
                fineET.setText(fineAmount + "");
                if (status == true) {
                    statusET.setText("Paid");
                    payBtn.setOnClickListener((v) -> {
                        Toast.makeText(getActivity(), "You've already paid for this fine!", Toast.LENGTH_SHORT).show();
                    });
                }
                else {
                    statusET.setText("Unpaid");
                    payBtn.setOnClickListener((v) -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentLink));
                        startActivity(browserIntent);
                    });
                }
                violationTypeET.setText(violationType);
                if (footage == "Null") {
                    launchVideo.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText("There's no footage for fines of type: " + violationType);
                } else {
                    launchVideo.setOnClickListener((v) -> {
//                        progressBar.setVisibility(View.VISIBLE);
                        Intent i = new Intent(getActivity(), VideoActivity.class);
                        i.putExtra("footage", footage);
                        startActivity(i);
//                        progressBar.setVisibility(View.GONE);
                    });
                }
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                formatted_date = formatter.format(dueDate);
                dueDateET.setText(formatted_date);



            }
        });
    }

    private void startDisputeFineFragment()
    {
        Fragment fragment = new DisputeFineFragment();

        args.putString("fineType", violationType);
        args.putString("license", license);
        args.putDouble("fineAmount", fineAmount);
        args.putString("fineId", fineId);
        args.putString("parentId", parentId);


        getActivity().getSupportFragmentManager().setFragmentResult("From Fine Fragment", args);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
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
