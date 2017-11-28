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
    private Spinner mAccountSpinner;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MainTabPagerAdapter mPagerAdapter;
    private ArrayAdapter<String> groupAdapter;
    private ArrayAdapter<String> accountAdapter;
    private List<String> grouplists = new ArrayList<>();
    private List<String> accountlists = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    public IRResolver ir = new IRResolver();

    interface Callback {
        void updateList();
    }

    private boolean m_condition = true;
    private Callback m_callback1,m_callback2,m_callback3,m_callback4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        initGroupSpinner();
        initAccountSpinner();
        initDataBase();
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
            case R.id.menu_editGroup:
                editGroupDialog();
                break;
            case R.id.menu_delGroup:
                delGroupDialog();
                break;
            case R.id.menu_setting:
                settingDialog();
                break;
        }
        return true;
    }

    public void setCallback1(Callback callback) {
        this.m_callback1 = callback;
    }
    public void setCallback2(Callback callback) {
        this.m_callback2 = callback;
    }
    public void setCallback3(Callback callback) {
        this.m_callback3 = callback;
    }
    public void setCallback4(Callback callback) {
        this.m_callback4 = callback;
    }

    public void CallUpdate1() {
        if(m_condition && (m_callback1 != null)) {
            m_callback1.updateList();
        }
    }
    public void CallUpdate2() {
        if(m_condition && (m_callback2 != null)) {
            m_callback2.updateList();
        }
    }
    public void CallUpdate3() {
        if(m_condition && (m_callback3 != null)) {
            m_callback3.updateList();
        }
    }
    public void CallUpdate4() {
        if(m_condition && (m_callback4 != null)) {
            m_callback4.updateList();
        }
    }

    private void initLayout() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(mToolbar);

        mTabLayout = (TabLayout) findViewById(R.id.main_tab);
        mTabLayout.setTabTextColors(Color.parseColor("#ffffff"),Color.parseColor("#00ff00"));
        mTabLayout.addTab(mTabLayout.newTab().setText("통합자산"));
        mTabLayout.addTab(mTabLayout.newTab().setText("계좌별이력"));
        mTabLayout.addTab(mTabLayout.newTab().setText("입출금입력"));
        mTabLayout.addTab(mTabLayout.newTab().setText("계좌관리"));
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
        updateGroupSpinner();
        updateAccountSpinner();
    }
    private void initGroupSpinner() {
        groupAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,grouplists);

        mGroupSpinner = (Spinner) findViewById(R.id.spinner_group);
        mGroupSpinner.setAdapter(groupAdapter);
        mGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(groups.size() != 0 ) {
                    ir.setCurrentGroup(groups.get(position).getId());
                }
                updateAccountSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initAccountSpinner() {
        accountAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,accountlists);
        mAccountSpinner = (Spinner) findViewById(R.id.spinner_account);
        mAccountSpinner.setAdapter(accountAdapter);
        mAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ir.setCurrentAccount(accounts.get(position).getId());

                CallUpdate1();
                CallUpdate2();
                CallUpdate3();
                CallUpdate4();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void inoutDialog(String selectedDate) {
        UserDialogFragment dialog = UserDialogFragment.newInstance(4, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {

            }

            @Override
            public void onDeleteAll() {

            }
        });

        dialog.setSelectedDate(selectedDate);
        dialog.show(getFragmentManager(), "dialog");
    }
    private void addGroupDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(0, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                if(!name.isEmpty()) ir.insertGroup(name);
                updateGroupSpinner();
            }

            @Override
            public void onDeleteAll() {

            }
        });
        dialog.show(getFragmentManager(), "dialog");
    }
    private void editGroupDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(1, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                if(!name.isEmpty()) ir.updateGroup(name);
                updateGroupSpinner();
            }

            @Override
            public void onDeleteAll() {

            }

        });
        dialog.initData(ir.getGroups());
        dialog.show(getFragmentManager(), "dialog");
    }
    private void delGroupDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(2, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                ir.deleteGroup("name",new String[] {name});
                updateGroupSpinner();
            }

            @Override
            public void onDeleteAll() {

            }
        });
        dialog.initData(ir.getGroups());
        dialog.show(getFragmentManager(), "dialog");
    }
    private void settingDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(3, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {

            }

            @Override
            public void onDeleteAll() {
                ir.deleteAll();
                updateGroupSpinner();
                updateAccountSpinner();
            }
        });

        dialog.show(getFragmentManager(), "dialog");
    }

    private void updateGroupList() {
        grouplists.clear();
        groups = ir.getGroups();

        if(groups.isEmpty()) return;

        for(int i = 0; i < groups.size(); i++) {
            grouplists.add(groups.get(i).getName());
        }
    }
    public void updateAccountList() {
        accountlists.clear();
        accounts = ir.getAccounts();

        if(accounts.isEmpty()) return;

        for (int i = 0; i < accounts.size(); i++) {
            accountlists.add(accounts.get(i).getNumber());
        }
    }

    public void updateGroupSpinner() {
        updateGroupList();
        groupAdapter.notifyDataSetChanged();
        if(groups.isEmpty()) ir.setCurrentGroup(0);
        else ir.setCurrentGroup(groups.get(0).getId());
    }
    public void updateAccountSpinner() {
        updateAccountList();
        accountAdapter.notifyDataSetChanged();
        if(accounts.isEmpty()) ir.setCurrentAccount(0);
        else ir.setCurrentAccount(accounts.get(0).getId());

        CallUpdate1();
        CallUpdate2();
        CallUpdate3();
        CallUpdate4();
    }
}