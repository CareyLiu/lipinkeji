package com.lipinkeji.cn.config;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lipinkeji.cn.util.GlideShowImageUtils;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2018/4/10 0010.
 *
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(MyApplication.getAppContext())
                .applyDefaultRequestOptions(GlideShowImageUtils.showBannerCelve())
                .load(path).
                into(imageView);


    }
}
