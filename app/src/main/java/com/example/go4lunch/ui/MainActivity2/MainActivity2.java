package com.example.go4lunch.ui.MainActivity2;

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

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMain2Binding;
import com.example.go4lunch.manager.UserManager;
import com.example.go4lunch.ui.SettingsActivity.SettingsActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity2 extends AppCompatActivity {

    private final UserManager userManager = UserManager.getInstance();
    public Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    };
    ViewPager mViewPager;
    TabLayout mTabLayout;
    ActivityMain2Binding binding;
    private MainActivity2PagerAdapter mMainActivity2PagerAdapter;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewPager = findViewById(R.id.activity_main_viewpager);
        mTabLayout = findViewById(R.id.tabs);
        configureToolBar();
        configureDrawerLayout();
        configurePagerAdapter();
        configureNavigationView();
        onDrawerOpened(mDrawerLayout);
    }

    private void configureToolBar() {
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

    public boolean onNavigationSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.activity_main_drawer_dining:
                break;
            case R.id.activity_main_drawer_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);

            case R.id.activity_main_drawer_logout:
                userManager.signOut(this).addOnSuccessListener(Void -> finish());
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtre, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        android.widget.SearchView searchView = (android.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public Filter getFilter() {
        return filter;
    }

    public void onDrawerOpened(View drawerView) {
        NavigationView navigationVIew = findViewById(R.id.activity_main_nav_view);
        View headerView = navigationVIew.getHeaderView(0);
        Menu menu = navigationVIew.getMenu();
        TextView mail = headerView.findViewById(R.id.nav_header_mail);
        TextView name = headerView.findViewById(R.id.nav_header_name);
        ImageView avatar = headerView.findViewById(R.id.nav_header_avatar);
        mail.setText(userManager.getCurrentUser().getEmail());
        name.setText(userManager.getCurrentUser().getDisplayName());
        avatar.setImageURI(userManager.getCurrentUser().getPhotoUrl());
    }
}