package com.example.watering.investrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("ALL")
public class Fragment3 extends Fragment {

    private View mView;
    private MainActivity mActivity;
    private String selectedDate;

    public Fragment3() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment3, container, false);

        initLayout();

        return mView;
    }

    private void initLayout() {
        DatePicker date = mView.findViewById(R.id.datePicker_frag3);
        selectedDate = String.format(Locale.getDefault(),"%04d-%02d-%02d", date.getYear(), date.getMonth(), date.getDayOfMonth());
        date.init(date.getYear(), date.getMonth(), date.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar select = Calendar.getInstance();
                select.set(year,monthOfYear,dayOfMonth);

                if(Calendar.getInstance().before(select)) {
                    Toast.makeText(mActivity.getApplicationContext(),R.string.toast_date_error,Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedDate = String.format(Locale.getDefault(),"%04d-%02d-%02d",year,monthOfYear+1,dayOfMonth);

                UserDialogFragment dialog = UserDialogFragment.newInstance(0, new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String date) {
                    }
                });
                dialog.setSelectedDate(selectedDate);
                dialog.show(getFragmentManager(), "dialog");
            }
        });
    }
}