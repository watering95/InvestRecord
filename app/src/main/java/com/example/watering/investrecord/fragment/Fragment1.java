package com.example.watering.investrecord.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.watering.investrecord.data.Account;
import com.example.watering.investrecord.IRResolver;
import com.example.watering.investrecord.info.InfoDairyKRW;
import com.example.watering.investrecord.info.InfoDairyTotal;
import com.example.watering.investrecord.info.InfoIOKRW;
import com.example.watering.investrecord.info.InfoList1;
import com.example.watering.investrecord.info.InfoList6;
import com.example.watering.investrecord.adapter.List1Adapter;
import com.example.watering.investrecord.MainActivity;
import com.example.watering.investrecord.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Fragment1 extends Fragment {

    private View mView;
    private MainActivity mActivity;
    private TextView mTxtTotalPrincipal;
    private TextView mTxtTotalEvaluate;
    private TextView mTxtTotalRate;
    private List1Adapter listAdapter;
    private IRResolver ir;
    private final ArrayList<InfoList1> lists = new ArrayList<>();
    private int sum_principal;
    private int sum_evaluate;
    private double total_rate;
    private static final String TAG = "InvestRecord";

    public Fragment1() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        ir = mActivity.ir;

        final FragmentSub1 fragmentSub1 = mActivity.fragmentSub1;

        FragmentSub1.Callback callback = new FragmentSub1.Callback() {
            @Override
            public void update() {
                DecimalFormat df = new DecimalFormat("#,###");
                updateInfoLists();
                mTxtTotalPrincipal.setText(df.format(sum_principal));
                mTxtTotalEvaluate.setText(df.format(sum_evaluate));
                mTxtTotalRate.setText(String.format(Locale.getDefault(),"%.2f",total_rate));
                listAdapter.notifyDataSetChanged();
            }
        };

        fragmentSub1.setCallback1(callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment1, container, false);

        updateInfoLists();
        initLayout();

        return mView;
    }

    private void initLayout() {
        DecimalFormat df = new DecimalFormat("#,###");

        mTxtTotalPrincipal = mView.findViewById(R.id.textView_frag1_total_principal);
        mTxtTotalPrincipal.setText(df.format(sum_principal));
        mTxtTotalEvaluate = mView.findViewById(R.id.textView_frag1_total_evaluate);
        mTxtTotalEvaluate.setText(df.format(sum_evaluate));
        mTxtTotalRate = mView.findViewById(R.id.textView_frag1_total_rate);
        mTxtTotalRate.setText(String.format(Locale.getDefault(),"%.2f",total_rate));

        ListView listView = mView.findViewById(R.id.listview_frag1_totalasset);
        listAdapter = new List1Adapter(mView.getContext(),lists);
        if(lists.size() != 0) {
            listView.setAdapter(listAdapter);
            // 처음 시작 시 현재 계좌 선택이 되지 않는 문제를 해결하기 위해 추가
            ir.setCurrentAccount(lists.get(0).getAccount().getId());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ir.setCurrentAccount(lists.get(position).getAccount().getId());

                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, mActivity.fragmentSub3).commit();
            }
        });
    }

    private void updateInfoLists() {
        int id_account;

        InfoDairyTotal dairy_total;
        InfoList1 list1;
        InfoList6 list6;

        List<Account> accounts = ir.getAccounts(ir.getCurrentGroup());

        sum_evaluate = 0;
        sum_principal = 0;
        total_rate = 0;

        lists.clear();

        if(accounts.isEmpty()) {
            Log.i(TAG, "No account");
            return;
        }

        for(int i = 0, n = accounts.size(); i < n; i++) {
            list1 = new InfoList1();
            list6 = new InfoList6();

            id_account = accounts.get(i).getId();

            dairy_total = ir.getLastInfoDairyTotal(id_account);
            if(dairy_total == null) {
                InfoDairyKRW dairy_krw = ir.getLastInfoDairyKRW(id_account);
                InfoIOKRW io_krw;
                if(dairy_krw == null) {
                    dairy_krw = new InfoDairyKRW();
                    io_krw = new InfoIOKRW();

                    dairy_krw.setRate(0);
                    dairy_krw.setPrincipal(0);
                    io_krw.setEvaluation(0);
                }
                else {
                    io_krw = ir.getLastInfoIOKRW(id_account, dairy_krw.getDate());
                }

                dairy_total = new InfoDairyTotal();

                dairy_total.setEvaluation(io_krw.getEvaluation());
                dairy_total.setRate(dairy_krw.getRate());
                dairy_total.setPrincipal(dairy_krw.getPrincipal());
            }

            list6.setDairyTotal(dairy_total);

            list1.setAccount(accounts.get(i));
            list1.setInfoList6(list6);
            sum_evaluate += dairy_total.getEvaluation();
            sum_principal += dairy_total.getPrincipal();

            lists.add(list1);
        }

        if(sum_principal != 0 && sum_evaluate != 0) total_rate = (double)sum_evaluate / (double)sum_principal * 100 - 100;
    }
}
