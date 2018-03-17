package com.example.watering.investrecord.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.watering.investrecord.IRResolver;
import com.example.watering.investrecord.MainActivity;
import com.example.watering.investrecord.R;

import java.text.DecimalFormat;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class FragmentSub2 extends Fragment {

    private ViewPager mFragSub2ViewPager;

    private View mView;
    private MainActivity mActivity;
    private IRResolver ir;

    private TextView textView_spend_month;
    private TextView textView_income_month;

    interface Callback {
        void update();
    }

    private FragmentSub2.Callback m_callback3, m_callback4, m_callback5;

    public FragmentSub2() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        ir = mActivity.ir;

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_sub2_category_basic:
                setBasicCategory();
                break;
            case R.id.item_category:
                break;
            case R.id.item_card:
                break;
            default:
                UserDialogFragment dialog = UserDialogFragment.newInstance(item.getItemId(), new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String name) {
                    }
                });
                //noinspection ConstantConditions
                dialog.show(getFragmentManager(), "dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initLayout() {
        TabLayout mFragSub2TabLayout = mView.findViewById(R.id.tab_frag_sub2);

        mFragSub2TabLayout.setTabTextColors(Color.parseColor("#ffffff"),Color.parseColor("#00ff00"));
        mFragSub2TabLayout.addTab(mFragSub2TabLayout.newTab().setText(R.string.spending));
        mFragSub2TabLayout.addTab(mFragSub2TabLayout.newTab().setText(R.string.income));
        mFragSub2TabLayout.addTab(mFragSub2TabLayout.newTab().setText(R.string.statistic));
        mFragSub2TabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        textView_spend_month = mView.findViewById(R.id.textView_frag_sub2_spend_month);
        textView_income_month = mView.findViewById(R.id.textView_frag_sub2_income_month);
        updateFragSub2();

        mFragSub2ViewPager = mView.findViewById(R.id.viewpager_frag_sub2);
        FragSub2TabPagerAdapter mFragSub2PagerAdapter = new FragSub2TabPagerAdapter(getChildFragmentManager());
        mFragSub2ViewPager.setAdapter(mFragSub2PagerAdapter);
        mFragSub2ViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mFragSub2TabLayout));

        mFragSub2TabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mFragSub2ViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void setBasicCategory() {
        String income = "income";
        String spend = "spend";
        int id_main;

        ir.insertCategoryMain("식비",spend);
        id_main = ir.getCategoryMainId("식비");
        ir.insertCategorySub("식자재",id_main);
        ir.insertCategorySub("외식",id_main);
        ir.insertCategorySub("간식",id_main);

        ir.insertCategoryMain("통신비",spend);
        id_main = ir.getCategoryMainId("통신비");
        ir.insertCategorySub("통신기기구입비",id_main);
        ir.insertCategorySub("유선인터넷",id_main);
        ir.insertCategorySub("전화요금",id_main);

        ir.insertCategoryMain("교육비",spend);
        id_main = ir.getCategoryMainId("교육비");
        ir.insertCategorySub("납부금",id_main);
        ir.insertCategorySub("학원비",id_main);
        ir.insertCategorySub("준비물",id_main);
        ir.insertCategorySub("교재비",id_main);

        ir.insertCategoryMain("교통비",spend);
        id_main = ir.getCategoryMainId("교통비");
        ir.insertCategorySub("주유비",id_main);
        ir.insertCategorySub("대중교통비",id_main);
        ir.insertCategorySub("차량수리비",id_main);
        ir.insertCategorySub("자동차보험료",id_main);
        ir.insertCategorySub("자동차세",id_main);

        ir.insertCategoryMain("주거생활비",spend);
        id_main = ir.getCategoryMainId("주거생활비");
        ir.insertCategorySub("관리비",id_main);
        ir.insertCategorySub("공공요금",id_main);
        ir.insertCategorySub("수리비",id_main);
        ir.insertCategorySub("생활용품구입비",id_main);

        ir.insertCategoryMain("의류구입관리비",spend);
        id_main = ir.getCategoryMainId("의류구입관리비");
        ir.insertCategorySub("의류구입비",id_main);
        ir.insertCategorySub("신발구입비",id_main);
        ir.insertCategorySub("수선비",id_main);
        ir.insertCategorySub("세탁비",id_main);

        ir.insertCategoryMain("문화,취미활동비",spend);
        id_main = ir.getCategoryMainId("문화,취미활동비");
        ir.insertCategorySub("구입비",id_main);
        ir.insertCategorySub("입장료",id_main);

        ir.insertCategoryMain("건강유지비",spend);
        id_main = ir.getCategoryMainId("건강유지비");
        ir.insertCategorySub("병원비",id_main);
        ir.insertCategorySub("처방의약품비",id_main);
        ir.insertCategorySub("구입비",id_main);

        ir.insertCategoryMain("가족행복비",spend);
        id_main = ir.getCategoryMainId("가족행복비");
        ir.insertCategorySub("용돈지급",id_main);
        ir.insertCategorySub("여행비",id_main);

        ir.insertCategoryMain("사회활동비",spend);
        id_main = ir.getCategoryMainId("사회활동비");
        ir.insertCategorySub("모임회비",id_main);
        ir.insertCategorySub("기부금",id_main);
        ir.insertCategorySub("경조사비",id_main);

        ir.insertCategoryMain("저축,투자비",spend);
        id_main = ir.getCategoryMainId("저축,투자비");
        ir.insertCategorySub("저축",id_main);
        ir.insertCategorySub("투자",id_main);

        ir.insertCategoryMain("정기수입",income);
        id_main = ir.getCategoryMainId("정기수입");
        ir.insertCategorySub("월급",id_main);
        ir.insertCategorySub("용돈",id_main);

        ir.insertCategoryMain("상여금",income);
        id_main = ir.getCategoryMainId("상여금");
        ir.insertCategorySub("명절상여금",id_main);
        ir.insertCategorySub("기타",id_main);

        ir.insertCategoryMain("투자이윤",income);
        id_main = ir.getCategoryMainId("투자이윤");
        ir.insertCategorySub("이자",id_main);
        ir.insertCategorySub("배당",id_main);
        ir.insertCategorySub("투자수익",id_main);

        ir.insertCategoryMain("기타수입",income);
        id_main = ir.getCategoryMainId("기타수입");
        ir.insertCategorySub("기타",id_main);

        Toast.makeText(getContext(),"카테고리 초기화",Toast.LENGTH_SHORT).show();
    }

    public void setCallback3(FragmentSub2.Callback callback) {
        this.m_callback3 = callback;
    }
    public void setCallback4(FragmentSub2.Callback callback) {
        this.m_callback4 = callback;
    }
    public void setCallback5(FragmentSub2.Callback callback) {
        this.m_callback5 = callback;
    }

    public void updateFragSub2() {

        DecimalFormat df = new DecimalFormat("#,###");

        int spend_month = ir.getSpendMonth(mActivity.getToday());
        int income_month = ir.getIncomeMonth(mActivity.getToday());
        textView_income_month.setText(df.format(income_month));
        textView_spend_month.setText(df.format(spend_month));
    }
    public void callUpdateFrag3() {
        if(m_callback3 != null) {
            m_callback3.update();
        }
    }
    public void callUpdateFrag4() {
        if(m_callback4 != null) {
            m_callback4.update();
        }
    }
    public void callUpdateFrag5() {
        if(m_callback5 != null) {
            m_callback5.update();
        }
    }
}
