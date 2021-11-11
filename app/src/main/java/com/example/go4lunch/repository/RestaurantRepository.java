package com.example.go4lunch.repository;

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
    private static volatile RestaurantRepository instance;


    private RestaurantRepository() {
    }
    //repository instance
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
    //get the Restaurant collection in firebase
    public CollectionReference getRestaurantCollection() {
        return FirebaseFirestore.getInstance().collection(RESTAURANT_COLLECTION);
    }
    //get the data for a restaurant in firebase
    public Task<DocumentSnapshot> getRestaurantsData(Restaurant restaurant) {
        String uid = restaurant.getUid();
        return this.getRestaurantCollection().document(uid).get();
    }
    //add a restaurant to collection in firebase
    public void createRestaurantFirebase(Restaurant restaurant) {
        this.getRestaurantCollection().document(restaurant.getUid()).set(restaurant);
        //create an array for "joiners" in firebase
        if(restaurant.getJoiners()==null){
            restaurant.setJoiners(new ArrayList<>());
        }
    }
    //update the "joiners" list on firebase
    public Task<Void> updateJoiners(Restaurant restaurant, List<User> joiners) {
        return this.getRestaurantCollection().document(restaurant.getUid()).update("joiners", joiners);
    }
    //update the restaurant's note on firebase
    public Task<Void> updateNote(Restaurant restaurant, int note) {
        return this.getRestaurantCollection().document(restaurant.getUid()).update("note", note);
    }
}
