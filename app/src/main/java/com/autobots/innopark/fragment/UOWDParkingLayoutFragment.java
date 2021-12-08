package com.autobots.innopark.fragment;

import static com.autobots.innopark.R.color.darkjunglegreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.data.DatabaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ortiz.touchview.TouchImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UOWDParkingLayoutFragment extends Fragment
{

    Toolbar toolbar;
    TextView toolbar_title;
    private static final String TAG = "UOWDParkingLayoutFragment";

    private UOWDParkingLayoutFragment obj;

    TouchImageView bgMap;

    FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    int finalMapWidth;
    int finalMapHeight;

    //x, y, width, height

    int num = 631;

    int i = 0;
    int height = (368-283);

    int[] box642 = {642, 191, 283, (242-191), height};
    int[] box641 = {641, 247, 283, (298-247), height};
    int[] box640 = {640, 303, 283, (355-303), height};
    int[] box639 = {639, 359, 283, (411-359), height};
    int[] box638 = {638, 416, 283, (468-416), height};
    int[] box637 = {637, 472, 283, (525-472), height};
    int[] box636 = {636, 539, 283, (591-539), height};
    int[] box635 = {635, 596, 283, (647-596), height};
    int[] box634 = {634, 652, 283, (704-652), height};
    int[] box633 = {633, 708, 283, (760-708), height};
    int[] box632 = {632, 764, 283, (816-764), height};
//    int[] box631 = {820, 283, (872-820), height};

//    String[] boxNames = ["631", "632", "633", "634", ""]

    List<int[]> boxes;

    Bitmap bm;
    BitmapDrawable drawable;
    Paint paint;
    Bitmap mutableBitmap;

    SwipeRefreshLayout swipeRefreshLayout;



    Canvas canvas;
    private int right;
    private boolean occupied;

    String documentId;

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

        View view = inflater.inflate(R.layout.fragment_uowd_parking_layout, container, false);
//        OpenCVLoader.initDebug();

        bgMap = view.findViewById(R.id.parking_layout);

        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);

        drawable = (BitmapDrawable) bgMap.getDrawable();
        bm = drawable.getBitmap();
        boxes = new ArrayList<>();

//        Log.d(TAG, "onCreateView: Bitmap width: " + bm.getWidth() + " Bitmap height: " + bm.getHeight());
        bgMap.setImageBitmap(bm);

        mutableBitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(mutableBitmap);

        boxes.add(box632); boxes.add(box633); boxes.add(box634); boxes.add(box635);
        boxes.add(box636); boxes.add(box637); boxes.add(box638); boxes.add(box639); boxes.add(box640);
        boxes.add(box641); boxes.add(box642);


        paint = new Paint();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadParkings(documentId);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        canvas.drawRect( 236, 20, 52, 87, paint);
//        drawRectangle(236, 20, 52, 87, canvas, paint);

//        setupToolbar(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

//        String boxPrefix = "box";
//        num = 631;
//        String arrName = boxPrefix + "" + num;


