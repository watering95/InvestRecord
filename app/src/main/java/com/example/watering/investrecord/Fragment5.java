package com.example.watering.investrecord;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Fragment5 extends Fragment {

    private View mView;
    private MainActivity mActivity;
    private IRResolver ir;
    private List5Adapter listAdapter;
    private final ArrayList<Info_List5> lists = new ArrayList<>();
    private static final String TAG = "InvestRecord";

    public Fragment5() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        ir = mActivity.ir;

        final FragmentSub2 fragmentSub2 = mActivity.fragmentSub2;

        FragmentSub2.Callback callback = new FragmentSub2.Callback() {
            @Override
            public void update() {
                updateInfoLists();
                listAdapter.notifyDataSetChanged();
            }
        };

        fragmentSub2.setCallback5(callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment5, container, false);

        updateInfoLists();
        initLayout();

        return mView;
    }

    private void initLayout() {
        ListView listView = mView.findViewById(R.id.listview_frag5);
        listAdapter = new List5Adapter(mView.getContext(), lists);
        if(lists.size() != 0) {
            listView.setAdapter(listAdapter);
        }
    }

    @SuppressLint("DefaultLocale")
    private void updateInfoLists() {
        int income, spend;
        String txtMonth, txtDate;

        Info_List5 list5;

        Calendar date = mActivity.strToCalendar(mActivity.getToday());
        txtDate = mActivity.calendarToStr(date);

        lists.clear();

        for(int i = 0; i < 12; i++) {
            list5 = new Info_List5();

            txtMonth = String.format("%04d-%02d", date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1);
            income = ir.getIncomeMonth(txtDate);
            spend = ir.getSpendMonth(txtDate);

            list5.setMonth(txtMonth);
            list5.setIncome(income);
            list5.setSpend(spend);
            lists.add(list5);

            txtDate = mActivity.monthChange(txtDate, -1);
            date = mActivity.strToCalendar(txtDate);
        }
    }
}