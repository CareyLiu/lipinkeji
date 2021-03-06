package com.lipinkeji.cn.activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.percentlayout.widget.PercentRelativeLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.lipinkeji.cn.activity.device_a.JiareqiGuzhangActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;
import com.rairmmd.andmqtt.MqttUnSubscribe;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.app.App;
import com.lipinkeji.cn.app.AppManager;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.ConfigValue;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.app.UIHelper;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.common.StringUtils;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.MyApplication;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.dialog.LordingDialog;
import com.lipinkeji.cn.dialog.MyCarCaoZuoDialog_Notify;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.CarDetails;
import com.lipinkeji.cn.service.WitMqttFormatService;
import com.lipinkeji.cn.util.AlertUtil;
import com.lipinkeji.cn.util.ConstantUtil;
import com.lipinkeji.cn.util.DoMqttValue;
import com.lipinkeji.cn.util.SoundPoolUtils;
import com.lipinkeji.cn.view.ArcProgressBar;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.lipinkeji.cn.app.ConfigValue.STARTSHELVES;
import static com.lipinkeji.cn.app.ConstanceValue.MSG_MQTT_CONNECTARRIVE;
import static com.lipinkeji.cn.app.ConstanceValue.MSG_MQTT_CONNECTCOMPLETE;
import static com.lipinkeji.cn.app.ConstanceValue.MSG_MQTT_CONNECTLOST;
import static com.lipinkeji.cn.app.ConstanceValue.MSG_MQTT_CONNECT_CHONGLIAN_ONFAILE;
import static com.lipinkeji.cn.app.ConstanceValue.MSG_MQTT_CONNECT_CHONGLIAN_ONSUCCESS;
import static com.lipinkeji.cn.config.MyApplication.CARBOX_GETNOW;
import static com.lipinkeji.cn.config.MyApplication.CAR_CTROL;
import static com.lipinkeji.cn.config.MyApplication.CAR_NOTIFY;
import static com.lipinkeji.cn.config.MyApplication.getAppContext;


