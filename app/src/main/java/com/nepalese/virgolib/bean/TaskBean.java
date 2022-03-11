package com.nepalese.virgolib.bean;

import com.nepalese.virgosdk.Beans.BaseBean;

/**
 * Created by Administrator on 2022/3/10.
 * Usage:
 */

public class TaskBean extends BaseBean {
    private String name;
    private Class c;//跳转时对应类

    public TaskBean(String name, Class c) {
        this.name = name;
        this.c = c;
    }

    public String getName() {
        return name;
    }

    public Class getC() {
        return c;
    }
}
