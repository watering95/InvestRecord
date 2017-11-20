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

public class ListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Info_Dairy> mData;
    LayoutInflater inflater;

    public ListAdapter(Context context, ArrayList<Info_Dairy> data) {
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

        TextView date = (TextView)convertView.findViewById(R.id.list_date);
        TextView evaluation = (TextView)convertView.findViewById(R.id.list_evaluation);
        TextView principal = (TextView)convertView.findViewById(R.id.list_principal);
        TextView rate = (TextView)convertView.findViewById(R.id.list_rate);

        date.setText(String.valueOf(mData.get(position).getDate()));
        principal.setText(String.valueOf(mData.get(position).getPrincipal()));
        rate.setText(String.valueOf(mData.get(position).getRate()));

        return convertView;
    }
}
