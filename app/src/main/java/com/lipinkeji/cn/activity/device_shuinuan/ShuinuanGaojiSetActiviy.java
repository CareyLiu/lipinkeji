package com.lipinkeji.cn.activity.device_shuinuan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.dialog.JiareqiMimaDialog;
import com.lipinkeji.cn.util.Y;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShuinuanGaojiSetActiviy extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.seekbar_dianhuosai_24v)
    SeekBar seekbarDianhuosai24v;
    @BindView(R.id.tv_dianhuosai_24v)
    TextView tvDianhuosai24v;
    @BindView(R.id.seekbar_dianhuosai_12v)
    SeekBar seekbarDianhuosai12v;
    @BindView(R.id.tv_dianhuosai_12v)
    TextView tvDianhuosai12v;
    @BindView(R.id.tv_dianhuosai_jingci)
    TextView tvDianhuosaiJingci;
    @BindView(R.id.tv_dianhuosai_limai)
    TextView tvDianhuosaiLimai;
    @BindView(R.id.tv_dianhuosai_gaide)
    TextView tvDianhuosaiGaide;
    @BindView(R.id.tv_dianhuosai_tuobo)
    TextView tvDianhuosaiTuobo;
    @BindView(R.id.seekbar_youbengguige)
    SeekBar seekbarYoubengguige;
    @BindView(R.id.tv_youbengguige)
    TextView tvYoubengguige;
    @BindView(R.id.seekbar_guoyabaohu_12v)
    SeekBar seekbarGuoyabaohu12v;
    @BindView(R.id.tv_guoyabaohu_12v)
    TextView tvGuoyabaohu12v;
    @BindView(R.id.seekbar_qianyabaohu_12v)
    SeekBar seekbarQianyabaohu12v;
    @BindView(R.id.tv_qianyabaohu_12v)
    TextView tvQianyabaohu12v;
    @BindView(R.id.seekbar_guoyabaohu_24v)
    SeekBar seekbarGuoyabaohu24v;
    @BindView(R.id.tv_guoyabaohu_24v)
    TextView tvGuoyabaohu24v;
    @BindView(R.id.seekbar_qianyabaohu_24v)
    SeekBar seekbarQianyabaohu24v;
    @BindView(R.id.tv_qianyabaohu_24v)
    TextView tvQianyabaohu24v;
    @BindView(R.id.tv_huoyan_redianou)
    TextView tvHuoyanRedianou;
    @BindView(R.id.tv_huoyan_pt1000)
    TextView tvHuoyanPt1000;
    @BindView(R.id.tv_huoyan_wu)
    TextView tvHuoyanWu;
    @BindView(R.id.tv_fengji_dan)
    TextView tvFengjiDan;
    @BindView(R.id.tv_fengji_shuang)
    TextView tvFengjiShuang;
    @BindView(R.id.tv_fengji_si)
    TextView tvFengjiSi;
    @BindView(R.id.tv_fengji_wu)
    TextView tvFengjiWu;
    @BindView(R.id.tv_jinshuikou_ntc)
    TextView tvJinshuikouNtc;
    @BindView(R.id.tv_jinshuikou_pt1000)
    TextView tvJinshuikouPt1000;
    @BindView(R.id.tv_jinshuikou_guanbi)
    TextView tvJinshuikouGuanbi;
    @BindView(R.id.tv_chushuikou_ntc)
    TextView tvChushuikouNtc;
    @BindView(R.id.tv_chushuikou_pt1000)
    TextView tvChushuikouPt1000;
    @BindView(R.id.tv_chushuikou_guanbi)
    TextView tvChushuikouGuanbi;
    @BindView(R.id.tv_shuibeng_shineng)
    TextView tvShuibengShineng;
    @BindView(R.id.tv_shuibeng_guanbi)
    TextView tvShuibengGuanbi;

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.a_shuinuan_act_set_gaoji;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShuinuanGaojiSetActiviy.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        showMimaDialog();
    }

    private void showMimaDialog() {
        JiareqiMimaDialog mimaDialog = new JiareqiMimaDialog(mContext, new JiareqiMimaDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, JiareqiMimaDialog dialog) {
                    finish();
            }

            @Override
            public void onClickConfirm(View v, JiareqiMimaDialog dialog) {
                String textContent = dialog.getTextContent();
                if (textContent.equals("123456")){
                    dialog.dismiss();
                }else {
                    Y.t("密码错误");
                }
            }
        });
        mimaDialog.show();
    }


    @OnClick({R.id.rl_back, R.id.tv_dianhuosai_jingci, R.id.tv_dianhuosai_limai, R.id.tv_dianhuosai_gaide, R.id.tv_dianhuosai_tuobo, R.id.tv_huoyan_redianou, R.id.tv_huoyan_pt1000, R.id.tv_huoyan_wu, R.id.tv_fengji_dan, R.id.tv_fengji_shuang, R.id.tv_fengji_si, R.id.tv_fengji_wu, R.id.tv_jinshuikou_ntc, R.id.tv_jinshuikou_pt1000, R.id.tv_jinshuikou_guanbi, R.id.tv_chushuikou_ntc, R.id.tv_chushuikou_pt1000, R.id.tv_chushuikou_guanbi, R.id.tv_shuibeng_shineng, R.id.tv_shuibeng_guanbi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                break;
            case R.id.tv_dianhuosai_jingci:
                break;
            case R.id.tv_dianhuosai_limai:
                break;
            case R.id.tv_dianhuosai_gaide:
                break;
            case R.id.tv_dianhuosai_tuobo:
                break;
            case R.id.tv_huoyan_redianou:
                break;
            case R.id.tv_huoyan_pt1000:
                break;
            case R.id.tv_huoyan_wu:
                break;
            case R.id.tv_fengji_dan:
                break;
            case R.id.tv_fengji_shuang:
                break;
            case R.id.tv_fengji_si:
                break;
            case R.id.tv_fengji_wu:
                break;
            case R.id.tv_jinshuikou_ntc:
                break;
            case R.id.tv_jinshuikou_pt1000:
                break;
            case R.id.tv_jinshuikou_guanbi:
                break;
            case R.id.tv_chushuikou_ntc:
                break;
            case R.id.tv_chushuikou_pt1000:
                break;
            case R.id.tv_chushuikou_guanbi:
                break;
            case R.id.tv_shuibeng_shineng:
                break;
            case R.id.tv_shuibeng_guanbi:
                break;
        }
    }
}
