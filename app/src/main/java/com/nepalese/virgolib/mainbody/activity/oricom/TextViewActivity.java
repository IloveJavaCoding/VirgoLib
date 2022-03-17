package com.nepalese.virgolib.mainbody.activity.oricom;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.widget.TextView;

import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Base.BaseActivity;

/**
 * 1. 文本样式；
 * 2. textview 实现走马灯；
 * 3. string  资源占位符
 */
public class TextViewActivity extends BaseActivity {
    private static final String DEFAULT_TEXT =
            "曾经沧海难为水，除却巫山不是云。取次花丛懒回顾，半缘修道半缘君。" +
            "葡萄美酒夜光杯，欲饮琵琶马上催。醉卧沙场君莫笑。古来征战几人回。" +
            "曾经沧海难为水，除却巫山不是云。取次花丛懒回顾，半缘修道半缘君。" +
            "葡萄美酒夜光杯，欲饮琵琶马上催。醉卧沙场君莫笑。古来征战几人回。";

    private TextView tvLamp, tvConcatStr, tvConcatDecimal, tvConcatFloat, tvSpan;
    private TextView tvTest1, tvTest2, tvTest3, tvTest5, tvTest6, tvTest7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textview);
        init();
    }

    @Override
    protected void initUI() {
        tvLamp = findViewById(R.id.tvLamp);
        tvConcatStr = findViewById(R.id.tvConcatStr);
        tvConcatDecimal = findViewById(R.id.tvConcatDecimal);
        tvConcatFloat = findViewById(R.id.tvConcatFloat);
        tvSpan = findViewById(R.id.tvSpannableString);

        tvTest1 = findViewById(R.id.tvTest1);
        tvTest2 = findViewById(R.id.tvTest2);
        tvTest3 = findViewById(R.id.tvTest3);
        tvTest5 = findViewById(R.id.tvTest5);
        tvTest6 = findViewById(R.id.tvTest6);
        tvTest7 = findViewById(R.id.tvTest7);
    }

    @Override
    protected void initData() {
        setTextView();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onBack() {
        finish();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void setTextView() {
        //==============文本样式===========
        testTextStyle();

        //==============走马灯：只有当文字长度超出可用宽度时才会滚动============
        tvLamp.setText(DEFAULT_TEXT);
        tvLamp.setSelected(true);//很重要

        //xml 关键属性设置
//        android:focusable="true"
//        android:singleLine="true"
//        android:ellipsize="marquee"
//        android:marqueeRepeatLimit="marquee_forever"


        //==============拼接内容=============
        //拼接字符串  <string name="concat_str">拼接_%1$s_</string>
        tvConcatStr.setText(String.format(getString(R.string.concat_str), "加我"));
        //拼接整数 <string name="concat_decimal">拼接_%1$d_</string>
        tvConcatDecimal.setText(String.format(getString(R.string.concat_decimal), 10));
        //拼接小数 .2f控制小数位 <string name="concat_float">拼接_%1$.2f_</string>
        tvConcatFloat.setText(String.format(getString(R.string.concat_float), 3.1415));


        //============局部控制 === 通过字符索引定位，左闭右开==============
        //============ 012345 6 78 910112 3 4 567 8 9 21 2 4 5 6 7 28
        String text = "SERIF字体_加粗斜体_下划线_删除线_红色_百度一下！";

        SpannableString string = new SpannableString(text);
        string.setSpan(new TypefaceSpan("serif"), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        string.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 8, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //粗斜体
        string.setSpan(new UnderlineSpan(), 13, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        string.setSpan(new StrikethroughSpan(), 17, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        string.setSpan(new ForegroundColorSpan(Color.MAGENTA), 21, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
        string.setSpan(new URLSpan("http://www.baidu.com"), 24, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //网络
        tvSpan.setText(string);
        tvSpan.setMovementMethod(LinkMovementMethod.getInstance());//required when add some superLinks
    }

    private void testTextStyle() {
        String text = "我是TEXT,请尽情地调试我吧!";
        //引用外部字体包
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/xingkai.ttf");


        //1. 设置字体：样式，粗体、斜体: typeface 可为 null
        tvTest1.setText(text);
//        tvTest1.setTypeface(typeface);//默认 Normal
//        tvTest1.setTypeface(typeface, Typeface.BOLD);//粗体
//        tvTest1.setTypeface(typeface, Typeface.ITALIC);//斜体
        tvTest1.setTypeface(typeface, Typeface.BOLD_ITALIC);//粗体+斜体

        //其他方法加粗：修改画笔属性
//        tvTest1.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
//        tvTest1.getPaint().setFakeBoldText(true);

        //xml:
//        android:textStyle="normal"//默认
//        android:textStyle="bold"//粗体
//        android:textStyle="italic"//斜体


        //2. 字体大小
//        tvTest1.setTextSize(18.0f);//默认：TypedValue.COMPLEX_UNIT_SP
        //字体常用单位：sp
        tvTest1.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_size_18));//引用demin内配置

        //xml
//        android:textSize="@dimen/text_size_18"


        //3. 修饰线： 删除线，下划线
        tvTest2.setText(text);
        tvTest2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//删除线
        tvTest3.setText(text);
        tvTest3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

        //借用html <u></u>
        String content2 = "<u>" + text + "</u>";
        tvTest5.setText(Html.fromHtml(content2));

        //在xml 可直接引用
//        <string name="str_underline"><u>测试</u></string>
//        android:text="@string/str_underline"

        //4. 字体颜色
        tvTest6.setText(text);
        tvTest6.setTextColor(Color.RED);
//        tvTest6.setTextColor(Color.argb(255,255,0,0));
//        tvTest6.setTextColor(Color.parseColor("#ff0000"));
//        tvTest6.setTextColor(getResources().getColor(R.color.colorRed));

        //借用html
        String content3 = "<font color=\"#ff0000\">" + text + "</font>";
        tvTest7.setText(Html.fromHtml(content3));

        //xml
//        android:textColor="@color/colorRed"
    }
}