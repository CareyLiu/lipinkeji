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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_shuinuan.FengNuanBaseNewActivity;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.app.UIHelper;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;
import com.lipinkeji.cn.util.Y;
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

    public String firstJinRu = "0";

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

                    if (firstJinRu.equals("0")) {

                    } else if (firstJinRu.equals("1")) {
                        firstJinRu = "0";
                        TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_SUCESS, new TishiDialog.TishiDialogListener() {
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


                    dismissProgressDialog();
                    handlerStart.removeMessages(1);
                    //接收到信息
                    Log.i("fengyoubi", message.content.toString());

                    //  String messageStr = "H060000708000090900155100017511001951200355130045540006554500.";
                    String messageStr = message.content.toString();
                    getData(messageStr);


                    dismissProgressDialog();
                }
            }
        }));
    }

    public void getData(String messageStr) {

        String biaoQianMa = messageStr.substring(0, 1);//标签码
        String tongFeng_fengJiZhuanSu = Y.quShouZiMuLing(messageStr.substring(1, 5));//通风风机转速
        String tongFeng_youBengPinlv = messageStr.substring(5, 8);//通风油泵频率
        String yiDangFengJiZhuanSu = Y.quShouZiMuLing(messageStr.substring(8, 12));//1档风机转速
        String yiDangYouBengPinLv = messageStr.substring(12, 15);//1档油泵频率
        String erDangFengJiZhuanSu = Y.quShouZiMuLing(messageStr.substring(15, 19));//2档风机转速
        String erDangYouBengPinLv = messageStr.substring(19, 22);//2档油泵频率
        String sanDangFengJiZhuanSu = Y.quShouZiMuLing(messageStr.substring(22, 26));//3档风机转速
        String sanDangYouBengPinLv = messageStr.substring(26, 29);//3档油泵频率
        String siDangFengJiZhuanSu = Y.quShouZiMuLing(messageStr.substring(29, 33));//4档风机转速
        String siDangYouBengPinLv = messageStr.substring(33, 36);//4档油泵频率
        String wuDangFengJiZhuanSu = Y.quShouZiMuLing(messageStr.substring(36, 40));//5档风机转速
        String wuDangYouBengPinLv = messageStr.substring(40, 43);//5档油泵频率
        String yuReFengJiZhuanSu = Y.quShouZiMuLing(messageStr.substring(43, 47));//预热风机转速
        String yuReYouBengPinLv = messageStr.substring(47, 50);//预热油泵频率
        String chuShiFengJiZhuanSu = Y.quShouZiMuLing(messageStr.substring(50, 54));//初始风机转速
        String chuShiYouBengPinLv = messageStr.substring(54, 57);//初始油泵频率
        String muBiaoFengJiZhuanSu = Y.quShouZiMuLing(messageStr.substring(57, 61));//目标风机转速
        String muBiaoYouBengPinLv = messageStr.substring(61, 64);//目标油泵频率

        etTongfengFengjizhuansu.setText(tongFeng_fengJiZhuanSu);


        etTongfengYoubengpinlv.setText(Y.fenJieString(tongFeng_youBengPinlv));
        et1dangFengjizhuansu.setText(yiDangFengJiZhuanSu);

        et1dangYoubengpinlv.setText(Y.fenJieString(yiDangYouBengPinLv));

        et2dangFengjizhuansu.setText(erDangFengJiZhuanSu);
        et2dangYoubengpinlv.setText(Y.fenJieString(erDangYouBengPinLv));

        et3dangFengjizhuansu.setText(sanDangFengJiZhuanSu);
        et3dangYoubengpinlv.setText(Y.fenJieString(sanDangYouBengPinLv));

        et4dangFengjizhuansu.setText(siDangFengJiZhuanSu);
        et4dangYoubengpinlv.setText(Y.fenJieString(siDangYouBengPinLv));

        et5dangFengjizhuansu.setText(wuDangFengJiZhuanSu);
        et5dangYoubengpinlv.setText(Y.fenJieString(wuDangYouBengPinLv));

        etYureFengjizhuansu.setText(yuReFengJiZhuanSu);
        etYureYoubengpinlv.setText(Y.fenJieString(yuReYouBengPinLv));

        etChushiFengjizhuansu.setText(chuShiFengJiZhuanSu);
        etChushiYoubengpinlv.setText(Y.fenJieString(chuShiYouBengPinLv));

        etMubiaoFengjizhuansu.setText(muBiaoFengJiZhuanSu);
        etMubiaoYoubengpinlv.setText(Y.fenJieString(muBiaoYouBengPinLv));

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
        btHuifuchuchang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mingling = "M502.";
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg(mingling)
                        .setQos(2).setRetained(false)
                        .setTopic(FN_Send), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {

                        showProgressDialog("设置中,请稍后...");
                        firstJinRu = "1";
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
        });
        tvBaocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
                    @Override
                    public void onClickCancel(View v, TishiDialog dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, TishiDialog dialog) {

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


        int tongFengZhuanSu = Y.getInt(etTongfengFengjizhuansu.getText().toString());
        Float tongFengPinLv = Y.getFloat(etTongfengYoubengpinlv.getText().toString());

        int yiDangZhuanSu = Y.getInt(et1dangFengjizhuansu.getText().toString());
        Float yiDangPinLv = Y.getFloat(et1dangYoubengpinlv.getText().toString());

        int erDangZhuanSu = Y.getInt(et2dangFengjizhuansu.getText().toString());
        Float erDangPinLv = Y.getFloat(et2dangYoubengpinlv.getText().toString());

        int sanDangZhuanSu = Y.getInt(et3dangFengjizhuansu.getText().toString());
        Float sanDangPinLv = Y.getFloat(et3dangYoubengpinlv.getText().toString());

        int siDangZhuanSu = Y.getInt(et4dangFengjizhuansu.getText().toString());
        Float siDangPinLv = Y.getFloat(et4dangYoubengpinlv.getText().toString());

        int wuDangZhuanSu = Y.getInt(et5dangFengjizhuansu.getText().toString());
        Float wuDangPinLv = Y.getFloat(et5dangYoubengpinlv.getText().toString());

        int yuReZhuanSu = Y.getInt(etYureFengjizhuansu.getText().toString());
        Float yuRePinLv = Y.getFloat(etYureYoubengpinlv.getText().toString());

        int chuShiZhuanSu = Y.getInt(etChushiFengjizhuansu.getText().toString());
        Float chuShiPinLv = Y.getFloat(etChushiYoubengpinlv.getText().toString());

        int muBiaoZhuanSu = Y.getInt(etMubiaoFengjizhuansu.getText().toString());
        Float muBiaoPinLv = Y.getFloat(etMubiaoYoubengpinlv.getText().toString());


        if (yiDangZhuanSu > erDangZhuanSu) {
            UIHelper.ToastMessage(mContext, "一档转速不能大于二档转速请您修改", Toast.LENGTH_LONG);
            return;
        } else if (erDangZhuanSu > sanDangZhuanSu) {
            UIHelper.ToastMessage(mContext, "二档转速不能大于三档转速请您修改", Toast.LENGTH_LONG);
            return;
        } else if (sanDangZhuanSu > siDangZhuanSu) {
            UIHelper.ToastMessage(mContext, "三档转速不能大于四档转速请您修改", Toast.LENGTH_LONG);
            return;
        } else if (siDangZhuanSu > wuDangZhuanSu) {
            UIHelper.ToastMessage(mContext, "四档转速不能大于五档转速请您修改", Toast.LENGTH_LONG);
            return;
        } else if (yuReZhuanSu > chuShiZhuanSu) {
            UIHelper.ToastMessage(mContext, "预热转速不能大于初始转速请您修改", Toast.LENGTH_LONG);
            return;
        } else if (chuShiZhuanSu > muBiaoZhuanSu) {
            UIHelper.ToastMessage(mContext, "初始转速不能大于目标转速请您修改", Toast.LENGTH_LONG);
            return;
        } else if (tongFengZhuanSu < 600 && tongFengZhuanSu > 6000) {
            UIHelper.ToastMessage(mContext, "请输入正确的通风风机转速范围", Toast.LENGTH_LONG);
            return;
        } else if (yiDangZhuanSu < 600 && yiDangZhuanSu > 6000) {
            UIHelper.ToastMessage(mContext, "请输入正确的1档风机转速范围", Toast.LENGTH_LONG);
            return;
        } else if (erDangZhuanSu < 600 && erDangZhuanSu > 6000) {
            UIHelper.ToastMessage(mContext, "请输入正确的2档风机转速范围", Toast.LENGTH_LONG);
            return;
        } else if (sanDangZhuanSu < 600 && sanDangZhuanSu > 6000) {
            UIHelper.ToastMessage(mContext, "请输入正确的3档风机转速范围", Toast.LENGTH_LONG);
            return;
        } else if (siDangZhuanSu < 600 && siDangZhuanSu > 6000) {
            UIHelper.ToastMessage(mContext, "请输入正确的4档风机转速范围", Toast.LENGTH_LONG);
            return;
        } else if (wuDangZhuanSu < 600 && wuDangZhuanSu > 6000) {
            UIHelper.ToastMessage(mContext, "请输入正确的5档风机转速范围", Toast.LENGTH_LONG);
            return;
        } else if (yuReZhuanSu < 600 && yuReZhuanSu > 6000) {
            UIHelper.ToastMessage(mContext, "请输入正确的预热风机转速范围", Toast.LENGTH_LONG);
            return;
        } else if (chuShiZhuanSu < 600 && chuShiZhuanSu > 6000) {
            UIHelper.ToastMessage(mContext, "请输入正确的初始风机转速范围", Toast.LENGTH_LONG);
            return;
        } else if (muBiaoZhuanSu < 600 && muBiaoZhuanSu > 6000) {
            UIHelper.ToastMessage(mContext, "请输入正确的目标风机转速范围", Toast.LENGTH_LONG);
            return;
        } else if (yiDangPinLv > erDangPinLv) {
            UIHelper.ToastMessage(mContext, "一档油泵频率不能大于二档油泵频率请您修改", Toast.LENGTH_LONG);
            return;
        } else if (erDangPinLv > sanDangPinLv) {
            UIHelper.ToastMessage(mContext, "二档油泵频率不能大于三档油泵频率请您修改", Toast.LENGTH_LONG);
            return;
        } else if (sanDangPinLv > siDangPinLv) {
            UIHelper.ToastMessage(mContext, "三档油泵频率不能大于四档油泵频率请您修改", Toast.LENGTH_LONG);
            return;
        } else if (siDangPinLv > wuDangPinLv) {
            UIHelper.ToastMessage(mContext, "四档油泵频率不能大于五档油泵频率请您修改", Toast.LENGTH_LONG);
            return;
        } else if (yuRePinLv > chuShiPinLv) {
            UIHelper.ToastMessage(mContext, "预热油泵频率不能大于初始油泵频率请您修改", Toast.LENGTH_LONG);
            return;
        } else if (chuShiPinLv > muBiaoPinLv) {
            UIHelper.ToastMessage(mContext, "初始油泵频率不能大于目标油泵频率请您修改", Toast.LENGTH_LONG);
            return;
        } else if (wuDangPinLv < muBiaoPinLv) {
            UIHelper.ToastMessage(mContext, "注意目标频率不能大于五档频率", Toast.LENGTH_LONG);
            return;
        } else if (wuDangZhuanSu < muBiaoZhuanSu) {
            UIHelper.ToastMessage(mContext, "注意目标转速不能大于五档转速", Toast.LENGTH_LONG);
            return;
        }


        tongFeng_FengJiZhuanSu = Y.zengJiaShouZiMuLing(etTongfengFengjizhuansu.getText().toString().trim());
        tongFeng_YouBengPinLv = Y.getFloatZhuanString(etTongfengYoubengpinlv.getText().toString().trim());

        yiDangFengJiZhuanSu = Y.zengJiaShouZiMuLing(et1dangFengjizhuansu.getText().toString().trim());
        yiDangYouBengPingLv = Y.getFloatZhuanString(et1dangYoubengpinlv.getText().toString().trim()).replace(".", "");

        erDangFengJiZhuanSu = Y.zengJiaShouZiMuLing(et2dangFengjizhuansu.getText().toString().trim());
        erDangYouBengPingLv = Y.getFloatZhuanString(et2dangYoubengpinlv.getText().toString().trim()).replace(".", "");

        sanDangFengJiZhuanSu = Y.zengJiaShouZiMuLing(et3dangFengjizhuansu.getText().toString().trim());
        sanDangYouBengPinLv = Y.getFloatZhuanString(et3dangYoubengpinlv.getText().toString().trim()).replace(".", "");

        siDangFengJiZhuanSu = Y.zengJiaShouZiMuLing(et4dangFengjizhuansu.getText().toString().trim());
        siDangYouBengPinLv = Y.getFloatZhuanString(et4dangYoubengpinlv.getText().toString().trim()).replace(".", "");

        wuDangFengJiZhuanSu = Y.zengJiaShouZiMuLing(et5dangFengjizhuansu.getText().toString().trim());
        wuDangYouBengPinLv = Y.getFloatZhuanString(et5dangYoubengpinlv.getText().toString().trim()).replace(".", "");

        yuReFengJiZhuanSu = Y.zengJiaShouZiMuLing(etYureFengjizhuansu.getText().toString().trim());
        yuReYouBengPinLv = Y.getFloatZhuanString(etYureYoubengpinlv.getText().toString().trim()).replace(".", "");

        chuShiFengJiZhuanSu = Y.zengJiaShouZiMuLing(etChushiFengjizhuansu.getText().toString().trim());
        chuShiYouBengPinLv = Y.getFloatZhuanString(etChushiYoubengpinlv.getText().toString().trim()).replace(".", "");

        muBiaoFengJiZhuanSu = Y.zengJiaShouZiMuLing(etMubiaoFengjizhuansu.getText().toString().trim());
        muBiaoYouBengPinLv = Y.getFloatZhuanString(etMubiaoYoubengpinlv.getText().toString().trim()).replace(".", "");


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
                firstJinRu = "1";
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerStart.removeMessages(1);
    }
}
