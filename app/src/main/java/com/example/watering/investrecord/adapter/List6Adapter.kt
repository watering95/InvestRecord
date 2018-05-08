package com.example.watering.investrecord.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.example.watering.investrecord.info.InfoDairyKRW
import com.example.watering.investrecord.info.InfoList6
import com.example.watering.investrecord.R

import java.text.DecimalFormat
import java.util.ArrayList
import java.util.Locale

/**
 * Created by watering on 17. 11. 17.
 */

class List6Adapter(context: Context, private val mData: ArrayList<InfoList6>) : BaseAdapter() {
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
            convertView = inflater.inflate(R.layout.layout_list6, parent, false)
        }

        val dairy_krw: InfoDairyKRW?
        val date = convertView!!.findViewById<TextView>(R.id.textView_layout_list2_1)
        val principal = convertView.findViewById<TextView>(R.id.textView_layout_list2_2)
        val evaluation = convertView.findViewById<TextView>(R.id.textView_layout_list2_3)
        val rate = convertView.findViewById<TextView>(R.id.textView_layout_list2_4)
        val df = DecimalFormat("#,###")

        dairy_krw = mData[position].dairy_krw
        date.text = dairy_krw!!.date.toString()
        principal.text = df.format(dairy_krw.principal.toLong())
        rate.text = String.format(Locale.getDefault(), "%.2f", dairy_krw.rate)
        evaluation.text = df.format(mData[position].evaluation.toLong())

        return convertView
    }
}
