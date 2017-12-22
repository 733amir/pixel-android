package com.rahnemacollege.pixel;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.rahnemacollege.pixel.Utilities.ClickListener;
import com.rahnemacollege.pixel.Utilities.Constants;
import com.rahnemacollege.pixel.Utilities.Post;
import com.rahnemacollege.pixel.Utilities.SquareImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    public ArrayList<Post> mDataset;
    private String access_token, username;
    private Context current;

    private ClickListener listener;

    String TAG = "In PostAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View root;

        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(View root, ClickListener listener) {
            super(root);
            this.root = root;
            this.listenerRef = new WeakReference<>(listener);

            ImageView like_icon = root.findViewById(R.id.post_like_icon);
            like_icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listenerRef.get().onClickListener(getAdapterPosition());
        }
    }

    public void setArgs(String access_token, Context current, ClickListener listener, String username) {
        this.access_token = access_token;
        this.current = current;
        this.listener = listener;
        this.username = username;
    }

    public PostAdapter(int initialCapacity) {
        mDataset = new ArrayList<>(initialCapacity);
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_holder, parent, false);

        ViewHolder vh = new ViewHolder(root, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView fullname = holder.root.findViewById(R.id.post_fullname);
        TextView time = holder.root.findViewById(R.id.post_time);
        TextView location = holder.root.findViewById(R.id.post_location_text);
        TextView like_count = holder.root.findViewById(R.id.post_like_count);
        final ImageView like_icon = holder.root.findViewById(R.id.post_like_icon);
        TextView comment_count = holder.root.findViewById(R.id.post_comment_count);
        TextView caption = holder.root.findViewById(R.id.post_caption);
        SquareImageView post_image = holder.root.findViewById(R.id.post_image);
        ImageView profile_image = holder.root.findViewById(R.id.post_profile_image);
        ImageView location_icon = holder.root.findViewById(R.id.post_location_icon);

        Post post = mDataset.get(position);

        fullname.setText(post.fullname);
        time.setText(post.time);
        if (post.lat != 0 || post.lon != 0) {
            location.setText(showLocation(post.lat, post.lon));
            location_icon.setImageResource(R.drawable.ic_location_on_blue_24dp);
        } else {
            location_icon.setImageResource(R.drawable.ic_location_on_black_24dp);
        }

        AndroidNetworking.get("http://192.168.10.193/api/like/{postId}")
                .addHeaders(Constants.AUTHORIZATION, access_token)
                .addPathParameter(Constants.POST_ID, post.id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Like response: " + response.toString());

                        boolean user_liked = false;
                        try {
                            JSONArray list = response.getJSONArray("usernameList");
                            for (int i = 0; i < list.length(); i++) {
                                if (username.equals(list.getString(i))) {
                                    user_liked = true;
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (user_liked) {
                            like_icon.setImageResource(R.drawable.ic_favorite_red_24dp);
                        }
                    }

                    @Override
                    public void onError(ANError e) {
                        Log.e(TAG, "Like response error: " + e.toString() + " code: " + e.getErrorCode());
                        e.printStackTrace();
                    }
                });
        like_icon.setImageResource(R.drawable.ic_favorite_border_black_24dp);

        like_count.setText(post.like_count);
        comment_count.setText(post.comment_count);
        caption.setText(post.caption);

        if (post.postImageUrl != null && !post.postImageUrl.isEmpty())
            Glide.with(holder.root).load(Constants.addAuthorization(post.postImageUrl, access_token)).into(post_image);

        if (post.profileImageUrl != null && !post.profileImageUrl.isEmpty())
        Glide.with(holder.root).load(Constants.addAuthorization(post.profileImageUrl, access_token)).into(profile_image);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addPost(String id, String fullname, String time, double lat, double lon, String like_count,
                        String comment_count, String caption, String postImageUrl, String profileImageUrl) {
        Post post = new Post(id, fullname, time, lat, lon, like_count, comment_count, caption, postImageUrl, profileImageUrl);
        mDataset.add(post);
        notifyDataSetChanged();
    }

    public void removeAll() {
        mDataset.clear();
        notifyDataSetChanged();
    }

    public String showLocation(double lat, double lon) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(current, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String city = addresses.get(0).getLocality();
            String knownName = addresses.get(0).getFeatureName();
            return city + " - " + knownName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}