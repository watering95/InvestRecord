package com.example.watering.investrecord.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.example.watering.investrecord.info.Info_List1
import com.example.watering.investrecord.R

import java.text.DecimalFormat
import java.util.ArrayList
import java.util.Locale

/**
 * Created by watering on 17. 11. 17.
 */

class List1Adapter(context: Context, private val mData: ArrayList<Info_List1>) : BaseAdapter() {
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): Any {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_list1, parent, false)
        }

        val txtAccount = convertView!!.findViewById<TextView>(R.id.textView_layout_list1_1)
        val principal = convertView.findViewById<TextView>(R.id.textView_layout_list1_2)
        val evaluation = convertView.findViewById<TextView>(R.id.textView_layout_list1_3)
        val rate = convertView.findViewById<TextView>(R.id.textView_layout_list1_4)
        val accountInfo = convertView.findViewById<TextView>(R.id.textView_layout_list1_5)

        val list6 = mData[position].list6
        val account = mData[position].account
        val df = DecimalFormat("#,###")

        txtAccount.text = account!!.number.toString()
        principal.text = df.format(list6!!.dairy!!.principal.toLong())
        rate.text = String.format(Locale.getDefault(), "%.2f", list6.dairy!!.rate)
        evaluation.text = df.format(list6.evaluation.toLong())
        accountInfo.text = String.format(Locale.getDefault(), "%s %s", account.institute, account.description)

        return convertView
    }
}
