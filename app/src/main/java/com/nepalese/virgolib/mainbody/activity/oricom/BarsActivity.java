package com.nepalese.virgolib.mainbody.activity.oricom;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Base.BaseActivity;

/**
 *  2022/03/24
 *  seekbar:（可滑动）
 *  1. 使用：setprocess, setMax, OnSeekBarChangeListener；
 *  2. 自定义滑块颜色|样式、进度条颜色；(api>21)
 *  3. 跟随滑块移动的提示框；
 *
 *  switch,
 *  processbar:（仅后台改变）
 *  1. 横向进度条；
 *  2. 自定义进度条颜色；(api>21)
 *
 *  ratingbar：（可变化）
 *  1. 自定义颜色|样式；(api>21)
 */
public class BarsActivity extends BaseActivity {

    private View colorView;
    private TextView rValue, gValue, bValue, colorValue;
    private SeekBar barR, barG, barB;
    private int iR, iG, iB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bars);
        init();
    }

    @Override
    protected void initUI() {
        colorView = findViewById(R.id.sb_color_view);
        colorValue = findViewById(R.id.tv_color_value);
        rValue = findViewById(R.id.tv_r_value);
        gValue = findViewById(R.id.tv_g_value);
        bValue = findViewById(R.id.tv_b_value);
        barR = findViewById(R.id.bar_r);
        barG = findViewById(R.id.bar_g);
        barB = findViewById(R.id.bar_b);
    }

    @Override
    protected void initData() {
        setSeekBar();
        setProcessBar();
        setRatingBar();
    }

    private void setSeekBar() {
//        1. 使用：setprocess, setMax, OnSeekBarChangeListener；
        barR.setMax(255);
        barR.setProgress(0);
        barG.setMax(255);
        barG.setProgress(0);
        barB.setMax(255);
        barB.setProgress(0);

        /**
         * 2. 自定义滑块颜色|样式、进度条颜色； api>21
         * xml:
         * android:progressTint="@color/colorGreen"//进度条颜色 默认：colorSecondary
         * android:thumbTint="@color/colorGreen"//滑块颜色 默认： colorSecondary
         *
         * android:maxHeight="2dp"//防止滑块变形
         * android:thumb="@drawable/ic_thumb_bike"//滑块样式
         *
         * //自定义进度条前景、背景色
         * android:progressDrawable="@drawable/seekbar_progress_stytle"
         *
         * seekbar_progress_stytle：
         * <layer-list xmlns:android="http://schemas.android.com/apk/res/android">
         *     <item android:id="@android:id/background"> <!--进度条背景颜色-->
         *         <shape>
         *             <corners android:radius="1dp"/>
         *             <solid android:color="@color/colorTY50"/>
         *         </shape>
         *     </item>
         *
         *     <item android:id="@android:id/secondaryProgress"> <!--滑块颜色: 若另设置样式，则被覆盖-->
         *         <clip>
         *             <shape>
         *                 <corners android:radius="1dp"/>
         *                 <solid android:color="@color/colorRed" />
         *             </shape>
         *         </clip>
         *     </item>
         *
         *     <item android:id="@android:id/progress"> <!--进度条颜色:前景色-->
         *         <clip>
         *             <shape>
         *                 <corners android:radius="1dp"/>
         *                 <gradient
         *                     android:startColor="#4facfe"
         *                     android:endColor="#00f2fe"/>
         *             </shape>
         *         </clip>
         *     </item>
         * </layer-list>
         *
         */


//        3. 跟随滑块移动的提示框；

    }

    private void setProcessBar() {
        //默认：圆形，一直转动
        //横向：
//        xml: style="?android:attr/progressBarStyleHorizontal"


        //设置进度值，颜色与seekbar一样
//        pb.setMax(255);
//        pb.setProgress(0);

//        android:progressTint="@color/colorRed" //进度前景颜色
//        android:progressBackgroundTint="@color/colorGray" //进度背景色
//        android:progressDrawable="@drawable/seekbar_progress_stytle" //自定义前、后景色，需设置高度（默认：4dp）
    }

    private void setRatingBar() {
        // android:progressTint="@color/colorTY"
        // android:progressBackgroundTint="@color/colorTY30"
        // android:rating="2" //当前确认值
        // android:numStars="5" //最大值
        // android:stepSize="1" //最小单位， 可小数（0.5）

        /**
         * 自定义样式：
         *  style="@style/VirgoRateBar"
         *
         *  styles->VirgoRateBar：
         *     <style name="VirgoRateBar" parent="Widget.AppCompat.RatingBar">
         *         <item name="android:progressDrawable">@drawable/ratebar_style</item>
         *         <item name="android:maxHeight">32dp</item>
         *         <item name="android:minHeight">32dp</item>
         *     </style>
         *
         *  drawable->ratebar_style：
         *  <?xml version="1.0" encoding="utf-8"?>
         * <layer-list xmlns:android="http://schemas.android.com/apk/res/android">
         *     <item android:id="@android:id/background"
         *         android:drawable="@drawable/icon_moon"/> //底部图
         *     <item android:id="@android:id/secondaryProgress"
         *         android:drawable="@drawable/icon_moon_half"/> //半选中图 -> 分阶决定可实现最小单位
         *     <item android:id="@android:id/progress"
         *         android:drawable="@drawable/icon_moon_full"/> //全选图
         * </layer-list>
         */

    }

    @Override
    protected void setListener() {
        barR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                iR = seekBar.getProgress();
                rValue.setText(String.valueOf(iR));
                Rendering(iR, iG, iB);
            }
        });

        barG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                iG = seekBar.getProgress();
                gValue.setText(String.valueOf(iG));
                Rendering(iR, iG, iB);
            }
        });

        barB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                iB = seekBar.getProgress();
                bValue.setText(String.valueOf(iB));
                Rendering(iR, iG, iB);
            }
        });

    }

    private void Rendering(int r, int g, int b){
        colorValue.setText(String.format(getString(R.string.bar_color_value),
                Integer.toHexString(r), Integer.toHexString(g), Integer.toHexString(b)));
        GradientDrawable back=(GradientDrawable) colorView.getBackground();
        back.setColor(Color.rgb(r,g,b));
    }

    @Override
    protected void release() {

    }

    @Override
    protected void onBack() {
        finish();
    }
}