package com.rahnemacollege.pixel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahnemacollege.pixel.Utilities.Notification;

import java.util.ArrayList;

/**
 * Created by parsahejabi on 12/4/17.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsViewHolder> {
    private ArrayList<Notification> notificationsArrayList;

    private int numOfNotifications;

    public NotificationsAdapter(int numOfNotifications) {
        this.numOfNotifications = numOfNotifications;
        notificationsArrayList = new ArrayList<>(numOfNotifications);
    }

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForFriendItem = R.layout.notification_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForFriendItem, parent, shouldAttachToParentImmediately);
        NotificationsViewHolder notificationsViewHolder = new NotificationsViewHolder(view);
        return notificationsViewHolder;
    }

    @Override
    public void onBindViewHolder(NotificationsViewHolder holder, int position) {
        holder.bind(notificationsArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return notificationsArrayList.size();
    }

    public void addNotificationForAdapter(String username, String profileImageUrl, String text){
        Notification notification = new Notification(username, profileImageUrl, text);
        notificationsArrayList.add(notification);
        notifyDataSetChanged();
    }
}
