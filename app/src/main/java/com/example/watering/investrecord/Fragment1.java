package com.example.watering.investrecord;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
public class Fragment1 extends Fragment {

    private View mView;
    private TextView mTxtTotalPrincipal;
    private TextView mTxtTotalEvaluate;
    private TextView mTxtTotalRate;
    private List1Adapter listAdapter;
    private IRResolver ir;
    private final ArrayList<Info_List1> lists = new ArrayList<>();
    private int sum_principal;
    private int sum_evaluate;
    private double total_rate;

    public Fragment1() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MainActivity mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        MainActivity.Callback callbackfromMain = new MainActivity.Callback() {
            @Override
            public void updateList() {
                DecimalFormat df = new DecimalFormat("#,###");
                updateInfoLists();
                mTxtTotalPrincipal.setText(df.format(sum_principal));
                mTxtTotalEvaluate.setText(df.format(sum_evaluate));
                mTxtTotalRate.setText(String.format("%.2f",total_rate));
                listAdapter.notifyDataSetChanged();
                mActivity.CallUpdate2();
            }
        };
        mActivity.setCallback1(callbackfromMain);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment1, container, false);

        updateInfoLists();
        initLayout();

        return mView;
    }

    private void initLayout() {
        DecimalFormat df = new DecimalFormat("#,###");

        mTxtTotalPrincipal = mView.findViewById(R.id.text_total_principal);
        mTxtTotalPrincipal.setText(df.format(sum_principal));
        mTxtTotalEvaluate = mView.findViewById(R.id.text_total_evaluate);
        mTxtTotalEvaluate.setText(df.format(sum_evaluate));
        mTxtTotalRate = mView.findViewById(R.id.text_total_rate);
        mTxtTotalRate.setText(String.format("%.2f",total_rate));

        ListView listView = mView.findViewById(R.id.listview_totalasset_frag1);
        listAdapter = new List1Adapter(mView.getContext(),lists);
        if(lists.size() != 0) listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void updateInfoLists() {
        Info_Dairy dairy;
        Info_IO io;

        sum_evaluate = 0;
        sum_principal = 0;
        total_rate = 0;

        lists.clear();
        List<Account> accounts = ir.getAccounts();

        for(int i = 0; i < accounts.size(); i++) {
            Info_List1 list1 = new Info_List1();
            Info_List2 list2 = new Info_List2();

            int id = accounts.get(i).getId();
            String account = String.valueOf(id);

            dairy = ir.getLastInfoDairy(account);
            if(dairy == null) {
                dairy = new Info_Dairy();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",java.util.Locale.getDefault());
                long now = System.currentTimeMillis();
                Date date = new Date(now);

                dairy.setDate(dateFormat.format(date));
                dairy.setAccount(id);
                dairy.setPrincipal(0);
                dairy.setRate(0);
            }
            io = ir.getInfoIO(id,dairy.getDate());
            if(io == null) {
                io = new Info_IO();
                io.setEvaluation(0);
            }

            list2.setDairy(dairy);
            list2.setEvaluation(io.getEvaluation());

            list1.setAccount(accounts.get(i));
            list1.setInfoList2(list2);
            sum_evaluate += list1.getList2().getEvaluation();
            sum_principal += dairy.getPrincipal();

            lists.add(list1);
        }

        if(sum_principal != 0 && sum_evaluate != 0) total_rate = (double)sum_evaluate / (double)sum_principal * 100 - 100;
    }
}
