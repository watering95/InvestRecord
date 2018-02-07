package com.example.watering.investrecord;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Fragment6 extends Fragment {

    private View mView;
    private List6Adapter list6Adapter;
    private MainActivity mActivity;
    private IRResolver ir;
    private WebView mWeb;
    private final ArrayList<Info_List6> lists = new ArrayList<>();
    private static final String TAG = "InvestRecord";

    public Fragment6() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        ir = mActivity.ir;

        final FragmentSub3 fragmentSub3 = mActivity.fragmentSub3;

        makeHTMLFile();

        FragmentSub3.Callback callback = new FragmentSub3.Callback() {
            @Override
            public void update() {
                updateListView();

                makeHTMLFile();
                mWeb.reload();
            }
        };

        fragmentSub3.setCallback6(callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment6, container, false);

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
        ListView listView = mView.findViewById(R.id.listview_frag2_totalasset);
        list6Adapter = new List6Adapter(mView.getContext(),lists);
        listView.setAdapter(list6Adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mActivity.inoutDialog(lists.get(position).getDairy().getDate());
            }
        });

        FloatingActionButton floating = mView.findViewById(R.id.floating_frag6);
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, mActivity.fragmentSub1).commit();

            }
        });
    }

    private void updateInfoLists() {
        lists.clear();
        ArrayList<Info_Dairy> daires = (ArrayList<Info_Dairy>) ir.getInfoDaires(ir.getCurrentAccount());
        if(daires.isEmpty()) {
            Log.i(TAG,"No dairy");
            return;
        }

        for(int i = 0; i < daires.size(); i++) {
            Info_IO io;
            Info_Dairy dairy;
            Info_List6 list = new Info_List6();

            dairy = daires.get(i);

            io = ir.getInfoIO(dairy.getAccount(), dairy.getDate());
            if(io != null) list.setEvaluation(io.getEvaluation());
            list.setDairy(dairy);
            lists.add(list);
        }
    }
    private void updateListView() {
        updateInfoLists();
        list6Adapter.notifyDataSetChanged();
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
                    rate = String.format(Locale.getDefault(),"%.2f",lists.get(i).getDairy().getRate());
                    date = "new Date('" + lists.get(i).getDairy().getDate() + "')";
                    data.append("[").append(date).append(", ").append(eval).append(", ").append(rate).append("],\n");
                }
                eval = String.valueOf(lists.get(0).getEvaluation());
                rate = String.format(Locale.getDefault(),"%.2f",lists.get(0).getDairy().getRate());
                date = "new Date('" + lists.get(0).getDairy().getDate() + "')";
                data.append("[").append(date).append(", ").append(eval).append(", ").append(rate).append("]\n");
            }
            else {
                data = new StringBuilder("[0, 0, 0]\n");
            }

            String accountnumber = null;

            if(lists.isEmpty()) accountnumber = "";
            else {
                int id_account = lists.get(0).getDairy().getAccount();
                Account account = ir.getAccount(id_account);
                if(account != null) accountnumber = account.getNumber();
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
    @SuppressLint("SetJavaScriptEnabled")
    private void openWebView() {
        mWeb = mView.findViewById(R.id.webView_frag6);
        mWeb.setWebViewClient(new WebViewClient());
        WebSettings set = mWeb.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        mWeb.loadUrl("file:///" + mActivity.getFilesDir() + "graph.html");
    }
}