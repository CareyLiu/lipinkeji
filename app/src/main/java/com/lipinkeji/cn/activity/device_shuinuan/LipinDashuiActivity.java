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

public class LipinDashuiActivity extends ShuinuanBaseNewActivity implements View.OnLongClickListener {


    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.rl_set)
    RelativeLayout rl_set;
    @BindView(R.id.iv_heater_host)
    ImageView iv_heater_host;
    @BindView(R.id.tv_youxiaoqi)
    TextView tv_youxiaoqi;
    @BindView(R.id.ll_shuoming)
    LinearLayout ll_shuoming;
    @BindView(R.id.ll_dingshi)
    LinearLayout ll_dingshi;
    @BindView(R.id.iv_xinhao)
    ImageView iv_xinhao;
    @BindView(R.id.tv_xinhao)
    TextView tv_xinhao;
    @BindView(R.id.tv_haibagaodu)
    TextView tv_haibagaodu;
    @BindView(R.id.tv_daqiya)
    TextView tv_daqiya;
    @BindView(R.id.tv_dianya)
    TextView tv_dianya;
    @BindView(R.id.tv_jinshuikou_wendu)
    TextView tv_jinshuikou_wendu;
    @BindView(R.id.tv_chushuikou_wendu)
    TextView tv_chushuikou_wendu;
    @BindView(R.id.bt_shuibeng)
    TextView bt_shuibeng;
    @BindView(R.id.bt_youbeng)
    TextView bt_youbeng;
    @BindView(R.id.bt_kaiji)
    TextView bt_kaiji;
    @BindView(R.id.bt_jitan)
    TextView bt_jitan;
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
    private int typeZaixian;//1 ?????????2 ?????????3 ?????????
    private int typeMingling;//0 ?????????????????????1 ?????????2 ?????????3 ????????????4 ????????????5 ????????????6????????? 7????????? 8?????????  9???????????????  10?????????
    private String waidianState;

    @Override
    public int getContentViewResId() {
        return R.layout.a_shuinuan_act_main_lipin_da;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(false);
        mImmersionBar.init();
    }

    /**
     * ????????????Activty????????????Activity
     */
    public static void actionStart(Context context, String ccid, String car_server_id) {
        Intent intent = new Intent(context, LipinDashuiActivity.class);
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
        setUiGuanji();
    }

    private void init() {
        String validdate = PreferenceHelper.getInstance(mContext).getString("validdate", "0");
        tv_youxiaoqi.setText("????????????:" + validdate);

        String car_server_id = getIntent().getStringExtra("car_server_id");
        ccid = getIntent().getStringExtra("ccid");

        SN_Send = "wh/hardware/" + car_server_id + ccid;
        SN_Accept = "wh/app/" + car_server_id + ccid;

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


        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/YOUSHEBIAOTIHEI-2.TTF");
        bt_shuibeng.setTypeface(face);
        bt_youbeng.setTypeface(face);
        bt_kaiji.setTypeface(face);
        bt_jitan.setTypeface(face);

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
        Log.e("?????????????????????????????????", msg);
        if (msg.contains("j_s")) {
            msgData = msg;
            typeZaixian = 1;
            handlerStart.removeMessages(1);

            tv_xinhao.setText("??????");
            sn_state = msg.substring(3, 4);//????????????
            String syscTime = msg.substring(4, 7);//??????????????????
            //????????????  1.?????????2.?????????
            shuibeng_state = msg.substring(7, 8);
            //????????????  1.?????????2.?????????
            youbeng_state = msg.substring(8, 9);
            String fengji_state = msg.substring(9, 10);//????????????  1.?????????2.?????????
            String dianyan = (Y.getInt(msg.substring(10, 14)) / 10.0f) + "";//??????  0253 = 25.3
            String fengjizhuansu = msg.substring(14, 19);//????????????   13245
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
            yushewendu = msg.substring(38, 40);//????????????????????? ?????????????????????
            String zongTime = msg.substring(40, 45);//????????? ????????????
            String daqiya = msg.substring(45, 48);//?????????
            String haibagaodu = msg.substring(48, 52);//????????????
            String hanyangliang = msg.substring(52, 55);//?????????

            firstCaozuo();

            String xinhaoStr = msg.substring(55, 57);

            if (msg.length() >= 61) {
                String banbenhao = msg.substring(57, 60);
                Y.e("??????????????? " + banbenhao);
            }


            if (msg.length() >= 62) {
                waidianState = msg.substring(60, 61);
                Y.e("?????????????????? " + waidianState);
                if (waidianState.equals("1")) {
//                    setWaijieUiKai();
                } else if (waidianState.equals("2")) {
//                    setWaijieUiGuan();
                } else {//???????????????

                }
            }

            if (msg.length() >= 63) {
                jitan_state = msg.substring(61, 62);
                Y.e("?????????????????? " + jitan_state);
                if (jitan_state.equals("1")) {
                    setJitanUiKai();
                    bt_jitan.setEnabled(true);
                    bt_jitan.getPaint().setFlags(0); // ????????????????????????
                } else if (jitan_state.equals("2")) {
                    setJitanUiGuan();
                    bt_jitan.setEnabled(true);
                    bt_jitan.getPaint().setFlags(0); // ????????????????????????
                } else {//??????????????????
                    bt_jitan.setEnabled(false);
                    bt_jitan.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//???????????????
                }
            } else {
                bt_jitan.setEnabled(false);
                bt_jitan.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//???????????????
            }


            if (xinhaoStr.equals("aa")) {
                iv_xinhao.setImageResource(R.mipmap.fengnuan_icon_signal2);
            } else {
                int xinhao = Y.getInt(xinhaoStr);//????????????
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
                    + "  ????????????" + xinhaoStr;

            Y.e(num);
            tv_haibagaodu.setText(Y.getInt(haibagaodu) + "M");
            tv_daqiya.setText(Y.getInt(daqiya) + "KPA");
            tv_dianya.setText(dianyan + "V");
            tv_chushuikou_wendu.setText(chushuikowendu + "???");
            tv_jinshuikou_wendu.setText(rushukowendu + "???");

            typeZaixian = 1;

            switch (sn_state) {
                case "1"://?????????
                case "2"://?????????
                case "4"://?????????
                    isKaiji = true;
                    break;
                case "0"://?????????
                case "3"://?????????
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
                    Log.i("????????????", "");
                } else {
                    Log.i("????????????", "");
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
            String dianya = msg.substring(3, 4);//??????	0.??????1.??????2.??????3.??????
            String youbeng = msg.substring(4, 5);//??????	0.??????1.??????2.??????3.??????
            String shuibeng = msg.substring(5, 6);//??????	0.??????1.??????2.??????3.??????4.??????5.??????
            String chushuiko = msg.substring(6, 7);//?????????	0.??????1.??????2.??????3.??????4.??????
            String rushuiko = msg.substring(7, 8);//?????????	0.??????1.??????2.??????3.??????4.??????
            String wensheng = msg.substring(8, 9);//??????????????????	0.??????1.??????
            String fengji = msg.substring(9, 10);//??????	0.??????1.??????2.??????3.??????4.??????5.??????
            String chufengko = msg.substring(10, 11);//?????????	0.??????1.??????2.??????3.??????4.??????
            String dianhuosai = msg.substring(11, 12);//?????????	0.??????1.??????2.??????3.??????
            String houyan = msg.substring(12, 13);//??????	0.??????1.??????
            String dianhuo = msg.substring(13, 14);//??????	0.??????1.??????

            List<String> guzhangs = new ArrayList<>();
            int yuyinId = -1;

            if (dianya.equals("1")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_a0;
            } else if (dianya.equals("2")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_a0;
            } else if (dianya.equals("3")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_a0;
            }

            if (youbeng.equals("1")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_b0;
            } else if (youbeng.equals("2")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_b0;
            } else if (youbeng.equals("3")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_b0;
            }

            if (shuibeng.equals("1")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_c0;
            } else if (shuibeng.equals("2")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_c0;
            } else if (shuibeng.equals("3")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_c0;
            } else if (shuibeng.equals("4")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_c0;
            } else if (shuibeng.equals("5")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_c0;
            }

            if (chushuiko.equals("1")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_d0;
            } else if (chushuiko.equals("2")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_d0;
            } else if (chushuiko.equals("3")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_d0;
            } else if (chushuiko.equals("4")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_d0;
            }

            if (rushuiko.equals("1")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_e0;
            } else if (rushuiko.equals("2")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_e0;
            } else if (rushuiko.equals("3")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_e0;
            } else if (rushuiko.equals("4")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_e0;
            }

            if (wensheng.equals("1")) {
                guzhangs.add("??????????????????");
                yuyinId = R.raw.sn_fault_f0;
            }

            if (fengji.equals("1")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_g0;
            } else if (fengji.equals("2")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_g0;
            } else if (fengji.equals("3")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_g0;
            } else if (fengji.equals("4")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_g0;
            } else if (fengji.equals("5")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_g0;
            }

            if (chufengko.equals("1")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_h0;
            } else if (chufengko.equals("2")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_h0;
            } else if (chufengko.equals("3")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_h0;
            } else if (chufengko.equals("4")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_h0;
            }

            if (dianhuosai.equals("1")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_i0;
            } else if (dianhuosai.equals("2")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_i0;
            } else if (dianhuosai.equals("3")) {
                guzhangs.add("???????????????");
                yuyinId = R.raw.sn_fault_i0;
            }

            if (houyan.equals("1")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_j0;
            }

            if (dianhuo.equals("1")) {
                guzhangs.add("????????????");
                yuyinId = R.raw.sn_fault_k0;
            }

            if (msg.length() > 16) {
                String xihuo = msg.substring(14, 15);//??????	0.??????1.??????
                String shilian = msg.substring(15, 16);//??????	0.??????1.??????
                String shuiwen = msg.substring(17, 18);//??????	0.??????1.??????

                if (xihuo.equals("1")) {
                    guzhangs.add("????????????");
                    yuyinId = R.raw.sn_fault_l0;
                }

                if (shilian.equals("1")) {
                    guzhangs.add("????????????");
                    yuyinId = R.raw.sn_fault_m0;
                }

                if (shuiwen.equals("1")) {
                    guzhangs.add("????????????");
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
                            Y.i("??????????????????");
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
            //??????????????????????????????????????????
            if (!sim_ccid_save_type.equals("1")) {
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("X_s.")
                        .setQos(2).setRetained(false)
                        .setTopic(SN_Send), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Y.i("??????????????????");
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
                    Y.i("?????????????????????");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });

            isFirst = false;
        }
    }

    /**
     * ????????????Mqtt
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

        //???????????????????????????app
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
            Y.e(msg.what + "  ?????????????????????  " + time);
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
        tishiDialog.setTextTitle("???????????????????????????");
        tishiDialog.setTextContent("????????????????????????1:???????????????????????? 2:??????????????????????????? 3:????????????????????? 4:????????????????????????????????????????????????????????????????????????");
        tishiDialog.setTextConfirm("??????");
        tishiDialog.setTextCancel("??????");
        tishiDialog.show();
    }

    @OnClick({R.id.rl_back, R.id.rl_set, R.id.ll_shuoming, R.id.ll_dingshi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_set:
                ShuinuanSetActivity.actionStart(mContext);
                break;
            case R.id.ll_shuoming:
                ShuinuanShuomingActivity.actionStart(mContext);
                break;
            case R.id.ll_dingshi:
                ShuinuanDingshiActivity.actionStart(mContext);
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
            Y.tLong("??????????????????????????????...");
        } else if (typeZaixian == 2) {
            chonglian();
        }
        return false;
    }

    private void showShebeima() {
        TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
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
        dialog.setTextTitle("?????????");
        dialog.setTextContent(ccid);
        dialog.setTextCancel("");
        dialog.show();
    }

    private void jitan() {
        if (isKaiji) {
            Y.tLong("??????????????????????????????");
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
            Y.tLong("??????????????????????????????");
            return;
        }

        if (isShuibeng) {
            Y.tLong("?????????????????????????????????????????????????????????????????????");
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
            Y.tLong("??????????????????????????????");
            return;
        }

        if (isYoubeng) {
            Y.tLong("?????????????????????????????????????????????????????????????????????");
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
            Y.tLong("?????????????????????????????????????????????????????????????????????");
            return;
        }

        if (isShuibeng) {
            Y.tLong("?????????????????????????????????????????????????????????????????????");
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
        bt_jitan.setBackgroundResource(R.mipmap.dsn_btn_jitan);
        bt_jitan.setTextColor(Y.getColor(R.color.color_main));
    }

    private void setJitanUiKai() {
        isJitan = true;
        bt_jitan.setBackgroundResource(R.mipmap.dsn_btn_sel_jitan);
        bt_jitan.setTextColor(Y.getColor(R.color.color_main_fu));
    }


    private void setYoubengUiGuan() {
        isYoubeng = false;
        bt_youbeng.setBackgroundResource(R.mipmap.dsn_btn_xgongyou);
        bt_youbeng.setTextColor(Y.getColor(R.color.color_main));
    }

    private void setYoubengUiKai() {
        isYoubeng = true;
        bt_youbeng.setBackgroundResource(R.mipmap.dsn_btn_sel_gongyou);
        bt_youbeng.setTextColor(Y.getColor(R.color.color_main_fu));
    }

    private void setShuibengUiGuan() {
        isShuibeng = false;
        bt_shuibeng.setBackgroundResource(R.mipmap.dsn_btn_xunhuan);
        bt_shuibeng.setTextColor(Y.getColor(R.color.color_main));
    }

    private void setShuibengUiKai() {
        isShuibeng = true;
        bt_shuibeng.setBackgroundResource(R.mipmap.dsn_btn_sel_xunhuan);
        bt_shuibeng.setTextColor(Y.getColor(R.color.color_main_fu));
    }

    private void setUiGuanji() {
        isKaiji = false;
        iv_heater_host.setBackgroundResource(R.drawable.shuinuan_guanji);

        bt_kaiji.setText("??????");
        bt_kaiji.setBackgroundResource(R.mipmap.dsn_btn_kaiji);
        bt_kaiji.setTextColor(Y.getColor(R.color.color_main));

        bt_shuibeng.setBackgroundResource(R.mipmap.dsn_btn_xunhuan);
        bt_shuibeng.setTextColor(Y.getColor(R.color.color_main));

        bt_youbeng.setBackgroundResource(R.mipmap.dsn_btn_xgongyou);
        bt_youbeng.setTextColor(Y.getColor(R.color.color_main));

        bt_jitan.setBackgroundResource(R.mipmap.dsn_btn_jitan);
        bt_jitan.setTextColor(Y.getColor(R.color.color_main));
    }

    private void setUiKaiji() {
        isKaiji = true;
        isShuibeng = false;
        isYoubeng = false;
        isJitan = false;

        iv_heater_host.setBackgroundResource(R.drawable.shuinuan_kaiji);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_heater_host.getBackground();
        animationDrawable.start();

        bt_kaiji.setText("??????");
        bt_kaiji.setBackgroundResource(R.mipmap.dsn_btn_sel_daji);
        bt_kaiji.setTextColor(Y.getColor(R.color.color_main_fu));

        if (shuibeng_state.equals("1")) {
            bt_shuibeng.setBackgroundResource(R.mipmap.dsn_btn_sel_xunhuan);
            bt_shuibeng.setTextColor(Y.getColor(R.color.color_main_fu));
        } else {
            bt_shuibeng.setBackgroundResource(R.mipmap.dsn_btn_xunhuan);
            bt_shuibeng.setTextColor(Y.getColor(R.color.color_main));
        }

        if (youbeng_state.equals("1")) {
            bt_youbeng.setBackgroundResource(R.mipmap.dsn_btn_sel_gongyou);
            bt_youbeng.setTextColor(Y.getColor(R.color.color_main_fu));
        } else {
            bt_youbeng.setBackgroundResource(R.mipmap.dsn_btn_xgongyou);
            bt_youbeng.setTextColor(Y.getColor(R.color.color_main));
        }

        if (jitan_state.equals("1")) {
            bt_jitan.setBackgroundResource(R.mipmap.dsn_btn_sel_jitan);
            bt_jitan.setTextColor(Y.getColor(R.color.color_main_fu));
        } else {
            bt_jitan.setBackgroundResource(R.mipmap.dsn_btn_jitan);
            bt_jitan.setTextColor(Y.getColor(R.color.color_main));
        }
    }

    private void sendMingling() {
        if (typeMingling == 0) {//??????????????????
            getNsData();
        } else if (typeMingling == 1) {//??????
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
        } else if (typeMingling == 2) {//??????
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
        } else if (typeMingling == 3) {//????????????
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
        } else if (typeMingling == 4) {//????????????
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
        } else if (typeMingling == 5) {//????????????
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
        } else if (typeMingling == 6) {//????????????
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
        } else if (typeMingling == 7) {//????????????
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
        } else if (typeMingling == 8) {//????????????
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
        } else if (typeMingling == 9) {//??????????????????
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
        } else if (typeMingling == 10) {//??????????????????
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
        handlerTime10.sendMessageDelayed(message, 20000);
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
