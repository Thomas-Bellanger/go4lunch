package com.example.go4lunch.DI;

import com.example.go4lunch.service.Dummy_List_api_Service;
import com.example.go4lunch.service.List_api_Service;

public class Di {
    private static final List_api_Service service = new Dummy_List_api_Service();

    public static List_api_Service getListApiService() {
        return service;
    }

    public static List_api_Service getNewInstanceApiService() {
        return new Dummy_List_api_Service();
    }
}

