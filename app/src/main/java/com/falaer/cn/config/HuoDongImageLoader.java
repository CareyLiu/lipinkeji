package com.falaer.cn.config;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.falaer.cn.util.GlideShowImageUtils;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2018/4/10 0010.
 */

public class HuoDongImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        Glide.with(MyApplication.getAppContext()).applyDefaultRequestOptions(GlideShowImageUtils.showBannerCelve()).load(path).into(imageView);
    }
}
