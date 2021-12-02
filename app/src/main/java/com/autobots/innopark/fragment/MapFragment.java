package com.autobots.innopark.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.data.Avenue;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.UserApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapFragment extends Fragment {

    Toolbar toolbar;
    TextView toolbarTitle;
    final int REQUEST_CODE_FINE_LOCATION = 1234;
    private static final String TAG = "MapFragment";

    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    MapView mapView;
    private GoogleMap googleMap;
    Geocoder geocoder;

    //firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("avenues");

    List<String> vehicles;
    List<GeoPoint> avenueInfo;

    Marker myMarker;


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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //map stuff
        mapView = view.findViewById(R.id.id_maps_mapview);
        mapView.onCreate(savedInstanceState);

        vehicles = new ArrayList<>();
        avenueInfo = new ArrayList<>();

        geocoder = new Geocoder(getActivity());

        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Log.d(TAG, "onCreateView: Exception in MapsInitializer: " + e.getMessage());
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap mMap) {
                googleMap = mMap;

                getLastLocation();


                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);
                googleMap.getUiSettings().setScrollGesturesEnabled(true);

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                googleMap.setMyLocationEnabled(true);


                //dropping marker at a point on map
//                LatLng sydney = new LatLng(-34, 151);
//                googleMap.addMarker(new MarkerOptions().position(sydney).title("Sydney").snippet("Sample body"));
//
//                //zooming in to location of marker
//                CameraPosition cameraPosition = new CameraPosition.Builder()
//                        .target(sydney)
//                        .zoom(12)
//                        .build();
//
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                try {
                    List<Address> addresses =  geocoder.getFromLocationName("Current Location", 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);  // 4 seconds
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) return;

                for (Location location : locationResult.getLocations()) {
//                    Log.d(TAG, "onLocationResult: Locations: " + location.toString() + "\n");
                }
            }
        };

        setupToolbar(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            getLastLocation();
            checkSettingsAndStartLocationUpdates();
        } else {
            askLocationPermission();
        }

        UserApi userApi = UserApi.getInstance();
        vehicles = userApi.getVehiclesCombined();

        //get all avenue GPS points
       db.collection("avenues")
               .get()
               .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                   @Override
                   public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       Log.d(TAG, "onSuccess: Main query successful");
                       List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                       for (DocumentSnapshot snapshot : snapshotList) {
                           GeoPoint geopoint = snapshot.getGeoPoint("gps_coordinate");
                           Object avenueName = snapshot.get("name");


                           LatLng storedParkings = new LatLng(geopoint.getLatitude(), geopoint.getLongitude());
                           Log.d(TAG, "onSuccess: Parkings: " + avenueName.toString());
                           myMarker = googleMap.addMarker(new MarkerOptions().position(storedParkings).title(avenueName.toString()).snippet("Click here to view parking layout!"));



//                               googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                                   @Override
//                                   public boolean onMarkerClick(@NonNull Marker marker) {
//                                           if (marker.getTitle().equals("The University of Wollongong in Dubai")) {
//                                               Log.d(TAG, "onMarkerClick: In UOWD on click");
////                                               setupUOWDParkingLayoutFragment();
//                                           }
//
//                                           if (marker.getTitle().equals("Dubai Mall")) {
//                                                   Log.d(TAG, "onMarkerClick: In Dubai Mall");
////                                                   setupDubaiMallParkingLayoutFragment();
//                                           }
//                                           return false;
//                                   }
//                               });

                           googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                               @Override
                               public void onInfoWindowClick(@NonNull Marker marker) {
                                   if (marker.getTitle().equals("The University of Wollongong in Dubai")) {
                                       Log.d(TAG, "onInfoWindowClick: In UOWD on click");
                                       setupUOWDParkingLayoutFragment();
                                   }

                                   if (marker.getTitle().equals("Dubai Mall")) {
                                       Log.d(TAG, "onInfoWindowClick: In Dubai Mall");
                                       setupDubaiMallParkingLayoutFragment();
                                   }
//
                               }
                           });
                           Log.d(TAG, "onSuccess: GPS coordinates: " + geopoint + "\n");
                       }
                   }
               })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Log.d(TAG, "onFailure: Query failed: " + e.getMessage());
                   }
               });

    }

    private void setupDubaiMallParkingLayoutFragment()
    {
        Fragment fragment = new DubaiMallParkingLayoutFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    private void setupUOWDParkingLayoutFragment()
    {
        Fragment fragment = new UOWDParkingLayoutFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.id_fragment_container_view, fragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());

        Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(locationSettingsRequest);

        locationSettingsResponseTask
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.d(TAG, "onSuccess: Settings of device satisfied");
                        startLocationUpdates();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Settings of device not satisfied: " + e.getMessage());
                        if (e instanceof ResolvableApiException) {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            try {
                                startIntentSenderForResult(resolvableApiException.getResolution().getIntentSender(), 1001, null, 0, 0, 0, null);
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                sendIntentException.printStackTrace();
                            }
                        }
                    }
                });

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates()
    {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void setupToolbar(View view) {
        toolbar = view.findViewById(R.id.id_map_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Map");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }


    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("We value your privacy, but we need to access your location in order for you to see our parking locations and available parking spots in each area.")
                        .setCancelable(false)
                        .setTitle("Hey there!")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FINE_LOCATION);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().onBackPressed();
                            }
                        })
                        .show();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

//        Log.d(TAG, "onRequestPermissionsResult: Inside method");

        boolean dontAsk = false;

        if (requestCode == REQUEST_CODE_FINE_LOCATION) {
//            Log.d(TAG, "onRequestPermissionsResult: Inside first if");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG, "onRequestPermissionsResult: Inside");
                getLastLocation();
                checkSettingsAndStartLocationUpdates();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "onRequestPermissionsResult: User has denied permission");
                getActivity().onBackPressed();

            } else if (PermissionChecker.checkCallingOrSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
                dontAsk = true;
                getActivity().onBackPressed();
                Toast.makeText(getActivity(), "Sorry, you cannot view the map without enabling location services.", Toast.LENGTH_SHORT).show();

            }
        }

        if (dontAsk)
            startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getActivity().getPackageName())));
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    //we have location
//                    Log.d(TAG, "onSuccess: " + location.toString());
//                    Log.d(TAG, "onSuccess: Latitude: " + location.getLatitude());
//                    Log.d(TAG, "onSuccess: Longitude: " + location.getLongitude());

                    LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(currentPos).title("Current Location"));

                    //zooming in to location of marker
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(currentPos)
                            .zoom(15)
                            .build();

                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                } else {
                    Log.d(TAG, "onSuccess: Location was null ");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    //    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//        if (isGranted) {
//            //location granted
//            getLastLocation();
//
//        } else {
//            getActivity().onBackPressed();
//            Toast.makeText(getActivity(), "Sorry, you cannot view the map without enabling location services.", Toast.LENGTH_SHORT).show();
//        }
//    });

}
