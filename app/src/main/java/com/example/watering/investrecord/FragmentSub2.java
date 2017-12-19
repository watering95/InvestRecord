package com.example.watering.investrecord;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
public class FragmentSub2 extends Fragment {

    private Fragment5 fragment5 = new Fragment5();
    private Fragment6 fragment6 = new Fragment6();
    private Fragment7 fragment7 = new Fragment7();

    private ViewPager mFragSub2ViewPager;

    private View mView;
    private IRResolver ir;


    public FragmentSub2() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MainActivity mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sub2, container, false);

        initLayout();
        initDataBase();

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_toolbar_sub2,menu);
    }

    private void initLayout() {
        TabLayout mFragSub2TabLayout = mView.findViewById(R.id.frag_sub2_tab);

        mFragSub2TabLayout.setTabTextColors(Color.parseColor("#ffffff"),Color.parseColor("#00ff00"));
        mFragSub2TabLayout.addTab(mFragSub2TabLayout.newTab().setText(R.string.spending));
        mFragSub2TabLayout.addTab(mFragSub2TabLayout.newTab().setText(R.string.income));
        mFragSub2TabLayout.addTab(mFragSub2TabLayout.newTab().setText(R.string.statistic));
        mFragSub2TabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mFragSub2ViewPager = mView.findViewById(R.id.frag_sub2_viewpager);
        FragSub2TabPagerAdapter mFragSub2PagerAdapter = new FragSub2TabPagerAdapter(getChildFragmentManager());
        mFragSub2ViewPager.setAdapter(mFragSub2PagerAdapter);
        mFragSub2ViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mFragSub2TabLayout));

        mFragSub2TabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mFragSub2ViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fragment5 = (Fragment5) mFragSub2PagerAdapter.getItem(0);
        fragment6 = (Fragment6) mFragSub2PagerAdapter.getItem(1);
        fragment7 = (Fragment7) mFragSub2PagerAdapter.getItem(2);
    }

    private void initDataBase() {

    }
}
