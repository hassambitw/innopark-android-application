package com.autobots.innopark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.R;
import com.autobots.innopark.data.NotificationData;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;

public class NotificationsRecyclerViewAdapter extends RecyclerView.Adapter<NotificationsRecyclerViewAdapter.MySwipeViewHolder> {

    private static Context context;
    private ArrayList<NotificationData> notifications;
    private final ViewBinderHelper vbh = new ViewBinderHelper();

    public NotificationsRecyclerViewAdapter(Context context, ArrayList<NotificationData> notifications)
    {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationsRecyclerViewAdapter.MySwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_swipe_row_item, parent, false);
        return new MySwipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsRecyclerViewAdapter.MySwipeViewHolder holder, int position)
    {
        NotificationData mNotifs = notifications.get(position);
        vbh.setOpenOnlyOne(true);
        vbh.bind(holder.swipeLayout, String.valueOf(mNotifs.getNotification_id()));
        vbh.closeLayout(String.valueOf(mNotifs.getNotification_info()));
        holder.mNotificationText.setText(mNotifs.getNotification_info());
    }

    @Override
    public int getItemCount()
    {
        return notifications.size();
    }


    public static class MySwipeViewHolder extends RecyclerView.ViewHolder
    {

        private TextView mNotificationText;
        private TextView mDelete;
        private SwipeRevealLayout swipeLayout;

        public MySwipeViewHolder (@NonNull View itemView)
        {
            super(itemView);
            mNotificationText = itemView.findViewById(R.id.id_notifications_text);
            mDelete = itemView.findViewById(R.id.id_delete_notification_textview);
            swipeLayout = itemView.findViewById(R.id.id_swipe_layout);

            mDelete.setOnClickListener(view -> {
                Toast.makeText(context, "Delete clicked", Toast.LENGTH_SHORT).show();
            });
        }

    }
}

