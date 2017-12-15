package com.example.watering.investrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
public class Fragment4 extends Fragment {

    private View mView;
    private MainActivity mActivity;
    private IRResolver ir;
    private EditText mTxtAccount;
    private EditText mTxtInstitute;
    private EditText mTxtDiscription;
    private int i_u = 0;

    public Fragment4() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        final FragmentSub1 fragmentSub1 = mActivity.fragmentSub1;

        FragmentSub1.Callback callbackfromMain = new FragmentSub1.Callback() {
            @Override
            public void updateList() {
                update();
            }
        };
        fragmentSub1.setCallback4(callbackfromMain);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment4, container, false);

        initLayout();

        return mView;
    }

    private void initLayout() {

        mTxtAccount = mView.findViewById(R.id.editText_account_frag4);
        mTxtInstitute = mView.findViewById(R.id.editText_institute);
        mTxtDiscription = mView.findViewById(R.id.editText_description);

        update();

        mView.findViewById(R.id.button_regist_frag4).setOnClickListener(mClickListener);
        mView.findViewById(R.id.button_edit_frag4).setOnClickListener(mClickListener);
        mView.findViewById(R.id.button_delete_frag4).setOnClickListener(mClickListener);
    }

    private final Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final FragmentSub1 fragmentSub1 = (FragmentSub1) getParentFragment();

            String account = mTxtAccount.getText().toString();
            String institute = mTxtInstitute.getText().toString();
            String discript = mTxtDiscription.getText().toString();

            switch(v.getId()) {
                case R.id.button_regist_frag4:
                    if(!account.isEmpty()) ir.insertAccount(institute,account,discript);
                    break;
                case R.id.button_edit_frag4:
                    if(!account.isEmpty()) ir.updateAccount(ir.getCurrentAccount(),institute,account,discript);
                    break;
                case R.id.button_delete_frag4:
                    if(!account.isEmpty()) ir.deleteAccount("num",new String[] {account});
                    break;
            }

            fragmentSub1.updateAccountSpinner();
        }
    };

    private void update() {
        int id = ir.getCurrentAccount();
        Account account = new Account();

        if(id > 0) {
            account = ir.getAccount(String.valueOf(id));
        }
        else {
            i_u = 0;
            mTxtAccount.setText("");
            mTxtDiscription.setText("");
            mTxtInstitute.setText("");

            return;
        }

        if(account == null) {
            i_u = 0;
            mTxtAccount.setText("");
            mTxtDiscription.setText("");
            mTxtInstitute.setText("");
        }
        else {
            i_u = 1;
            mTxtAccount.setText(account.getNumber());
            mTxtDiscription.setText(account.getDiscription());
            mTxtInstitute.setText(account.getInstitute());
            ir.setCurrentAccount(account.getId());
        }
    }
}
