package com.example.go4lunch.service;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.manager.RestaurantManager;
import com.example.go4lunch.manager.UserManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.example.go4lunch.ui.MainActivity2.MapViewModel;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ApiService implements ApiServiceInterface {
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Restaurant> restaurantFiltered = new ArrayList<>();
    private List<User> userFiltered = new ArrayList<>();
    private List<Restaurant> favorites = new ArrayList<>();
    private UserManager mUserManager = UserManager.getInstance();
    private RestaurantManager mRestaurantManager = RestaurantManager.getInstance();
    private MutableLiveData<List<Restaurant>> liveRestaurants = new MutableLiveData<>();
    private MutableLiveData<List<User>> liveUsers = new MutableLiveData<>();
    private MutableLiveData<String> liveDistance = new MutableLiveData<>();
    private MutableLiveData<String> sort= new MutableLiveData();
    private MapViewModel mMapViewModel = MapViewModel.getInstance();

    @Override
    public List<Restaurant> filterRestaurant(String filterPattern) {
        restaurantFiltered = new ArrayList<>();
        if (filterPattern == null || filterPattern.length() == 0) {
            restaurantFiltered = restaurants;
            liveRestaurants.setValue(restaurantFiltered);
            return restaurantFiltered;
        } else {
            String filterLowerCase = filterPattern.toLowerCase();
            for (Restaurant restaurant : restaurants) {
                if (restaurant.getName().toLowerCase().contains(filterLowerCase)) {
                restaurantFiltered.add(restaurant);
            }
               else if (restaurant.getType().toLowerCase().contains(filterLowerCase)) {
                    restaurantFiltered.add(restaurant);
                }
            }
        }
        liveRestaurants.setValue(restaurantFiltered);
        return restaurantFiltered;
    }

    @Override
    public List<User> filterUser(String filterPattern) {
        userFiltered= new ArrayList<>();
        if (filterPattern == null || filterPattern.length() == 0) {
            userFiltered = users;
        } else {
            String filterLowerCase = filterPattern.toLowerCase();
            for (User user : users) {
                if (user.getName().toLowerCase().contains(filterLowerCase)) {
                    userFiltered.add(user);
                } else if (user.getChosenRestaurant().getName().toLowerCase().contains(filterLowerCase)) {
                    userFiltered.add(user);
                }
            }
        }
        liveUsers.setValue(userFiltered);
        return userFiltered;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    @Override
    public List<Restaurant> getFilteredRestaurants() {
        return restaurantFiltered;
    }

    @Override
    public List<User> getFilteredUsers() {
        return userFiltered;
    }

    @Override
    public List<Restaurant> getFavorites() {
        return favorites;
    }

    @Override
    public void populateUser() {
        users.clear();
        userFiltered.clear();
        liveUsers.setValue(new ArrayList<>());
        mUserManager.getUserCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (QueryDocumentSnapshot userCollection : queryDocumentSnapshots) {
                    User user = userCollection.toObject(User.class);
                    users.add(user);
                    userFiltered.add(user);
                    liveUsers.setValue(userFiltered);
                }
            }
        }).addOnFailureListener(e ->  {
            //Log.e("fail", e.getMessage())
        });
    }

    @Override
    public LiveData<List<Restaurant>> getLiveRestaurant() {
        liveRestaurants.setValue(restaurantFiltered);
        return liveRestaurants;
    }

    @Override
    public LiveData<List<User>> getLiveUsers() {
        liveUsers.setValue(userFiltered);
        return liveUsers;
    }

    @Override
    public MutableLiveData<String> getLiveDistance() {
        return liveDistance;
    }

    public static class RestaurantAZComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant left, Restaurant right) {
            return left.getName().compareTo(right.getName());
        }
    }

    public static class RestaurantDistanceComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant left, Restaurant right) {
            if (left.getDistance() > right.getDistance()) {
                Log.e("return comparator", "1");
                return 1;
            }
            if (left.getDistance() < right.getDistance()) {
                return -1;
            }
            return 0;
        }
    }
    public static class RestaurantNoteComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant left, Restaurant right) {
            if (left.getNote() > right.getNote()) {
                Log.e("return comparator", "1");
                return 1;
            }
            if (left.getNote() < right.getNote()) {
                return -1;
            }
            return 0;
        }
    }
    public MutableLiveData<String> getSort(){
        return sort;
    }
}

