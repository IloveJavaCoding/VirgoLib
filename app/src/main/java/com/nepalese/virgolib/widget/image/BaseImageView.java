package com.nepalese.virgolib.widget.image;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

/**
 * Created by Administrator on 2022/4/18.
 * Usage:自带动画的图片播放器: 两张图之间
 */

public class BaseImageView extends View {
    private static final String TAG = "BaseImageView";

    private static final long INTERVAL_ANIMATION = 600L;//动画时长

    public static final int ANIM_NONE = 0;//无动画
    public static final int ANIM_FADE = 1;//淡入淡出 默认
    public static final int ANIM_RIGHT = 2;//右进左出
    public static final int ANIM_SCALE = 3;//中心缩放

    private final Context context;
    private Drawable[] drawables;//操作的两张图
    private ValueAnimator animator;//动画

    private int width, height;//控件宽高
    private int animType;//动画类型
    private int curIndex;//0|1
    private int CV;//线性变化的基础值
    private int alphaLast, alphaCur;//上一张、当前图片透明值[0-255]
    private int leftLast, leftCur;//上一张、当前图片左上点x[0-width]
    private Rect rectLast, rectCur;//上一张、当前图片位置
    private float whRate;//宽高比
    private boolean isSecond;//第二部分
    private boolean isOver;//动画结束

    public BaseImageView(Context context) {
        this(context, null);
    }

    public BaseImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        curIndex = -1;
        isSecond = false;
        isOver = false;
        animType = ANIM_FADE;
    }

    /**
     * 设置|更改布局时调用
     * @param width
     * @param height
     */
    public void updateLayout(int width, int height) {
        Log.d(TAG, "updateLayout: ");
        this.width = width;
        this.height = height;
        whRate = width * 1f / height;
        initAnimator();
    }

