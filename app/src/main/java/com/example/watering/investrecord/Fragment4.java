package com.example.watering.investrecord;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

@SuppressWarnings("DefaultFileTemplate")
public class Fragment4 extends Fragment {

    private View mView;
    private IRResolver ir;
    private EditText mTxtAccount;
    private EditText mTxtInstitute;
    private EditText mTxtDescription;

    public Fragment4() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity mActivity = (MainActivity) getActivity();
        assert mActivity != null;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment4, container, false);

        initLayout();

        return mView;
    }

    private void initLayout() {

        mTxtAccount = mView.findViewById(R.id.editText_frag4_account);
        mTxtInstitute = mView.findViewById(R.id.editText_frag4_institute);
        mTxtDescription = mView.findViewById(R.id.editText_frag4_description);

        update();

        mView.findViewById(R.id.button_frag4_regist).setOnClickListener(mClickListener);
        mView.findViewById(R.id.button_frag4_edit).setOnClickListener(mClickListener);
        mView.findViewById(R.id.button_frag4_delete).setOnClickListener(mClickListener);
    }

    private final Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final FragmentSub1 fragmentSub1 = (FragmentSub1) getParentFragment();

            String account = mTxtAccount.getText().toString();
            String institute = mTxtInstitute.getText().toString();
            String descript = mTxtDescription.getText().toString();

            switch(v.getId()) {
                case R.id.button_frag4_regist:
                    if(!account.isEmpty()) ir.insertAccount(institute,account,descript);
                    break;
                case R.id.button_frag4_edit:
                    if(!account.isEmpty()) ir.updateAccount(ir.getCurrentAccount(),institute,account,descript);
                    break;
                case R.id.button_frag4_delete:
                    if(!account.isEmpty()) ir.deleteAccount("number",new String[] {account});
                    break;
            }

            assert fragmentSub1 != null;
            fragmentSub1.updateAccountSpinner();
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
