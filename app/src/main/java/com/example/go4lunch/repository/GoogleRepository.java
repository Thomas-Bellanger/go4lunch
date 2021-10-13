package com.example.go4lunch.repository;

import com.example.go4lunch.nearbysearchmodel.ResultsItem;
import com.example.go4lunch.utils.GooglePlaceService;

public class GoogleRepository {

    private static volatile GoogleRepository instance;
    private ResultsItem nearbySearchResult = new ResultsItem();


    public static GoogleRepository getInstance() {
        GoogleRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new GoogleRepository();
            }
            return instance;
        }
    }

    public ResultsItem callRestaurant(String location){
        GooglePlaceService googlePlaceService = GooglePlaceService.retrofit.create(GooglePlaceService.class);
        ResultsItem liveDataCall = googlePlaceService.getRestaurants(location);
        nearbySearchResult = liveDataCall;

        return liveDataCall;
    }

    public ResultsItem getNearbySearchResult() {
        return nearbySearchResult;
    }
}
