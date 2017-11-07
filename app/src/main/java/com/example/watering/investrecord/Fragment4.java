package com.example.watering.investrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by watering on 17. 10. 21.
 */

public class Fragment4 extends Fragment {

    private Spinner mAccountSpinner;
    private EditText mTextAccount;
    private EditText mTextInstitute;
    private EditText mTextDiscription;
    private Button mBtnChange;

    private List<String> accountlists = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private ArrayAdapter<String> accountAdapter;


    public Fragment4() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initLayout();
        initAccountSpinner();

        return inflater.inflate(R.layout.fragment4, container, false);
    }

    private void initLayout() {
    }

    private void initAccountSpinner() {
        updateAccountList();

        accountAdapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,accountlists);
        mAccountSpinner = (Spinner) getActivity().findViewById(R.id.spinner_group);
        if(accountlists.size() != 0) mAccountSpinner.setAdapter(accountAdapter);
        mAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateAccountList() {
        accountlists.clear();
        accounts = ((MainActivity)getActivity()).dbManager.getAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            accountlists.add(accounts.get(i).getNumber());
        }
    }
}
