package com.rahnemacollege.pixel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahnemacollege.pixel.Utilities.Friend;

import java.util.ArrayList;


public class FriendsAdapter extends RecyclerView.Adapter<FriendsViewHolder> {
    private ArrayList<Friend> friendsArrayList;

    private int numOfFriends;

    public FriendsAdapter(int numOfFriends) {
        this.numOfFriends = numOfFriends;
        friendsArrayList = new ArrayList<>(numOfFriends);
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
        holder.bind(friendsArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendsArrayList.size();
    }

    public void addFriendForAdapter(String fullname, String profileImageUrl){
        Friend friend = new Friend(fullname, profileImageUrl);
        friendsArrayList.add(friend);
        notifyDataSetChanged();
    }
}
