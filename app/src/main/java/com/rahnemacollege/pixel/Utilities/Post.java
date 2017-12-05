package com.rahnemacollege.pixel.Utilities;


public class Post {
    public String id, fullname, postImageUrl, profileImageUrl, caption, time, like_count, comment_count;
    public double lat, lon;

    public Post(String id, String fullname, String time, double lat, double lon, String like_count,
                String comment_count, String caption, String postImageUrl, String profileImageUrl) {
        this.id = id;
        this.fullname = fullname;
        this.time = time;
        this.lat = lat;
        this.lon = lon;
        this.like_count = like_count;
        this.comment_count = comment_count;
        this.caption = caption;
        this.postImageUrl = postImageUrl;
        this.profileImageUrl = profileImageUrl;
    }
}
