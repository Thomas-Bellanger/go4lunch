package com.example.go4lunch.service;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;

import java.util.List;

public interface List_api_Service {

    List<Restaurant> getRestaurants();

    List<User> getCoWorkers();
}
