package com.rahnemacollege.pixel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahnemacollege.pixel.FriendsViewHolder;
import com.rahnemacollege.pixel.R;

/**
 * Created by parsahejabi on 11/30/17.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsViewHolder> {

    private int numOfFriends;

    public FriendsAdapter(int numOfFriends) {
        this.numOfFriends = numOfFriends;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForFriendItem = R.layout.friend_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForFriendItem, parent, shouldAttachToParentImmediately);
        FriendsViewHolder friendsViewHolder = new FriendsViewHolder(view);
        return friendsViewHolder;
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        //TODO get numOfFriends from server here!
        return numOfFriends;
    }
}