//    private void initPosition() {
//        width = getWidth();
//        height = getHeight();
//        whRate = width * 1f / height;
//    }

    private void initAnimator() {
        cancelAnim();

        switch (animType) {
            case ANIM_NONE:
                CV = 0;
                break;
            case ANIM_FADE:
                CV = 255; //透明值
                break;
            case ANIM_RIGHT:
                CV = width;
                break;
            case ANIM_SCALE:
                rectLast = new Rect();
                rectCur = new Rect();
                CV = width / 2;
                break;
        }

        if (CV > 0) {
            animator = ValueAnimator.ofInt(0, CV);
            animator.setDuration(INTERVAL_ANIMATION);
            animator.setInterpolator(new LinearInterpolator());//插值器设为线性
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawables == null) {
            return;
        }

        switch (animType) {
            case ANIM_NONE:
                drawNone(canvas);
                break;
            case ANIM_FADE:
                drawFade(canvas);
                break;
            case ANIM_RIGHT:
                drawRight(canvas);
                break;
            case ANIM_SCALE:
                drawScal(canvas);
                break;
        }
    }

    private void drawNone(Canvas canvas) {
        drawables[curIndex].setBounds(0, 0, width, height);
        drawables[curIndex].draw(canvas);
    }

    private void drawFade(Canvas canvas) {
        //如果有上一张，先消失，再加载当前的
        if (curIndex == 0) {
            if (drawables[1] == null) {
                //第一张图
                drawables[curIndex].setBounds(0, 0, width, height);
                drawables[curIndex].setAlpha(alphaCur);
                drawables[curIndex].draw(canvas);
            } else {
                if (isSecond) {
                    drawables[curIndex].setBounds(0, 0, width, height);
                    drawables[curIndex].setAlpha(alphaCur);
                    drawables[curIndex].draw(canvas);
                } else {
                    //上一张，先消失
                    drawables[1].setBounds(0, 0, width, height);
                    drawables[1].setAlpha(alphaLast);
                    drawables[1].draw(canvas);
                }
            }
        } else {
            if (isSecond) {
                drawables[curIndex].setBounds(0, 0, width, height);
                drawables[curIndex].setAlpha(alphaCur);
                drawables[curIndex].draw(canvas);
            } else {
                //上一张，先消失
                drawables[0].setBounds(0, 0, width, height);
                drawables[0].setAlpha(alphaLast);
                drawables[0].draw(canvas);
            }
        }
    }

    /**
     * 右进左出：进入时由变不变（宽度慢慢变大），出去时保持宽度不变
     */
    private void drawRight(Canvas canvas) {
        //如果有上一张，先消失，再加载当前的
        if (isOver) {
            //动画结束
            drawables[curIndex].setBounds(0, 0, width, height);
        } else {
            if (curIndex == 0) {
                if (drawables[1] != null) {
                    //上一张
                    drawables[1].setBounds(leftLast, 0, width + leftLast, height);
                    drawables[1].draw(canvas);
                }
            } else {
                //上一张
                drawables[0].setBounds(leftLast, 0, width + leftLast, height);
                drawables[0].draw(canvas);
            }
            drawables[curIndex].setBounds(leftCur, 0, width, height);
        }
        drawables[curIndex].draw(canvas);
    }

    private void drawScal(Canvas canvas) {
        if (isOver) {
            //动画结束
            drawables[curIndex].setBounds(0, 0, width, height);
        } else {
            if (curIndex == 0) {
                if (drawables[1] != null) {
                    //上一张
                    drawables[1].setBounds(rectLast);
                    drawables[1].draw(canvas);
                }
            } else {
                //上一张
                drawables[0].setBounds(rectLast);
                drawables[0].draw(canvas);
            }
            drawables[curIndex].setBounds(rectCur);
        }
        drawables[curIndex].draw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        releaseBase();
        super.onDetachedFromWindow();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private Drawable getDrawableFromFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        } else {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            } else {
                FileInputStream inputStream = null;
                Drawable drawable = null;

                try {
                    inputStream = new FileInputStream(filePath);
                    drawable = new BitmapDrawable(getResources(), BitmapFactory.decodeStream(inputStream));
                } catch (IOException var5) {
                    var5.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return drawable;
            }
        }
    }

    protected void cancelAnim() {
        if (animator != null) {
            animator.removeAllListeners();
            animator.end();
            animator = null;
        }
    }

    private void doAnimation() {
        if (animType == ANIM_NONE || animator == null) {
            //无动画
            return;
        }

        animator.removeAllListeners();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isSecond = false;
                isOver = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isOver = true;
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int av = (int) animation.getAnimatedValue();
                switch (animType) {
                    case ANIM_FADE:
                        if (av > CV / 2) {//一半
                            isSecond = true;
                        }
                        alphaCur = av;
                        alphaLast = CV - av;
                        break;
                    case ANIM_RIGHT:
                        leftLast = -av - 10;//增加两图之间间隔
                        leftCur = CV - av;
                        break;
                    case ANIM_SCALE:
                        if (av > CV / 2) {//一半
                            isSecond = true;
                        }

                        if (!isSecond) {
                            //后面不用变化上一张 变小
                            rectLast.left = av;
                            rectLast.top = (int) (av / whRate);
                            rectLast.right = width - av;
                            rectLast.bottom = height - rectLast.top;
                        }

                        //当前：变大
                        rectCur.left = CV - av;
                        rectCur.top = (int) (rectCur.left / whRate);
                        rectCur.right = CV + av;
                        rectCur.bottom = height - rectCur.top;
                        break;
                }

                invalidate();
            }
        });
        animator.start();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 设置动画类型
     */
    public void setAnimType(int animType) {
        this.animType = animType;
    }

    /**
     * 设置图片路径，默认路径存在（外部校验）
     *
     * @param path 图片路径
     */
    public boolean setImageResource(String path) {
        return setImageResource(getDrawableFromFile(path));
    }

    /**
     * 设置图片
     */
    public boolean setImageResource(@DrawableRes int resId) {
        Drawable d;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            d = ContextCompat.getDrawable(context, resId);
        } else {
            d = ResourcesCompat.getDrawable(context.getResources(), resId, null);
        }

        return setImageResource(d);
    }

    /**
     * 设置图片
     *
     * @return 是否成功播放
     */
    public boolean setImageResource(Drawable drawable) {
        if (drawable == null) {
            Log.e(TAG, "图片资源为空！");
            return false;
        }

        if (drawables == null) {
            drawables = new Drawable[2];
        }

        curIndex++;
        if (curIndex > 1) {
            curIndex = 0;
        }

        drawables[curIndex] = drawable;

        //Animators may only be run on Looper threads
        doAnimation();
        return true;
    }

    public void releaseBase() {
        cancelAnim();
        drawables = null;
        curIndex = -1;
    }
}
