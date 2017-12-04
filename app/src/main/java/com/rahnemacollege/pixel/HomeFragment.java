package com.rahnemacollege.pixel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {
    String TAG = "Home Fragment";
    String username, access_token;

    private RecyclerView postsRecyclerView;
    private HomeAdapter homeAdapter;
    //TODO change the quantity of items
    private static final int NUM_POSTS_ITEM = 10;

    public HomeFragment() {
    }

    public void setArgs(String access_token, String username) {
        this.username = username;
        this.access_token = access_token;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        postsRecyclerView = view.findViewById(R.id.home_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        postsRecyclerView.setLayoutManager(layoutManager);
        postsRecyclerView.setHasFixedSize(true);

        homeAdapter = new HomeAdapter(NUM_POSTS_ITEM);
        postsRecyclerView.setAdapter(homeAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(postsRecyclerView.getContext(),
                layoutManager.getOrientation());
        postsRecyclerView.addItemDecoration(dividerItemDecoration);

        homeAdapter.addPostForAdapter("parsa", "2016", "haminja", "20", "30", "salam chetori", "0.jpg", "0.jpg");
        homeAdapter.addPostForAdapter("parsa", "2016", "haminja", "20", "30", "salam chetori", "0.jpg", "0.jpg");

        return view;
    }

}
