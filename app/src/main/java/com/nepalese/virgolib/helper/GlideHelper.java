package com.nepalese.virgolib.helper;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class GlideHelper {
    private static RequestOptions requestOptions;
    private static RequestOptions circleRequestOptions;
    private static DrawableTransitionOptions transitionOptions;

    static {
        requestOptions = new RequestOptions().skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        circleRequestOptions = RequestOptions.circleCropTransform()
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        transitionOptions = new DrawableTransitionOptions().dontTransition();
    }

    public static void loadImage(String file, ImageView imageView) {
        if (imageView == null || imageView.getContext() == null || file == null) return;
        
        if (file.endsWith(".gif")) {
            try {
                Glide.with(imageView.getContext())
                        .asGif()
                        .load(file)
//                        .apply(requestOptions)
                        .transition(transitionOptions)
                        .into(imageView);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            try {
                Glide.with(imageView.getContext())
                        .load(file)
                        .apply(requestOptions)
                        .transition(transitionOptions)
                        .into(imageView);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void loadImage(File file, ImageView imageView) {
        if (imageView == null || file == null) return;
        
        try {
            Glide.with(imageView.getContext())
                    .load(file)
//                .apply(requestOptions)
                    .transition(transitionOptions)
                    .into(imageView);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    public static void loadImage(Uri uri, ImageView imageView) {
        if (imageView == null || uri == null) return;
        
        try {
            Glide.with(imageView.getContext())
                    .load(uri)
//                .apply(requestOptions)
                    .transition(transitionOptions)
                    .into(imageView);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    } 
    
    public static void loadImage(Integer resId, ImageView imageView) {
        if (imageView == null || resId == null) return;
        try {
            Glide.with(imageView.getContext())
                    .load(resId)
//                .apply(requestOptions)
                    .transition(transitionOptions)
                    .into(imageView);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @GlideModule
    public static class GeneratedAppGlideModule extends AppGlideModule {
        @Override
        public boolean isManifestParsingEnabled() {
            return false;
        }
    }
}
