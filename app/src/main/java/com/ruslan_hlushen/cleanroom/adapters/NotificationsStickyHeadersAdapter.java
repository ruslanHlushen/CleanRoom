package com.ruslan_hlushen.cleanroom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruslan_hlushen.cleanroom.R;
import com.ruslan_hlushen.cleanroom.models.MyNotification;

import java.util.ArrayList;
import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;

/**
 * Created by Руслан on 18.01.2017.
 */
public class NotificationsStickyHeadersAdapter extends RecyclerView.Adapter<NotificationsStickyHeadersAdapter.ViewHolder>
implements StickyHeaderAdapter<NotificationsStickyHeadersAdapter.HeaderViewHolder> {

    Context context;
    List<MyNotification> myNotificationList = new ArrayList<>();


    public NotificationsStickyHeadersAdapter(Context context1, List<MyNotification> myNotificationList1) {

        this.context = context1;
        this.myNotificationList = myNotificationList1;
    }


    @Override
    public NotificationsStickyHeadersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public long getHeaderId(int position) {

        return myNotificationList.get(position).getStringDate(context.getString(R.string.time_unknown)).hashCode();
    }


    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_notification, parent, false);
        return new NotificationsStickyHeadersAdapter.HeaderViewHolder(itemView);
    }


    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {

        MyNotification myNotification = myNotificationList.get(position);

        holder.textViewHeader.setText(myNotification.getStringDate(context.getString(R.string.time_unknown)));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MyNotification myNotification = myNotificationList.get(position);

        holder.textViewLogin.setText(myNotification.getUserLogin());
        holder.textViewTime.setText(myNotification.getStringDateWithTime(context.getString(R.string.time_unknown)));

        if (myNotification.getNotificationType() == MyNotification.newNotificationType) {
            holder.textViewMessage.setText(myNotification.getMessageWithFloor(context.getString(R.string.floor_text_for_notification)));
        } else if (myNotification.getNotificationType() == MyNotification.infoNotificationType) {
            holder.textViewMessage.setText(myNotification.getMessage());
        }
    }


    @Override
    public int getItemCount() { return myNotificationList.size(); }


    public void setData(List<MyNotification> newNotifications) {

        try {
            myNotificationList.clear();
            myNotificationList.addAll(newNotifications);

        } catch (NullPointerException e) {

            myNotificationList = new ArrayList<>();
            myNotificationList.addAll(newNotifications);
        }

        notifyDataSetChanged();
    }


    public void addData(List<MyNotification> newNotifications) {

        try {
            myNotificationList.addAll(newNotifications);

        } catch (NullPointerException e) {

            myNotificationList = new ArrayList<>();
            myNotificationList.addAll(newNotifications);
        }

        notifyDataSetChanged();
    }


    public void addOneItem(int position, MyNotification newNotification) {

        try {
            myNotificationList.add(position, newNotification);

        } catch (NullPointerException e) {

            myNotificationList = new ArrayList<>();
            myNotificationList.add(0, newNotification);
        }

        notifyDataSetChanged();
    }


    public MyNotification getLastItem() {

        if (myNotificationList != null && getItemCount() > 0) {

            return myNotificationList.get(getItemCount() - 1);
        } else {
            return null;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewMessage, textViewTime, textViewLogin;


        public ViewHolder(View itemView) {

            super(itemView);

            textViewLogin = (TextView) itemView.findViewById(R.id.item_notification_new_sender_login);
            textViewTime = (TextView) itemView.findViewById(R.id.item_notification_time);
            textViewMessage = (TextView) itemView.findViewById(R.id.item_notification_message);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView textViewHeader;


        public HeaderViewHolder(View itemView) {

            super(itemView);

            textViewHeader = (TextView) itemView.findViewById(R.id.item_header_notification_textView);
        }
    }
}