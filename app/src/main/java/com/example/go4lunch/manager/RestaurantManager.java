package com.example.go4lunch.manager;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.example.go4lunch.repository.RestaurantRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;

import java.util.List;

public class RestaurantManager {
    private static volatile RestaurantManager instance;
    private final RestaurantRepository restaurantRepository;

    private RestaurantManager() {
        restaurantRepository = RestaurantRepository.getInstance();
    }

    public static RestaurantManager getInstance() {
        RestaurantManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (RestaurantManager.class) {
            if (instance == null) {
                instance = new RestaurantManager();
            }
            return instance;
        }
    }

    public Task<Restaurant> getRestaurantData(Restaurant restaurant) {
        return restaurantRepository.getRestaurantsData(restaurant).continueWith(task -> task.getResult().toObject(Restaurant.class));
    }

    public void createRestaurantFirebase(Restaurant restaurant) {
        restaurantRepository.createRestaurantFirebase(restaurant);
    }

    public CollectionReference getRestaurantCollection() {
        return restaurantRepository.getRestaurantCollection();
    }

    public Task<Void> updateJoiners(Restaurant restaurant, List<User> joiners) {
        return restaurantRepository.updateJoiners(restaurant, joiners);
    }

    public Task<Void> updateNote(Restaurant restaurant, int note) {
        return restaurantRepository.updateNote(restaurant, note);
    }
}
