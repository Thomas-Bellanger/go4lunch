package com.example.go4lunch.domain.service;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.domain.manager.UserManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ApiService implements ApiServiceInterface {
    private List<User> users = new ArrayList<>();
    private List<Restaurant> restaurantFiltered = new ArrayList<>();
    private List<User> userFiltered = new ArrayList<>();
    private UserManager mUserManager = UserManager.getInstance();
    private MutableLiveData<List<User>> liveUsers = new MutableLiveData<>();
    private MutableLiveData<String> liveDistance = new MutableLiveData<>();
    private MutableLiveData<String> sort= new MutableLiveData();

    //filter for users
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
    //users list
    @Override
    public List<User> getUsers() {
        return users;
    }
    //users list after filter
    @Override
    public List<User> getFilteredUsers() {
        return userFiltered;
    }
    //get users from firebase
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
    //live users livedata
    @Override
    public LiveData<List<User>> getLiveUsers() {
        liveUsers.setValue(userFiltered);
        return liveUsers;
    }
    //live data distance
    @Override
    public MutableLiveData<String> getLiveDistance() {
        return liveDistance;
    }
    //comparators for restaurant list
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

