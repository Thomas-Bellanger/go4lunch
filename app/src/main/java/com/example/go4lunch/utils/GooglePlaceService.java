package com.example.go4lunch.utils;

import android.media.Image;

import com.example.go4lunch.detailmodel.Response;
import com.example.go4lunch.nearbysearchmodel.PhotosItem;
import com.example.go4lunch.nearbysearchmodel.ResponseAPI;

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
    @GET("nearbysearch/json?radius=6000&type=restaurant&key=AIzaSyC77ax8lhHQbeqgqiqJ7rqhJfCdEWE4FCk")
    Call<ResponseAPI> getRestaurants(@Query("location") String location);
    //fFor detail search
    @GET("details/json?place_id=ChIJN1t_tDeuEmsRUsoyG83frY4&fields=name%2Crating%2Cformatted_phone_number&key=AIzaSyC77ax8lhHQbeqgqiqJ7rqhJfCdEWE4FCk")
    Call<Response> getDetail(@Query("placeid") String id);
    @GET("photo?&photo_reference=&key=AIzaSyC77ax8lhHQbeqgqiqJ7rqhJfCdEWE4FCk")
    Call<Image> getPhoto(@Query("photo_reference") String ref);

}
