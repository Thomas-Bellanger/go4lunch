package com.example.go4lunch.model;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final List<Restaurant> favorite = new ArrayList<>();
    private String uid;
    private String name;
    private String avatar;
    private String mail;
    @Nullable
    private Restaurant chosenRestaurant;
    private boolean notification;

    public User(String uid, String name, @Nullable String avatar, @Nullable Restaurant chosenRestaurant, String mail) {
        this.uid = uid;
        this.name = name;
        this.avatar = avatar;
        this.chosenRestaurant = chosenRestaurant;
        this.mail = mail;
    }

    public User() {
    }

    //create a "User" from account to connect
    public static User firebaseUserToUser(FirebaseUser fbUser) {
        if (fbUser == null){
        return new User("0", "fbUser.getDisplayName()", "https://www.gravatar.com/avatar/HASH", Restaurant.noRestaurant, "fbUser.getEmail()");
    }
        else {
            try {
                return new User(fbUser.getUid(), fbUser.getDisplayName(), fbUser.getPhotoUrl().toString(), Restaurant.noRestaurant, fbUser.getEmail());
            } catch (NullPointerException exception) {
                return new User(fbUser.getUid(), fbUser.getDisplayName(), "https://www.gravatar.com/avatar/HASH", Restaurant.noRestaurant, fbUser.getEmail());
            }
        }
    }

    public Restaurant getChosenRestaurant() {
        return chosenRestaurant;
    }

    public void setChosenRestaurant(Restaurant chosenRestaurant) {
        this.chosenRestaurant = chosenRestaurant;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUid() {
        return uid;
    }

    public List<Restaurant> getFavorite() {
        return favorite;
    }

    public void setId(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void addFavorite(Restaurant restaurant) {
        favorite.add(restaurant);
    }

    public void removeFavorite(Restaurant restaurant) {
        favorite.remove(restaurant);
    }
}
