package com.nepalese.virgolib.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Administrator on 2022/3/10.
 * Usage:
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init(){
        initUI();
        initData();
        setListener();
    }

    protected abstract void initUI();

    protected abstract void initData();

    protected abstract void setListener();

}
