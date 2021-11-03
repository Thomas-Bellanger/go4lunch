package com.example.go4lunch.ui.MainActivity2;

import android.media.Image;
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
    private ApiService mApiService = DI.getASIService();
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

    public void googleToFirebase(Restaurant restaurant){
        mRestaurantManager.getRestaurantCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.getDocuments().contains(restaurant.getUid())){
            }
            else {
                mRestaurantManager.createRestaurantFirebase(restaurant);
            }
        });
    }

    public void checkForDetail(Restaurant restaurant){
        mGoogleManager.getDetail(this, restaurant.getUid());
    }

    public void checkForPhoto(Restaurant restaurant){
        mGoogleManager.getPhoto(this, restaurant.getAvatar());
    }

    @Override
    public void onResponse(@Nullable ResponseAPI itemLive) {
        restaurantList = new ArrayList<>();
        liveRestaurantsCall.setValue(new ArrayList<>());
        for (ResultsItem item : itemLive.getResults()){
            Log.e("call", ""+liveRestaurantsCall.getValue().size());
            Restaurant restaurant = Restaurant.googleRestaurantToRestaurant(item);
            mRestaurantManager.createRestaurantFirebase(restaurant);
            restaurantList.add(restaurant);
            liveRestaurantsCall.setValue(restaurantList);
        }
    }

    @Override
    public void onFailure() {
    }

    @Override
    public void onResponsePhoto(@Nullable Image image) {
        Log.e("photo", ""+image);
    }

    @Override
    public void onFailurePhoto() {
    }

    @Override
    public void onResponseDetail(@Nullable Result result) {
        Log.e("call", result.getFormattedPhoneNumber());
                phoneNumber = result.getFormattedPhoneNumber();
                url = result.getWebsite();
    }

    @Override
    public void onFailureDetail() {

    }


}
