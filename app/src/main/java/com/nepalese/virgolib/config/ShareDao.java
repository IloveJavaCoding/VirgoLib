package com.nepalese.virgolib.config;

import android.content.Context;

import com.nepalese.virgosdk.Util.SPUtil;

/**
 * Created by Administrator on 2022/6/28.
 * Usage: 本地 key-value 存储
 * 减少频繁调用
 */

public class ShareDao {
    private static final String SHARE_CONFIGURATION = "virgo_config";
    private static final String KEY_IMG_DIR = "img_dir";//图片动画图片文件夹

    public static void setImgDir(Context context, String value) {
        SPUtil.setString(context, SHARE_CONFIGURATION, KEY_IMG_DIR, value);
    }

    public static String getImgDir(Context context) {
        return SPUtil.getString(context, SHARE_CONFIGURATION, KEY_IMG_DIR, "");
    }
}
