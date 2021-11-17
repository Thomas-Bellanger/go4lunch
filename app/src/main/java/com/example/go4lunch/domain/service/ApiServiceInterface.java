package com.example.go4lunch.domain.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.User;

import java.util.List;

public interface ApiServiceInterface {

    List<User> filterUser(String filterPattern);

    List<User> getUsers();

    List<User> getFilteredUsers();

    void populateUser();

    LiveData<List<User>> getLiveUsers();

    MutableLiveData<String> getLiveDistance();

    MutableLiveData<String> getSort();
}
