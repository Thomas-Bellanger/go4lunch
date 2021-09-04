package com.example.go4lunch.ui.MainActivity2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainActivity2PagerAdapter extends FragmentPagerAdapter {

    public MainActivity2PagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return Co_Worker_Fragment.newInstance();
            case 1:
                return RestaurantListView.newInstance();
            case 2:
                return Co_Worker_Fragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
