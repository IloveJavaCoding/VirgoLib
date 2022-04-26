package com.nepalese.virgolib.mainbody.activity.thirdlib;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Base.BaseActivity;

/**
 * glide 图片加载
 */
public class GlideImageActivity extends BaseActivity {

    private ImageView imageView;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_image);

        init();
    }

    public void onChangeImg(View view) {
        if(index>2) {
            index = 0;
        }

        switch (index){
            case 0:
                loadImage(R.mipmap.img_1, imageView);
                break;
            case 1:
                loadImage(R.mipmap.img_2, imageView);
                break;
            case 2:
                loadImage(R.mipmap.img_3, imageView);
                break;
        }

        index++;
    }

    @Override
    protected void initUI() {
        imageView = findViewById(R.id.img_container);
    }

    @Override
    protected void initData() {
        index = 0;
        loadImage(R.mipmap.img_1, imageView);
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

    /**
     * 加载图片到ImageView
     * @param resId 资源id
     * @param imageView
     */
    private void loadImage(Integer resId, ImageView imageView) {
        try {
            Glide.with(imageView.getContext())
                    .load(resId)
//                    .placeholder(R.drawable.icon_moon)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .transition(new DrawableTransitionOptions().transition())//自定义的动画效果
//                    .transition(new DrawableTransitionOptions().crossFade())
                    .transition(new DrawableTransitionOptions().dontTransition())
                    .into(imageView);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}