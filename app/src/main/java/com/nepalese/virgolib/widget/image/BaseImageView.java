package com.nepalese.virgolib.widget.image;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

/**
 * Created by Administrator on 2022/4/18.
 * Usage:自带动画的图片播放器: 两张图之间
 * <p>
 * BaseImageView imageView = new BaseImageView(this);
 * imageView.setAnimType(BaseImageView.ANIM_RANDOM);
 * MyUtils.add2ParentIfNeed(root, imageView);
 * updateLayoutParams(imageView, width / 2, 100, width / 2, (height - 100) / 2);
 * imageView.initLayout(width / 2, (height - 100) / 2);
 */

public class BaseImageView extends View {
    private static final String TAG = "BaseImageView";
    private static final int ANIM_NUM = 6;//动画效果数（除随机）
    private static final int LENTH = 50;//像素跨值|cube 边长

    public static final int ANIM_NONE = 0;//无动画
    public static final int ANIM_FADE = 1;//淡入淡出 默认
    public static final int ANIM_RIGHT = 2;//右进左出
    public static final int ANIM_SCALE = 3;//中心缩放
    public static final int ANIM_CRASH = 4;//破碎效果
    public static final int ANIM_JUMP = 5;//弹跳退出
    public static final int ANIM_RANDOM = 10;//随机

    private final Context context;
    private Drawable[] drawables;//操作的两张图
    private ValueAnimator animator;//动画
    private Paint paint;//画笔

