package com.autobots.innopark.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.R;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.Session;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TariffInactiveSessionRecyclerViewAdapter extends RecyclerView.Adapter<TariffInactiveSessionRecyclerViewAdapter.TariffViewHolder>
{

    private static final String TAG = "InactiveRecyclerView";
    final private ArrayList<Session> tariff_items;
//    final private ArrayList<Avenue> avenue_items;
    private Context mContext;
    private OnTariffClickListener onTariffClickListener;


    final FirebaseAuth firebaseAuth = DatabaseUtils.firebaseAuth;
    FirebaseUser currentUser;

    //firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("avenues");

    public TariffInactiveSessionRecyclerViewAdapter(ArrayList<Session> tariff_items, Context mContext, OnTariffClickListener onTariffClickListener)
    {
        this.tariff_items = tariff_items;
//        this.avenue_items = avenue_items;
        this.mContext = mContext;
        this.onTariffClickListener = onTariffClickListener;
    }



    @NonNull
    @Override
    public TariffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_tariff_3, parent, false);
        return new TariffViewHolder(view, onTariffClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffViewHolder holder, int position)
    {

        Session tariff = tariff_items.get(position);
//        Avenue avenue = avenue_items.get(position);

//        if (tariff.getEnd_datetime().toLowerCase().contains("-")) {
//            holder.itemView.setVisibility(View.GONE);
//            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//        } else {
//            holder.itemView.setVisibility(View.VISIBLE);
//            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (tariff.isIs_paid() != true) {
            holder.tariffLogo.setImageResource(R.drawable.card_unpaid_cross);
        }
        if (tariff.isIs_paid() == true) {
            holder.tariffLogo.setImageResource(R.drawable.card_paid_tick);
        }
        //holder.tariffAmount.setText(String.valueOf(tariff.getTariffRate()) + " DHS/hr");
        String parking_spot = tariff.getParking_id();
        //Log.d(TAG, "onBindViewHolder: " + parking_spot);
        char parking_lvl = parking_spot.charAt(0);
        String avenue_name = tariff.getAvenue_name();
        //Log.d(TAG, "onBindViewHolder: " + avenue_name);

        Date start_time = tariff.getStart_datetime();
        Date end_time = tariff.getEnd_datetime();

        if (end_time != null) {
            long difference_in_time = end_time.getTime() - start_time.getTime();
            long difference_in_seconds = TimeUnit.MILLISECONDS.toSeconds(difference_in_time) % 60;
            long difference_in_minutes = TimeUnit.MILLISECONDS.toMinutes(difference_in_time) % 60;
            long difference_in_hours = TimeUnit.MILLISECONDS.toHours(difference_in_time) % 24;
            holder.tariffParkingDuration.setText(difference_in_hours + "h : " + difference_in_minutes + "m : " + difference_in_seconds + "s");
        } else
        {
            holder.tariffParkingDuration.setText("-");
        }
        //Log.d(TAG, "onBindViewHolder: " + difference_in_hours + " " + difference_in_minutes + difference_in_seconds);

        String words[] = avenue_name.split(" ");

//        String avenue_name_initials = "";
//
//        for (String s : words) {
//            avenue_name_initials += Character.toUpperCase(s.charAt(0));
//            Log.d(TAG, "onBindViewHolder: " + avenue_name_initials);
//        }

        holder.tariffParkingArea.setText(avenue_name); // location
        holder.tariffParkingLevel.setText("L"+parking_lvl);
//        holder.tariffParkingSpace.setText(tariff.getParkingSpace());
        holder.tariffParkingSpot.setText("Spot " + parking_spot);


    }



    @Override
    public int getItemCount() {

        return tariff_items.size();
    }


        public class TariffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView tariffParkingArea;
        public TextView tariffParkingLevel;
        public TextView tariffParkingSpace;
        public TextView tariffParkingSpot;
        public TextView tariffParkingDuration;
        public ImageView tariffLogo;
        OnTariffClickListener onTariffClickListener;

        public TariffViewHolder(@NonNull View itemView, OnTariffClickListener onTariffClickListener) {
            super(itemView);
            tariffParkingArea = itemView.findViewById(R.id.id_card_inactive_session_parking_area);
            tariffParkingLevel = itemView.findViewById(R.id.id_card_inactive_session_parking_level);
            //tariffParkingSpace = itemView.findViewById(R.id.id_card_inactive_session_parking_space);
            tariffParkingSpot = itemView.findViewById(R.id.id_card_inactive_session_parking_spot);
            tariffParkingDuration = itemView.findViewById(R.id.id_card_inactive_session_duration);
            tariffLogo = itemView.findViewById(R.id.id_card_inactive_session_tariff_logo);

            this.onTariffClickListener = onTariffClickListener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view)
        {
            onTariffClickListener.onTariffClick(getAdapterPosition());
        }
    }

    public interface OnTariffClickListener
    {
        public void onTariffClick(int position);
    }
}
