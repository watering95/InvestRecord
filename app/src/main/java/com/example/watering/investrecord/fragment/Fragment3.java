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

import com.example.watering.investrecord.IRResolver;
import com.example.watering.investrecord.MainActivity;
import com.example.watering.investrecord.R;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Fragment3 extends Fragment {

    private MainActivity mActivity;
    private IRResolver ir;
    private View mView;
    private WebView mWeb;
    private static final String TAG = "InvestRecord";
    private String function = "<script type=\"text/javascript\" src=\"http://watering.iptime.org:3000/script/graph_1month.js\"></script>\n";

    public Fragment3() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        ir = mActivity.ir;

        final FragmentSub1 fragmentSub1 = mActivity.fragmentSub1;

        FragmentSub1.Callback callback = new FragmentSub1.Callback() {
            @Override
            public void update() {
                makeHTMLFile(function);
                mWeb.reload();
            }
        };

        fragmentSub1.setCallback3(callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment3, container, false);

        initLayout();
        openWebView();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        makeHTMLFile(function);
        mWeb.reload();
    }

    private void initLayout() {
        Button buttonWeek = mView.findViewById(R.id.button_frag3_due_month_1);
        Button buttonMonth = mView.findViewById(R.id.button_frag3_due_month_3);
        Button buttonYear = mView.findViewById(R.id.button_frag3_due_year_1);
        Button buttonFull = mView.findViewById(R.id.button_frag3_due_year_3);

        buttonWeek.setOnClickListener(listener);
        buttonMonth.setOnClickListener(listener);
        buttonYear.setOnClickListener(listener);
        buttonFull.setOnClickListener(listener);
    }

    private final Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.button_frag3_due_month_1:
                    function = "<script type=\"text/javascript\" src=\"http://watering.iptime.org:3000/script/graph_1month.js\"></script>\n";
                    break;
                case R.id.button_frag3_due_month_3:
                    function = "<script type=\"text/javascript\" src=\"http://watering.iptime.org:3000/script/graph_3month.js\"></script>\n";
                    break;
                case R.id.button_frag3_due_year_1:
                    function = "<script type=\"text/javascript\" src=\"http://watering.iptime.org:3000/script/graph_1year.js\"></script>\n";
                    break;
                case R.id.button_frag3_due_year_3:
                    function = "<script type=\"text/javascript\" src=\"http://watering.iptime.org:3000/script/graph_3year.js\"></script>\n";
                    break;
            }
            makeHTMLFile(function);
            mWeb.reload();
        }
    };

    private void makeHTMLFile(String function) {
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(mActivity.getFilesDir() + "graph_frag3.html",false));

            String script = "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n"
                + function;

            String body = "<div id=\"chart1\"></div>\n" + "<div id=\"chart2\"></div>\n";

            String html = "<!DOCTYPE html>\n" + "<head>\n" + script + "</head>\n" + "<body>\n" + body + "</body>\n" + "</html>";
            Log.i(TAG, String.format("%s",html));
            bw.write(html);
            bw.close();
        } catch (IOException e) {
            Toast.makeText(mActivity.getApplicationContext(), R.string.toast_htmlfile, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void openWebView() {
        mWeb = mView.findViewById(R.id.webView_frag3);
        mWeb.setWebViewClient(new WebViewClient());
        WebSettings set = mWeb.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        mWeb.loadUrl("file:///" + mActivity.getFilesDir() + "graph_frag3.html");
    }
}
