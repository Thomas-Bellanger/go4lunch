package com.example.go4lunch.utils;

import com.example.go4lunch.R;
import com.example.go4lunch.nearbysearchmodel.ResultsItem;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceService {

    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://maps.googleapis.com/maps/api/place/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
    // For Restaurants search
    @GET("nearbysearch/json?radius=1000&type=restaurant&key=" + R.string.MAPS_API_KEY)
    Call<ResultsItem> getRestaurants(@Query("location") String position);
}
