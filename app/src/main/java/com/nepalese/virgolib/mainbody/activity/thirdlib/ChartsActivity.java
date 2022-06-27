package com.nepalese.virgolib.mainbody.activity.thirdlib;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Base.BaseActivity;

/**
 * 统计图表
 * implementation 'com.github.PhilJay:MPAndroidChart:v3.0.1'
 * 1. 条形统计图
 *      a: 竖向单条
 *      b: 横向两条合一
 *
 * 2. 折线统计图
 *      a: 单条
 *      b: 多条
 *      
 * 3. 扇形统计图
 *      
 * 4. 组合统计图
 *      a: 竖向两条条形分开 + 折线
 *      b: 
 *      
 * 5. 3d统计图
 *      
 */
public class ChartsActivity extends BaseActivity {
    private static final String TAG = "ChartsActivity";
    private static final float TAG_TEXT_SIZE = 8f;
    private static final float TAG_INNERTEXT_SIZE = 6f;
    private static final int COLOR_AXIS = Color.CYAN;
    
    private HorizontalBarChart hbChart;
    private BarChart bcChart;
    private LineChart lineSChart, lineMChart;
    private PieChart pie1Chart, pie2Chart;
    private CombinedChart cbChart;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        initUI();
    }

    @Override
    protected void initUI() {
        bcChart = findViewById(R.id.bc_chart);
        hbChart = findViewById(R.id.hbc_chart);
        lineSChart = findViewById(R.id.sl_chart);
        lineMChart = findViewById(R.id.ml_chart);
        pie1Chart = findViewById(R.id.p1_chart);
        pie2Chart = findViewById(R.id.p2_chart);
        cbChart = findViewById(R.id.cb_chart);
    }

    @Override
    protected void initData() {
        initChartStyle();
    }

    private void initChartStyle() {
        initBarChart();
        initHBarChart();
        initSlineSChart();
        initMlineSChart();
        initPie1Chart();
        initPie2Chart();
        initCombineChart();
    }

    //竖向单条 条形统计图
    private void initBarChart() {
        bcChart.setExtraOffsets(10, 5, 10, 5);
        bcChart.getDescription().setEnabled(false);
        bcChart.setMaxVisibleValueCount(60);//如果60多个条目显示在图表,drawn没有值
        bcChart.setDrawValueAboveBar(true);
        bcChart.setPinchZoom(false);//扩展现在只能分别在x轴和y轴
        bcChart.setDrawBarShadow(false);
        bcChart.setDrawGridBackground(false);//是否显示表格颜色
        bcChart.animateY(1000);

        XAxis xAxis = bcChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(TAG_TEXT_SIZE);
        xAxis.setTextColor(COLOR_AXIS);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(-0.5f);

        YAxis axisLeft = bcChart.getAxisLeft();
        axisLeft.setTextSize(TAG_TEXT_SIZE);
        axisLeft.setDrawGridLines(true);
        axisLeft.setTextColor(COLOR_AXIS);
        axisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return String.valueOf((int) v);
            }
        });

        //标签样式
        Legend legend = bcChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setFormSize(TAG_TEXT_SIZE);
        legend.setXEntrySpace(4f);
        legend.setTextColor(COLOR_AXIS);
        legend.setEnabled(true);
    }

    //横向两条合一 条形统计图
    private void initHBarChart() {
        hbChart.setExtraOffsets(10, 5, 10, 5);
        hbChart.getDescription().setEnabled(false);
        hbChart.setMaxVisibleValueCount(60);//如果60多个条目显示在图表,drawn没有值
        hbChart.setDrawValueAboveBar(true);
        hbChart.setPinchZoom(false);//扩展现在只能分别在x轴和y轴
        hbChart.setDrawBarShadow(false);
        hbChart.setDrawGridBackground(false);//是否显示表格颜色
        hbChart.animateY(1000);

        XAxis xAxis = hbChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(TAG_TEXT_SIZE);
        xAxis.setTextColor(COLOR_AXIS);
        xAxis.setDrawGridLines(false);

        YAxis axisLeft = hbChart.getAxisLeft();
        axisLeft.setEnabled(false);

        YAxis axisRight = hbChart.getAxisRight();
        axisRight.setTextSize(TAG_TEXT_SIZE);
        axisRight.setTextColor(COLOR_AXIS);
        axisRight.setEnabled(true);

        Legend legend = hbChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setFormSize(TAG_TEXT_SIZE);
        legend.setXEntrySpace(4f);
        legend.setTextColor(COLOR_AXIS);
        legend.setEnabled(true);
    }

    //单条 折线统计图
    private void initSlineSChart() {
        lineSChart.setExtraOffsets(10, 5, 10, 5);
        lineSChart.getDescription().setEnabled(false);
        lineSChart.setMaxVisibleValueCount(60);//如果60多个条目显示在图表,drawn没有值
        lineSChart.setPinchZoom(false);//扩展现在只能分别在x轴和y轴
        lineSChart.setDrawGridBackground(false);//是否显示表格颜色
        lineSChart.animateY(1000);

        XAxis xAxis = lineSChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(TAG_TEXT_SIZE);
        xAxis.setTextColor(COLOR_AXIS);
        xAxis.setDrawGridLines(false);
//        xAxis.setAxisMinimum(-0.5f);
//        xAxis.setAxisMaximum(times.length - 0.5f);
//        xAxis.setLabelCount(times.length); // 设置X轴标签数量
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return times[(int) value];
//            }
//        });

        YAxis axisLeft = lineSChart.getAxisLeft();
        axisLeft.setTextSize(TAG_TEXT_SIZE);
        axisLeft.setDrawGridLines(true);
        axisLeft.setAxisMaximum(0f);
        axisLeft.setAxisMaximum(100f);
        axisLeft.setTextColor(COLOR_AXIS);
        axisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return (int) v + "%";
            }
        });

        YAxis axisRight = lineSChart.getAxisRight();
        axisRight.setEnabled(false);

        //标签样式
        Legend legend = lineSChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setFormSize(TAG_TEXT_SIZE);
        legend.setXEntrySpace(4f);
        legend.setTextColor(COLOR_AXIS);
        legend.setEnabled(true);
    }

    //多条 折线统计图
    private void initMlineSChart() {
        
    }

    //
    private void initPie1Chart() {
        
    }

    //
    private void initPie2Chart() {
        
    }

    //组合统计图 竖向两条条形分开 + 折线
    private void initCombineChart() {
        
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