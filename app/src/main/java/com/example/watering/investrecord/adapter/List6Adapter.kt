package com.example.watering.investrecord.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.example.watering.investrecord.info.InfoList6
import com.example.watering.investrecord.R

import java.text.DecimalFormat
import java.util.ArrayList

/**
 * Created by watering on 17. 11. 17.
 */

class List6Adapter(context: Context, private val mData: ArrayList<InfoList6>) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

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
            convertView = inflater.inflate(R.layout.layout_list6, parent, false)
        }

        val txtMonth = convertView!!.findViewById<TextView>(R.id.textView_layout_list6_1)
        val txtIncome = convertView.findViewById<TextView>(R.id.textView_layout_list6_2)
        val txtSpend = convertView.findViewById<TextView>(R.id.textView_layout_list6_3)

        val list5 = mData[position]
        val df = DecimalFormat("#,###")

        txtMonth.text = list5.month
        txtIncome.text = df.format(list5.income.toLong())
        txtSpend.text = df.format(list5.spend.toLong())

        return convertView
    }
}
