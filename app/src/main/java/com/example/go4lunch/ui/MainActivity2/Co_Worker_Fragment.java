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
import com.example.go4lunch.model.User;
import com.example.go4lunch.service.ApiService;

import java.util.List;

public class Co_Worker_Fragment extends Fragment {
    private ApiService mApiService;
    private RecyclerView mRecyclerView;
    //fragment instance
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
        mApiService = DI.getAPIService();
        //get the list to show
        mApiService.getLiveUsers().observe(getActivity(), this::initList);

        return view;
    }
    //refresh the list
    private void initList(List<User> users) {
        mRecyclerView.setAdapter(new Co_Worker_List_View_Adapter(users));
    }
}
