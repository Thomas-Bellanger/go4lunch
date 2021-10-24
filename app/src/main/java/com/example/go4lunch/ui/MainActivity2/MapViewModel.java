package com.example.go4lunch.ui.MainActivity2;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.DI.DI;
import com.example.go4lunch.manager.GoogleManager;
import com.example.go4lunch.manager.RestaurantManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.nearbysearchmodel.ResponseAPI;
import com.example.go4lunch.nearbysearchmodel.ResultsItem;
import com.example.go4lunch.repository.GoogleRepository;
import com.example.go4lunch.service.ApiService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MapViewModel implements GoogleRepository.Callbacks {
    private static volatile MapViewModel instance;
    private ApiService mApiService = DI.getASIService();
    private GoogleManager mGoogleManager = GoogleManager.getInstance();
    private RestaurantManager mRestaurantManager = RestaurantManager.getInstance();
    public static MapViewModel getInstance(){
        MapViewModel result = instance;
        if(instance != null){
            return result;
        }
        else {
            instance = new MapViewModel();
        }
        return instance;
    }

    public void setLocation(LatLng latLng) {
        if(latLng!=null){
            mGoogleManager.fetchRestaurant(this,latLng.getLatitude()+","+latLng.getLongitude());
        }
    }

    public void googleToFirebase(Restaurant restaurant){
        mRestaurantManager.getRestaurantCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.getDocuments().contains(restaurant.getUid())){
            }
            else {
                mRestaurantManager.createRestaurantFirebase(restaurant);
            }
        });
    }

    @Override
    public void onResponse(@Nullable ResponseAPI itemLive) {
        for (ResultsItem item : itemLive.getResults()){
            Restaurant restaurant = Restaurant.googleRestaurantToRestaurant(item);
            mRestaurantManager.createRestaurantFirebase(restaurant);
            mApiService.populateRestaurant();
        }
    }

    @Override
    public void onFailure() {
    }
}
