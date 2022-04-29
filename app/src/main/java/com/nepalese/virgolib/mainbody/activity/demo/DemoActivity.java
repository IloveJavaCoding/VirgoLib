package com.nepalese.virgolib.mainbody.activity.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.mainbody.activity.oricom.TextViewActivity;
import com.nepalese.virgosdk.Util.SystemUtil;

/**
 * 1. 截屏；
 * 2. 全屏走字；
 * 3. 相册；
 * 4. 文字时钟；
 * 5. 和风天气；
 */
public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    public void onScreenCap(View view) {
        SystemUtil.jumActivity(this, TextViewActivity.class);
    }

    public void onScrollText(View view) {

    }

    public void onAlbum(View view) {

    }

    public void onTextClock(View view) {

    }
}