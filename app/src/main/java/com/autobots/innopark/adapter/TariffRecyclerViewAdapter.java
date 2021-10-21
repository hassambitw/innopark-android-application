package com.autobots.innopark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.R;
import com.autobots.innopark.data.Tariff;

import java.util.ArrayList;

public class TariffRecyclerViewAdapter extends RecyclerView.Adapter<TariffRecyclerViewAdapter.TariffViewHolder>
{

    final private ArrayList<Tariff> tariff_items;
    private Context mContext;
    private OnTariffClickListener onTariffClickListener;

    public TariffRecyclerViewAdapter(ArrayList<Tariff> tariff_items, Context mContext, OnTariffClickListener onTariffClickListener)
    {
        this.tariff_items = tariff_items;
        this.mContext = mContext;
        this.onTariffClickListener = onTariffClickListener;
    }


    @NonNull
    @Override
    public TariffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_tariff_2, parent, false);
        return new TariffViewHolder(view, onTariffClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffViewHolder holder, int position) {

        Tariff tariff = tariff_items.get(position);
        holder.tariffAmount.setText(String.valueOf(tariff.getTariffRate()));
        holder.tariffParkingArea.setText(tariff.getParkingArea());
        holder.tariffParkingLevel.setText(tariff.getParkingLevel());
        holder.tariffParkingSpace.setText(tariff.getParkingSpace());
        holder.tariffParkingSpot.setText(tariff.getParkingSpot());
        holder.tariffParkingDuration.setText(tariff.getDuration());

    }

    @Override
    public int getItemCount() {
        return tariff_items.size();
    }

    public class TariffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView tariffAmount;
        public TextView tariffParkingArea;
        public TextView tariffParkingLevel;
        public TextView tariffParkingSpace;
        public TextView tariffParkingSpot;
        public TextView tariffParkingDuration;
        OnTariffClickListener onTariffClickListener;

        public TariffViewHolder(@NonNull View itemView, OnTariffClickListener onTariffClickListener) {
            super(itemView);
            tariffAmount = itemView.findViewById(R.id.id_card_active_session_tariff_amt);
            tariffParkingArea = itemView.findViewById(R.id.id_card_active_session_parking_area);
            tariffParkingLevel = itemView.findViewById(R.id.id_card_active_session_parking_level);
            tariffParkingSpace = itemView.findViewById(R.id.id_card_active_session_parking_space);
            tariffParkingSpot = itemView.findViewById(R.id.id_card_active_session_parking_spot);
            tariffParkingDuration = itemView.findViewById(R.id.id_card_active_session_duration);

            this.onTariffClickListener = onTariffClickListener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view)
        {
            onTariffClickListener.onTariffClick(getBindingAdapterPosition());
        }
    }

    public interface OnTariffClickListener
    {
        public void onTariffClick(int position);
    }
}
