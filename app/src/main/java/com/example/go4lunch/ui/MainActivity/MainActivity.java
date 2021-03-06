package com.example.go4lunch.ui.MainActivity;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.DI.DI;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMain2Binding;
import com.example.go4lunch.domain.manager.UserManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.example.go4lunch.domain.service.ApiServiceInterface;
import com.example.go4lunch.ui.MainActivity.adapter.MainActivityPagerAdapter;
import com.example.go4lunch.ui.MainActivity.fragment.RestaurantListView;
import com.example.go4lunch.ui.MainActivity.viewModel.MapViewModel;
import com.example.go4lunch.ui.RestaurantDetail.RestaurantDetail;
import com.example.go4lunch.ui.SettingsActivity.SettingsActivity;
import com.example.go4lunch.ui.SignInActivity.SignInActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    public static String toolbarTitle = "title";
    public static String searchTip = "tip";
    private final UserManager userManager = DI.getUserManager();
    private final MapViewModel mMapViewModel = MapViewModel.getInstance();
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ActivityMain2Binding binding;
    private ApiServiceInterface mApiService;
    private User currentUser;
    private MainActivityPagerAdapter mMainActivityPagerAdapter;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Restaurant chosenRestaurant;
    private android.widget.SearchView searchView;
    //filter for restaurant
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
    //filter for users
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = User.firebaseUserToUser(userManager.getCurrentUser());
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        mApiService = DI.getAPIService();
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
    //toolbar
    public void configureToolBar() {
        this.mToolbar = findViewById(R.id.includeToolbar);
        mToolbar.setTitle(R.string.i_m_hungry);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
    }
    //drawer layout
    private void configureDrawerLayout() {
        this.mDrawerLayout = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    //sort the restaurant list by alphabetical, distance or note
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            mApiService.getSort().setValue(RestaurantListView.ALPHABETICAL);
        } else if (id == R.id.filterDistance) {
            mApiService.getSort().setValue(RestaurantListView.DISTANCE);
        } else if (id == R.id.filterNote) {
            mApiService.getSort().setValue(RestaurantListView.NOTE);
        }

        return super.onOptionsItemSelected(item);
    }
    //menu
    public boolean onNavigationSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.activity_main_drawer_dining:
                userManager.getUserData().addOnSuccessListener(user -> {
                    chosenRestaurant = user.getChosenRestaurant();
                    if (chosenRestaurant != null) {
                        Intent intent = new Intent(MainActivity.this, RestaurantDetail.class);
                        intent.putExtra(RestaurantDetail.KEY_RESTAURANT, chosenRestaurant);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.noLunch), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {//Log.e("tag", "fail" + e.getMessage())
                });
                break;

            case R.id.activity_main_drawer_settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;

            case R.id.activity_main_drawer_logout:
                logout();
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //logout
    private void logout() {
        Intent finish = new Intent(this, SignInActivity.class);
        userManager.signOut(this).addOnSuccessListener(Void -> {
            startActivity(finish);
        });
    }
    //navigation view for fragments
    public void configureNavigationView() {
        this.mNavigationView = binding.activityMainNavView;
        mNavigationView.setNavigationItemSelectedListener(this::onNavigationSelected);
    }

    private void configurePagerAdapter() {
        mMainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMainActivityPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.setOffscreenPageLimit(2);
    }
    //filter with name (users or restaurant)
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
        //change the hint for filter with fragment
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

    //menu configuration
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
        //toolbar title and searchtip with fragment
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
                        searchTip = getResources().getString(R.string.searchWorkmates);
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
    //snackbar
    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }
}