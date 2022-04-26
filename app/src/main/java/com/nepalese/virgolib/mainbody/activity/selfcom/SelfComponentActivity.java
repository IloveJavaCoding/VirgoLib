package com.nepalese.virgolib.mainbody.activity.selfcom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nepalese.virgolib.R;

/**
 * 1. 当文本高度超出容器高度时，自动滚动到最新的内容的文本控件；
 * 2. 颜色画板；
 * 3. 时钟：电子，转盘；
 * 4. 渐变；
 * 5.
 */
public class SelfComponentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_component);
    }
}