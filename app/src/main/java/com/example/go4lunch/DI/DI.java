package com.example.go4lunch.DI;

import com.example.go4lunch.domain.manager.GoogleManager;
import com.example.go4lunch.domain.manager.RestaurantManager;
import com.example.go4lunch.domain.manager.UserManager;
import com.example.go4lunch.domain.service.ApiService;

public class DI {
    private static final ApiService service = new ApiService();
    private static GoogleManager mGoogleManager = GoogleManager.getInstance();
    private static UserManager mUserManager = UserManager.getInstance();
    private static RestaurantManager mRestaurantManager = RestaurantManager.getInstance();

    public static ApiService getAPIService() {
        return service;
    }

    public static GoogleManager getGoogleManager() {
        return mGoogleManager;}

    public static UserManager getUserManager() {
        return mUserManager;}

    public static RestaurantManager getRestaurantManager() {
        return mRestaurantManager;}
    }

