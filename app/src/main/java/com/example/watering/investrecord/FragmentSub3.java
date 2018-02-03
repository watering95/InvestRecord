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
public class FragmentSub3 extends Fragment {

    private MainActivity mActivity;
    private ViewPager mFragSub3ViewPager;
    private View mView;
    private IRResolver ir;
    private Spinner mAccountSpinner;
    private static final String TAG = "InvestRecord";

    private ArrayAdapter<String> groupAdapter;
    private ArrayAdapter<String> accountAdapter;
    private final List<String> grouplists = new ArrayList<>();
    private final List<String> accountlists = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();

    interface Callback {
        void updateList();
    }

    private Callback m_callback6, m_callback8;

    public FragmentSub3() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();

        assert mActivity != null;
        ir = mActivity.ir;

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sub3, container, false);

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
        UserDialogFragment dialog = UserDialogFragment.newInstance(item.getItemId(), new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                updateGroupSpinner();
            }
        });
        //noinspection ConstantConditions
        dialog.show(getFragmentManager(), "dialog");
        return super.onOptionsItemSelected(item);
    }

    private void initLayout() {
        TabLayout mFragSub3TabLayout = mView.findViewById(R.id.frag_sub3_tab);

        mFragSub3TabLayout.setTabTextColors(Color.parseColor("#ffffff"),Color.parseColor("#00ff00"));
        mFragSub3TabLayout.addTab(mFragSub3TabLayout.newTab().setText("계좌별이력"));
        mFragSub3TabLayout.addTab(mFragSub3TabLayout.newTab().setText("입출금입력"));
        mFragSub3TabLayout.addTab(mFragSub3TabLayout.newTab().setText("계좌관리"));
        mFragSub3TabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mFragSub3ViewPager = mView.findViewById(R.id.viewpager_frag_sub3);
        FragSub3TabPagerAdapter mFragSub3PagerAdapter = new FragSub3TabPagerAdapter(getChildFragmentManager());
        mFragSub3ViewPager.setAdapter(mFragSub3PagerAdapter);
        mFragSub3ViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mFragSub3TabLayout));

        mFragSub3TabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mFragSub3ViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setCallback6(Callback callback) {
        this.m_callback6 = callback;
    }
    public void setCallback8(Callback callback) {
        this.m_callback8 = callback;
    }

    public void CallUpdate6() {
        if(m_callback6 != null) {
            m_callback6.updateList();
        }
    }
    public void CallUpdate8() {
        if(m_callback8 != null) {
            m_callback8.updateList();
        }
    }

    public void initDataBase() {
        //noinspection ConstantConditions
        ir.getContentResolver(getActivity().getContentResolver());
        updateGroupSpinner();
        updateAccountSpinner();
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

        Spinner mAccountSpinner = mView.findViewById(R.id.spinner_frag_sub3_account);
        mAccountSpinner.setSelection(0);
    }
    public void setPositionAccountSpinner(int position) {
        mAccountSpinner.setSelection(position);
    }

    private void initGroupSpinner() {
        //noinspection ConstantConditions
        groupAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, grouplists);

        Spinner mGroupSpinner = mView.findViewById(R.id.spinner_frag_sub3_group);
        mGroupSpinner.setAdapter(groupAdapter);
        mGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(groups.size() != 0 ) {
                    ir.setCurrentGroup(groups.get(position).getId());
                }
                updateAccountSpinner();
                mActivity.fragmentSub1.CallUpdate1();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initAccountSpinner() {
        //noinspection ConstantConditions
        accountAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, accountlists);
        mAccountSpinner = mView.findViewById(R.id.spinner_frag_sub3_account);
        mAccountSpinner.setAdapter(accountAdapter);
        mAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ir.setCurrentAccount(accounts.get(position).getId());
                mActivity.fragmentSub1.CallUpdate1();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
    private void updateAccountList() {
        String str;
        Account account;

        accountlists.clear();
        accounts = ir.getAccounts(ir.getCurrentGroup());
        if(accounts.isEmpty()) {
            Log.i(TAG, "No account");
            return;
        }

        for (int i = 0; i < accounts.size(); i++) {
            account = accounts.get(i);
            str = account.getNumber() + " " + account.getInstitute() + " " + account.getDescription();
            accountlists.add(str);
        }
    }

    private int findAccount(int id) {
        int result = -1;

        if(accounts.isEmpty()) return -1;

        for (int i = 0; i < accounts.size(); i++) {
            if(accounts.get(i).getId() == id) result = i;
        }
        return result;
    }
}