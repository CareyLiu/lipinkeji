package com.lipinkeji.cn.activity.device_fengnuan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_shuinuan.ShuinuanDaqiyaShoumingActivty;
import com.lipinkeji.cn.app.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FengnuanDaqiyaActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.iv_daqiya_shuoming)
    LinearLayout ivDaqiyaShuoming;
    @BindView(R.id.seekBar_daqiya)
    SeekBar seekBarDaqiya;
    @BindView(R.id.tv_daqiya)
    TextView tvDaqiya;

    @Override
    public int getContentViewResId() {
        return R.layout.a_fengnuan_act_set_daqiya;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, FengnuanDaqiyaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_back, R.id.iv_daqiya_shuoming})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_daqiya_shuoming:
                ShuinuanDaqiyaShoumingActivty.actionStart(mContext);
                break;
        }
    }
}
