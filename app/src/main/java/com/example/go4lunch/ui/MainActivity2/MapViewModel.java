package com.example.go4lunch.ui.MainActivity2;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.manager.GoogleManager;
import com.example.go4lunch.manager.RestaurantManager;
import com.example.go4lunch.nearbysearchmodel.ResultsItem;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class MapViewModel {
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

    public void setLocation(LatLng latLng){
        if(latLng!=null){
            mGoogleManager.callRestaurant(latLng.toString());
        }
    }

    public ResultsItem getLiveData(){
        return mGoogleManager.getNearbySearchResult();
    }

    public void googleToFirebase(){
        mRestaurantManager.createRestaurantFirebase(mGoogleManager.getRestaurantItem());
    }
}
