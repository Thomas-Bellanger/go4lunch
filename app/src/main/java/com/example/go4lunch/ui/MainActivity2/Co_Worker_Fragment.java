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
import com.example.go4lunch.manager.UserManager;
import com.example.go4lunch.service.ApiService;

public class Co_Worker_Fragment extends Fragment {
    private final UserManager userManager = UserManager.getInstance();
    private ApiService mApiService;
    private RecyclerView mRecyclerView;

    public static Co_Worker_Fragment newInstance() {
        return new Co_Worker_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview_workmates, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        mApiService = DI.getASIService();

        return view;
    }

    public void initList() {
        mRecyclerView.setAdapter(new Co_Worker_List_View_Adapter(mApiService.getFilteredUsers()));
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        initList();
    }
}
