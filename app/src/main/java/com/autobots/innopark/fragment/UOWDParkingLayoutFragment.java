package com.autobots.innopark.fragment;

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

import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.data.DatabaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ortiz.touchview.TouchImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class UOWDParkingLayoutFragment extends Fragment
{

    Toolbar toolbar;
    TextView toolbar_title;
    private static final String TAG = "UOWDParkingLayoutFragment";

    TouchImageView bgMap;

    FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    int finalMapWidth;
    int finalMapHeight;

    Canvas canvas;
    private int right;


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
        OpenCVLoader.initDebug();

        bgMap = view.findViewById(R.id.parking_layout);

//        int mapWidth = bgMap.getDrawable().getIntrinsicWidth();
//        int mapHeight = bgMap.getDrawable().getIntrinsicHeight();
//        Log.d(TAG, "onCreateView: Map width: " + mapWidth + " Map height: " + mapHeight);

//        ViewTreeObserver vto = bgMap.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//                bgMap.getViewTreeObserver().removeOnPreDrawListener(this);
//                bgMap.setScaleType(ImageView.ScaleType.CENTER);
//                finalMapWidth = bgMap.getMeasuredHeight();
//                finalMapHeight = bgMap.getMeasuredWidth();
//                Log.d(TAG, "onPreDraw: Width: " + finalMapHeight + " Height: " + finalMapHeight);
//                return true;
//            }
//        });

//        bgMap.setImageDrawable(getResources().getDrawable(R.drawable.occupied_parking));
//
//        Log.d(TAG, "onCreateView: X: " +  bgMap.getX());
//        Log.d(TAG, "onCreateView: Y: " + bgMap.getY());

//        Mat m = null;
//        try {
//            m = Utils.loadResource(getActivity(), R.drawable.empty_lot_labeled, Imgcodecs.IMREAD_GRAYSCALE);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(),Bitmap.Config.ARGB_8888);
//        Log.d(TAG, "onCreateView: Cols: " + m.cols() + " Rows: " + m.rows());
//        Utils.matToBitmap(m, bm);

//        bgMap.setDrawingCacheEnabled(true);
//
//        // this is the important code :)
//        // Without it the view will have a dimension of 0,0 and the bitmap will be null
//        bgMap.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        bgMap.layout(0, 0, bgMap.getMeasuredWidth(), bgMap.getMeasuredHeight());
//
//        bgMap.buildDrawingCache(true);
//        Bitmap bm = Bitmap.createBitmap(bgMap.getDrawingCache());
//        bgMap.setDrawingCacheEnabled(false); // clear drawing cache

//        bgMap.buildDrawingCache();
//        Bitmap bm = bgMap.getDrawingCache();

        BitmapDrawable drawable = (BitmapDrawable) bgMap.getDrawable();
        Bitmap bm = drawable.getBitmap();

        Log.d(TAG, "onCreateView: Bitmap width: " + bm.getWidth() + " Bitmap height: " + bm.getHeight());
        bgMap.setImageBitmap(bm);


        Paint paint=new Paint();
        paint.setColor(Color.RED);

        Bitmap mutableBitmap = bm.copy(Bitmap.Config.ARGB_8888, true);

        canvas = new Canvas(mutableBitmap);

//        canvas.drawRect( 236, 20, 52, 87, paint);
        drawRectangle(236, 20, 52, 87, canvas, paint);
        bgMap.setImageBitmap(mutableBitmap);

//        bm.copyPixelsFromBuffer();
//        ByteBuffer byteBuffer = ar
//
//
////
//        int screenWidth = getActivity().getWindow().getWindowManager().getDefaultDisplay().getWidth();
//        Log.e(TAG,"screen width : "+screenWidth);
//        int screenHeight = getActivity().getWindow().getWindowManager().getDefaultDisplay().getHeight();
//        Log.d(TAG, "onCreateView: " + screenHeight);
////
//        int width = bm.getWidth();
//        Log.e(TAG,"bitmap width : "+width);
//        int height = bm.getHeight();
//        Log.e(TAG,"bitmap height : "+height);
//
//        float scale = 0;
//        if(width > height){
//            scale = (float) width / (float) height;
//        } else if(height > width){
//            scale = (float) height / (float) width;
//        }
//        Log.e(TAG,"scale : "+scale);
//
//        float newWidth = (float) screenWidth * scale;
//
//        Log.d(TAG,"new height : "+newWidth);
//
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, screenWidth, (int) newWidth, true);
//        Log.e(TAG,"new bitmap width : " + scaledBitmap.getWidth());
//        Log.e(TAG,"new bitmap height : " + scaledBitmap.getHeight());
//
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, (int)newWidth);
//        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//        view.setLayoutParams(params);
//
//        bgMap.setImageBitmap(scaledBitmap);


//        String imagePath = getResources().getDrawable(Integer.parseInt("empty_lot_labeled")).toString();
//        String overlayPath = getResources().getDrawable(Integer.parseInt("occupied_parking")).toString();
//
//
//        overlayImage(imagePath, overlayPath, 236, 288, (288-236), (106-19));


//        setupToolbar(view);

        return view;
    }

    public void drawRectangle(int left, int top, int right, int bottom, Canvas canvas, Paint paint) {
        right = left + right;
        bottom = top + bottom;
        canvas.drawRect((float) left, (float) top, (float) right, (float) bottom, paint);

    }

    public static Bitmap cropCenter(Bitmap bmp) {
        int dimension = Math.min(bmp.getWidth(), bmp.getHeight());
        return ThumbnailUtils.extractThumbnail(bmp, dimension, dimension);
    }

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


