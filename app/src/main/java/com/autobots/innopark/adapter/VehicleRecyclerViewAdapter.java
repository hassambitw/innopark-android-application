package com.autobots.innopark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.R;
import com.autobots.innopark.data.Vehicle;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class VehicleRecyclerViewAdapter extends RecyclerView.Adapter<VehicleRecyclerViewAdapter.vehicleViewHolder>
{

    private ArrayList<Vehicle> vehicleList;
    private Context mContext;
    private OnVehicleClickListener onVehicleClickListener;

    public VehicleRecyclerViewAdapter(ArrayList<Vehicle> vehicleList, Context mContext, OnVehicleClickListener onVehicleClickListener) {
        this.vehicleList = vehicleList;
        this.mContext = mContext;
        this.onVehicleClickListener = onVehicleClickListener;
    }

    @NonNull
    @Override
    public vehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.vehicle_row_item, parent, false);
        return new vehicleViewHolder(view, onVehicleClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull vehicleViewHolder holder, int position) {

        Vehicle vehicle = vehicleList.get(position);
        String vehicleDrivers = Arrays.asList(vehicle.getDriverName()).toString();

        holder.licenseNum.setText(vehicle.getLicensePlateNum());
        holder.driverNames.setText(vehicleDrivers.substring(1, vehicleDrivers.length() - 1));



    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public static class vehicleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView licenseNum;
        TextView driverNames;
        OnVehicleClickListener onVehicleClickListener;


        public vehicleViewHolder(@NonNull View itemView, OnVehicleClickListener onVehicleClickListener) {
            super(itemView);
            licenseNum = itemView.findViewById(R.id.id_vehicle_row_item_license);
            driverNames = itemView.findViewById(R.id.id_vehicle_row_item_drivers);
            this.onVehicleClickListener = onVehicleClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onVehicleClickListener.onVehicleClick(getBindingAdapterPosition());
        }
    }

    public interface OnVehicleClickListener
    {
        public void onVehicleClick(int position);
    }
}
