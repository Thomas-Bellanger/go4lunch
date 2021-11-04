package com.example.go4lunch.ui.MainActivity2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.DI.DI;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMain2Binding;
import com.example.go4lunch.manager.RestaurantManager;
import com.example.go4lunch.manager.UserManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.example.go4lunch.service.ApiService;
import com.example.go4lunch.service.ApiServiceInterface;
import com.example.go4lunch.ui.MainActivity.MainActivity;
import com.example.go4lunch.ui.RestaurantDetail.RestaurantDetail;
import com.example.go4lunch.ui.SettingsActivity.SettingsActivity;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.Collections;

public class MainActivity2 extends AppCompatActivity {

    public static String toolbarTitle = "title";
    public static String searchTip = "tip";
    private final UserManager userManager = UserManager.getInstance();
    private final RestaurantManager mRestaurantManager = RestaurantManager.getInstance();
    MapViewModel mMapViewModel = MapViewModel.getInstance();
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ActivityMain2Binding binding;
    private ApiServiceInterface mApiService;
    private User currentUser;

    public Filter filterRestaurant = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            results.values = mMapViewModel.filterRestaurant(constraint.toString());
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mMapViewModel.liveRestaurantsCall.getValue();
        }
    };
    public Filter filterUser = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            results.values = mApiService.filterUser(constraint.toString());
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mApiService.getLiveUsers();
        }
    };
    private MainActivity2PagerAdapter mMainActivity2PagerAdapter;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Restaurant chosenRestaurant;
    private android.widget.SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = User.firebaseUserToUser(userManager.getCurrentUser());
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        mApiService = DI.getASIService();
        setContentView(binding.getRoot());
        showSnackBar(getString(R.string.connection_succeed));
        mViewPager = findViewById(R.id.activity_main_viewpager);
        mTabLayout = findViewById(R.id.tabs);
        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();
        onDrawerOpened(mDrawerLayout);
        configurePagerAdapter();
        mApiService.populateUser();
    }

    public void configureToolBar() {
        this.mToolbar = findViewById(R.id.includeToolbar);
        mToolbar.setTitle(R.string.i_m_hungry);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
    }

    private void configureDrawerLayout() {
        this.mDrawerLayout = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            mApiService.getSort().setValue(RestaurantListView.ALPHABETICAL);
        } else if (id == R.id.filterDistance) {
            mApiService.getSort().setValue(RestaurantListView.DISTANCE);
        }
        else if (id == R.id.filterNote) {
            mApiService.getSort().setValue(RestaurantListView.NOTE);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.activity_main_drawer_dining:
                userManager.getUserData().addOnSuccessListener(user -> {
                    chosenRestaurant = user.getChosenRestaurant();
                    if (chosenRestaurant != null) {
                        Intent intent = new Intent(MainActivity2.this, RestaurantDetail.class);
                        intent.putExtra(RestaurantDetail.KEY_RESTAURANT, chosenRestaurant);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity2.this, getResources().getString(R.string.noLunch), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {//Log.e("tag", "fail" + e.getMessage())
                    });
                break;

            case R.id.activity_main_drawer_settings:
                Intent settingsIntent = new Intent(MainActivity2.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;

            case R.id.activity_main_drawer_logout:
                logout();
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout(){
        Intent finish = new Intent(this, MainActivity.class);
        userManager.signOut(this).addOnSuccessListener(Void -> {
            startActivity(finish);
        });
    }

    public void configureNavigationView() {
        this.mNavigationView = binding.activityMainNavView;
        mNavigationView.setNavigationItemSelectedListener(this::onNavigationSelected);
    }

    private void configurePagerAdapter() {
        mMainActivity2PagerAdapter = new MainActivity2PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMainActivity2PagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.setOffscreenPageLimit(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtre, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (android.widget.SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getRestaurantFilter().filter(newText);
                getUserFilter().filter(newText);
                return false;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                searchView.setQueryHint(searchTip);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return true;
    }

    public Filter getRestaurantFilter() {
        return filterRestaurant;
    }

    public Filter getUserFilter() {
        return filterUser;
    }

    public void onDrawerOpened(View drawerView) {
        NavigationView navigationVIew = findViewById(R.id.activity_main_nav_view);
        View headerView = navigationVIew.getHeaderView(0);
        Menu menu = navigationVIew.getMenu();
        TextView mail = headerView.findViewById(R.id.nav_header_mail);
        TextView name = headerView.findViewById(R.id.nav_header_name);
        ImageView avatar = headerView.findViewById(R.id.nav_header_avatar);
        mail.setText(currentUser.getMail());
        name.setText(currentUser.getName());
        Glide.with(avatar.getContext())
                .load(currentUser.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .into(avatar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        toolbarTitle = getResources().getString(R.string.i_m_hungry);
                        searchTip = getResources().getString(R.string.searchRestaurant);
                    case 1:
                        toolbarTitle = getResources().getString(R.string.i_m_hungry);
                        searchTip = getResources().getString(R.string.searchRestaurant);
                        break;
                    case 2:
                        toolbarTitle = getResources().getString(R.string.availableWorkmate);
                        searchTip = getResources().getString(R.string.searchWorkmates);;
                        break;
                }
                mToolbar.setTitle(toolbarTitle);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }
}