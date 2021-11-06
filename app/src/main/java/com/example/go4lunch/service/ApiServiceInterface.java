package com.example.go4lunch.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;

import java.util.List;

public interface ApiServiceInterface {

    List<User> filterUser(String filterPattern);

    List<User> getUsers();

    List<User> getFilteredUsers();

    List<Restaurant> getFavorites();

    void populateUser();

    LiveData<List<User>> getLiveUsers();

    MutableLiveData<String> getLiveDistance();

    MutableLiveData<String> getSort();
}
