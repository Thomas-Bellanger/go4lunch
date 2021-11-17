package com.example.go4lunch.domain.repository;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.model.detailmodel.Result;
import com.example.go4lunch.model.nearbysearchmodel.nearbysearchmodel.ResponseAPI;
import com.example.go4lunch.model.nearbysearchmodel.nearbysearchmodel.ResultsItem;
import com.example.go4lunch.utils.GooglePlaceService;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleRepository {

    private static volatile GoogleRepository instance;
    private ResultsItem nearbySearchResult = new ResultsItem();

    //Callbacks used for Call
    public interface Callbacks{
        void onResponse(@Nullable ResponseAPI response);
        void onFailure();

        void onResponseDetail(@Nullable Result result);
        void onFailureDetail();
    }

    //repository instance
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
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

    //get the detail to get phone number and website url
    public void getDetail (Callbacks callbacksDetail, String id){
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacksDetail);
        // get retrofit instance
        GooglePlaceService googlePlaceService = GooglePlaceService.retrofit.create(GooglePlaceService.class);
        // create the call on the API
        Call<com.example.go4lunch.model.detailmodel.Response> liveDataCall = googlePlaceService.getDetail(id);
        // 2.4 - Start the call
        liveDataCall.enqueue(new Callback<com.example.go4lunch.model.detailmodel.Response>() {
            @Override
            public void onResponse(Call<com.example.go4lunch.model.detailmodel.Response> liveData, Response<com.example.go4lunch.model.detailmodel.Response> response) {
                // Call the proper callback used in controller
                if (callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().onResponseDetail(response.body().getResult());
            }

            @Override
            public void onFailure(Call<com.example.go4lunch.model.detailmodel.Response> call, Throwable t) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
