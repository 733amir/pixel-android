//package com.rahnemacollege.pixel;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.rahnemacollege.pixel.Utilities.Notification;
//import com.rahnemacollege.pixel.Utilities.Post;
//import com.rahnemacollege.pixel.Utilities.SquareImageView;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
///**
// * Created by parsahejabi on 12/4/17.
// */
//
//public class HomeViewHolder extends RecyclerView.ViewHolder {
//
//    private CircleImageView postProfilePicture;
//    private TextView postFullname;
//    private TextView postTime;
//    private SquareImageView postImage;
//    private TextView postLocation;
//    private TextView postLikeCount;
//    private TextView postCommentCount;
//    private TextView postCaption;
//
//
//    public HomeViewHolder(View itemView) {
//        super(itemView);
//
//        postProfilePicture = itemView.findViewById(R.id.post_profile_image);
//        postFullname = itemView.findViewById(R.id.post_fullname);
//        postTime = itemView.findViewById(R.id.post_time);
//        postImage = itemView.findViewById(R.id.post_image);
//        postLocation = itemView.findViewById(R.id.post_location_text);
//        postLikeCount = itemView.findViewById(R.id.post_like_count);
//        postCommentCount = itemView.findViewById(R.id.post_comment_count);
//        postCaption = itemView.findViewById(R.id.post_caption);
//    }
//
//    public void bind(Post post) {
//        //TODO use glide to show post image url from server
////        Glide.with(postProfilePicture).load(post.postImageUrl).into(postProfilePicture);
//        postFullname.setText(post.fullname);
//        postTime.setText(post.time);
////        Glide.with(postImage).load(post.postImageUrl).into(postImage);
////        postLocation.setText(post.location);
//        postLikeCount.setText(post.like_count);
//        postCommentCount.setText(post.comment_count);
//        postCaption.setText(post.caption);
//    }
//}
