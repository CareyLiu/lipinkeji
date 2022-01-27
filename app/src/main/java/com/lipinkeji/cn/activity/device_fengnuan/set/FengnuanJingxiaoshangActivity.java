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

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_shuinuan.ShuinuanBaseNewActivity;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    SeekBar seekbar_guoyabaohu_12v;
    @BindView(R.id.tv_guoyabaohu_12v)
    TextView tv_guoyabaohu_12v;
    @BindView(R.id.seekbar_qianyabaohu_12v)
    SeekBar seekbar_qianyabaohu_12v;
    @BindView(R.id.tv_qianyabaohu_12v)
    TextView tv_qianyabaohu_12v;
    @BindView(R.id.seekbar_guoyabaohu_24v)
    SeekBar seekbar_guoyabaohu_24v;
    @BindView(R.id.tv_guoyabaohu_24v)
    TextView tv_guoyabaohu_24v;
    @BindView(R.id.seekbar_qianyabaohu_24v)
    SeekBar seekbar_qianyabaohu_24v;
    @BindView(R.id.tv_qianyabaohu_24v)
    TextView tv_qianyabaohu_24v;
    @BindView(R.id.bt_save)
    TextView bt_save;

    private int dianhuosai12V;
    private int dianhuosai24V;
    private int jiaresai;
    private int youbengguige;
    private int guoya12V;
    private int qianya12V;
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
        initHuidiao();
        registerKtMqtt();
    }

    private void initData() {
        initView();


        dianhuosai12V = 60;
        dianhuosai24V = 60;
        jiaresai = 1;
        youbengguige = 12;
        guoya12V = 12;
        qianya12V = 7;
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
                guoya12V = progress;
                tv_guoyabaohu_12v.setText(guoya12V + "w");
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
                qianya12V = progress;
                tv_qianyabaohu_12v.setText(qianya12V + "w");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekbar_guoyabaohu_24v.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                guoya24V = progress;
                tv_guoyabaohu_24v.setText(guoya24V + "w");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbar_qianyabaohu_24v.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qianya24V = progress;
                tv_qianyabaohu_24v.setText(qianya24V + "w");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

    private void getData(String msg) {
        if (msg.contains("m_s")) {
            dismissProgressDialog();
            handlerStart.removeMessages(1);

            //12v点火塞功率  60 - 100w
            dianhuosai12V = Y.getInt(msg.substring(3, 6));
            //24v点火塞功率  60 - 100w
            dianhuosai24V = Y.getInt(msg.substring(6, 9));
            //加热塞  1、京瓷 2、利麦 3、盖得 4、拖博 5、默认
            jiaresai = Y.getInt(msg.substring(9, 10));
            //油泵规格  12 - 70P
            youbengguige = Y.getInt(msg.substring(10, 12));
            //12v过压保护阙值  12 - 40v
            guoya12V = Y.getInt(msg.substring(12, 14));
            //12v欠压保护阙值  7 -12v
            qianya12V = Y.getInt(msg.substring(14, 16));
            //24v过压保护阙值  24 – 40v
            guoya24V = Y.getInt(msg.substring(16, 18));
            //24v欠压保护阙值  7 – 22v
            qianya24V = Y.getInt(msg.substring(18, 20));

            setView();
        }
    }

    private void setView() {
        seekbar_dianhuosai_12v.setProgress(dianhuosai12V);
        tv_dianhuosai_12v.setText(dianhuosai12V + "w");

        seekbar_dianhuosai_24v.setProgress(dianhuosai24V);
        tv_dianhuosai_24v.setText(dianhuosai24V + "w");

        seekbar_youbengguige.setProgress(youbengguige);
        tv_youbengguige.setText(youbengguige + "w");

        seekbar_guoyabaohu_12v.setProgress(guoya12V);
        tv_guoyabaohu_12v.setText(guoya12V + "w");

        seekbar_qianyabaohu_12v.setProgress(qianya12V);
        tv_qianyabaohu_12v.setText(qianya12V + "w");

        seekbar_guoyabaohu_24v.setProgress(guoya24V);
        tv_guoyabaohu_24v.setText(guoya24V + "w");

        seekbar_qianyabaohu_24v.setProgress(qianya24V);
        tv_qianyabaohu_24v.setText(qianya24V + "w");

        clickDianhuosai(jiaresai);
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
                .setMsg("M_s115.")
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

    @OnClick({R.id.bt_save, R.id.rl_back, R.id.tv_dianhuosai_jingci, R.id.tv_dianhuosai_limai, R.id.tv_dianhuosai_gaide, R.id.tv_dianhuosai_tuobo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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

        String qianya12VS = qianya12V + "";
        if (qianya12VS.length() == 1) {
            qianya12VS = "0" + qianya12VS;
        }


        String qianya24VS = qianya24V + "";
        if (qianya24VS.length() == 1) {
            qianya24VS = "0" + qianya24VS;
        }


        String mingling = "M_s17" + dianhuosai12VS + dianhuosai24VS + jiaresai + youbengguige + guoya12V + qianya12VS + guoya24V + qianya24VS + ".";
        Y.e("我发送的数据是什么啊啊啊  " + mingling);

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
                tv_dianhuosai_jingci.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_dianhuosai_jingci.setTextColor(Color.WHITE);
                break;
        }
    }
}
