package com.example.go4lunch.ui.RestaurantDetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityRestaurantDetailBinding;
import com.example.go4lunch.manager.RestaurantManager;
import com.example.go4lunch.manager.UserManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.example.go4lunch.ui.WebView.WebView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetail extends AppCompatActivity {
    public static final String KEY_RESTAURANT = "restaurant";
    private final UserManager userManager = UserManager.getInstance();
    private final User currentUser = User.firebaseUserToUser(userManager.getCurrentUser());
    private final RestaurantManager restaurantManager = RestaurantManager.getInstance();
    public List<User> joiners;
    private Restaurant mRestaurant;
    private ActivityRestaurantDetailBinding binding;
    private RecyclerView mRecyclerView;
    private Restaurant_Detail_ViewAdapter mAdapter;
    @Nullable
    private Restaurant chosenRestaurant;
    private List<Restaurant> favorites;
    private boolean chosen = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        configureRecyclerView();
        checkNote();
        //call
        binding.buttonCall.setOnClickListener(v -> call());
        //website
        binding.buttonWebsite.setOnClickListener(v -> navigate());
        //like the restaurant
        binding.buttonLike.setOnClickListener(v -> changeState());
        //choose this restaurant...or not
        binding.fabLike.setOnClickListener(v -> chooseRestaurant());
    }

    //change the like star if liked
    public void checkIfRestaurantIsLicked() {
        if (favorites.contains(mRestaurant)) {
            binding.buttonLike.setColorFilter(ContextCompat.getColor(this, R.color.yellow));
        } else {
            binding.buttonLike.setColorFilter(ContextCompat.getColor(this, R.color.orange));
        }
        checkNote();
    }

    //choose restaurant function and check if the user already had a chosen restaurant
    public void chooseRestaurant() {
        if (chosen) {
            userManager.updateLunch(null).addOnSuccessListener(unused -> {
                Toast.makeText(RestaurantDetail.this, "Canceled", Toast.LENGTH_SHORT).show();
                fabCleared();
                mRestaurant.removeJoiners(currentUser);
                initList();
            });
        } else {
            userManager.updateLunch(mRestaurant).addOnSuccessListener(unused -> {
                Toast.makeText(RestaurantDetail.this, "Restaurant chosen!", Toast.LENGTH_SHORT).show();
                fabChecked();
                joiners.add(currentUser);
                initList();
            });
        }
    }

    //check is the restaurant is already chosen by the user
    public void checkIfRestaurantIsChosen() {
        if (chosenRestaurant.getName().equals(mRestaurant.getName())) {
            fabChecked();
        } else {
            fabCleared();
        }
    }

    //add or remove the restaurant from user's favorites
    public void changeState() {
        if (favorites.contains(mRestaurant)) {
            mRestaurant.setNote(mRestaurant.getNote() - 1);
            favorites.remove(mRestaurant);
        } else {
            mRestaurant.setNote(mRestaurant.getNote() + 1);
            favorites.add(mRestaurant);
        }
        checkIfRestaurantIsLicked();
    }

    //call intent
    public void call() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + mRestaurant.getPhoneNumber()));
        startActivity(callIntent);
    }

    //website intent
    public void navigate() {
        Intent navigate = new Intent(RestaurantDetail.this, WebView.class);
        navigate.putExtra(KEY_RESTAURANT, mRestaurant);
        startActivity(navigate);
    }

    //recycler view
    public void configureRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView_joiners);
        mAdapter = new Restaurant_Detail_ViewAdapter(joiners);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    //update the list for recyclerview
    public void initList() {
        mAdapter.update(joiners);
    }

    //check the note of the restaurant and show stars
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

    @Override
    protected void onResume() {
        super.onResume();
        userManager.getUserData().addOnSuccessListener(user -> {
            chosenRestaurant = user.getChosenRestaurant();
            favorites = user.getFavorite();
            if (chosenRestaurant == null) {
                chosenRestaurant = Restaurant.noRestaurant;
            }
            checkIfRestaurantIsChosen();
        });
        Log.e("restaurantuid", "" + mRestaurant.getUid());
    }

    public void fabChecked() {
        binding.fabLike.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green));
        binding.fabLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_circle_24));
        chosen = true;
    }

    public void fabCleared() {
        binding.fabLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_local_dining_24));
        binding.fabLike.clearColorFilter();
        chosen = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        restaurantManager.getRestaurantData(mRestaurant).addOnSuccessListener(restaurant -> joiners = restaurant.getJoiners())
                .addOnFailureListener(e -> Log.e("fail", e.getMessage()));
    }

}