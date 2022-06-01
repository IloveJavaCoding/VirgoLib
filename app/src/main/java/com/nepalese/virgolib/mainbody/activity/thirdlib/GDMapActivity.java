package com.nepalese.virgolib.mainbody.activity.thirdlib;

import android.os.Bundle;

import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Base.BaseActivity;

public class GDMapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_d_map);
        init();
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