package com.nepalese.virgolib.mainbody.activity.thirdlib;

import android.os.Bundle;
import android.view.View;

import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Base.BaseActivity;

/**
 * pdf 阅读器
 * implementation 'com.github.barteksc:android-pdf-viewer:2.8.0'
 */
public class PDFViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_view);
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

    public void onImportFile(View view) {
    }

    public void onJumpPage(View view) {
    }

}