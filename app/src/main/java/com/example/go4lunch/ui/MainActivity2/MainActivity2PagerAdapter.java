package com.example.go4lunch.ui.MainActivity2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainActivity2PagerAdapter extends FragmentPagerAdapter {

    Fragment mFragment;

    public MainActivity2PagerAdapter(FragmentManager fm) {
        super(fm);
    }

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

    @Override
    public int getCount() {
        return 3;
    }
}
