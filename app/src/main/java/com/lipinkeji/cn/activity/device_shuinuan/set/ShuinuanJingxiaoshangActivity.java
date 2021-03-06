package com.lipinkeji.cn.activity.device_shuinuan.set;

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
import com.lipinkeji.cn.activity.device_shuinuan.ShuinuanBaseNewActivity;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;
import com.lipinkeji.cn.util.Y;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ShuinuanJingxiaoshangActivity extends ShuinuanBaseNewActivity {


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
    @BindView(R.id.bt_huifuchuchang)
    TextView btHuifuchuchang;

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
        return R.layout.a_shuinuan_act_set_jingxiaoshang;
    }

    /**
     * ????????????Activty????????????Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShuinuanJingxiaoshangActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initHuidiao();
        registerKtMqtt();
        btHuifuchuchang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zhuangTai = "2";
                TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
                    @Override
                    public void onClickCancel(View v, TishiDialog dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, TishiDialog dialog) {
                        huiFuChuChangSheZhi();
                        showProgressDialog("??????????????????,?????????...");
                    }

                    @Override
                    public void onDismiss(TishiDialog dialog) {

                    }
                });
                dialog.show();

            }
        });
        showProgressDialog("?????????,?????????...");
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
                tv_youbengguige.setText(youbengguige + "p");
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
                tv_guoyabaohu_12v.setText(guoya12V + "v");
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
                tv_qianyabaohu_12v.setText(qianya12V + "v");
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
                tv_guoyabaohu_24v.setText(guoya24V + "v");
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
                tv_qianyabaohu_24v.setText(qianya24V + "v");
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

    private String zhuangTai = "0";//0 ??????????????? 1.?????????????????? 2.??????????????????

    //???????????????????????????
    private void huiFuChuChangSheZhi() {

        String mingling = "M_s10" + "1.";
        Y.e("????????????????????????????????????  " + mingling);

        //??????????????????????????????????????????
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg(mingling)
                .setQos(2).setRetained(false)
                .setTopic(SN_Send), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

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

    private void getData(String msg) {
        if (msg.contains("m_s")) {
            if (zhuangTai.equals("0")) {

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
            handlerStart.removeMessages(1);

            //12v???????????????  60 - 100w
            dianhuosai12V = Y.getInt(msg.substring(3, 6));
            //24v???????????????  60 - 100w
            dianhuosai24V = Y.getInt(msg.substring(6, 9));
            //?????????  1????????? 2????????? 3????????? 4????????? 5?????????
            jiaresai = Y.getInt(msg.substring(9, 10));
            //????????????  12 - 70P
            youbengguige = Y.getInt(msg.substring(10, 12));
            //12v??????????????????  12 - 40v
            guoya12V = Y.getInt(msg.substring(12, 15));
            //12v??????????????????  7 -12v
            qianya12V = Y.getInt(msg.substring(15, 18));
            //24v??????????????????  24 ??? 40v
            guoya24V = Y.getInt(msg.substring(18, 21));
            //24v??????????????????  7 ??? 22v
            qianya24V = Y.getInt(msg.substring(21, 24));

            setView();
        }
    }

    private void setView() {
        seekbar_dianhuosai_12v.setProgress(dianhuosai12V);
        tv_dianhuosai_12v.setText(dianhuosai12V + "w");

        seekbar_dianhuosai_24v.setProgress(dianhuosai24V);
        tv_dianhuosai_24v.setText(dianhuosai24V + "w");

        seekbar_youbengguige.setProgress(youbengguige);
        tv_youbengguige.setText(youbengguige + "p");

        BigDecimal guoYa12Vdecimal = new BigDecimal(guoya12V);
        BigDecimal guoya12floag = guoYa12Vdecimal.divide(new BigDecimal(5));
        seekbar_guoyabaohu_12v.setProgress(guoya12floag.intValue());

        tv_guoyabaohu_12v.setText(guoya12floag.intValue() + "v");
        guoya12V = guoYa12Vdecimal.intValue();

        BigDecimal qianYa12Vdecimal = new BigDecimal(qianya12V);
        BigDecimal qianya12floag = qianYa12Vdecimal.divide(new BigDecimal(5));
        seekbar_qianyabaohu_12v.setProgress(qianya12floag.intValue());
        tv_qianyabaohu_12v.setText(qianya12floag.intValue() + "v");

        qianya12V = qianya12floag.intValue();

        BigDecimal guoya24Vdecimal = new BigDecimal(guoya24V);
        BigDecimal guoya24floag = guoya24Vdecimal.divide(new BigDecimal(5));

        seekbar_guoyabaohu_24v.setProgress(guoya24floag.intValue());
        tv_guoyabaohu_24v.setText(guoya24floag.intValue() + "v");

        guoya24V = guoya24floag.intValue();

        BigDecimal qianya24Vdecimal = new BigDecimal(qianya24V);
        BigDecimal qiamua24floag = qianya24Vdecimal.divide(new BigDecimal(5));
        //seekbar_guoyabaohu_24v.setProgress(guoya24floag.intValue());

        seekbar_qianyabaohu_24v.setProgress(qiamua24floag.intValue());
        tv_qianyabaohu_24v.setText(qiamua24floag.intValue() + "v");

        qianya24V = qiamua24floag.intValue();
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
        dialog.setTextTitle("??????");
        dialog.setTextContent("????????????????????????");
        dialog.setTextConfirm("??????");
        dialog.show();
    }


    /**
     * ????????????Mqtt
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
        //???????????????????????????
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

        //??????????????????????????????????????????
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("M_s115.")
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
                        getHost();
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

                TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
                    @Override
                    public void onClickCancel(View v, TishiDialog dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, TishiDialog dialog) {
                        clickSave();
                        showProgressDialog("?????????????????????...");
                    }

                    @Override
                    public void onDismiss(TishiDialog dialog) {

                    }
                });
                dialog.show();

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

        String qianya12VS = qianya12V * 5 + "";
        if (qianya12VS.length() == 2) {
            qianya12VS = "0" + qianya12VS;
        }


        String qianya24VS = qianya24V * 5 + "";
        if (qianya24VS.length() == 2) {
            qianya24VS = "0" + qianya24VS;
        }


        String guoya12 = guoya12V * 5 + "";
        if (guoya12.length() == 2) {
            guoya12 = "0" + guoya12;
        }

        String guoya24 = guoya24V * 5 + "";
        if (guoya24.length() == 2) {
            guoya24 = "0" + guoya24;
        }

        String mingling = "M_s17" + dianhuosai12VS + dianhuosai24VS + jiaresai + youbengguige + guoya12 + qianya12VS + guoya24 + qianya24VS + ".";
        Y.e("????????????????????????????????????  " + mingling);

        //??????????????????????????????????????????
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg(mingling)
                .setQos(2).setRetained(false)
                .setTopic(SN_Send), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

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
