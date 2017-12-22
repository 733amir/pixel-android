package com.rahnemacollege.pixel;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rahnemacollege.pixel.Utilities.Friend;
import com.rahnemacollege.pixel.Utilities.Post;

import de.hdodenhof.circleimageview.CircleImageView;


public class FriendsViewHolder extends RecyclerView.ViewHolder {

    private Button viewProfile;
    private TextView friendFullName;
    private CircleImageView friendProfilePicture;

    public FriendsViewHolder(View itemView) {
        super(itemView);

        viewProfile = itemView.findViewById(R.id.friends_b_viewprofile);
        friendFullName = itemView.findViewById(R.id.friends_tv_fullname);
        friendProfilePicture = itemView.findViewById(R.id.friends_i_profileimage);
    }

    public void bind(Friend friend) {
        //TODO handle view profile button
        //TODO handle friend request button
        friendFullName.setText(friend.fullname);
        Glide.with(friendProfilePicture).load(friend.profileImageUrl).into(friendProfilePicture);
    }
}