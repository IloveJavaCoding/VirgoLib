package com.nepalese.virgolib.mainbody.activity.thirdlib;

import android.os.Bundle;

import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Base.BaseActivity;

/**
 * 统计图表
 * implementation 'com.github.PhilJay:MPAndroidChart:v3.0.1'
 */
public class ChartsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        initUI();
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void release() {

    }

    @Override
    protected void onBack() {
        finish();
    }
}