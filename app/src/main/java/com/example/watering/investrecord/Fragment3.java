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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by watering on 17. 10. 21.
 */

public class Fragment3 extends Fragment {

    private View mView;
    private MainActivity mActivity;
    private IRResolver ir;
    private Spinner mAccountSpinner;
    private DatePicker date;
    private EditText mTxtInput;
    private EditText mTxtOutput;
    private EditText mTxtEvaluation;

    private List<String> accountlists = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private ArrayAdapter<String> accountAdapter;

    private String selectedDate;

    public Fragment3() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment3, container, false);
        mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        initLayout();
        initAccountSpinner();

        return mView;
    }

    private void initLayout() {
        mTxtInput = (EditText) mView.findViewById(R.id.editText_input);
        mTxtOutput = (EditText) mView.findViewById(R.id.editText_output);
        mTxtEvaluation = (EditText) mView.findViewById(R.id.editText_evaluation);

        mView.findViewById(R.id.button_regist_frag3).setOnClickListener(mClickListener);
        mView.findViewById(R.id.button_edit_frag3).setOnClickListener(mClickListener);
        mView.findViewById(R.id.button_delete_frag3).setOnClickListener(mClickListener);

        date = (DatePicker) mView.findViewById(R.id.date);
        selectedDate = String.format("%d/%d/%d",date.getYear(),date.getMonth(),date.getDayOfMonth());
        date.init(date.getYear(), date.getMonth(), date.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate = String.format("%d/%d/%d",year,monthOfYear+1,dayOfMonth);
            }
        });
    }

    private void initAccountSpinner() {
        updateAccountList();

        accountAdapter = new ArrayAdapter<String>(mActivity,R.layout.support_simple_spinner_dropdown_item,accountlists);
        mAccountSpinner = (Spinner) mView.findViewById(R.id.spinner_account_frag3);
        if(accountlists.size() != 0) mAccountSpinner.setAdapter(accountAdapter);
        mAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ir.setCurrentAccount(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void updateAccountList() {
        accountlists.clear();
        accounts = ir.getAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            accountlists.add(accounts.get(i).getNumber());
        }
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.button_regist_frag3:
                    int input = Integer.parseInt(mTxtInput.getText().toString());
                    int output= Integer.parseInt(mTxtOutput.getText().toString());
                    int evaluation = Integer.parseInt(mTxtEvaluation.getText().toString());
                    ir.addInfoIO(selectedDate,input,output,evaluation);
                    break;
                case R.id.button_edit_frag3:
                    break;
                case R.id.button_delete_frag3:
                    break;
            }
        }
    };
}