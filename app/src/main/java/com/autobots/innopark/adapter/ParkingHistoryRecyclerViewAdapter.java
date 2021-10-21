package com.autobots.innopark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.R;
import com.autobots.innopark.data.ParkingHistoryData;

import java.util.ArrayList;
import java.util.Arrays;

public class ParkingHistoryRecyclerViewAdapter extends RecyclerView.Adapter<ParkingHistoryRecyclerViewAdapter.parkingHistoryViewHolder>
{

    public ArrayList<ParkingHistoryData> parkingHistoryList;
    private Context mContext;

    public ParkingHistoryRecyclerViewAdapter(ArrayList<ParkingHistoryData> parkingHistoryList, Context mContext)
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
        ParkingHistoryData phd = parkingHistoryList.get(position);

        holder.parkingHistoryArea.setText(phd.getLocation());
        holder.parkingHistoryDate.setText(phd.getDate());
        holder.parkingHistorySpot.setText(phd.getParkingLevel());

        String vehicleDrivers = Arrays.asList(phd.getDrivers()).toString();
        holder.parkingHistoryDrivers.setText(vehicleDrivers.substring(1, vehicleDrivers.length() - 1));

        holder.parkingHistoryCar.setText(phd.getCarLicense() + " " + phd.getCarName());
        if (phd.isPaidStatus() == true) {
            holder.parkingHistoryTariffStatus.setText(phd.getPaidMessage());
            holder.parkingHistoryTariffStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.parking_history_paid_bg));
        } else {
            holder.parkingHistoryTariffStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.parking_history_unpaid_bg));
        }

        holder.parkingHistoryTariffStatus.setText(phd.getPaidMessage());
        holder.parkingHistoryTariffAmt.setText(String.valueOf(phd.getTariffPrice()));

    }

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


        public parkingHistoryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            parkingHistoryArea = itemView.findViewById(R.id.id_parking_history_location);
            parkingHistoryDate = itemView.findViewById(R.id.id_parking_history_date);
            parkingHistorySpot = itemView.findViewById(R.id.id_parking_history_parking_spot);
            parkingHistoryTime = itemView.findViewById(R.id.id_parking_history_time);
            parkingHistoryDrivers = itemView.findViewById(R.id.id_parking_history_drivers);
            parkingHistoryCar = itemView.findViewById(R.id.id_parking_history_car);
            parkingHistoryTariffStatus = itemView.findViewById(R.id.id_parking_history_tariff_status);
            parkingHistoryTariffAmt = itemView.findViewById(R.id.id_parking_history_tariff_amt);
        }
    }
}
