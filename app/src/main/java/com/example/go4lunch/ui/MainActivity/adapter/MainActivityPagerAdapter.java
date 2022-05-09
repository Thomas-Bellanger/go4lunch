package com.example.go4lunch.ui.MainActivity.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.go4lunch.ui.MainActivity.fragment.Co_Worker_Fragment;
import com.example.go4lunch.ui.MainActivity.fragment.MapFragment;
import com.example.go4lunch.ui.MainActivity.fragment.RestaurantListView;

public class MainActivityPagerAdapter extends FragmentPagerAdapter {
    Fragment mFragment;

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //fragment to show
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mFragment = MapFragment.newInstance();
                break;
            case 1:
                mFragment = RestaurantListView.newInstance();
                break;
            case 2:
                mFragment = Co_Worker_Fragment.newInstance();
                break;
            default:
                return null;
        }
        return mFragment;
    }

    //maximum number of page
    @Override
    public int getCount() {
        return 3;
    }
}
