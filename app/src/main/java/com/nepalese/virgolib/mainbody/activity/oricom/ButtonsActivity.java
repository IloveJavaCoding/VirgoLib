package com.nepalese.virgolib.mainbody.activity.oricom;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Base.BaseActivity;

import androidx.core.content.res.ResourcesCompat;

/**
 * button :(Button extends TextView)
 * 1. 点击监听；
 * 2. textview 实现button效果；
 *
 * imagebutton：（ImageButton extends ImageView）
 * 1. 点击监听;
 * 2. 修改点击效果；
 * 3. 点击时更换图片源；
 *
 * checkbox： 多选，多个互相之间不影响
 * 1. 设置默认选中；
 * 2. 监听；
 * 3. 自定义图标样式；
 *
 * radiobutton：单选（同一RadioGroup下，同时有且仅能选中一个）
 * 1. 设置默认选中；
 * 2. 监听；
 * 3. 自定义图标样式；
 *
 * togglebutton：
 * 1. 设置默认选中；
 * 2. 监听；
 * 3. 自定义图标样式；
 *
 * switch：
 */
public class ButtonsActivity extends BaseActivity {
    private TextView tvLog;
    private Button button1;
    private ImageButton imageButton1;
    private CheckBox checkBox1, checkBox2;

    private RadioGroup rgOri, rgSelf1, rgSelf2, rgSelf3;
    private ToggleButton tb1, tb2;

