package com.lipinkeji.cn.activity.device_fengnuan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_a.JiareqiGuzhangActivity;
import com.lipinkeji.cn.app.App;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.config.MyApplication;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.dialog.MyCarCaoZuoDialog_Notify;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;
import com.lipinkeji.cn.util.DoMqttValue;
import com.lipinkeji.cn.util.SoundPoolUtils;
import com.lipinkeji.cn.util.Y;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;
import com.rairmmd.andmqtt.MqttUnSubscribe;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.lipinkeji.cn.config.MyApplication.CARBOX_GETNOW;
import static com.lipinkeji.cn.config.MyApplication.CAR_CTROL;
import static com.lipinkeji.cn.config.MyApplication.CAR_NOTIFY;

public class LipinFengnuanActivityNew extends BaseActivity implements View.OnLongClickListener {


    @BindView(R.id.bt_back)
    RelativeLayout bt_back;
    @BindView(R.id.iv_jiareqi)
    ImageView iv_jiareqi;
    @BindView(R.id.ll_dingshi)
    LinearLayout ll_dingshi;
    @BindView(R.id.iv_xinhao)
    ImageView iv_xinhao;
    @BindView(R.id.tv_xinhao)
    TextView tv_xinhao;
    @BindView(R.id.tv_dianya)
    TextView tv_dianya;
    @BindView(R.id.tv_chufengkouwendu)
    TextView tv_chufengkouwendu;
    @BindView(R.id.id_line)
    View id_line;
    @BindView(R.id.tv_huanjingwendu)
    TextView tv_huanjingwendu;
    @BindView(R.id.tv_rufengkouwendu)
    TextView tv_rufengkouwendu;
    @BindView(R.id.tv_shebeima)
    TextView tv_shebeima;
    @BindView(R.id.tv_dangqianzhuangtai)
    TextView tv_dangqianzhuangtai;
    @BindView(R.id.tv_youxiaoqi)
    TextView tv_youxiaoqi;
    @BindView(R.id.tv_dangwei)
    TextView tv_dangwei;
    @BindView(R.id.view_dangwei5)
    ImageView view_dangwei5;
    @BindView(R.id.view_dangwei4)
    ImageView view_dangwei4;
    @BindView(R.id.view_dangwei3)
    ImageView view_dangwei3;
    @BindView(R.id.view_dangwei2)
    ImageView view_dangwei2;
    @BindView(R.id.view_dangwei1)
    ImageView view_dangwei1;
    @BindView(R.id.tv_wendu)
    TextView tv_wendu;
    @BindView(R.id.view_wendu5)
    ImageView view_wendu5;
    @BindView(R.id.view_wendu4)
    ImageView view_wendu4;
    @BindView(R.id.view_wendu3)
    ImageView view_wendu3;
    @BindView(R.id.view_wendu2)
    ImageView view_wendu2;
    @BindView(R.id.view_wendu1)
    ImageView view_wendu1;
    @BindView(R.id.bt_set)
    TextView bt_set;
    @BindView(R.id.bt1)
    View bt1;
    @BindView(R.id.bt2)
    View bt2;
    @BindView(R.id.bt3)
    View bt3;
    @BindView(R.id.bt4)
    View bt4;
    @BindView(R.id.bt5)
    View bt5;
    @BindView(R.id.iv_mode_shoudong)
    ImageView iv_mode_shoudong;
    @BindView(R.id.tv_mode_shoudong)
    TextView tv_mode_shoudong;
    @BindView(R.id.iv_jia)
    ImageView iv_jia;
    @BindView(R.id.tv_jia)
    TextView tv_jia;
    @BindView(R.id.iv_jian)
    ImageView iv_jian;
    @BindView(R.id.tv_jian)
    TextView tv_jian;
    @BindView(R.id.iv_mode_zidong)
    ImageView iv_mode_zidong;
    @BindView(R.id.tv_mode_zidong)
    TextView tv_mode_zidong;
    @BindView(R.id.bt_tongfeng)
    TextView bt_tongfeng;
    @BindView(R.id.bt_bengyou)
    TextView bt_bengyou;
    @BindView(R.id.rl_shezhi)
    RelativeLayout rlShezhi;

