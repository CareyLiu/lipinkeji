package com.lipinkeji.cn.activity.device_shuinuan.set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_shuinuan.ShuinuanBaseNewActivity;
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
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ShuinuanFengyoubiActivity extends ShuinuanBaseNewActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.bt_save)
    TextView btSave;
    @BindView(R.id.ed_1dan_fengji)
    EditText ed1danFengji;
    @BindView(R.id.ed_1dan_youbeng)
    EditText ed1danYoubeng;
    @BindView(R.id.ed_2dan_fengji)
    EditText ed2danFengji;
    @BindView(R.id.ed_2dan_youbeng)
    EditText ed2danYoubeng;
    @BindView(R.id.et_dianhuozhuansu)
    EditText etDianhuozhuansu;
    @BindView(R.id.et_dianhuoyoubeng)
    EditText etDianhuoyoubeng;
    @BindView(R.id.et_mubiaozhuansu)
    EditText etMubiaozhuansu;
    @BindView(R.id.et_mubiaoyoubeng)
    EditText etMubiaoyoubeng;
    @BindView(R.id.bt_huifuchuchang)
    TextView btHuifuchuchang;
    private String zhuangTai = "0";//0 第一次进入 1.保存基本数据 2.恢复出厂设置

    @Override
    public int getContentViewResId() {
        return R.layout.a_shuinuan_act_set_fengyoubi;
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
        Intent intent = new Intent(context, ShuinuanFengyoubiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initHuidiao();
        registerKtMqtt();
        showProgressDialog("正在加载风油比参数,请稍后...");
        btHuifuchuchang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zhuangTai = "2";
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

            if (zhuangTai.equals("0")) {

            } else {
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

//            //1档：风机转速
//            int fengji1 = Y.getInt(msg.substring(3, 8));
//            ed1danFengji.setText(fengji1 + "");
//            //1档：油泵频率
//            int you1_qian = Y.getInt(msg.substring(8, 10));
//            int you1_hou = Y.getInt(msg.substring(10, 12));
//            ed1danYoubeng.setText(you1_qian + "." + you1_hou);
//
//            //2档：风机转速
//            int fengji2 = Y.getInt(msg.substring(12, 17));
//            ed2danFengji.setText(fengji2 + "");
//            //2档：油泵频率
//            int you2_qian = Y.getInt(msg.substring(17, 19));
//            int you2_hou = Y.getInt(msg.substring(19, 21));
//            ed2danYoubeng.setText(you2_qian + "." + you2_hou);


            String dianHuoZhuanSu = msg.substring(3, 8).substring(1);//点火转速

            //  String dianHuoYouBeng = msg.substring(8, 12);//点火油泵
            int dianHuoYouBeng_dianQian = Y.getInt(msg.substring(8, 10));
            int dianHuoYouBeng_dianHou = Y.getInt(msg.substring(10, 12));

            String dianHuoYouBeng = dianHuoYouBeng_dianQian + "." + dianHuoYouBeng_dianHou;
            String muBiaoZhuanSu = msg.substring(12, 17).substring(1);//目标转速
            //String muBiaoYouBeng = msg.substring(17, 21);//目标油泵
            int muBiaoYouBeng_qian = Y.getInt(msg.substring(17, 19));
            int muBiaoYouBeng_hou = Y.getInt(msg.substring(19, 21));
            String muBiaoYouBeng = muBiaoYouBeng_qian + "." + muBiaoYouBeng_hou;

            String fengJiZhuanSu1 = msg.substring(21, 26).substring(1);//风机转速
            //  String fengJiYouBeng1 = msg.substring(26, 30);//风机油泵

            int fengjiyoubegn_qian = Y.getInt(msg.substring(26, 28));
            int fengjiyoubegn_hou = Y.getInt(msg.substring(28, 30));
            String fengJiYouBeng1 = fengjiyoubegn_qian + "." + fengjiyoubegn_hou;


            String fengJiZhuanSu2 = msg.substring(30, 35).substring(1);//风机转速2
            //   String fengJiYouBeng2 = msg.substring(35, 39);//风机油泵2

            int fengjiyoubeg2_qian = Y.getInt(msg.substring(35, 37));
            int fengjiyoubeng2_hou = Y.getInt(msg.substring(37, 39));
            String fengJiYouBeng2 = fengjiyoubeg2_qian + "." + fengjiyoubeng2_hou;

            etDianhuozhuansu.setText(dianHuoZhuanSu);
            etDianhuoyoubeng.setText(dianHuoYouBeng);
            etMubiaozhuansu.setText(muBiaoZhuanSu);
            etMubiaoyoubeng.setText(muBiaoYouBeng);

            ed1danFengji.setText(fengJiZhuanSu1);
            ed1danYoubeng.setText(fengJiYouBeng1);

            ed2danFengji.setText(fengJiZhuanSu2);
            ed2danYoubeng.setText(fengJiYouBeng2);

        }
    }

    /**
     * x
     * 注册订阅Mqtt
     */
    private void registerKtMqtt() {
        initHandlerStart();
        getHost();
    }

    private void initHandlerStart() {
        Message message = handlerStart.obtainMessage(1);
        handlerStart.sendMessageDelayed(message, 1000);
    }

    private void getHost() {
        //注册水暖加热器订阅
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(SN_Send)
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
                .setTopic(SN_Accept)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        //向水暖加热器发送获取实时数据
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("M_s111.")
                .setQos(2).setRetained(false)
                .setTopic(SN_Send), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerStart.removeMessages(1);
    }

    @OnClick({R.id.rl_back, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.bt_save:

                int fengji1 = Y.getInt(ed1danFengji.getText().toString());
                float youbeng1 = Y.getFloat(ed1danYoubeng.getText().toString());
                int fengji2 = Y.getInt(ed2danFengji.getText().toString());
                float youbeng2 = Y.getFloat(ed2danYoubeng.getText().toString());

                int dianhuozhuansu = Y.getInt(etDianhuozhuansu.getText().toString());
                float dianHuoYouBeng = Y.getFloat(etDianhuoyoubeng.getText().toString());

                int muBianZhuanSu = Y.getInt(etMubiaozhuansu.getText().toString());
                float muBiaoYouBeng = Y.getFloat(etMubiaoyoubeng.getText().toString());

                if (youbeng2 < youbeng1) {
                    UIHelper.ToastMessage(mContext, "请校验输出2档油泵频率大于1档油泵频率");
                    return;
                } else if (youbeng1 < muBiaoYouBeng) {
                    UIHelper.ToastMessage(mContext, "请校验1档油泵频率大于目标油泵频率");
                    return;
                } else if (muBiaoYouBeng < dianHuoYouBeng) {
                    UIHelper.ToastMessage(mContext, "请校验目标油泵频率大于点火油泵频率");
                    return;
                } else if (fengji2 < fengji1) {
                    UIHelper.ToastMessage(mContext, "请校验2档风机转速大于1档风机转速");
                    return;
                } else if (fengji1 < muBianZhuanSu) {
                    UIHelper.ToastMessage(mContext, "请校验1档风机转速大于目标转速");
                    return;
                } else if (muBianZhuanSu < dianhuozhuansu) {
                    UIHelper.ToastMessage(mContext, "请校验目标转速大于点火转速");
                    return;
                } else if (dianhuozhuansu < 500 || dianhuozhuansu > 10000) {
                    Y.t("请输入正确的点火转速");
                    return;
                } else if (dianHuoYouBeng < 0.5 || dianHuoYouBeng > 10.0) {
                    Y.t("请输入正确的点火油泵");
                    return;
                } else if (muBianZhuanSu < 1000 || muBianZhuanSu > 10000) {
                    Y.t("请输入正确目标转速");
                    return;
                } else if (muBiaoYouBeng < 0.5 || muBiaoYouBeng > 10.0) {
                    Y.t("请输入正确的目标油泵");
                    return;
                } else if (fengji1 < 1000 || fengji1 > 10000) {
                    Y.t("请输入1档正确的风机转速");
                    return;
                } else if (youbeng1 < 0.5 || youbeng1 > 16) {
                    Y.t("请输入1档正确的油泵频率");
                    return;
                } else if (fengji2 < 1000 || fengji2 > 10000) {
                    Y.t("请输入2档正确的风机转速");
                    return;
                } else if (youbeng2 < 0.5 || youbeng2 > 16) {
                    Y.t("请输入2档正确的油泵频率");
                    return;
                }

                TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
                    @Override
                    public void onClickCancel(View v, TishiDialog dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, TishiDialog dialog) {
                        //showProgressDialog("设置中，请稍后......");
                        zhuangTai = "1";


                        clickSave();

                    }

                    @Override
                    public void onDismiss(TishiDialog dialog) {

                    }
                });
                dialog.show();

                break;
        }
    }

    //恢复经销商主机参数
    private void huiFuChuChangSheZhi() {

        String mingling = "M_s10" + "2.";
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


    private void clickSave() {
        int fengji1 = Y.getInt(ed1danFengji.getText().toString());
        float youbeng1 = Y.getFloat(ed1danYoubeng.getText().toString());
        int fengji2 = Y.getInt(ed2danFengji.getText().toString());
        float youbeng2 = Y.getFloat(ed2danYoubeng.getText().toString());

        int dianhuozhuansu = Y.getInt(etDianhuozhuansu.getText().toString());
        float dianHuoYouBeng = Y.getFloat(etDianhuoyoubeng.getText().toString());

        int muBianZhuanSu = Y.getInt(etMubiaozhuansu.getText().toString());
        float muBiaoYouBeng = Y.getFloat(etMubiaoyoubeng.getText().toString());

        if (youbeng2 < youbeng1) {
            UIHelper.ToastMessage(mContext, "请校验输出2档油泵频率大于1档油泵频率");
            return;
        } else if (youbeng1 < muBiaoYouBeng) {
            UIHelper.ToastMessage(mContext, "请校验1档油泵频率大于目标油泵频率");
            return;
        } else if (muBiaoYouBeng < dianHuoYouBeng) {
            UIHelper.ToastMessage(mContext, "请校验目标油泵频率大于点火油泵频率");
            return;
        } else if (fengji2 < fengji1) {
            UIHelper.ToastMessage(mContext, "请校验2档风机转速大于1档风机转速");
            return;
        } else if (fengji1 < muBianZhuanSu) {
            UIHelper.ToastMessage(mContext, "请校验1档风机转速大于目标转速");
            return;
        } else if (muBianZhuanSu < dianhuozhuansu) {
            UIHelper.ToastMessage(mContext, "请校验目标转速大于点火转速");
            return;
        } else if (dianhuozhuansu < 1000 || dianhuozhuansu > 3000) {
            Y.t("请输入正确的点火转速");
            return;
        } else if (dianHuoYouBeng < 0.5 || dianHuoYouBeng > 10.0) {
            Y.t("请输入正确的点火油泵");
            return;
        } else if (muBianZhuanSu < 3000 || muBianZhuanSu > 7000) {
            Y.t("请输入正确目标转速");
            return;
        } else if (muBiaoYouBeng < 0.5 || muBiaoYouBeng > 10.0) {
            Y.t("请输入正确的目标油泵");
            return;
        } else if (fengji1 < 5000 || fengji1 > 9000) {
            Y.t("请输入1档正确的风机转速");
            return;
        } else if (youbeng1 < 0.5 || youbeng1 > 16) {
            Y.t("请输入1档正确的油泵频率");
            return;
        } else if (fengji2 < 6000 || fengji2 > 9900) {
            Y.t("请输入2档正确的风机转速");
            return;
        } else if (youbeng2 < 0.5 || youbeng2 > 16) {
            Y.t("请输入2档正确的油泵频率");
            return;
        }

        String dianHuoZhuanSu1 = "0" + dianhuozhuansu;
        String dianHuoYouBeng1 = Y.getMoney(dianHuoYouBeng).replace(".", "");

        String mubiaozhuansu = "0" + muBianZhuanSu;
        String mubiaobengyou1 = Y.getMoney(muBiaoYouBeng).replace(".", "");


        String feng1 = "0" + fengji1;
        String feng2 = "0" + fengji2;

        String you1 = Y.getMoney(youbeng1).replace(".", "");
        String you2 = Y.getMoney(youbeng2).replace(".", "");

        if (you1.length() == 3) {
            you1 = "0" + you1;
        }

        if (you2.length() == 3) {
            you2 = "0" + you2;
        }

        if (dianHuoYouBeng1.length() == 3) {
            dianHuoYouBeng1 = "0" + dianHuoYouBeng1;
        }

        if (mubiaobengyou1.length() == 3) {
            mubiaobengyou1 = "0" + mubiaobengyou1;
        }


        String mingling = "M_s12" + dianHuoZhuanSu1 + dianHuoYouBeng1 + mubiaozhuansu + mubiaobengyou1 + feng1 + you1 + feng2 + you2 + ".";
        //向水暖加热器发送获取实时数据
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg(mingling)
                .setQos(2).setRetained(false)
                .setTopic(SN_Send), new IMqttActionListener() {
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
}
