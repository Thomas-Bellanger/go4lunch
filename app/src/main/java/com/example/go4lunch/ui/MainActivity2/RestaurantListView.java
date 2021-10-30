package com.example.go4lunch.ui.MainActivity2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.DI.DI;
import com.example.go4lunch.R;
import com.example.go4lunch.manager.RestaurantManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.service.ApiService;

import java.util.Collections;
import java.util.List;

public class RestaurantListView extends Fragment {
    private final RestaurantManager mRestaurantManager = RestaurantManager.getInstance();
    private ApiService mApiService;
    private RecyclerView mRecyclerView;
    public static String ALPHABETICAL = "alphabetical";
    public static String DISTANCE = "distance";
    private MapViewModel mMapViewModel = MapViewModel.getInstance();

    public static RestaurantListView newInstance() {
        return new RestaurantListView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_recyclerview, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mApiService = DI.getASIService();
        mApiService.getLiveDistance().observe(this.getActivity(), this::update);
        mApiService.getLiveRestaurant().observe(getActivity(), this::initList);
        mApiService.getSort().observe(this.getActivity(), this::sortBy);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initList(List<Restaurant> restaurants) {
        mRecyclerView.setAdapter(new RestaurantListViewAdapter(restaurants));
    }

    private void update(String s) {
        initList(mApiService.getFilteredRestaurants());
    }


    @Override
    public void onStart() {
        super.onStart();
    }



    public void sortBy(String sort){
        List<Restaurant> restaurants = mMapViewModel.liveRestaurantsCall.getValue();
        if (sort.equals(ALPHABETICAL)){
            Collections.sort(restaurants, new ApiService.RestaurantAZComparator());
        }
        if (sort.equals(DISTANCE)){
            Collections.sort(restaurants, new ApiService.RestaurantDistanceComparator());
        }
            initList(restaurants);
    }
}
