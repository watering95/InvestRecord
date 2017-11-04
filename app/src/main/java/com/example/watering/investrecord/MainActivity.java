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
    private Spinner mSpinner;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MainTabPagerAdapter mPagerAdapter;
    private List<String> spinnerlists = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        initDataBase();
        initSpinner();
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

        dbManager = new DBManager();
        dbManager.getContentResolver(getContentResolver());
        if(dbManager.initGroup() < 0) {
            addGroupDialog();
        }
    }

    private void addGroupDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(0, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                dbManager.addGroup(name);
                updateSpinner();
            }
        });
        dialog.show(getFragmentManager(), "dialog");
    }

    private void delGroupDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(1, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                dbManager.removeGroup(new String[]{name});
                updateSpinner();
            }
        });
        dialog.initData(dbManager.getGroup());
        dialog.show(getFragmentManager(), "dialog");
    }

    private void initSpinner() {
        updateSpinner();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,spinnerlists);
        mSpinner = (Spinner) findViewById(R.id.spinner_group);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateSpinner() {
        spinnerlists.clear();
        groups = dbManager.getGroup();
        for(int i=0; i<groups.size(); i++) {
            spinnerlists.add(groups.get(i).getName());
        }
    }
}