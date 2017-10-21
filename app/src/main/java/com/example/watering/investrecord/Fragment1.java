package com.example.watering.investrecord;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by watering on 17. 10. 21.
 */

public class Fragment1 extends Fragment {
    public static Fragment1 newInstance() {
        Bundle args = new Bundle();

        Fragment1 frag = new Fragment1();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View linear = inflater.inflate(R.layout.fragment1, container, false);
        return linear;
    }
}
