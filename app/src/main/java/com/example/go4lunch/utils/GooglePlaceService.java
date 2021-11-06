package com.example.go4lunch.utils;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.detailmodel.Response;
import com.example.go4lunch.nearbysearchmodel.ResponseAPI;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceService {

    String API_KEY = BuildConfig.API_KEY;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // For Restaurants search
    @GET("nearbysearch/json?radius=600&type=restaurant&key="+API_KEY)
    Call<ResponseAPI> getRestaurants(@Query("location") String location);

    //fFor detail search
    @GET("details/json?fields=name%2Crating%2Cformatted_phone_number%2Cwebsite&key="+API_KEY)
    Call<Response> getDetail(@Query("place_id") String id);
}
