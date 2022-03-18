package com.lipinkeji.cn.activity.device_fengnuan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lipinkeji.cn.R;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.util.Y;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.gyf.barlibrary.ImmersionBar;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.lipinkeji.cn.config.MyApplication.CARBOX_GETNOW;
import static com.lipinkeji.cn.config.MyApplication.CAR_CTROL;
import static com.lipinkeji.cn.config.MyApplication.CAR_NOTIFY;

public class FengnuanStateActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.tv_mode)
    TextView tv_mode;
    @BindView(R.id.tv_dianya)
    TextView tv_dianya;
    @BindView(R.id.tv_rufengkouwendu)
    TextView tv_rufengkouwendu;
    @BindView(R.id.tv_chufengkouwendu)
    TextView tv_chufengkouwendu;
    @BindView(R.id.tv_fengjizhuansu)
    TextView tv_fengjizhuansu;
    @BindView(R.id.tv_youbengpinlv)
    TextView tv_youbengpinlv;
    @BindView(R.id.tv_jiaresaigonglv)
    TextView tv_jiaresaigonglv;
    @BindView(R.id.tv_daqiya)
    TextView tv_daqiya;
    @BindView(R.id.tv_haibagaodu)
    TextView tv_haibagaodu;
    @BindView(R.id.tv_hanyangliang)
    TextView tv_hanyangliang;
    @BindView(R.id.tv_xinhaoqiangdu)
    TextView tv_xinhaoqiangdu;

    private String ccid;
    private boolean isZaixian;

    @Override
    public int getContentViewResId() {
        return R.layout.a_fengnuan_act_set_state;
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
        Intent intent = new Intent(context, FengnuanStateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        isZaixian = false;
        initMqtt();
        initHuidiao();
        initHandlerNS();
    }

    private void initMqtt() {
        initHandlerStart();
        registerKtMqtt();
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

        getNData();
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

    private void initHandlerStart() {
        Message message = handlerStart.obtainMessage(1);
        handlerStart.sendMessageDelayed(message, 1000);
    }


    private int time = 0;

    private Handler handlerStart = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            time++;
            switch (msg.what) {
                case 1:
                    if (!isZaixian) {
                        if (time >= 30) {

                        } else {
                            if (time == 5 || time == 10 || time == 15 || time == 20 || time == 25) {
                                registerKtMqtt();
                            }
                            initHandlerStart();
                        }
                    } else {
                        time = 0;
                    }
                    break;
            }
            return false;
        }
    });

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
        isZaixian = true;

        String messageData = msg.substring(1);
        Y.e("风暖的实时数据是" + messageData + "   " + messageData.length());

        //当前档位1至5档	    1
        String dangwei = messageData.substring(0, 1);

        //预设温度1至32度	2
        String yushewendu = messageData.substring(1, 3);

        //1.档位开机2.空调开机3.关机 4.水泵开机9.关机中6.预泵油7.预通风	1
        String jiareqizhuangtai = messageData.substring(3, 4);

        //当前温度 256=25.6     3
        String dangqianwendu = Y.getInt(messageData.substring(4, 6)) + "." + messageData.substring(6, 7);

        //信号强度 00-35    2
        String xinhaoqiangdu = messageData.substring(7, 9);

        //预留字段，占位    1
        String yuliu = messageData.substring(9, 10);

        //电压->0253 = 25.3    4
        String dianya = Y.getInt(messageData.substring(10, 13)) + "." + messageData.substring(13, 14);

        //风机转速->13245    5
        int fenjizhuansu = Y.getInt(messageData.substring(14, 19));
        tv_fengjizhuansu.setText(fenjizhuansu + "r");

        //加热塞功率->0264=26.4	    4
        String jiaresaigonglv = Y.getInt(messageData.substring(19, 22)) + "." + messageData.substring(22, 23);

        //油泵频率->0265=26.5       4
        String a = messageData.substring(26, 27);
        String youbengpinlv;
        if (a.equals("a")) {
            tv_youbengpinlv.setText("0Hz");
        } else {
            youbengpinlv = Y.getInt(messageData.substring(23, 26)) + "." + messageData.substring(26, 27);
            tv_youbengpinlv.setText(youbengpinlv + "Hz");
        }

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
        String hanyangliang = messageData.substring(53, 56);

        if (jiareqizhuangtai.equals("1")) {
            tv_mode.setText("档位模式");
        } else if (jiareqizhuangtai.equals("2")) {
            tv_mode.setText("恒温模式");
        } else if (jiareqizhuangtai.equals("3")) {
            tv_mode.setText("关机");
        } else {
            tv_mode.setText("开机");
        }

        tv_dianya.setText(dianya + "V");
        tv_rufengkouwendu.setText(Y.getInt(wendu_rufengkou) + "℃");
        tv_chufengkouwendu.setText(Y.getInt(wendu_chufengkou) + "℃");
        tv_jiaresaigonglv.setText(jiaresaigonglv + "kw");
        tv_daqiya.setText(Y.getInt(daqiya) + "Kpa");
        tv_haibagaodu.setText(Y.getInt(haibagaodu) + "km");
        tv_hanyangliang.setText(Y.getInt(hanyangliang) + "g/m³");
        tv_xinhaoqiangdu.setText(xinhaoqiangdu);
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerStart.removeMessages(1);
        handlerTime10.removeMessages(1);
    }


    private Handler handlerTime10 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    getNData();
                    initHandlerNS();
                    break;
            }
            return false;
        }
    });

    private void initHandlerNS() {
        Message message = handlerTime10.obtainMessage(1);
        handlerTime10.sendMessageDelayed(message, 20000);
    }
}
