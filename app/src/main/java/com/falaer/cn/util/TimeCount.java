package com.falaer.cn.util;

import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.falaer.cn.R;
import com.falaer.cn.config.MyApplication;


/**
 * Created by Administrator on 2017/3/31.
 */
//计时器
public class TimeCount extends CountDownTimer {
    private TextView view;
    private int colorId;

    public TimeCount(long millisInFuture, long countDownInterval, TextView view) {
        super(millisInFuture, countDownInterval);
        this.view = view;
        colorId = R.color.text_color_main;
    }

    public TimeCount(long millisInFuture, long countDownInterval, TextView view, int type) {
        super(millisInFuture, countDownInterval);
        this.view = view;
        colorId = R.color.white;
    }

    @Override
    public void onFinish() {// 计时完毕
        view.setClickable(true);
        view.setText(R.string.get_code);
        view.setTextColor(ContextCompat.getColor(MyApplication.context, colorId));
        view.setTextSize(14);
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程
        view.setClickable(false);
        view.setTextSize(12);
        view.setText(millisUntilFinished / 1000 + "s后重新发送");
        view.setTextColor(ContextCompat.getColor(MyApplication.context, colorId));

    }
}