    private boolean isFirst;//是否第一次进入
    private boolean isKaiji;//是否开机
    private boolean isOnActivity;//是否处于当前页面
    private boolean isHenwenMode;//true恒温模式   false档位模式
    private int typeZaixian;//1 在线、2 离线、3 连接中
    private int typeMingling;//0 发送实时数据、1 档位模式、2 恒温模式、3 关机、4 预泵油、5 预通风、6 水泵、
    private int typeWendang;//1 改变温度、2 改变档位

    private String jiareqizhuangtai;//1.档位开机2.空调开机3.关机 4.水泵开机9.关机中6.预泵油7.预通风
    private String dangwei;
    private String dangweiYushe;
    private String yushewendu;
    private String yushewenduYushe;

    private MyCarCaoZuoDialog_Notify myCarCaoZuoDialog_notify;
    private TishiDialog bengyouDialog;
    private TishiDialog tongfengDialog;

    private String sim_ccid_save_type;
    public static String messageData;
    private String sendMsg;

    private boolean isCanKaiguan;
    private boolean isSetWenduDangwei;


    @Override
    public int getContentViewResId() {
        return R.layout.a_fengnuan_act_main_lipin;
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
        Intent intent = new Intent(context, LipinFengnuanActivityNew.class);
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
        setUiGuanji();

        showProgressDialog("数据加载中，请稍后");
    }

    private void initData() {
        String ccid = PreferenceHelper.getInstance(mContext).getString("ccid", "");
        ccid = ccid.replace("a", "");
        tv_shebeima.setText("设备编码：" + ccid);


        String validdate = PreferenceHelper.getInstance(mContext).getString("validdate", "0");
        tv_youxiaoqi.setText(validdate);

        sim_ccid_save_type = PreferenceHelper.getInstance(mContext).getString("sim_ccid_save_type", "0");
        PreferenceHelper.getInstance(mContext).putString(App.CHOOSE_KONGZHI_XIANGMU, DoMqttValue.FENGNUAN);
        isFirst = true;
        isKaiji = false;
        isHenwenMode = false;
        jiareqizhuangtai = "3";
        typeZaixian = 3;

        MyApplication.mqttDingyue.add(CAR_NOTIFY);
        MyApplication.mqttDingyue.add(CARBOX_GETNOW);
        MyApplication.mqttDingyue.add(CAR_CTROL);

        bt1.setOnLongClickListener(this);
        bt4.setOnLongClickListener(this);
        bt5.setOnLongClickListener(this);
        bt_tongfeng.setOnLongClickListener(this);
        bt_bengyou.setOnLongClickListener(this);

        isCanKaiguan = true;
        isSetWenduDangwei = false;
        timeWendang = 0;
    }

