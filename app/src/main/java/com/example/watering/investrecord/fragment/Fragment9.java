package com.example.watering.investrecord.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.watering.investrecord.data.Account;
import com.example.watering.investrecord.IRResolver;
import com.example.watering.investrecord.MainActivity;
import com.example.watering.investrecord.R;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Fragment9 extends Fragment {

    private View mView;
    private IRResolver ir;
    private EditText mTxtAccount;
    private EditText mTxtInstitute;
    private EditText mTxtDescription;

    public Fragment9() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        ir = mActivity.ir;

        final FragmentSub3 fragmentSub3 = mActivity.fragmentSub3;

        FragmentSub3.Callback callback = new FragmentSub3.Callback() {
            @Override
            public void update() {
                Fragment9.this.update();
            }
        };
        fragmentSub3.setCallback8(callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment9, container, false);

        initLayout();

        return mView;
    }

    private void initLayout() {

        mTxtAccount = mView.findViewById(R.id.editText_frag9_account);
        mTxtInstitute = mView.findViewById(R.id.editText_frag9_institute);
        mTxtDescription = mView.findViewById(R.id.editText_frag9_description);

        update();

        mView.findViewById(R.id.button_frag9_regist).setOnClickListener(mClickListener);
        mView.findViewById(R.id.button_frag9_edit).setOnClickListener(mClickListener);
        mView.findViewById(R.id.button_frag9_delete).setOnClickListener(mClickListener);
    }

    private final Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final FragmentSub3 fragmentSub3 = (FragmentSub3) getParentFragment();

            String account = mTxtAccount.getText().toString();
            String institute = mTxtInstitute.getText().toString();
            String descript = mTxtDescription.getText().toString();

            switch(v.getId()) {
                case R.id.button_frag9_regist:
                    if(!account.isEmpty()) ir.insertAccount(institute,account,descript);
                    break;
                case R.id.button_frag9_edit:
                    if(!account.isEmpty()) ir.updateAccount(ir.getCurrentAccount(),institute,account,descript);
                    break;
                case R.id.button_frag9_delete:
                    if(!account.isEmpty()) ir.deleteAccount("number",new String[] {account});
                    break;
            }

            assert fragmentSub3 != null;
            fragmentSub3.updateAccountSpinner();
        }
    };

    private void update() {
        int id_account = ir.getCurrentAccount();
        Account account;

        if(id_account > 0) {
            account = ir.getAccount(id_account);
        }
        else {
            mTxtAccount.setText("");
            mTxtDescription.setText("");
            mTxtInstitute.setText("");

            return;
        }

        if(account == null) {
            mTxtAccount.setText("");
            mTxtDescription.setText("");
            mTxtInstitute.setText("");
        }
        else {
            mTxtAccount.setText(account.getNumber());
            mTxtDescription.setText(account.getDescription());
            mTxtInstitute.setText(account.getInstitute());
            ir.setCurrentAccount(account.getId());
        }
    }
}
