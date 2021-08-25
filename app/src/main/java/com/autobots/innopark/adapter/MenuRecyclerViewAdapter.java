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
import com.autobots.innopark.data.MenuItemList;


import java.util.ArrayList;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder>
{

    final private ArrayList<MenuItemList> menu_items;
    private Context context;

    public MenuRecyclerViewAdapter(ArrayList<MenuItemList> menu_items, Context context)
    {
        this.menu_items = menu_items;
        this.context = context;
    }

    @NonNull
    @Override
    public MenuRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_row_item_view, parent, false);
        return new ViewHolder(view);
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


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView myImageView;
        public TextView m_text_view;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            myImageView = itemView.findViewById(R.id.id_menu_image);
            m_text_view = itemView.findViewById(R.id.id_menu_txt);

        }
    }

    public interface OnMenuClickListener
    {
        void onMenuClick(int position);
    }


}
