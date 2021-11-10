package com.autobots.innopark.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.R;
import com.autobots.innopark.data.ParkingHistoryData;
import com.autobots.innopark.data.Session;
import com.autobots.innopark.data.Tariff;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ParkingHistoryRecyclerViewAdapter extends RecyclerView.Adapter<ParkingHistoryRecyclerViewAdapter.parkingHistoryViewHolder>
{

    private static final String TAG = "ParkingHistoryRecyclerViewAdapter";
    public ArrayList<Session> parkingHistoryList;
    private Context mContext;

    public ParkingHistoryRecyclerViewAdapter(ArrayList<Session> parkingHistoryList, Context mContext)
    {
        this.parkingHistoryList = parkingHistoryList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public parkingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_parking_history, parent, false);
        return new parkingHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull parkingHistoryViewHolder holder, int position)
    {
        Session phd = parkingHistoryList.get(position);

        Double rate = phd.getRate_per_hour();
        String parking_location = phd.getAvenue_name();
        String parking_spot = phd.getParking_id();
        char parking_lvl = parking_spot.charAt(0);
        Date end_time = phd.getEnd_datetime();

        SimpleDateFormat formatter;
        String formatted_date;

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatted_date = formatter.format(end_time);

       if (phd.getAvenue_name() != null) holder.parkingHistoryArea.setText(phd.getAvenue_name());
       else {
           holder.parkingHistoryArea.setText("Unknown Destination");
           holder.parkingHistoryArea.setTextColor(ContextCompat.getColor(mContext,R.color.slategray));
       }

       holder.parkingHistorySpot.setText("L" + parking_lvl);

       holder.parkingHistoryDate.setText(formatted_date);

        Date start_time = phd.getStart_datetime();

        if (end_time != null) {
            long difference_in_time = end_time.getTime() - start_time.getTime();
            long difference_in_hours = TimeUnit.MILLISECONDS.toHours(difference_in_time) % 24;
            long difference_in_minutes = TimeUnit.MILLISECONDS.toMinutes(difference_in_time) % 60;
            long difference_in_seconds = TimeUnit.MILLISECONDS.toSeconds(difference_in_time) % 60;
            if (difference_in_hours <= 0) holder.parkingHistoryTime.setText(difference_in_hours + " Hour(s)");
            else {
                holder.parkingHistoryTime.setText(difference_in_minutes + " Minutes");
            }
            if (difference_in_minutes <= 1) holder.parkingHistoryTime.setText(difference_in_seconds + " Seconds");
        }

        Date current_date = new Date();
        Date due_date = phd.getDue_datetime();
        boolean overdue = false;

        if (due_date != null) {
            if (current_date.after(due_date)) overdue = true;
        }

        Log.d(TAG, "onBindViewHolder: Current date: " + current_date + " " + " Due Date " + due_date);

//        holder.parkingHistoryTime.setText(phd.getDuration());

//        String vehicleDrivers = Arrays.asList(phd.getDrivers()).toString();
//        holder.parkingHistoryDrivers.setText(vehicleDrivers.substring(1, vehicleDrivers.length() - 1));

        holder.parkingHistoryCar.setText(phd.getVehicle());

        if (phd.isIs_paid() == true) {
            holder.parkingHistoryTariffStatus.setText("Paid");
            holder.parkingHistoryTariffStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.parking_history_paid_bg));
            holder.parkingHistoryAED.setTextColor(ContextCompat.getColor(mContext,R.color.black_1));
            holder.parkingHistoryTariffAmt.setTextColor(ContextCompat.getColor(mContext,R.color.black_1));
        } else if (phd.isIs_paid() == false && overdue == false){
            holder.parkingHistoryTariffStatus.setText("Unpaid");
            holder.parkingHistoryTariffStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.parking_history_unpaid_bg));
            holder.parkingHistoryAED.setTextColor(ContextCompat.getColor(mContext,R.color.black_1));
            holder.parkingHistoryTariffAmt.setTextColor(ContextCompat.getColor(mContext,R.color.black_1));
        }

        if (phd.isIs_paid() == false && overdue == true) {
            holder.parkingHistoryTariffStatus.setText("Unpaid (F)");
            holder.parkingHistoryTariffStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.parking_history_overdue_bg));
            holder.parkingHistoryAED.setTextColor(ContextCompat.getColor(mContext,R.color.red));
            holder.parkingHistoryTariffAmt.setTextColor(ContextCompat.getColor(mContext,R.color.red));

        }

//        holder.parkingHistoryTariffStatus.setText(phd.getPaidMessage());
        holder.parkingHistoryTariffAmt.setText(String.valueOf(round(phd.getTariff_amount(), 1)));

    }

    private double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();

            BigDecimal bd = BigDecimal.valueOf(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
    }


