package com.example.go4lunch.service;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;

import java.util.List;

public class Dummy_List_api_Service implements List_api_Service {
    private final List<Restaurant> mRestaurants = Dummy_List_Generator.generateRestaurant();
    private final List<User> mUsers = Dummy_List_Generator.generateCoWorker();

    public List<Restaurant> getRestaurants() {
        return mRestaurants;
    }

    public List<User> getCoWorkers() {
        return mUsers;
    }
}
