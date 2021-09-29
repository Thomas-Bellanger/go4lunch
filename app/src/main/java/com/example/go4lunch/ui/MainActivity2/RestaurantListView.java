package com.example.go4lunch.ui.MainActivity2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.DI.DI;
import com.example.go4lunch.R;
import com.example.go4lunch.manager.RestaurantManager;
import com.example.go4lunch.service.ApiService;

public class RestaurantListView extends Fragment {
    private final RestaurantManager mRestaurantManager = RestaurantManager.getInstance();
    private ApiService mApiService;
    private RecyclerView mRecyclerView;

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    public void initList() {
        mRecyclerView.setAdapter(new RestaurantListViewAdapter(mApiService.getFilteredRestaurants()));
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
