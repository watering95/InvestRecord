package com.example.watering.investrecord.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.watering.investrecord.IRResolver;
import com.example.watering.investrecord.data.CategoryMain;
import com.example.watering.investrecord.info.*;
import com.example.watering.investrecord.adapter.*;
import com.example.watering.investrecord.MainActivity;
import com.example.watering.investrecord.R;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Fragment6 extends Fragment {

    private View mView;
    private WebView mWeb;
    private MainActivity mActivity;
    private IRResolver ir;
    private List6Adapter listAdapter;
    private final ArrayList<InfoList6> lists = new ArrayList<>();
    private static final String TAG = "InvestRecord";
    private String strDate = new String();

    public Fragment6() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        ir = mActivity.ir;

        final FragmentSub2 fragmentSub2 = mActivity.fragmentSub2;

        FragmentSub2.Callback callback = new FragmentSub2.Callback() {
            @Override
            public void update() {
                updateInfoLists();
                listAdapter.notifyDataSetChanged();
                makeHTMLFile();
                mWeb.reload();
            }
        };

        strDate = mActivity.getToday();

        fragmentSub2.setCallback6(callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment6, container, false);

        updateInfoLists();
        openWebView();
        initLayout();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        makeHTMLFile();
        mWeb.reload();
    }

    private void initLayout() {
        ListView listView = mView.findViewById(R.id.listview_frag6);
        listAdapter = new List6Adapter(mView.getContext(), lists);
        if(lists.size() != 0) {
            listView.setAdapter(listAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strDate = lists.get(position).getMonth() + "-01";

                makeHTMLFile();
                mWeb.reload();;
            }
        });

        makeHTMLFile();
        mWeb.reload();;
    }

    @SuppressLint("DefaultLocale")
    private void updateInfoLists() {
        int income, spend;
        String txtMonth, txtDate;

        InfoList6 list5;

        Calendar date = mActivity.strToCalendar(mActivity.getToday());
        txtDate = mActivity.calendarToStr(date);

        lists.clear();

        for(int i = 0; i < 12; i++) {
            list5 = new InfoList6();

            txtMonth = String.format("%04d-%02d", date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1);
            income = ir.getIncomeMonth(txtDate);
            spend = ir.getSpendMonth(txtDate);

            list5.setMonth(txtMonth);
            list5.setIncome(income);
            list5.setSpend(spend);
            lists.add(list5);

            txtDate = mActivity.monthChange(txtDate, -1);
            date = mActivity.strToCalendar(txtDate);
        }
    }

    private void makeHTMLFile() {
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(mActivity.getFilesDir() + "graph_frag6.html",false));

            StringBuilder data = new StringBuilder();
            int sum;

            List<CategoryMain> categoryMains = ir.getCategoryMains(1);
            for(int i = 0, limit = categoryMains.size(); i < limit; i++){
                CategoryMain categoryMain = categoryMains.get(i);
                sum = ir.getSpendsCategorySum(categoryMain.getId(), strDate);
                if(sum > 0) data.append("['").append(categoryMain.getName()).append("', ").append(sum).append("],\n");
            }
            data.delete(data.length()-2,data.length()-1);

            Log.i(TAG, String.format("%s",data));

            String function = "function drawChart() {\n"
                    + "var chartDiv = document.getElementById('chart_div');\n\n"

                    + "var data = new google.visualization.arrayToDataTable([\n"
                    + "['Category', 'Sum'],\n"
                    + data
                    + "]);\n\n"

                    + "var options = {"
                    + "title: " + new StringBuilder().append("'").append(strDate).delete(strDate.length()-2,strDate.length()+1).append(" 지출'")
                    + "};\n\n"

                    + "var chart = new google.visualization.PieChart(chartDiv);\n"
                    + "chart.draw(data, options);\n"

                    + "}\n";

            String script = "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n"
                    + "<script type=\"text/javascript\">\n"
                    + "google.charts.load('current', {'packages':['corechart']});\n"
                    + "google.charts.setOnLoadCallback(drawChart);\n"
                    + function
                    + "</script>\n";

            String body = "<div id=\"chart_div\" align=\"center\"></div>\n";

            String html = "<!DOCTYPE html>\n" + "<head>\n" + script + "</head>\n" + "<body>\n" + body + "</body>\n" + "</html>";

            Log.i(TAG, String.format("%s",html));

            bw.write(html);
            bw.close();
        } catch (IOException e) {
            Toast.makeText(mActivity.getApplicationContext(), R.string.toast_htmlfile, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void openWebView() {
        mWeb = mView.findViewById(R.id.webView_frag6);
        mWeb.setWebViewClient(new WebViewClient());
        WebSettings set = mWeb.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        mWeb.loadUrl("file://" + mActivity.getFilesDir() + "graph_frag6.html");
    }
}