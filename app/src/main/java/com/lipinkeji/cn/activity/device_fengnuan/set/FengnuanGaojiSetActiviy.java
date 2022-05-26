package com.lipinkeji.cn.activity.device_fengnuan.set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_a.dialog.JiareqiMimaDialog;
import com.lipinkeji.cn.activity.device_fengnuan.FengnuanWendusetActivity;
import com.lipinkeji.cn.activity.device_shuinuan.FengNuanBaseNewActivity;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;
import com.lipinkeji.cn.util.Y;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.lipinkeji.cn.activity.device_shuinuan.FengNuanBaseNewActivity.FN_Send;

public class FengnuanGaojiSetActiviy extends FengNuanBaseNewActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rl_zhujicanshu)
    RelativeLayout rlZhujicanshu;
    @BindView(R.id.rl_fengyoubicanshu)
    RelativeLayout rlFengyoubicanshu;
    @BindView(R.id.rl_daqiyacanshu)
    RelativeLayout rlDaqiyacanshu;
    @BindView(R.id.tv_youbeng_16p)
    TextView tvYoubeng16p;
    @BindView(R.id.tv_youbeng_22p)
    TextView tvYoubeng22p;
    @BindView(R.id.tv_youbeng_28p)
    TextView tvYoubeng28p;
    @BindView(R.id.tv_youbeng_32p)
    TextView tvYoubeng32p;
    @BindView(R.id.tv_youbeng_35p)
    TextView tvYoubeng35p;
    @BindView(R.id.tv_youbeng_65p)
    TextView tvYoubeng65p;
    @BindView(R.id.rl_jingxiaoshang)
    RelativeLayout rl_jingxiaoshang;
    @BindView(R.id.rl_houtaifuwu)
    RelativeLayout rl_houtaifuwu;
    @BindView(R.id.rl_wendushezhi)
    RelativeLayout rl_wendushezhi;


    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.a_fengnuan_act_set_gaoji;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, FengnuanGaojiSetActiviy.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        showMimaDialog();
        initHuidiao();
//        selectYoubeng(0);
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
                if (textContent.equals("123456")) {
                    dialog.dismiss();
                } else {
                    Y.t("密码错误");
                }
            }
        });
        mimaDialog.show();
    }

    @OnClick({R.id.rl_wendushezhi, R.id.rl_jingxiaoshang, R.id.rl_houtaifuwu,
            R.id.rl_back, R.id.rl_zhujicanshu, R.id.rl_fengyoubicanshu,
            R.id.rl_daqiyacanshu, R.id.tv_youbeng_16p, R.id.tv_youbeng_22p,
            R.id.tv_youbeng_28p, R.id.tv_youbeng_32p, R.id.tv_youbeng_35p, R.id.tv_youbeng_65p})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_zhujicanshu:
                FengnuanZhujicanshuActivity.actionStart(mContext);
                break;
            case R.id.rl_fengyoubicanshu:
                //RatioActivity.actionStart(mContext);
                FengnuanFengyoubiActivity.actionStart(mContext);
                break;
            case R.id.rl_daqiyacanshu:
                FengnuanDaqiyaActivity.actionStart(mContext);
                break;
            case R.id.tv_youbeng_16p:
                selectYoubeng(0);
                break;
            case R.id.tv_youbeng_22p:
                selectYoubeng(1);
                break;
            case R.id.tv_youbeng_28p:
                selectYoubeng(2);
                break;
            case R.id.tv_youbeng_32p:
                selectYoubeng(3);
                break;
            case R.id.tv_youbeng_35p:
                selectYoubeng(4);
                break;
            case R.id.tv_youbeng_65p:
                selectYoubeng(5);
                break;
            case R.id.rl_jingxiaoshang:
                FengnuanJingxiaoshangActivity.actionStart(mContext);
                break;
            case R.id.rl_houtaifuwu:
                FengnuanHoutaiActivity.actionStart(mContext);
                break;
            case R.id.rl_wendushezhi:
                FengnuanWendusetActivity.actionStart(mContext);
                break;
            case R.id.rl_huifuchuchang_shezhi:
                String mingling = "M503.";
                //向水暖加热器发送获取实时数据
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg(mingling)
                        .setQos(2).setRetained(false)
                        .setTopic(FN_Send), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        showProgressDialog("设置中,请稍后...");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_FAILED, new TishiDialog.TishiDialogListener() {
                            @Override
                            public void onClickCancel(View v, TishiDialog dialog) {

                            }

                            @Override
                            public void onClickConfirm(View v, TishiDialog dialog) {

                            }

                            @Override
                            public void onDismiss(TishiDialog dialog) {

                            }
                        });
                        dialog.show();
                    }
                });
                break;
        }
    }

    private void initHuidiao() {

    }

    private void selectYoubeng(int pos) {
        tvYoubeng16p.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvYoubeng22p.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvYoubeng28p.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvYoubeng32p.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvYoubeng35p.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvYoubeng65p.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tvYoubeng16p.setTextColor(Color.BLACK);
        tvYoubeng22p.setTextColor(Color.BLACK);
        tvYoubeng28p.setTextColor(Color.BLACK);
        tvYoubeng32p.setTextColor(Color.BLACK);
        tvYoubeng35p.setTextColor(Color.BLACK);
        tvYoubeng65p.setTextColor(Color.BLACK);
        switch (pos) {
            case 0:
                tvYoubeng16p.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvYoubeng16p.setTextColor(Color.WHITE);
                break;
            case 1:
                tvYoubeng22p.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvYoubeng22p.setTextColor(Color.WHITE);
                break;
            case 2:
                tvYoubeng28p.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvYoubeng28p.setTextColor(Color.WHITE);
                break;
            case 3:
                tvYoubeng32p.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvYoubeng32p.setTextColor(Color.WHITE);
                break;
            case 4:
                tvYoubeng35p.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvYoubeng35p.setTextColor(Color.WHITE);
                break;
            case 5:
                tvYoubeng65p.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvYoubeng65p.setTextColor(Color.WHITE);
                break;
        }
    }
}
