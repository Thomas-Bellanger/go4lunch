package com.example.go4lunch.ui.RestaurantDetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.DI.DI;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityRestaurantDetailBinding;
import com.example.go4lunch.domain.manager.RestaurantManager;
import com.example.go4lunch.domain.manager.UserManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.example.go4lunch.domain.service.ApiService;
import com.example.go4lunch.ui.MainActivity.viewModel.MapViewModel;
import com.example.go4lunch.ui.RestaurantDetail.adapter.Restaurant_Detail_ViewAdapter;
import com.example.go4lunch.ui.WebView.WebView;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetail extends AppCompatActivity {
    public static final String KEY_RESTAURANT = "restaurant";
    private final UserManager userManager = DI.getUserManager();
    private final User currentUser = User.firebaseUserToUser(userManager.getCurrentUser());
    private final RestaurantManager restaurantManager = DI.getRestaurantManager();
    public List<User> joiners;
    private Restaurant mRestaurant;
    private ActivityRestaurantDetailBinding binding;
    private RecyclerView mRecyclerView;
    private Restaurant_Detail_ViewAdapter mAdapter;
    @Nullable
    private Restaurant chosenRestaurant;
    private List<Restaurant> favorites = new ArrayList<>();
    private boolean chosen = false;
    private boolean liked = false;
    private final ApiService mApiService = DI.getAPIService();
    private final MapViewModel mMapViewModel = MapViewModel.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        mRestaurant = intent.getParcelableExtra(KEY_RESTAURANT);
        mMapViewModel.checkForDetail(mRestaurant);
        binding.restaurantDetailName.setText(mRestaurant.getName());
        binding.restaurantDetailAdress.setText(mRestaurant.getAdress());
        binding.restaurantDetailStyle.setText(mRestaurant.getType());
        Glide.with(binding.restaurantDetailAvatar.getContext())
                .load(mRestaurant.getAvatar())
                .apply(RequestOptions.centerCropTransform())
                .into(binding.restaurantDetailAvatar);
        this.joiners = mRestaurant.getJoiners();
        configureRecyclerView();
        //call
        binding.buttonCall.setOnClickListener(v -> call());
        //website
        binding.buttonWebsite.setOnClickListener(v -> navigate());
        //like the restaurant
        binding.buttonLike.setOnClickListener(v ->
                changeState());
        //choose this restaurant...or not
        binding.fabLike.setOnClickListener(v -> chooseRestaurant());
    }

    //change the like star if liked
    public void checkIfRestaurantIsLicked() {
        for (Restaurant restaurant : favorites)
            if ((restaurant.getUid().equals(mRestaurant.getUid()))) {
                liked = true;
            }
        setStar();
    }

    //change the color of the "star button" if the restaurant is liked or not
    public void setStar() {
        if (liked) {
            binding.buttonLike.setColorFilter(ContextCompat.getColor(this, R.color.yellow));
        } else {
            binding.buttonLike.clearColorFilter();
        }
        checkNote();
    }

    //check is the restaurant is already chosen by the user
    public void checkIfRestaurantIsChosen() {
        if (chosenRestaurant.getUid().equals(mRestaurant.getUid())) {
            fabChecked();
        } else {
            fabCleared();
        }
    }

    //choose restaurant function and check if the user already had a chosen restaurant
    public void chooseRestaurant() {
        if (chosen) {
            userManager.updateLunch(null).addOnSuccessListener(unused -> {
                fabCleared();
                removeUser(mRestaurant);
                Toast.makeText(RestaurantDetail.this, getResources().getString(R.string.canceled), Toast.LENGTH_SHORT).show();
            });
        } else {
            removeUser(chosenRestaurant);
            joiners.add(currentUser);
            userManager.updateLunch(mRestaurant).addOnSuccessListener(unused -> {
                fabChecked();
                restaurantManager.updateJoiners(mRestaurant, joiners).addOnSuccessListener(unused12 -> {
                    Toast.makeText(RestaurantDetail.this, getResources().getString(R.string.restaurantChosen), Toast.LENGTH_SHORT).show();
                    initList();
                });
            });
        }
        mApiService.populateUser();
    }

    //remove the user from the joiners list
    public void removeUser(Restaurant restaurant) {
        for (User user : restaurant.getJoiners()) {
            if (user.getUid().equals(currentUser.getUid())) {
                restaurant.getJoiners().remove(user);
                restaurantManager.updateJoiners(restaurant, restaurant.getJoiners()).addOnSuccessListener(unused12 -> {
                    //Log.e("removed from joiners", "list size " + restaurant.getJoiners().size());
                    initList();
                });
            }
        }
    }

    //add or remove the restaurant from user's favorites
    public void changeState() {
        if (liked) {
            removeFromFavorites();
        } else {
            addFavorites();
        }
        restaurantManager.updateNote(mRestaurant, mRestaurant.getNote()).addOnSuccessListener(unused -> {
        });
        userManager.updateFavorites(favorites).addOnSuccessListener(unused -> {
            //Log.e("favorite", "size    " + favorites.size())
        });
        checkIfRestaurantIsLicked();
    }

    public void addFavorites() {
        mRestaurant.setNote(mRestaurant.getNote() + 1);
        favorites.add(mRestaurant);
        liked = true;
    }

    public void removeFromFavorites() {
        for (Restaurant restaurant : favorites) {
            if ((restaurant.getUid().equals(mRestaurant.getUid()))) {
                favorites.remove(restaurant);
                mRestaurant.setNote(mRestaurant.getNote() - 1);
                liked = false;
            }
        }
    }

    //check the note of the restaurant and show stars
    public void checkNote() {
        if (mRestaurant.getNote() < 50) {
            binding.restaurantDetailStarIcon.setColorFilter(ContextCompat.getColor(this, R.color.orange));
        } else {
            binding.restaurantDetailStarIcon.setColorFilter(ContextCompat.getColor(this, R.color.yellow));
        }
        if (mRestaurant.getNote() < 100) {
            binding.restaurantDetailStarIcon2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        } else {
            binding.restaurantDetailStarIcon2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
        }
        if (mRestaurant.getNote() < 150) {
            binding.restaurantDetailStarIcon3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        } else {
            binding.restaurantDetailStarIcon3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
        }
    }

    //call intent
    public void call() {
        String number = mMapViewModel.phoneNumber;
        if (number == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.noNumber), Toast.LENGTH_SHORT).show();
        } else {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + number));
            startActivity(callIntent);
        }
    }

    //website intent
    public void navigate() {
        String url = mMapViewModel.url;
        if (url == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.noUrl), Toast.LENGTH_SHORT).show();
        } else {
            mRestaurant.setUrl(url);
            Intent navigate = new Intent(RestaurantDetail.this, WebView.class);
            navigate.putExtra(KEY_RESTAURANT, mRestaurant);
            startActivity(navigate);
        }
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
        //get restaurant joiner
        mRestaurant.getJoiners().clear();
        userManager.getUserCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot userCollection : queryDocumentSnapshots) {
                User user = userCollection.toObject(User.class);
                if (user.getChosenRestaurant() == null) {
                    fabCleared();
                } else {
                    if (user.getChosenRestaurant().getUid().equals(mRestaurant.getUid())) {
                        joiners.add(user);
                        restaurantManager.updateJoiners(mRestaurant, joiners).addOnSuccessListener(unused ->
                                initList());
                    }
                }
            }
        }).addOnFailureListener(e -> {//Log.e("fail", e.getMessage())
        });
        //get current user data and avoid error
        userManager.getUserData().addOnSuccessListener(user -> {
                    chosenRestaurant = user.getChosenRestaurant();
                    favorites = user.getFavorite();
                    if (chosenRestaurant == null) {
                        chosenRestaurant = Restaurant.noRestaurant;
                    }
                    checkIfRestaurantIsChosen();
                    checkIfRestaurantIsLicked();
                }
        ).addOnFailureListener(e -> {
            //Log.e("fail", e.getMessage())
        });
        restaurantManager.getRestaurantData(mRestaurant).addOnSuccessListener(restaurant -> mRestaurant.setNote(restaurant.getNote()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //refresh data with firebase
        mMapViewModel.populateRestaurant();
        mApiService.populateUser();
    }
}