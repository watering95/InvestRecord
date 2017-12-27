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

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("ALL")
public class Fragment5 extends Fragment {

    private View mView;
    private MainActivity mActivity;
    private String selectedDate;
    private List5Adapter list5Adapter;

    public Fragment5() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment5, container, false);

        initLayout();

        return mView;
    }

    private void initLayout() {
        final EditText editText_date = mView.findViewById(R.id.editText_frag5_date);
        editText_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.editText_frag5_date, new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String date) {
                        editText_date.setText(date);
                        selectedDate = date;
                    }
                });

                dialog.show(getFragmentManager(), "dialog");
            }
        });

        ListView listView = mView.findViewById(R.id.listview_frag5);
        listView.setAdapter(list5Adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        FloatingActionButton floating = mView.findViewById(R.id.floating_frag5);
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.floating_frag5, new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String date) {

                    }
                });

                dialog.show(getFragmentManager(), "dialog");
            }
        });
    }
}