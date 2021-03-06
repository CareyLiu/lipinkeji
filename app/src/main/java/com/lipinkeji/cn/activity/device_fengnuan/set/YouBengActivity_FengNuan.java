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

public class YouBengActivity_FengNuan extends FengNuanBaseNewActivity {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llJiare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv1.getText().equals("0") && tv2.getText().equals("0") && tv3.getText().equals("0")) {
                    UIHelper.ToastMessage(mContext, "????????????????????????");
                    return;
                }


                if (tvJiare.getText().equals("??????")) {
                    thread.interrupt();
                    xianChengFlag = false;
                    tvJiare.setText("??????");
                    tv1.setText("0");
                    tv2.setText("0");
                    tv3.setText("0");
                    wendu_int = 0;
                    jiaRe_stop();

                } else {
                    xianChengFlag = true;
                    tvJiare.setText("??????");
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
                            UIHelper.ToastMessage(mContext, "???????????????????????????");
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
        return R.layout.layout_youbeng_fengnuan;
    }

    /**
     * ???????????????????????????????????? 0-240??? ?????????M_s03000.????????????  M_s03120.????????????120???   ???2022???3???21??????????????????-????????????????????????????????????????????????????????????b_s??????
     */
    public void jiaRe() {
        Log.i("dianHuoSai_Rair", youbeng + wendu_zhiling);
        MqttPublish mqttPublish = new MqttPublish();
        mqttPublish.setTopic(FN_Send);
        if (wendu_zhiling.length() == 2) {
            wendu_zhiling = "0" + wendu_zhiling;
        }else if (wendu_zhiling.length()==1){
            wendu_zhiling = "00" + wendu_zhiling;
        }
        mqttPublish.setMsg(youbeng + wendu_zhiling + ".");
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
     * ???????????????????????????????????? 0-240??? ?????????M_s03000.????????????  M_s03120.????????????120???   ???2022???3???21??????????????????-????????????????????????????????????????????????????????????b_s??????
     */
    public void jiaRe_stop() {

        MqttPublish mqttPublish = new MqttPublish();
        mqttPublish.setTopic(FN_Send);
        mqttPublish.setMsg(youbeng + "000.");
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
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, YouBengActivity_FengNuan.class);
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
