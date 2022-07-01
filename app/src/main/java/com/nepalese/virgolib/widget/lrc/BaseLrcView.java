package com.nepalese.virgolib.widget.lrc;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
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
    private static final float PADD_VALUE = 25f;//时间线两边缩进值
    private static final long INTERVAL_ANIMATION = 400L;//动画时长
    private static final String DEFAULT_TEXT = "暂无歌词，快去下载吧！";

    private final Context context;
    private Paint paint;//画笔, 仅一个
    private ValueAnimator animator;//动画
    private List<LrcBean> lineList;//歌词行
    private LrcCallback callback;//手动滑动进度刷新回调

    //可设置变量
    private int textColorMain;//选中字体颜色
    private int textColorSec;//其他字体颜色
    private float textSize;//字体大小
    private float dividerHeight;//行间距

    private int width, height;//控件宽高
    private int curLine;//当前行数
    private int locateLine;//滑动时居中行数
    private int underRows;//中分下需显示行数
    private float itemHeight;//一行字+行间距
    private float centerY;//居中y
    private float startY;//首行y
    private float offsetY;//动画已偏移量
    private float offsetY2;//每次手动滑动偏移量
    private long maxTime;//歌词显示最大时间
    private boolean isDown;//按压界面
    private boolean isReverse;//往回滚动？

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
        textSize = 45f;
        dividerHeight = 28f;

        curLine = 0;
        maxTime = 0;
        isDown = false;
        isReverse = false;
        lineList = new ArrayList<>();

        paint = new Paint();
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);

        calculateItem();
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
        centerY = (height - itemHeight) / 2.0f;
        startY = centerY;
        underRows = (int) Math.ceil(height / itemHeight / 3);
        Log.d(TAG, "itemHeight: " + itemHeight + ", underRows: " + underRows);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //提示无歌词
        if (lineList.isEmpty()) {
            paint.setColor(textColorMain);
            canvas.drawText(DEFAULT_TEXT, getStartX(DEFAULT_TEXT, paint), centerY, paint);
            return;
        }

        if (isDown) {
            paint.setColor(textColorSec);
            //画时间
            if (locateLine >= 0) {
                canvas.drawText(lineList.get(locateLine).getStrTime(), PADD_VALUE, centerY, paint);
            }
            //画选择线
            canvas.drawLine(PADD_VALUE, centerY, width - PADD_VALUE, centerY, paint);

            //手动滑动
            drawTexts(canvas, startY - offsetY2);
        } else {
            //自动滚动
            if (isReverse) {
                drawTexts(canvas, startY + offsetY);
            } else {
                drawTexts(canvas, startY - offsetY);
            }
        }
    }

    private void drawTexts(Canvas canvas, float tempY) {
        for (int i = 0; i < lineList.size(); i++) {
            float y = tempY + i * itemHeight;

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

    private float oldY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                if (animator != null) {
                    if (animator.isRunning()) {
                        //停止动画
                        animator.end();
                    }
                }
                locateLine = -1;
                oldY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                offsetY2 = oldY - event.getY();
                calculateCurLine(oldY - event.getY());//定位时间啊
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDown = false;
                postNewLine();
                break;
        }

        return true;
    }

    //计算滑动后当前居中的行
    private void calculateCurLine(float y) {
        int offLine = (int) Math.floor(y / itemHeight);
        if (offLine == 0) {
            return;
        }

        locateLine = curLine + offLine;
        if (locateLine > lineList.size() - 1) {
            //最后一行
            locateLine = lineList.size() - 1;
        } else if (locateLine < 0) {
            //第一行
            locateLine = 0;
        }
    }

    private void postNewLine() {
        //返回当前行对应的时间线
        if (callback == null) {
            return;
        }
        if (locateLine >= 0) {
            callback.onUpdateTime(lineList.get(locateLine).getTime());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        releaseBase();
        super.onDetachedFromWindow();
    }

    /**
     * 移除控件，注销资源
     */
    private void releaseBase() {
        cancelAnim();

        lineList.clear();
        lineList = null;

        if (callback != null) {
            callback = null;
        }
    }

    private void calculateItem() {
        itemHeight = getTextHeight() + dividerHeight;
    }

    //计算使文字水平居中
    private float getStartX(String str, Paint paint) {
        return (width - paint.measureText(str)) / 2.0f;
    }

    //获取文字高度
    private float getTextHeight() {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
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

        maxTime = lineList.get(lineList.size() - 1).getTime() + 1000;//多加一秒
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
            if (con.matches("^\\d.+")) {//time
                time = parseTime(con);
                str = " ";
            } else {
                return;
            }
            lineList.add(new LrcBean(time, str, con));
            return;
        }

        //[00:23.24]让自己变得快乐
        line = line.replaceAll("\\[", "");
        String[] result = line.split("]");
        lineList.add(new LrcBean(parseTime(result[0]), result[1], result[0]));
    }

    private void reset() {
        lineList.clear();
        curLine = 0;
        maxTime = 0;
        isReverse = false;
        cancelAnim();
    }

    ///////////////////////////////////////动画/////////////////////////////////////////////////////

    /**
     * 更新动画
     *
     * @param lineNum 需跳转行数
     */
    private void updateAnim(int lineNum) {
        if (lineNum == 0) {
            return;
        } else if (lineNum == 1) {
            //自然变化
            if (curLine >= lineList.size() - underRows) {
                //停止动画 仅变更颜色
                cancelAnim();
                invalidate();
                return;
            }
        }
        isReverse = lineNum < 0;
        cancelAnim();
        setAnimator(Math.abs(lineNum));
        doAnimation();
    }

    /**
     * 注销已有动画
     */
    protected void cancelAnim() {
        if (animator != null) {
            animator.removeAllListeners();
            animator.end();
            animator = null;
        }
    }


    /**
     * 动态创建动画
     *
     * @param lineNum 需跳转行数
     */
    private void setAnimator(int lineNum) {
        animator = ValueAnimator.ofFloat(0, itemHeight * lineNum);//一行
        animator.setDuration(INTERVAL_ANIMATION);
        animator.setInterpolator(new LinearInterpolator());//插值器设为线性
    }

    /**
     * 监听动画
     */
    private void doAnimation() {
        if (animator == null) {
            return;
        }

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                offsetY = 0;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isReverse) {
                    startY += offsetY;
                } else {
                    startY -= offsetY;
                }
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
            if (av == 0) {
                return;
            }
            offsetY = av;
            invalidate();
        });

        animator.start();
    }

    public interface LrcCallback {
        void onUpdateTime(long time);

        void onFinish();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 滑动监听
     *
     * @param callback LrcCallback
     */
    public void setCallback(LrcCallback callback) {
        this.callback = callback;
    }

    public void setTextColorMain(int textColorMain) {
        this.textColorMain = textColorMain;
    }

    public void setTextColorSec(int textColorSec) {
        this.textColorSec = textColorSec;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        paint.setTextSize(textSize);
        calculateItem();
    }

    public void setDividerHeight(float dividerHeight) {
        this.dividerHeight = dividerHeight;
        calculateItem();
    }

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
        if (isDown) {
            return;
        }

        if (time == 0) {
            //刷新
            invalidate();
            return;
        } else if (time > maxTime) {
            //超最大时间
            if (callback != null) {
                callback.onFinish();
            }
            return;
        }

        for (int i = 0; i < lineList.size(); i++) {
            if (i < lineList.size() - 1) {
                if (time >= lineList.get(i).getTime() && time < lineList.get(i + 1).getTime()) {
                    int temp = i - curLine;
                    curLine = i;

                    updateAnim(temp);
                    break;
                }
            } else {//last line
                int temp = i - curLine;
                curLine = i;

                updateAnim(temp);
                break;
            }
        }
    }
}
