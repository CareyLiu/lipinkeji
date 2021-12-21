package com.lipinkeji.cn.activity.shuinuan_jiareqi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.shuinuan.ShuinuanBaseNewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShuinuanCaozuoSetActivity extends ShuinuanBaseNewActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_dianya_12v)
    TextView tv_dianya_12v;
    @BindView(R.id.tv_dianya_24v)
    TextView tv_dianya_24v;
    @BindView(R.id.tv_dianya_moren)
    TextView tv_dianya_moren;
    @BindView(R.id.tv_you_chaiyou)
    TextView tv_you_chaiyou;
    @BindView(R.id.tv_you_qiyou)
    TextView tv_you_qiyou;
    @BindView(R.id.tv_cheliang1)
    TextView tvCheliang1;
    @BindView(R.id.tv_cheliang2)
    TextView tvCheliang2;
    @BindView(R.id.tv_cheliang3)
    TextView tvCheliang3;
    @BindView(R.id.tv_cheliang4)
    TextView tvCheliang4;
    @BindView(R.id.tv_cheliang5)
    TextView tvCheliang5;
    @BindView(R.id.tv_cheliang6)
    TextView tvCheliang6;
    @BindView(R.id.iv_daqiya_shuoming)
    LinearLayout iv_daqiya_shuoming;
    @BindView(R.id.seekBar_daqiya)
    SeekBar seekBarDaqiya;
    @BindView(R.id.tv_daqiya)
    TextView tv_daqiya;

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_shuinuan_set_caozuo;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShuinuanCaozuoSetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        seekBarDaqiya.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_daqiya.setText(progress+"kPa");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                tv_daqiya.setText(progress+"kPa");
            }
        });
    }

    private void initData() {
        selectDianya(0);
        selectYou(0);
        selectCheliang(0);
    }


    @OnClick({R.id.iv_daqiya_shuoming, R.id.rl_back, R.id.tv_dianya_12v, R.id.tv_dianya_24v, R.id.tv_dianya_moren, R.id.tv_you_chaiyou, R.id.tv_you_qiyou, R.id.tv_cheliang1, R.id.tv_cheliang2, R.id.tv_cheliang3, R.id.tv_cheliang4, R.id.tv_cheliang5, R.id.tv_cheliang6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_dianya_12v:
                selectDianya(0);
                break;
            case R.id.tv_dianya_24v:
                selectDianya(1);
                break;
            case R.id.tv_dianya_moren:
                selectDianya(2);
                break;
            case R.id.tv_you_chaiyou:
                selectYou(0);
                break;
            case R.id.tv_you_qiyou:
                selectYou(1);
                break;
            case R.id.tv_cheliang1:
                selectCheliang(0);
                break;
            case R.id.tv_cheliang2:
                selectCheliang(1);
                break;
            case R.id.tv_cheliang3:
                selectCheliang(2);
                break;
            case R.id.tv_cheliang4:
                selectCheliang(3);
                break;
            case R.id.tv_cheliang5:
                selectCheliang(4);
                break;
            case R.id.tv_cheliang6:
                selectCheliang(5);
                break;
            case R.id.iv_daqiya_shuoming:
                ShuinuanDaqiyaShoumingActivty.actionStart(mContext);
                break;
        }
    }

    private void selectCheliang(int pos) {
        tvCheliang1.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvCheliang2.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvCheliang3.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvCheliang4.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvCheliang5.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvCheliang6.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tvCheliang1.setTextColor(Color.BLACK);
        tvCheliang2.setTextColor(Color.BLACK);
        tvCheliang3.setTextColor(Color.BLACK);
        tvCheliang4.setTextColor(Color.BLACK);
        tvCheliang5.setTextColor(Color.BLACK);
        tvCheliang6.setTextColor(Color.BLACK);
        switch (pos) {
            case 0:
                tvCheliang1.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvCheliang1.setTextColor(Color.WHITE);
                break;
            case 1:
                tvCheliang2.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvCheliang2.setTextColor(Color.WHITE);
                break;
            case 2:
                tvCheliang3.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvCheliang3.setTextColor(Color.WHITE);
                break;
            case 3:
                tvCheliang4.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvCheliang4.setTextColor(Color.WHITE);
                break;
            case 4:
                tvCheliang5.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvCheliang5.setTextColor(Color.WHITE);
                break;
            case 5:
                tvCheliang6.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvCheliang6.setTextColor(Color.WHITE);
                break;
        }
    }

    private void selectDianya(int pos) {//0-12V  1-24V  2-默认
        tv_dianya_12v.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_dianya_24v.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_dianya_moren.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tv_dianya_12v.setTextColor(Color.BLACK);
        tv_dianya_24v.setTextColor(Color.BLACK);
        tv_dianya_moren.setTextColor(Color.BLACK);
        switch (pos) {
            case 0:
                tv_dianya_12v.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_dianya_12v.setTextColor(Color.WHITE);
                break;
            case 1:
                tv_dianya_24v.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_dianya_24v.setTextColor(Color.WHITE);
                break;
            case 2:
                tv_dianya_moren.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_dianya_moren.setTextColor(Color.WHITE);
                break;
        }
    }

    private void selectYou(int pos) {//0-柴油  1-汽油
        tv_you_chaiyou.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_you_qiyou.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tv_you_chaiyou.setTextColor(Color.BLACK);
        tv_you_qiyou.setTextColor(Color.BLACK);
        switch (pos) {
            case 0:
                tv_you_chaiyou.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_you_chaiyou.setTextColor(Color.WHITE);
                break;
            case 1:
                tv_you_qiyou.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_you_qiyou.setTextColor(Color.WHITE);
                break;
        }
    }
}
