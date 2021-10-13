package com.example.go4lunch.manager;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.nearbysearchmodel.ResultsItem;
import com.example.go4lunch.repository.GoogleRepository;
import com.example.go4lunch.repository.UserRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleManager {
    private static volatile GoogleManager instance;
    private final GoogleRepository mGoogleRepository;

    private GoogleManager() {
        mGoogleRepository = GoogleRepository.getInstance();
    }

    public static GoogleManager getInstance() {
        GoogleManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (GoogleManager.class) {
            if (instance == null) {
                instance = new GoogleManager();
            }
            return instance;
        }
    }

    public ResultsItem getNearbySearchResult() {
        return mGoogleRepository.getNearbySearchResult();
    }

    public ResultsItem callRestaurant(String location){
        return mGoogleRepository.callRestaurant(location);
    }

    public Restaurant getRestaurantItem(){
        Restaurant restaurant = Restaurant.googleRestaurantToRestaurant(getNearbySearchResult());
        return restaurant;
    }
}
