package com.example.watering.investrecord;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("ALL")
class MainTabPagerAdapter extends FragmentPagerAdapter {

    FragmentMain fragmentMain;
    FragmentSub fragmentSub;

    public MainTabPagerAdapter(FragmentManager fm) {
        super(fm);

        fragmentMain = new FragmentMain();
        fragmentSub = new FragmentSub();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragmentMain;
            case 1:
                return fragmentSub;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 2;
    }
}
