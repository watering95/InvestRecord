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

public class List1Adapter extends BaseAdapter {

    Context mContext;
    ArrayList<Info_List1> mData;
    LayoutInflater inflater;

    public List1Adapter(Context context, ArrayList<Info_List1> data) {
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

        TextView account = (TextView)convertView.findViewById(R.id.list_1);
        TextView principal = (TextView)convertView.findViewById(R.id.list_2);
        TextView evaluation = (TextView)convertView.findViewById(R.id.list_3);
        TextView rate = (TextView)convertView.findViewById(R.id.list_4);

        Info_List2 list2 = mData.get(position).getList2();

        account.setText(String.valueOf(mData.get(position).getAccount().getNumber()));
        principal.setText(String.valueOf(list2.getDairy().getPrincipal()));
        rate.setText(String.format("%.2f",list2.getDairy().getRate()));
        evaluation.setText(String.valueOf(list2.getEvaluation()));

        return convertView;
    }
}
