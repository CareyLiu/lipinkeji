package com.lipinkeji.cn.activity.device_fengnuan.set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;
import com.lipinkeji.cn.util.Y;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.lipinkeji.cn.config.MyApplication.CARBOX_GETNOW;
import static com.lipinkeji.cn.config.MyApplication.CAR_CTROL;
import static com.lipinkeji.cn.config.MyApplication.CAR_NOTIFY;

public class FengnuanJingxiaoshangActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.seekbar_dianhuosai_12v)
    SeekBar seekbar_dianhuosai_12v;
    @BindView(R.id.tv_dianhuosai_12v)
    TextView tv_dianhuosai_12v;
    @BindView(R.id.seekbar_dianhuosai_24v)
    SeekBar seekbar_dianhuosai_24v;
    @BindView(R.id.tv_dianhuosai_24v)
    TextView tv_dianhuosai_24v;
    @BindView(R.id.tv_dianhuosai_jingci)
    TextView tv_dianhuosai_jingci;
    @BindView(R.id.tv_dianhuosai_limai)
    TextView tv_dianhuosai_limai;
    @BindView(R.id.tv_dianhuosai_gaide)
    TextView tv_dianhuosai_gaide;
    @BindView(R.id.tv_dianhuosai_tuobo)
    TextView tv_dianhuosai_tuobo;
    @BindView(R.id.seekbar_youbengguige)
    SeekBar seekbar_youbengguige;
    @BindView(R.id.tv_youbengguige)
    TextView tv_youbengguige;
    @BindView(R.id.seekbar_guoyabaohu_12v)
    SeekBar seekbar_guoyabaohu_12v;//高温降油阀值
    @BindView(R.id.tv_guoyabaohu_12v)
    TextView tv_guoyabaohu_12v;
    @BindView(R.id.seekbar_qianyabaohu_12v)
    SeekBar seekbar_qianyabaohu_12v;
    @BindView(R.id.tv_qianyabaohu_12v)
    TextView tv_qianyabaohu_12v;

    @BindView(R.id.bt_save)
    TextView bt_save;
    @BindView(R.id.tv_shuangcitie)
    TextView tvShuangcitie;
    @BindView(R.id.tv_dancitie)
    TextView tvDancitie;
    @BindView(R.id.bt_huifuchuchang)
    TextView btHuifuchuchang;
    @BindView(R.id.tv_dayou_guanbi)
    TextView tvDayouGuanbi;
    @BindView(R.id.tv_dayou_kaiqi)
    TextView tvDayouKaiqi;
    @BindView(R.id.tv_dianhuosaineizu_guanbi)
    TextView tvDianhuosaineizuGuanbi;
    @BindView(R.id.tv_dianhuosaineizu_kaiqi)
    TextView tvDianhuosaineizuKaiqi;
    @BindView(R.id.tv_jidian_12)
    TextView tvJidian12;
    @BindView(R.id.tv_jidian_24)
    TextView tvJidian24;
    @BindView(R.id.tv_jidian_zidong)
    TextView tvJidianZidong;
    @BindView(R.id.tv_chuanganqi_0)
    TextView tvChuanganqi0;
    @BindView(R.id.tv_chuanganqi_1)
    TextView tvChuanganqi1;
    @BindView(R.id.tv_chuanganqi_2)
    TextView tvChuanganqi2;
    @BindView(R.id.tv_jiqigonglv_2kw)
    TextView tvJiqigonglv2kw;
    @BindView(R.id.tv_jiqigonglv_5kw)
    TextView tvJiqigonglv5kw;
    @BindView(R.id.tv_jiqigonglv_zidingyi)
    TextView tvJiqigonglvZidingyi;
    @BindView(R.id.tv_wucitie)
    TextView tvWucitie;

    private int dianhuosai12V;
    private int dianhuosai24V;
    private int jiaresai;
    private int youbengguige;
    private int gaowenjiangyou;
    private int gaowenbaojing;
    private int guoya24V;
    private int qianya24V;

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.a_fengnuan_act_set_jingxiaoshang;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, FengnuanJingxiaoshangActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        showProgressDialog("数据加载中，请稍后......");
        initHuidiao();
        registerKtMqtt();
    }

    private void initData() {
        initView();


        dianhuosai12V = 60;
        dianhuosai24V = 60;
        jiaresai = 1;
        youbengguige = 12;
        gaowenjiangyou = 12;
        gaowenbaojing = 7;
        guoya24V = 24;
        qianya24V = 7;

        setView();
    }

    private void initView() {
        seekbar_dianhuosai_12v.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dianhuosai12V = progress;
                tv_dianhuosai_12v.setText(dianhuosai12V + "w");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekbar_dianhuosai_24v.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dianhuosai24V = progress;
                tv_dianhuosai_24v.setText(dianhuosai24V + "w");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekbar_youbengguige.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                youbengguige = progress;
                tv_youbengguige.setText(youbengguige + "w");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekbar_guoyabaohu_12v.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gaowenjiangyou = progress;
                tv_guoyabaohu_12v.setText(gaowenjiangyou + "w");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekbar_qianyabaohu_12v.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gaowenbaojing = progress;
                tv_qianyabaohu_12v.setText(gaowenbaojing + "w");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


//        seekbar_guoyabaohu_24v.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                guoya24V = progress;
//                tv_guoyabaohu_24v.setText(guoya24V + "w");
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
    }

    private String firstEnter = "0";

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_FENGNUAN_ZHUJI) {
                    String msg = message.content.toString();
                    getData(msg);
                    dismissProgressDialog();
                }
            }
        }));
    }

    public String firstJinRu = "0";

    private void getData(String msg) {
        if (msg.contains("m")) {
            if (firstJinRu.equals("0")) {

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
            firstJinRu = "1";
            handlerStart.removeMessages(1);
            msg = "aa" + msg;//加占位符
            //12v点火塞功率  60 - 100w
            dianhuosai12V = Y.getInt(msg.substring(3, 6));
            //24v点火塞功率  60 - 100w
            dianhuosai24V = Y.getInt(msg.substring(6, 9));
            //油泵规格  12 - 70P
            youbengguige = Y.getInt(msg.substring(9, 11));
            //磁铁
            ciTie = Y.getInt(msg.substring(11, 12));
            //机器功率
            jiqigonglv = Y.getInt(msg.substring(12, 14));

            //大风大油
            daYou = Y.getInt(msg.substring(14, 15));

            //点火塞内阻
            dianHuoSaiKaiOrGuanBie = Y.getInt(msg.substring(15, 16));
            //传感器类型
            chuanGanQi_NP0 = Y.getInt(msg.substring(16, 17));

            //高温降油阀值  105 - 150
            gaowenjiangyou = Y.getInt(msg.substring(17, 20));
            //高温报警范围
            gaowenbaojing = Y.getInt(msg.substring(20, 23));
            //机电类型
            jiDianLeiXing = Y.getInt(msg.substring(23, 25));
            setView();
        }
    }

    private void setView() {
        clickDianhuosai_setview(jiaresai);

        clickDanShuangCiTie(ciTie);
        clickJiQiGongLv(jiqigonglv);
        clickDaYou(daYou);
        clickDianHuoSai_kaiQi_GuanBi(dianHuoSaiKaiOrGuanBie);
        clickChuanGanQi_NP(chuanGanQi_NP0);
        clickJiDianLeiXing(jiDianLeiXing);


        seekbar_guoyabaohu_12v.setProgress(gaowenjiangyou);
        tv_guoyabaohu_12v.setText(gaowenjiangyou + "w");

        seekbar_qianyabaohu_12v.setProgress(gaowenbaojing);
        tv_qianyabaohu_12v.setText(gaowenbaojing + "w");

//        seekbar_guoyabaohu_24v.setProgress(guoya24V);
//        tv_guoyabaohu_24v.setText(guoya24V + "w");
//
//        seekbar_qianyabaohu_24v.setProgress(qianya24V);
//        tv_qianyabaohu_24v.setText(qianya24V + "w");

        seekbar_dianhuosai_12v.setProgress(dianhuosai12V);
        tv_dianhuosai_12v.setText(dianhuosai12V + "w");

        seekbar_dianhuosai_24v.setProgress(dianhuosai24V);
        tv_dianhuosai_24v.setText(dianhuosai24V + "w");

        seekbar_youbengguige.setProgress(youbengguige);
        tv_youbengguige.setText(youbengguige + "w");


    }

    private void showNodata() {
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
        dialog.setTextContent("暂无主机参数信息");
        dialog.setTextConfirm("关闭");
        dialog.show();
    }


    /**
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

        sendMingling();
    }

    private void sendMingling() {
        //向水暖加热器发送获取实时数据
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("M514.")
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

    private int time = 0;

    private Handler handlerStart = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                time++;
                if (time >= 20) {
                    showTishiDialog();
                } else {
                    if (time == 5 || time == 10 || time == 15) {
                        sendMingling();
                    }
                    initHandlerStart();
                }
            }
            return false;
        }
    });

    private void showTishiDialog() {
        time = 0;
        showNodata();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerStart.removeMessages(1);
    }

    @OnClick({R.id.bt_save, R.id.rl_back, R.id.tv_dianhuosai_jingci, R.id.tv_dianhuosai_limai, R.id.tv_dianhuosai_gaide, R.id.tv_dianhuosai_tuobo
            , R.id.tv_dancitie, R.id.tv_shuangcitie, R.id.tv_dayou_kaiqi, R.id.tv_dayou_guanbi, R.id.tv_dianhuosaineizu_kaiqi, R.id.tv_dianhuosaineizu_guanbi,
            R.id.tv_jidian_12, R.id.tv_jidian_24, R.id.tv_jidian_zidong, R.id.tv_chuanganqi_0, R.id.tv_chuanganqi_1, R.id.tv_chuanganqi_2, R.id.tv_jiqigonglv_2kw, R.id.tv_jiqigonglv_5kw
            , R.id.tv_jiqigonglv_zidingyi, R.id.bt_huifuchuchang, R.id.tv_wucitie})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_huifuchuchang:
                clickHuiFuChuChang();
                break;
            case R.id.tv_chuanganqi_0:
                clickChuanGanQi_NP(0);
                break;
            case R.id.tv_chuanganqi_1:
                clickChuanGanQi_NP(1);
                break;
            case R.id.tv_chuanganqi_2:
                clickChuanGanQi_NP(2);
                break;
            case R.id.tv_jidian_12:
                clickJiDianLeiXing(1);
                break;
            case R.id.tv_jidian_24:
                clickJiDianLeiXing(2);
                break;
            case R.id.tv_jidian_zidong:
                clickJiDianLeiXing(15);
                break;
            case R.id.bt_save:
                clickSave();
                break;
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_dianhuosai_jingci:
                clickDianhuosai(1);
                break;
            case R.id.tv_dianhuosai_limai:
                clickDianhuosai(2);
                break;
            case R.id.tv_dianhuosai_gaide:
                clickDianhuosai(3);
                break;
            case R.id.tv_dianhuosai_tuobo:
                clickDianhuosai(4);
                break;
            case R.id.tv_dancitie:
                clickDanShuangCiTie(1);
                break;
            case R.id.tv_shuangcitie:
                clickDanShuangCiTie(0);
                break;
            case R.id.tv_wucitie:
                clickDanShuangCiTie(2);
                break;
            case R.id.tv_dayou_kaiqi:
                clickDaYou(1);
                break;
            case R.id.tv_dayou_guanbi:
                clickDaYou(0);
                break;
            case R.id.tv_dianhuosaineizu_kaiqi:
                clickDianHuoSai_kaiQi_GuanBi(1);
                break;
            case R.id.tv_dianhuosaineizu_guanbi:
                clickDianHuoSai_kaiQi_GuanBi(0);
                break;
            case R.id.tv_jiqigonglv_2kw:
                clickJiQiGongLv(2);
                break;
            case R.id.tv_jiqigonglv_5kw:
                clickJiQiGongLv(5);
                break;
            case R.id.tv_jiqigonglv_zidingyi:
                clickJiQiGongLv(50);
                break;


        }
    }

    private void clickHuiFuChuChang() {
        String mingling = "M501.";
        //向水暖加热器发送获取实时数据
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg(mingling)
                .setQos(2).setRetained(false)
                .setTopic(CAR_CTROL), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
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

    private int jiqigonglv;

    private void clickJiQiGongLv(int pos) {
        jiqigonglv = pos;
        tvJiqigonglv2kw.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvJiqigonglv5kw.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvJiqigonglvZidingyi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);


        tvJiqigonglv2kw.setTextColor(Color.BLACK);
        tvJiqigonglv5kw.setTextColor(Color.BLACK);
        tvJiqigonglvZidingyi.setTextColor(Color.BLACK);


        switch (pos) {
            case 2:

                tvJiqigonglv2kw.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvJiqigonglv2kw.setTextColor(Color.WHITE);
                break;
            case 3:

                tvJiqigonglv5kw.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvJiqigonglv5kw.setTextColor(Color.WHITE);
                break;
            case 50:

                tvJiqigonglvZidingyi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvJiqigonglvZidingyi.setTextColor(Color.WHITE);
                break;
            case 1:
            default:
                break;
        }
    }


    private void clickSave() {
        String dianhuosai12VS = dianhuosai12V + "";
        if (dianhuosai12VS.length() == 2) {
            dianhuosai12VS = "0" + dianhuosai12VS;
        }

        String dianhuosai24VS = dianhuosai24V + "";
        if (dianhuosai24VS.length() == 2) {
            dianhuosai24VS = "0" + dianhuosai24VS;
        }

        String gaoWenBaoJingVs = gaowenbaojing + "";
        if (gaoWenBaoJingVs.length() == 1) {
            gaoWenBaoJingVs = "0" + gaoWenBaoJingVs;
        }
        String youBengGuiGeVs = String.valueOf(youbengguige);//油泵规格
        String ciTieShuLiangVs = String.valueOf(ciTie);

        String jiQiGongLvVs = String.valueOf(jiqigonglv);

        if (jiqigonglv > 10) {

        } else {
            jiQiGongLvVs = "0" + jiqigonglv;
        }
        String daYouVs = String.valueOf(daYou);
        String dianHuoSaiNeiVs = String.valueOf(dianHuoSaiKaiOrGuanBie);
        String chuanGanQiVs = String.valueOf(chuanGanQi_NP0);
        String gaoWenJiangYouVs = String.valueOf(gaowenjiangyou);
        String jiQiDianYaVs = String.valueOf(jiDianLeiXing);
        if (jiDianLeiXing > 10) {

        } else {
            jiQiDianYaVs = "0" + jiDianLeiXing;
        }


        String mingling = "M55" + dianhuosai12VS + dianhuosai24VS +
                youBengGuiGeVs + ciTieShuLiangVs + jiQiGongLvVs + daYouVs +
                dianHuoSaiNeiVs + chuanGanQiVs + gaoWenJiangYouVs + gaoWenBaoJingVs + jiQiDianYaVs + ".";

        Y.e("我发送的数据是什么啊啊啊  " + mingling);

        //向水暖加热器发送获取实时数据
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg(mingling)
                .setQos(2).setRetained(false)
                .setTopic(CAR_CTROL), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
//                TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
//                    @Override
//                    public void onClickCancel(View v, TishiDialog dialog) {
//
//                    }
//
//                    @Override
//                    public void onClickConfirm(View v, TishiDialog dialog) {
//
//                        showProgressDialog("设置中，请稍后...");
//                    }
//
//                    @Override
//                    public void onDismiss(TishiDialog dialog) {
//
//                    }
//                });
//                dialog.show();

                showProgressDialog("设置中，请稍后...");
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

    private void clickDianhuosai(int pos) {
        jiaresai = pos;
        tv_dianhuosai_jingci.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_dianhuosai_limai.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_dianhuosai_gaide.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_dianhuosai_tuobo.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tv_dianhuosai_jingci.setTextColor(Color.BLACK);
        tv_dianhuosai_limai.setTextColor(Color.BLACK);
        tv_dianhuosai_gaide.setTextColor(Color.BLACK);
        tv_dianhuosai_tuobo.setTextColor(Color.BLACK);

        switch (pos) {
            case 2:
                seekbar_dianhuosai_12v.setProgress(83);
                seekbar_dianhuosai_24v.setProgress(83);
                tv_dianhuosai_limai.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_dianhuosai_limai.setTextColor(Color.WHITE);
                break;
            case 3:
                seekbar_dianhuosai_12v.setProgress(83);
                seekbar_dianhuosai_24v.setProgress(93);
                tv_dianhuosai_gaide.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_dianhuosai_gaide.setTextColor(Color.WHITE);
                break;
            case 4:
                seekbar_dianhuosai_12v.setProgress(83);
                seekbar_dianhuosai_24v.setProgress(93);
                tv_dianhuosai_tuobo.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_dianhuosai_tuobo.setTextColor(Color.WHITE);
                break;
            case 1:
            default:
                break;
        }
    }

    private void clickDianhuosai_setview(int pos) {
        jiaresai = pos;
        tv_dianhuosai_jingci.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_dianhuosai_limai.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_dianhuosai_gaide.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_dianhuosai_tuobo.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tv_dianhuosai_jingci.setTextColor(Color.BLACK);
        tv_dianhuosai_limai.setTextColor(Color.BLACK);
        tv_dianhuosai_gaide.setTextColor(Color.BLACK);
        tv_dianhuosai_tuobo.setTextColor(Color.BLACK);

        switch (pos) {
            case 2:

                tv_dianhuosai_limai.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_dianhuosai_limai.setTextColor(Color.WHITE);
                break;
            case 3:

                tv_dianhuosai_gaide.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_dianhuosai_gaide.setTextColor(Color.WHITE);
                break;
            case 4:

                tv_dianhuosai_tuobo.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_dianhuosai_tuobo.setTextColor(Color.WHITE);
                break;
            case 1:
            default:
                break;
        }
    }
    private int ciTie;

    private void clickDanShuangCiTie(int pos) {
        ciTie = pos;
        tvDancitie.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvShuangcitie.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tvWucitie.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);


        tvDancitie.setTextColor(Color.BLACK);
        tvShuangcitie.setTextColor(Color.BLACK);
        tvWucitie.setTextColor(Color.BLACK);

        switch (pos) {
            case 0://双磁铁
                tvShuangcitie.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvShuangcitie.setTextColor(Color.WHITE);
                break;
            case 1://单磁铁
                tvDancitie.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvDancitie.setTextColor(Color.WHITE);
                break;
            case 2://无磁铁

                tvWucitie.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvWucitie.setTextColor(Color.WHITE);

        }
    }

    public int daYou;

    private void clickDaYou(int pos) {
        daYou = pos;
        tvDayouKaiqi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvDayouGuanbi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);


        tvDayouKaiqi.setTextColor(Color.BLACK);
        tvDayouGuanbi.setTextColor(Color.BLACK);


        switch (pos) {
            case 1:
                tvDayouKaiqi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvDayouKaiqi.setTextColor(Color.WHITE);
                break;
            case 0:
                tvDayouGuanbi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvDayouGuanbi.setTextColor(Color.WHITE);
                break;

        }
    }

    private int dianHuoSaiKaiOrGuanBie = 0;//模式开启

    private void clickDianHuoSai_kaiQi_GuanBi(int pos) {
        //0关闭 1开启

        dianHuoSaiKaiOrGuanBie = pos;
        tvDianhuosaineizuKaiqi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvDianhuosaineizuGuanbi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);


        tvDianhuosaineizuKaiqi.setTextColor(Color.BLACK);
        tvDianhuosaineizuGuanbi.setTextColor(Color.BLACK);


        switch (pos) {
            case 0://关闭
                tvDianhuosaineizuGuanbi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvDianhuosaineizuGuanbi.setTextColor(Color.WHITE);
                break;
            case 1://开启
                tvDianhuosaineizuKaiqi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvDianhuosaineizuKaiqi.setTextColor(Color.WHITE);
                break;

        }
    }

    private int jiDianLeiXing;

    private void clickJiDianLeiXing(int pos) {

        jiDianLeiXing = pos;

        tvJidian12.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvJidian24.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvJidianZidong.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);


        tvJidian12.setTextColor(Color.BLACK);
        tvJidian24.setTextColor(Color.BLACK);
        tvJidianZidong.setTextColor(Color.BLACK);


        switch (pos) {
            case 1:
                tvJidian12.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvJidian12.setTextColor(Color.WHITE);
                break;
            case 2:
                tvJidian24.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvJidian24.setTextColor(Color.WHITE);
                break;
            case 15:
                tvJidianZidong.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvJidianZidong.setTextColor(Color.WHITE);
                break;
        }
    }

    private int chuanGanQi_NP0;

    private void clickChuanGanQi_NP(int pos) {

        chuanGanQi_NP0 = pos;

        tvChuanganqi0.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvChuanganqi1.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tvChuanganqi2.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tvChuanganqi0.setTextColor(Color.BLACK);
        tvChuanganqi1.setTextColor(Color.BLACK);
        tvChuanganqi2.setTextColor(Color.BLACK);


        switch (pos) {
            case 0:
                tvChuanganqi0.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvChuanganqi0.setTextColor(Color.WHITE);
                break;
            case 1:
                tvChuanganqi1.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvChuanganqi1.setTextColor(Color.WHITE);
                break;
            case 2:
                tvChuanganqi2.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tvChuanganqi2.setTextColor(Color.WHITE);
                break;

        }
    }
}
