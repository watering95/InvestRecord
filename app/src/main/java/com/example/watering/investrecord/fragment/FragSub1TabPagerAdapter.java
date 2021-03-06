package com.example.watering.investrecord.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
class FragSub1TabPagerAdapter extends FragmentPagerAdapter {

    private final Fragment1 fragment1;
    private final Fragment2 fragment2;
    private final Fragment3 fragment3;

    FragSub1TabPagerAdapter(FragmentManager fm) {
        super(fm);
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragment1;
            case 1:
                return fragment2;
            case 2:
                return fragment3;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }
}
