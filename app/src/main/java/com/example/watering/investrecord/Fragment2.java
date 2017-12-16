package com.example.watering.investrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
public class Fragment2 extends Fragment {

    private View mView;
    private List2Adapter list2Adapter;
    private MainActivity mActivity;
    private IRResolver ir;
    private WebView mWeb;
    private final ArrayList<Info_List2> lists = new ArrayList<>();

    public Fragment2() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        final FragmentSub1 fragmentSub1 = mActivity.fragmentSub1;

        makeHTMLFile();

        FragmentSub1.Callback callbackfromMain = new FragmentSub1.Callback() {
            @Override
            public void updateList() {
                updateListView();

                makeHTMLFile();
                mWeb.reload();
                fragmentSub1.CallUpdate4();
            }
        };
        fragmentSub1.setCallback2(callbackfromMain);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment2, container, false);

        initLayout();
        updateListView();
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
        ListView listView = mView.findViewById(R.id.listview_totalasset_frag2);
        list2Adapter = new List2Adapter(mView.getContext(),lists);
        listView.setAdapter(list2Adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mActivity.inoutDialog(lists.get(position).getDairy().getDate());
            }
        });
    }

    private void updateInfoLists() {
        lists.clear();
        ArrayList<Info_Dairy> daires = (ArrayList<Info_Dairy>) ir.getInfoDaires();
        for(int i = 0; i < daires.size(); i++) {
            Info_IO io;
            Info_Dairy dairy;
            Info_List2 list = new Info_List2();

            dairy = daires.get(i);

            io = ir.getInfoIO(dairy.getAccount(), dairy.getDate());
            list.setEvaluation(io.getEvaluation());
            list.setDairy(dairy);
            lists.add(list);
        }
    }
    private void updateListView() {
        updateInfoLists();
        list2Adapter.notifyDataSetChanged();
    }

    private void makeHTMLFile() {
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(mActivity.getFilesDir() + "graph.html",false));

            StringBuilder data = new StringBuilder();
            String date;
            int size = lists.size();
            String eval,rate;

            if(size != 0) {
                for (int i = size - 1; i > 0; i--) {
                    eval = String.valueOf(lists.get(i).getEvaluation());
                    rate = String.format("%.2f",lists.get(i).getDairy().getRate());
                    date = "new Date('" + lists.get(i).getDairy().getDate() + "')";
                    data.append("[").append(date).append(", ").append(eval).append(", ").append(rate).append("],\n");
                }
                eval = String.valueOf(lists.get(0).getEvaluation());
                rate = String.format("%.2f",lists.get(0).getDairy().getRate());
                date = "new Date('" + lists.get(0).getDairy().getDate() + "')";
                data.append("[").append(date).append(", ").append(eval).append(", ").append(rate).append("]\n");
            }
            else {
                data = new StringBuilder("[0, 0, 0]\n");
            }

            String accountnumber;

            if(lists.isEmpty()) accountnumber = "";
            else {
                int id = lists.get(0).getDairy().getAccount();
                Account account = ir.getAccount(String.valueOf(id));
                accountnumber = account.getNumber();
            }

            String function = "function drawChart() {\n"
                    + "var chartDiv = document.getElementById('chart_div');\n\n"

                    + "var data = new google.visualization.DataTable();\n"
                    + "data.addColumn('date','Day');\n"
                    + "data.addColumn('number','평가액');\n"
                    + "data.addColumn('number','수익률');\n"
                    + "data.addRows([\n" + data + "]);\n\n"

                    + "var options = {"
                    + "title: '" + accountnumber +"',"
                    + "series: {0: {targetAxisIndex: 0}, 1: {targetAxisIndex: 1}},"
                    + "vAxes: {0: {title: '평가액'}, 1: {title: '수익률'}},"
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
        mWeb = mView.findViewById(R.id.web);
        mWeb.setWebViewClient(new WebViewClient());
        WebSettings set = mWeb.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        mWeb.loadUrl("file:///" + mActivity.getFilesDir() + "graph.html");
    }
}