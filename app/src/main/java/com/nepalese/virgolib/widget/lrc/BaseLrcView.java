package com.nepalese.virgolib.widget.lrc;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.camera2.params.LensShadingMap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.nepalese.virgocomponent.common.CommonUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

/**
 * Created by Administrator on 2022/6/29.
 * Usage:更流畅、丝滑的滚动歌词控件
 */

public class BaseLrcView extends View {
    private static final String TAG = "BaseLrcView";
    private static final long INTERVAL_ANIMATION = 1000L;//动画时长
    private static final String DEFAULT_TEXT = "暂无歌词，快去下载吧！";

    private Context context;
    private Paint paint;//画笔, 仅一个
    private ValueAnimator animator;//动画

    private int width, height;//控件宽高
    private int textColorMain;//选中字体颜色
    private int textColorSec;//其他字体颜色
    private int curLine = 0;//当前行数

    private float centerY;
    private float textSize;//字体大小
    private float dividerHeight;//行间距
    private float startY;//首行y
    private float offsetY;//每次动画偏移量
    private float lastValue;//动画上次已偏移量

    private long nextTime = 0;//下一行时间线

    private boolean isPlaying = false;
    private boolean isDown = false;//按压界面

    private List<LrcBean> lineList;

    public BaseLrcView(Context context) {
        this(context, null);
    }

    public BaseLrcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLrcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // todo 解析自定义属性
//        TypedArray ta = getContext().obtainStyledAttributes(attrs, );

//        ta.recycle();

        textColorMain = Color.CYAN;
        textColorSec = Color.GRAY;
        textSize = 20f;
        dividerHeight = 18f;

        lineList = new ArrayList<>();

