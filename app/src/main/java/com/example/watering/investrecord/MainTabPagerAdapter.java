package com.example.watering.investrecord;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by watering on 17. 10. 21.
 */

public class MainTabPagerAdapter extends FragmentPagerAdapter {

    private static int PAGE_NUMBER = 4;
    private Context mContext;
    private Fragment1 mFrag1 = null;
    private Fragment2 mFrag2 = null;
    private Fragment3 mFrag3 = null;
    private Fragment4 mFrag4 = null;

    public MainTabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mFrag1 = Fragment1.newInstance();
                return mFrag1;
            case 1:
                mFrag2 = Fragment2.newInstance();
                return mFrag2;
            case 2:
                mFrag3 = Fragment3.newInstance();
                return mFrag3;
            case 3:
                mFrag4 = Fragment4.newInstance();
                return mFrag4;
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }
}
