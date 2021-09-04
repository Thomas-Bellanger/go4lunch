package com.example.go4lunch.ui.RestaurantDetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.DI.Di;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityRestaurantDetailBinding;
import com.example.go4lunch.manager.UserManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.example.go4lunch.service.List_api_Service;
import com.example.go4lunch.ui.WebView.WebView;

import java.util.List;

public class RestaurantDetail extends AppCompatActivity {
    public static final String KEY_RESTAURANT = "restaurant";
    private final UserManager userManager = UserManager.getInstance();
    public List<User> joiners;
    private Restaurant mRestaurant;
    private ActivityRestaurantDetailBinding binding;
    private RecyclerView mRecyclerView;
    private Restaurant_Detail_ViewAdapter mAdapter;
    private List_api_Service mList_api_service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList_api_service = Di.getListApiService();
        binding = ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        mRestaurant = intent.getParcelableExtra(KEY_RESTAURANT);
        this.joiners = mRestaurant.getJoiners();
        binding.restaurantDetailName.setText(mRestaurant.getName());
        binding.restaurantDetailAdress.setText(mRestaurant.getAdress());
        binding.restaurantDetailStyle.setText(mRestaurant.getType());
        Glide.with(binding.restaurantDetailAvatar.getContext())
                .load(mRestaurant.getAvatar())
                .apply(RequestOptions.centerCropTransform())
                .into(binding.restaurantDetailAvatar);
        joiners = mList_api_service.getCoWorkers();
        checkIfRestaurantIsLicked();
        configureRecyclerView();
        checkNote();
        binding.buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState();
            }
        });
        binding.fabLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRestaurant();
            }
        });
        binding.buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        binding.buttonWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate();
            }
        });
    }

    public void checkIfRestaurantIsLicked() {
        if (mRestaurant.isLike() == true) {
            binding.buttonLike.setColorFilter(ContextCompat.getColor(this, R.color.yellow));
        } else {
            binding.buttonLike.setColorFilter(ContextCompat.getColor(this, R.color.orange));
        }
        checkNote();
    }

    public void chooseRestaurant() {
        binding.fabLike.setColorFilter(ContextCompat.getColor(this, R.color.green));
        binding.fabLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_circle_24));
        initList();
    }

    public void checkIfRestaurantIsChosen() {
        if (mRestaurant.getJoiners().contains(userManager.getUserData().getResult())) {
            binding.buttonLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_circle_24));
        } else {
            binding.buttonLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_local_dining_24));
        }
    }

    public void changeState() {
        if (mRestaurant.isLike() == false) {
            mRestaurant.setNote(mRestaurant.getNote() + 1);
            mRestaurant.setLike(true);
        } else {
            mRestaurant.setLike(false);
            mRestaurant.setNote(mRestaurant.getNote() - 1);
        }
        checkIfRestaurantIsLicked();
    }

    public void call() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + mRestaurant.getPhoneNumber()));
        startActivity(callIntent);
    }

    public void navigate() {
        Intent navigate = new Intent(RestaurantDetail.this, WebView.class);
        navigate.putExtra(KEY_RESTAURANT, mRestaurant);
        startActivity(navigate);

    }

    public void configureRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView_joiners);
        mAdapter = new Restaurant_Detail_ViewAdapter(joiners);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initList() {
        mRecyclerView.setAdapter(new Restaurant_Detail_ViewAdapter(joiners) {
        });
    }

    public void checkNote() {
        if (mRestaurant.getNote() < 2) {
            binding.restaurantDetailStarIcon.setColorFilter(ContextCompat.getColor(this, R.color.orange));
        } else {
            binding.restaurantDetailStarIcon.setColorFilter(ContextCompat.getColor(this, R.color.yellow));
        }
        if (mRestaurant.getNote() < 6) {
            binding.restaurantDetailStarIcon2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        } else {
            binding.restaurantDetailStarIcon2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
        }
        if (mRestaurant.getNote() < 9) {
            binding.restaurantDetailStarIcon3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        } else {
            binding.restaurantDetailStarIcon3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
        }

    }
}