//        paint.setColor(Color.RED);

        
        getActivity().getSupportFragmentManager().setFragmentResultListener("From Map Fragment", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                documentId = result.getString("documentId");
                Log.d(TAG, "onFragmentResult: Document Id: " + documentId);
                loadParkings(documentId);
            }
        });



    }

    private void loadParkings(String documentId) {
//        Field fd[] = UOWDParkingLayoutFragment.class.getDeclaredFields();
//
//
            db.collection("avenues")
                    .document(documentId)
                    .collection("parkings_info")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
//                                for (int i = 0; i < boxes.size(); i++) {

                                    for (DocumentSnapshot snapshot : snapshotList) {
//                                        convertToPercentage(boxes.get(i));
//                                        Log.d(TAG, "onSuccess: i: " + i);
                                        if (boxes.get(i)[0] == Integer.parseInt(snapshot.getId())) {
//                                            convertToPercentage(boxes.get(i));
//                                        for (int i = 0; i < boxes.size(); i++) {
//                                            if (boxes.get(i)[0] != Integer.parseInt(snapshot.getId())) continue;

//                                        if (snapshot.getBoolean("is_occupied") == true) {
//                                            Log.d(TAG, "IS OCCUPIED onSuccess: " + Integer.parseInt(snapshot.getId()) + " " + boxes.get(i)[0]);
//                                            if (boxes.get(i)[0] == Integer.parseInt(snapshot.getId())) {
//                                                convertToPercentage(boxes.get(i));
//                                                paint.setColor(getResources().getColor(R.color.red));
//                                            }
//                                        } else if (snapshot.getBoolean("is_occupied") == false) {
//                                            Log.d(TAG, "IS NOT OCCUPIED onSuccess: " + Integer.parseInt(snapshot.getId()) + " " + boxes.get(i)[0]);
//                                            if (boxes.get(i)[0] == Integer.parseInt(snapshot.getId())) {
//                                                convertToPercentage(boxes.get(i));
//                                                paint.setColor(getResources().getColor(R.color.parkinggreen));
//                                            }
//                                        }
//                                    }
//                                        Map<String, Object> map = new HashMap<>();
//                                        Log.d(TAG, "onSuccess: Snapshots: " + snapshot.get("is_occupied"));
//                                        Log.d(TAG, "onSuccess: Before if: i: " + i + " " + boxes.get(i)[0] + " " + " Document: " + snapshot.getId());
                                            Log.d(TAG, "onSuccess: " + String.valueOf(boxes.get(i)[0]) + " " + snapshot.getId());
//                                            if (boxes.get(i)[0] == Integer.parseInt(snapshot.getId())) {
//                                                Log.d(TAG, "onSuccess: " + String.valueOf(boxes.get(i)[0]) + " " + snapshot.getId());
//                                            Log.d(TAG, "onSuccess: i: " + i + " " + boxes.get(i)[0] + " " + " Document: " + snapshot.getId());
                                            if (snapshot.getBoolean("is_occupied") == true) {
//                                                    Log.d(TAG, "onSuccess: Is occupied: i: " + i + " " + boxes.get(i)[0] + " " + " Document: " + snapshot.getId());
//                                                convertToPercentage(boxes.get(i));
                                                paint.setColor(Color.RED);
//                                                occupied = true;
//                                                occupied = true;
                                            } else {
//                                                    Log.d(TAG, "onSuccess: Is Not Occupied: i" + i + " " + boxes.get(i)[0] + " " + " Document: " + snapshot.getId());
//                                                convertToPercentage(boxes.get(i));
                                                paint.setColor(getResources().getColor(R.color.parkinggreen));

//                                                occupied = false;
                                            }



//                                            continue;
//                                                Log.d(TAG, "onSuccess: Is Not Occupied: i" + i + " " + boxes.get(i)[0] + " " + " Document: " + snapshot.getId());
//                                            }
//                                        continue;
//                                        } for loop
                                        }

//                                        convertToPercentage(boxes.get(i));
                                        Log.d(TAG, "onSuccess: i" + i);
                                        convertToPercentage(boxes.get(i));
                                        if (i <= 9) i++;

                                    }


//                                    }
                            } else {
                                Log.d(TAG, "onSuccess: Found no parkings");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Error: " + e.getMessage());
                        }
                    });


