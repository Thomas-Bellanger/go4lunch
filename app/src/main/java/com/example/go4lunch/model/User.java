package com.example.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uid;
    private String name;
    private String avatar;
    private String chosenRestaurant;
    private final List<Restaurant> favorite = new ArrayList<>();

    public User(String uid, String name, @Nullable String avatar) {
        this.uid = uid;
        this.name = name;
        this.avatar = avatar;
        this.chosenRestaurant = "none";
    }

    public String getChosenRestaurant() {
        return chosenRestaurant;
    }

    public void setChosenRestaurant(String chosenRestaurant) {
        this.chosenRestaurant = chosenRestaurant;
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
}