    private int width, height;//控件宽高
    private int animType;//动画类型
    private int curIndex;//0|1
    private int CV;//线性变化的基础值
    private int JUMP_THRESHOLD;//跳动偏移阈值 px
    private int alphaLast, alphaCur;//上一张、当前图片透明值[0-255]
    private int leftLast, leftCur;//上一张、当前图片左上点x[0-width]
    private Rect rectLast, rectCur, rectJump;//上一张、当前图片位置
    private List<Cube> pixelList;
    private float animTime;//动画运行时间
    private float whRate;//宽高比
    private long INTERVAL_ANIMATION;//动画时长
    private boolean isSecond;//第二部分
    private boolean isOver;//动画结束
    private boolean isRandom;//随机动画效果

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
        animTime = 0;
        curIndex = -1;
        isOver = false;
        isSecond = false;
        isRandom = false;
        animType = ANIM_NONE;
    }

    /**
     * 通过 java 创建设置布局
     * 设置|更改布局时调用
     *
     * @param width  容器宽
     * @param height 容器高
     */
    public void initLayout(int width, int height) {
        this.width = width;
        this.height = height;
        if (width == 0 || height == 0) {
            return;
        }
        whRate = width * 1f / height;
        initAnimator();
    }

    private void initAnimator() {
        cancelAnim();
        resetAnimator();
    }

    private void resetAnimator() {
        switch (animType) {
            case ANIM_NONE:
                CV = 0;
                break;
            case ANIM_FADE:
                CV = 255; //透明值
                INTERVAL_ANIMATION = 600L;
                break;
            case ANIM_RIGHT:
                CV = width;
                INTERVAL_ANIMATION = 600L;
                break;
            case ANIM_SCALE:
                rectLast = new Rect();
                rectCur = new Rect();
                CV = width / 2;
                INTERVAL_ANIMATION = 800L;
                break;
            case ANIM_CRASH:
                if (paint == null) {
                    paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setStyle(Paint.Style.FILL);
                }
                if (pixelList == null) {
                    pixelList = new ArrayList<>();
                }
                CV = 100;
                INTERVAL_ANIMATION = 2000L;
                break;
            case ANIM_JUMP:
                rectJump = new Rect();
                JUMP_THRESHOLD = Math.max(width / 5, 30);
                CV = width + JUMP_THRESHOLD;
                INTERVAL_ANIMATION = 1200L;
                break;
        }

        if (CV > 0) {
            animator = ValueAnimator.ofInt(0, CV);
            animator.setDuration(INTERVAL_ANIMATION);
            animator.setInterpolator(new LinearInterpolator());//插值器设为线性
        } else {
            animator = null;
        }
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
        if (width == 0 || height == 0) {
            return;
        }
        whRate = width * 1f / height;
        initAnimator();
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
            case ANIM_CRASH:
                drawCrash(canvas);
                break;
            case ANIM_JUMP:
                drawJump(canvas);
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

    private void drawCrash(Canvas canvas) {
        if (isOver) {
            //动画结束
            drawables[curIndex].setBounds(0, 0, width, height);
            drawables[curIndex].draw(canvas);
        } else {
            for (Cube item : pixelList) {
                if (item.sY > height) {
                    //超出容器不用画
                    continue;
                }
                paint.setColor(item.color);
                canvas.drawRect(item.sX, item.sY, item.sX + item.cL, item.sY + item.cL, paint);

                //变化 s = v0t + at^2
                item.sY += (float) (item.vY * animTime + item.aY * Math.pow(animTime, 2));
            }
        }
    }

    private void drawJump(Canvas canvas) {
        //当前图片一直存在
        drawables[curIndex].setBounds(0, 0, width, height);
        drawables[curIndex].draw(canvas);

        if (isOver) {
            return;
        }

        //上一张图
        if (curIndex == 0) {
            if (drawables[1] != null) {
                //上一张
                drawables[1].setBounds(rectJump);
                drawables[1].draw(canvas);
            }
        } else {
            //上一张
            drawables[0].setBounds(rectJump);
            drawables[0].draw(canvas);
        }
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
            Drawable drawable = null;

            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = 1;// 1/insample

                drawable = new BitmapDrawable(getResources(), BitmapFactory.decodeFile(filePath, options));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return drawable;
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
        if (animator != null) {
            animator.removeAllListeners();
        }

        if (isRandom) {
            animType = (int) (Math.random() * 1000) % ANIM_NUM;
//            Log.d(TAG, "animType: " + animType);
            resetAnimator();
        }

        if (animType == ANIM_NONE || animator == null) {
            //无动画
            invalidate();
            return;
        } else if (animType == ANIM_CRASH) {
            pixelList.clear();
            //获取上一张图片的像素
            BitmapDrawable bitmapDrawable = null;
            if (curIndex == 0) {
                bitmapDrawable = (BitmapDrawable) drawables[1];
            } else if (curIndex == 1) {
                bitmapDrawable = (BitmapDrawable) drawables[0];
            }
            if (bitmapDrawable == null) {
                isOver = true;
                invalidate();
                return;
            }

            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null) {
                //该参数控制原来每一个像素点在屏幕上的缩放比例，此时为放大两倍
                //该参数控制原来每一个像素点在屏幕上的缩放比例，此时为放大两倍
                //基于控件宽高， 获取等比缩放下对应的像素点
                float rW = bitmap.getWidth() * 1f / width;
                float rH = bitmap.getHeight() * 1f / height;
                Cube item;
                for (int i = 0; i < width; i += LENTH) {//像素跨值
                    for (int j = 0; j < height; j += LENTH) {
                        item = new Cube();
                        item.color = bitmap.getPixel((int) (i * rW), (int) (j * rH));//取样点

                        item.sX = i;
                        item.sY = j;
                        item.cL = LENTH;

                        //初始速度
                        item.vY = getRandom(5, 25);
                        //加速度
                        item.aY = 30f;//9.8f;

                        pixelList.add(item);
                    }
                }
            }
        }

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animTime = 0;
                isSecond = false;
                isOver = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animTime = 0;
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

        animator.addUpdateListener(animation -> {
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
                case ANIM_CRASH:
                    animTime = animation.getCurrentPlayTime() / 1000f;//ms
                    break;
                case ANIM_JUMP:
                    if (curIndex == 0) {
                        //右出
                        if (av < JUMP_THRESHOLD) {
                            //先向左移动
                            rectJump.left = -av;
                        } else {
                            //向右跳出
                            rectJump.left = av - JUMP_THRESHOLD;
                        }
                    } else {
                        //左出
                        if (av < JUMP_THRESHOLD) {
                            //先向左移动
                            rectJump.left = av;
                        } else {
                            //向右跳出
                            rectJump.left = JUMP_THRESHOLD - av;
                        }
                    }

                    rectJump.right = width + rectJump.left;
                    rectJump.top = 0;
                    rectJump.bottom = height;
                    break;
            }

            invalidate();
        });
        animator.start();
    }

    private float getRandom(int a, int b) {
        return (float) (Math.random() * (b - a) + a);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 设置是否开启随机动画效果: 默认 false
     *
     * @param random b
     */
    public void setRandom(boolean random) {
        this.isRandom = random;
    }

    /**
     * 设置动画类型
     */
    public void setAnimType(int animType) {
        if (animType >= ANIM_NUM) {
            //随机
            this.isRandom = true;
            return;
        }
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
            d = ResourcesCompat.getDrawable(getResources(), resId, null);
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
            //图片资源为空
            return false;
        }

        if (drawables == null) {
            drawables = new Drawable[2];
        }

        curIndex++;

        if (curIndex > 1) {
            curIndex = 0;
        }

        if (drawables[curIndex] != null) {
            drawables[curIndex] = null;//回收
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
        if (pixelList != null) {
            pixelList.clear();
            pixelList = null;
        }
    }
}
