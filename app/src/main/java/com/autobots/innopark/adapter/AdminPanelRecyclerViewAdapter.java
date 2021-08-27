package com.autobots.innopark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdminPanelRecyclerViewAdapter extends RecyclerView.Adapter<AdminPanelRecyclerViewAdapter.ViewHolder>
{

    final private ArrayList<String> adminPanelList;
    private Context mContext;
    private OnAdminPanelClickListener onAdminPanelClickListener;

    public AdminPanelRecyclerViewAdapter(ArrayList<String> adminPanelList, Context mContext, OnAdminPanelClickListener onAdminPanelClickListener)
    {
        this.adminPanelList = adminPanelList;
        this.mContext = mContext;
        this.onAdminPanelClickListener = onAdminPanelClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.admin_row_item, parent, false);
        return new ViewHolder(view, onAdminPanelClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.mTextView.setText(adminPanelList.get(position));
    }

    @Override
    public int getItemCount()
    {
        return adminPanelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView mTextView;
        OnAdminPanelClickListener onAdminPanelClickListener;

        public ViewHolder(@NonNull View itemView, OnAdminPanelClickListener onAdminPanelClickListener)
        {
            super(itemView);
            mTextView = itemView.findViewById(R.id.id_admin_panel_text_view);
            this.onAdminPanelClickListener = onAdminPanelClickListener;


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            onAdminPanelClickListener.onAdminPanelClick(getBindingAdapterPosition());
        }
    }

    public interface OnAdminPanelClickListener
    {
        void onAdminPanelClick(int position);
    }
}
