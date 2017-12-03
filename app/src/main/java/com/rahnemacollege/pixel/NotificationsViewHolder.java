package com.rahnemacollege.pixel;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rahnemacollege.pixel.Utilities.Notification;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by parsahejabi on 12/3/17.
 */

public class NotificationsViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView notificationProfilePicture;
    private TextView notificationUsername;
    private TextView notificationText;

    public NotificationsViewHolder(View itemView) {
        super(itemView);

        notificationProfilePicture = itemView.findViewById(R.id.friends_i_profileimage);
        notificationUsername = itemView.findViewById(R.id.notifications_tv_username);
        notificationText = itemView.findViewById(R.id.notifications_tv_text);
    }

    public void bind(Notification notification) {
        //TODO use glide to show notification image url from server
        notificationUsername.setText(notification.username);
//        Glide.with(notificationProfilePicture).load(notification.notificationImageUrl).into(notificationProfilePicture);
        notificationText.setText(notification.text);
    }
}