    private boolean isGreen = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);
        init();
    }

    @Override
    protected void initUI() {
        tvLog = findViewById(R.id.tv_log);
        button1 = findViewById(R.id.btn_test1);
        imageButton1 = findViewById(R.id.img_button1);

        checkBox1 = findViewById(R.id.checkbox1);
        checkBox2 = findViewById(R.id.checkbox2);

        rgOri = findViewById(R.id.rg_ori);
        rgSelf1 = findViewById(R.id.rg_self1);
        rgSelf2 = findViewById(R.id.rg_self2);
        rgSelf3 = findViewById(R.id.rg_self3);

        tb1 = findViewById(R.id.tb1);
        tb2 = findViewById(R.id.tb2);
    }

    @Override
    protected void initData() {
        setCheckBox();
        setRadioButton();
        setToggleButton();
        setSwitch();
    }

    private void setCheckBox() {
        //checkbox
        //=====设置默认选中==============================
        //1.
        checkBox1.setChecked(true);

        //2. xml android:checked="true"

        //=====自定义图标样式===============================
        /**
         * 替换原button样式
         * android:button="@drawable/check_selector"
         *
         * drawable/check_selector：（两种状态图标）
         * <?xml version="1.0" encoding="utf-8"?>
         * <selector xmlns:android="http://schemas.android.com/apk/res/android">
         *     <item android:state_checked="true" android:state_enabled="true" android:state_pressed="true"
         *         android:drawable="@mipmap/icon_check_true"/>
         *     <item android:state_checked="false" android:state_enabled="true" android:state_pressed="true"
         *         android:drawable="@mipmap/icon_check_true"/>
         *
         *     <item android:state_checked="true" android:state_enabled="true"
         *         android:drawable="@mipmap/icon_check_true"/>
         *     <item android:state_checked="false" android:state_enabled="true"
         *         android:drawable="@mipmap/icon_check_false"/>
         *
         *     <item android:state_checked="true" android:state_enabled="false"
         *         android:drawable="@mipmap/icon_check_true"/>
         *     <item android:state_checked="false" android:state_enabled="false"
         *         android:drawable="@mipmap/icon_check_false"/>
         * </selector>
         */
    }

    private void setRadioButton() {
        //radiobutton
        //===== 设置默认选中=================================
        //1.
//        rbOri1.setChecked(true);

        //2. xml android:checked="true"

        //=====自定义图标样式===============================
        /**
         * 1. 按钮样式：
         *  android:button="@null"
         *  android:gravity="center"
         *  android:background="@drawable/check_selector_bg
         *
         *  drawable/check_selector_bg：
         *  <?xml version="1.0" encoding="utf-8"?>
         * <selector xmlns:android="http://schemas.android.com/apk/res/android">
         *     <item android:state_checked="true">
         *         <shape android:shape="rectangle">
         *             <stroke android:color="@color/colorPrimary"
         *                 android:width="1dp"/>
         *             <solid android:color="@color/colorPrimary"/>
         *             <corners android:radius="@dimen/text_button_radius"/>
         *         </shape>
         *     </item>
         *
         *     <item android:state_checked="false">
         *         <shape android:shape="rectangle">
         *             <stroke android:color="@color/colorGray"
         *                 android:width="1dp"/>
         *             <solid android:color="@color/colorTransparent"/>
         *             <corners android:radius="@dimen/text_button_radius"/>
         *         </shape>
         *     </item>
         * </selector>
         */

        /**
         * 2. 替换原button样式：
         * android:button="@drawable/check_selector"
         *
         * drawable/check_selector：（两种状态图标）
         * <?xml version="1.0" encoding="utf-8"?>
         * <selector xmlns:android="http://schemas.android.com/apk/res/android">
         *     <item android:state_checked="true" android:state_enabled="true" android:state_pressed="true"
         *         android:drawable="@mipmap/icon_check_true"/>
         *     <item android:state_checked="false" android:state_enabled="true" android:state_pressed="true"
         *         android:drawable="@mipmap/icon_check_true"/>
         *
         *     <item android:state_checked="true" android:state_enabled="true"
         *         android:drawable="@mipmap/icon_check_true"/>
         *     <item android:state_checked="false" android:state_enabled="true"
         *         android:drawable="@mipmap/icon_check_false"/>
         *
         *     <item android:state_checked="true" android:state_enabled="false"
         *         android:drawable="@mipmap/icon_check_true"/>
         *     <item android:state_checked="false" android:state_enabled="false"
         *         android:drawable="@mipmap/icon_check_false"/>
         * </selector>
         */

        /**
         * 3. 指定图标相对文字的位置：
         * android:button="@null"
         * android:gravity="center"
         * android:drawableTop="@drawable/check_selector"
         * //drawableTop: 上面； drawableBottom: 下面；drawableRight/drawableEnd :右边； drawableLeft/drawableStart :左边；
         *
         * drawable/check_selector：（同2）
         */
    }

    private void setToggleButton() {
        //togglebutton
        //===== 设置默认选中=================================
        //1.
        tb1.setChecked(true);

        //2. xml android:checked="true"

        //=====自定义图标样式===============================
        /**
         * 设置背景
         * background="@drawable/troggle_selector"
         * android:textOn=""
         * android:textOff=""
         * //默认textOn="ON", textOff="OFF";
         *
         * drawable/troggle_selector:(两种状态图标)
         * <?xml version="1.0" encoding="utf-8"?>
         * <selector xmlns:android="http://schemas.android.com/apk/res/android">
         *     <item android:state_checked="true"
         *         android:drawable="@mipmap/icon_troggle_on"/>
         *     <item android:state_checked="false"
         *         android:drawable="@mipmap/icon_troggle_off"/>
         * </selector>
         */
    }

    private void setSwitch() {
        //===== 设置默认选中=================================
        //1.
//        switchcompat.setChecked(true);

        //2. xml android:checked="true"

        //==========颜色======================
//        app:thumbTint="@color/black"  //滑块颜色
//        app:trackTint="@color/colorGreen" //轨道颜色
    }

    @Override
    protected void setListener() {
        //==========button 点击监听=====================================================
        //1. setOnClickListener();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printLog("1111");
            }
        });

        //2. xml android:onClick="onButtonTest"; 然后再对应activity内创建同名函数 public void onButtonTest(View view) {}

        //====================imagebutton 点击监听, 点击时更换图片源=========================================================
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGreen){
                    isGreen = false;
                    imageButton1.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.mipmap.icon_img_btn_red, null));
                }else{
                    isGreen = true;
                    imageButton1.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.mipmap.icon_img_btn_green, null));
                    //imageButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_img_btn_red));
                }
            }
        });

        //xml android:onClick="onImgBtn1" ; public void onImgBtn1(View view){}

        //========================checkbox 状态变化监听========================
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //选中
                    printLog("选中貂蝉！");
                }else{
                    //取消选中
                    printLog("取消貂蝉！");
                }
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //选中
                    printLog("选中孙尚香！");
                }else{
                    //取消选中
                    printLog("取消孙尚香！");
                }
            }
        });

        //=================radiogroup 状态变化监听========================
        //每个 radiobutton 都需要命名
        rgOri.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_ori1:
                        printLog("rb_ori1");
                        break;
                    case R.id.rb_ori2:
                        printLog("rb_ori2");
                        break;
                    case R.id.rb_ori3:
                        printLog("rb_ori3");
                        break;
                }
            }
        });

        rgSelf1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_self11:
                        printLog("rb_self11");
                        break;
                    case R.id.rb_self12:
                        printLog("rb_self12");
                        break;
                    case R.id.rb_self13:
                        printLog("rb_self13");
                        break;
                }
            }
        });

        rgSelf2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_self21:
                        printLog("rb_self21");
                        break;
                    case R.id.rb_self22:
                        printLog("rb_self22");
                        break;
                    case R.id.rb_self23:
                        printLog("rb_self23");
                        break;
                }
            }
        });

        rgSelf3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_self31:
                        printLog("rb_self31");
                        break;
                    case R.id.rb_self32:
                        printLog("rb_self32");
                        break;
                    case R.id.rb_self33:
                        printLog("rb_self33");
                        break;
                }
            }
        });

        //==============togglebutton 状态变化监听================================
        tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //选中
                    printLog("1开！");
                }else{
                    //取消选中
                    printLog("1关！");
                }
            }
        });

        tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //选中
                    printLog("2开！");
                }else{
                    //取消选中
                    printLog("2关！");
                }
            }
        });
    }

    @Override
    protected void release() {

    }

    private void printLog(String s) {
        tvLog.setText(String.format(getString(R.string.print_log), s));
    }

    @Override
    protected void onBack() {
        finish();
    }

    public void onButtonTest(View view) {
        printLog("2222");
    }

    public void onButtonTest2(View view) {
        printLog("3333");

        /**
         * textview 实现button效果
         *
         *  <TextView
         *         android:layout_width="@dimen/button_width"
         *         android:layout_height="wrap_content"
         *         android:layout_marginStart="10dp"
         *         android:text="@string/button_text"
         *         android:textColor="@color/white"
         *         android:textAllCaps="true" //英文时大写（button 默认）
         *         android:background="@drawable/text_button_shape" //自定义外框
         *         android:gravity="center" //居中
         *         android:paddingTop="10dp"
         *         android:paddingBottom="10dp"
         *         android:onClick="onButtonTest2"/>
         *
         *
         *       drawable/text_button_shape：
         *         <?xml version="1.0" encoding="utf-8"?>
         *        <layer-list xmlns:android="http://schemas.android.com/apk/res/android">
         * <!--上面的在底层-->
         * <!--    给text_button外框加上阴影效果-->
         *     <item>
         *         <shape android:shape="rectangle">
         *             <stroke android:color="@color/colorGray30"
         *                 android:width="1dp"/>
         *             <solid android:color="@color/colorGray30"/>
         *             <corners android:radius="@dimen/text_button_radius"/>
         *         </shape>
         *     </item>
         *
         *     <item
         *         android:drawable="@drawable/text_button"
         *         android:bottom="2dp"
         *         android:right="3dp"></item>
         * </layer-list>
         *
         * drawable/text_button：
         * <?xml version="1.0" encoding="utf-8"?>
         * <selector xmlns:android="http://schemas.android.com/apk/res/android">
         *
         *     <!--    未点击时样式-->
         *     <item android:state_pressed="false">
         *         <shape android:shape="rectangle">
         *             <stroke android:color="@color/colorPrimary"
         *                 android:width="1dp"/>
         *             <solid android:color="@color/colorPrimary"/>
         *             <corners android:radius="@dimen/text_button_radius"/>
         *         </shape>
         *     </item>
         *
         *     <!--    点击时样式-->
         *     <item android:state_pressed="true">
         *         <shape android:shape="rectangle">
         *             <stroke android:color="@color/colorGray"
         *                 android:width="1dp"/>
         *             <solid android:color="@color/colorGray"/>
         *             <corners android:radius="@dimen/text_button_radius"/>
         *         </shape>
         *     </item>
         *
         *     <item android:state_hovered="true">
         *         <shape android:shape="rectangle">
         *             <stroke android:color="@color/colorGray"
         *                 android:width="1dp"/>
         *             <solid android:color="@color/colorGray"/>
         *             <corners android:radius="@dimen/text_button_radius"/>
         *         </shape>
         *     </item>
         * </selector>
         */


    }

    public void onImgBtn1(View view) {
        printLog("来自QQ的问候！");

        /**
         * 图片默认：
         * <enum name="matrix" value="0" /> //不改变原图的大小，从View的左上角开始绘制，超出部分做剪切处理;
         *  <enum name="fitXY" value="1" /> //不按比例扩大/缩小图片，目标是把图片塞满整个View;
         *  <enum name="fitStart" value="2" />//把图片按比例扩大/缩小到View的宽度, 前部/上部显示;
         *  <enum name="fitCenter" value="3" />//把图片按比例扩大/缩小到View的宽度，居中显示;
         *  <enum name="fitEnd" value="4" /> //把图片按比例扩大/缩小到View的宽度，尾部/底部显示;
         *  <enum name="center" value="5" /> //按图片的原来size居中显示，当原图的size大于View时，多出来的部分被截掉;
         *  <enum name="centerCrop" value="6" /> //按比例扩大/缩小图片的size居中显示，以原图填满View为目的;
         *  <enum name="centerInside" value="7" />//将图片的内容完整居中显示，当原图的size大于View时,按比例缩小原图，居中显示在View中;
         */
    }

    public void onImgBtn2(View view) {
        printLog("来自WeChat的问候！");

        /**
         * 自定义背景（未点击时背景透明）：android:background="@drawable/img_button_transprant"
         *
         * drawable/img_button_transprant：
         * <?xml version="1.0" encoding="utf-8"?>
         * <selector xmlns:android="http://schemas.android.com/apk/res/android">
         *
         * <!--    未点击时背景透明-->
         *     <item android:state_pressed="false">
         *         <shape android:shape="rectangle">
         *             <solid android:color="@color/colorTransparent"/>
         *         </shape>
         *     </item>
         *
         *     <item android:state_pressed="true">
         *         <shape android:shape="rectangle">
         *             <stroke android:color="@color/colorGray"
         *                 android:width="1dp"/>
         *             <solid android:color="@color/colorGray"/>
         *             <corners android:radius="@dimen/img_button_radius"/>
         *         </shape>
         *     </item>
         *
         *     <item android:state_hovered="true">
         *         <shape android:shape="rectangle">
         *             <stroke android:color="@color/colorGray"
         *                 android:width="1dp"/>
         *             <solid android:color="@color/colorGray"/>
         *             <corners android:radius="@dimen/img_button_radius"/>
         *         </shape>
         *     </item>
         * </selector>
         */
    }
}