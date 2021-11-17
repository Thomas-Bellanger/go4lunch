package com.example.go4lunch.domain.manager;

import com.example.go4lunch.domain.repository.GoogleRepository;

public class GoogleManager {
    private static volatile GoogleManager instance;
    private final GoogleRepository mGoogleRepository;

    //manager instance
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

    public void fetchRestaurant(GoogleRepository.Callbacks callbacks, String location){
        mGoogleRepository.fetchRestaurant(callbacks, location);
    }

    public void getDetail (GoogleRepository.Callbacks callbacksDetail, String id){
        mGoogleRepository.getDetail (callbacksDetail, id);
    }
}
