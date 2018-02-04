package com.example.watering.investrecord;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
class FragSub2TabPagerAdapter extends FragmentPagerAdapter {

    private final Fragment3 fragment3;
    private final Fragment4 fragment4;
    private final Fragment5 fragment5;

    FragSub2TabPagerAdapter(FragmentManager fm) {
        super(fm);
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
        fragment5 = new Fragment5();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragment3;
            case 1:
                return fragment4;
            case 2:
                return fragment5;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }
}
