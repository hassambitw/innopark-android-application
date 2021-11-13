//package com.autobots.innopark.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.autobots.innopark.R;
//import com.autobots.innopark.data.UserToken;
//
//import java.util.ArrayList;
//
//public class NotificationsRecyclerView2 extends RecyclerView.Adapter<NotificationsRecyclerView2.NotificationsRecyclerViewHoler>
//{
//
//    private static Context context;
//    private ArrayList<UserToken> notifications;
//
//    public NotificationsRecyclerView2(Context context, ArrayList<UserToken> notifications) {
//        this.context = context;
//        this.notifications = notifications;
//    }
//
//    @NonNull
//    @Override
//    public NotificationsRecyclerViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.row_item_notification_swipe, parent, false);
//        return new NotificationsRecyclerViewHoler(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull NotificationsRecyclerView2 holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return notifications.size();
//    }
//
//    public class NotificationsRecyclerViewHoler extends RecyclerView.ViewHolder
//    {
//        TextView mNotificationText;
//
//        public NotificationsRecyclerViewHoler(@NonNull View itemView) {
//            super(itemView);
//            mNotificationText = itemView.findViewById(R.id.id_notifications_text);
//        }
//    }
//}
