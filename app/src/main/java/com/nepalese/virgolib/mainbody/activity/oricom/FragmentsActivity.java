package com.nepalese.virgolib.mainbody.activity.oricom;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.mainbody.fragments.FragmentGridView;
import com.nepalese.virgolib.mainbody.fragments.FragmentListView;
import com.nepalese.virgolib.mainbody.fragments.FragmentRecycleView;
import com.nepalese.virgosdk.Base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * fragment:
 * 1. radiogruop：导航栏
 * 2. viewpager + list<Fragment>：页面切换
 */
public class FragmentsActivity extends BaseActivity {
    private static final String TAG = "FragmentsActivity";

    private RadioGroup radioGroup;
    private ViewPager viewPager;
    private List<Fragment> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);
        init();
    }

    @Override
    protected void initUI() {
        radioGroup = findViewById(R.id.rg_fragment1);
        viewPager = findViewById(R.id.vp_container1);
    }

    @Override
    protected void initData() {
        views = new ArrayList<>();
        views.add(new FragmentListView());
        views.add(new FragmentGridView());
        views.add(new FragmentRecycleView());

        //设置页面切换适配器
        viewPager.setAdapter(pagerAdapter);
        //默认显示页
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void setListener() {
        //顶部导航栏 切换监听
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);

        //viewpager 监听页面变化
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    protected void release() {
        views.clear();
        views = null;
    }

    @Override
    protected void onBack() {
        finish();
    }

    private final PagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return views.get(position);
        }

        @Override
        public int getCount() {
            return views.size();
        }
    };

    private final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.i(TAG, "onPageScrolled: " + position);
        }

        @Override
        public void onPageSelected(int position) {
//            Log.i(TAG, "onPageSelected: " + position);
            //页面滑动变化改变对应的导航栏
            switch (position){
                case 0:
                    radioGroup.check(R.id.radio_listview);
                    break;
                case 1:
                    radioGroup.check(R.id.radio_gridview);
                    break;
                case 2:
                    radioGroup.check(R.id.radio_recycleview);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            Log.i(TAG, "onPageScrollStateChanged: " + state);
        }
    };

    private final RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            Log.i(TAG, "onCheckedChanged: " + checkedId);
            switch (checkedId){
                case R.id.radio_listview:
                    viewPager.setCurrentItem(0, true);
                    break;
                case R.id.radio_gridview:
                    viewPager.setCurrentItem(1, true);
                    break;
                case R.id.radio_recycleview:
                    viewPager.setCurrentItem(2, true);
                    break;
            }
        }
    };
}