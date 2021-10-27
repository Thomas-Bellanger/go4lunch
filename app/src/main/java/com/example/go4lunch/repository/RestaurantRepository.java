package com.example.go4lunch.repository;

import android.util.Log;

import com.example.go4lunch.manager.RestaurantManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class RestaurantRepository {
    private static final String RESTAURANT_COLLECTION = "restaurants";
    private static final String RESTAURANT_NAME = "restaurantName";
    private static volatile RestaurantRepository instance;


    private RestaurantRepository() {
    }

    public static RestaurantRepository getInstance() {
        RestaurantRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (RestaurantRepository.class) {
            if (instance == null) {
                instance = new RestaurantRepository();
            }
            return instance;
        }
    }

    public CollectionReference getRestaurantCollection() {
        return FirebaseFirestore.getInstance().collection(RESTAURANT_COLLECTION);
    }

    public Task<DocumentSnapshot> getRestaurantsData(Restaurant restaurant) {
        String uid = restaurant.getUid();
        return this.getRestaurantCollection().document(uid).get();
    }

    public void createRestaurantFirebase(Restaurant restaurant) {
        this.getRestaurantCollection().document(restaurant.getUid()).set(restaurant);
        if(restaurant.getJoiners()==null){
            restaurant.setJoiners(new ArrayList<>());
        }
    }

    public Task<Void> updateJoiners(Restaurant restaurant, List<User> joiners) {
        return this.getRestaurantCollection().document(restaurant.getUid()).update("joiners", joiners);
    }

    public Task<Void> updateNote(Restaurant restaurant, int note) {
        return this.getRestaurantCollection().document(restaurant.getUid()).update("note", note);
    }
}
