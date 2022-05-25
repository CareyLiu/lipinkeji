package com.lipinkeji.cn.activity.device_fengnuan.set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_a.dialog.DianHuoSaiTiaoJieDialog;
import com.lipinkeji.cn.activity.device_shuinuan.FengNuanBaseNewActivity;
import com.lipinkeji.cn.activity.device_shuinuan.ShuinuanBaseNewActivity;
import com.lipinkeji.cn.app.UIHelper;
import com.lipinkeji.cn.common.StringUtils;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import butterknife.BindView;

public class DianHuoSaiActivity_FengNuan extends FengNuanBaseNewActivity {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.tv_jiare)
    TextView tvJiare;
    @BindView(R.id.ll_jiare)
    LinearLayout llJiare;

    String topic = "";
    String jiaReTime;
    @BindView(R.id.ll_shezhishijian)
    LinearLayout llShezhishijian;

    int wendu_int;
    String wendu_zhiling;
    DianHuoSaiTiaoJieDialog dianHuoSaiTiaoJieDialog;
    Thread thread;
    private boolean xianChengFlag = true;
    String car_server_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        car_server_id = getIntent().getStringExtra("car_server_id");
        llJiare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv1.getText().equals("0") && tv2.getText().equals("0") && tv3.getText().equals("0")) {
                    UIHelper.ToastMessage(mContext, "请先设置加热时间");
                    return;
                }


                if (tvJiare.getText().equals("关闭")) {
                    thread.interrupt();
                    xianChengFlag = false;
                    tvJiare.setText("加热");
                    tv1.setText("0");
                    tv2.setText("0");
                    tv3.setText("0");
                    wendu_int = 0;
                    jiaRe_stop();

                } else {
                    xianChengFlag = true;
                    tvJiare.setText("关闭");
                    TimeHandler timeHandler = new TimeHandler();
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {


                            while (xianChengFlag) {
                                try {
                                    thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (wendu_int == 0) {

                                    xianChengFlag = false;
                                    thread.interrupt();
                                } else {
                                    wendu_int = wendu_int - 1;
                                    Message message = timeHandler.obtainMessage(1);
                                    timeHandler.sendMessageDelayed(message, 1000);

                                }


                            }
                        }
                    });
                    thread.start();

                    jiaRe();

                }


            }
        });

        llShezhishijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dianHuoSaiTiaoJieDialog = new DianHuoSaiTiaoJieDialog(mContext, new DianHuoSaiTiaoJieDialog.TishiDialogListener() {
                    @Override
                    public void onClickCancel(View v, DianHuoSaiTiaoJieDialog dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, DianHuoSaiTiaoJieDialog dialog, String wendu) {

                        if (StringUtils.isEmpty(wendu)) {
                            UIHelper.ToastMessage(mContext, "请手动选择区间范围");
                            return;
                        }
                        if (wendu.length() == 1) {
                            tv1.setText("0");
                            tv2.setText("0");
                            tv3.setText(wendu);
                        } else if (wendu.length() == 2) {
                            tv1.setText("0");
                            tv2.setText(wendu.substring(0, 1));
                            tv3.setText(wendu.substring(1, 2));
                        } else if (wendu.length() == 3) {
                            tv1.setText(wendu.substring(0, 1));
                            tv2.setText(wendu.substring(1, 2));
                            tv3.setText(wendu.substring(2, 3));
                        }
                        wendu_int = Integer.valueOf(wendu);
                        wendu_zhiling = wendu;
                        dianHuoSaiTiaoJieDialog.dismiss();
                    }
                });

                dianHuoSaiTiaoJieDialog.show();


            }
        });


    }

    public class TimeHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    String wendu = String.valueOf(wendu_int);

                    if (wendu.length() == 1) {
                        tv1.setText("0");
                        tv2.setText("0");
                        tv3.setText(wendu);
                    } else if (wendu.length() == 2) {
                        tv1.setText("0");
                        tv2.setText(wendu.substring(0, 1));
                        tv3.setText(wendu.substring(1, 2));
                    } else if (wendu.length() == 3) {
                        tv1.setText(wendu.substring(0, 1));
                        tv2.setText(wendu.substring(1, 2));
                        tv3.setText(wendu.substring(2, 3));
                    }

                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_dianhuosai_fengnuan;
    }

    /**
     * 点火塞预热开关，时间范围 0-240秒 例如：M_s03000.表示停止  M_s03120.表示加热120秒   （2022年3月21日星期一修改-点火塞加热模式，只在待机条件下有效）应答b_s参数
     */
    public void jiaRe() {
        Log.i("dianHuoSai_Rair", dianHuoSai + wendu_zhiling);
        Log.i("dianHuoSai_Rair", "ccid:" + ccid);
        MqttPublish mqttPublish = new MqttPublish();
        mqttPublish.setTopic(FN_Send);
        if (wendu_zhiling.length() == 2) {
            wendu_zhiling = "0" + wendu_zhiling;
        }
        mqttPublish.setMsg(dianHuoSai + wendu_zhiling + ".");
        mqttPublish.setQos(2);
        AndMqtt.getInstance().publish(mqttPublish, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }


    /**
     * 点火塞预热开关，时间范围 0-240秒 例如：M_s03000.表示停止  M_s03120.表示加热120秒   （2022年3月21日星期一修改-点火塞加热模式，只在待机条件下有效）应答b_s参数
     */
    public void jiaRe_stop() {

        MqttPublish mqttPublish = new MqttPublish();
        mqttPublish.setTopic(FN_Send);
        mqttPublish.setMsg(dianHuoSai + "000.");
        mqttPublish.setQos(2);
        AndMqtt.getInstance().publish(mqttPublish, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }

    /**
     * 用于其他Activty跳转到该Activity
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DianHuoSaiActivity_FengNuan.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public boolean showToolBar() {
        return false;
    }

    @Override
    public void initImmersion() {
        // super.initImmersion();
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread = null;
        xianChengFlag = false;
    }
}
