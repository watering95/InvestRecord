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

@SuppressWarnings("DefaultFileTemplate")
class List1Adapter extends BaseAdapter {

    private final ArrayList<Info_List1> mData;
    private final LayoutInflater inflater;

    public List1Adapter(Context context, ArrayList<Info_List1> data) {
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
            convertView = inflater.inflate(R.layout.layout_list1,parent,false);
        }

        TextView txtAccount = convertView.findViewById(R.id.textView_layout_list1_1);
        TextView principal = convertView.findViewById(R.id.textView_layout_list1_2);
        TextView evaluation = convertView.findViewById(R.id.textView_layout_list1_3);
        TextView rate = convertView.findViewById(R.id.textView_layout_list1_4);
        TextView accountInfo = convertView.findViewById(R.id.textView_layout_list1_5);

        Info_List2 list2 = mData.get(position).getList2();
        Account account = mData.get(position).getAccount();
        DecimalFormat df = new DecimalFormat("#,###");

        txtAccount.setText(String.valueOf(account.getNumber()));
        principal.setText(df.format(list2.getDairy().getPrincipal()));
        rate.setText(String.format(Locale.getDefault(),"%.2f",list2.getDairy().getRate()));
        evaluation.setText(df.format(list2.getEvaluation()));
        accountInfo.setText(String.format(Locale.getDefault(),"%s %s",account.getInstitute(),account.getDescription()));

        return convertView;
    }
}