    private void initMqtt() {
        time = 0;
        typeMingling = 0;
        registerKtMqtt();
        sendMingling();
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
        messageData = msg.substring(1);
        Y.e("风暖的实时数据是" + messageData + "   " + messageData.length());

        dismissProgressDialog();
        //当前档位1至5档	    1
        dangweiYushe = messageData.substring(0, 1);
        if (TextUtils.isEmpty(dangwei)) {
            dangwei = dangweiYushe;
        }

        //预设温度1至32度	2
        yushewenduYushe = messageData.substring(1, 3);
        if (TextUtils.isEmpty(yushewendu)) {
            yushewendu = yushewenduYushe;
        }

        //1.档位开机2.空调开机3.关机 4.水泵开机9.关机中6.预泵油7.预通风	1
        jiareqizhuangtai = messageData.substring(3, 4);

        //当前温度 256=25.6     3
        int dangqianwenduDian = Y.getInt(messageData.substring(6, 7));
        if (dangqianwenduDian == 0) {
            tv_huanjingwendu.setText(Y.getInt(messageData.substring(4, 6)) + "℃");
        } else {
            String dangqianwendu = Y.getInt(messageData.substring(4, 6)) + "." + messageData.substring(6, 7);
            tv_huanjingwendu.setText(dangqianwendu + "℃");
        }

        //信号强度 00-35    2
        String xinhaoqiangdu = messageData.substring(7, 9);

        //预留字段，占位    1
        String yuliu = messageData.substring(9, 10);

        //电压->0253 = 25.3    4
        String dianya = Y.getInt(messageData.substring(10, 13)) + "." + messageData.substring(13, 14);

        //风机转速->13245    5
        int fenjizhuansu = Y.getInt(messageData.substring(14, 19));
//        tv_fengjizhuansu.setText("风机转速:" + fenjizhuansu + "r");

        //加热塞功率->0264=26.4	    4
        String jiaresaigonglv = Y.getInt(messageData.substring(19, 22)) + "." + messageData.substring(22, 23);

        //油泵频率->0265=26.5       4
        String a = messageData.substring(26, 27);
        String youbengpinlv;
        if (a.equals("a")) {
//            tv_youbengpinlv.setText("油泵频率：0Hz");
        } else {
            youbengpinlv = Y.getInt(messageData.substring(23, 26)) + "." + messageData.substring(26, 27);
//            tv_youbengpinlv.setText("油泵频率：" + youbengpinlv + "Hz");
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
        String hanyangliang = messageData.substring(53);

        tv_dianya.setText(dianya + "V");
        tv_chufengkouwendu.setText(Y.getInt(wendu_chufengkou) + "℃");
        tv_rufengkouwendu.setText(Y.getInt(wendu_rufengkou) + "℃");


        if (typeMingling == 1) {
            if (jiareqizhuangtai.equals("1")) {
                isCanKaiguan = true;
                handlerStart.removeMessages(1);
            }
        } else if (typeMingling == 2) {
            if (jiareqizhuangtai.equals("2")) {
                isCanKaiguan = true;
                handlerStart.removeMessages(1);
            }
        } else if (typeMingling == 3) {
            if (jiareqizhuangtai.equals("3")) {
                isCanKaiguan = true;
                handlerStart.removeMessages(1);
            }
        } else if (typeMingling == 4) {
            if (jiareqizhuangtai.equals("6")) {
                isCanKaiguan = true;
                handlerStart.removeMessages(1);
            }
        } else if (typeMingling == 5) {
            if (jiareqizhuangtai.equals("7")) {
                isCanKaiguan = true;
                handlerStart.removeMessages(1);
            }
        } else {
            isCanKaiguan = true;
            handlerStart.removeMessages(1);
        }

        if (isCanKaiguan) {
            if (jiareqizhuangtai.equals("1")) {
                setUiShoudong();
            } else if (jiareqizhuangtai.equals("2")) {
                setUiHengwen();
            } else if (jiareqizhuangtai.equals("3")) {
                setUiGuanji();
            } else if (jiareqizhuangtai.equals("6")) {
                setUiBengyou();
            } else if (jiareqizhuangtai.equals("7")) {
                setUiTongfeng();
            } else {
                setUiGuanji();
            }
        }


        if (!StringUtils.isEmpty(guzhangdaima)) {
            guzhangdaima = 0 <= guzhangdaima.indexOf("a") ? "" : String.valueOf(Integer.parseInt(guzhangdaima));
        }

        if (!StringUtils.isEmpty(guzhangdaima)) {
            if (isOnActivity) {
                if (!myCarCaoZuoDialog_notify.isShowing()) {
                    myCarCaoZuoDialog_notify.show();
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

        firstCaozuo();
        tv_xinhao.setText("在线");
        typeZaixian = 1;
    }


    private void firstCaozuo() {
        if (isFirst) {
            //向水暖加热器发送获取实时数据
            if (!sim_ccid_save_type.equals("1")) {
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("X.")
                        .setQos(2).setRetained(false)
                        .setTopic(CAR_CTROL), new IMqttActionListener() {
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
                    .setMsg("Y.")
                    .setQos(2).setRetained(false)
                    .setTopic(CAR_CTROL), new IMqttActionListener() {
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

    private void setwendu(String yushewendu) {
        view_wendu1.setImageResource(R.mipmap.fn_dangkuangdi);
        view_wendu2.setImageResource(R.mipmap.fn_dangkuangdi);
        view_wendu3.setImageResource(R.mipmap.fn_dangkuangdi);
        view_wendu4.setImageResource(R.mipmap.fn_dangkuangdi);
        view_wendu5.setImageResource(R.mipmap.fn_dangkuangdi);
        tv_wendu.setText("--℃");
        int wendu = Y.getInt(yushewendu);
        if (isKaiji) {
            if (wendu >= 1 && wendu <= 6) {
                view_wendu1.setImageResource(R.mipmap.fn_dangkuang);
            } else if (wendu >= 7 && wendu <= 13) {
                view_wendu1.setImageResource(R.mipmap.fn_dangkuang);
                view_wendu2.setImageResource(R.mipmap.fn_dangkuang);
            } else if (wendu >= 14 && wendu <= 20) {
                view_wendu1.setImageResource(R.mipmap.fn_dangkuang);
                view_wendu2.setImageResource(R.mipmap.fn_dangkuang);
                view_wendu3.setImageResource(R.mipmap.fn_dangkuang);
            } else if (wendu >= 21 && wendu <= 27) {
                view_wendu1.setImageResource(R.mipmap.fn_dangkuang);
                view_wendu2.setImageResource(R.mipmap.fn_dangkuang);
                view_wendu3.setImageResource(R.mipmap.fn_dangkuang);
                view_wendu4.setImageResource(R.mipmap.fn_dangkuang);
            } else if (wendu >= 27 && wendu <= 33) {
                view_wendu1.setImageResource(R.mipmap.fn_dangkuang);
                view_wendu2.setImageResource(R.mipmap.fn_dangkuang);
                view_wendu3.setImageResource(R.mipmap.fn_dangkuang);
                view_wendu4.setImageResource(R.mipmap.fn_dangkuang);
                view_wendu5.setImageResource(R.mipmap.fn_dangkuang);
            }
            tv_wendu.setText(yushewendu + "℃");
        }
    }

    private void setDangwei(String oper_dang) {
        view_dangwei1.setImageResource(R.mipmap.fn_dangkuangdi);
        view_dangwei2.setImageResource(R.mipmap.fn_dangkuangdi);
        view_dangwei3.setImageResource(R.mipmap.fn_dangkuangdi);
        view_dangwei4.setImageResource(R.mipmap.fn_dangkuangdi);
        view_dangwei5.setImageResource(R.mipmap.fn_dangkuangdi);
        tv_dangwei.setText("--档");
        if (isKaiji) {
            if (oper_dang.equals("1")) {
                tv_dangwei.setText("1档");
                view_dangwei1.setImageResource(R.mipmap.fn_dangkuang);
            } else if (oper_dang.equals("2")) {
                tv_dangwei.setText("2档");
                view_dangwei1.setImageResource(R.mipmap.fn_dangkuang);
                view_dangwei2.setImageResource(R.mipmap.fn_dangkuang);
            } else if (oper_dang.equals("3")) {
                tv_dangwei.setText("3档");
                view_dangwei1.setImageResource(R.mipmap.fn_dangkuang);
                view_dangwei2.setImageResource(R.mipmap.fn_dangkuang);
                view_dangwei3.setImageResource(R.mipmap.fn_dangkuang);
            } else if (oper_dang.equals("4")) {
                tv_dangwei.setText("4档");
                view_dangwei1.setImageResource(R.mipmap.fn_dangkuang);
                view_dangwei2.setImageResource(R.mipmap.fn_dangkuang);
                view_dangwei3.setImageResource(R.mipmap.fn_dangkuang);
                view_dangwei4.setImageResource(R.mipmap.fn_dangkuang);
            } else if (oper_dang.equals("5")) {
                tv_dangwei.setText("5档");
                view_dangwei1.setImageResource(R.mipmap.fn_dangkuang);
                view_dangwei2.setImageResource(R.mipmap.fn_dangkuang);
                view_dangwei3.setImageResource(R.mipmap.fn_dangkuang);
                view_dangwei4.setImageResource(R.mipmap.fn_dangkuang);
                view_dangwei5.setImageResource(R.mipmap.fn_dangkuang);
            }
        }
    }

    private void initDialog() {
        myCarCaoZuoDialog_notify = new MyCarCaoZuoDialog_Notify(mContext, new MyCarCaoZuoDialog_Notify.OnDialogItemClickListener() {
            @Override
            public void clickLeft() {
                finish();
            }

            @Override
            public void clickRight() {
                JiareqiGuzhangActivity.actionStart(mContext);
            }
        });
        myCarCaoZuoDialog_notify.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG);

        initTongfeng();
        initBengyou();
    }

    private void initTongfeng() {
        tongfengDialog = new TishiDialog(mContext, TishiDialog.TYPE_XIAOXI, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {
                tongfengDialog.setTextConfirm("正在停止...");
                time = 0;
                handlerStart.removeMessages(1);
                typeMingling = 3;
                sendMingling();
            }

            @Override
            public void onDismiss(TishiDialog dialog) {

            }
        });
        tongfengDialog.setTextTitle("预通风模式运行中");
        tongfengDialog.setTextContent("正在通风");
        tongfengDialog.setTextConfirm("停止通风");
        tongfengDialog.setTextCancel("");
        tongfengDialog.setDismissAfterClick(false);
    }

    private void initBengyou() {
        bengyouDialog = new TishiDialog(mContext, TishiDialog.TYPE_XIAOXI, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {
                bengyouDialog.setTextConfirm("正在停止...");
                time = 0;
                handlerStart.removeMessages(1);
                typeMingling = 3;
                sendMingling();
            }

            @Override
            public void onDismiss(TishiDialog dialog) {

            }
        });
        bengyouDialog.setTextTitle("预泵油模式运行中");
        bengyouDialog.setTextContent("正在泵油");
        bengyouDialog.setTextConfirm("停止泵油");
        bengyouDialog.setTextCancel("");
        bengyouDialog.setDismissAfterClick(false);
    }


    @OnClick({R.id.bt_back, R.id.ll_dingshi, R.id.bt_set, R.id.bt2, R.id.bt3, R.id.rl_shezhi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.ll_dingshi:
                FengnuanDingshiActivity.actionStart(mContext);
                break;
            case R.id.bt_set:
                FengnuanSetActivity.actionStart(mContext);
                break;
            case R.id.bt2:
                clickDown();
                break;
            case R.id.bt3:
                clickUp();
                break;
            case R.id.rl_shezhi:
                FengnuanSetActivity.actionStart(mContext);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (typeZaixian == 1) {
            switch (v.getId()) {
                case R.id.bt1:
                    clickShoudong();
                    break;
                case R.id.bt4:
                    clickHengwen();
                    break;
                case R.id.bt5:
                    kaiguan();
                    break;
                case R.id.bt_tongfeng:
                    clickTongfeng();
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

    private void clickBengyou() {
        if (isKaiji) {
            Y.t("请关机后再执行泵油操作！");
            return;
        }

        time = 0;
        handlerStart.removeMessages(1);

        SoundPoolUtils.soundPool(mContext, R.raw.yubengyou);
        typeMingling = 4;//预泵油
        setUiBengyou();
        sendMingling();
    }

    private void clickTongfeng() {
        if (isKaiji) {
            Y.t("请关机后再执行通风操作！");
            return;
        }

        time = 0;
        handlerStart.removeMessages(1);

        SoundPoolUtils.soundPool(mContext, R.raw.yutongfeng);
        typeMingling = 5;//预通风
        setUiTongfeng();
        sendMingling();
    }


    private void clickUp() {
        if (typeZaixian != 1) {
            return;
        }

        if (!isKaiji) {
            return;
        }

        if (jiareqizhuangtai.equals("3")) {
            Y.t("正在开机中，请稍后...");
            return;
        }

        if (isHenwenMode) {
            int wendu = Y.getInt(yushewendu);
            if (wendu >= 33) {
                Y.t("预设温度已达最高值，无法调高！");
                return;
            }
            handlerWendang.removeMessages(1);
            timeWendang = 0;

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
            sendMsg = "M6" + mingling + ".";
            typeWendang = 1;
            sendMinglingWendang();
        } else {
            if (dangwei.equals("5")) {
                Y.t("已是最高档位，无法升档！");
                return;
            }
            handlerWendang.removeMessages(1);
            timeWendang = 0;

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
            sendMsg = "M62" + dangwei + ".";
            typeWendang = 2;
            sendMinglingWendang();
        }
    }

    private void clickDown() {
        if (typeZaixian != 1) {
            return;
        }

        if (!isKaiji) {
            return;
        }

        if (jiareqizhuangtai.equals("3")) {
            Y.t("正在开机中，请稍后...");
            return;
        }

        if (isHenwenMode) {
            int wendu = Y.getInt(yushewendu);
            if (wendu <= 0) {
                Y.t("预设温度已达最低值，无法降低！");
                return;
            }
            handlerWendang.removeMessages(1);
            timeWendang = 0;

            wendu--;

            if (wendu < 10) {
                yushewendu = "0" + wendu;
            } else {
                yushewendu = "" + wendu;
            }

            setwendu(yushewendu);
            String mingling = 50 + wendu + "";
            sendMsg = "M6" + mingling + ".";
            typeWendang = 1;
            sendMinglingWendang();
        } else {
            if (dangwei.equals("1")) {
                Y.t("已是最低档位，无法降档！");
                return;
            }

            handlerWendang.removeMessages(1);
            timeWendang = 0;

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
            sendMsg = "M62" + dangwei + ".";
            typeWendang = 2;
            sendMinglingWendang();
        }
    }

    private void clickHengwen() {

        time = 0;
        isSetWenduDangwei = true;
        handlerStart.removeMessages(1);
        handlerWendang.removeMessages(1);

        SoundPoolUtils.soundPool(mContext, R.raw.mode_temp);
        setUiHengwen();
        typeMingling = 2;//档位模式

        sendMingling();

    }

    private void clickShoudong() {

        time = 0;
        isSetWenduDangwei = false;
        handlerStart.removeMessages(1);
        handlerWendang.removeMessages(1);

        SoundPoolUtils.soundPool(mContext, R.raw.mode_gear);
        setUiShoudong();
        typeMingling = 1;//档位模式

        sendMingling();

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

        if (isHenwenMode) {
            clickHengwen();
        } else {
            clickShoudong();
        }
    }


    private void guanji() {
        if (!isKaiji) {
            return;
        }

        time = 0;
        isSetWenduDangwei = false;
        handlerStart.removeMessages(1);
        handlerWendang.removeMessages(1);

        SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_start_off);
        typeMingling = 3;
        setUiGuanji();
        sendMingling();
    }


    private void setUiShoudong() {
        isKaiji = true;
        isHenwenMode = false;

        if (isSetWenduDangwei) {
            if (dangwei.equals(dangweiYushe)) {
                handlerWendang.removeMessages(1);
                isSetWenduDangwei = false;
                setDangwei(dangwei);
            }
        } else {
            handlerWendang.removeMessages(1);
            isSetWenduDangwei = false;
            dangwei = dangweiYushe;
            setDangwei(dangwei);
        }

        Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji).into(iv_jiareqi);

        iv_mode_shoudong.setImageResource(R.mipmap.fn_shoudong_sel);
        tv_mode_shoudong.setTextColor(Y.getColor(R.color.color_lipin));

        iv_mode_zidong.setImageResource(R.mipmap.fn_zd);
        tv_mode_zidong.setTextColor(Y.getColor(R.color.white));

        iv_jia.setImageResource(R.mipmap.fn_jia_sel);
        tv_jia.setTextColor(Y.getColor(R.color.color_lipin));

        iv_jian.setImageResource(R.mipmap.fn_jian_sel);
        tv_jian.setTextColor(Y.getColor(R.color.color_lipin));

        tv_dangqianzhuangtai.setText("档位模式：" + dangwei + "档");

        if (bengyouDialog != null && bengyouDialog.isShowing()) {
            bengyouDialog.dismiss();
        }

        if (tongfengDialog != null && tongfengDialog.isShowing()) {
            tongfengDialog.dismiss();
        }
    }

    private void setUiHengwen() {
        isKaiji = true;
        isHenwenMode = true;

        if (isSetWenduDangwei) {
            if (yushewendu.equals(yushewenduYushe)) {
                handlerWendang.removeMessages(1);
                isSetWenduDangwei = false;
                setwendu(yushewendu);
            }
        } else {
            handlerWendang.removeMessages(1);
            isSetWenduDangwei = false;
            yushewendu = yushewenduYushe;
            setwendu(yushewendu);
        }

        Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji).into(iv_jiareqi);

        iv_mode_shoudong.setImageResource(R.mipmap.fn_shoudong);
        tv_mode_shoudong.setTextColor(Y.getColor(R.color.white));

        iv_mode_zidong.setImageResource(R.mipmap.fn_zd_sel);
        tv_mode_zidong.setTextColor(Y.getColor(R.color.color_lipin));

        iv_jia.setImageResource(R.mipmap.fn_jia_sel);
        tv_jia.setTextColor(Y.getColor(R.color.color_lipin));

        iv_jian.setImageResource(R.mipmap.fn_jian_sel);
        tv_jian.setTextColor(Y.getColor(R.color.color_lipin));

        tv_dangqianzhuangtai.setText("恒温模式：" + yushewendu + "℃");

        if (bengyouDialog != null && bengyouDialog.isShowing()) {
            bengyouDialog.dismiss();
        }

        if (tongfengDialog != null && tongfengDialog.isShowing()) {
            tongfengDialog.dismiss();
        }
    }

    private void setUiGuanji() {
        handlerWendang.removeMessages(1);
        isKaiji = false;
        isSetWenduDangwei = false;

        if (isHenwenMode) {
            setwendu(yushewendu);
        } else {
            setDangwei(dangwei);
        }

        Glide.with(mContext).load(R.mipmap.fegnnuan_guanji).into(iv_jiareqi);

        iv_mode_shoudong.setImageResource(R.mipmap.fn_shoudong);
        tv_mode_shoudong.setTextColor(Y.getColor(R.color.white));

        iv_mode_zidong.setImageResource(R.mipmap.fn_zd);
        tv_mode_zidong.setTextColor(Y.getColor(R.color.white));

        iv_jia.setImageResource(R.mipmap.fn_jia);
        tv_jia.setTextColor(Y.getColor(R.color.white));

        iv_jian.setImageResource(R.mipmap.fn_jian);
        tv_jian.setTextColor(Y.getColor(R.color.white));

        tv_dangqianzhuangtai.setText("设备已关闭");


        bt_tongfeng.setBackgroundResource(R.mipmap.fn_btn_moshi_nor);
        bt_tongfeng.setTextColor(Y.getColor(R.color.yjt_danwei_wendu_nor));

        bt_bengyou.setBackgroundResource(R.mipmap.fn_btn_moshi_nor);
        bt_bengyou.setTextColor(Y.getColor(R.color.yjt_danwei_wendu_nor));

        if (bengyouDialog != null && bengyouDialog.isShowing()) {
            bengyouDialog.dismiss();
        }

        if (tongfengDialog != null && tongfengDialog.isShowing()) {
            tongfengDialog.dismiss();
        }
    }


    private void setUiBengyou() {
        Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji_mode).into(iv_jiareqi);
        isKaiji = false;
        bt_bengyou.setBackgroundResource(R.mipmap.fn_btn_moshi_sel);
        bt_bengyou.setTextColor(Y.getColor(R.color.yjt_danwei_wendu_sel));
        bengyouDialog.setTextConfirm("停止泵油");
        if (bengyouDialog != null && !bengyouDialog.isShowing()) {
            bengyouDialog.show();
        }

        tv_dangqianzhuangtai.setText("预泵油");
    }

    private void setUiTongfeng() {
        Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji_mode).into(iv_jiareqi);
        isKaiji = false;
        bt_tongfeng.setBackgroundResource(R.mipmap.fn_btn_moshi_sel);
        bt_tongfeng.setTextColor(Y.getColor(R.color.yjt_danwei_wendu_sel));
        tongfengDialog.setTextConfirm("停止通风");
        if (tongfengDialog != null && !tongfengDialog.isShowing()) {
            tongfengDialog.show();
        }

        tv_dangqianzhuangtai.setText("预通风");
    }

    private void sendMingling() {
        isCanKaiguan = false;
        initHandlerMingling();

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
        } else if (typeMingling == 5) {//预通风
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M617.")
                    .setQos(2).setRetained(false)
                    .setTopic(CAR_CTROL), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 6) {//水泵模式
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M711.")
                    .setQos(2).setRetained(false)
                    .setTopic(CAR_CTROL), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 7) {//水泵模式关
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M712.")
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

    private void sendMinglingWendang() {
        isSetWenduDangwei = true;
        initHandlerWendagn();
        if (typeWendang == 1) {//改变温度
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(sendMsg)
                    .setQos(2).setRetained(false)
                    .setTopic(CAR_CTROL), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeWendang == 2) {//改变档位
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(sendMsg)
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


    private int time;

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


    private int timeWendang;

    private Handler handlerWendang = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            timeWendang += 5;
            switch (msg.what) {
                case 1:
                    if (timeWendang >= 15) {
                        isSetWenduDangwei = false;
                        handlerWendang.removeMessages(1);
                    } else {
                        sendMinglingWendang();
                    }
                    break;
            }
            Y.e(msg.what + "  温档时间时多少啊  " + timeWendang);
            return false;
        }
    });

    private void initHandlerWendagn() {
        Message message = handlerWendang.obtainMessage(1);
        handlerWendang.sendMessageDelayed(message, 5000);
    }

    private void showTishiDialog() {
        tv_xinhao.setText("离线");
        iv_xinhao.setImageResource(R.mipmap.fengnuan_icon_signal1);
        handlerStart.removeMessages(1);
        typeZaixian = 2;
        time = 0;
        isCanKaiguan = true;
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
                    if (isOnActivity) {
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
        handlerTime10.sendMessageDelayed(message, 20000);
    }

    private void getNData() {
        Y.e("我发送了实时数据");
        //向风暖加热器发送获取实时数据
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("N.")
                .setQos(2)
                .setTopic(CAR_CTROL)
                .setRetained(false), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
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
        handlerWendang.removeMessages(1);

        PreferenceHelper.getInstance(mContext).removeKey(App.CHOOSE_KONGZHI_XIANGMU);
        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(CAR_NOTIFY), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(CARBOX_GETNOW), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(CAR_CTROL), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

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
