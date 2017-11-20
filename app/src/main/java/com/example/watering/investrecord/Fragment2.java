package com.example.watering.investrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by watering on 17. 10. 21.
 */

public class Fragment2 extends Fragment {

    private View mView;
    private ListView listView;
    private ListAdapter dairyAdapter;
    private MainActivity mActivity;
    private IRResolver ir;
    private Spinner mAccountSpinner;

    private List<String> accountlists = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private ArrayList<Info_Dairy> dairylists = new ArrayList<>();
    private ArrayAdapter<String> accountAdapter;
    MainActivity.Callback callbackfromMain;

    public Fragment2() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment2, container, false);
        mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        initAccountSpinner();
        updateInfoDairyList();
        initLayout();

        callbackfromMain = new MainActivity.Callback() {
            @Override
            public void updateList() {
                updateAccountList();
                if(accountlists.size() != 0) mAccountSpinner.setAdapter(accountAdapter);

                updateInfoDairyList();
                dairyAdapter.notifyDataSetChanged();
            }
        };
        mActivity.setCallback2(callbackfromMain);

        return mView;
    }

    private void initLayout() {
        listView = (ListView)mView.findViewById(R.id.listview_totalasset_frag2);
        dairyAdapter = new ListAdapter(mView.getContext(),dairylists);
        if(dairylists.size() != 0) listView.setAdapter(dairyAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void initAccountSpinner() {
        updateAccountList();

        accountAdapter = new ArrayAdapter<String>(mActivity,R.layout.support_simple_spinner_dropdown_item,accountlists);
        mAccountSpinner = (Spinner) mView.findViewById(R.id.spinner_account_frag2);
        if(accountlists.size() != 0) mAccountSpinner.setAdapter(accountAdapter);
        mAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Account account;
                if(!accounts.isEmpty()) {
                    account = accounts.get(position);
                    ir.setCurrentAccount(account.getId());
                    updateInfoDairyList();
                    dairyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateAccountList() {

        accountlists.clear();
        accounts = ir.getAccounts();

        if(accounts.isEmpty()) {
            accountlists.add("Empty");
            ir.setCurrentAccount(0);
            return;
        }

        for (int i = 0; i < accounts.size(); i++) {
            accountlists.add(accounts.get(i).getNumber());
        }

        ir.setCurrentAccount(accounts.get(0).getId());
    }

    private void updateInfoDairyList() {
        dairylists.clear();
        dairylists = (ArrayList) ir.getInfoDaires();
    }
}
