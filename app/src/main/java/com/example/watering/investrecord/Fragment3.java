package com.example.watering.investrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by watering on 17. 10. 21.
 */

public class Fragment3 extends Fragment {

    private View mView;
    private MainActivity mActivity;
    private IRResolver ir;
    private DatePicker date;

    private MainActivity.Callback callbackfromMain;

    private String selectedDate;

    public Fragment3() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment3, container, false);
        mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        initLayout();

        callbackfromMain = new MainActivity.Callback() {
            @Override
            public void updateList() {

            }
        };
        mActivity.setCallback3(callbackfromMain);

        return mView;
    }

    private void initLayout() {
        date = (DatePicker) mView.findViewById(R.id.date);
        selectedDate = String.format("%04d-%02d-%02d",date.getYear(),date.getMonth(),date.getDayOfMonth());
        date.init(date.getYear(), date.getMonth(), date.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate = String.format("%04d-%02d-%02d",year,monthOfYear+1,dayOfMonth);
                mActivity.inoutDialog(selectedDate);
            }
        });
    }
}