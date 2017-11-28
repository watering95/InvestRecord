package com.example.watering.investrecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by watering on 17. 11. 17.
 */

public class List2Adapter extends BaseAdapter {

    Context mContext;
    ArrayList<Info_List2> mData;
    LayoutInflater inflater;

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
            convertView = inflater.inflate(R.layout.layout_list,parent,false);
        }

        Info_Dairy dairy = new Info_Dairy();
        TextView date = (TextView)convertView.findViewById(R.id.list_1);
        TextView principal = (TextView)convertView.findViewById(R.id.list_2);
        TextView evaluation = (TextView)convertView.findViewById(R.id.list_3);
        TextView rate = (TextView)convertView.findViewById(R.id.list_4);

        dairy = mData.get(position).getDairy();
        date.setText(String.valueOf(dairy.getDate()));
        principal.setText(String.valueOf(dairy.getPrincipal()));
        rate.setText(String.format("%.2f",dairy.getRate()));
        evaluation.setText(String.valueOf(mData.get(position).getEvaluation()));

        return convertView;
    }
}
