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
     * ????????????Activty????????????Activity
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
//        showTiDialog("???????????????????????????......");
        showProgressDialog("?????????,?????????......");
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
            String sn_state = msg.substring(3, 4);//????????????
            String syscTime = msg.substring(4, 7);//??????????????????
            String shuibeng_state = msg.substring(7, 8);//????????????  1.?????????2.?????????
            String youbeng_state = msg.substring(8, 9);//????????????  1.?????????2.?????????
            String fengji_state = msg.substring(9, 10);//????????????  1.?????????2.?????????
            String dianyan = (Y.getInt(msg.substring(10, 14)) / 10.0f) + "";//??????  0253 = 25.3
            String fengjizhuansu = Y.getInt(msg.substring(14, 19)) + "";//????????????   13245
            String jairesaigonglv = (Y.getInt(msg.substring(19, 23)) / 10.0f) + "";// ???????????????  0264=26.4
            String youbenggonglv = (Y.getInt(msg.substring(23, 27)) / 10.0f) + "";// ????????????  0264=26.4

            int rushukowenduInt = (Y.getInt(msg.substring(27, 30)) - 50);
            if (rushukowenduInt <= 0) {
                rushukowenduInt = 0;
            }
            String rushukowendu = rushukowenduInt + "";// ????????????????????????  -50???150???000 = -50???100 = 50???

            int chushuikowenduInt = (Y.getInt(msg.substring(30, 33)) - 50);
            if (chushuikowenduInt <= 0) {
                chushuikowenduInt = 0;
            }
            String chushuikowendu = chushuikowenduInt + "";// ????????????????????????  -50???150???000 = -50???100 = 50???

            int weiqiwenduInt = (Y.getInt(msg.substring(33, 37)) - 100);
            if (weiqiwenduInt <= 0) {
                weiqiwenduInt = 0;
            }
            String weiqiwendu = weiqiwenduInt + "";// ?????????????????????  -50???2000???000 = -50???100 = 50???

            String danqiandangwei = msg.substring(37, 38);// 1.??????2.??????????????????*?????????
            String yushewendu = msg.substring(38, 40);//????????????????????? ?????????????????????
            String zongTime = Y.getInt(msg.substring(40, 45)) + "";//????????? ????????????
            String daqiya = Y.getInt(msg.substring(45, 48)) + "";//?????????
            String haibagaodu = Y.getInt(msg.substring(48, 52)) + "";//????????????
            String hanyangliang = Y.getInt(msg.substring(52, 55)) + "";//?????????
            String xinhaoStr = msg.substring(55, 57);

            int xinhao;
            if (xinhaoStr.contains("a")) {
                xinhao = 22;
            } else {
                xinhao = Y.getInt(xinhaoStr);//????????????
            }

            String num = "????????????" + sn_state + "  ??????????????????" + syscTime + "  ????????????" + shuibeng_state + "  ????????????" + youbeng_state
                    + "  ????????????" + fengji_state
                    + "  ??????" + dianyan
                    + "  ????????????" + fengjizhuansu
                    + "  ???????????????" + jairesaigonglv
                    + "  ????????????" + youbenggonglv
                    + "    ???????????????" + rushukowendu
                    + "    ???????????????" + chushuikowendu
                    + "    ????????????" + weiqiwendu
                    + "    ????????????" + weiqiwendu
                    + "  ????????????" + danqiandangwei
                    + "  ?????????" + zongTime + "   ?????????" + daqiya + "    ????????????" + haibagaodu + "  ?????????" + hanyangliang
                    + "  ????????????" + xinhao;
            Y.e(num);

            tvXinhaoqiangdu.setText(xinhao + "");

            switch (sn_state) {
                case "1"://?????????
                case "2"://?????????
                case "4"://?????????
                    tvJiareqizhuangtai.setText("??????");
                    tvShebeizhuangtai.setText("??????");
                    break;
                case "0"://?????????
                case "3"://?????????
                    tvJiareqizhuangtai.setText("??????");
                    tvShebeizhuangtai.setText("??????");
                    break;
            }

            switch (shuibeng_state) {
                case "1":
                    tvShuibengzhuangtai.setText("?????????");
                    break;
                case "2":
                    tvShuibengzhuangtai.setText("?????????");
                    break;
            }

            switch (youbeng_state) {
                case "1":
                    tvYoubengzhuangtai.setText("?????????");
                    break;
                case "2":
                    tvYoubengzhuangtai.setText("?????????");
                    break;
            }

//            switch (fengji_state) {
//                case "1":
//                    tvShebeizhuangtai.setText("?????????");
//                    break;
//                case "2":
//                    tvShebeizhuangtai.setText("?????????");
//                    break;
//            }

            tvDangqiandianya.setText(dianyan + "v");
            tvFengjizhuansu.setText(fengjizhuansu + "rpm");
            tvDianhuosaigonglv.setText(jairesaigonglv + "w");
            tvYoubengpinlv.setText(youbenggonglv + "Hz");
            tvRushuiwendu.setText(rushukowendu + "???");
            tvChushuiwendu.setText(chushuikowendu + "???");
            tvHuoyanwendu.setText(weiqiwendu + "???");
//
//            switch (danqiandangwei) {
//                case "1":
//                    tvDangwei.setText("1???");
//                    break;
//                case "2":
//                    tvDangwei.setText("2???");
//                    break;
//                default:
//                    tvDangwei.setText("????????????");
//                    break;
//            }

            tvYushewendu.setText(yushewendu + "???");
            tvGongzuoshichang.setText(zongTime + "h");
            tvDaqiya.setText(daqiya + "kpa");
            tvHaibagaodu.setText(haibagaodu + "m");
            tvHanyangliang.setText(hanyangliang + "kg/cm3");
            handlerStart.removeMessages(1);
        }
    }


    /**
     * ????????????Mqtt
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
        //???????????????????????????
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(SN_Send)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Y.i("????????????" + SN_Send);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        //???????????????????????????app
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(SN_Accept)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Y.i("????????????" + SN_Accept);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        //??????????????????????????????????????????
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("N_s.")
                .setQos(2).setRetained(false)
                .setTopic(SN_Send), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("app???????????????????????????????????????", "");
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
