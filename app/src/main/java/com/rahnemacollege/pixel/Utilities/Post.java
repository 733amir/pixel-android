package com.rahnemacollege.pixel.Utilities;


public class Post {
    public String fullname, postImageUrl, profileImageUrl, location, caption, time, like_count, comment_count;

    public Post(String fullname, String time, String location, String like_count,
                String comment_count, String caption, String postImageUrl, String profileImageUrl) {
        this.fullname = fullname;
        this.time = time;
        this.location = location;
        this.like_count = like_count;
        this.comment_count = comment_count;
        this.caption = caption;
        this.postImageUrl = postImageUrl;
        this.profileImageUrl = profileImageUrl;
    }
}
