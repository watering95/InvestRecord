package com.example.watering.investrecord;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Fragment5 extends Fragment {

    private IRResolver ir;

    public Fragment5() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MainActivity mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        ir = mActivity.ir;

        final FragmentSub2 fragmentSub2 = mActivity.fragmentSub2;

        FragmentSub2.Callback callbackfromMain = new FragmentSub2.Callback() {
            @Override
            public void updateList() {

            }
        };

        fragmentSub2.setCallback5(callbackfromMain);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment5, container, false);

        initLayout();

        return mView;
    }

    private void initLayout() {

    }
}