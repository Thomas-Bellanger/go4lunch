package com.example.go4lunch.repository;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.go4lunch.nearbysearchmodel.ResponseAPI;
import com.example.go4lunch.nearbysearchmodel.ResultsItem;
import com.example.go4lunch.utils.GooglePlaceService;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleRepository {

    private static volatile GoogleRepository instance;
    private ResultsItem nearbySearchResult = new ResultsItem();

    public interface Callbacks{
        void onResponse(@Nullable ResponseAPI response);
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

    public void fetchRestaurant(Callbacks callbacks, String location){
        //Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);
        // get retrofit instance
        GooglePlaceService googlePlaceService = GooglePlaceService.retrofit.create(GooglePlaceService.class);
        // create the call on the API
        Call<ResponseAPI> liveDataCall = googlePlaceService.getRestaurants(location);
        // 2.4 - Start the call
        liveDataCall.enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> liveDataCall, Response<ResponseAPI> response) {
                // Call the proper callback used in controller (MainFragment)

                if (callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().onResponse(response.body());
                Log.e("checkbody","ok"+response.body().getResults());
                Log.e("check","ok"+response.toString());
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                Log.e("checkNOK",t.getMessage());
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

}
