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
        View view = LayoutInflater.from(mContext).inflate(R.layout.tariff_row_item, parent, false);
        return new TariffViewHolder(view, onTariffClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffViewHolder holder, int position) {

        Tariff tariff = tariff_items.get(position);
        holder.tariffAmount.setText(String.valueOf(tariff.getTariffRate()));

    }

    @Override
    public int getItemCount() {
        return tariff_items.size();
    }

    public class TariffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView tariffAmount;
        public TextView tariffLicense;
        public TextView tariffDrivers;
        OnTariffClickListener onTariffClickListener;

        public TariffViewHolder(@NonNull View itemView, OnTariffClickListener onTariffClickListener) {
            super(itemView);
            tariffAmount = itemView.findViewById(R.id.id_tariff_row_tariff_amount);
            tariffLicense = itemView.findViewById(R.id.id_tariff_row_item_license);
            tariffDrivers = itemView.findViewById(R.id.id_tariff_row_item_drivers);

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
