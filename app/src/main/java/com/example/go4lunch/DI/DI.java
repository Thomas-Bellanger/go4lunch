package com.example.go4lunch.DI;

import com.example.go4lunch.domain.service.ApiService;

public class DI {
    private static final ApiService service = new ApiService();

    public static ApiService getAPIService() {
        return service;
    }
}
