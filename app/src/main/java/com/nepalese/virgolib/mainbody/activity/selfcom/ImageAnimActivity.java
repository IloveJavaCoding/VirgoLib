package com.nepalese.virgolib.mainbody.activity.selfcom;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.nepalese.virgocomponent.view.VirgoFileSelectorDialog;
import com.nepalese.virgolib.R;
import com.nepalese.virgolib.config.MyApp;
import com.nepalese.virgolib.config.ShareDao;
import com.nepalese.virgolib.widget.image.BaseImageView;
import com.nepalese.virgosdk.Base.BaseActivity;
import com.nepalese.virgosdk.Util.UIUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//全屏模式, 常亮
public class ImageAnimActivity extends BaseActivity implements VirgoFileSelectorDialog.SelectFileCallback {
    private static final String TAG = "ImageAnimActivity";

    private Context context;
    private ImageButton imageButton;
    private VirgoFileSelectorDialog fileSelectorDialog;
    private BaseImageView baseImageView;
    private List<String> imgList;//图片文件路径
    private int index = -1;//当前应播放图片索引

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_anim);
        //隐藏顶部状态栏
        UIUtil.setSNHide(this);
        //常亮
        //View.keepScreenOn和android:keepScreenO

        init();
        scanFiles(ShareDao.getImgDir(context));
    }

    @Override
    protected void initUI() {
        baseImageView = findViewById(R.id.base_image);
        imageButton = findViewById(R.id.ib_select_dir);
    }

    @Override
    protected void initData() {
        context = getApplicationContext();
        imgList = new ArrayList<>();
        baseImageView.setAnimType(BaseImageView.ANIM_RANDOM);

        fileSelectorDialog = new VirgoFileSelectorDialog(this);
        fileSelectorDialog.setFlag(VirgoFileSelectorDialog.FLAG_DIR);
        if (MyApp.getInstance().isLandscape()) {
            fileSelectorDialog.setDialogWidth(MyApp.getInstance().getsWidth() / 2);
        } else {
            fileSelectorDialog.setDialogWidth(MyApp.getInstance().getsWidth() * 2 / 3);
        }
        fileSelectorDialog.setDialogHeight(MyApp.getInstance().getsHeight() / 2);
    }

    @Override
    protected void setListener() {
        fileSelectorDialog.setCallback(this);
    }

    @Override
    protected void release() {
        removeMsg();
        if (baseImageView != null) {
            baseImageView.releaseBase();
        }
        if (fileSelectorDialog != null) {
            fileSelectorDialog.destory();
        }
    }

    @Override
    protected void onBack() {
        finish();
    }

    @Override
    public void onResult(List<File> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        //保存选择路径
        ShareDao.setImgDir(context, list.get(0).getPath());
        scanFiles(list.get(0).getPath());
    }

    private void scanFiles(String dir) {
        if (TextUtils.isEmpty(dir)) {
            dir = Environment.getExternalStorageDirectory().getPath() + "/Pictures";
        }

        File file = new File(dir);
        if (file.exists() && file.isDirectory()) {
            String[] names = file.list();
            if (names == null || names.length < 1) {
                showToast("空文件夹！");
                return;
            }

            stopPlay();
            imgList.clear();
            for (String name : names) {
                if (name.endsWith("jpg") || name.endsWith("jpeg") || name.endsWith("png")) {
                    imgList.add(dir + File.separator + name);
                }
            }

            if (imgList.size() > 0) {
                hideButton();
                playNext();
            } else {
                showToast("未找到图片！");
            }
        } else {
            showToast("路径不存在！");
        }
    }

    public void onSelectDir(View view) {
        //切换根目录
        if (fileSelectorDialog != null) {
            fileSelectorDialog.show();
        }
    }

    private void showButton() {
        handler.removeMessages(MSG_HIDE);
        handler.sendEmptyMessageDelayed(MSG_HIDE, 3000L);
        imageButton.setVisibility(View.VISIBLE);
    }

    /**
     * 播放图片时自动退出
     */
    private void hideButton() {
        imageButton.setVisibility(View.INVISIBLE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private final int MSG_NEXT = 1;
    private final int MSG_LAST = 2;
    private final int MSG_HIDE = 3;
    private final long DELAY_CHANGE = 10000L;

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_NEXT:
                    changeImageRes(true);
                    handler.sendEmptyMessageDelayed(MSG_NEXT, DELAY_CHANGE);
                    break;
                case MSG_LAST:
                    changeImageRes(false);
                    handler.sendEmptyMessageDelayed(MSG_NEXT, DELAY_CHANGE);
                    break;
                case MSG_HIDE:
                    hideButton();
                    break;
            }
            return false;
        }
    });

    private void removeMsg() {
        handler.sendEmptyMessage(MSG_NEXT);
        handler.removeMessages(MSG_HIDE);
    }

    /**
     * 播放下一张：默认
     */
    private void playNext() {
        stopPlay();
        handler.sendEmptyMessage(MSG_NEXT);
    }

    /**
     * 播放上一张：仅滑动触发
     */
    private void playLast() {
        stopPlay();
        handler.sendEmptyMessage(MSG_LAST);
    }

    private void stopPlay() {
        handler.removeMessages(MSG_NEXT);
    }

    private void continuePlay() {
        handler.sendEmptyMessageDelayed(MSG_NEXT, DELAY_CHANGE / 2);
    }

    /**
     * 切换图片
     *
     * @param next 下一张？
     */
    private void changeImageRes(boolean next) {
        if (baseImageView != null) {
            if (next) {
                //下一张
                ++index;
                if (index >= imgList.size()) {
                    index = 0;
                }
            } else {
                //上一张
                --index;
                if (index <= 0) {
                    index = imgList.size() - 1;
                }
            }

//            Log.i(TAG, "changeImageRes: " + index);
            baseImageView.setImageResource(imgList.get(index));
        }
    }

    private float oldX = 0f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = event.getX();
                stopPlay();
                break;
            case MotionEvent.ACTION_UP:
                float newX = event.getX();
                if (newX - oldX > 50) {
                    //向右滑：上一张
                    Log.e(TAG, "onTouch: 向右滑");
                    playLast();
                } else if (newX - oldX < -50) {
                    //向左滑：下一张
                    Log.e(TAG, "onTouch: 向左滑");
                    playNext();
                } else {
                    //仅点击
                    showButton();
                    continuePlay();
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
        }
        return false;
    }
}