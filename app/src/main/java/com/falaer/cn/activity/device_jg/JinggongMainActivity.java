package com.falaer.cn.activity.device_jg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.falaer.cn.R;
import com.falaer.cn.activity.DiagnosisActivity;
import com.falaer.cn.activity.shuinuan.Y;
import com.falaer.cn.app.App;
import com.falaer.cn.app.AppManager;
import com.falaer.cn.app.BaseActivity;
import com.falaer.cn.app.ConstanceValue;
import com.falaer.cn.app.Notice;
import com.falaer.cn.config.MyApplication;
import com.falaer.cn.config.PreferenceHelper;
import com.falaer.cn.dialog.MyCarCaoZuoDialog_Notify;
import com.falaer.cn.dialog.newdia.TishiDialog;
import com.falaer.cn.util.DoMqttValue;
import com.falaer.cn.util.SoundPoolUtils;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;
import com.rairmmd.andmqtt.MqttUnSubscribe;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.falaer.cn.config.MyApplication.CARBOX_GETNOW;
import static com.falaer.cn.config.MyApplication.CAR_CTROL;
import static com.falaer.cn.config.MyApplication.CAR_NOTIFY;
import static com.falaer.cn.config.MyApplication.getAppContext;

public class JinggongMainActivity extends BaseActivity implements View.OnLongClickListener {