//    @Override
//    public int getItemViewType(int position) {
//        if (parkingHistoryList.get(position).isFinePaidStatus() == true)
//            return 0;
//
//        return 1;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
//        View view;
//
//        if (viewType == 0) {
//            view = layoutInflater.inflate(R.layout.row_item_parking_history, parent, false);
//            return new fineHistoryViewHolder(view);
//        }
//
//        view = layoutInflater.inflate(R.layout.row_item_parking_history, parent, false);
//        return new parkingHistoryViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//        ParkingHistoryData phd = parkingHistoryList.get(position);
//
//        if (phd.isFinePaidStatus() == true)
//        {
//            fineHistoryViewHolder fineHistoryViewHolder = (fineHistoryViewHolder) holder;
//            fineHistoryViewHolder.parkingHistoryArea.setText(phd.getLocation());
//            fineHistoryViewHolder.parkingHistoryDate.setText(phd.getDate());
//            fineHistoryViewHolder.parkingHistorySpot.setText(phd.getParkingLevel());
//            fineHistoryViewHolder.parkingHistoryTime.setText(phd.getDuration());
//
//            String vehicleDrivers = Arrays.asList(phd.getDrivers()).toString();
//            fineHistoryViewHolder.parkingHistoryDrivers.setText(vehicleDrivers.substring(1, vehicleDrivers.length() - 1));
//
//            fineHistoryViewHolder.parkingHistoryCar.setText(phd.getCarLicense() + " " + phd.getCarName());
//            if (phd.isTariffPaidStatus() == true) {
//                fineHistoryViewHolder.parkingHistoryTariffStatus.setText(phd.getPaidMessage());
//                fineHistoryViewHolder.parkingHistoryTariffStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.parking_history_paid_bg));
//            } else {
//                fineHistoryViewHolder.parkingHistoryTariffStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.parking_history_unpaid_bg));
//            }
//        }
//
//    }

    @Override
    public int getItemCount() {
        return parkingHistoryList.size();
    }

    public class parkingHistoryViewHolder extends RecyclerView.ViewHolder
    {
        TextView parkingHistoryArea;
        TextView parkingHistoryDate;
        TextView parkingHistorySpot;
        TextView parkingHistoryTime;
        TextView parkingHistoryDrivers;
        TextView parkingHistoryCar;
        TextView parkingHistoryTariffStatus;
        TextView parkingHistoryTariffAmt;
        TextView parkingHistoryAED;


        public parkingHistoryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            parkingHistoryArea = itemView.findViewById(R.id.id_parking_history_location);
            parkingHistoryDate = itemView.findViewById(R.id.id_parking_history_date);
            parkingHistorySpot = itemView.findViewById(R.id.id_parking_history_parking_spot);
            parkingHistoryTime = itemView.findViewById(R.id.id_parking_history_time);
            parkingHistoryAED = itemView.findViewById(R.id.id_parking_history_AED);
//            parkingHistoryDrivers = itemView.findViewById(R.id.id_parking_history_drivers);
            parkingHistoryCar = itemView.findViewById(R.id.id_parking_history_car);
            parkingHistoryTariffStatus = itemView.findViewById(R.id.id_parking_history_tariff_status);
            parkingHistoryTariffAmt = itemView.findViewById(R.id.id_parking_history_tariff_amt);
        }
    }

//    public class fineHistoryViewHolder extends RecyclerView.ViewHolder
//    {
//        TextView parkingHistoryArea;
//        TextView parkingHistoryDate;
//        TextView parkingHistorySpot;
//        TextView parkingHistoryTime;
//        TextView parkingHistoryDrivers;
//        TextView parkingHistoryCar;
//        TextView parkingHistoryTariffStatus;
//        TextView parkingHistoryTariffAmt;
//
//
//        public fineHistoryViewHolder(@NonNull View itemView)
//        {
//            super(itemView);
//            parkingHistoryArea = itemView.findViewById(R.id.id_parking_history_location);
//            parkingHistoryDate = itemView.findViewById(R.id.id_parking_history_date);
//            parkingHistorySpot = itemView.findViewById(R.id.id_parking_history_parking_spot);
//            parkingHistoryTime = itemView.findViewById(R.id.id_parking_history_time);
//            parkingHistoryDrivers = itemView.findViewById(R.id.id_parking_history_drivers);
//            parkingHistoryCar = itemView.findViewById(R.id.id_parking_history_car);
//            parkingHistoryTariffStatus = itemView.findViewById(R.id.id_parking_history_tariff_status);
//            parkingHistoryTariffAmt = itemView.findViewById(R.id.id_parking_history_tariff_amt);
//        }
//    }
}
