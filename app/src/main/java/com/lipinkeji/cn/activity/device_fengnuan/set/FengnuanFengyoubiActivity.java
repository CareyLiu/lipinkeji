package com.lipinkeji.cn.activity.device_fengnuan.set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_shuinuan.FengNuanBaseNewActivity;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class FengnuanFengyoubiActivity extends FengNuanBaseNewActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.bt_save)
    TextView btSave;
    @BindView(R.id.et_tongfeng_fengjizhuansu)
    EditText etTongfengFengjizhuansu;
    @BindView(R.id.et_tongfeng_youbengpinlv)
    EditText etTongfengYoubengpinlv;
    @BindView(R.id.et_1dang_fengjizhuansu)
    EditText et1dangFengjizhuansu;
    @BindView(R.id.et_1dang_youbengpinlv)
    EditText et1dangYoubengpinlv;
    @BindView(R.id.et_2dang_fengjizhuansu)
    EditText et2dangFengjizhuansu;
    @BindView(R.id.et_2dang_youbengpinlv)
    EditText et2dangYoubengpinlv;
    @BindView(R.id.et_3dang_fengjizhuansu)
    EditText et3dangFengjizhuansu;
    @BindView(R.id.et_3dang_youbengpinlv)
    EditText et3dangYoubengpinlv;
    @BindView(R.id.et_4dang_fengjizhuansu)
    EditText et4dangFengjizhuansu;
    @BindView(R.id.et_4dang_youbengpinlv)
    EditText et4dangYoubengpinlv;
    @BindView(R.id.et_5dang_fengjizhuansu)
    EditText et5dangFengjizhuansu;
    @BindView(R.id.et_5dang_youbengpinlv)
    EditText et5dangYoubengpinlv;
    @BindView(R.id.et_yure_fengjizhuansu)
    EditText etYureFengjizhuansu;
    @BindView(R.id.et_yure_youbengpinlv)
    EditText etYureYoubengpinlv;
    @BindView(R.id.et_chushi_fengjizhuansu)
    EditText etChushiFengjizhuansu;
    @BindView(R.id.et_chushi_youbengpinlv)
    EditText etChushiYoubengpinlv;
    @BindView(R.id.et_mubiao_fengjizhuansu)
    EditText etMubiaoFengjizhuansu;
    @BindView(R.id.et_mubiao_youbengpinlv)
    EditText etMubiaoYoubengpinlv;
    @BindView(R.id.bt_huifuchuchang)
    TextView btHuifuchuchang;
    @BindView(R.id.tv_baocun)
    TextView tvBaocun;

    @Override
    public int getContentViewResId() {
        return R.layout.a_fengnuan_act_set_fengyoubi;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_CAR_FEGNYOUBI) {//获得风油比 数据
                    //接收到信息
                    Log.i("fengyoubi", message.content.toString());

                    String messageStr = "H060000708000090900155100017511001951200355130045540006554500.";

                    String biaoQianMa = messageStr.substring(0, 1);//标签码
                    String tongFeng_fengJiZhuanSu = messageStr.substring(1, 5);//通风风机转速
                    String tongFeng_youBengPinlv = messageStr.substring(5, 8);//通风油泵频率
                    String yiDangFengJiZhuanSu = messageStr.substring(8, 12);//1档风机转速
                    String yiDangYouBengPinLv = messageStr.substring(12, 15);//1档油泵频率
                    String erDangFengJiZhuanSu = messageStr.substring(15, 19);//2档风机转速
                    String erDangYouBengPinLv = messageStr.substring(19, 22);//2档油泵频率
                    String sanDangFengJiZhuanSu = messageStr.substring(22, 26);//3档风机转速
                    String sanDangYouBengPinLv = messageStr.substring(26, 29);//3档油泵频率
                    String siDangFengJiZhuanSu = messageStr.substring(29, 32);//4档风机转速
                    String siDangYouBengPinLv = messageStr.substring(32, 35);//4档油泵频率
                    String wuDangFengJiZhuanSu = messageStr.substring(35, 39);//5档风机转速
                    String wuDangYouBengPinLv = messageStr.substring(39, 42);//5档油泵频率
                    String yuReFengJiZhuanSu = messageStr.substring(42, 46);//预热风机转速
                    String yuReYouBengPinLv = messageStr.substring(46, 49);//预热油泵频率
                    String chuShiFengJiZhuanSu = messageStr.substring(49, 53);//初始风机转速
                    String chuShiYouBengPinLv = messageStr.substring(53, 56);//初始油泵频率
                    String muBiaoFengJiZhuanSu = messageStr.substring(56, 60);//目标风机转速
                    String muBiaoYouBengPinLv = messageStr.substring(60, 63);//目标油泵频率


                    etTongfengFengjizhuansu.setText(tongFeng_fengJiZhuanSu);
                    etTongfengYoubengpinlv.setText(tongFeng_youBengPinlv);

                    et1dangFengjizhuansu.setText(yiDangFengJiZhuanSu);
                    et1dangYoubengpinlv.setText(yiDangYouBengPinLv);

                    et2dangFengjizhuansu.setText(erDangFengJiZhuanSu);
                    et2dangYoubengpinlv.setText(erDangYouBengPinLv);

                    et3dangFengjizhuansu.setText(sanDangFengJiZhuanSu);
                    et3dangYoubengpinlv.setText(sanDangYouBengPinLv);

                    et4dangFengjizhuansu.setText(siDangFengJiZhuanSu);
                    et4dangYoubengpinlv.setText(siDangYouBengPinLv);

                    et5dangFengjizhuansu.setText(wuDangFengJiZhuanSu);
                    et5dangYoubengpinlv.setText(wuDangYouBengPinLv);

                    etYureFengjizhuansu.setText(yuReFengJiZhuanSu);
                    etYureYoubengpinlv.setText(yuReYouBengPinLv);

                    etChushiFengjizhuansu.setText(chuShiFengJiZhuanSu);
                    etChushiYoubengpinlv.setText(chuShiYouBengPinLv);

                    etMubiaoFengjizhuansu.setText(muBiaoFengJiZhuanSu);
                    etMubiaoYoubengpinlv.setText(muBiaoYouBengPinLv);


                }
            }
        }));
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, FengnuanFengyoubiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        registerKtMqtt();
        initHuidiao();
        showProgressDialog("正在加载风油比参数,请稍后...");
        tvBaocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
                    @Override
                    public void onClickCancel(View v, TishiDialog dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, TishiDialog dialog) {
                        //showProgressDialog("设置中，请稍后......");
                        // zhuangTai = "1";


                        clickSave();

                    }

                    @Override
                    public void onDismiss(TishiDialog dialog) {

                    }
                });
                dialog.show();
            }
        });
    }

    public String tongFeng_FengJiZhuanSu,
            tongFeng_YouBengPinLv,
            yiDangFengJiZhuanSu,
            yiDangYouBengPingLv,
            erDangFengJiZhuanSu,
            erDangYouBengPingLv,
            sanDangFengJiZhuanSu,
            sanDangYouBengPinLv,
            siDangFengJiZhuanSu,
            siDangYouBengPinLv,
            wuDangFengJiZhuanSu,
            wuDangYouBengPinLv,
            yuReFengJiZhuanSu,
            yuReYouBengPinLv,
            chuShiFengJiZhuanSu,
            chuShiYouBengPinLv,
            muBiaoFengJiZhuanSu,
            muBiaoYouBengPinLv;

    public void clickSave() {
        String mingling = "M53" + tongFeng_FengJiZhuanSu + tongFeng_YouBengPinLv + yiDangFengJiZhuanSu +
                yiDangYouBengPingLv + erDangFengJiZhuanSu + erDangYouBengPingLv + sanDangFengJiZhuanSu +
                sanDangYouBengPinLv + siDangFengJiZhuanSu + siDangYouBengPinLv + wuDangFengJiZhuanSu +
                wuDangYouBengPinLv + yuReFengJiZhuanSu + yuReYouBengPinLv + chuShiFengJiZhuanSu +
                chuShiYouBengPinLv + muBiaoFengJiZhuanSu + muBiaoYouBengPinLv + ".";
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
    }


    private int time = 0;

    private Handler handlerStart = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                time++;
                if (time >= 20) {
                    showNodata();
                } else {
                    if (time == 5 || time == 10 || time == 15) {
                        getHost();
                    }
                    initHandlerStart();
                }
            }
            return false;
        }
    });

    /**
     * x
     * 注册订阅Mqtt
     */
    private void registerKtMqtt() {
        initHandlerStart();
        getHost();
    }


    private void getHost() {
        //注册水暖加热器订阅
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(FN_Send)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        //模拟水暖加热器订阅app
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(FN_Accept)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        //订阅和请求 mqtt  查询风油比参数

        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("M511.")
                .setQos(2).setRetained(false)
                .setTopic(FN_Send), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(CAR_NOTIFY.java:79)-onSuccess:-&gt;发布成功");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
            }
        });

    }

    private void showNodata() {
        time = 0;
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
        dialog.setTextTitle("提示");
        dialog.setTextContent("暂无风油比参数信息");
        dialog.setTextConfirm("关闭");
        dialog.show();
    }

    private void initHandlerStart() {
        Message message = handlerStart.obtainMessage(1);
        handlerStart.sendMessageDelayed(message, 1000);
    }
}
