package com.autobots.innopark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.R;
import com.autobots.innopark.data.Driver;
import com.autobots.innopark.data.User;

import java.util.ArrayList;

public class DriverRecyclerView extends RecyclerView.Adapter<DriverRecyclerView.driverViewHolder>
{

    private static final String TAG = "DriverRecyclerView";
    private ArrayList<Driver> driverList;
    private Context mContext;

    public DriverRecyclerView(ArrayList<Driver> driverList, Context mContext)
    {
        this.driverList = driverList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DriverRecyclerView.driverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_time_driver, parent, false);
        return new driverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverRecyclerView.driverViewHolder holder, int position)
    {
        Driver driver = driverList.get(position);

        holder.driverName.setText(driver.getFirst_name() + " " + driver.getLast_name());
        holder.driverEmail.setText(driver.getEmail_address());
//        holder.driverAge.setText(driver.getDriverAge());
//        holder.driverDOB.setText(driver.getDriverDOB());
//        holder.driverNationality.setText(driver.getDriverNationality());
    }

    @Override
    public int getItemCount()
    {
        return driverList.size();
    }

    public class driverViewHolder extends RecyclerView.ViewHolder
    {
        TextView driverName;
        TextView driverEmail;


        public driverViewHolder(@NonNull View itemView) {
            super(itemView);
            driverName = itemView.findViewById(R.id.id_add_drivers_name);
            driverEmail = itemView.findViewById(R.id.id_add_drivers_email);

        }
    }
}
