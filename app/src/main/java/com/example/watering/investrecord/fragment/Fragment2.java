package com.example.watering.investrecord.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.watering.investrecord.data.Account;
import com.example.watering.investrecord.IRResolver;
import com.example.watering.investrecord.info.InfoDairyKRW;
import com.example.watering.investrecord.info.InfoIOKRW;
import com.example.watering.investrecord.MainActivity;
import com.example.watering.investrecord.R;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;


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
    private int duration = 7;
    private int interval = 1;

    public Fragment2() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        ir = mActivity.ir;

//        makeHTMLFile();

        final FragmentSub1 fragmentSub1 = mActivity.fragmentSub1;

        FragmentSub1.Callback callback = new FragmentSub1.Callback() {
            @Override
            public void update() {
                makeHTMLFile();
                mWeb.reload();
            }
        };

        fragmentSub1.setCallback2(callback);
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
        Button buttonWeek = mView.findViewById(R.id.button_frag2_due_week_1);
        Button buttonMonth = mView.findViewById(R.id.button_frag2_due_month_1);
        Button buttonYear = mView.findViewById(R.id.button_frag2_due_year_1);
        Button buttonFull = mView.findViewById(R.id.button_frag2_due_full);

        buttonWeek.setOnClickListener(listener);
        buttonMonth.setOnClickListener(listener);
        buttonYear.setOnClickListener(listener);
        buttonFull.setOnClickListener(listener);
    }

    private final Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.button_frag2_due_week_1:
                    duration = 7;
                    interval = 1;
                    break;
                case R.id.button_frag2_due_month_1:
                    duration = 30;
                    interval = 1;
                    break;
                case R.id.button_frag2_due_year_1:
                    duration = 365;
                    interval = 7;
                    break;
                case R.id.button_frag2_due_full:
                    duration = -1;
                    interval = 10;
                    break;
            }
            makeHTMLFile();
            mWeb.reload();
        }
    };

    private void makeHTMLFile() {
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(mActivity.getFilesDir() + "graph_total.html",false));

            List<Account> accounts = ir.getAccounts(ir.getCurrentGroup());

            StringBuilder data = new StringBuilder();
            String strToday = mActivity.getToday();
            String strDate = strToday;
            Calendar firstDate = mActivity.strToCalendar(ir.getFirstDate());
            InfoIOKRW io_krw;
            InfoDairyKRW dairy_krw;

            int i = 0, sumEvaluation = 0, sumPrincipal = 0;
            double rate = 0;
            do {
                // 특정일의 합계와 평가액 계산
                for (int index = 0, n = accounts.size(); index < n; index++) {
                    dairy_krw = ir.getLastInfoDairyKRW(accounts.get(index).getId(), strDate);
                    io_krw = ir.getLastInfoIOKRW(accounts.get(index).getId(), strDate);
                    if(io_krw != null) {
                        sumEvaluation += io_krw.getEvaluation();
                        sumPrincipal += dairy_krw.getPrincipal();
                    }
                }
                // 특정일의 수익율 계산
                if(sumPrincipal != 0 && sumEvaluation != 0) rate = (double)sumEvaluation / (double)sumPrincipal * 100 - 100;
                // 특정일 데이터 추가
                data.append("[").append("new Date('").append(strDate).append("')").append(", ").append(sumEvaluation).append(", ").append(rate).append("],\n");
                // 초기화
                sumEvaluation = 0;
                sumPrincipal = 0;
                rate = 0;
                i++;
                // 날짜 변경
                if(duration >= 0 && i > duration) break;
                strDate = mActivity.dateChange(strToday, -i*interval);
            } while(mActivity.strToCalendar(strDate).compareTo(firstDate) > 0);

            data.delete(data.length()-2,data.length()-1);

            Log.i(TAG, String.format("%s",data));

            String function = "function drawChart() {\n"
                    + "var chartDiv = document.getElementById('chart_div');\n\n"

                    + "var data = new google.visualization.DataTable();\n"
                    + "data.addColumn('date','Day');\n"
                    + "data.addColumn('number','평가액');\n"
                    + "data.addColumn('number','수익율');\n"
                    + "data.addRows([\n" + data + "]);\n\n"

                    + "var options = {"
                    + "series: {0: {targetAxisIndex: 0}, 1: {targetAxisIndex: 1}},"
                    + "vAxes: {0: {title: '평가액'}, 1: {title: '수익율'}},"
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
        mWeb = mView.findViewById(R.id.webView_frag2);
        mWeb.setWebViewClient(new WebViewClient());
        WebSettings set = mWeb.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        mWeb.loadUrl("file:///" + mActivity.getFilesDir() + "graph_total.html");
    }
}
