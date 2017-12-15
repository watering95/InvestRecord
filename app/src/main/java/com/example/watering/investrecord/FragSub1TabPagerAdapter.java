package com.example.watering.investrecord;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("ALL")
class FragSub1TabPagerAdapter extends FragmentPagerAdapter {

    public Fragment1 fragment1;
    public Fragment2 fragment2;
    public Fragment3 fragment3;
    public Fragment4 fragment4;

    public FragSub1TabPagerAdapter(FragmentManager fm) {
        super(fm);
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
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
            case 3:
                return fragment4;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 4;
    }
}