//        if (!queryDocumentSnapshots.isEmpty()) {
//            //found parkings
//            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
//            for (DocumentSnapshot snapshot : snapshotList) {
////                                Log.d(TAG, "onSuccess: " + snapshot.get("is_occupied").toString());
//                if (snapshot.get("is_occupied").toString().toLowerCase() == "true") {
//                    //occupied parkings
//                    for (int i = 0; i < boxes.size(); i++) {
//                        if (boxes.get(i)[0] == Integer.parseInt(snapshot.getId())) {
//                            convertToPercentage(boxes.get(i));
//                            paint.setColor(getResources().getColor(R.color.red));
//                        }
//                    }
//                } else {
//                    for (int i = 0; i < boxes.size(); i++) {
//                        if (boxes.get(i)[0] == Integer.parseInt(snapshot.getId())) {
//                            convertToPercentage(boxes.get(i));
//                            paint.setColor(getResources().getColor(R.color.northtexasgreen));
//                        }
//                    }

    }



    public void convertToPercentage(int[] arr)
    {

        //x, y, width, height
//        Log.d(TAG, "convertToPercentage: Inside");


        float mapWidth = 1177;
        float mapHeight = 814;

//        Log.d(TAG, "convertToPercentage: " + arr[0] + " " + arr[1] + " " + arr[2] + " " + arr[3]);

       float x_percent = (float) arr[1] / mapWidth;
       float y_percent = (float) arr[2] / mapHeight;
       float width_percent = (float) arr[3] / mapWidth;
       float height_percent = (float) arr[4] / mapHeight;

//        Log.d(TAG, "convertToPercentage: x_percent: " + x_percent + " y_percent: " + y_percent + " width_percent: " + width_percent + " height_percent: " + height_percent);

        drawRectangle(x_percent, y_percent, width_percent, height_percent, canvas, paint);


//        Log.d(TAG, "onCreateView: Device map width: " + mutableBitmap.getWidth() + " Device map height: " + mutableBitmap.getHeight());


    }

//    public void drawRectangle(int left, int top, int right, int bottom, Canvas canvas, Paint paint) {
//        right = left + right;
//        bottom = top + bottom;
//        canvas.drawRect((float) left, (float) top, (float) right, (float) bottom, paint);
//
//        bgMap.setImageBitmap(mutableBitmap);
//
//    }

    public void drawRectangle(float left, float top, float right, float bottom, Canvas canvas, Paint paint) {

        //convert percentages back to device resolution value

//        Log.d(TAG, "drawRectangle: Inside drawRectangle");

        int deviceMapWidth = mutableBitmap.getWidth();
        int deviceMapHeight = mutableBitmap.getHeight();

//        Log.d(TAG, "drawRectangle: deviceMapWidth: " + deviceMapWidth + " deviceMapHeight: " + deviceMapHeight);

        left *= deviceMapWidth;
        top *= deviceMapHeight;
        right *= deviceMapWidth;
        bottom *= deviceMapHeight;


        right = left + right;
        bottom = top + bottom;

//        Log.d(TAG, "drawRectangle: x: " + left + " y: " + top + " Width: " + right + " Bottom: " + bottom );
        canvas.drawRect((float) left, (float) top, (float) right, (float) bottom, paint);
        bgMap.setImageBitmap(mutableBitmap);

//        Log.d(TAG, "drawRectangle: Set Map");


    }

//    public static Bitmap cropCenter(Bitmap bmp) {
//        int dimension = Math.min(bmp.getWidth(), bmp.getHeight());
//        return ThumbnailUtils.extractThumbnail(bmp, dimension, dimension);
//    }

    public static void overlayImage(String imagePath, String overlayPath, int x, int y, int width, int height) {
        Mat overlay = Imgcodecs.imread(overlayPath, Imgcodecs.IMREAD_UNCHANGED);
        Mat image = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_UNCHANGED);

        Rect rect = new Rect(x, y, width, height);
        Imgproc.resize(overlay, overlay, rect.size());
        Mat submat = image.submat(new Rect(rect.x, rect.y, overlay.cols(), overlay.rows()));
        overlay.copyTo(submat);
        Imgcodecs.imwrite(imagePath, image);
    }

//    private void setupToolbar(View view)
//    {
//        toolbar = view.findViewById(R.id.id_menu_toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar_title = toolbar.findViewById(R.id.id_toolbar_title);
//        toolbar_title.setText("UOWD Parking Layout");
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().onBackPressed();
//            }
//        });
//    }
}


