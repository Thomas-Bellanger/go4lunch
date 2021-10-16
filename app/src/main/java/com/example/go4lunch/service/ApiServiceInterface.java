package com.example.go4lunch.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;

import java.util.List;

public interface ApiServiceInterface {

    List<Restaurant> filterRestaurant(String filterPattern);

    List<User> filterUser(String filterPattern);

    List<User> getUsers();

    List<Restaurant> getRestaurants();

    List<Restaurant> getFilteredRestaurants();

    List<User> getFilteredUsers();

    List<Restaurant> getFavorites();

    void populateRestaurant();

    void populateUser();

    LiveData<List<Restaurant>> getLiveRestaurant();

    LiveData<List<User>> getLiveUsers();

    MutableLiveData<String> getLiveDistance();
}
