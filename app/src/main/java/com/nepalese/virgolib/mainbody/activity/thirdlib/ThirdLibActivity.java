package com.nepalese.virgolib.mainbody.activity.thirdlib;

import android.os.Bundle;
import android.view.View;

import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Util.SystemUtil;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 1. 定位，地图；
 * 2. pdf 阅读器；
 * 3. 二维码生成及扫描；
 * 4. 和风天气；
 * 5. 视频播放器；
 * 6. 日历；
 * 7. Glide 图片加载；
 * 8. 统计图表
 */
public class ThirdLibActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_lib);
    }

    //定位，地图；
    public void onMap(View view) {
        SystemUtil.jumActivity(this, GDMapActivity.class);
    }

    //pdf 阅读器
    public void onPDF(View view) {
        SystemUtil.jumActivity(this, PDFViewActivity.class);
    }

    //二维码生成及扫描；
    public void onQRCode(View view) {
        SystemUtil.jumActivity(this, QRActivity.class);
    }

    //和风天气
    public void onWeather(View view) {

    }

    //视频播放器；
    public void onVideoTest(View view) {
        SystemUtil.jumActivity(this, VideoPlayTestActivity.class);
    }

    //日历
    public void onCalender3rd(View view) {
        SystemUtil.jumActivity(this, VideoPlayTestActivity.class);
    }

    public void onGlideImg(View view) {
        SystemUtil.jumActivity(this, GlideImageActivity.class);
    }

    //统计图表
    public void onCharts(View view) {
        SystemUtil.jumActivity(this, ChartsActivity.class);
    }
}