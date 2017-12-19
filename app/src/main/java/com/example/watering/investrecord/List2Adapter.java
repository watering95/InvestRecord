package com.example.watering.investrecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by watering on 17. 11. 17.
 */

@SuppressWarnings("ALL")
class List2Adapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Info_List2> mData;
    private final LayoutInflater inflater;

    public List2Adapter(Context context, ArrayList<Info_List2> data) {
        mContext = context;
        mData = data;
        inflater = LayoutInflater.from(mContext);
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
            convertView = inflater.inflate(R.layout.layout_list2,parent,false);
        }

        Info_Dairy dairy;
        TextView date = convertView.findViewById(R.id.textView_layout_list2_1);
        TextView principal = convertView.findViewById(R.id.textView_layout_list2_2);
        TextView evaluation = convertView.findViewById(R.id.textView_layout_list2_3);
        TextView rate = convertView.findViewById(R.id.textView_layout_list2_4);
        DecimalFormat df = new DecimalFormat("#,###");

        dairy = mData.get(position).getDairy();
        date.setText(String.valueOf(dairy.getDate()));
        principal.setText(df.format(dairy.getPrincipal()));
        rate.setText(String.format(Locale.getDefault(),"%.2f",dairy.getRate()));
        evaluation.setText(df.format(mData.get(position).getEvaluation()));

        return convertView;
    }
}
