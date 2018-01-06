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

    public List5Adapter(Context context, ArrayList<Info_List5> data) {
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

        DecimalFormat df = new DecimalFormat("#,###");
        TextView textView_1 = convertView.findViewById(R.id.textView_layout_list5_1);
        TextView textView_2 = convertView.findViewById(R.id.textView_layout_list5_2);
        TextView textView_3 = convertView.findViewById(R.id.textView_layout_list5_3);

        textView_1.setText(mData.get(position).getSpend().getDetails());
        textView_3.setText(df.format(mData.get(position).getSpend().getAmount()));

        return convertView;
    }
}
