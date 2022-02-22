package com.lipinkeji.cn.activity.device_shuinuan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_a.dialog.GuzhangDialog;
import com.lipinkeji.cn.activity.device_fengnuan.FengnuanDingshiActivity;
import com.lipinkeji.cn.app.App;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.config.MyApplication;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;
import com.lipinkeji.cn.util.DoMqttValue;
import com.lipinkeji.cn.util.SoundPoolUtils;
import com.lipinkeji.cn.util.Y;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;
import com.rairmmd.andmqtt.MqttUnSubscribe;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class LipinDashuiActivityNew extends ShuinuanBaseNewActivity implements View.OnLongClickListener {


    @BindView(R.id.bt_back)
    RelativeLayout bt_back;
    @BindView(R.id.tv_shebei_title)
    TextView tv_shebei_title;
    @BindView(R.id.iv_jiareqi)
    ImageView iv_jiareqi;
    @BindView(R.id.tv_shebeima)
    TextView tv_shebeima;
    @BindView(R.id.tv_haibagaodu)
    TextView tv_haibagaodu;
    @BindView(R.id.tv_daqiya)
    TextView tv_daqiya;
    @BindView(R.id.tv_dianya)
    TextView tv_dianya;
    @BindView(R.id.tv_chushuikou_wendu)
    TextView tv_chushuikou_wendu;
    @BindView(R.id.tv_jinshuikou_wendu)
    TextView tv_jinshuikou_wendu;
    @BindView(R.id.tv_dangqianzhuangtai)
    TextView tv_dangqianzhuangtai;
    @BindView(R.id.ll_dingshi)
    LinearLayout ll_dingshi;
    @BindView(R.id.iv_xinhao)
    ImageView iv_xinhao;
    @BindView(R.id.tv_xinhao)
    TextView tv_xinhao;
    @BindView(R.id.tv_youxiaoqi)
    TextView tv_youxiaoqi;
    @BindView(R.id.bt_youbeng)
    LinearLayout bt_youbeng;
    @BindView(R.id.bt_shuibeng)
    LinearLayout bt_shuibeng;
    @BindView(R.id.bt_jitan)
    LinearLayout bt_jitan;
    @BindView(R.id.bt_kaiji)
    ImageView bt_kaiji;
    @BindView(R.id.bt_set)
    TextView bt_set;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private GuzhangDialog guzhangDialog;
    private String sim_ccid_save_type;

    private String sn_state;
    private String shuibeng_state;
    private String youbeng_state;
    private String jitan_state;
    private String yushewendu;

    private boolean isFirst;
    private boolean isKaiji;
    private boolean isShuibeng;
    private boolean isYoubeng;
    private boolean isJitan;
    private boolean isWaijizhuangzhi;
    private boolean isOnActivity;
    private int typeZaixian;//1 在线、2 离线、3 连接中
    private int typeMingling;//0 获取实时数据、1 开机、2 关机、3 油泵开、4 油泵关、5 水泵开、6水泵关 7外接开 8外接关  9清理积碳开  10积碳关
    private String waidianState;
    private String jiareqiType;

    @Override
    public int getContentViewResId() {
        return R.layout.a_shuinuan_act_main_lipin_new;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(false);
        mImmersionBar.init();
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context, String ccid, String car_server_id) {
        Intent intent = new Intent(context, LipinDashuiActivityNew.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("ccid", ccid);
        intent.putExtra("car_server_id", car_server_id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        init();
        initSM();
        initView();
        initHuidiao();
        registerKtMqtt();
        initHandlerNS();
    }

    private void init() {
        String validdate = PreferenceHelper.getInstance(mContext).getString("validdate", "0");
        tv_youxiaoqi.setText(validdate);

        String car_server_id = getIntent().getStringExtra("car_server_id");
        ccid = getIntent().getStringExtra("ccid");
        String ccidNew = ccid.replace("a", "");
        tv_shebeima.setText("设备编码：" + ccidNew);

        SN_Send = "wh/hardware/" + car_server_id + ccid;
        SN_Accept = "wh/app/" + car_server_id + ccid;

        jiareqiType = ccid.substring(22, 23);
        if (jiareqiType.equals("2")) {
            tv_shebei_title.setText("水暖加热器(小水)");
        } else if (jiareqiType.equals("4")) {
            tv_shebei_title.setText("水暖加热器(大水)");
        }


        MyApplication.mqttDingyue.add(SN_Send);
        MyApplication.mqttDingyue.add(SN_Accept);

        sim_ccid_save_type = PreferenceHelper.getInstance(mContext).getString("sim_ccid_save_type", "0");
        isFirst = true;
        typeZaixian = 3;

        jitan_state = "0";
        isJitan = false;
    }

    private void initSM() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                typeZaixian = 3;
                registerKtMqtt();
                smartRefreshLayout.finishRefresh();
            }
        });
    }

    private void initView() {
        bt_youbeng.setOnLongClickListener(this);
        bt_shuibeng.setOnLongClickListener(this);
        bt_kaiji.setOnLongClickListener(this);
        bt_jitan.setOnLongClickListener(this);

//        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/YOUSHEBIAOTIHEI-2.TTF");
//        bt_shuibeng.setTypeface(face);
//        bt_youbeng.setTypeface(face);
//        bt_kaiji.setTypeface(face);
//        bt_jitan.setTypeface(face);

        PreferenceHelper.getInstance(mContext).putString(App.CHOOSE_KONGZHI_XIANGMU, DoMqttValue.SHUINUAN);

        setUiGuanji();
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_SN_DATA) {
                    String msg = message.content.toString();
                    getData(msg);
                } else if (message.type == ConstanceValue.MSG_JIEBANG) {
                    finish();
                } else if (message.type == ConstanceValue.MSG_NETWORK_CHANGE) {
                    getNs();
                }
            }
        }));
    }

    private void getData(String msg) {
        Log.e("水暖加热器返回的数据是", msg);
        if (msg.contains("j_s")) {
            msgData = msg;
            typeZaixian = 1;
            handlerStart.removeMessages(1);

            tv_xinhao.setText("在线");
            sn_state = msg.substring(3, 4);//水暖状态
            String syscTime = msg.substring(4, 7);//加热剩余时长
            //水泵状态  1.工作中2.待机中
            shuibeng_state = msg.substring(7, 8);
            //油泵状态  1.工作中2.待机中
            youbeng_state = msg.substring(8, 9);
            String fengji_state = msg.substring(9, 10);//风机状态  1.工作中2.待机中
            String dianyan = (Y.getInt(msg.substring(10, 14)) / 10.0f) + "";//电压  0253 = 25.3
            String fengjizhuansu = msg.substring(14, 19);//风机转速   13245
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
            yushewendu = msg.substring(38, 40);//预设温度（℃） 预设温度（℃）
            String zongTime = msg.substring(40, 45);//总时长 （小时）
            String daqiya = msg.substring(45, 48);//大气压
            String haibagaodu = msg.substring(48, 52);//海拔高度
            String hanyangliang = msg.substring(52, 55);//含氧量

            firstCaozuo();

            String xinhaoStr = msg.substring(55, 57);

            if (msg.length() >= 61) {
                String banbenhao = msg.substring(57, 60);
                Y.e("我有版本号 " + banbenhao);
            }


            if (msg.length() >= 62) {
                waidianState = msg.substring(60, 61);
                Y.e("我有外电装置 " + waidianState);
                if (waidianState.equals("1")) {
//                    setWaijieUiKai();
                } else if (waidianState.equals("2")) {
//                    setWaijieUiGuan();
                } else {//没有此功能

                }
            }

            if (msg.length() >= 63) {
                jitan_state = msg.substring(61, 62);
                Y.e("我有积碳功能 " + jitan_state);
                if (jitan_state.equals("1")) {
                    setJitanUiKai();
//                    bt_jitan.getPaint().setFlags(0); // 取消设置的的划线
                } else if (jitan_state.equals("2")) {
                    setJitanUiGuan();
//                    bt_jitan.getPaint().setFlags(0); // 取消设置的的划线
                } else {//没有积碳功能
//                    bt_jitan.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//设置中划线
                }
            } else {
//                bt_jitan.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//设置中划线
            }


            if (xinhaoStr.equals("aa")) {
                iv_xinhao.setImageResource(R.mipmap.fengnuan_icon_signal2);
            } else {
                int xinhao = Y.getInt(xinhaoStr);//信号强度
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
                    + "  信号强度" + xinhaoStr;

            Y.e(num);
            tv_haibagaodu.setText(Y.getInt(haibagaodu) + "M");
            tv_daqiya.setText(Y.getInt(daqiya) + "KPA");
            tv_dianya.setText(dianyan + "V");
            tv_chushuikou_wendu.setText(chushuikowendu + "℃");
            tv_jinshuikou_wendu.setText(rushukowendu + "℃");

            typeZaixian = 1;

            switch (sn_state) {
                case "1"://开机中
                case "2"://加热中
                case "4"://循环水
                    isKaiji = true;
                    break;
                case "0"://关机中
                case "3"://待机中
                    isKaiji = false;
                    break;
            }

            if (isKaiji) {
                setUiKaiji();
            } else {
                setUiGuanji();
                if (shuibeng_state.equals("1")) {
                    setShuibengUiKai();
                } else {
                    setShuibengUiGuan();
                }

                if (youbeng_state.equals("1")) {
                    setYoubengUiKai();
                } else {
                    setYoubengUiGuan();
                }
            }
        } else if (msg.contains("k_s")) {
            String code = msg.substring(3, 5);
            if (code.equals("01")) {
                String zhuantai = msg.substring(5, 6);
                if (zhuantai.equals("1")) {
                    Log.i("操作成功", "");
                } else {
                    Log.i("操作失败", "");
                }
            } else {

            }
        } else if (msg.contains("M_s")) {
            String code = msg.substring(3, 5);
            if (code.equals("01")) {

            } else if (code.equals("02")) {

            }
        } else if (msg.contains("g_s.")) {
            typeZaixian = 1;
        } else if (msg.contains("r_s")) {
            String dianya = msg.substring(3, 4);//电压	0.正常1.过高2.过低3.故障
            String youbeng = msg.substring(4, 5);//油泵	0.正常1.开路2.短路3.故障
            String shuibeng = msg.substring(5, 6);//水泵	0.正常1.开路2.短路3.过流4.堵转5.故障
            String chushuiko = msg.substring(6, 7);//出水口	0.正常1.开路2.短路3.高温4.故障
            String rushuiko = msg.substring(7, 8);//入水口	0.正常1.开路2.短路3.高温4.故障
            String wensheng = msg.substring(8, 9);//壳体温度异常	0.正常1.异常
            String fengji = msg.substring(9, 10);//风机	0.正常1.开路2.短路3.过流4.堵转5.故障
            String chufengko = msg.substring(10, 11);//出风口	0.正常1.开路2.短路3.高温4.故障
            String dianhuosai = msg.substring(11, 12);//点火塞	0.正常1.开路2.短路3.故障
            String houyan = msg.substring(12, 13);//火焰	0.正常1.熄火
            String dianhuo = msg.substring(13, 14);//点火	0.正常1.失败

            List<String> guzhangs = new ArrayList<>();
            int yuyinId = -1;

            if (dianya.equals("1")) {
                guzhangs.add("电压过高");
                yuyinId = R.raw.sn_fault_a0;
            } else if (dianya.equals("2")) {
                guzhangs.add("电压过低");
                yuyinId = R.raw.sn_fault_a0;
            } else if (dianya.equals("3")) {
                guzhangs.add("电压故障");
                yuyinId = R.raw.sn_fault_a0;
            }

            if (youbeng.equals("1")) {
                guzhangs.add("油泵开路");
                yuyinId = R.raw.sn_fault_b0;
            } else if (youbeng.equals("2")) {
                guzhangs.add("油泵短路");
                yuyinId = R.raw.sn_fault_b0;
            } else if (youbeng.equals("3")) {
                guzhangs.add("油泵故障");
                yuyinId = R.raw.sn_fault_b0;
            }

            if (shuibeng.equals("1")) {
                guzhangs.add("水泵开路");
                yuyinId = R.raw.sn_fault_c0;
            } else if (shuibeng.equals("2")) {
                guzhangs.add("水泵短路");
                yuyinId = R.raw.sn_fault_c0;
            } else if (shuibeng.equals("3")) {
                guzhangs.add("水泵过流");
                yuyinId = R.raw.sn_fault_c0;
            } else if (shuibeng.equals("4")) {
                guzhangs.add("水泵堵转");
                yuyinId = R.raw.sn_fault_c0;
            } else if (shuibeng.equals("5")) {
                guzhangs.add("水泵故障");
                yuyinId = R.raw.sn_fault_c0;
            }

            if (chushuiko.equals("1")) {
                guzhangs.add("出水口开路");
                yuyinId = R.raw.sn_fault_d0;
            } else if (chushuiko.equals("2")) {
                guzhangs.add("出水口短路");
                yuyinId = R.raw.sn_fault_d0;
            } else if (chushuiko.equals("3")) {
                guzhangs.add("出水口高温");
                yuyinId = R.raw.sn_fault_d0;
            } else if (chushuiko.equals("4")) {
                guzhangs.add("出水口故障");
                yuyinId = R.raw.sn_fault_d0;
            }

            if (rushuiko.equals("1")) {
                guzhangs.add("入水口开路");
                yuyinId = R.raw.sn_fault_e0;
            } else if (rushuiko.equals("2")) {
                guzhangs.add("入水口短路");
                yuyinId = R.raw.sn_fault_e0;
            } else if (rushuiko.equals("3")) {
                guzhangs.add("入水口高温");
                yuyinId = R.raw.sn_fault_e0;
            } else if (rushuiko.equals("4")) {
                guzhangs.add("入水口故障");
                yuyinId = R.raw.sn_fault_e0;
            }

            if (wensheng.equals("1")) {
                guzhangs.add("壳体温度异常");
                yuyinId = R.raw.sn_fault_f0;
            }

            if (fengji.equals("1")) {
                guzhangs.add("风机开路");
                yuyinId = R.raw.sn_fault_g0;
            } else if (fengji.equals("2")) {
                guzhangs.add("风机短路");
                yuyinId = R.raw.sn_fault_g0;
            } else if (fengji.equals("3")) {
                guzhangs.add("风机过流");
                yuyinId = R.raw.sn_fault_g0;
            } else if (fengji.equals("4")) {
                guzhangs.add("风机堵转");
                yuyinId = R.raw.sn_fault_g0;
            } else if (fengji.equals("5")) {
                guzhangs.add("风机故障");
                yuyinId = R.raw.sn_fault_g0;
            }

            if (chufengko.equals("1")) {
                guzhangs.add("出风口开路");
                yuyinId = R.raw.sn_fault_h0;
            } else if (chufengko.equals("2")) {
                guzhangs.add("出风口短路");
                yuyinId = R.raw.sn_fault_h0;
            } else if (chufengko.equals("3")) {
                guzhangs.add("出风口高温");
                yuyinId = R.raw.sn_fault_h0;
            } else if (chufengko.equals("4")) {
                guzhangs.add("出风口故障");
                yuyinId = R.raw.sn_fault_h0;
            }

            if (dianhuosai.equals("1")) {
                guzhangs.add("点火塞开路");
                yuyinId = R.raw.sn_fault_i0;
            } else if (dianhuosai.equals("2")) {
                guzhangs.add("点火塞短路");
                yuyinId = R.raw.sn_fault_i0;
            } else if (dianhuosai.equals("3")) {
                guzhangs.add("点火塞故障");
                yuyinId = R.raw.sn_fault_i0;
            }

            if (houyan.equals("1")) {
                guzhangs.add("火焰熄火");
                yuyinId = R.raw.sn_fault_j0;
            }

            if (dianhuo.equals("1")) {
                guzhangs.add("点火失败");
                yuyinId = R.raw.sn_fault_k0;
            }

            if (msg.length() > 16) {
                String xihuo = msg.substring(14, 15);//熄火	0.正常1.失败
                String shilian = msg.substring(15, 16);//失联	0.正常1.故障
                String shuiwen = msg.substring(17, 18);//水温	0.正常1.过高

                if (xihuo.equals("1")) {
                    guzhangs.add("熄火失败");
                    yuyinId = R.raw.sn_fault_l0;
                }

                if (shilian.equals("1")) {
                    guzhangs.add("失联故障");
                    yuyinId = R.raw.sn_fault_m0;
                }

                if (shuiwen.equals("1")) {
                    guzhangs.add("水温过高");
                    yuyinId = R.raw.sn_fault_n0;
                }
            }


            if (guzhangs.size() > 0) {
                showguzhangla(guzhangs, yuyinId);
            } else {
                if (guzhangDialog != null) {
                    guzhangDialog.dismiss();
                }
            }
        }
    }


    private void showguzhangla(List<String> strings, int yuyinId) {
        if (guzhangDialog == null) {
            guzhangDialog = new GuzhangDialog(mContext, new GuzhangDialog.Guzhang() {
                @Override
                public void onClickConfirm(View v, GuzhangDialog dialog) {
                    dealGuzhang();
                }

                @Override
                public void onDismiss(GuzhangDialog dialog) {
                    SoundPoolUtils.setSoundPoolRelease();
                }

                @Override
                public void onHulue(View view, GuzhangDialog dialog) {
                    finish();
                }
            });
        }
        guzhangDialog.showDD(strings, yuyinId);
    }

    private void dealGuzhang() {
        String data = "M_s071.";
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg(data)
                .setQos(2).setRetained(false)
                .setTopic(SN_Send), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        Message message = handlerGuanzhang.obtainMessage(1);
        handlerGuanzhang.sendMessageDelayed(message, 700);
    }

    Handler handlerGuanzhang = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("Z_s.")
                            .setQos(2).setRetained(false)
                            .setTopic(SN_Send), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Y.i("查询一次故障");
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                        }
                    });
                    break;
            }
            return false;
        }
    });

    private void firstCaozuo() {
        if (isFirst) {
            //向水暖加热器发送获取实时数据
            if (!sim_ccid_save_type.equals("1")) {
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("X_s.")
                        .setQos(2).setRetained(false)
                        .setTopic(SN_Send), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Y.i("查询一次卡号");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });
            }

            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("Y_s.")
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Y.i("查询一次经纬度");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });

            isFirst = false;
        }
    }


    /**
     * 注册订阅Mqtt
     */
    private void registerKtMqtt() {
        time = 0;
        typeMingling = 0;
        initHandlerMingling();
        getNs();
        getNsData();
    }

    private void getNs() {
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
    }

    private void getNsData() {
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
        typeZaixian = 2;
        time = 0;
        TishiDialog tishiDialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {
                finish();
            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {
                registerKtMqtt();
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

    @OnClick({R.id.bt_back, R.id.ll_dingshi, R.id.bt_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.ll_dingshi:
                ShuinuanDingshiActivity.actionStart(mContext);
                break;
            case R.id.bt_set:
                ShuinuanSetActivity.actionStart(mContext);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (typeZaixian == 1) {
            if (typeMingling == 1 || typeMingling == 2) {
                handlerStart.removeMessages(2);
            } else if (typeMingling == 3 || typeMingling == 4) {
                handlerStart.removeMessages(4);
            } else if (typeMingling == 5 || typeMingling == 6) {
                handlerStart.removeMessages(3);
            } else {
                handlerStart.removeMessages(1);
            }

            time = 0;

            switch (v.getId()) {
                case R.id.bt_kaiji:
                    kaiguan();
                    break;
                case R.id.bt_youbeng:
                    youbeng();
                    break;
                case R.id.bt_shuibeng:
                    shuibeng();
                    break;
                case R.id.bt_jitan:
                    jitan();
                    break;
            }
        } else if (typeZaixian == 3) {
            Y.tLong("正在连接设备，请稍后...");
        } else if (typeZaixian == 2) {
            chonglian();
        }
        return false;
    }


    private void jitan() {
        if (isKaiji) {
            Y.tLong("开机状态无法清理积碳");
            return;
        }

        if (isJitan) {
//            SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_youbeng_off);
            typeMingling = 10;
            sendMingling();
            setJitanUiGuan();
        } else {
//            SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_youbeng_on);
            typeMingling = 9;
            sendMingling();
            setJitanUiKai();
        }
    }

    private void youbeng() {
        if (isKaiji) {
            Y.tLong("开机状态无法操控油泵");
            return;
        }

        if (isShuibeng) {
            Y.tLong("当前水泵处于开启状态，请等待水泵关闭再进行操作");
            return;
        }

        if (isYoubeng) {
            SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_youbeng_off);
            typeMingling = 4;
            sendMingling();
            setYoubengUiGuan();
        } else {
            SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_youbeng_on);
            typeMingling = 3;
            sendMingling();
            setYoubengUiKai();
        }
    }

    private void shuibeng() {
        if (isKaiji) {
            Y.tLong("开机状态无法操控水泵");
            return;
        }

        if (isYoubeng) {
            Y.tLong("当前油泵处于开启状态，请等待油泵关闭再进行操作");
            return;
        }

        if (isShuibeng) {
            SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_shuibeng_off);
            typeMingling = 6;
            sendMingling();
            setShuibengUiGuan();
        } else {
            SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_shuibeng_on);
            typeMingling = 5;
            sendMingling();
            setShuibengUiKai();
        }
    }

    private void kaiguan() {
        if (isKaiji) {
            guanji();
        } else {
            kaiji();
        }
    }


    private void kaiji() {
        if (isKaiji) {
            return;
        }

        if (isYoubeng) {
            Y.tLong("当前油泵处于开启状态，请等待油泵关闭再进行操作");
            return;
        }

        if (isShuibeng) {
            Y.tLong("当前水泵处于开启状态，请等待水泵关闭再进行操作");
            return;
        }

        SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_start_on);
        typeMingling = 1;
        sendMingling();
        setUiKaiji();
    }

    private void guanji() {
        if (!isKaiji) {
            return;
        }

        SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_start_off);
        typeMingling = 2;
        sendMingling();
        setUiGuanji();
    }

    private void setJitanUiGuan() {
        isJitan = false;
        bt_jitan.setBackgroundResource(R.mipmap.sn_anjian_nor);
    }

    private void setJitanUiKai() {
        isJitan = true;
        bt_jitan.setBackgroundResource(R.mipmap.sn_anjian_sel);
    }


    private void setYoubengUiGuan() {
        isYoubeng = false;
        bt_youbeng.setBackgroundResource(R.mipmap.sn_anjian_nor);
    }

    private void setYoubengUiKai() {
        isYoubeng = true;
        bt_youbeng.setBackgroundResource(R.mipmap.sn_anjian_sel);
    }

    private void setShuibengUiGuan() {
        isShuibeng = false;
        bt_shuibeng.setBackgroundResource(R.mipmap.sn_anjian_nor);
    }

    private void setShuibengUiKai() {
        isShuibeng = true;
        bt_shuibeng.setBackgroundResource(R.mipmap.sn_anjian_sel);
    }

    private void setUiGuanji() {
        isKaiji = false;

        if (jiareqiType.equals("2")) {
            iv_jiareqi.setBackgroundResource(R.mipmap.wzw_xiaoshui_shebei_nor);
        } else if (jiareqiType.equals("4")) {
            iv_jiareqi.setBackgroundResource(R.drawable.shuinuan_guanji);
        }

        bt_kaiji.setBackgroundResource(R.mipmap.sn_kaiguan_nor);

        bt_shuibeng.setBackgroundResource(R.mipmap.sn_anjian_nor);
        bt_youbeng.setBackgroundResource(R.mipmap.sn_anjian_nor);
//        bt_jitan.setBackgroundResource(R.mipmap.sn_anjian_nor);
    }

    private void setUiKaiji() {
        isKaiji = true;
        isShuibeng = false;
        isYoubeng = false;
        isJitan = false;

        if (jiareqiType.equals("2")) {
            iv_jiareqi.setBackgroundResource(R.drawable.shuinuan_kaiji_xiao);
        } else if (jiareqiType.equals("4")) {
            iv_jiareqi.setBackgroundResource(R.drawable.shuinuan_kaiji);
        }

        AnimationDrawable animationDrawable = (AnimationDrawable) iv_jiareqi.getBackground();
        animationDrawable.start();

        bt_kaiji.setBackgroundResource(R.mipmap.sn_kaiguan_sel);

        if (shuibeng_state.equals("1")) {
            bt_shuibeng.setBackgroundResource(R.mipmap.sn_anjian_sel);
        } else {
            bt_shuibeng.setBackgroundResource(R.mipmap.sn_anjian_nor);
        }

        if (youbeng_state.equals("1")) {
            bt_youbeng.setBackgroundResource(R.mipmap.sn_anjian_sel);
        } else {
            bt_youbeng.setBackgroundResource(R.mipmap.sn_anjian_nor);
        }

//        if (isJitan) {
//            bt_jitan.setBackgroundResource(R.mipmap.sn_anjian_sel);
//        } else {
//            bt_jitan.setBackgroundResource(R.mipmap.sn_anjian_nor);
//        }
    }

    private void sendMingling() {
        if (typeMingling == 0) {//发送实时数据
            getNsData();
        } else if (typeMingling == 1) {//开机
            String data = "M_s011000080.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 2) {//关机
            String data = "M_s012.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 3) {//开启油泵
            String data = "M_s051.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 4) {//关闭油泵
            String data = "M_s052.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 5) {//水泵开启
            String data = "M_s021.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 6) {//水泵关闭
            String data = "M_s022.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 7) {//外电开启
            String data = "M_s141.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 8) {//外电关闭
            String data = "M_s142.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 9) {//清理积碳开启
            String data = "M_s142.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 10) {//清理积碳关闭
            String data = "M_s142.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
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
    }

    private Handler handlerTime10 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    if (isOnActivity) {
                        getNsData();
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


    private void chonglian() {
        if (handlerStart != null) {
            handlerStart.removeMessages(1);
        }

        showTishiDialog();
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

        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(SN_Send), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(SN_Accept), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        for (int i = 0; i < MyApplication.mqttDingyue.size(); i++) {
            if (MyApplication.mqttDingyue.get(i).equals(SN_Send)) {
                MyApplication.mqttDingyue.remove(i);
            }
        }

        for (int i = 0; i < MyApplication.mqttDingyue.size(); i++) {
            if (MyApplication.mqttDingyue.get(i).equals(SN_Accept)) {
                MyApplication.mqttDingyue.remove(i);
            }
        }
    }
}
