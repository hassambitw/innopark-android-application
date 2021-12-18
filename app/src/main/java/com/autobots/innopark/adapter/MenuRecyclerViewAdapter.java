package com.autobots.innopark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.autobots.innopark.R;
import com.autobots.innopark.data.models.MenuItemList;


import java.util.ArrayList;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder>
{

    final private ArrayList<MenuItemList> menu_items;
    private Context context;
    private OnMenuClickListener onMenuClickListener;


    public MenuRecyclerViewAdapter(ArrayList<MenuItemList> menu_items, Context context, OnMenuClickListener onMenuClickListener)
    {
        this.menu_items = menu_items;
        this.context = context;
        this.onMenuClickListener = onMenuClickListener;
    }

    @NonNull
    @Override
    public MenuRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_view_menu_2, parent, false);
        return new ViewHolder(view, onMenuClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuRecyclerViewAdapter.ViewHolder holder, int position)
    {
        MenuItemList current_item = menu_items.get(position);

        holder.myImageView.setImageResource(current_item.getImage_resource());
        holder.m_text_view.setText(current_item.getText());
    }

    @Override
    public int getItemCount()
    {
        return menu_items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView myImageView;
        public TextView m_text_view;
        OnMenuClickListener onMenuClickListener;


        public ViewHolder(@NonNull View itemView, OnMenuClickListener onMenuClickListener)
        {
            super(itemView);
            myImageView = itemView.findViewById(R.id.grid_menu_image_id);
            m_text_view = itemView.findViewById(R.id.grid_menu_text_id);
            this.onMenuClickListener = onMenuClickListener;

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view)
        {
           onMenuClickListener.onMenuClick(getBindingAdapterPosition());
        }
    }

    public interface OnMenuClickListener
    {
        void onMenuClick(int position);
    }


}
