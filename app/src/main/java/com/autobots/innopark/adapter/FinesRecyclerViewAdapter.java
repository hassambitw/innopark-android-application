package com.autobots.innopark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.R;
import com.autobots.innopark.data.Fine;

import java.util.ArrayList;

public class FinesRecyclerViewAdapter extends RecyclerView.Adapter<FinesRecyclerViewAdapter.FinesViewHolder>
{

    final private ArrayList<Fine> fine_items;
    private Context mContext;
    private OnFineClickListener onFineClickListener;

    public FinesRecyclerViewAdapter(ArrayList<Fine> fine_items, Context mContext, OnFineClickListener onFineClickListener)
    {
        this.fine_items = fine_items;
        this.mContext = mContext;
        this.onFineClickListener = onFineClickListener;
    }


    @NonNull
    @Override
    public FinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fine_row_item, parent, false);
        return new FinesViewHolder(view, onFineClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FinesViewHolder holder, int position) {

        Fine fine = fine_items.get(position);
        holder.fineAmount.setText(String.valueOf(fine.getFineAmount()));

    }

    @Override
    public int getItemCount() {
        return fine_items.size();
    }

    public class FinesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView fineAmount;
        public TextView fineLicense;
        public TextView fineDrivers;
        OnFineClickListener onFineClickListener;

        public FinesViewHolder(@NonNull View itemView, OnFineClickListener onFineClickListener) {
            super(itemView);
            fineAmount = itemView.findViewById(R.id.id_fine_row_fine_amount);
            fineLicense = itemView.findViewById(R.id.id_fine_row_item_license);
            fineDrivers = itemView.findViewById(R.id.id_fine_row_item_drivers);

            this.onFineClickListener = onFineClickListener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view)
        {
            onFineClickListener.onFineClick(getBindingAdapterPosition());
        }
    }

    public interface OnFineClickListener
    {
        public void onFineClick(int position);
    }
}
