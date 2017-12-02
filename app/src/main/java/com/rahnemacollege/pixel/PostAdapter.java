package com.rahnemacollege.pixel;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rahnemacollege.pixel.Utilities.Post;
import com.rahnemacollege.pixel.Utilities.SquareImageView;

import java.util.ArrayList;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private ArrayList<Post> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View root;

        public ViewHolder(View root) {
            super(root);
            this.root = root;
        }
    }

    public PostAdapter(int initialCapacity) {
        mDataset = new ArrayList<>(initialCapacity);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_holder, parent, false);

        ViewHolder vh = new ViewHolder(root);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView fullname = holder.root.findViewById(R.id.post_fullname);
        TextView time = holder.root.findViewById(R.id.post_time);
        TextView location = holder.root.findViewById(R.id.post_location_text);
        TextView like_count = holder.root.findViewById(R.id.post_like_count);
        TextView comment_count = holder.root.findViewById(R.id.post_comment_count);
        TextView caption = holder.root.findViewById(R.id.post_caption);
        SquareImageView post_image = holder.root.findViewById(R.id.post_image);
        ImageView profile_image = holder.root.findViewById(R.id.post_profile_image);

        Post post = mDataset.get(position);

        fullname.setText(post.fullname);
        time.setText(post.time);
        location.setText(post.location);
        like_count.setText(post.like_count);
        comment_count.setText(post.comment_count);
        caption.setText(post.caption);
        Glide.with(holder.root).load(post.postImageUrl).into(post_image);
        Glide.with(holder.root).load(post.profileImageUrl).into(profile_image);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addPost(String fullname, String time, String location, String like_count,
                        String comment_count, String caption, String postImageUrl, String profileImageUrl) {
        Post post = new Post(fullname, time, location, like_count, comment_count, caption, postImageUrl, profileImageUrl);
        mDataset.add(post);
        notifyDataSetChanged();
    }
}