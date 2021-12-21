package com.lipinkeji.cn.activity.device_youjiate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.app.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FengnuanZhujicanshuActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.cb_citie_shuang)
    CheckBox cb_citie_shuang;
    @BindView(R.id.cb_citie_dan)
    CheckBox cb_citie_dan;
    @BindView(R.id.cb_jiaresai_jingci)
    CheckBox cb_jiaresai_jingci;
    @BindView(R.id.cb_jiaresai_limai)
    CheckBox cb_jiaresai_limai;
    @BindView(R.id.cb_gonglv_2kw)
    CheckBox cb_gonglv_2kw;
    @BindView(R.id.cb_gonglv_5kw)
    CheckBox cb_gonglv_5kw;
    @BindView(R.id.cb_fengyou_shineng)
    CheckBox cb_fengyou_shineng;
    @BindView(R.id.cb_fengyou_youneng)
    CheckBox cb_fengyou_youneng;
    @BindView(R.id.cb_chuanganqi_ntc)
    CheckBox cb_chuanganqi_ntc;
    @BindView(R.id.cb_chuanganqi_ptc)
    CheckBox cb_chuanganqi_ptc;
    @BindView(R.id.cb_chuanganqi_zidong)
    CheckBox cb_chuanganqi_zidong;
    @BindView(R.id.cb_dianya_12v)
    CheckBox cb_dianya_12v;
    @BindView(R.id.cb_dianya_24v)
    CheckBox cb_dianya_24v;
    @BindView(R.id.cb_dianya_zidong)
    CheckBox cb_dianya_zidong;
    @BindView(R.id.bt_huifuchuchang)
    TextView bt_huifuchuchang;

    @Override
    public int getContentViewResId() {
        return R.layout.a_fengnuan_act_zhujicanshu;
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
        Intent intent = new Intent(context, FengnuanZhujicanshuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_back, R.id.bt_huifuchuchang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.bt_huifuchuchang:

                break;
        }
    }
}
