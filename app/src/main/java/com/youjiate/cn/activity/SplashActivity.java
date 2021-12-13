package com.youjiate.cn.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.youjiate.cn.R;
import com.youjiate.cn.config.PreferenceHelper;

import java.lang.ref.WeakReference;


public class SplashActivity extends AppCompatActivity  {

    public static final int UPDATE_OK = 2;
    public static final int OVERTIME = 1;
    protected boolean isAnimationEnd;
    private final NotLeakHandler mHandler = new NotLeakHandler(this);
    private RelativeLayout logoView;


    private static class NotLeakHandler extends Handler {
        private WeakReference<SplashActivity> mWeakReference;

        private NotLeakHandler(SplashActivity reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity reference = mWeakReference.get();
            if (reference == null) { // the referenced object has been cleared
                return;
            }
            // do something
            switch (msg.what) {
                case UPDATE_OK:
                    SplashOverToGo();
                    break;
                case OVERTIME:
                    SplashOverToGo();
            }
        }

        /**
         * 进入主界面
         */
        private void SplashOverToGo() {

            SharedPreferences sharedPreferences = mWeakReference.get().getSharedPreferences("share", MODE_PRIVATE);
            boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (isFirstRun) {
                editor.putBoolean("isFirstRun", false);
                editor.apply();
                mWeakReference.get().startActivity(new Intent(mWeakReference.get(), LoginActivity.class));
                mWeakReference.get().finish();
            } else {
                if (PreferenceHelper.getInstance(mWeakReference.get()).getBoolean("ISLOGIN", false)) {
                    //验证手势

                } else {
                    if (PreferenceHelper.getInstance(mWeakReference.get()).getString("app_token", "").equals("")) {
                        mWeakReference.get().startActivity(new Intent(mWeakReference.get(), LoginActivity.class));
                    } else {
                        //判断上次登录类型
                        switch (PreferenceHelper.getInstance(mWeakReference.get()).getString("power_state", "1")) {
                            case "1"://车主端
                                mWeakReference.get().startActivity(new Intent(mWeakReference.get(), HomeActivity.class));
                                break;
                            case "2"://维修厂
                                break;
                            case "3"://代理商
                                break;
                            case "4"://未知
                                break;
                        }
                    }
                    mWeakReference.get().finish();
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logoView = findViewById(R.id.iv_welcome);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        // 动画效果时间为2秒
        alphaAnimation.setDuration(2000);
        // 设置开始动画
        logoView.startAnimation(alphaAnimation);
        // 动画监听
        alphaAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { // 动画开始时执行此方法

            }

            @Override
            public void onAnimationRepeat(Animation animation) { // 动画重复调用时执行此方法
            }

            @Override
            public void onAnimationEnd(Animation animation) { // 动画结束时执行此方法

                isAnimationEnd = true;
                mHandler.sendEmptyMessage(UPDATE_OK);
            }
        });

        ImmersionBar immersionBar = ImmersionBar.with(SplashActivity.this);
        immersionBar.with(this)
                .init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}