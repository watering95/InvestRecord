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
    MainActivity.Callback callbackfromMain;

    private String selectedDate;

    public Fragment3() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment3, container, false);
        mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        initLayout();
        initAccountSpinner();

        callbackfromMain = new MainActivity.Callback() {
            @Override
            public void updateList() {
                updateAccountList();

                if(accountlists.size() != 0) mAccountSpinner.setAdapter(accountAdapter);

                mTxtInput.setText("");
                mTxtOutput.setText("");
                mTxtEvaluation.setText("");
            }
        };
        mActivity.setCallback3(callbackfromMain);

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
        selectedDate = String.format("%04d-%02d-%02d",date.getYear(),date.getMonth(),date.getDayOfMonth());
        date.init(date.getYear(), date.getMonth(), date.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate = String.format("%04d-%02d-%02d",year,monthOfYear+1,dayOfMonth);
                showSelectedDateInfo();
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
                Account account;
                if(!accounts.isEmpty()) {
                    account = accounts.get(position);
                    ir.setCurrentAccount(account.getId());
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

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            int input = 0, output = 0, evaluation = 0;
            String in = mTxtInput.getText().toString();
            String out = mTxtOutput.getText().toString();
            String eval = mTxtEvaluation.getText().toString();

            if(!in.isEmpty()) input = Integer.parseInt(in);
            else input = 0;
            if(!out.isEmpty()) output = Integer.parseInt(out);
            else output = 0;
            if(!eval.isEmpty()) evaluation = Integer.parseInt(eval);
            else evaluation = 0;

            switch(v.getId()) {
                case R.id.button_regist_frag3:
                    ir.insertInfoIO(selectedDate,input,output,evaluation);
                    calInfoDairy(0,evaluation);
                    showSelectedDateInfo();
                    mActivity.Callback3to2();
                    break;
                case R.id.button_edit_frag3:
                    ir.updateInfoIO(selectedDate,input,output,evaluation);
                    calInfoDairy(1,evaluation);
                    showSelectedDateInfo();
                    mActivity.Callback3to2();
                    break;
                case R.id.button_delete_frag3:
                    break;
            }
        }
    };

    private void calInfoDairy(int select, int evaluation) {
        int sum_in, sum_out, principal;
        double rate = 0;

        sum_in = ir.getSum(new String[]{"input"},selectedDate);
        sum_out = ir.getSum(new String[]{"output"},selectedDate);
        principal = sum_in - sum_out;
        if(principal !=0 && evaluation !=0) rate = (double)evaluation / (double)principal * 100 - 100;

        switch(select) {
            case 0:
                ir.insertInfoDairy(selectedDate,principal,rate);
                break;
            case 1:
                ir.updateInfoDairy(selectedDate,principal,rate);
                break;
        }
    }
    private void showSelectedDateInfo() {
        Info_IO io;

        io = ir.getInfoIO(String.valueOf(ir.getCurrentAccount()),selectedDate);
        if(io == null || accountlists.get(0) == "Empty") {
            mTxtInput.setText("");
            mTxtOutput.setText("");
            mTxtEvaluation.setText("");
        }
        else {
            mTxtInput.setText(String.valueOf(io.getInput()));
            mTxtOutput.setText(String.valueOf(io.getOutput()));
            mTxtEvaluation.setText(String.valueOf(io.getEvaluation()));
        }
    }
}