package com.example.watering.investrecord.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.watering.investrecord.IRResolver;
import com.example.watering.investrecord.MainActivity;
import com.example.watering.investrecord.R;

import java.text.DecimalFormat;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class FragmentSub1 extends Fragment {

    private ViewPager mFragSub1ViewPager;

    private MainActivity mActivity;
    private IRResolver ir;

    private TextView textView_spend_month;
    private TextView textView_income_month;

    private View mView;
    private static final String TAG = "InvestRecord";

    interface Callback {
        void update();
    }

    private Callback m_callback1, m_callback2, m_callback3;

    public FragmentSub1() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        ir = mActivity.ir;
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
    public void setCallback3(FragmentSub1.Callback callback) {
        this.m_callback3 = callback;
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
    public void callUpdateFrag3() {
        if(m_callback3 != null) {
            m_callback3.update();
        }
    }

    private void initLayout() {
        TabLayout mFragSub1TabLayout = mView.findViewById(R.id.tab_frag_sub1);

        mFragSub1TabLayout.setTabTextColors(Color.parseColor("#ffffff"),Color.parseColor("#00ff00"));
        mFragSub1TabLayout.addTab(mFragSub1TabLayout.newTab().setText(R.string.total_review));
        mFragSub1TabLayout.addTab(mFragSub1TabLayout.newTab().setText(R.string.analysis));
        mFragSub1TabLayout.addTab(mFragSub1TabLayout.newTab().setText(R.string.reference));
        mFragSub1TabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        textView_spend_month = mView.findViewById(R.id.textView_frag_sub1_spend_month);
        textView_income_month = mView.findViewById(R.id.textView_frag_sub1_income_month);

        LinearLayout linearLayout = mView.findViewById(R.id.linearLayout_frag_sub1);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, mActivity.fragmentSub2).commit();
            }
        });

        updateFragSub1();

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

    public void updateFragSub1() {

        DecimalFormat df = new DecimalFormat("#,###");

        int spend_month = ir.getSpendMonth(mActivity.getToday());
        int income_month = ir.getIncomeMonth(mActivity.getToday());
        textView_income_month.setText(df.format(income_month));
        textView_spend_month.setText(df.format(spend_month));
    }
}
