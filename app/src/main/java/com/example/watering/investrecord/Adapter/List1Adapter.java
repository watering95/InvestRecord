package com.example.watering.investrecord.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.watering.investrecord.Data.Account;
import com.example.watering.investrecord.Info.Info_List1;
import com.example.watering.investrecord.Info.Info_List6;
import com.example.watering.investrecord.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by watering on 17. 11. 17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class List1Adapter extends BaseAdapter {

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

        Info_List6 list6 = mData.get(position).getList6();
        Account account = mData.get(position).getAccount();
        DecimalFormat df = new DecimalFormat("#,###");

        txtAccount.setText(String.valueOf(account.getNumber()));
        principal.setText(df.format(list6.getDairy().getPrincipal()));
        rate.setText(String.format(Locale.getDefault(),"%.2f",list6.getDairy().getRate()));
        evaluation.setText(df.format(list6.getEvaluation()));
        accountInfo.setText(String.format(Locale.getDefault(),"%s %s",account.getInstitute(),account.getDescription()));

        return convertView;
    }
}