        paint = new Paint();
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (width == 0 || height == 0) {
            initLayout();
        }
    }

    private void initLayout() {
        width = getWidth();
        height = getHeight();
        //显示页内中心y轴坐标
        centerY = (height - textSize) / 2.0f;
        startY = centerY;

        scaleBackground();
    }

    private void scaleBackground() {
//        if (background != null) {
//            background = Bitmap.createScaledBitmap(background, viewWidth, viewHeight, true);
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //提示无歌词
        if (lineList.isEmpty()) {
            paint.setColor(textColorMain);
            canvas.drawText(DEFAULT_TEXT, getStartX(DEFAULT_TEXT, paint), centerY, paint);
            return;
        }

        //画选择线
//        if (isPlaying && isDown) {
//            float baseLine = centerY + getScrollY();
//            canvas.drawLine(padValue, baseLine, width - padValue, baseLine, paint);
//        }


        startY -= offsetY;
        for (int i = 0; i < lineList.size(); i++) {
            float y = startY + i * (textSize + dividerHeight);

            if (y < 0 || y > height) {
                continue;
            }

            if (curLine == i) {
                paint.setColor(textColorMain);
            } else {
                paint.setColor(textColorSec);
            }

            canvas.drawText(lineList.get(i).getLrc(), getStartX(lineList.get(i).getLrc(), paint), y, paint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        releaseBase();
        super.onDetachedFromWindow();
    }

    private void releaseBase() {
        lineList.clear();
        lineList = null;
    }

    //计算使文字水平居中
    private float getStartX(String str, Paint paint) {
        return (width - paint.measureText(str)) / 2.0f;
    }

    protected void cancelAnim() {
        if (animator != null) {
            animator.removeAllListeners();
            animator.end();
            animator = null;
        }
    }

    private void doAnimation() {
        if (animator != null) {
            animator.removeAllListeners();
        }

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                offsetY = 0;
                lastValue = 0;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                offsetY = 0;
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.addUpdateListener(animation -> {
            float av = (float) animation.getAnimatedValue();
            offsetY = av - lastValue;
            lastValue = av;
            invalidate();
        });
        animator.start();
    }

    //解析歌词
    private void parseLrc(InputStreamReader inputStreamReader) {
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                parseLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long parseTime(String time) {
        // 00:01.10
        String[] min = time.split(":");
        String[] sec = min[1].split("\\.");

        long minInt = Long.parseLong(min[0].replaceAll("\\D+", "")
                .replaceAll("\r", "").replaceAll("\n", "").trim());
        long secInt = Long.parseLong(sec[0].replaceAll("\\D+", "")
                .replaceAll("\r", "").replaceAll("\n", "").trim());
        long milInt = Long.parseLong(sec[1].replaceAll("\\D+", "")
                .replaceAll("\r", "").replaceAll("\n", "").trim());

        return minInt * 60 * 1000 + secInt * 1000 + milInt;// * 10;
    }

    private void parseLine(String line) {
        Matcher matcher = Pattern.compile("\\[\\d.+].+").matcher(line);
        // 如果形如：[xxx]后面啥也没有的，则return空
        if (!matcher.matches()) {
            long time;
            String str;
            String con = line.replace("\\[", "").replace("\\]", "");
            Log.d(TAG, con);
            if (con.matches("^\\d.+")) {//time
                time = parseTime(con);
                str = " ";
            } else {
                return;
            }
            lineList.add(new LrcBean(time, str));
            return;
        }

        //[00:23.24]让自己变得快乐
        line = line.replaceAll("\\[", "");
        String[] result = line.split("]");
        lineList.add(new LrcBean(parseTime(result[0]), result[1]));
    }

    private void reset() {
        lineList.clear();
        curLine = 0;
        nextTime = 0;
        cancelAnim();
//        if (scroller.isFinished()) {
//            scroller.abortAnimation();
//        }
    }

    private void updateAnim(int lineNum) {
        if (lineNum <= 0) {
            return;
        }
        cancelAnim();
        setAnimator(lineNum);
        doAnimation();
    }

    private void setAnimator(int lineNum) {
        animator = ValueAnimator.ofFloat(0, (dividerHeight + textSize) * lineNum);//一行
        animator.setDuration(INTERVAL_ANIMATION);
        animator.setInterpolator(new LinearInterpolator());//插值器设为线性
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 设置歌词
     *
     * @param lrc 解析后的string
     */
    public void setLrc(String lrc) {
        if (TextUtils.isEmpty(lrc)) {
            return;
        }
        reset();
        parseLrc(new InputStreamReader(new ByteArrayInputStream(lrc.getBytes())));
    }

    /**
     * 设置歌词
     *
     * @param resId 资源文件id
     */
    public void setLrc(@RawRes int resId) {
        reset();

        parseLrc(new InputStreamReader(context.getResources().openRawResource(resId), StandardCharsets.UTF_8));
    }

    /**
     * 设置歌词
     *
     * @param path lrc文件路径
     */
    public void setLrcFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            reset();
            String format;
            if (CommonUtil.isUtf8(file)) {
                format = "UTF-8";
            } else {
                format = "GBK";
            }

            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            InputStreamReader inputStreamReader = null;//'utf-8' 'GBK'
            try {
                inputStreamReader = new InputStreamReader(inputStream, format);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            parseLrc(inputStreamReader);
        }
    }

    /**
     * 调整播放位置
     *
     * @param time ms
     */
    public void seekTo(long time) {
        if (time == 0) {
            //刷新
            invalidate();
            return;
        }

        if (time < lineList.get(curLine).getTime()) {//往回跳
            return;
        } else if (time < nextTime) {
            return;
        }

        for (int i = 0; i < lineList.size(); i++) {
            if (i < lineList.size() - 1) {
                if (time >= lineList.get(i).getTime() && time < lineList.get(i + 1).getTime()) {
                    int temp = i - curLine;
                    curLine = i;
                    nextTime = lineList.get(i + 1).getTime();

                    updateAnim(temp);
                    break;
                }
            } else {//last line
                int temp = i - curLine;
                curLine = i;
                nextTime = lineList.get(i).getTime() + 60000;

                updateAnim(temp);
                break;
            }
        }
    }

}
