package com.rahnemacollege.pixel.Utilities;


//public class User {
//    public String username, fullname, email, bio, token;
//}

import java.io.Serializable;

public class User implements Serializable {

    private static User userInstance = null; // the only instance of the class
    private String username, fullname, email, bio, token; // userName = the short phone number

    private User(){}

    public static User getInstance() {
        if (userInstance == null)
            userInstance = new User();
        return userInstance;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public String getToken() {
        return token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return String.format("User username=%s fullname=%s email=%s bio=%s token=%s%n",
                getUsername(), getFullname(), getEmail(), getBio(), getToken());
    }
}