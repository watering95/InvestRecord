package com.example.watering.investrecord;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by watering on 17. 10. 21.
 */

public class Fragment1 extends Fragment {

    private View mView;
    private ListView listView;
    private List1Adapter listAdapter;
    private MainActivity mActivity;
    private IRResolver ir;
    private ArrayList<Info_List1> lists = new ArrayList<>();
    MainActivity.Callback callbackfromMain;

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
                initLayout();
            }
        };
        mActivity.setCallback1(callbackfromMain);

        return mView;
    }

    private void initLayout() {
        listView = (ListView)mView.findViewById(R.id.listview_totalasset_frag1);
        listAdapter = new List1Adapter(mView.getContext(),lists);
        if(lists.size() !=0) listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void updateInfoLists() {

        lists.clear();

        Info_List1 list1 = new Info_List1();
        Info_List2 list2 = new Info_List2();
        List<Account> accounts = ir.getAccounts();

        Info_Dairy dairy;

        for(int i = 0; i < accounts.size(); i++) {

            String account = String.valueOf(accounts.get(i).getId());

            dairy = ir.getLastInfoDairy(account);
            if(dairy == null) {
                return;
            }

            list2.setDairy(dairy);
            list2.setEvaluation(ir.getInfoIO(account,dairy.getDate()).getEvaluation());

            list1.setAccount(accounts.get(i));
            list1.setInfoList2(list2);

            lists.add(list1);
        }
    }
}
