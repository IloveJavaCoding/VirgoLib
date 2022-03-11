package com.nepalese.virgolib.mainbody.activity.network;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nepalese.virgolib.R;

/**
 * 1. 网络爬虫
 * 2. http通讯
 * 3. websocket通讯
 * 4. multicastsocket 组播
 */
public class NetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
    }
}