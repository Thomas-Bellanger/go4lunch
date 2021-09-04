package com.example.go4lunch.service;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Dummy_List_Generator {

    public static List<User> DUMMY_CO_WORKERS = Arrays.asList(
            new User("0", "John", "https://i.pravatar.cc/150?u=a042581f4e29026704d"),
            new User("1", "Tim", "https://i.pravatar.cc/150?u=a042581f4e29026704d"));


    public static List<Restaurant> DUMMY_RESTAURANT = Arrays.asList(
            new Restaurant("restaurant 1", "8 Rue des restaurants", "French", 11, 21, "210", "https://i.pravatar.cc/150?u=a042581f4e29026704d", "https://i.pravatar.cc/150?u=a042581f4e29026704d", 3418, false, 10),
            new Restaurant("restaurant 2", "10 Rue des restaurants", "French", 11, 21, "210", "https://i.pravatar.cc/150?u=a042581f4e29026704d", "https://i.pravatar.cc/150?u=a042581f4e29026704d", 3418, false, 5)
    );

    static List<Restaurant> generateRestaurant() {
        return new ArrayList<>(DUMMY_RESTAURANT);
    }

    static List<User> generateCoWorker() {
        return new ArrayList<>(DUMMY_CO_WORKERS);
    }
}
