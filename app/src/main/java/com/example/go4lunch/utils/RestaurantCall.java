package com.example.go4lunch.utils;

import androidx.annotation.Nullable;

import com.example.go4lunch.model.Restaurant;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantCall {
    // 2 - Public method to start fetching users following by Jake Wharton
    public static void fetchRestaurant(Callbacks callbacks, String restaurantname) {

        // 2.1 - Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);

        // 2.2 - Get a Retrofit instance and the related endpoints
        RestaurantService restaurantService = RestaurantService.retrofit.create(RestaurantService.class);

        // 2.3 - Create the call on Github API
        Call<List<Restaurant>> call = restaurantService.getRestaurant();
        // 2.4 - Start the call
        call.enqueue(new Callback<List<Restaurant>>() {

            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

    // 1 - Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable List<Restaurant> restaurants);

        void onFailure();
    }
}
