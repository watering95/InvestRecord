package com.example.watering.investrecord.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.watering.investrecord.Info.Info_Dairy;
import com.example.watering.investrecord.Info.Info_List6;
import com.example.watering.investrecord.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by watering on 17. 11. 17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class List6Adapter extends BaseAdapter {

    private final ArrayList<Info_List6> mData;
    private final LayoutInflater inflater;

    public List6Adapter(Context context, ArrayList<Info_List6> data) {
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
            convertView = inflater.inflate(R.layout.layout_list6,parent,false);
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
