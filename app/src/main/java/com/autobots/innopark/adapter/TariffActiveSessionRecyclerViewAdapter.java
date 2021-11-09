package com.autobots.innopark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.R;
import com.autobots.innopark.data.Session;
import com.autobots.innopark.data.Tariff;

import java.util.ArrayList;

public class TariffActiveSessionRecyclerViewAdapter extends RecyclerView.Adapter
{

    final private ArrayList<Session> tariff_items;
    private Context mContext;
    private OnTariffClickListener onTariffClickListener;

    public TariffActiveSessionRecyclerViewAdapter(ArrayList<Session> tariff_items, Context mContext, OnTariffClickListener onTariffClickListener)
    {
        this.tariff_items = tariff_items;
        this.mContext = mContext;
        this.onTariffClickListener = onTariffClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view;

        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.row_item_tariff_2, parent, false);
            return new ActiveViewHolder(view, onTariffClickListener);
        }

        view = layoutInflater.inflate(R.layout.row_item_tariff_2, parent, false);
        return new InactiveViewHolder(view, onTariffClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {


        Session tariff = tariff_items.get(position);

        Double rate = tariff.getRate_per_hour();
        String parking_location = tariff.getAvenue_name();
        String parking_spot = tariff.getParking_id();
        char parking_lvl = parking_spot.charAt(0);



        ActiveViewHolder activeViewHolder = (ActiveViewHolder) holder;

        activeViewHolder.tariffAmount.setText(rate + " DHS/hr");
        activeViewHolder.tariffParkingArea.setText(parking_location);
        activeViewHolder.tariffParkingSpot.setText("Spot " + parking_spot);
        activeViewHolder.tariffParkingLevel.setText("L" + parking_lvl);
        activeViewHolder.tariffParkingDuration.setText("-");



//        if (tariff.getDuration().contains("-")) {
//            ActiveViewHolder activeViewHolder = (ActiveViewHolder) holder;
//            activeViewHolder.itemView.setVisibility(View.VISIBLE);
//            activeViewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            activeViewHolder.tariffAmount.setText(String.valueOf(tariff.getTariffRate()) + " DHS/hr");
//            activeViewHolder.tariffParkingArea.setText(tariff.getParkingArea());
//            activeViewHolder.tariffParkingLevel.setText(tariff.getParkingLevel());
//            activeViewHolder.tariffParkingSpace.setText(tariff.getParkingSpace());
//            activeViewHolder.tariffParkingSpot.setText(tariff.getParkingSpot());
//            activeViewHolder.tariffParkingDuration.setText(tariff.getDuration());
//        } else {
//            ActiveViewHolder activeViewHolder = (ActiveViewHolder) holder;
//            activeViewHolder.itemView.setVisibility(View.GONE);
//            activeViewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//        }

//        if (holder.getItemViewType() == 0)
//        {
//            ActiveViewHolder activeViewHolder = (ActiveViewHolder) holder;
//            if (tariff.getDuration().toLowerCase().contains("-")) {
//                activeViewHolder.tariffAmount.setText(String.valueOf(tariff.getTariffRate()) + " DHS/hr");
//                activeViewHolder.tariffParkingArea.setText(tariff.getParkingArea());
//                activeViewHolder.tariffParkingLevel.setText(tariff.getParkingLevel());
//                activeViewHolder.tariffParkingSpace.setText(tariff.getParkingSpace());
//                activeViewHolder.tariffParkingSpot.setText(tariff.getParkingSpot());
//                activeViewHolder.tariffParkingDuration.setText(tariff.getDuration());
//            } else {
//                activeViewHolder.itemView.setVisibility(View.GONE);
//                activeViewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//            }
//        }
//        else if (holder.getItemViewType() == 1)
//        {
//            InactiveViewHolder inactiveViewHolder = (InactiveViewHolder) holder;
//            inactiveViewHolder.itemView.setVisibility(View.GONE);
//            inactiveViewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//            if (tariff.getDuration().toLowerCase().contains("-")) {
//                inactiveViewHolder.itemView.setVisibility(View.GONE);
//                inactiveViewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//            } else {
//                inactiveViewHolder.tariffAmount.setText(String.valueOf(tariff.getTariffRate()) + " DHS/hr");
//                inactiveViewHolder.tariffParkingArea.setText(tariff.getParkingArea());
//                inactiveViewHolder.tariffParkingLevel.setText(tariff.getParkingLevel());
//                inactiveViewHolder.tariffParkingSpace.setText(tariff.getParkingSpace());
//                inactiveViewHolder.tariffParkingSpot.setText(tariff.getParkingSpot());
//                inactiveViewHolder.tariffParkingDuration.setText(tariff.getDuration());
//            }
//        }

    }

    @Override
    public int getItemCount() {
        return tariff_items.size();
    }

    public class ActiveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public TextView tariffAmount;
        public TextView tariffParkingArea;
        public TextView tariffParkingLevel;
        public TextView tariffParkingSpace;
        public TextView tariffParkingSpot;
        public TextView tariffParkingDuration;
        OnTariffClickListener onTariffClickListener;

        public ActiveViewHolder(@NonNull View itemView, OnTariffClickListener onTariffClickListener) {
            super(itemView);
            tariffAmount = itemView.findViewById(R.id.id_card_active_session_tariff_amt);
            tariffParkingArea = itemView.findViewById(R.id.id_card_active_session_parking_area);
            tariffParkingLevel = itemView.findViewById(R.id.id_card_active_session_parking_level);
            //tariffParkingSpace = itemView.findViewById(R.id.id_card_active_session_parking_space);
            tariffParkingSpot = itemView.findViewById(R.id.id_card_active_session_parking_spot);
            tariffParkingDuration = itemView.findViewById(R.id.id_card_active_session_duration);

            this.onTariffClickListener = onTariffClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTariffClickListener.onTariffClick(getBindingAdapterPosition());
        }
    }

    public class InactiveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView tariffAmount;
        public TextView tariffParkingArea;
        public TextView tariffParkingLevel;
        public TextView tariffParkingSpace;
        public TextView tariffParkingSpot;
        public TextView tariffParkingDuration;
        OnTariffClickListener onTariffClickListener;

        public InactiveViewHolder(@NonNull View itemView, OnTariffClickListener onTariffClickListener) {
            super(itemView);
            tariffAmount = itemView.findViewById(R.id.id_card_active_session_tariff_amt);
            tariffParkingArea = itemView.findViewById(R.id.id_card_active_session_parking_area);
            tariffParkingLevel = itemView.findViewById(R.id.id_card_active_session_parking_level);
           // tariffParkingSpace = itemView.findViewById(R.id.id_card_active_session_parking_space);
            tariffParkingSpot = itemView.findViewById(R.id.id_card_active_session_parking_spot);
            tariffParkingDuration = itemView.findViewById(R.id.id_card_active_session_duration);

            this.onTariffClickListener = onTariffClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTariffClickListener.onTariffClick(getBindingAdapterPosition());
        }
    }


