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
import com.autobots.innopark.data.Tariff;

import java.util.ArrayList;
import java.util.Arrays;

public class FineHistoryRecyclerViewAdapter extends RecyclerView.Adapter<FineHistoryRecyclerViewAdapter.fineHistoryViewHolder>
{

    public ArrayList<ParkingHistoryData> parkingHistoryList;
    private Context mContext;

    public FineHistoryRecyclerViewAdapter(ArrayList<ParkingHistoryData> parkingHistoryList, Context mContext)
    {
        this.parkingHistoryList = parkingHistoryList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public fineHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_fine_history, parent, false);
        return new fineHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull fineHistoryViewHolder holder, int position)
    {
        ParkingHistoryData phd = parkingHistoryList.get(position);

        holder.fineHistoryArea.setText(phd.getLocation());
        holder.fineHistoryDate.setText(phd.getDate());
        holder.fineHistoryTime.setText(phd.getDuration());

        String vehicleDrivers = Arrays.asList(phd.getDrivers()).toString();
        holder.fineHistoryDrivers.setText(vehicleDrivers.substring(1, vehicleDrivers.length() - 1));

        holder.fineHistoryCar.setText(phd.getCarLicense() + " " + phd.getCarName());
        if (phd.isTariffPaidStatus() == true) {
            holder.fineHistoryTariffStatus.setText(phd.getPaidMessage());
            holder.fineHistoryTariffStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.parking_history_paid_bg));
        } else {
            holder.fineHistoryTariffStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.parking_history_unpaid_bg));
        }

        holder.fineHistoryFineAmt.setText(String.valueOf(phd.getFinePrice()));

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

    public class fineHistoryViewHolder extends RecyclerView.ViewHolder
    {
        TextView fineHistoryArea;
        TextView fineHistoryDate;
        TextView fineHistoryTime;
        TextView fineHistoryDrivers;
        TextView fineHistoryCar;
        TextView fineHistoryTariffStatus;
        TextView fineHistoryFineAmt;


        public fineHistoryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            fineHistoryArea = itemView.findViewById(R.id.id_fine_history_location);
            fineHistoryDate = itemView.findViewById(R.id.id_fine_history_date);
            fineHistoryTime = itemView.findViewById(R.id.id_fine_history_time);
            fineHistoryDrivers = itemView.findViewById(R.id.id_fine_history_drivers);
            fineHistoryCar = itemView.findViewById(R.id.id_fine_history_car);
            fineHistoryTariffStatus = itemView.findViewById(R.id.id_fine_history_tariff_status);
            fineHistoryFineAmt = itemView.findViewById(R.id.id_fine_history_fine_amt);
        }
    }
}