public class WindHeaterActivity extends BaseActivity implements View.OnLongClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "WindHeaterActivity";
    public Handler mHandler;
    public static Handler stateHandler;
    @BindView(R.id.iv_heater_host)
    RelativeLayout ivHeaterHost;


    @BindView(R.id.arcProgressBar)
    ArcProgressBar arcProgressBar;
    @BindView(R.id.btn_heater_close)
    Button btnHeaterClose;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.tv_wd)
    TextView mTvWd;
    @BindView(R.id.iv_guanji)
    ImageView ivGuanji;
    @BindView(R.id.iv_kaiji)
    ImageView ivKaiji;
    @BindView(R.id.pl_temperature)
    PercentRelativeLayout plTemperature;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private int progressValue;
    DrawerLayout drawer;
    WitMqttFormatService witMqttFormatService;
    ValueAnimator valueAnimator;
    //????????????
    int gear = 0;
    //??????????????????
    int openMode = 1;
    ImageView ivBrandPic;
    TextView tvBrandName, tvCarNumber;
    List<RadioButton> btns;
    @BindView(R.id.rb_heater_air_mode)
    RadioButton rbHeaterAirMode;
    @BindView(R.id.rb_heater_gear_mode)
    RadioButton rbHeaterGearMode;
    @BindView(R.id.rb_heater_pump_mode)
    RadioButton rbHeaterPumpMode;
    @BindView(R.id.rb_heater_yby_mode)
    RadioButton rbHeaterYbyMode;
    @BindView(R.id.rb_heater_ytf_mode)
    RadioButton rbHeaterYtfMode;
    @BindView(R.id.rg_magnet)
    RadioGroup rgMagnet;
    @BindView(R.id.tv_yushe_wendu)
    TextView tvYuShe_WenDu;
    RadioButton button;
    public String car_server_id;
    public String ccid;
    public String of_user_id;
    Toolbar toolbar;
    String version;
    boolean flag = true;//?????????????????????

    private LordingDialog lordingDialog;
    MyCarCaoZuoDialog_Notify myCarCaoZuoDialog_notify;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_wind_heater;
    }

    ProgressDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceHelper.getInstance(mContext).putString(App.CHOOSE_KONGZHI_XIANGMU, DoMqttValue.FENGNUAN);

        ButterKnife.bind(this);
        waitDialog = ProgressDialog.show(mContext, null, "?????????????????????,???????????????", true, true);
        lordingDialog = new LordingDialog(mContext);
        initView();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        @SuppressLint("ResourceType") ColorStateList csl = getResources().getColorStateList(R.drawable.menu_item_color);
        navigationView.setItemTextColor(csl);
        ivBrandPic = navigationView.getHeaderView(0).findViewById(R.id.iv_brand_pic);
        tvBrandName = navigationView.getHeaderView(0).findViewById(R.id.tv_brand_name);
        tvCarNumber = navigationView.getHeaderView(0).findViewById(R.id.tv_car_number);
        mToolbarTitle.setText("???????????????");

        initHandler();
        arcProgressBar.setHandler(mHandler);
        btnHeaterClose.setOnLongClickListener(this);
        rbHeaterAirMode.setOnLongClickListener(this);
        rbHeaterGearMode.setOnLongClickListener(this);
        rbHeaterPumpMode.setOnLongClickListener(this);
        rbHeaterYbyMode.setOnLongClickListener(this);
        rbHeaterYtfMode.setOnLongClickListener(this);
        witMqttFormatService = new WitMqttFormatService();
        car_server_id = PreferenceHelper.getInstance(mContext).getString("car_server_id", "");
        ccid = PreferenceHelper.getInstance(mContext).getString("ccid", "");
        of_user_id = PreferenceHelper.getInstance(mContext).getString("of_user_id", "");
        setMqttZhiLing();

        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                Log.i("message.type", String.valueOf(message.type));
                if (message.type == ConstanceValue.MSG_CAR_J_G) {
                    showLoadSuccess();
                    //???????????????
                    Log.i("msg_car_j_", message.content.toString());
                    String messageData = message.content.toString().substring(1, message.content.toString().length() - 1);
                    if (0 <= messageData.substring(0, 6).indexOf("a")) {
                        // ??????x 045.666666=045666666 9
                        BigDecimal dec = new BigDecimal(messageData.substring(0, 3) + "." + messageData.substring(3, 9));
                        String gps_x = dec.toString();
                        // ??????y 126.666666=126666666 9
                        dec = new BigDecimal(messageData.substring(9, 12) + "." + messageData.substring(12, 18));
                        String gps_y = dec.toString();
                        // ??????????????????:1???2???3???4???5?????? 6??????7?????? 8??????
                        String car_orientations = messageData.substring(18, 19);
                        if (!"a".equals(car_orientations)) {

                            if (car_orientations.equals("1")) {

                            } else if (car_orientations.equals("2")) {

                            } else if (car_orientations.equals("3")) {

                            } else if (car_orientations.equals("4")) {

                            } else if (car_orientations.equals("5")) {

                            } else if (car_orientations.equals("6")) {

                            } else if (car_orientations.equals("7")) {

                            } else if (car_orientations.equals("8")) {

                            }
                        }
                    }
                } else if (message.type == ConstanceValue.MSG_CAR_J_M) {
                    showLoadSuccess();
                    //???????????????
                    Log.i("msg_car_j_m", message.content.toString());

                    String messageData = message.content.toString().substring(1, message.content.toString().length() - 1);
                    Log.i("msg_car_j_m_data", messageData);
                    // ???????????????:????????????1???5???	1	???
                    String oper_dang = messageData.substring(0, 1);
                    if (0 <= oper_dang.indexOf("a")) {
                        oper_dang = "";
                    }
                    // ????????????1???32???	2	???
                    String oper_wendu_now = messageData.substring(1, 3);
                    oper_wendu_now = 0 <= oper_wendu_now.indexOf("a") ? "" : new BigDecimal(oper_wendu_now).toString();

                    // ???????????????:1.????????????2.????????????3.????????????4.??????:??????5.??????:??????	1	???
                    String oper_open_close = messageData.substring(3, 4);
                    /**
                     * ???????????????:1.????????????2.????????????3.?????? 4.????????????9.?????????
                     * 6.?????????7.?????????
                     */
                    switch (oper_open_close) {
                        case "1":
                            tvYuShe_WenDu.setText("?????????????????????" + oper_dang + "???");
                            PreferenceHelper.getInstance(mContext).putString(STARTSHELVES, "1");
                            openMode(Integer.parseInt(oper_dang));
                            switchModel(rbHeaterGearMode, 1);
                            button = rbHeaterGearMode;

                            if (oper_dang != null) {
                                openMode(Integer.parseInt(oper_dang));
                            }

                            break;
                        case "2":
                            showLoadSuccess();
                            if (oper_wendu_now != null) {
                                tvYuShe_WenDu.setText("???????????????????????????" + oper_wendu_now + "???");
                            }

                            PreferenceHelper.getInstance(mContext).putString(STARTSHELVES, "2");
                            if (null != oper_wendu_now) {
                                openKongTiaoMode(Integer.parseInt(oper_wendu_now));
                            } else {
                                openMode(0);
                            }

                            switchModel(rbHeaterAirMode, 2);
                            button = rbHeaterAirMode;
                            break;
                        case "3":
                            showLoadSuccess();
                            closeMode(0);
                            //  AlertUtil.t(WindHeaterActivity.this, "?????????????????????");
                            tvYuShe_WenDu.setText("????????????????????????");
                            break;
                        case "4"://????????????
                            //showLoadSuccess();
//
//                            if (version != null) {
//                                if (version.equals("2019")) {
//                                    flag = false;
//
//                                }
//                            }
//
//                            if (flag) {
//                                tvYuShe_WenDu.setText("?????????????????????" + oper_dang + "???");
//                                PreferenceHelper.getInstance(mContext).putString(STARTSHELVES, "4");
//                                button = rbHeaterPumpMode;
//
//                                switchModel(rbHeaterPumpMode, 4);
//                                //????????????
//                                if (oper_dang != null) {
//                                    openMode(Integer.parseInt(oper_dang));
//                                }
//                            } else {
//                                UIHelper.ToastMessage(WindHeaterActivity.this, "?????????????????????", Toast.LENGTH_SHORT);
//                            }


                            break;
                        case "6":
                            showLoadSuccess();
                            tvYuShe_WenDu.setText("?????????????????????" + oper_dang + "???");
                            PreferenceHelper.getInstance(mContext).putString(STARTSHELVES, "6");
                            button = rbHeaterYbyMode;
                            switchModel(rbHeaterYbyMode, 6);
                            //????????????
                            if (oper_dang != null) {
                                openMode(Integer.parseInt(oper_dang));
                            }

                            break;
                        case "7":
                            showLoadSuccess();
                            tvYuShe_WenDu.setText("?????????????????????" + oper_dang + "???");
                            button = rbHeaterYtfMode;
                            PreferenceHelper.getInstance(mContext).putString(STARTSHELVES, "7");

                            switchModel(rbHeaterYtfMode, 7);
                            //????????????
                            if (oper_dang != null) {
                                openMode(Integer.parseInt(oper_dang));
                            }

                            break;
                        case "9":
                            mTvWd.setText("00");
                            // showLoadSuccess();
                            //  AlertUtil.t(WindHeaterActivity.this, "????????????????????????");
                            break;
                    }
                    // ???????????????:????????????	3	???
                    String oper_wendu = messageData.substring(4, 6);
                    oper_wendu += "0".equals(messageData.substring(6, 7)) ? "" : "." + messageData.substring(6, 7);

                    // ???????????????:???????????? ??????:-03	3	???
                    String xinhaoQiangDu = messageData.substring(7, 9);
                    String jiGeXinHao = "??????????????????";
                    if (!StringUtils.isEmpty(xinhaoQiangDu)) {
                        int xinhao = Integer.valueOf(xinhaoQiangDu);

                        if (xinhao < 15) {
                            jiGeXinHao = "?????????";
                        } else if (xinhao >= 15 && xinhao < -19) {
                            jiGeXinHao = "????????????";
                        } else if (xinhao >= 20 && xinhao <= 25) {
                            jiGeXinHao = "????????????";
                        } else if (xinhao >= 26 && xinhao <= 30) {
                            jiGeXinHao = "????????????";
                        } else if (xinhao >= 30 && xinhao <= 35) {
                            jiGeXinHao = "????????????";
                        }

                        //UIHelper.ToastMessage(mContext, jiGeXinHao);
                    }


                    // ???????????????:??????->0253 = 25.3	4	???
                    String machine_voltage = messageData.substring(10, 13) + "." + messageData.substring(13, 14);
                    // ???????????????:????????????->13245	5	???
                    String revolution = messageData.substring(14, 19);
                    // ???????????????:???????????????->0264=26.4	4	???
                    String power = messageData.substring(19, 23);
                    // ???????????????:????????????->0265=26.5	4	???
                    String frequency = messageData.substring(23, 27);
                    frequency = frequency.substring(0, 3) + "." + frequency.substring(3);
                    // ???????????????:???????????????->??????:-026	4	???
                    String in_temperature = messageData.substring(27, 31);
                    // ???????????????:???????????????->0128	4	???
                    String out_temperature = messageData.substring(31, 35);

                    String wendu = out_temperature.substring(2, 4);
                    mTvWd.setText(wendu);

                    // ????????????????????????->01???18	2	 ???????????????
                    String zhu_car_stoppage_no = messageData.substring(35, 37);
                    zhu_car_stoppage_no = 0 <= zhu_car_stoppage_no.indexOf("a") ? "" : String.valueOf(Integer.parseInt(zhu_car_stoppage_no));

                    if (!StringUtils.isEmpty(zhu_car_stoppage_no)) {
                        Activity currentActivity = AppManager.getAppManager().currentActivity();
                        if (currentActivity != null) {
                            if (!currentActivity.getClass().getSimpleName().equals(JiareqiGuzhangActivity.class.getSimpleName())) {

                                if (!myCarCaoZuoDialog_notify.isShowing()) {
                                    myCarCaoZuoDialog_notify.show();
                                }

                            }
                        }

                    }
                    if (messageData.length() >= 38) {
                        // ????????????   0 ???  1 ??? a ?????????
                        String shuiBengZhuangTai = messageData.substring(37, 38);

                        switch (shuiBengZhuangTai) {
                            case "0"://0???
                                rbHeaterPumpMode.setChecked(false);
                                rbHeaterPumpMode.setVisibility(View.VISIBLE);
                                break;
                            case "1"://1???
                                rbHeaterPumpMode.setChecked(true);
                                rbHeaterPumpMode.setVisibility(View.VISIBLE);
                                break;
                            case "a"://???
                                flag = false;
                                rbHeaterPumpMode.setVisibility(View.GONE);
                                break;
                        }
                    }


                    String worktime = "1";

                    if (messageData.length() >= 44) {
                        worktime = messageData.substring(38, 44);        // ???????????????:????????????(??????)
                    }

                    if (messageData.length() >= 46) {
                        // ????????????????????????->01???18	2	???????????????
                        String zhu_car_stoppage_no_o = messageData.substring(44, 46);
                        zhu_car_stoppage_no_o = 0 <= zhu_car_stoppage_no_o.indexOf("a") ? "" : String.valueOf(Integer.parseInt(zhu_car_stoppage_no_o));


                    }


                } else if (message.type == ConstanceValue.MSG_CAR_K) {
                    showLoadSuccess();
                } else if (message.type == ConstanceValue.MSG_CAR_I) {
                    showLoadSuccess();
                    //???????????????
                    Log.i("msg_car_i", message.content.toString());
                    String message1 = message.content.toString();
                    version = message1.substring(message1.indexOf("i") + 7, message1.indexOf("i") + 11);


                    /**
                     *     int MSG_MQTT_CONNECTCOMPLETE = 0x10045;//????????????
                     *     int MSG_MQTT_CONNECTLOST = 0x10046;//????????????
                     *     int MSG_MQTT_CONNECTARRIVE = 0x10047;//????????????
                     *     int MSG_MQTT_CONNECT_CHONGLIAN_ONSUCCESS = 0x10048;//????????????
                     *     int MSG_MQTT_CONNECT_CHONGLIAN_ONFAILE = 0x10049;//????????????
                     */
                } else if (message.type == MSG_MQTT_CONNECTCOMPLETE) {
                    showLoadSuccess();
                    if (waitDialog.isShowing()) {
                        waitDialog.dismiss();
                        //  setMqttZhiLing();
                    }
                    Log.i("rair", "complete");

                } else if (message.type == MSG_MQTT_CONNECTLOST) {
                    showLoadSuccess();
                    if (!waitDialog.isShowing()) {
                        waitDialog.show();
                    }
                    Log.i("rair", "complete_lost");
                } else if (message.type == MSG_MQTT_CONNECTARRIVE) {
                    showLoadSuccess();
                    if (waitDialog.isShowing()) {
                        waitDialog.dismiss();
                    }
                    Log.i("rair", "arrive");
                } else if (message.type == MSG_MQTT_CONNECT_CHONGLIAN_ONSUCCESS) {
                    showLoadSuccess();
                    if (waitDialog.isShowing()) {
                        waitDialog.dismiss();
                    }

                    Log.i("rair", "chonglian_success");

                } else if (message.type == MSG_MQTT_CONNECT_CHONGLIAN_ONFAILE) {
                    showLoadSuccess();
                    if (!waitDialog.isShowing()) {
                        waitDialog.show();
                    }
                    Log.i("rair", "chonglian_failer");
                } else if (message.type == ConstanceValue.MSG_K6111) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lordingDialog.dismiss();
                            SoundPoolUtils.soundPool(mContext, R.raw.yikaiji);
                        }
                    }, ConfigValue.YANCHI);//3????????????Runnable??????run??????

                } else if (message.type == ConstanceValue.MSG_K6131) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SoundPoolUtils.soundPool(mContext, R.raw.yiguanji);
                            lordingDialog.dismiss();
                        }
                    }, ConfigValue.YANCHI);//3????????????Runnable??????run??????

                } else if (message.type == ConstanceValue.MSG_K6121) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SoundPoolUtils.soundPool(mContext, R.raw.yikaiji);
                            lordingDialog.dismiss();
                        }
                    }, ConfigValue.YANCHI);//3????????????Runnable??????run??????
                } else if (message.type == ConstanceValue.MSG_K6141) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SoundPoolUtils.soundPool(mContext, R.raw.yikaiji);
                            lordingDialog.dismiss();
                        }
                    }, ConfigValue.YANCHI);//3????????????Runnable??????run??????
                } else if (message.type == ConstanceValue.MSG_K6161) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SoundPoolUtils.soundPool(mContext, R.raw.yikaiji);
                            lordingDialog.dismiss();
                        }
                    }, ConfigValue.YANCHI);//3????????????Runnable??????run??????
                } else if (message.type == ConstanceValue.MSG_K6171) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SoundPoolUtils.soundPool(mContext, R.raw.yikaiji);
                            lordingDialog.dismiss();
                        }
                    }, ConfigValue.YANCHI);//3????????????Runnable??????run??????
                } else if (message.type == ConstanceValue.MSG_ZHILINGMA) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lordingDialog.dismiss();
                        }
                    }, ConfigValue.YANCHI);//3????????????Runnable??????run??????

                }
            }
        }));
        Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji).into(ivKaiji);

        ivKaiji.setVisibility(View.GONE);
        ivGuanji.setVisibility(View.VISIBLE);


        myCarCaoZuoDialog_notify = new MyCarCaoZuoDialog_Notify(getAppContext(), new MyCarCaoZuoDialog_Notify.OnDialogItemClickListener() {
            @Override
            public void clickLeft() {
                // player.stop();

            }

            @Override
            public void clickRight() {
                JiareqiGuzhangActivity.actionStart(mContext);
                //SoundPoolUtils.soundPool.release();
                myCarCaoZuoDialog_notify.dismiss();

            }
        }
        );

        myCarCaoZuoDialog_notify.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG);
    }

    @Override
    public void initImmersion() {
        mImmersionBar.with(this).titleBar(toolbar).init();
    }

    public void initView() {
        btns = new ArrayList<>();
        btns.add(rbHeaterAirMode);
        btns.add(rbHeaterGearMode);
        btns.add(rbHeaterPumpMode);
        btns.add(rbHeaterYbyMode);
        btns.add(rbHeaterYtfMode);
        for (int i = 0; i < btns.size(); i++) {
            btns.get(i).setOnLongClickListener(this);
        }

    }

    private int gearToValue(int gear) {

        switch (gear) {
            case 1:
                if (progressValue > 20 || progressValue < 5)
                    progressValue = 10;
                break;
            case 2:
                if (progressValue < 20 || progressValue > 40)
                    progressValue = 30;
                break;
            case 3:
                if (progressValue < 40 || progressValue > 60)
                    progressValue = 50;
                break;
            case 4:
                if (progressValue < 60 || progressValue > 80)
                    progressValue = 70;
                break;
            case 5:
                if (progressValue < 80)
                    progressValue = 100;
                break;
            case 0:
                progressValue = 0;
                break;
        }
        return progressValue;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceHelper.getInstance(mContext).removeKey(App.CHOOSE_KONGZHI_XIANGMU);
        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(CAR_NOTIFY), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(MainActivity.java:93)-onSuccess:-&gt;??????????????????");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:98)-onFailure:-&gt;??????????????????");
            }
        });

        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(CARBOX_GETNOW), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(MainActivity.java:93)-onSuccess:-&gt;??????????????????");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:98)-onFailure:-&gt;??????????????????");
            }
        });

        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(CAR_CTROL), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(MainActivity.java:93)-onSuccess:-&gt;??????????????????");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:98)-onFailure:-&gt;??????????????????");
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

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param button ???????????????????????????
     * @param model  ?????????????????????????????????
     **/
    public void heaterSwitch(RadioButton button, int model) {
        if (button != null && button.isChecked()) {

            if (model == 4) {

                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("M712.")
                        .setQos(2).setRetained(false)
                        .setTopic(CAR_CTROL), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;????????????");
                        //UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????,?????????????????????", Toast.LENGTH_SHORT);
                        PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k71" + "2" + "1.");
                        lordingDialog.setTextMsg("????????????????????????????????????");
                        lordingDialog.show();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
                        UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????", Toast.LENGTH_SHORT);
                    }
                });

                return;
            }

            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M613.")
                    .setQos(2).setRetained(false)
                    .setTopic(CAR_CTROL), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;????????????");
                    //UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????,?????????????????????", Toast.LENGTH_SHORT);


                    switch (model) {
                        //????????????
                        case 1:
                        case 7:
                        case 6:
                        case 2:
                            SoundPoolUtils.soundPool(mContext, R.raw.guanjizhong);
                            lordingDialog.setTextMsg("????????????????????????");
                            lordingDialog.show();
                            break;
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
                    UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????", Toast.LENGTH_SHORT);
                }
            });
        } else {
            if (!arcProgressBar.getIsOpen()) {

                if (model == 4) {

                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("M711.")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_CTROL), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;????????????");
                            //UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????,?????????????????????", Toast.LENGTH_SHORT);
                            // SoundPoolUtils.soundPool(mContext, R.raw.guanjizhong);
                            PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k71" + "1" + "1.");
                            lordingDialog.setTextMsg("????????????????????????????????????");
                            lordingDialog.show();
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
                            UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????", Toast.LENGTH_SHORT);
                        }
                    });
                    return;
                }

                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("M61" + model + ".")
                        .setQos(2).setRetained(false)
                        .setTopic(CAR_CTROL), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;????????????");

                        switch (model) {
                            //????????????
                            case 1:
                                SoundPoolUtils.soundPool(mContext, R.raw.dangwei);
                                lordingDialog.setTextMsg("???????????????????????????????????????");
                                lordingDialog.show();
                                break;
                            case 2:
                                SoundPoolUtils.soundPool(mContext, R.raw.kongtiao);
                                lordingDialog.setTextMsg("???????????????????????????????????????");
                                lordingDialog.show();
                                break;
                            case 4:
                                SoundPoolUtils.soundPool(mContext, R.raw.shuibeng);
                                lordingDialog.setTextMsg("???????????????????????????????????????");
                                lordingDialog.show();
                                break;
                            case 6:
                                SoundPoolUtils.soundPool(mContext, R.raw.yubengyou);
                                lordingDialog.setTextMsg("??????????????????????????????????????????");
                                lordingDialog.show();
                                break;
                            case 7:
                                SoundPoolUtils.soundPool(mContext, R.raw.yutongfeng);
                                lordingDialog.setTextMsg("??????????????????????????????????????????");
                                lordingDialog.show();
                                break;
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
                        UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????", Toast.LENGTH_SHORT);
                    }
                });

            } else {
                //AlertUtil.t(WindHeaterActivity.this, "??????????????????????????????");
                lordingDialog.setTextMsg("???????????????????????????,????????????????????????????????????");
                lordingDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lordingDialog.dismiss();
                    }
                }, 2500);//3????????????Runnable??????run??????
            }
        }
    }

    /**
     * ????????????????????????RadioButton?????????
     *
     * @param button   ?????????????????????RadioButton
     * @param openMode ???????????????????????????
     **/
    public void switchModel(RadioButton button, int openMode) {
        rgMagnet.clearCheck();
        if (button != null) {
            rgMagnet.check(button.getId());
        }
        this.openMode = openMode;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ConstantUtil.MSG_HEATER_ROGRESS_VALUE_CHANGE:
                        handleProgressMsg(msg);
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    Integer myValue;

    private void handleProgressMsg(Message msg) {
        Bundle b = msg.getData();
        progressValue = b.getInt("progress_value");
        //????????????????????????????????????????????????

        //?????? ?????? ?????? ?????????  ?????????
        /**
         * ???????????????:1.????????????2.????????????3.?????? 4.????????????9.?????????
         * 6.?????????7.?????????
         */

        String dangWeiValue = PreferenceHelper.getInstance(mContext).getString(STARTSHELVES, "1");
        if (dangWeiValue.equals("1")) {//????????????
            Log.i("MyProgressValue", String.valueOf(progressValue));
            dangWeiMode(progressValue);
        } else if (dangWeiValue.equals("2")) {//????????????
            Log.i("MyProgressValue", String.valueOf(progressValue));
            myValue = 14 + progressValue;
            Log.i("myValue", String.valueOf(myValue));
            if (myValue > 0 && myValue <= 9) {
            } else if (myValue > 9 && myValue < 20) {
                String value = myValue.toString().substring(1, 2);
                Log.i("myValue", value);
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("M66" + value + ".")
                        .setQos(2)
                        .setTopic(CAR_CTROL).setRetained(false), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;????????????");
                        PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k66" + value + "1.");
                        lordingDialog.setTextMsg("???????????????,?????????");
                        lordingDialog.show();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????" + exception.getMessage());
                    }
                });

            } else if (myValue > 20 && myValue < 30) {
                String value = myValue.toString().substring(1, 2);
                Log.i("myValue", value);
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("M67" + value + ".")
                        .setQos(2)
                        .setTopic(CAR_CTROL).setRetained(false), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;????????????");
                        PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k67" + value + "1.");
                        lordingDialog.setTextMsg("???????????????,?????????");
                        lordingDialog.show();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????" + exception.getMessage());
                    }
                });
            } else if (myValue > 20 && myValue <= 32) {
                String value = myValue.toString().substring(1, 2);
                Log.i("myValue", value);
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("M68" + value + ".")
                        .setQos(2)
                        .setTopic(CAR_CTROL).setRetained(false), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;????????????");
                        PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k68" + value + "1.");
                        lordingDialog.setTextMsg("???????????????,?????????");
                        lordingDialog.show();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????" + exception.getMessage());
                    }
                });
            }
        } else if (dangWeiValue.equals("3")) {//??????
        } else if (dangWeiValue.equals("4")) {//????????????
            dangWeiMode(progressValue);
        } else if (dangWeiValue.equals("6")) {//?????????
            dangWeiMode(progressValue);
        } else if (dangWeiValue.equals("7")) {//?????????
            dangWeiMode(progressValue);
        }
    }

    private void dangWeiMode(int progressValue) {
        progressValue = progressValue + 14;
        if (progressValue >= 0 && progressValue <= 18) {
            gear = 1;
        } else if (progressValue > 18 && progressValue <= 21) {
            gear = 2;
        } else if (progressValue > 21 && progressValue <= 25) {
            gear = 3;
        } else if (progressValue > 25 && progressValue <= 28) {
            gear = 4;
        } else if (progressValue > 28 && progressValue <= 32) {
            gear = 5;
        } else {
            gear = 3;
        }
        //    HeaterMqttService.mqttService.publish("M62" + gear + ".", HeaterMqttService.TOPIC_SERVER_ORDER, 2, false);
        //String TOPIC_SERVER_ORDER = "wit/cbox/hardware/" + car_server_id + ccid;
        //???????????? ??????????????????????????????????????? ???????????????

        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("M62" + gear + ".")
                .setQos(2)
                .setTopic(CAR_CTROL).setRetained(false), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;????????????");
                lordingDialog.setTextMsg("???????????????,?????????...");
                lordingDialog.show();
                PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k62" + gear + "1.");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????:" + exception.getMessage(), Toast.LENGTH_SHORT);

            }
        });


        int dangValue = 3;

        if (gear == 1) {
            dangValue = 4;

        } else if (gear == 2) {

            dangValue = 7;
        } else if (gear == 3) {

            dangValue = 11;
        } else if (gear == 4) {
            dangValue = 14;

        } else if (gear == 5) {
            dangValue = 18;
        }


        valueAnimator = ValueAnimator.ofInt(arcProgressBar.getCurrentProgerss(), dangValue);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                arcProgressBar.setCurrentProgress((int) animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }

    String value = "";

    @Override
    public boolean onLongClick(View view) {
        if (!AndMqtt.getInstance().isConneect()) {
            UIHelper.ToastMessage(mContext, "???????????????,??????????????????????????????");
            return true;
        }
        switch (view.getId()) {
            case R.id.btn_heater_close:
//                Log.d("isSub", "isSub==" + isSub);
                if (arcProgressBar.getIsOpen()) {
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("M613.")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_CTROL), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;????????????");
                            //  UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????,?????????????????????", Toast.LENGTH_SHORT);
                            lordingDialog.setTextMsg("?????????????????????...");
                            lordingDialog.show();

                            SoundPoolUtils.soundPool(mContext, R.raw.guanjizhong);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
                            UIHelper.ToastMessage(WindHeaterActivity.this, "????????????????????????????????????????????????", Toast.LENGTH_SHORT);
                        }
                    });
                } else {
                    //??????
                    //?????????????????????????????????
                    //HeaterMqttService.mqttService.publish("M61" + openMode + PreferenceHelper.getInstance(WindHeaterActivity.this).getString("atmos", "") + ".", HeaterMqttService.TOPIC_SERVER_ORDER, 2, false);
                    //??????
                    //  HeaterMqttService.mqttService.publish("M61", CAR_CTROL, 2, false);
                    Log.i("Rair", "?????????" + CAR_CTROL + "  " + "M61" + PreferenceHelper.getInstance(this).getString(STARTSHELVES, "1") + ".");

                    String str = PreferenceHelper.getInstance(this).getString(STARTSHELVES, "1");

                    //   1.?????????????????? 2.?????????????????? 3.??????
                    //   4.?????????????????? 6.?????????7.?????????


                    switch (str) {
                        case "1":
                            value = "????????????";
                            break;
                        case "2":
                            value = "????????????";
                            break;
                        case "4":
                            value = "????????????";
                            break;
                        case "6":
                            value = "???????????????";
                            break;
                        case "7":
                            value = "???????????????";
                            break;

                    }

                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("M61" + PreferenceHelper.getInstance(this).getString(STARTSHELVES, "1") + ".")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_CTROL), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;????????????");
                            //UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????,?????????????????????", Toast.LENGTH_SHORT);
                            lordingDialog.setTextMsg("?????????" + value + "???????????????...");
                            lordingDialog.show();
                            SoundPoolUtils.soundPool(mContext, R.raw.kaijizhong);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
                            UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????", Toast.LENGTH_SHORT);
                        }
                    });

                }
                // heaterSwitch(button, openMode);
                break;
            case R.id.rb_heater_air_mode: //????????????
                heaterSwitch(rbHeaterAirMode, 2);
                break;
            case R.id.rb_heater_gear_mode://????????????
                heaterSwitch(rbHeaterGearMode, 1);
                break;
            case R.id.rb_heater_pump_mode://????????????
                //heaterSwitch(rbHeaterPumpMode, 4);
                setShuiBeng(rbHeaterPumpMode, 4);
                break;
            case R.id.rb_heater_yby_mode://?????????
                heaterSwitch(rbHeaterYbyMode, 6);
                break;
            case R.id.rb_heater_ytf_mode://?????????
                heaterSwitch(rbHeaterYtfMode, 7);
                break;
        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.heater_menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings:
                if (!AndMqtt.getInstance().isConneect()) {
                    UIHelper.ToastMessage(mContext, "???????????????,???????????????????????????????????????????????????");
                    break;
                }
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawers();
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_bluetooth:
                //??????????????????
                UIHelper.ToastMessage(this, "????????????");
                break;
            case R.id.nav_trajectory:
                //????????????
                startActivity(new Intent(this, HistoryLocusActivity.class));
                break;
            case R.id.nav_location:
                //??????
             //   startActivity(new Intent(this, LocationActivity.class));
                break;
            case R.id.nav_timing:
                //??????
                //startActivity(new Intent(this, AppointmentActivity.class));
                AppointmentActivity.actionStart(mContext, ccid);
                break;
            case R.id.nav_record:
                //????????????
                startActivity(new Intent(this, RepairOrderAcitivity.class));
                break;
            case R.id.nav_state:
                //???????????????
                startActivity(new Intent(this, DeviceStateActivity.class));
                break;
            case R.id.nav_report:
                //????????????
                // startActivity(new Intent(this, DiagnosisActivity.class));
                JiareqiGuzhangActivity.actionStart(this);
                break;
            case R.id.nav_corral:
                //????????????
                startActivity(new Intent(this, WebViewActivity.class).
                        putExtra("url", PreferenceHelper.getInstance(this).getString("fence_url", "")).putExtra("title", "????????????"));
                break;

            case R.id.fengnuan_jiebang:
                break;
            case R.id.nav_setting:
                startActivity(new Intent(this, HeaterSettingActivity.class));
                //??????
                break;
        }


        return false;
    }

    public void requestData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03107");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("user_car_id", PreferenceHelper.getInstance(this).getString("car_id", ""));
        Gson gson = new Gson();
        OkGo.<AppResponse<CarDetails.DataBean>>post(Urls.SERVER_URL + "wit/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<CarDetails.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<CarDetails.DataBean>> response) {
                        PreferenceHelper.getInstance(WindHeaterActivity.this).putString("fence_url", response.body().data.get(0).getWeilan_url());
                        Glide.with(WindHeaterActivity.this).load(response.body().data.get(0).getCar_brand_url_one()).into(ivBrandPic);
                        tvBrandName.setText(response.body().data.get(0).getCar_brand_name_one());
                        tvCarNumber.setText(response.body().data.get(0).getPlate_number());

                    }

                    @Override
                    public void onError(Response<AppResponse<CarDetails.DataBean>> response) {
                        AlertUtil.t(WindHeaterActivity.this, response.getException().getMessage());
                    }
                });


    }

    public void openKongTiaoMode(int value) {
        arcProgressBar.setOpen(true);

        //??????gif??? -- ?????????

        ivKaiji.setVisibility(View.VISIBLE);
        ivGuanji.setVisibility(View.GONE);
        //    btnHeaterClose.setBackground(getResources().getDrawable(R.drawable.bg_heater_close_btn_on));
        btnHeaterClose.setSelected(true);

        if (value > 32) {
            value = 32;
        }

        if (value > 15) {
            value = value - 14;
        }
        Log.i("arcProgressBar", value + "");
        valueAnimator = ValueAnimator.ofInt(arcProgressBar.getCurrentProgerss(), value);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                arcProgressBar.setCurrentProgress((int) animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.start();


    }

    public void openMode(int value) {
        arcProgressBar.setOpen(true);

        //??????gif  -- ????????????


        ivKaiji.setVisibility(View.VISIBLE);
        ivGuanji.setVisibility(View.GONE);

        //    btnHeaterClose.setBackground(getResources().getDrawable(R.drawable.bg_heater_close_btn_on));
        btnHeaterClose.setSelected(true);


        int dangValue = 3;

        if (value == 1) {
            dangValue = 4;

        } else if (value == 2) {

            dangValue = 7;
        } else if (value == 3) {

            dangValue = 11;
        } else if (value == 4) {
            dangValue = 14;

        } else if (value == 5) {
            dangValue = 18;
        }


        valueAnimator = ValueAnimator.ofInt(arcProgressBar.getCurrentProgerss(), dangValue);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                arcProgressBar.setCurrentProgress((int) animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.start();


    }

    public void closeMode(int value) {
        arcProgressBar.setOpen(false);

        // ???????????? ??????gif


        ivKaiji.setVisibility(View.GONE);
        ivGuanji.setVisibility(View.VISIBLE);
        //   btnHeaterClose.setBackground(getResources().getDrawable(R.drawable.bg_heater_close_btn_off));
        btnHeaterClose.setSelected(false);
        valueAnimator = ValueAnimator.ofInt(arcProgressBar.getCurrentProgerss(), value);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                arcProgressBar.setCurrentProgress((int) animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.start();
        switchModel(null, openMode);
    }

    @OnClick({R.id.rb_heater_air_mode, R.id.rb_heater_gear_mode, R.id.rb_heater_pump_mode, R.id.rb_heater_yby_mode, R.id.rb_heater_ytf_mode})
    public void onViewClicked(View view) {
        rgMagnet.clearCheck();
        if (button != null && arcProgressBar.getIsOpen())
            rgMagnet.check(button.getId());

    }

    public void setMqttZhiLing() {

        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(CAR_NOTIFY)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "notify:  " + CAR_NOTIFY + "CAR_NOTIFY ???????????????????????????");


                MyApplication.mqttDingyue.add(CAR_NOTIFY);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("CAR_NOTIFY", "(MainActivity.java:68)-onFailure:-&gt;????????????");
            }
        });


        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("k001.")
                .setQos(2).setRetained(false)
                .setTopic(CAR_NOTIFY), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(CAR_NOTIFY.java:79)-onSuccess:-&gt;????????????" + "k001 ???????????????????????????");

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
            }
        });

        //??????????????????????????????????????????
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(CARBOX_GETNOW)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "???????????? carbox_getnow:  " + CARBOX_GETNOW + " CARBOX_GETNOW ???????????????????????????");
                MyApplication.mqttDingyue.add(CARBOX_GETNOW);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:68)-onFailure:-&gt;????????????");
            }
        });


        //????????????
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(CAR_CTROL)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", " Rair??????????????????" + "wit/app/" + of_user_id + " CAR_CTROL ???????????????????????????");
                MyApplication.mqttDingyue.add(CAR_CTROL);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:68)-onFailure:-&gt;????????????");
            }


        });


        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("N9.")
                .setQos(2)
                .setTopic(CAR_CTROL)
                .setRetained(false), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;????????????" + " N9 ???????????????????????????");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
            }
        });

        //????????????????????????
        // requestData();

        // HeaterMqttService.mqttService.publish("M512.", HeaterMqttService.TOPIC_SERVER_ORDER, 2, false);
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("M512.")
                .setQos(2).setRetained(false)
                .setTopic(CAR_CTROL), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(CAR_NOTIFY.java:79)-onSuccess:-&gt;????????????" + "M512 ???????????????????????????");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
            }
        });
    }

    public void playMusic(int res) {
        boolean flag = false;

        Activity currentActivity = AppManager.getAppManager().currentActivity();
        if (currentActivity != null) {
            if (!currentActivity.getClass().getSimpleName().equals(JiareqiGuzhangActivity.class.getSimpleName())) {
                MyCarCaoZuoDialog_Notify myCarCaoZuoDialog_notify = new MyCarCaoZuoDialog_Notify(getAppContext(), new MyCarCaoZuoDialog_Notify.OnDialogItemClickListener() {
                    @Override
                    public void clickLeft() {
                        // player.stop();
                        if (SoundPoolUtils.soundPool != null) {
                            SoundPoolUtils.soundPool.release();
                        }

                    }

                    @Override
                    public void clickRight() {
                        JiareqiGuzhangActivity.actionStart(mContext);
                        //SoundPoolUtils.soundPool.release();
                        if (SoundPoolUtils.soundPool != null) {
                            SoundPoolUtils.soundPool.release();
                        }

                    }
                }
                );

                myCarCaoZuoDialog_notify.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG);
                myCarCaoZuoDialog_notify.show();
                myCarCaoZuoDialog_notify.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (SoundPoolUtils.soundPool != null) {
                            SoundPoolUtils.soundPool.release();
                        }
                    }
                });

            } else {
                flag = true;
            }
        }

        if (flag) {
            return;
        }
        SoundPoolUtils.soundPool(mContext, res);

    }


    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param button ???????????????????????????
     * @param model  ?????????????????????????????????
     **/
    public void setShuiBeng(RadioButton button, int model) {
        if (button != null && button.isChecked()) {

            if (model == 4) {
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("M712.")
                        .setQos(2).setRetained(false)
                        .setTopic(CAR_CTROL), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;????????????");
                        //UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????,?????????????????????", Toast.LENGTH_SHORT);
                        PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k71" + "2" + "1.");
                        lordingDialog.setTextMsg("????????????????????????????????????");
                        lordingDialog.show();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
                        UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????", Toast.LENGTH_SHORT);
                    }
                });

                return;
            }
        } else {
            if (model == 4) {
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("M711.")
                        .setQos(2).setRetained(false)
                        .setTopic(CAR_CTROL), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;????????????");
                        //UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????,?????????????????????", Toast.LENGTH_SHORT);
                        PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k71" + "1" + "1.");
                        lordingDialog.setTextMsg("????????????????????????????????????");
                        lordingDialog.show();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
                        UIHelper.ToastMessage(WindHeaterActivity.this, "??????????????????", Toast.LENGTH_SHORT);
                    }
                });
                return;
            }

        }
    }

}
