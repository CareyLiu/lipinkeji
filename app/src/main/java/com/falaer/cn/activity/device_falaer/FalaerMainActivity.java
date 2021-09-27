package com.falaer.cn.activity.device_falaer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
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
import com.gyf.barlibrary.ImmersionBar;
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

public class FalaerMainActivity extends BaseActivity implements View.OnLongClickListener {


    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.bt_mode_shuibeng)
    TextView bt_mode_shuibeng;
    @BindView(R.id.iv_set)
    ImageView iv_set;
    @BindView(R.id.ll_shuoming)
    LinearLayout ll_shuoming;
    @BindView(R.id.iv_xinhao)
    ImageView iv_xinhao;
    @BindView(R.id.tv_zaixian)
    TextView tv_zaixian;
    @BindView(R.id.iv_jiareqi)
    ImageView iv_jiareqi;
    @BindView(R.id.tv_dangwei)
    TextView tv_dangwei;
    @BindView(R.id.seekBar1)
    SeekBar seekBar1;
    @BindView(R.id.tv_seek_qian)
    TextView tv_seek_qian;
    @BindView(R.id.tv_seek_hou)
    TextView tv_seek_hou;
    @BindView(R.id.bt_mode_shoudong)
    TextView bt_mode_shoudong;
    @BindView(R.id.bt_mode_hengwen)
    TextView bt_mode_hengwen;
    @BindView(R.id.tv_daqiya)
    TextView tv_daqiya;
    @BindView(R.id.tv_haibagaodu)
    TextView tv_haibagaodu;
    @BindView(R.id.tv_hanyangliang)
    TextView tv_hanyangliang;
    @BindView(R.id.tv_jia)
    TextView tv_jia;
    @BindView(R.id.bt_jia)
    LinearLayout bt_jia;
    @BindView(R.id.tv_jian)
    TextView tv_jian;
    @BindView(R.id.bt_jian)
    LinearLayout bt_jian;
    @BindView(R.id.tv_shebei_state)
    TextView tv_shebei_state;
    @BindView(R.id.tv_gangtiwendu)
    TextView tv_gangtiwendu;
    @BindView(R.id.tv_huanjingwendu)
    TextView tv_huanjingwendu;
    @BindView(R.id.tv_fengjizhuansu)
    TextView tv_fengjizhuansu;
    @BindView(R.id.tv_youbengpinlv)
    TextView tv_youbengpinlv;
    @BindView(R.id.ll_center_kuang)
    LinearLayout ll_center_kuang;
    @BindView(R.id.bt_mode_youbeng)
    TextView bt_mode_youbeng;
    @BindView(R.id.bt_mode_tongfeng)
    TextView bt_mode_tongfeng;

    private boolean isFirst;//是否第一次进入
    private boolean isKaiji;//是否开机
    private boolean isOnActivity;//是否处于当前页面
    private boolean isHenwenMode;//true恒温模式   false档位模式
    private boolean isCanGetNs;//是否处于操作中   false 操作中、true 未操作
    private int typeZaixian;//1 在线、2 离线、3 连接中
    private int typeMingling;//0 发送实时数据、1 档位模式、2 恒温模式、3 关机、4 预泵油、5 预通风、6 水泵

    private String jiareqizhuangtai;//1.档位开机2.空调开机3.关机 4.水泵开机9.关机中6.预泵油7.预通风
    private String dangwei;
    private String yushewendu;
    private TishiDialog bengyouDialog;
    private TishiDialog tongfengDialog;
    private TishiDialog shuibengDialog;
    private MyCarCaoZuoDialog_Notify myCarCaoZuoDialog_notify;

    private String sim_ccid_save_type;

    @Override
    public int getContentViewResId() {
        return R.layout.a_falaer_act_main;
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
        Intent intent = new Intent(context, FalaerMainActivity.class);
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
        initSeekBar();
        initHandlerNS();
    }

    private void initData() {
        sim_ccid_save_type = PreferenceHelper.getInstance(mContext).getString("sim_ccid_save_type", "0");
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
        MyApplication.mqttDingyue.add(CARBOX_GETNOW);
        MyApplication.mqttDingyue.add(CAR_CTROL);

        bt_mode_shoudong.setOnLongClickListener(this);
        bt_mode_hengwen.setOnLongClickListener(this);
        bt_mode_shuibeng.setOnLongClickListener(this);
        bt_mode_youbeng.setOnLongClickListener(this);
        bt_mode_tongfeng.setOnLongClickListener(this);
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
        int dangqianwenduDian = Y.getInt(messageData.substring(6, 7));
        if (dangqianwenduDian==0){
            tv_huanjingwendu.setText("环境温度：" + Y.getInt(messageData.substring(4, 6)) + "℃");
        }else {
            String dangqianwendu = Y.getInt(messageData.substring(4, 6)) + "." + messageData.substring(6, 7);
            tv_huanjingwendu.setText(dangqianwendu);
        }

        //信号强度 00-35    2
        String xinhaoqiangdu = messageData.substring(7, 9);

        //预留字段，占位    1
        String yuliu = messageData.substring(9, 10);

        //电压->0253 = 25.3    4
        String dianya = Y.getInt(messageData.substring(10, 13)) + "." + messageData.substring(13, 14);

        //风机转速->13245    5
        int fenjizhuansu = Y.getInt(messageData.substring(14, 19));
        tv_fengjizhuansu.setText("风机转速:" + fenjizhuansu + "r");

        //加热塞功率->0264=26.4	    4
        String jiaresaigonglv = Y.getInt(messageData.substring(19, 22)) + "." + messageData.substring(22, 23);

        //油泵频率->0265=26.5       4
        String a = messageData.substring(26, 27);
        String youbengpinlv;
        if (a.equals("a")) {
            tv_youbengpinlv.setText("油泵频率：0Hz");
        } else {
            youbengpinlv = Y.getInt(messageData.substring(23, 26)) + "." + messageData.substring(26, 27);
            tv_youbengpinlv.setText("油泵频率：" + youbengpinlv + "Hz");
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


        tv_daqiya.setText("大气压：" + Y.getInt(daqiya) + "kpa");
        tv_haibagaodu.setText("海拔高度：" + Y.getInt(haibagaodu) + "m");
        tv_hanyangliang.setText("含氧量：" + Y.getInt(hanyangliang) + "g/m³");
        tv_gangtiwendu.setText("缸体温度：" + Y.getInt(wendu_chufengkou) + "℃");

        if (shuibengzhuangtai.equals("0")) {
            bt_mode_shuibeng.setEnabled(true);
            setUiGuanji();
        } else if (shuibengzhuangtai.equals("1")) {
            bt_mode_shuibeng.setEnabled(true);
            setUiShuibeng();
        } else {
            bt_mode_shuibeng.setEnabled(false);
            bt_mode_shuibeng.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            bt_mode_shuibeng.setTextColor(Y.getColor(R.color.text_color_9));
        }


        if (jiareqizhuangtai.equals("1")) {
            setUiShoudong(dangwei);
        } else if (jiareqizhuangtai.equals("2")) {
            setUiHengwen(yushewendu);
        } else if (jiareqizhuangtai.equals("3")) {
            setUiGuanji();
        } else if (jiareqizhuangtai.equals("6")) {
            setUiBengyou();
        } else if (jiareqizhuangtai.equals("7")) {
            setUiTongfeng();
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

        firstCaozuo();
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


    private void initDialog() {
        initShuibeng();
        initTongfeng();
        initBengyou();


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

    private void initShuibeng() {
        shuibengDialog = new TishiDialog(mContext, TishiDialog.TYPE_XIAOXI, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {
                shuibengDialog.setTextConfirm("正在停止...");
                typeMingling = 7;
                sendMingling();
                initHandlerMingling();
            }

            @Override
            public void onDismiss(TishiDialog dialog) {

            }
        });
        shuibengDialog.setTextTitle("水泵模式运行中");
        shuibengDialog.setTextContent("水泵模式开启");
        shuibengDialog.setTextConfirm("停止水泵模式");
        shuibengDialog.setTextCancel("");
        shuibengDialog.setCancelable(false);
        shuibengDialog.setDismissAfterClick(false);
        shuibengDialog.setCanceledOnTouchOutside(false);
    }

    private void initTongfeng() {
        tongfengDialog = new TishiDialog(mContext, TishiDialog.TYPE_XIAOXI, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {
                tongfengDialog.setTextConfirm("正在停止...");
                typeMingling = 3;
                sendMingling();
                initHandlerMingling();
            }

            @Override
            public void onDismiss(TishiDialog dialog) {

            }
        });
        tongfengDialog.setTextTitle("预通风模式运行中");
        tongfengDialog.setTextContent("正在通风");
        tongfengDialog.setTextConfirm("停止通风");
        tongfengDialog.setTextCancel("");
        tongfengDialog.setCancelable(false);
        tongfengDialog.setDismissAfterClick(false);
        tongfengDialog.setCanceledOnTouchOutside(false);
    }

    private void initBengyou() {
        bengyouDialog = new TishiDialog(mContext, TishiDialog.TYPE_XIAOXI, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {
                bengyouDialog.setTextConfirm("正在停止...");
                typeMingling = 3;
                sendMingling();
                initHandlerMingling();
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
    }


    private void initSeekBar() {
        seekBar1.setPressed(false);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isHenwenMode) {
                    if (progress < 10) {
                        yushewendu = "0" + progress;
                    } else {
                        yushewendu = "" + progress;
                    }
                    setSeekBar();
                } else {
                    dangwei = progress + "";
                    setSeekBar();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (isHenwenMode) {
                    if (progress < 10) {
                        yushewendu = "0" + progress;
                    } else {
                        yushewendu = "" + progress;
                    }
                    String mingling = 50 + progress + "";
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
                    setSeekBar();
                } else {
                    dangwei = progress + "";
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
                    setSeekBar();
                }
            }
        });
    }

    @SuppressLint("NewApi")
    private void setSeekBar() {
        if (isHenwenMode) {
            if (jiareqizhuangtai.equals("3")) {
                tv_dangwei.setText("设定温度:0℃");
                seekBar1.setProgress(0);
            } else {
                tv_dangwei.setText("设定温度:" + Y.getInt(yushewendu) + "℃");
                seekBar1.setProgress(Y.getInt(yushewendu));
            }
        } else {
            if (jiareqizhuangtai.equals("3")) {
                tv_dangwei.setText("当前档位:1档");
                seekBar1.setProgress(1);
            } else {
                tv_dangwei.setText("当前档位:" + dangwei + "档");
                seekBar1.setProgress(Y.getInt(dangwei));
            }
        }
    }

    @OnClick({R.id.rl_back, R.id.iv_set, R.id.ll_shuoming, R.id.bt_jia, R.id.bt_jian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_set:
                FalaerSetActivity.actionStart(mContext);
                break;
            case R.id.ll_shuoming:
                FalaerShuomingActivity.actionStart(mContext);
                break;
            case R.id.bt_jia:
                clickUp();
                break;
            case R.id.bt_jian:
                clickDown();
                break;
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

    private void setDangwei(String dangwei) {
        seekBar1.setProgress(Y.getInt(dangwei));
        tv_dangwei.setText("当前档位:" + dangwei + "档");
    }


    private void setwendu(String yushewendu) {
        seekBar1.setProgress(Y.getInt(yushewendu));
        tv_dangwei.setText("设定温度:" + Y.getInt(yushewendu) + "℃");
    }

    @Override
    public boolean onLongClick(View v) {
        if (typeZaixian == 1) {
            time = 0;
            handlerStart.removeMessages(1);

            switch (v.getId()) {
                case R.id.bt_mode_shoudong:
                    clickShoudong();
                    break;
                case R.id.bt_mode_hengwen:
                    cilckHengwen();
                    break;
                case R.id.bt_mode_youbeng:
                    clickBengyou();
                    break;
                case R.id.bt_mode_tongfeng:
                    clickTongfeng();
                    break;
                case R.id.bt_mode_shuibeng:
                    clickShuibeng();
                    break;
            }
        } else if (typeZaixian == 3) {
            Y.t("正在连接设备，请稍后...");
        } else if (typeZaixian == 2) {
            showTishiDialog();
        }
        return false;
    }

    private void cilckHengwen() {
        if (!isKaiji) {
            SoundPoolUtils.soundPool(mContext, R.raw.mode_temp);
            typeMingling = 2;//恒温模式
            setUiHengwen(yushewendu);
            sendMingling();
            initHandlerMingling();
        } else if (jiareqizhuangtai.equals("2")) {
            if (isHenwenMode) {
                guanji();
            }
        }
    }

    private void clickShoudong() {
        if (!isKaiji) {
            SoundPoolUtils.soundPool(mContext, R.raw.mode_gear);
            typeMingling = 1;//档位模式
            setUiShoudong(dangwei);
            sendMingling();
            initHandlerMingling();
        } else {
            if (!isHenwenMode) {
                guanji();
            }
        }
    }

    private void guanji() {
        SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_start_off);
        typeMingling = 3;
        setUiGuanji();
        sendMingling();
        initHandlerMingling();
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

    private void clickTongfeng() {
        if (isKaiji) {
            Y.t("请关机后再执行通风操作！");
            return;
        }

        SoundPoolUtils.soundPool(mContext, R.raw.yutongfeng);
        typeMingling = 5;//预通风
        setUiTongfeng();
        sendMingling();
        initHandlerMingling();
    }

    private void clickShuibeng() {
        if (isKaiji) {
            Y.t("请关机后再开启水泵模式！");
            return;
        }

        SoundPoolUtils.soundPool(mContext, R.raw.shuibeng);
        typeMingling = 6;//水泵模式
        setUiShuibeng();
        sendMingling();
        initHandlerMingling();
    }

    @SuppressLint("NewApi")
    private void setUiHengwen(String yushewendu) {
        isKaiji = true;
        isHenwenMode = true;
        seekBar1.setPressed(true);
        tv_seek_qian.setText("0℃");
        tv_seek_hou.setText("33℃");
        seekBar1.setMax(33);
        seekBar1.setMin(0);
        seekBar1.setProgress(Y.getInt(yushewendu));
        tv_dangwei.setText("设定温度:" + Y.getInt(yushewendu) + "℃");

        tv_jia.setText("升温");
        tv_jian.setText("降温");

        Glide.with(mContext).load(R.mipmap.falaer_shebei_hengwen).into(iv_jiareqi);
        isKaiji = true;

        bt_mode_hengwen.setBackgroundResource(R.mipmap.falaer_bt_sel);
        bt_mode_shoudong.setBackgroundResource(R.mipmap.falaer_bt_nor);

        tv_shebei_state.setText("设备状态:恒温模式");
    }

    @SuppressLint("NewApi")
    private void setUiShoudong(String dangwei) {
        isKaiji = true;
        isHenwenMode = false;
        seekBar1.setPressed(true);
        tv_seek_qian.setText("1档");
        tv_seek_hou.setText("5档");
        seekBar1.setMax(5);
        seekBar1.setMin(1);
        seekBar1.setProgress(Y.getInt(dangwei));
        tv_dangwei.setText("当前档位:" + dangwei + "档");

        tv_jia.setText("加档");
        tv_jian.setText("减档");

        Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji_falaer).into(iv_jiareqi);
        isKaiji = true;

        bt_mode_shoudong.setBackgroundResource(R.mipmap.falaer_bt_sel);
        bt_mode_hengwen.setBackgroundResource(R.mipmap.falaer_bt_nor);

        tv_shebei_state.setText("设备状态:手动模式");
    }

    private void setUiGuanji() {
        isKaiji = false;
        Glide.with(mContext).load(R.mipmap.falaer_shebei_guanji).into(iv_jiareqi);
        seekBar1.setPressed(false);

        bt_mode_hengwen.setBackgroundResource(R.mipmap.falaer_bt_nor);
        bt_mode_shoudong.setBackgroundResource(R.mipmap.falaer_bt_nor);
        bt_mode_youbeng.setBackgroundResource(R.mipmap.falaer_bt_nor);
        bt_mode_tongfeng.setBackgroundResource(R.mipmap.falaer_bt_nor);
        bt_mode_shuibeng.setBackgroundResource(R.mipmap.falaer_bt_nor);

        if (bengyouDialog != null && bengyouDialog.isShowing()) {
            bengyouDialog.dismiss();
        }

        if (tongfengDialog != null && tongfengDialog.isShowing()) {
            tongfengDialog.dismiss();
        }

        if (shuibengDialog != null && shuibengDialog.isShowing()) {
            shuibengDialog.dismiss();
        }

        if (isHenwenMode) {
            tv_dangwei.setText("设定温度:0℃");
            seekBar1.setProgress(0);
        } else {
            tv_dangwei.setText("当前档位:1档");
            seekBar1.setProgress(1);
        }

        tv_shebei_state.setText("设备状态:关机");
    }

    private void setUiBengyou() {
        Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji_mode).into(iv_jiareqi);
        isKaiji = false;
        bt_mode_youbeng.setBackgroundResource(R.mipmap.falaer_bt_sel);
        bengyouDialog.setTextConfirm("停止泵油");
        if (bengyouDialog != null && !bengyouDialog.isShowing()) {
            bengyouDialog.show();
        }

        tv_shebei_state.setText("设备状态:预泵油");
    }

    private void setUiTongfeng() {
        Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji_mode).into(iv_jiareqi);
        isKaiji = false;
        bt_mode_tongfeng.setBackgroundResource(R.mipmap.falaer_bt_sel);
        tongfengDialog.setTextConfirm("停止通风");
        if (tongfengDialog != null && !tongfengDialog.isShowing()) {
            tongfengDialog.show();
        }

        tv_shebei_state.setText("设备状态:预通风");
    }

    private void setUiShuibeng() {
        Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji_mode).into(iv_jiareqi);
        isKaiji = false;
        bt_mode_shuibeng.setBackgroundResource(R.mipmap.falaer_bt_sel);
        shuibengDialog.setTextConfirm("停止水泵模式");
        if (shuibengDialog != null && !shuibengDialog.isShowing()) {
            shuibengDialog.show();
        }

        tv_shebei_state.setText("设备状态:水泵模式");
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

    private void getNData() {
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
