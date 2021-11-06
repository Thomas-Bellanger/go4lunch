package com.example.go4lunch.ui.MainActivity2;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.DI.DI;
import com.example.go4lunch.detailmodel.Result;
import com.example.go4lunch.manager.GoogleManager;
import com.example.go4lunch.manager.RestaurantManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.nearbysearchmodel.ResponseAPI;
import com.example.go4lunch.nearbysearchmodel.ResultsItem;
import com.example.go4lunch.repository.GoogleRepository;
import com.example.go4lunch.service.ApiService;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel implements GoogleRepository.Callbacks {
    private static volatile MapViewModel instance;
    private GoogleManager mGoogleManager = GoogleManager.getInstance();
    private RestaurantManager mRestaurantManager = RestaurantManager.getInstance();
    private List<Restaurant> restaurantFiltered = new ArrayList<>();
    public List<Restaurant> restaurantList = new ArrayList<>();
    public MutableLiveData<List<Restaurant>> liveRestaurantsCall = new MutableLiveData<>();
    public String url;
    public String phoneNumber;
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

    public List<Restaurant> filterRestaurant(String filterPattern) {
        restaurantFiltered = new ArrayList<>();
        if (filterPattern == null || filterPattern.length() == 0) {
            restaurantFiltered = restaurantList;
            liveRestaurantsCall.setValue(restaurantFiltered);
            return restaurantFiltered;
        } else {
            String filterLowerCase = filterPattern.toLowerCase();
            for (Restaurant restaurant : restaurantList) {
                if (restaurant.getName().toLowerCase().contains(filterLowerCase)) {
                    restaurantFiltered.add(restaurant);
                }
                else if (restaurant.getType().toLowerCase().contains(filterLowerCase)) {
                    restaurantFiltered.add(restaurant);
                }
            }
        }
        liveRestaurantsCall.setValue(restaurantFiltered);

        return restaurantFiltered;
    }

    public void checkForDetail(Restaurant restaurant){
        mGoogleManager.getDetail(this, restaurant.getUid());
    }

    @Override
    public void onResponse(@Nullable ResponseAPI itemLive) {
        restaurantList = new ArrayList<>();
        liveRestaurantsCall.setValue(new ArrayList<>());
        for (ResultsItem item : itemLive.getResults()){

            Restaurant restaurant = Restaurant.googleRestaurantToRestaurant(item);
            mRestaurantManager.getRestaurantCollection().document(restaurant.getUid()).get().addOnCompleteListener(task -> {
                if(task.isComplete()){
                    if(task.getResult().exists()){
                        mRestaurantManager.getRestaurantData(restaurant).addOnSuccessListener(restaurant1 -> {
                            restaurantList.add(restaurant1);
                            restaurantFiltered.add(restaurant1);
                            liveRestaurantsCall.setValue(restaurantList);
                        });
                    }
                    else {
                        mRestaurantManager.createRestaurantFirebase(restaurant);
                        restaurantList.add(restaurant);
                        restaurantFiltered.add(restaurant);
                    }
                    liveRestaurantsCall.setValue(restaurantList);
                }
            });
            }
        }

    @Override
    public void onFailure() {
    }

    @Override
    public void onResponseDetail(@Nullable Result result) {
                phoneNumber = result.getFormattedPhoneNumber();
                url = result.getWebsite();
    }

    @Override
    public void onFailureDetail() {

    }

    public void populateRestaurant() {
       for (Restaurant restaurant : restaurantList){
           mRestaurantManager.getRestaurantData(restaurant).addOnSuccessListener(restaurant1 -> {
               restaurant.setJoiners(restaurant1.getJoiners());
               restaurant.setNote(restaurant1.getNote());
           });
       }
    }

    public List<Restaurant> getFilteredRestaurants() {
        return restaurantFiltered;
    }

    public void testList(){
        liveRestaurantsCall.setValue(new ArrayList<>());
        restaurantFiltered.clear();
        restaurantList.add(Restaurant.restaurant1);
        restaurantFiltered.add(Restaurant.restaurant1);
        liveRestaurantsCall.setValue(restaurantFiltered);
    }
}
