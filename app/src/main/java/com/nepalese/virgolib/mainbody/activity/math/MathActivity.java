package com.nepalese.virgolib.mainbody.activity.math;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.nepalese.virgolib.R;

/**
 * 1. 计算器
 * 2. 算法：斐波那契数列，阶乘，质数
 * 3. 汉莫尼塔
 * 4. 中国剩余定理
 * 5. 随机数生成器
 */
public class MathActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
    }

    //计算器
    public void onCalculate(View view) {
    }

    //算法：斐波那契数列，阶乘，质数
    public void onAlgorithm(View view) {
    }

    //汉莫尼塔
    public void onHamoni(View view) {
    }

    //中国剩余定理
    public void onCRT(View view) {
    }

    //随机数生成器
    public void onRandom(View view) {
    }
}