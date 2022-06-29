package com.nepalese.virgocomponent.component.bean;

import android.graphics.Color;

import com.nepalese.virgocomponent.common.CommonUtil;

/**
 * @author nepalese on 2021/5/14 18:02
 * @usage
 */
public class VirgoPoint2 {
    private float x;
    private float y;
    private final int offset;//分36等分，每份10度
    private final int color;//随机颜色

    public VirgoPoint2(int x, int y) {
        this.x = x;
        this.y = y;
        this.offset = CommonUtil.getRandomInt(1, 36);
        this.color = Color.rgb(CommonUtil.getRandomInt(0, 255),
                CommonUtil.getRandomInt(0, 255),
                CommonUtil.getRandomInt(0, 255));
    }

    public int getColor() {
        return color;
    }

    public int getOffset() {
        return offset;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
