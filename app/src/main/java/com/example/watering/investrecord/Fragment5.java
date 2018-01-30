package com.example.watering.investrecord;

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
    private EditText editText_date;
    private ArrayList<Spend> spends = new ArrayList<>();
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

        FragmentSub2.Callback callbackfromMain = new FragmentSub2.Callback() {
            @Override
            public void updateList() {
                updateListView();
            }
        };

        fragmentSub2.setCallback5(callbackfromMain);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment5, container, false);

        initLayout();

        return mView;
    }

    private void initLayout() {
        editText_date = mView.findViewById(R.id.editText_frag5_date);

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
                String spend_code = spends.get(position).getCode();
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.floating_frag5, new UserDialogFragment.UserListener() {
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
        lists.clear();
        spends = (ArrayList<Spend>) ir.getSpends(selectedDate);

        if(spends.isEmpty()) {
            Log.i(TAG,"No spend");
            return;
        }

        for(int i = 0; i < spends.size(); i++) {
            Info_List5 list = new Info_List5();

            list.setSpend(spends.get(i));
            lists.add(list);
        }
    }
    private void updateListView() {
        updateInfoLists();
        list5Adapter.notifyDataSetChanged();
    }
}