package com.example.watering.investrecord.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.watering.investrecord.IRResolver;
import com.example.watering.investrecord.data.Income;
import com.example.watering.investrecord.info.*;
import com.example.watering.investrecord.adapter.*;
import com.example.watering.investrecord.MainActivity;
import com.example.watering.investrecord.R;

import java.util.ArrayList;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Fragment5 extends Fragment {

    private View mView;
    private MainActivity mActivity;
    private IRResolver ir;
    private String selectedDate;
    private List5Adapter list5Adapter;
    private ArrayList<Income> incomes = new ArrayList<>();
    private final ArrayList<InfoList5> lists = new ArrayList<>();
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
                updateListView();
            }
        };

        fragmentSub2.setCallback4(callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment5, container, false);

        initLayout();

        return mView;
    }

    private void initLayout() {
        final EditText editText_date = mView.findViewById(R.id.editText_frag5_date);

        selectedDate = mActivity.getToday();

        ImageButton image_btn_backward = mView.findViewById(R.id.image_btn_frag5_date_backward);
        ImageButton image_btn_forward = mView.findViewById(R.id.image_btn_frag5_date_forward);

        image_btn_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = mActivity.dateChange(selectedDate, -1);
                editText_date.setText(selectedDate);
                updateListView();
            }
        });

        image_btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = mActivity.dateChange(selectedDate, 1);
                editText_date.setText(selectedDate);
                updateListView();
            }
        });

        editText_date.setText(selectedDate);
        editText_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.editText_frag5_date, new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String date) {
                        selectedDate = date;
                        editText_date.setText(selectedDate);
                        updateListView();
                    }
                });
                dialog.setSelectedDate(selectedDate);
                //noinspection ConstantConditions
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        ListView listView = mView.findViewById(R.id.listview_frag5);

        updateInfoLists();
        list5Adapter = new List5Adapter(mView.getContext(),lists);
        listView.setAdapter(list5Adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id_income = incomes.get(position).getId();
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.floating_frag5, new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String date) {
                        updateListView();
                    }
                });
                dialog.initId(id_income);
                //noinspection ConstantConditions
                dialog.show(getFragmentManager(),"dialog");
            }
        });

        FloatingActionButton floating = mView.findViewById(R.id.floating_frag5);
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.floating_frag5, new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String date) {
                        updateListView();
                    }
                });
                dialog.setSelectedDate(selectedDate);
                //noinspection ConstantConditions
                dialog.show(getFragmentManager(), "dialog");
            }
        });
    }

    private void updateInfoLists() {
        InfoList5 list;

        lists.clear();
        incomes = (ArrayList<Income>) ir.getIncomes(selectedDate);
        if(incomes.isEmpty()) {
            Log.i(TAG, "No income");
            return;
        }

        for(int i = 0, n = incomes.size(); i < n; i++) {
            list = new InfoList5();
            list.setIncome(incomes.get(i));

            lists.add(list);
        }
    }
    private void updateListView() {
        updateInfoLists();
        list5Adapter.notifyDataSetChanged();
    }
}