//    @NonNull
//    @Override
//    public TariffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
//    {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_tariff_2, parent, false);
//        return new TariffViewHolder(view, onTariffClickListener);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TariffViewHolder holder, int position) {
//
//        Tariff tariff = tariff_items.get(position);
//        holder.tariffAmount.setText(String.valueOf(tariff.getTariffRate()) + " DHS/hr");
//        holder.tariffParkingArea.setText(tariff.getParkingArea());
//        holder.tariffParkingLevel.setText(tariff.getParkingLevel());
//        holder.tariffParkingSpace.setText(tariff.getParkingSpace());
//        holder.tariffParkingSpot.setText(tariff.getParkingSpot());
//        holder.tariffParkingDuration.setText(tariff.getDuration());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return tariff_items.size();
//    }

//    public class TariffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
//    {
//        public TextView tariffAmount;
//        public TextView tariffParkingArea;
//        public TextView tariffParkingLevel;
//        public TextView tariffParkingSpace;
//        public TextView tariffParkingSpot;
//        public TextView tariffParkingDuration;
//        OnTariffClickListener onTariffClickListener;
//
//        public TariffViewHolder(@NonNull View itemView, OnTariffClickListener onTariffClickListener) {
//            super(itemView);
//            tariffAmount = itemView.findViewById(R.id.id_card_active_session_tariff_amt);
//            tariffParkingArea = itemView.findViewById(R.id.id_card_active_session_parking_area);
//            tariffParkingLevel = itemView.findViewById(R.id.id_card_active_session_parking_level);
//            tariffParkingSpace = itemView.findViewById(R.id.id_card_active_session_parking_space);
//            tariffParkingSpot = itemView.findViewById(R.id.id_card_active_session_parking_spot);
//            tariffParkingDuration = itemView.findViewById(R.id.id_card_active_session_duration);
//
//            this.onTariffClickListener = onTariffClickListener;
//
//            itemView.setOnClickListener(this);
//        }
//
//
//        @Override
//        public void onClick(View view)
//        {
//            onTariffClickListener.onTariffClick(getBindingAdapterPosition());
//        }
//    }
//
    public interface OnTariffClickListener
    {
        public void onTariffClick(int position);
    }
}
