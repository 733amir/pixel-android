package com.rahnemacollege.pixel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahnemacollege.pixel.Utilities.Post;

import java.util.ArrayList;

/**
 * Created by parsahejabi on 12/4/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private ArrayList<Post> postsArrayList;

    private int numOfposts;

    public HomeAdapter(int numOfposts) {
        this.numOfposts = numOfposts;
        postsArrayList = new ArrayList<>(numOfposts);
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForFriendItem = R.layout.post_holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForFriendItem, parent, shouldAttachToParentImmediately);
        HomeViewHolder homeViewHolder = new HomeViewHolder(view);
        return homeViewHolder;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        holder.bind(postsArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public void addPostForAdapter(String fullname, String time, String location, String like_count,
                                  String comment_count, String caption, String postImageUrl, String profileImageUrl){
        Post post = new Post(fullname, time, location, like_count, comment_count, caption, postImageUrl, profileImageUrl);
        postsArrayList.add(post);
        notifyDataSetChanged();
    }
}
