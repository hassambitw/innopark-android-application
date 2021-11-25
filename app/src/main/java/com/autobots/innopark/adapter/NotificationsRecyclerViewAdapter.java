package com.autobots.innopark.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autobots.innopark.R;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.UserToken;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class NotificationsRecyclerViewAdapter extends RecyclerView.Adapter<NotificationsRecyclerViewAdapter.MySwipeViewHolder> {

    private static Context context;
    private ArrayList<Map<String, Object>> notifications;
    private final ViewBinderHelper vbh = new ViewBinderHelper();

    public NotificationsRecyclerViewAdapter(Context context, ArrayList<Map<String, Object>> notifications)
    {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationsRecyclerViewAdapter.MySwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_notification_swipe, parent, false);
        return new MySwipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsRecyclerViewAdapter.MySwipeViewHolder holder, int position)
    {
        Map<String, Object> mNotifs = notifications.get(position);

        vbh.setOpenOnlyOne(true);

        vbh.bind(holder.swipeLayout, mNotifs.get("notif_title").toString());
        vbh.closeLayout(mNotifs.get("notif_title").toString());
        holder.mNotificationText.setText(mNotifs.get("notif_body").toString());

//        long current_time = Timestamp.now().toDate().getTime();
//        long notif_time = ((Timestamp) mNotifs.get("notif_datetime")).toDate().getTime();
//        int time_difference = (int) (Math.ceil((current_time - notif_time)/3600));
//
//        Log.d("ONSUCCESS", "DIFFERENCE BETWEEN THE TWO TIMESTAMPS: "+time_difference);

         Date current_time = new Date();
         Date notif_time = (Date) mNotifs.get("notif_datetime");
         long elapsed_time = current_time.getTime() - notif_time.getTime();

         holder.mNotificationTime.setText(elapsed_time + "");

    }

    @Override
    public int getItemCount()
    {
        return notifications.size();
    }


    public static class MySwipeViewHolder extends RecyclerView.ViewHolder
    {

        private TextView mNotificationText;
        private TextView mNotificationTime;
        private TextView mDelete;
        private SwipeRevealLayout swipeLayout;

        public MySwipeViewHolder (@NonNull View itemView)
        {
            super(itemView);
            mNotificationText = itemView.findViewById(R.id.id_notifications_text);
            mNotificationTime = itemView.findViewById(R.id.id_notifications_time);
            mDelete = itemView.findViewById(R.id.id_delete_notification_textview);
            swipeLayout = itemView.findViewById(R.id.id_swipe_layout);

            mDelete.setOnClickListener(view -> {
                Toast.makeText(context, "Delete clicked", Toast.LENGTH_SHORT).show();
            });
        }

    }
}

