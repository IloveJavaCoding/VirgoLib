package com.nepalese.virgosdk.Base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.KeyEvent;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author nepalese on 2020/11/21 12:07
 * @usage 带有常用方法的基础activity
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog processDialog;
    private AlertDialog.Builder msgDialog;

    public BaseActivity() {
    }

    /**
     * 常规调用顺序：
     * initUI();
     * initData();
     * setListener();
     */
    protected void init(){
        initUI();
        initData();
        setListener();
    }

    /**
     * 绑定控件
     */
    protected abstract void initUI();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 事件监听
     */
    protected abstract void setListener();

    /**
     * 按返回键时触发
     */
    protected abstract void onBack();

    /**
     * 调用加载弹框，需手动调用关闭
     * @param title 标题
     * @param content 提示内容
     */
    protected void showDialog(String title, String content) {
        if(processDialog==null){
            processDialog = new ProgressDialog(this);
        }
        processDialog.setTitle(title);
        processDialog.setMessage(content);
        processDialog.setCancelable(false);
        processDialog.show();
    }

    /**
     * 调用信息提示弹框
     * @param title 标题
     * @param msg 提示内容
     */
    protected void showMsgDialog(String title, String msg) {
        if(msgDialog==null){
            msgDialog = new AlertDialog.Builder(this);//不能用getApplicationContext(): token 不一致
            msgDialog.setPositiveButton("关闭", (dialog, which) -> dialog.dismiss());
            msgDialog.setCancelable(false);
        }
        msgDialog.setTitle(title);
        msgDialog.setMessage(msg);
        msgDialog.show();
    }

    /**
     * 隐藏弹框
     */
    protected void hideDialog() {
        if (processDialog != null) {
            processDialog.dismiss();
        }
    }

    private void releaseDialog() {
        if (processDialog != null) {
            processDialog.dismiss();
            processDialog = null;
        }

        if(msgDialog!=null){
            msgDialog = null;
        }
    }

    /**
     * EventBus 推送消息
     * @param obj
     */
    protected void postEvent(Object obj) {
        EventBus.getDefault().post(obj);
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.onBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        releaseDialog();
        super.onDestroy();
    }
}