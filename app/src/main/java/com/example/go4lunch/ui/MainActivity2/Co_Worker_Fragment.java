package com.example.go4lunch.ui.MainActivity2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.manager.UserManager;
import com.example.go4lunch.model.User;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Co_Worker_Fragment extends Fragment {
    private final UserManager userManager = UserManager.getInstance();
    private List<User> mUsers = new ArrayList<>();
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

        return view;
    }

    public void initList() {
        mRecyclerView.setAdapter(new Co_Worker_List_View_Adapter(mUsers));
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();

        userManager.getUserCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
            mUsers = new ArrayList<>();
            if (!queryDocumentSnapshots.isEmpty()) {
                for (QueryDocumentSnapshot userCollection : queryDocumentSnapshots) {
                    User user = userCollection.toObject(User.class);
                    mUsers.add(user);
                }
            }
            initList();
        }).addOnFailureListener(e -> Log.e("fail", e.getMessage()));
    }
}
