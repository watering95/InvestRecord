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

@SuppressWarnings({"ALL"})
public class FragmentSub1 extends Fragment {

    private Fragment1 fragment1 = new Fragment1();
    private Fragment2 fragment2 = new Fragment2();
    private Fragment3 fragment3 = new Fragment3();
    private Fragment4 fragment4 = new Fragment4();

    private View mView;
    private IRResolver ir;
    private ViewPager mFragSub1ViewPager;

    private ArrayAdapter<String> groupAdapter;
    private ArrayAdapter<String> accountAdapter;
    private final List<String> grouplists = new ArrayList<>();
    private final List<String> accountlists = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();

    interface Callback {
        void updateList();
    }

    private Callback m_callback1,m_callback2,m_callback4;

    public FragmentSub1() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ir = ((MainActivity) getActivity()).ir;

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sub1, container, false);

        initLayout();
        initGroupSpinner();
        initAccountSpinner();
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
        switch (item.getItemId()) {
            case R.id.menu_sub1_addGroup:
                addGroupDialog();
                break;
            case R.id.menu_sub1_editGroup:
                editGroupDialog();
                break;
            case R.id.menu_sub1_delGroup:
                delGroupDialog();
                break;
        }
        return true;
    }

    private void initLayout() {
        TabLayout mFragSub1TabLayout = mView.findViewById(R.id.frag_sub1_tab);

        mFragSub1TabLayout.setTabTextColors(Color.parseColor("#ffffff"),Color.parseColor("#00ff00"));
        mFragSub1TabLayout.addTab(mFragSub1TabLayout.newTab().setText("통합자산"));
        mFragSub1TabLayout.addTab(mFragSub1TabLayout.newTab().setText("계좌별이력"));
        mFragSub1TabLayout.addTab(mFragSub1TabLayout.newTab().setText("입출금입력"));
        mFragSub1TabLayout.addTab(mFragSub1TabLayout.newTab().setText("계좌관리"));
        mFragSub1TabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mFragSub1ViewPager = mView.findViewById(R.id.frag_sub1_viewpager);
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

        fragment1 = (Fragment1) mFragSub1PagerAdapter.getItem(0);
        fragment2 = (Fragment2) mFragSub1PagerAdapter.getItem(1);
        fragment3 = (Fragment3) mFragSub1PagerAdapter.getItem(2);
        fragment4 = (Fragment4) mFragSub1PagerAdapter.getItem(3);
    }

    public void setCallback1(Callback callback) {
        this.m_callback1 = callback;
    }
    public void setCallback2(Callback callback) {
        this.m_callback2 = callback;
    }
    public void setCallback4(Callback callback) {
        this.m_callback4 = callback;
    }

    public void CallUpdate1() {
        if(m_callback1 != null) {
            m_callback1.updateList();
        }
    }
    public void CallUpdate2() {
        if(m_callback2 != null) {
            m_callback2.updateList();
        }
    }
    public void CallUpdate4() {
        if(m_callback4 != null) {
            m_callback4.updateList();
        }
    }

    public void initDataBase() {
        ir.getContentResolver(getActivity().getContentResolver());
        updateGroupSpinner();
        updateAccountSpinner();
    }
    private void initGroupSpinner() {
        groupAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, grouplists);

        Spinner mGroupSpinner = mView.findViewById(R.id.spinner_group);
        mGroupSpinner.setAdapter(groupAdapter);
        mGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(groups.size() != 0 ) {
                    ir.setCurrentGroup(groups.get(position).getId());
                }
                updateAccountSpinner();
                CallUpdate1();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initAccountSpinner() {
        accountAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, accountlists);
        Spinner mAccountSpinner = mView.findViewById(R.id.spinner_account);
        mAccountSpinner.setAdapter(accountAdapter);
        mAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ir.setCurrentAccount(accounts.get(position).getId());
                CallUpdate1();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateGroupList() {
        grouplists.clear();
        groups = ir.getGroups();

        if(groups.isEmpty()) return;

        for(int i = 0; i < groups.size(); i++) {
            grouplists.add(groups.get(i).getName());
        }
    }
    private void updateAccountList() {
        String str;
        Account account;

        accountlists.clear();
        accounts = ir.getAccounts();

        if(accounts.isEmpty()) return;

        for (int i = 0; i < accounts.size(); i++) {
            account = accounts.get(i);
            str = account.getNumber() + " " + account.getInstitute() + " " + account.getDiscription();
            accountlists.add(str);
        }
    }
    public void updateGroupSpinner() {
        updateGroupList();
        groupAdapter.notifyDataSetChanged();
        if(groups.isEmpty()) ir.setCurrentGroup(-1);
        else ir.setCurrentGroup(groups.get(0).getId());
    }
    public void updateAccountSpinner() {
        updateAccountList();
        accountAdapter.notifyDataSetChanged();
        if(accounts.isEmpty()) ir.setCurrentAccount(-1);
        else ir.setCurrentAccount(accounts.get(0).getId());

        Spinner mAccountSpinner = mView.findViewById(R.id.spinner_account);
        mAccountSpinner.setSelection(0);
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
                if(!name.isEmpty()) ir.updateGroup(ir.getCurrentGroup(),name);
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
}
