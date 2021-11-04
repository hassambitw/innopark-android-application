package com.autobots.innopark.fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autobots.innopark.Config;
import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.data.Callbacks.HashmapCallback;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.Session;
import com.autobots.innopark.data.Tags;
import com.autobots.innopark.data.User;
import com.autobots.innopark.data.UserApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class HomeFragment extends Fragment
{

    ImageView arrow;
    ImageView profile;
    CardView parkingCardView;
    CardView mapCardView;
    CardView activeSessionView;
    TextView activeSessionParking;
    TextView activeSessionLocation;
    CardView fineCardView;
    TextView profileName;

    String parent_id;

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    //firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("avenues");

    ArrayList<String> myList = new ArrayList<>();
    List<String> newList = new ArrayList<>();
    List<String> vehiclesDriven = new ArrayList<>();





    public HomeFragment()
    {

    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        //        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_2, container, false);

        arrow = view.findViewById(R.id.id_home_arrow);
        profile = view.findViewById(R.id.id_home_profile);
        profileName = view.findViewById(R.id.id_home_name);
        parkingCardView = view.findViewById(R.id.id_parking_history_card_view);
        activeSessionView = view.findViewById(R.id.id_home_card_active_session);
        activeSessionParking = view.findViewById(R.id.id_home_card_parking_lvl);
        activeSessionLocation = view.findViewById(R.id.id_home_card_parking_location);
        fineCardView = view.findViewById(R.id.id_home_card_fine_history);
        mapCardView = view.findViewById(R.id.id_home_card_view_map);

        myList.add("Hello");
        myList.add("Hello");
        System.out.println(myList);

        newList = myList
                .stream()
                .distinct()
                .collect(Collectors.toList());




        String userId = UserApi.getInstance().getUserId();

        parkingCardView.setOnClickListener((v) -> {
            startParkingHistoryFragment();
        });

        activeSessionView.setOnClickListener((v) -> {
            startCurrentSessionFragment();
        });

        profile.setOnClickListener((v) -> {
            startProfileFragment();
        });

        mapCardView.setOnClickListener((v) -> {
            startMapFragment();
        });

        fineCardView.setOnClickListener((v) -> {
            startParkingHistoryFragmentAgain();
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        UserApi userApi = UserApi.getInstance();
        String email = userApi.getUserEmail();
        String currentUserUid = currentUser.getUid();

        //db query for getting fullname and vehicles
        User.getUser(email, new HashmapCallback() {
            @Override
            public void passHashmapResult(HashMap<String, Object> result) {
                if(!result.isEmpty()){

                    String firstName = (String) result.get("first_name");
                    String lastName = (String) result.get("last_name");
                    String fullName = firstName + " " + lastName;

                    //getting vehicles owned
                    List<String> vehiclesOwned = (List<String>) result.get("vehicles_owned");
                    userApi.setVehiclesOwned(vehiclesOwned);

                    //vehicles driven
                    //vehiclesDriven = (List<String>) result.get("vehicles_driven");
                    userApi.setVehiclesDriven((List<String>) result.get("vehicles_driven"));
                    Log.d("Tag", "passHashmapResult: " + userApi.getVehiclesDriven());

                    profileName.setText(fullName);
                    userApi.setUserId(currentUserUid);
                    userApi.setUsername(fullName);


                    db.collectionGroup("sessions_info")
                            .whereEqualTo("end_datetime", null)
                            .whereIn("vehicle", userApi.getVehiclesDriven())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        Log.d("TAG", "onSuccess: Inside onSuccess");
                                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                        for (DocumentSnapshot snapshot: snapshotList) {
                                            Session session = snapshot.toObject(Session.class);
                                            //System.out.println(session.getParking_id());
                                            //Log.d("TAG", "onSuccess: " + snapshot.getId());
                                            activeSessionParking.setText("Level " + session.getParking_id().charAt(0));

                                            parent_id = String.valueOf(snapshot.getReference().getParent().getParent().getId());
                                            Log.d(TAG, "onSuccess: " + parent_id);

                                            db.collection("avenues").document(parent_id)
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            String venue = documentSnapshot.getString("name");
                                                            Log.d(TAG, "onSuccess: " + documentSnapshot.getString("name"));
                                                            activeSessionLocation.setText(venue.trim());
                                                        }
                                                    });
                                        }
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure of subcollection: Not reached inside query" + e.getMessage());
                                }
                            });


                    Log.w(Tags.SUCCESS.name(), result.toString());

                }else Log.w(Tags.FAILURE.name(), "ERROR: USER NOT FOUND");
            }
        });

        //vehiclesDriven.add(String.valueOf(userApi.getVehiclesDriven()));
        Log.d("TAG", "onStart: " + userApi.getVehiclesDriven());



    }

    private void startParkingHistoryFragmentAgain()
    {
        Fragment fragment = new ParkingHistoryFragment();
        Bundle bundle = new Bundle();

        bundle.putString("Message", "From Fine History Card");
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    private void startMapFragment()
    {
        Fragment fragment = new MapFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    private void startParkingHistoryFragment()
    {
        Fragment fragment = new ParkingHistoryFragment();
        Bundle bundle = new Bundle();

        bundle.putString("Message", "From Parking History Card");
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    private void startProfileFragment()
    {
        Fragment fragment = new ProfileFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    private void startCurrentSessionFragment()
    {
        Fragment fragment = new CurrentSessionFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }
}