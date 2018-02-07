package com.example.watering.investrecord;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Fragment2 extends Fragment {

    private MainActivity mActivity;
    private IRResolver ir;
    private View mView;
    private WebView mWeb;
    private static final String TAG = "InvestRecord";

    public Fragment2() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        ir = mActivity.ir;

        makeHTMLFile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment2, container, false);

        initLayout();
        openWebView();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        makeHTMLFile();
        mWeb.reload();
    }

    private void initLayout() {
        DecimalFormat df = new DecimalFormat("#,###");

    }

    private void makeHTMLFile() {
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(mActivity.getFilesDir() + "graph_total.html",false));

            List<Account> accounts = ir.getAccounts(ir.getCurrentGroup());
            Map<String, Integer> dataMap = new HashMap<>();

            StringBuilder data = new StringBuilder();
            String today = mActivity.getToday();
            String date = today;
            Info_IO io;

            dataMap.clear();

            int i = 0, sum = 0;
            do {
                for (int index = 0; index < accounts.size(); index++) {
                    io = ir.getLatestInfoIO(accounts.get(index).getId(), date);
                    if(io != null) {
                        sum += io.getEvaluation();
                    }
                    else {
                        break;
                    }
                }
                if(i < 100) {
                    dataMap.put(date,sum);
                    sum = 0;
                    i++;
                    date = mActivity.dateChange(today, -i);
                }
            } while(i < 100);

            Set<String> set = dataMap.keySet();
            Iterator<String> iterator = set.iterator();
            while(iterator.hasNext()) {
                String key = iterator.next();
                int value = dataMap.get(key);
                data.append("[").append("new Date('").append(key).append("')").append(", ").append(value).append("],\n");
            }
            data.delete(data.length()-2,data.length()-1);

            String function = "function drawChart() {\n"
                    + "var chartDiv = document.getElementById('chart_div');\n\n"

                    + "var data = new google.visualization.DataTable();\n"
                    + "data.addColumn('date','Day');\n"
                    + "data.addColumn('number','평가액');\n"
                    + "data.addRows([\n" + data + "]);\n\n"

                    + "var options = {"
                    + "series: {0: {targetAxisIndex: 0}},"
                    + "vAxes: {0: {title: '평가액'}},"
                    + "};\n\n"

                    + "var chart = new google.visualization.LineChart(chartDiv);\n"
                    + "chart.draw(data, options);\n"

                    + "}\n";

            String script = "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n"
                    + "<script type=\"text/javascript\">\n"
                    + "google.charts.load('current', {'packages':['line', 'corechart']});\n"
                    + "google.charts.setOnLoadCallback(drawChart);\n"
                    + function
                    + "</script>\n";

            String body = "<div id=\"chart_div\"></div>\n";

            String html = "<!DOCTYPE html>\n" + "<head>\n" + script + "</head>\n" + "<body>\n" + body + "</body>\n" + "</html>";

            bw.write(html);
            bw.close();
        } catch (IOException e) {
            Toast.makeText(mActivity.getApplicationContext(), R.string.toast_htmlfile, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void openWebView() {
        mWeb = mView.findViewById(R.id.webView_frag2);
        mWeb.setWebViewClient(new WebViewClient());
        WebSettings set = mWeb.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        mWeb.loadUrl("file:///" + mActivity.getFilesDir() + "graph_total.html");
    }
}
