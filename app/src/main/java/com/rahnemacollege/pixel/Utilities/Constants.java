package com.rahnemacollege.pixel.Utilities;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;


public class Constants {
    // Shared Preferences Key
    public static final String USER_INFO = "user_info";

    // Request header
    public static final String AUTHORIZATION = "Authorization";

    // JSON parameters
    public static final String STATUS = "status", ACCESS_TOKEN = "access_token", USERNAME = "username",
            EMAIL = "email", FULLNAME = "name", OBJECT = "object", BIO = "bio", INTERESTS = "interests",
            INTEREST = "interest", PAGE = "page", POSTS = "posts", PHOTO = "photo", CREATED_DATE = "createdDate",
            LIKE_COUNT = "likeCount", COMMENT_COUNT = "commentCount", TEXT = "text", COVER_PHOTO = "coverPhoto",
            PROFILE_PHOTO = "profilePhoto";

    // Status codes
    public static final String OK = "1", USERNAME_EXISTS = "2", EMAIL_EXISTS = "3", NOT_FOUND = "4";

    // TODO move all apis here.

    public static GlideUrl addAuthorization(String url, String access_token){
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader(AUTHORIZATION, access_token)
                .build());
    }
}
