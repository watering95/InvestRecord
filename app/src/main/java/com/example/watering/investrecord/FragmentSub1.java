package com.example.watering.investrecord;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class FragmentSub1 extends Fragment {

    private ViewPager mFragSub1ViewPager;

    private View mView;
    private static final String TAG = "InvestRecord";

    interface Callback {
        void update();
    }

    private Callback m_callback1, m_callback2;

    public FragmentSub1() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        IRResolver ir = mActivity.ir;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sub1, container, false);

        initLayout();

        return mView;
    }

    public void setCallback1(FragmentSub1.Callback callback) {
        this.m_callback1 = callback;
    }
    public void setCallback2(FragmentSub1.Callback callback) {
        this.m_callback2 = callback;
    }

    public void callUpdateFrag1() {
        if(m_callback1 != null) {
            m_callback1.update();
        }
    }
    public void callUpdateFrag2() {
        if(m_callback2 != null) {
            m_callback2.update();
        }
    }

    private void initLayout() {
        TabLayout mFragSub1TabLayout = mView.findViewById(R.id.tab_frag_sub1);

        mFragSub1TabLayout.setTabTextColors(Color.parseColor("#ffffff"),Color.parseColor("#00ff00"));
        mFragSub1TabLayout.addTab(mFragSub1TabLayout.newTab().setText(R.string.total_review));
        mFragSub1TabLayout.addTab(mFragSub1TabLayout.newTab().setText(R.string.analysis));
        mFragSub1TabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mFragSub1ViewPager = mView.findViewById(R.id.viewpager_frag_sub1);
        FragSub1TabPagerAdapter mFragSub1PagerAdapter = new FragSub1TabPagerAdapter(getChildFragmentManager());
        mFragSub1ViewPager.setAdapter(mFragSub1PagerAdapter);
        mFragSub1ViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mFragSub1TabLayout));

        mFragSub1TabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mFragSub1ViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
