package com.lipinkeji.cn.activity.device_shuinuan.set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.DianHuoSaiActivity;
import com.lipinkeji.cn.activity.YouBengActivity;
import com.lipinkeji.cn.activity.device_a.dialog.JiareqiMimaDialog;
import com.lipinkeji.cn.activity.device_shuinuan.ShuinuanWendusetActivity;
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

import static com.lipinkeji.cn.activity.device_shuinuan.ShuinuanBaseNewActivity.SN_Send;

public class ShuinuanGaojiSetActiviy extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rl_fengyoubicanshu)
    RelativeLayout rlFengyoubicanshu;
    @BindView(R.id.rl_jingxiaoshang)
    RelativeLayout rlJingxiaoshang;
    @BindView(R.id.rl_houtaifuwu)
    RelativeLayout rlHoutaifuwu;
    @BindView(R.id.rl_daqiyacanshu)
    RelativeLayout rlDaqiyacanshu;
    @BindView(R.id.rl_wendushezhi)
    RelativeLayout rl_wendushezhi;
    @BindView(R.id.rl_kaijimoshishezhi)
    RelativeLayout rlKaijimoshishezhi;
    @BindView(R.id.rl_dianhuosai_shezhi)
    RelativeLayout rlDianhuosaiShezhi;
    @BindView(R.id.rl_youbeng_shezhi)
    RelativeLayout rlYoubengShezhi;
    @BindView(R.id.rl_huifuchuchang)
    RelativeLayout rlHuifuchuchang;

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

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_SN_DATA) {
                    String msg = message.content.toString();
                    getData(msg);
                }
            }
        }));
    }
    private void getData(String msg) {
        if (msg.contains("h_s")) {
            dismissProgressDialog();
//                TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_SUCESS, new TishiDialog.TishiDialogListener() {
//                    @Override
//                    public void onClickCancel(View v, TishiDialog dialog) {
//
//                    }
//
//                    @Override
//                    public void onClickConfirm(View v, TishiDialog dialog) {
//
//                    }
//
//                    @Override
//                    public void onDismiss(TishiDialog dialog) {
//
//                    }
//                });
//                dialog.show();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        showMimaDialog();
        initHuidiao();
        rlHuifuchuchang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
                    @Override
                    public void onClickCancel(View v, TishiDialog dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, TishiDialog dialog) {
                        huiFuChuChangSheZhi();
                        showProgressDialog("正在恢复出厂,请稍后...");
                    }

                    @Override
                    public void onDismiss(TishiDialog dialog) {

                    }
                });
                dialog.show();

            }
        });
    }




    //恢复经销商主机参数
    private void huiFuChuChangSheZhi() {

        String mingling = "M_s10" + "5.";
        Y.e("我发送的数据是什么啊啊啊  " + mingling);

        //向水暖加热器发送获取实时数据
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg(mingling)
                .setQos(2).setRetained(false)
                .setTopic(SN_Send), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

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


    @OnClick({R.id.rl_daqiyacanshu, R.id.rl_back, R.id.rl_fengyoubicanshu,
            R.id.rl_jingxiaoshang, R.id.rl_houtaifuwu, R.id.rl_wendushezhi,
            R.id.rl_kaijimoshishezhi, R.id.rl_dianhuosai_shezhi, R.id.rl_youbeng_shezhi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_fengyoubicanshu:
                ShuinuanFengyoubiActivity.actionStart(mContext);
                break;
            case R.id.rl_jingxiaoshang:
                ShuinuanJingxiaoshangActivity.actionStart(mContext);
                break;
            case R.id.rl_houtaifuwu:
                ShuinuanHoutaiActivity.actionStart(mContext);
                break;
            case R.id.rl_daqiyacanshu:
                ShuinuanDaqiyaActivity.actionStart(mContext);
                break;
            case R.id.rl_wendushezhi:
                ShuinuanWendusetActivity.actionStart(mContext);
                break;
            case R.id.rl_kaijimoshishezhi:
                //ShuiNuanKaiJiMoShiSheZhiActivity.actionStart(mContext);
                break;
            case R.id.rl_dianhuosai_shezhi:
                DianHuoSaiActivity.actionStart(mContext);
                break;

            case R.id.rl_youbeng_shezhi:
                YouBengActivity.actionStart(mContext);
                break;

        }
    }


}
