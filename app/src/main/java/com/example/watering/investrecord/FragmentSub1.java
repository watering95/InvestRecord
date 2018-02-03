package com.example.watering.investrecord;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class FragmentSub1 extends Fragment {

    private ViewPager mFragSub1ViewPager;
    private MainActivity mActivity;

    private View mView;
    private IRResolver ir;
    private static final String TAG = "InvestRecord";

    private ArrayAdapter<String> groupAdapter;
    private final List<String> grouplists = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();

    interface Callback {
        void updateList();
    }

    private Callback m_callback1, m_callback2;

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
        initGroupSpinner();
        initDataBase();

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_toolbar_sub1,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCallback1(FragmentSub1.Callback callback) {
        this.m_callback1 = callback;
    }
    public void CallUpdate1() {
        if(m_callback1 != null) {
            m_callback1.updateList();
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

    private void initDataBase() {
        //noinspection ConstantConditions
        ir.getContentResolver(getActivity().getContentResolver());
        updateGroupSpinner();
     }

    private void initGroupSpinner() {
        //noinspection ConstantConditions
        groupAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, grouplists);

        Spinner mGroupSpinner = mView.findViewById(R.id.spinner_frag_sub1_group);
        mGroupSpinner.setAdapter(groupAdapter);
        mGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(groups.size() != 0 ) {
                    ir.setCurrentGroup(groups.get(position).getId());
                }
                mActivity.fragmentSub1.CallUpdate1();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateGroupSpinner() {
        updateGroupList();
        groupAdapter.notifyDataSetChanged();
        if(groups.isEmpty()) ir.setCurrentGroup(-1);
        else ir.setCurrentGroup(groups.get(0).getId());
    }

    private void updateGroupList() {
        grouplists.clear();
        groups = ir.getGroups();
        if(groups.isEmpty()) {
            Log.i(TAG, "No group");
            return;
        }

        for(int i = 0; i < groups.size(); i++) {
            grouplists.add(groups.get(i).getName());
        }
    }
}
