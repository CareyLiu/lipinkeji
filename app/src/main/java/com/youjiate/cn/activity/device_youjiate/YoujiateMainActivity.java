package com.youjiate.cn.activity.device_youjiate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.youjiate.cn.R;
import com.youjiate.cn.activity.device_falaer.FalaerSetActivity;
import com.youjiate.cn.app.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YoujiateMainActivity extends BaseActivity implements View.OnLongClickListener {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.iv_set)
    ImageView ivSet;
    @BindView(R.id.iv_jiareqi)
    ImageView ivJiareqi;
    @BindView(R.id.tv_dangwei)
    TextView tvDangwei;
    @BindView(R.id.view_dangwei5)
    View viewDangwei5;
    @BindView(R.id.view_dangwei4)
    View viewDangwei4;
    @BindView(R.id.view_dangwei3)
    View viewDangwei3;
    @BindView(R.id.view_dangwei2)
    View viewDangwei2;
    @BindView(R.id.view_dangwei1)
    View viewDangwei1;
    @BindView(R.id.bt_kaiguan)
    ImageView btKaiguan;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    @BindView(R.id.iv_reduce)
    ImageView ivReduce;
    @BindView(R.id.tv_reduce)
    TextView tvReduce;
    @BindView(R.id.ll_reduce)
    LinearLayout llReduce;
    @BindView(R.id.tv_wendu)
    TextView tvWendu;
    @BindView(R.id.view_wendu5)
    View viewWendu5;
    @BindView(R.id.view_wendu4)
    View viewWendu4;
    @BindView(R.id.view_wendu3)
    View viewWendu3;
    @BindView(R.id.view_wendu2)
    View viewWendu2;
    @BindView(R.id.view_wendu1)
    View viewWendu1;
    @BindView(R.id.iv_mode_shoudong)
    ImageView ivModeShoudong;
    @BindView(R.id.iv_mode_hengwen)
    ImageView ivModeHengwen;
    @BindView(R.id.tv_dianya)
    TextView tvDianya;
    @BindView(R.id.tv_chufengkouwendu)
    TextView tvChufengkouwendu;
    @BindView(R.id.tv_huanjingwendu)
    TextView tvHuanjingwendu;
    @BindView(R.id.tv_rufengkouwendu)
    TextView tvRufengkouwendu;

    @Override
    public int getContentViewResId() {
        return R.layout.a_youjiate_act_main;
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
        Intent intent = new Intent(context, YoujiateMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @OnClick({R.id.rl_back, R.id.iv_set, R.id.ll_add, R.id.ll_reduce})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_set:
                FalaerSetActivity.actionStart(mContext);
                break;
            case R.id.ll_add:
                break;
            case R.id.ll_reduce:
                break;
        }
    }
}
