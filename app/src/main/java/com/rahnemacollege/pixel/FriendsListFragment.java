package com.rahnemacollege.pixel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FriendsListFragment extends Fragment {

    private RecyclerView friendsRecyclerView;
    private FriendsAdapter friendsAdapter;
    //TODO change the quantity of items
    private static final int NUM_FRIENDS_ITEM = 100;

    public FriendsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        friendsRecyclerView = view.findViewById(R.id.friends_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        friendsRecyclerView.setLayoutManager(layoutManager);
        friendsRecyclerView.setHasFixedSize(true);

        friendsAdapter = new FriendsAdapter(NUM_FRIENDS_ITEM);
        friendsRecyclerView.setAdapter(friendsAdapter);

        return view;
    }
}