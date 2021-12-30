package com.lipinkeji.cn.activity.device_shuinuan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShuinuanDangweiActivity extends ShuinuanBaseNewActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_dangwei_1dang)
    TextView tvDangwei1dang;
    @BindView(R.id.tv_dangwei_2dang)
    TextView tvDangwei2dang;

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.a_shuinuan_act_set_dangwei;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShuinuanDangweiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        init();
        selectDangwei(0);
    }

    private void init() {
//        if (!TextUtils.isEmpty(msgData)) {
//            if (msgData.length() >= 62) {
//                String waidian = msgData.substring(60, 61);
//                if (waidian.equals("a")) {
//                    showTiDialog("当前版本暂不支持外接装置功能");
//                } else {
//
//                }
//            } else {
//                showTiDialog("当前版本暂不支持外接装置功能");
//            }
//        } else {
//            showTiDialog("设备未链接");
//        }
    }

    private void selectDangwei(int pos) {
        tvDangwei1dang.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvDangwei2dang.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tvDangwei1dang.setTextColor(Color.BLACK);
        tvDangwei2dang.setTextColor(Color.BLACK);
        switch (pos) {
            case 0:
                tvDangwei1dang.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvDangwei1dang.setTextColor(Color.WHITE);
                break;
            case 1:
                tvDangwei2dang.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvDangwei2dang.setTextColor(Color.WHITE);
                break;
        }
    }

    @OnClick({R.id.rl_back, R.id.tv_dangwei_1dang, R.id.tv_dangwei_2dang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_dangwei_1dang:
                selectDangwei(0);
                break;
            case R.id.tv_dangwei_2dang:
                selectDangwei(1);
                break;
        }
    }
}