    @BindView(R.id.rl_set)
    RelativeLayout rl_set;
    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.ll_shuoming)
    LinearLayout ll_shuoming;
    @BindView(R.id.tv_dianya)
    TextView tv_dianya;
    @BindView(R.id.tv_chufengkouwendu)
    TextView tv_chufengkouwendu;
    @BindView(R.id.tv_danqianwendu)
    TextView tv_danqianwendu;
    @BindView(R.id.tv_yushe_wendu)
    TextView tv_yushe_wendu;
    @BindView(R.id.tv_daqiya)
    TextView tv_daqiya;
    @BindView(R.id.tv_haibagaodu)
    TextView tv_haibagaodu;
    @BindView(R.id.tv_hanyangliang)
    TextView tv_hanyangliang;
    @BindView(R.id.tv_set_dangwei)
    TextView tv_set_dangwei;
    @BindView(R.id.dangwei1)
    View dangwei1;
    @BindView(R.id.dangwei2)
    View dangwei2;
    @BindView(R.id.dangwei3)
    View dangwei3;
    @BindView(R.id.dangwei4)
    View dangwei4;
    @BindView(R.id.dangwei5)
    View dangwei5;
    @BindView(R.id.tv_set_wendu)
    TextView tv_set_wendu;
    @BindView(R.id.wendu1)
    View wendu1;
    @BindView(R.id.wendu2)
    View wendu2;
    @BindView(R.id.wendu3)
    View wendu3;
    @BindView(R.id.wendu4)
    View wendu4;
    @BindView(R.id.wendu5)
    View wendu5;
    @BindView(R.id.wendu6)
    View wendu6;
    @BindView(R.id.bt_up)
    ImageView bt_up;
    @BindView(R.id.bt_down)
    ImageView bt_down;
    @BindView(R.id.bt_on)
    ImageView bt_on;
    @BindView(R.id.bt_off)
    ImageView bt_off;
    @BindView(R.id.rl_yuanpan)
    RelativeLayout rl_yuanpan;
    @BindView(R.id.iv_mode)
    ImageView iv_mode;
    @BindView(R.id.tv_zaixian)
    TextView tv_zaixian;
    @BindView(R.id.bt_bengyou)
    ImageView bt_bengyou;
    @BindView(R.id.iv_xinhao)
    ImageView iv_xinhao;

    private boolean isFirst;//是否第一次进入
    private boolean isKaiji;//是否开机
    private boolean isOnActivity;//是否处于当前页面
    private boolean isHenwenMode;//true恒温模式   false档位模式
    private boolean isCanGetNs;//是否处于操作中   false 操作中、true 未操作
    private int typeZaixian;//1 在线、2 离线、3 连接中
    private int typeMingling;//0 发送实时数据、1 档位模式、2 恒温模式、3 关机、4 预泵油

    private String jiareqizhuangtai;//1.档位开机2.空调开机3.关机 4.水泵开机9.关机中6.预泵油7.预通风
    private String dangwei;
    private String yushewendu;
    private TishiDialog bengyouDialog;
    private MyCarCaoZuoDialog_Notify myCarCaoZuoDialog_notify;

    @Override
    public int getContentViewResId() {
        return R.layout.a_jinggong_act_main;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
        mImmersionBar.statusBarDarkFont(true);
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, JinggongMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initData();
        initMqtt();
        initHuidiao();
        initDialog();
        initHandlerNS();
    }

    private void initDialog() {
        bengyouDialog = new TishiDialog(mContext, TishiDialog.TYPE_XIAOXI, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {
                bengyouDialog.setTextConfirm("正在停止...");
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("M613.")
                        .setQos(2).setRetained(false)
                        .setTopic(CAR_CTROL), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });
            }

            @Override
            public void onDismiss(TishiDialog dialog) {

            }
        });
        bengyouDialog.setTextTitle("预泵油模式运行中");
        bengyouDialog.setTextContent("正在泵油");
        bengyouDialog.setTextConfirm("停止泵油");
        bengyouDialog.setTextCancel("");
        bengyouDialog.setCancelable(false);
        bengyouDialog.setDismissAfterClick(false);
        bengyouDialog.setCanceledOnTouchOutside(false);

        myCarCaoZuoDialog_notify = new MyCarCaoZuoDialog_Notify(getAppContext(), new MyCarCaoZuoDialog_Notify.OnDialogItemClickListener() {
            @Override
            public void clickLeft() {
                finish();
            }

            @Override
            public void clickRight() {
                DiagnosisActivity.actionStart(mContext);

            }
        });
        myCarCaoZuoDialog_notify.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG);
    }

    private void initData() {
        PreferenceHelper.getInstance(mContext).putString(App.CHOOSE_KONGZHI_XIANGMU, DoMqttValue.FENGNUAN);
        isFirst = true;
        isKaiji = false;
        isHenwenMode = false;
        jiareqizhuangtai = "3";
        typeZaixian = 3;
        isCanGetNs = true;
        yushewendu = "0";
        dangwei = "1";

        MyApplication.mqttDingyue.add(CAR_NOTIFY);
        MyApplication.mqttDingyue.add(CAR_CTROL);
        MyApplication.mqttDingyue.add(CARBOX_GETNOW);

        bt_on.setOnLongClickListener(this);
        bt_off.setOnLongClickListener(this);
        bt_bengyou.setOnLongClickListener(this);
    }

    private void initMqtt() {
        time = 0;
        typeMingling = 0;
        registerKtMqtt();
        sendMingling();
        initHandlerMingling();
    }

    public void registerKtMqtt() {
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(CAR_NOTIFY)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        //获得车辆的实时数据和基本信息
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(CARBOX_GETNOW)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        //控制车辆
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(CAR_CTROL)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }

    private void getNData() {
        //向风暖加热器发送获取实时数据
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("N.")
                .setQos(2)
                .setTopic(CAR_CTROL)
                .setRetained(false), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Y.i("Rair", "订阅风暖实时数据");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Y.i("Rair", "订阅风暖实时数据失败");
            }
        });
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_CAR_J_M) {
                    String msg = message.content.toString();
                    getData(msg);
                } else if (message.type == ConstanceValue.MSG_JIEBANG) {
                    finish();
                }
            }
        }));
    }

    private void getData(String msg) {//接收到信息
        isCanGetNs = true;
        typeZaixian = 1;
        handlerStart.removeMessages(1);

        String messageData = msg.substring(1);
        Y.e("风暖的实时数据是" + messageData + "   " + messageData.length());

        //当前档位1至5档	    1
        dangwei = messageData.substring(0, 1);

        //预设温度1至32度	2
        yushewendu = messageData.substring(1, 3);

        //1.档位开机2.空调开机3.关机 4.水泵开机9.关机中6.预泵油7.预通风	1
        jiareqizhuangtai = messageData.substring(3, 4);

        //当前温度 256=25.6     3
        String dangqianwendu = Y.getInt(messageData.substring(4, 6)) + "." + messageData.substring(6, 7);

        //信号强度 00-35    2
        String xinhaoqiangdu = messageData.substring(7, 9);

        //预留字段，占位    1
        String yuliu = messageData.substring(9, 10);

        //电压->0253 = 25.3    4
        String dianya = Y.getInt(messageData.substring(10, 13)) + "." + messageData.substring(13, 14);

        //风机转速->13245    5
        String fenjizhuansu = messageData.substring(14, 19);

        //加热塞功率->0264=26.4	    4
        String jiaresaigonglv = Y.getInt(messageData.substring(19, 22)) + "." + messageData.substring(22, 23);

        //油泵频率->0265=26.5       4
        String youbengpinlv = Y.getInt(messageData.substring(23, 26)) + "." + messageData.substring(26, 27);

        //入风口温度->例如:-026       4
        String wendu_rufengkou = messageData.substring(27, 31);

        //出风口温度->0128           4
        String wendu_chufengkou = messageData.substring(31, 35);

        //加热器故障码:通用->01至18      2
        String guzhangdaima = messageData.substring(35, 37);

        //水泵开关 0:关 1:开 a:无水泵            1
        String shuibengzhuangtai = messageData.substring(37, 38);

        //工作时长(单位:小时)           6
        String gongzuoshichang = messageData.substring(38, 44);

        //加热器故障码:定制->01至18      2
        String guzhangdaima_dingzhi = messageData.substring(44, 46);

        //大气压kpa         3
        String daqiya = messageData.substring(46, 49);

        //海拔高度m         4
        String haibagaodu = messageData.substring(49, 53);

        //含氧量g/立方米      2
        String hanyangliang = messageData.substring(53);


        tv_dianya.setText("工作电压：" + dianya + "v");
        tv_danqianwendu.setText("当前温度：" + Y.getInt(wendu_rufengkou) + "℃");
        tv_chufengkouwendu.setText("出风口温度：" + Y.getInt(wendu_chufengkou) + "℃");
        tv_daqiya.setText("大气压：" + Y.getInt(daqiya) + "kpa");
        tv_haibagaodu.setText("海拔高度：" + Y.getInt(haibagaodu) + "m");
        tv_hanyangliang.setText("含氧量：" + Y.getInt(hanyangliang) + "g/m³");

        if (jiareqizhuangtai.equals("1")) {
            setUiShoudong();
        } else if (jiareqizhuangtai.equals("2")) {
            setUiHengwen();
        } else if (jiareqizhuangtai.equals("3")) {
            setUiGuanji();
        } else if (jiareqizhuangtai.equals("6")) {
            setUiBengyou();
        } else {
            setUiGuanji();
        }

        if (!StringUtils.isEmpty(guzhangdaima)) {
            guzhangdaima = 0 <= guzhangdaima.indexOf("a") ? "" : String.valueOf(Integer.parseInt(guzhangdaima));
        }

        if (!StringUtils.isEmpty(guzhangdaima)) {
            Activity currentActivity = AppManager.getAppManager().currentActivity();
            if (currentActivity != null) {
                if (!currentActivity.getClass().getSimpleName().equals(DiagnosisActivity.class.getSimpleName())) {
                    if (!myCarCaoZuoDialog_notify.isShowing()) {
                        myCarCaoZuoDialog_notify.show();
                    }
                }
            }
        } else {
            myCarCaoZuoDialog_notify.dismiss();
        }

        if (xinhaoqiangdu.equals("aa")) {
            iv_xinhao.setImageResource(R.mipmap.fengnuan_icon_signal2);
        } else {
            int xinhao = Y.getInt(xinhaoqiangdu);//信号强度
            if (xinhao >= 15 && xinhao <= 19) {
                iv_xinhao.setImageResource(R.mipmap.fengnuan_icon_signal2);
            } else if (xinhao >= 20 && xinhao <= 25) {
                iv_xinhao.setImageResource(R.mipmap.fengnuan_icon_signal3);
            } else if (xinhao >= 26 && xinhao <= 35) {
                iv_xinhao.setImageResource(R.mipmap.fengnuan_icon_signal4);
            } else {
                iv_xinhao.setImageResource(R.mipmap.fengnuan_icon_signal1);
            }
        }
    }

    private void setwendu(String yushewendu) {
        wendu1.setBackgroundResource(R.drawable.jinggong_dangwei_nor);
        wendu2.setBackgroundResource(R.drawable.jinggong_dangwei_nor);
        wendu3.setBackgroundResource(R.drawable.jinggong_dangwei_nor);
        wendu4.setBackgroundResource(R.drawable.jinggong_dangwei_nor);
        wendu5.setBackgroundResource(R.drawable.jinggong_dangwei_nor);
        wendu6.setBackgroundResource(R.drawable.jinggong_dangwei_nor);
        tv_set_wendu.setText("--℃");
        tv_yushe_wendu.setText("预设温度：" + "--" + "℃");
        int wendu = Y.getInt(yushewendu);
        if (isKaiji) {
            if (wendu >= 1 && wendu <= 5) {
                wendu6.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
            } else if (wendu >= 6 && wendu <= 11) {
                wendu6.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu5.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
            } else if (wendu >= 12 && wendu <= 16) {
                wendu6.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu5.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu4.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
            } else if (wendu >= 17 && wendu <= 22) {
                wendu6.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu5.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu4.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu3.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
            } else if (wendu >= 23 && wendu <= 27) {
                wendu6.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu5.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu4.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu3.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu2.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
            } else if (wendu >= 27 && wendu <= 33) {
                wendu6.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu5.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu4.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu3.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu2.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                wendu1.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
            }
            tv_set_wendu.setText(yushewendu + "℃");
            tv_yushe_wendu.setText("预设温度：" + yushewendu + "℃");
        }
    }

    private void setDangwei(String oper_dang) {
        dangwei1.setBackgroundResource(R.drawable.jinggong_dangwei_nor);
        dangwei2.setBackgroundResource(R.drawable.jinggong_dangwei_nor);
        dangwei3.setBackgroundResource(R.drawable.jinggong_dangwei_nor);
        dangwei4.setBackgroundResource(R.drawable.jinggong_dangwei_nor);
        dangwei5.setBackgroundResource(R.drawable.jinggong_dangwei_nor);
        tv_set_dangwei.setText("--档");

        if (isKaiji) {
            if (oper_dang.equals("1")) {
                tv_set_dangwei.setText("1档");
                dangwei1.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
            } else if (oper_dang.equals("2")) {
                tv_set_dangwei.setText("2档");
                dangwei1.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                dangwei2.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
            } else if (oper_dang.equals("3")) {
                tv_set_dangwei.setText("3档");
                dangwei1.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                dangwei2.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                dangwei3.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
            } else if (oper_dang.equals("4")) {
                tv_set_dangwei.setText("4档");
                dangwei1.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                dangwei2.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                dangwei3.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                dangwei4.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
            } else if (oper_dang.equals("5")) {
                tv_set_dangwei.setText("5档");
                dangwei1.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                dangwei2.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                dangwei3.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                dangwei4.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
                dangwei5.setBackgroundResource(R.drawable.jinggong_dangwei_sel);
            }
        }
    }


    @OnClick({R.id.iv_mode, R.id.rl_back, R.id.rl_set, R.id.ll_shuoming, R.id.bt_up, R.id.bt_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_set:
                JinggongSetActivity.actionStart(mContext);
                break;
            case R.id.ll_shuoming:
                JinggongShuomingActivity.actionStart(mContext);
                break;
            case R.id.bt_up:
                clickUp();
                break;
            case R.id.bt_down:
                clickDown();
                break;
            case R.id.iv_mode:
                clickModeChange();
                break;

        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (typeZaixian == 1) {
            time = 0;
            handlerStart.removeMessages(1);

            switch (v.getId()) {
                case R.id.bt_on:
                    kaiji();
                    break;
                case R.id.bt_off:
                    guanji();
                    break;
                case R.id.bt_bengyou:
                    clickBengyou();
                    break;
            }
        } else if (typeZaixian == 3) {
            Y.t("正在连接设备，请稍后...");
        } else if (typeZaixian == 2) {
            showTishiDialog();
        }
        return false;
    }

    private void clickDown() {
        if (typeZaixian != 1) {
            return;
        }

        if (!isKaiji) {
            return;
        }

        if (isHenwenMode) {
            int wendu = Y.getInt(yushewendu);
            if (wendu <= 0) {
                Y.t("预设温度已达最低值，无法降低！");
                return;
            }

            wendu--;

            if (wendu < 10) {
                yushewendu = "0" + wendu;
            } else {
                yushewendu = "" + wendu;
            }

            setwendu(yushewendu);
            String mingling = 50 + wendu + "";

            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M6" + mingling + ".")
                    .setQos(2).setRetained(false)
                    .setTopic(CAR_CTROL), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else {
            if (dangwei.equals("1")) {
                Y.t("已是最低档位，无法降档！");
                return;
            }

            switch (dangwei) {
                case "2":
                    dangwei = "1";
                    break;
                case "3":
                    dangwei = "2";
                    break;
                case "4":
                    dangwei = "3";
                    break;
                case "5":
                    dangwei = "4";
                    break;
            }
            setDangwei(dangwei);
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M62" + dangwei + ".")
                    .setQos(2).setRetained(false)
                    .setTopic(CAR_CTROL), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        }
    }

    private void clickUp() {
        if (typeZaixian != 1) {
            return;
        }

        if (!isKaiji) {
            return;
        }

        if (isHenwenMode) {
            int wendu = Y.getInt(yushewendu);
            if (wendu >= 33) {
                Y.t("预设温度已达最高值，无法调高！");
                return;
            }

            if (wendu < 33) {
                wendu++;
            }

            if (wendu < 10) {
                yushewendu = "0" + wendu;
            } else {
                yushewendu = "" + wendu;
            }
            setwendu(yushewendu);
            String mingling = 50 + wendu + "";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M6" + mingling + ".")
                    .setQos(2).setRetained(false)
                    .setTopic(CAR_CTROL), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else {
            if (dangwei.equals("5")) {
                Y.t("已是最高档位，无法升档！");
                return;
            }

            switch (dangwei) {
                case "1":
                    dangwei = "2";
                    break;
                case "2":
                    dangwei = "3";
                    break;
                case "3":
                    dangwei = "4";
                    break;
                case "4":
                    dangwei = "5";
                    break;
            }
            setDangwei(dangwei);
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M62" + dangwei + ".")
                    .setQos(2).setRetained(false)
                    .setTopic(CAR_CTROL), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        }
    }

    private void kaiji() {
        if (isKaiji) {
            return;
        }

        if (isHenwenMode) {
            SoundPoolUtils.soundPool(mContext, R.raw.mode_temp);
            typeMingling = 2;//恒温模式
            setUiHengwen();
        } else {
            SoundPoolUtils.soundPool(mContext, R.raw.mode_gear);
            setUiShoudong();
            typeMingling = 1;//档位模式
        }
        sendMingling();
        initHandlerMingling();
    }

    private void guanji() {
        if (!isKaiji) {
            return;
        }

        SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_start_off);
        typeMingling = 3;
        setUiGuanji();
        sendMingling();
        initHandlerMingling();
    }

    private void clickModeChange() {
        if (!isKaiji) {
            isHenwenMode = !isHenwenMode;
            if (isHenwenMode) {
                iv_mode.setImageResource(R.mipmap.jg_home_hengwen_sel);
            } else {
                iv_mode.setImageResource(R.mipmap.jg_home_hengwen_nor);
            }
        } else {
            Y.t("请关机之后再切换模式！");
        }
    }

    private void clickBengyou() {
        if (isKaiji) {
            Y.t("请关机后再执行泵油操作！");
            return;
        }

        SoundPoolUtils.soundPool(mContext, R.raw.yubengyou);
        typeMingling = 4;//预泵油
        setUiBengyou();
        sendMingling();
        initHandlerMingling();
    }

    private void setUiShoudong() {
        isKaiji = true;
        isHenwenMode = false;
        bt_off.setImageResource(R.mipmap.jg_off_nor);
        bt_on.setImageResource(R.mipmap.jg_on_sel);
        setDangwei(dangwei);
        iv_mode.setImageResource(R.mipmap.jg_home_hengwen_nor);
    }

    private void setUiHengwen() {
        isKaiji = true;
        isHenwenMode = true;
        bt_off.setImageResource(R.mipmap.jg_off_nor);
        bt_on.setImageResource(R.mipmap.jg_on_sel);
        setwendu(yushewendu);
        iv_mode.setImageResource(R.mipmap.jg_home_hengwen_sel);
    }

    private void setUiGuanji() {
        isKaiji = false;
        bt_off.setImageResource(R.mipmap.jg_off_sel);
        bt_on.setImageResource(R.mipmap.jg_on_nor);
        bt_bengyou.setImageResource(R.mipmap.jg_icon_bengyou_nor);
        if (bengyouDialog.isShowing()) {
            bengyouDialog.dismiss();
        }

        if (isHenwenMode) {
            setwendu(yushewendu);
        } else {
            setDangwei(dangwei);
        }
    }

    private void setUiBengyou() {
        isKaiji = false;
        bt_bengyou.setImageResource(R.mipmap.jg_icon_bengyou_sel);
        bengyouDialog.setTextConfirm("停止泵油");
        if (bengyouDialog != null && !bengyouDialog.isShowing()) {
            bengyouDialog.show();
        }
    }

    private void sendMingling() {
        isCanGetNs = false;
        if (typeMingling == 0) {//发送实时数据
            getNData();
        } else if (typeMingling == 1) {//档位模式
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M611.")
                    .setQos(2).setRetained(false)
                    .setTopic(CAR_CTROL), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 2) {//恒温模式
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M612.")
                    .setQos(2).setRetained(false)
                    .setTopic(CAR_CTROL), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 3) {//关机
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M613.")
                    .setQos(2).setRetained(false)
                    .setTopic(CAR_CTROL), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 4) {//预泵油
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M616.")
                    .setQos(2).setRetained(false)
                    .setTopic(CAR_CTROL), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        }
    }

    private int time = 0;

    private Handler handlerStart = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            time += 5;
            switch (msg.what) {
                case 1:
                    if (time >= 30) {
                        showTishiDialog();
                    } else {
                        sendMingling();
                        initHandlerMingling();
                    }
                    break;
            }
            Y.e(msg.what + "  的时间时多少啊  " + time);
            return false;
        }
    });

    private void initHandlerMingling() {
        Message message = handlerStart.obtainMessage(1);
        handlerStart.sendMessageDelayed(message, 5000);
    }

    private void showTishiDialog() {
        handlerStart.removeMessages(1);
        isCanGetNs = true;
        typeZaixian = 2;
        time = 0;
        dismissProgressDialog();
        TishiDialog tishiDialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {
                initMqtt();
            }

            @Override
            public void onDismiss(TishiDialog dialog) {

            }
        });
        tishiDialog.setTextTitle("提示：网络信号异常");
        tishiDialog.setTextContent("请检查设备情况。1:设备是否接通电源 2:设备信号灯是否闪烁 3:设备是否有损坏 4:手机是否开启网络，如已确认以上问题，请重新尝试。");
        tishiDialog.setTextConfirm("重试");
        tishiDialog.setTextCancel("忽略");
        tishiDialog.show();
    }


    private Handler handlerTime10 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    if (isOnActivity && isCanGetNs) {
                        getNData();
                    }
                    initHandlerNS();
                    break;
            }
            return false;
        }
    });

    private void initHandlerNS() {
        Message message = handlerTime10.obtainMessage(1);
        handlerTime10.sendMessageDelayed(message, 10000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnActivity = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnActivity = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerStart.removeMessages(1);
        handlerTime10.removeMessages(1);

        PreferenceHelper.getInstance(mContext).removeKey(App.CHOOSE_KONGZHI_XIANGMU);
        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(CAR_NOTIFY), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(MainActivity.java:93)-onSuccess:-&gt;取消订阅成功");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:98)-onFailure:-&gt;取消订阅失败");
            }
        });

        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(CARBOX_GETNOW), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(MainActivity.java:93)-onSuccess:-&gt;取消订阅成功");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:98)-onFailure:-&gt;取消订阅失败");
            }
        });

        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(CAR_CTROL), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(MainActivity.java:93)-onSuccess:-&gt;取消订阅成功");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:98)-onFailure:-&gt;取消订阅失败");
            }
        });


        for (int i = 0; i < MyApplication.mqttDingyue.size(); i++) {
            if (MyApplication.mqttDingyue.get(i).equals(CAR_NOTIFY)) {
                MyApplication.mqttDingyue.remove(i);
            }
        }

        for (int i = 0; i < MyApplication.mqttDingyue.size(); i++) {
            if (MyApplication.mqttDingyue.get(i).equals(CARBOX_GETNOW)) {
                MyApplication.mqttDingyue.remove(i);
            }
        }

        for (int i = 0; i < MyApplication.mqttDingyue.size(); i++) {
            if (MyApplication.mqttDingyue.get(i).equals(CAR_CTROL)) {
                MyApplication.mqttDingyue.remove(i);
            }
        }
    }
}
