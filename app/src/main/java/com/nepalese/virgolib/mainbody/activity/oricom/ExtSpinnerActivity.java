package com.nepalese.virgolib.mainbody.activity.oricom;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.mainbody.Adapter.Adapter_Spinner_Simple;
import com.nepalese.virgosdk.Base.BaseActivity;
import com.nepalese.virgosdk.Util.ConvertUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EditText:
 * 1. 输入框数据类型；
 * 2. 自定义边框样式；
 * 3. 取消自动获焦|进入时不自动弹键盘；
 * 4. 监听enter键；
 * 5. 密码显隐切换；
 * 6. 禁用系统键盘；
 *
 *
 * Spinner:
 * 1. 使用方法；
 * 2.
 */
public class ExtSpinnerActivity extends BaseActivity {
    private static final String TAG = "ExtSpinnerActivity";

    private TextView tvLog;
    private EditText etPassword, etEnter, etNoKey;
    private CheckBox cbHidePassword;
    private Spinner spinner1, spinner2, spinner3;

    private String[] array;
    private List<Integer> list;
    private List<String> list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyBorad();

        setContentView(R.layout.activity_ext_spinner);
        init();
    }

    private void hideKeyBorad() {
        //屏蔽进入时弹出键盘，无需对edittext额外处理， 不影响后面手动请求显示
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //或者在AndroidManifest.xml 对应activity 添加
        //android:windowSoftInputMode="stateHidden|adjustPan"

        //====================取消自动获焦================
        //在目标edittext 的任意父控件内设置下面两个属性即可；
//        android:focusable="true"
//        android:focusableInTouchMode="true"
    }

    @Override
    protected void initUI() {
        tvLog = findViewById(R.id.tvLog);

        etPassword = findViewById(R.id.etPassword);
        cbHidePassword = findViewById(R.id.cbHidePassword);

        etEnter = findViewById(R.id.etEnter);
        etNoKey = findViewById(R.id.etNoKeyBorad);

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
    }

    @Override
    protected void initData() {
        setEditText();
        useSelfKeyBorad();

        initSpinner();
    }

    private void initSpinner() {
        //1. 配置spinner 数据：array|list
        array = getResources().getStringArray(R.array.Date);
        int[] intArray = getResources().getIntArray(R.array.google_colors);
        list = ConvertUtil.intArr2List(intArray);
        list2 = ConvertUtil.strings2List(array);

        //2. 设置适配器：绑定数据
        //android.R.layout.simple_spinner_item: 未展开菜单时Spinner的样式;
        //android.R.layout.simple_spinner_dropdown_item: 展开菜单时Spinner的样式;
//        ArrayAdapter<String> strAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_item, array);
//        strAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner1.setAdapter(strAdapter);
        //a: xml 配置数据：
//        android:entries="@array/Date"

        //b: 使用默认适配器
        ArrayAdapter<Integer> intAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        intAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(intAdapter);

        //c: 自定义适配器：类似于 listview 的自定义适配器
        Adapter_Spinner_Simple selfAdapter = new Adapter_Spinner_Simple(this, list2);
        spinner3.setAdapter(selfAdapter);

        //3. 设置默认值（按列表索引）：默认选择第一个
        spinner1.setSelection(1);
    }

    //禁用系统软件盘,使用自定义键盘;
    private void useSelfKeyBorad() {
        //前提：防进入时自动弹出；

        //1. 设置输入类型
        etNoKey.setInputType(InputType.TYPE_CLASS_TEXT);

        //2. 点击输入框时禁用系统键盘
        etNoKey.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = etNoKey.getInputType(); // backup the input type
                etNoKey.setInputType(InputType.TYPE_NULL); // disable soft input
                etNoKey.onTouchEvent(event); // call native handler
                etNoKey.setInputType(inType); // restore input type

                //todo 显示自定义键盘
                return true;
            }
        });

        //添加字符监听（用来设置光标）
        etNoKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                etEnter.setSelection(etEnter.length());
            }
        });
    }

    private void disableSystemKeyboard(EditText editText) {
//        防进入时自动弹出： getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(editText, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEditText() {
        //==========输入数据类型
        //1. text: 任意字符（默认） android:inputType="text"
//        etTest.setInputType(InputType.TYPE_CLASS_TEXT);

        //2. number: 数字
//        etTest.setInputType(InputType.TYPE_CLASS_NUMBER);//整数 android:inputType="number"
//        etTest.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);//支持小数 android:inputType="numberDecimal"
//        etTest.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);//支持正负数 android:inputType="numberSigned"
//        etTest.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);//android 3.0+ 纯文本字符

        //3. password: 密码
//        etTest.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);//文本密码  android:inputType="textPassword"
//        etTest.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//可见文本密码（用于切换密码是否可见） android:inputType="textVisiblePassword"
//        etTest.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);//用于web-from内输入密码 android:inputType="textWebPassword"
//        etTest.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);//数字密码  android:inputType="numberPassword"
    }

    @Override
    protected void setListener() {
        //===================密码显隐切换============
        cbHidePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //隐藏 （默认样式：*）
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    //显示（仅适用于 TEXT 密码）
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                    //或者（numberPassword）
//                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        //==================监听enter键、搜索键, 执行输入=================
        etEnter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))){
                    //1. 隐藏键盘(暂时)
                    ((InputMethodManager)etEnter.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(etEnter.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    //2. 获取输入框内容
                    String input = etEnter.getText().toString().trim();

                    //3. 执行处理任务
                    tvLog.setText(input);
                    return true;
                }
                return false;
            }
        });

        //===================自定义边框样式=================
        //在目标edittext 设置如下属性
//        android:background="@drawable/edittext_frame"

        //edittext_frame:
//        <?xml version="1.0" encoding="utf-8"?>
//        <shape xmlns:android="http://schemas.android.com/apk/res/android"
//        android:shape="rectangle">
//            <stroke
    //            android:color="@color/colorCyan"  //边框颜色
    //            android:width="1dp"/>             //边框粗细
//            <corners android:radius="3dp"/>
//        </shape>


        //=================spinner 选择变化监听==============================
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //i --> index of array
                String out = "select1: " + array[i];
                tvLog.setText(out);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //选空：点击外部
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String out = "select2: " + list.get(i);
                tvLog.setText(out);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String out = "select3: " + list2.get(i);
                tvLog.setText(out);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onBack() {
        finish();
    }
}