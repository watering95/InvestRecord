package com.example.watering.investrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("ALL")
public class Fragment6 extends Fragment {

    private View mView;
    private MainActivity mActivity;
    private IRResolver ir;
    private String selectedDate;
    private List6Adapter list6Adapter;
    private ArrayList<Income> incomes = new ArrayList<>();

    public Fragment6() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment6, container, false);

        initLayout();

        return mView;
    }

    private void initLayout() {
        final EditText editText_date = mView.findViewById(R.id.editText_frag6_date);

        selectedDate = mActivity.getToday();

        editText_date.setText(selectedDate);
        editText_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.editText_frag6_date, new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String date) {
                        selectedDate = date;
                        editText_date.setText(selectedDate);
                        incomes = (ArrayList<Income>) ir.getIncomes(selectedDate);
                        list6Adapter.notifyDataSetChanged();
                    }
                });
                dialog.setSelectedDate(selectedDate);
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        ListView listView = mView.findViewById(R.id.listview_frag6);

        incomes = (ArrayList<Income>) ir.getIncomes(selectedDate);
        list6Adapter = new List6Adapter(mView.getContext(),incomes);
        listView.setAdapter(list6Adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id_income = incomes.get(position).getId();
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.floating_frag6, new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String date) {
                        incomes = (ArrayList<Income>) ir.getIncomes(selectedDate);
                        list6Adapter.notifyDataSetChanged();
                    }
                });
                dialog.initId(String.valueOf(id_income));
                dialog.show(getFragmentManager(),"dialog");
            }
        });

        FloatingActionButton floating = mView.findViewById(R.id.floating_frag6);
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.floating_frag6, new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String date) {
                        incomes = (ArrayList<Income>) ir.getIncomes(selectedDate);
                        list6Adapter.notifyDataSetChanged();
                    }
                });
                dialog.setSelectedDate(selectedDate);
                dialog.show(getFragmentManager(), "dialog");
            }
        });
    }
}