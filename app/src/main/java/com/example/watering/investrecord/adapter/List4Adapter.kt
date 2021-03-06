package com.example.watering.investrecord.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.example.watering.investrecord.info.InfoList4
import com.example.watering.investrecord.R

import java.text.DecimalFormat
import java.util.ArrayList

/**
 * Created by watering on 17. 11. 17.
 */

class List4Adapter(context: Context, private val mData: ArrayList<InfoList4>) : BaseAdapter() {
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
            convertView = inflater.inflate(R.layout.layout_list4, parent, false)
        }

        val df = DecimalFormat("#,###")
        val textView1 = convertView!!.findViewById<TextView>(R.id.textView_layout_list4_1)
        val textView2 = convertView.findViewById<TextView>(R.id.textView_layout_list4_2)
        val textView3 = convertView.findViewById<TextView>(R.id.textView_layout_list4_3)

        textView1.text = mData[position].spend!!.details
        textView3.text = df.format(mData[position].spend!!.amount.toLong())

        return convertView
    }
}
