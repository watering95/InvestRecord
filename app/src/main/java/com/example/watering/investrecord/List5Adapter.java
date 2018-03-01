package com.example.watering.investrecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by watering on 17. 11. 17.
 */

@SuppressWarnings("DefaultFileTemplate")
class List5Adapter extends BaseAdapter {

    private final ArrayList<Info_List5> mData;
    private final LayoutInflater inflater;

    List5Adapter(Context context, ArrayList<Info_List5> data) {
        mData = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.layout_list5,parent,false);
        }

        TextView txtMonth = convertView.findViewById(R.id.textView_layout_list5_1);
        TextView txtIncome = convertView.findViewById(R.id.textView_layout_list5_2);
        TextView txtSpend = convertView.findViewById(R.id.textView_layout_list5_3);

        Info_List5 list5 = mData.get(position);
        DecimalFormat df = new DecimalFormat("#,###");

        txtMonth.setText(list5.getMonth());
        txtIncome.setText(df.format(list5.getIncome()));
        txtSpend.setText(df.format(list5.getSpend()));

        return convertView;
    }
}
