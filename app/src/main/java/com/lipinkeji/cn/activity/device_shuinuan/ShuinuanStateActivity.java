package com.lipinkeji.cn.activity.device_shuinuan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.util.Y;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ShuinuanStateActivity extends ShuinuanBaseNewActivity {


    @BindView(R.id.tv_jiareqizhuangtai)
    TextView tvJiareqizhuangtai;
    @BindView(R.id.tv_shuibengzhuangtai)
    TextView tvShuibengzhuangtai;
    @BindView(R.id.tv_youbengzhuangtai)
    TextView tvYoubengzhuangtai;
    @BindView(R.id.tv_youbengpinlv)
    TextView tvYoubengpinlv;
    @BindView(R.id.tv_fengjizhuansu)
    TextView tvFengjizhuansu;
    @BindView(R.id.tv_dianhuosaigonglv)
    TextView tvDianhuosaigonglv;
    @BindView(R.id.tv_rushuiwendu)
    TextView tvRushuiwendu;
    @BindView(R.id.tv_chushuiwendu)
    TextView tvChushuiwendu;
    @BindView(R.id.tv_yushewendu)
    TextView tvYushewendu;
    @BindView(R.id.tv_dangqiandianya)
    TextView tvDangqiandianya;
    @BindView(R.id.tv_gongzuoshichang)
    TextView tvGongzuoshichang;
    @BindView(R.id.tv_shebeizhuangtai)
    TextView tvShebeizhuangtai;
    @BindView(R.id.tv_huoyanwendu)
    TextView tvHuoyanwendu;
    @BindView(R.id.tv_daqiya)
    TextView tvDaqiya;
    @BindView(R.id.tv_haibagaodu)
    TextView tvHaibagaodu;
    @BindView(R.id.tv_hanyangliang)
    TextView tvHanyangliang;
    @BindView(R.id.tv_xinhaoqiangdu)
    TextView tvXinhaoqiangdu;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_jiareqi_zhuangtai;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShuinuanStateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initHuidiao();
        registerKtMqtt();
        initHandlerNS();
//        showTiDialog("数据加载中，请稍后......");
        showProgressDialog("加载中,请稍后......");
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

    @SuppressLint("SetTextI18n")
    private void getData(String msg) {
        if (msg.contains("j_s")) {
            dismissProgressDialog();
            String sn_state = msg.substring(3, 4);//水暖状态
            String syscTime = msg.substring(4, 7);//加热剩余时长
            String shuibeng_state = msg.substring(7, 8);//水泵状态  1.工作中2.待机中
            String youbeng_state = msg.substring(8, 9);//油泵状态  1.工作中2.待机中
            String fengji_state = msg.substring(9, 10);//风机状态  1.工作中2.待机中
            String dianyan = (Y.getInt(msg.substring(10, 14)) / 10.0f) + "";//电压  0253 = 25.3
            String fengjizhuansu = Y.getInt(msg.substring(14, 19)) + "";//风机转速   13245
            String jairesaigonglv = (Y.getInt(msg.substring(19, 23)) / 10.0f) + "";// 加热塞功率  0264=26.4
            String youbenggonglv = (Y.getInt(msg.substring(23, 27)) / 10.0f) + "";// 油泵频率  0264=26.4

            int rushukowenduInt = (Y.getInt(msg.substring(27, 30)) - 50);
            if (rushukowenduInt <= 0) {
                rushukowenduInt = 0;
            }
            String rushukowendu = rushukowenduInt + "";// 入水口温度（℃）  -50至150（000 = -50，100 = 50）

            int chushuikowenduInt = (Y.getInt(msg.substring(30, 33)) - 50);
            if (chushuikowenduInt <= 0) {
                chushuikowenduInt = 0;
            }
            String chushuikowendu = chushuikowenduInt + "";// 出水口温度（℃）  -50至150（000 = -50，100 = 50）

            int weiqiwenduInt = (Y.getInt(msg.substring(33, 37)) - 100);
            if (weiqiwenduInt <= 0) {
                weiqiwenduInt = 0;
            }
            String weiqiwendu = weiqiwenduInt + "";// 尾气温度（℃）  -50至2000（000 = -50，100 = 50）

            String danqiandangwei = msg.substring(37, 38);// 1.一档2.二档（注：用*占位）
            String yushewendu = msg.substring(38, 40);//预设温度（℃） 预设温度（℃）
            String zongTime = Y.getInt(msg.substring(40, 45)) + "";//总时长 （小时）
            String daqiya = Y.getInt(msg.substring(45, 48)) + "";//大气压
            String haibagaodu = Y.getInt(msg.substring(48, 52)) + "";//海拔高度
            String hanyangliang = Y.getInt(msg.substring(52, 55)) + "";//含氧量
            String xinhaoStr = msg.substring(55, 57);

            int xinhao;
            if (xinhaoStr.contains("a")) {
                xinhao = 22;
            } else {
                xinhao = Y.getInt(xinhaoStr);//信号强度
            }

            String num = "水暖状态" + sn_state + "  加热剩余时长" + syscTime + "  水泵状态" + shuibeng_state + "  油泵状态" + youbeng_state
                    + "  风机状态" + fengji_state
                    + "  电压" + dianyan
                    + "  风机转速" + fengjizhuansu
                    + "  加热塞功率" + jairesaigonglv
                    + "  油泵频率" + youbenggonglv
                    + "    入水口温度" + rushukowendu
                    + "    出水口温度" + chushuikowendu
                    + "    尾气温度" + weiqiwendu
                    + "    尾气温度" + weiqiwendu
                    + "  一档二挡" + danqiandangwei
                    + "  总时长" + zongTime + "   大气压" + daqiya + "    海拔高度" + haibagaodu + "  含氧量" + hanyangliang
                    + "  信号强度" + xinhao;
            Y.e(num);

            tvXinhaoqiangdu.setText(xinhao + "");

            switch (sn_state) {
                case "1"://开机中
                case "2"://加热中
                case "4"://循环水
                    tvJiareqizhuangtai.setText("开机");
                    tvShebeizhuangtai.setText("开机");
                    break;
                case "0"://待机中
                case "3"://关机中
                    tvJiareqizhuangtai.setText("关机");
                    tvShebeizhuangtai.setText("关机");
                    break;
            }

            switch (shuibeng_state) {
                case "1":
                    tvShuibengzhuangtai.setText("工作中");
                    break;
                case "2":
                    tvShuibengzhuangtai.setText("待机中");
                    break;
            }

            switch (youbeng_state) {
                case "1":
                    tvYoubengzhuangtai.setText("工作中");
                    break;
                case "2":
                    tvYoubengzhuangtai.setText("待机中");
                    break;
            }

//            switch (fengji_state) {
//                case "1":
//                    tvShebeizhuangtai.setText("工作中");
//                    break;
//                case "2":
//                    tvShebeizhuangtai.setText("待机中");
//                    break;
//            }

            tvDangqiandianya.setText(dianyan + "v");
            tvFengjizhuansu.setText(fengjizhuansu + "rpm");
            tvDianhuosaigonglv.setText(jairesaigonglv + "w");
            tvYoubengpinlv.setText(youbenggonglv + "Hz");
            tvRushuiwendu.setText(rushukowendu + "℃");
            tvChushuiwendu.setText(chushuikowendu + "℃");
            tvHuoyanwendu.setText(weiqiwendu + "℃");
//
//            switch (danqiandangwei) {
//                case "1":
//                    tvDangwei.setText("1档");
//                    break;
//                case "2":
//                    tvDangwei.setText("2档");
//                    break;
//                default:
//                    tvDangwei.setText("暂无档位");
//                    break;
//            }

            tvYushewendu.setText(yushewendu + "℃");
            tvGongzuoshichang.setText(zongTime + "h");
            tvDaqiya.setText(daqiya + "kpa");
            tvHaibagaodu.setText(haibagaodu + "m");
            tvHanyangliang.setText(hanyangliang + "kg/cm3");
            handlerStart.removeMessages(1);
        }
    }


    /**
     * 注册订阅Mqtt
     */
    private void registerKtMqtt() {
        initHandlerStart();
        getNs();
    }

    private void initHandlerStart() {
        Message message = handlerStart.obtainMessage(1);
        handlerStart.sendMessageDelayed(message, 250);
    }

    private void getNs() {
        //注册水暖加热器订阅
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(SN_Send)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Y.i("我订阅了" + SN_Send);
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
                Y.i("我订阅了" + SN_Accept);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        //向水暖加热器发送获取实时数据
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("N_s.")
                .setQos(2).setRetained(false)
                .setTopic(SN_Send), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("app端向水暖加热器请求实时数据", "");
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

                } else {
                    if (time == 5 || time == 10 || time == 15) {
                        getNs();
                    }
                    initHandlerStart();
                }
            }
            return false;
        }
    });

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
                    getNs();
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
