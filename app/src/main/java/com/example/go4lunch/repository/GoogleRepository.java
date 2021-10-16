package com.example.go4lunch.repository;

import androidx.annotation.Nullable;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.nearbysearchmodel.ResultsItem;
import com.example.go4lunch.utils.GooglePlaceService;

import java.io.IOException;
import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleRepository {

    private static volatile GoogleRepository instance;
    private ResultsItem nearbySearchResult = new ResultsItem();

    public interface Callbacks{
        void onResponse(@Nullable ResultsItem item);
        void onFailure();
    }


    public static GoogleRepository getInstance() {
        GoogleRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new GoogleRepository();
            }
            return instance;
        }
    }

    public ResultsItem callRestaurant(String location) throws IOException {
        GooglePlaceService googlePlaceService = GooglePlaceService.retrofit.create(GooglePlaceService.class);
        ResultsItem liveDataCall = googlePlaceService.getRestaurants(location).execute().body();
        nearbySearchResult = liveDataCall;

        return liveDataCall;
    }
    public void fetchRestaurant(Callbacks callbacks, String location){
        //Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);
        // get retrofit instance
        GooglePlaceService googlePlaceService = GooglePlaceService.retrofit.create(GooglePlaceService.class);
        // create the call on the API
        Call<ResultsItem> liveDataCall = googlePlaceService.getRestaurants(location);
        // 2.4 - Start the call
        liveDataCall.enqueue(new Callback<ResultsItem>() {

            @Override
            public void onResponse(Call<ResultsItem> liveDataCall, Response<ResultsItem> response) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<ResultsItem> call, Throwable t) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

}
