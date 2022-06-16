package com.nepalese.virgolib.widget.image;

import com.nepalese.virgosdk.Beans.BaseBean;

/**
 * Created by Administrator on 2022/6/10.
 * Usage: 正方形， 仅下落
 */
public class Cube extends BaseBean {
    public int color; // 图片像素点颜色值
    public float sX; // 坐标x
    public float sY;// 坐标y
    public float cL;// 边长

    public float vY;// 垂直速度
    public float aY;// 垂直加速度
}
