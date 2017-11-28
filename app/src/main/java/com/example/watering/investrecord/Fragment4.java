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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by watering on 17. 10. 21.
 */

public class Fragment4 extends Fragment {

    private View mView;
    private MainActivity mActivity;
    private IRResolver ir;
    private EditText mTxtAccount;
    private EditText mTxtInstitute;
    private EditText mTxtDiscription;

    private MainActivity.Callback callbackfromMain;

    public Fragment4() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment4, container, false);
        mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        initLayout();

        callbackfromMain = new MainActivity.Callback() {
            @Override
            public void updateList() {
                int id = ir.getCurrentAccount();

                Account account = ir.getAccount(String.valueOf(id));

                if(account == null) {
                    mTxtAccount.setText("");
                    mTxtDiscription.setText("");
                    mTxtInstitute.setText("");
                }
                else {
                    mTxtAccount.setText(account.getNumber());
                    mTxtDiscription.setText(account.getDiscription());
                    mTxtInstitute.setText(account.getInstitute());
                    ir.setCurrentAccount(account.getId());
                }
            }
        };
        mActivity.setCallback4(callbackfromMain);

        return mView;
    }

    private void initLayout() {

        mTxtAccount = (EditText) mView.findViewById(R.id.editText_account_frag4);
        mTxtInstitute = (EditText) mView.findViewById(R.id.editText_institute);
        mTxtDiscription = (EditText) mView.findViewById(R.id.editText_discription);

        mView.findViewById(R.id.button_regist_frag4).setOnClickListener(mClickListener);
        mView.findViewById(R.id.button_delete_frag4).setOnClickListener(mClickListener);
        mView.findViewById(R.id.button_edit_frag4).setOnClickListener(mClickListener);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            String account = mTxtAccount.getText().toString();
            String institute = mTxtInstitute.getText().toString();
            String discript = mTxtDiscription.getText().toString();

            switch(v.getId()) {
                case R.id.button_regist_frag4:
                    if(!account.isEmpty()) ir.insertAccount(institute,account,discript);
                    break;
                case R.id.button_edit_frag4:
                    if(!account.isEmpty()) ir.updateAccount(institute,account,discript);
                    break;
                case R.id.button_delete_frag4:
                    if(!account.isEmpty()) ir.deleteAccount("num",new String[] {account});
                    break;
            }

            updateOtherFragment();
        }
    };

    private void updateOtherFragment() {
        mActivity.updateAccountSpinner();
        mActivity.CallUpdate3();
        mActivity.CallUpdate2();
        mActivity.CallUpdate1();
    }
}
