package com.autobots.innopark.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.R;
import com.autobots.innopark.data.UserApi;
import com.autobots.innopark.data.Vehicle;
import com.autobots.innopark.data.Vehicle2;

import java.util.ArrayList;
import java.util.Arrays;

public class VehicleRecyclerViewAdapter extends RecyclerView.Adapter<VehicleRecyclerViewAdapter.vehicleViewHolder>
{

    private static final String TAG = "VehicleRecyclerViewAdapter";

//    final private ArrayList<Vehicle> vehicleList;
    final private ArrayList<Vehicle2> vehicleList2;
    final private ArrayList<String> driverEmails;
    private ArrayList<String> vehiclesOwned;
    ArrayList<String> licenses;
    ArrayList<String> drivers;
    private Context mContext;
    private OnVehicleClickListener onVehicleClickListener;

    public VehicleRecyclerViewAdapter(ArrayList<Vehicle2> vehicleList2, ArrayList<String> driverEmails, Context mContext, OnVehicleClickListener onVehicleClickListener) {
        this.vehicleList2 = vehicleList2;
        this.mContext = mContext;
        this.onVehicleClickListener = onVehicleClickListener;
        this.driverEmails = driverEmails;
    }

    @NonNull
    @Override
    public vehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_vehicle, parent, false);
        return new vehicleViewHolder(view, onVehicleClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull vehicleViewHolder holder, int position) {

//        vehiclesOwned = new ArrayList<>();
        licenses = new ArrayList<>();
        drivers = new ArrayList<>();

        Vehicle2 vehicle = vehicleList2.get(position);
//        Log.d(TAG, "onBindViewHolder: " + driverEmails);
        holder.licenseNum.setText(vehicle.getDocumentId());
        Log.d(TAG, "onBindViewHolder: Driver emails: " + vehicle.getDriven_by());
        String driverEmails = vehicle.getDriven_by().toString();
//        Log.d(TAG, "onBindViewHolder: driver Emails variable: " + driverEmails);
//        Log.d(TAG, "onBindViewHolder: Substring: " + driverEmails.substring(1, driverEmails.length() - 1));
        holder.driverNames.setText(driverEmails.substring(1, driverEmails.length() - 1));


//        holder.driverNames.setText(driverName + ", ");
//        Log.d(TAG, "onBindViewHolder: " + vehicle.getDriverName() + " " + vehicle.getDriven_by());
//        holder.licenseNum.setText(vehicle.toString());

//        UserApi userApi = UserApi.getInstance();
//        vehiclesOwned = (ArrayList<String>) userApi.getVehiclesOwned();
//        Log.d(TAG, "onBindViewHolder: Vehicles Owned: " + vehiclesOwned);
//        Log.d(TAG, "onBindViewHolder: VehicleList: " + vehicle.getVehicles_owned());

//        String firstName = vehicle.getFirst_name();
//        String lastName = vehicle.getLast_name();

//        ArrayList<String> fetchedOwnedCars = new ArrayList<>();
//        fetchedOwnedCars.add(String.valueOf(vehicle.getVehicles_owned()));

//        ArrayList<String> fetchedDrivenCars = new ArrayList<>();
//        fetchedDrivenCars.add(String.valueOf(vehicle.getVehicles_driven()));
//        Log.d(TAG, "onBindViewHolder: " + fetchedOwnedCars);
//
//        for (String s : fetchedOwnedCars) {
//            Log.d(TAG, "Inside for loop: " + s);
//            if (Arrays.toString(new ArrayList[]{vehiclesOwned}).contains(s)) {
//                holder.licenseNum.setText(s);
//            } else {
////                Log.d(TAG, "onBindViewHolder: Not inside if");
////                Log.d(TAG, "onBindViewHolder: " + vehiclesOwned);
//            }
////            if ((innerList != null || innerList.isEmpty()) && innerList.contains(fetchedOwnedCars)) {
////                Log.d(TAG, "onBindViewHolder: Inside this IF" + innerList.stream().toArray());
////            }
//        }

//        for (int i = 0; i < vehicleList.size(); i++) {
//            Log.d(TAG, "Inside for loop: " + vehicle.getVehicles_owned());
//            if (Arrays.toString(new ArrayList[]{vehiclesOwned}).contains(vehicle.getVehicles_owned().get(position))) {
////                holder.licenseNum.setText(vehicle.getVehicles_owned());
//                Log.d(TAG, "Inside first if: " + vehicle.getVehicles_owned());
//            }
////            Log.d(TAG, "onBindViewHolder: Vehicles owned: " + vehicle.getVehicles_owned() +  "\n");
////            Log.d(TAG, "onBindViewHolder: Vehicles driven: " + vehicle.getVehicles_driven() + "\n");
//             if (vehiclesOwned.contains(vehicle.getVehicles_driven())) {
////                 holder.driverNames.setText(vehicle.getFirst_name() + " " + vehicle.getLast_name() + ", ");
//                 Log.d(TAG, "onBindViewHolder: Inside second if: " + vehicle.getVehicles_driven());
//             }
//        }

//        Log.d(TAG, "onBindViewHolder: Outside for loop");


//        String vehicleDrivers = Arrays.asList(vehicle.getDriverName()).toString();

//        holder.licenseNum.setText(vehicle.getLicensePlateNum());
//        holder.driverNames.setText(vehicleDrivers.substring(1, vehicleDrivers.length() - 1));



    }

    @Override
    public int getItemCount() {
        return vehicleList2.size() + driverEmails.size();
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
