package com.example.watering.investrecord;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by watering on 17. 10. 21.
 */

public class Fragment1 extends Fragment {

    private View mView;
    private TextView mTxtTotal;
    private ListView listView;
    private List1Adapter listAdapter;
    private MainActivity mActivity;
    private IRResolver ir;
    private ArrayList<Info_List1> lists = new ArrayList<>();
    private MainActivity.Callback callbackfromMain;
    private int sum;

    public Fragment1() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment1, container, false);
        mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        updateInfoLists();
        initLayout();

        callbackfromMain = new MainActivity.Callback() {
            @Override
            public void updateList() {
                updateInfoLists();
                mTxtTotal.setText(String.valueOf(sum));
                listAdapter.notifyDataSetChanged();
            }
        };
        mActivity.setCallback1(callbackfromMain);

        return mView;
    }

    private void initLayout() {
        mTxtTotal = (TextView)mView.findViewById(R.id.text_total);
        mTxtTotal.setText(String.valueOf(sum));
        listView = (ListView)mView.findViewById(R.id.listview_totalasset_frag1);
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

        sum = 0;
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

                dairy.setDate(dateFormat.format(date).toString());
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
            sum += list1.getList2().getEvaluation();

            lists.add(list1);
        }
    }
}
