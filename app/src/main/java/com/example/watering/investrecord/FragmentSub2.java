package com.example.watering.investrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
public class FragmentSub2 extends Fragment {

    private View mView;
    private IRResolver ir;

    public FragmentSub2() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MainActivity mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sub2, container, false);

        initLayout();

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_toolbar_sub2,menu);
    }

    private void initLayout() {

    }
}
