package com.example.watering.investrecord;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
class FragSub2TabPagerAdapter extends FragmentPagerAdapter {

    private final Fragment5 fragment5;
    private final Fragment6 fragment6;
    private final Fragment7 fragment7;

    public FragSub2TabPagerAdapter(FragmentManager fm) {
        super(fm);
        fragment5 = new Fragment5();
        fragment6 = new Fragment6();
        fragment7 = new Fragment7();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragment5;
            case 1:
                return fragment6;
            case 2:
                return fragment7;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }
}
