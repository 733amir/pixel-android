package com.rahnemacollege.pixel;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    public void bind(int listIndex) {
        //TODO handle view profile button
        //TODO set text of ViewText and pic of CircleImageView from server
        friendFullName.setText("Test FULL NAME");
//        friendProfilePicture
    }
}