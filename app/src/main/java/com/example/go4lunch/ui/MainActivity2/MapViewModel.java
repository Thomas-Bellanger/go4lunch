package com.example.go4lunch.ui.MainActivity2;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.detailmodel.Result;
import com.example.go4lunch.manager.GoogleManager;
import com.example.go4lunch.manager.RestaurantManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.nearbysearchmodel.ResponseAPI;
import com.example.go4lunch.nearbysearchmodel.ResultsItem;
import com.example.go4lunch.repository.GoogleRepository;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel implements GoogleRepository.Callbacks {
    private static volatile MapViewModel instance;
    public List<Restaurant> restaurantList = new ArrayList<>();
    public MutableLiveData<List<Restaurant>> liveRestaurantsCall = new MutableLiveData<>();
    public String url;
    public String phoneNumber;
    private final GoogleManager mGoogleManager = GoogleManager.getInstance();
    private final RestaurantManager mRestaurantManager = RestaurantManager.getInstance();
    private List<Restaurant> restaurantFiltered = new ArrayList<>();

    //instance of the service
    public static MapViewModel getInstance() {
        MapViewModel result = instance;
        if (instance != null) {
            return result;
        } else {
            instance = new MapViewModel();
        }
        return instance;
    }
    //call api place with users location in parameter
    public void setLocation(LatLng latLng) {
        if (latLng != null) {
            mGoogleManager.fetchRestaurant(this, latLng.getLatitude() + "," + latLng.getLongitude());
        }
    }
    //fiter for restaurant list
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
                } else if (restaurant.getType().toLowerCase().contains(filterLowerCase)) {
                    restaurantFiltered.add(restaurant);
                }
            }
        }
        liveRestaurantsCall.setValue(restaurantFiltered);

        return restaurantFiltered;
    }
    //call detail api with restaurant uid in parameter
    public void checkForDetail(Restaurant restaurant) {
        mGoogleManager.getDetail(this, restaurant.getUid());
    }
    //get response of the api place
    @Override
    public void onResponse(@Nullable ResponseAPI itemLive) {
        restaurantList = new ArrayList<>();
        liveRestaurantsCall.setValue(new ArrayList<>());
        restaurantFiltered.clear();
        //create a "Restaurant" from response item and add it to firebase if not in yet, else get data from it
        for (ResultsItem item : itemLive.getResults()) {
            Restaurant restaurant = Restaurant.googleRestaurantToRestaurant(item);
            mRestaurantManager.getRestaurantCollection().document(restaurant.getUid()).get().addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    if (task.getResult().exists()) {
                        mRestaurantManager.getRestaurantData(restaurant).addOnSuccessListener(restaurant1 -> {
                            restaurantList.add(restaurant1);
                            restaurantFiltered.add(restaurant1);
                            liveRestaurantsCall.setValue(restaurantList);
                        });
                    } else {
                        mRestaurantManager.createRestaurantFirebase(restaurant);
                        restaurantList.add(restaurant);
                        restaurantFiltered.add(restaurant);
                        liveRestaurantsCall.setValue(restaurantList);
                    }

                }
            });
        }
    }

    @Override
    public void onFailure() {
        //Log.e("fail place", "fail");
    }

    @Override
    public void onResponseDetail(@Nullable Result result) {
        phoneNumber = result.getFormattedPhoneNumber();
        url = result.getWebsite();
    }

    @Override
    public void onFailureDetail() {
        //Log.e("fail detail", "fail");
    }

    //refresh restaurant list with data from firebase
    public void populateRestaurant() {
        for (Restaurant restaurant : restaurantList) {
            mRestaurantManager.getRestaurantData(restaurant).addOnSuccessListener(restaurant1 -> {
                restaurant.setJoiners(restaurant1.getJoiners());
                restaurant.setNote(restaurant1.getNote());
            });
        }
    }

    //restaurant list after filter
    public List<Restaurant> getFilteredRestaurants() {
        return restaurantFiltered;
    }

    public void testList() {
        liveRestaurantsCall.setValue(new ArrayList<>());
        restaurantFiltered.clear();
        restaurantList.add(Restaurant.restaurant1);
        restaurantFiltered.add(Restaurant.restaurant1);
        liveRestaurantsCall.setValue(restaurantFiltered);
    }
}
