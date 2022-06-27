package com.nepalese.virgolib.mainbody.activity.selfcom;

import android.os.Bundle;
import android.view.View;

import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Util.SystemUtil;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 1. 当文本高度超出容器高度时，自动滚动到最新的内容的文本控件；
 * 2. 颜色画板；
 * 3. 时钟：电子，转盘；
 * 4. 渐变；
 * 5. 图片切换；
 */
public class SelfComponentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_component);
    }

    //文本
    public void onTextSpace(View view) {

    }

    //颜色画板
    public void onPickColor(View view) {
    }

    //时钟：电子，转盘
    public void onClock(View view) {
    }

    //渐变
    public void onGradient(View view) {
    }

    //图片切换
    public void onImgAnim(View view) {
        SystemUtil.jumActivity(this, ImageAnimActivity.class);
    }
}