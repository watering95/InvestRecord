package com.example.watering.investrecord.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.example.watering.investrecord.info.InfoList1
import com.example.watering.investrecord.R

import java.text.DecimalFormat
import java.util.ArrayList
import java.util.Locale

/**
 * Created by watering on 17. 11. 17.
 */

class List1Adapter(context: Context, private val mData: ArrayList<InfoList1>) : BaseAdapter() {
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
            convertView = inflater.inflate(R.layout.layout_list1, parent, false)
        }

        val viewAccount = convertView!!.findViewById<TextView>(R.id.textView_layout_list1_1)
        val viewPrincipal = convertView.findViewById<TextView>(R.id.textView_layout_list1_2)
        val viewEvaluation = convertView.findViewById<TextView>(R.id.textView_layout_list1_3)
        val viewRateProfit = convertView.findViewById<TextView>(R.id.textView_layout_list1_4)
        val viewAccountInfo = convertView.findViewById<TextView>(R.id.textView_layout_list1_5)
        val viewRateTotal = convertView.findViewById<TextView>(R.id.textView_layout_list1_6)

        val list6 = mData[position].infoList7
        val dairyTotal = list6!!.dairyTotal
        val account = mData[position].account
        val df = DecimalFormat("#,###")
        val total = mData[count-1].total
        val evaluation = dairyTotal!!.evaluation.toLong()

        viewAccount.text = account!!.number.toString()
        viewPrincipal.text = df.format(dairyTotal.principal.toLong())
        viewRateProfit.text = String.format(Locale.getDefault(), "%.2f", dairyTotal.rate)
        viewEvaluation.text = df.format(evaluation)
        viewAccountInfo.text = String.format(Locale.getDefault(), "%s %s", account.institute, account.description)
        viewRateTotal.text = String.format(Locale.getDefault(), "%.2f", evaluation.toFloat()/total.toFloat()*100.0) + "%"

        return convertView
    }
}
