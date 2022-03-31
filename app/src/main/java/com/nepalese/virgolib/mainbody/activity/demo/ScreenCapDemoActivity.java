package com.nepalese.virgolib.mainbody.activity.demo;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Picture;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.base.MyApp;
import com.nepalese.virgosdk.Base.BaseActivity;
import com.nepalese.virgosdk.Util.BitmapUtil;
import com.nepalese.virgosdk.Util.FileUtil;
import com.nepalese.virgosdk.Util.SystemUtil;

import java.io.File;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 1. 全屏截图；
 * 2. 控件截图；
 * 3. 长截图；
 */
public class ScreenCapDemoActivity extends BaseActivity {
    private static final String TAG = "ScreenCapDemoActivity";

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_cap_demo);
        init();
    }

    @Override
    protected void initUI() {
        imageView = findViewById(R.id.img_cap);
    }

    @Override
    protected void initData() {

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void onCapAdb(View view) {
        String path = screenCapAdb(this);
        if (path != null) {
            imageView.setImageBitmap(BitmapUtil.getBitmapFromFile(path));
        }
    }

    public void onCapDrawCache(View view) {
        Bitmap bitmap = screenCapDrawCache(this, 0, 0, MyApp.getInstance().getsWidth(), MyApp.getInstance().getsHeight());
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    public void onCapMP(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            screenCapMP(this, 1);
        } else {
            SystemUtil.showToast(this, "当前版本太低！");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Bitmap bitmap = parseData(getApplicationContext(), data);
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 全屏截图，需root权限
     * 使用adb shell screencap -p path 命令
     * 以当前时间命名，直接存储到本地
     *
     * @param context
     * @return 可能null
     */
    private String screenCapAdb(Context context) {
        String path = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault());

        try {
            path = FileUtil.getAppRootPth(context) + File.separator + sdf.format(new Date(System.currentTimeMillis())) + ".png";
            Runtime.getRuntime().exec("screencap -p " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }

    /**
     * 拦截系统缓存，可自定义截图大小
     * 不需要添加任何权限，无法截取WebView页面，部分surfaceview
     *
     * @param activity
     * @param x        左上x坐标
     * @param y        左上y坐标
     * @param width    宽
     * @param height   高
     * @return
     */
    private Bitmap screenCapDrawCache(Activity activity, int x, int y, int width, int height) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache(true);
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(decorView.getDrawingCache(), x, y, width, height);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        //关闭
        decorView.setDrawingCacheEnabled(false);
        decorView.destroyDrawingCache();

        return bitmap;
    }

    /**
     * 调用系统截屏服务
     * api>=21, andriod 5.0+
     * 需重写onActivityResult方法
     *
     * @param context
     * @param code
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void screenCapMP(Context context, int code) {
        MediaProjectionManager manager = (MediaProjectionManager) context.getSystemService(Service.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(manager.createScreenCaptureIntent(), code);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Bitmap parseData(Context context, Intent data) {
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.getDisplay().getRealMetrics(dm);
        } else {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
        }

        MediaProjectionManager manager = (MediaProjectionManager) context.getSystemService(Service.MEDIA_PROJECTION_SERVICE);
        MediaProjection mMediaProjection = manager.getMediaProjection(Activity.RESULT_OK, data);

        ImageReader mImageReader = ImageReader.newInstance(dm.widthPixels, dm.heightPixels, ImageFormat.RGB_565, 2);
        VirtualDisplay mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                dm.widthPixels, dm.heightPixels, Resources.getSystem().getDisplayMetrics().densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);

        Image image = mImageReader.acquireLatestImage();
        Bitmap bitmap = null;
        if (image != null) {
            final Image.Plane[] planes = image.getPlanes();
            if (planes.length > 0) {
                final ByteBuffer buffer = planes[0].getBuffer();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * dm.widthPixels;
                bitmap = Bitmap.createBitmap(dm.widthPixels + rowPadding / pixelStride, dm.heightPixels, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(buffer);
                image.close();
            }
        }

        //截屏之后要及时关闭VirtualDisplay ，因为VirtualDisplay 是十分消耗内存和电量
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
        }

        mMediaProjection.stop();

        return bitmap;
    }

    /**
     * 截取某控件截屏
     *
     * @param view 某控件
     * @return
     */
    private Bitmap screenCapView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(view.getDrawingCache());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        //关闭
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        return bitmap;
    }

    /**
     * 截取某控件截屏2
     *
     * @param view 某控件
     * @return
     */
    private Bitmap screenCapView2(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 长截屏：实原理就是截取整个ScrollView或者ListView的视图
     * scrollview
     */
    private Bitmap screenLongCap(ScrollView view) {
        int h = 0;
        Bitmap bitmap;
        // 获取scrollView实际高度
        for (int i = 0; i < view.getChildCount(); i++) {
            h += view.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(view.getWidth(), h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 长截屏：实原理就是截取整个ScrollView或者ListView的视图
     * listview
     */
    private Bitmap screenLongCap(ListView view) {
        int h = 0;
        Bitmap bitmap;
        // 获取listView实际高度
        for (int i = 0; i < view.getChildCount(); i++) {
            h += view.getChildAt(i).getHeight();
        }

        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(view.getWidth(), h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * webview 截图
     * @param view
     * @return
     */
    private Bitmap captureWebView(WebView view) {
        Picture snapShot = view.capturePicture();
        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }
}