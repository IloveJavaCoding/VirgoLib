package com.nepalese.virgolib.mainbody.activity.oricom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Util.SystemUtil;

/**
 * 原配控件使用
 * 1. textview, editview, spinner
 * 2. button, imagebutton, checkboc, radiobutton, togglebutton,
 * 3. fragments
 * 4. listview, gridview, recycleview
 * 5. seekbar, switch, processbar, ratingbar
 * 6. calender, textclock
 * 7. dialog
 * 8. webview
 * 9. camera/camera2
 */
public class OriComponentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ori_component);
    }

    //textview
    public void onTextView(View view) {
        SystemUtil.jumActivity(this, TextViewActivity.class);
    }

    //editview, spinner
    public void onEtSp(View view) {
        SystemUtil.jumActivity(this, ExtSpinnerActivity.class);
    }


    //button, imagebutton, checkboc, radiobutton, togglebutton,
    public void onButton(View view) {
        SystemUtil.jumActivity(this, ButtonsActivity.class);
    }

    //fragments, listview, gridview, recycleview
    public void onFragments(View view) {
        SystemUtil.jumActivity(this, FragmentsActivity.class);
    }

    //seekbar, switch, processbar
    public void onBar(View view) {
        SystemUtil.jumActivity(this, BarsActivity.class);
    }

    //calender, textclock
    public void onCalender(View view) {
        SystemUtil.jumActivity(this, CalenderClockActivity.class);
    }

    //dialog
    public void onDialog(View view) {
        SystemUtil.jumActivity(this, DialogsActivity.class);
    }

    //webview
    public void onWebView(View view) {
        SystemUtil.jumActivity(this, FragmentsActivity.class);
    }

    //camera
    public void onCamrea(View view){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            SystemUtil.jumActivity(this, Camera2Activity.class);
        }else{
            SystemUtil.jumActivity(this, CameraActivity.class);
        }
    }
}