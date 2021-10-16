package com.example.go4lunch.ui.MainActivity2;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.manager.GoogleManager;
import com.example.go4lunch.manager.RestaurantManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.nearbysearchmodel.ResultsItem;
import com.example.go4lunch.repository.GoogleRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.IOException;

public class MapViewModel implements GoogleRepository.Callbacks {
    private static volatile MapViewModel instance;
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
            mGoogleManager.fetchRestaurant(this,latLng.toString());
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
    public void onResponse(@Nullable ResultsItem item) {
        googleToFirebase(Restaurant.googleRestaurantToRestaurant(item));
    }

    @Override
    public void onFailure() {
    }
}
