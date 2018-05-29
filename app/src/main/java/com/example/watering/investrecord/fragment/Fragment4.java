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
import com.example.watering.investrecord.MainActivity;
import com.example.watering.investrecord.R;
import com.example.watering.investrecord.data.Spend;
import com.example.watering.investrecord.adapter.*;
import com.example.watering.investrecord.info.*;

import java.util.ArrayList;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Fragment4 extends Fragment {

    private View mView;
    private MainActivity mActivity;
    private IRResolver ir;
    private String selectedDate;
    private List4Adapter list4Adapter;
    private EditText editText_date;
    private ArrayList<Spend> spends = new ArrayList<>();
    private final ArrayList<InfoList4> lists = new ArrayList<>();
    private static final String TAG = "InvestRecord";

    public Fragment4() {
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

        fragmentSub2.setCallback3(callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment4, container, false);

        initLayout();

        return mView;
    }

    private void initLayout() {
        editText_date = mView.findViewById(R.id.editText_frag4_date);

        selectedDate = mActivity.getToday();

        ImageButton image_btn_backward = mView.findViewById(R.id.image_btn_frag4_date_backward);
        ImageButton image_btn_forward = mView.findViewById(R.id.image_btn_frag4_date_forward);

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
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.editText_frag4_date, new UserDialogFragment.UserListener() {
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

        ListView listView = mView.findViewById(R.id.listview_frag4);

        updateInfoLists();
        list4Adapter = new List4Adapter(mView.getContext(),lists);
        listView.setAdapter(list4Adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String spend_code = spends.get(position).getCode();
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.floating_frag4, new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String date) {
                        updateListView();
                    }
                });
                dialog.initCode(spend_code);
                //noinspection ConstantConditions
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        FloatingActionButton floating = mView.findViewById(R.id.floating_frag4);
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.floating_frag4, new UserDialogFragment.UserListener() {
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
        InfoList4 list;

        lists.clear();

        spends = (ArrayList<Spend>) ir.getSpends(selectedDate);

        if(spends.isEmpty()) {
            Log.i(TAG,"No spend");
            return;
        }

        for(int i = 0, n = spends.size(); i < n; i++) {
            list = new InfoList4();
            list.setSpend(spends.get(i));

            lists.add(list);
        }
    }
    private void updateListView() {
        updateInfoLists();
        list4Adapter.notifyDataSetChanged();
    }
}