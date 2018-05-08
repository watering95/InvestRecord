package com.example.watering.investrecord.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
class FragSub3TabPagerAdapter extends FragmentPagerAdapter {

    private final Fragment6 fragment6;
    private final Fragment7 fragment7;
    private final Fragment8 fragment8;

    FragSub3TabPagerAdapter(FragmentManager fm) {
        super(fm);
        fragment6 = new Fragment6();
        fragment7 = new Fragment7();
        fragment8 = new Fragment8();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragment6;
            case 1:
                return fragment7;
            case 2:
                return fragment8;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }
}
