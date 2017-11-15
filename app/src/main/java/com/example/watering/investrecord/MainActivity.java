package com.example.watering.investrecord;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Spinner mGroupSpinner;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MainTabPagerAdapter mPagerAdapter;
    private ArrayAdapter<String> spinnerAdapter;
    private List<String> grouplists = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    public IRResolver ir = new IRResolver();

    interface Callback {
        void updateList();
    }

    private boolean m_condition = true;
    private Callback m_callback3,m_callback4;

    public void setCallback3(Callback callback) {
        this.m_callback3 = callback;
    }
    public void setCallback4(Callback callback) {
        this.m_callback4 = callback;
    }
    public void Callback4to3() {
        if(m_condition && (m_callback3 != null)) {
            m_callback3.updateList();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        initDataBase();
        initGroupSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_addGroup:
                addGroupDialog();
                break;
            case R.id.menu_delGroup:
                delGroupDialog();
                break;
        }
        return true;
    }

    private void initLayout() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(mToolbar);

        mTabLayout = (TabLayout) findViewById(R.id.main_tab);
        mTabLayout.addTab(mTabLayout.newTab().setText("1"));
        mTabLayout.addTab(mTabLayout.newTab().setText("2"));
        mTabLayout.addTab(mTabLayout.newTab().setText("3"));
        mTabLayout.addTab(mTabLayout.newTab().setText("4"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mPagerAdapter = new MainTabPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

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
        ir.getContentResolver(getContentResolver());

        if(ir.initGroup() < 0) {
            addGroupDialog();
        }
        if(ir.initAccount() < 0) {

        }

        ir.setCurrentGroup(0);
    }

    private void addGroupDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(0, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                ir.addGroup(name);
                updateGroupList();
            }
        });
        dialog.show(getFragmentManager(), "dialog");
    }

    private void delGroupDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(1, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                ir.removeGroup(new String[]{name});
                updateGroupList();
            }
        });
        dialog.initData(ir.getGroups());
        dialog.show(getFragmentManager(), "dialog");
    }

    private void initGroupSpinner() {
        updateGroupList();

        spinnerAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,grouplists);

        mGroupSpinner = (Spinner) findViewById(R.id.spinner_group);
        mGroupSpinner.setAdapter(spinnerAdapter);
        mGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ir.setCurrentGroup(position);
                if(m_condition && (m_callback3 != null)) {
                    m_callback3.updateList();
                }
                if(m_condition && (m_callback4 != null)) {
                    m_callback4.updateList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateGroupList() {
        grouplists.clear();
        groups = ir.getGroups();
        for(int i=0; i<groups.size(); i++) {
            grouplists.add(groups.get(i).getName());
        }
    }
}