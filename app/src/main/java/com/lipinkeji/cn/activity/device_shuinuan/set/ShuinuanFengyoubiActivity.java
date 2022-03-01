package com.lipinkeji.cn.activity.device_shuinuan.set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ShuinuanFengyoubiActivity extends ShuinuanBaseNewActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.bt_save)
    TextView btSave;
    @BindView(R.id.ed_1dan_fengji)
    EditText ed1danFengji;
    @BindView(R.id.ed_1dan_youbeng)
    EditText ed1danYoubeng;
    @BindView(R.id.ed_2dan_fengji)
    EditText ed2danFengji;
    @BindView(R.id.ed_2dan_youbeng)
    EditText ed2danYoubeng;

    @Override
    public int getContentViewResId() {
        return R.layout.a_shuinuan_act_set_fengyoubi;
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
        Intent intent = new Intent(context, ShuinuanFengyoubiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initHuidiao();
        registerKtMqtt();
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
        if (msg.contains("h_s")) {
            dismissProgressDialog();
            handlerStart.removeMessages(1);

            //1档：风机转速
            int fengji1 = Y.getInt(msg.substring(3, 8));
            ed1danFengji.setText(fengji1 + "");
            //1档：油泵频率
            int you1_qian = Y.getInt(msg.substring(8, 10));
            int you1_hou = Y.getInt(msg.substring(10, 12));
            ed1danYoubeng.setText(you1_qian + "." + you1_hou);

            //2档：风机转速
            int fengji2 = Y.getInt(msg.substring(12, 17));
            ed2danFengji.setText(fengji2 + "");
            //2档：油泵频率
            int you2_qian = Y.getInt(msg.substring(17, 19));
            int you2_hou = Y.getInt(msg.substring(19, 21));
            ed2danYoubeng.setText(you2_qian + "." + you2_hou);
        }
    }

    /**
     * x
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
        //注册水暖加热器订阅
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

        //向水暖加热器发送获取实时数据
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("M_s111.")
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
                    showNodata();
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

    private void showNodata() {
        time = 0;
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
        dialog.setTextContent("暂无风油比参数信息");
        dialog.setTextConfirm("关闭");
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerStart.removeMessages(1);
    }

    @OnClick({R.id.rl_back, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.bt_save:
                clickSave();
                break;
        }
    }

    private void clickSave() {
        int fengji1 = Y.getInt(ed1danFengji.getText().toString());
        float youbeng1 = Y.getFloat(ed1danYoubeng.getText().toString());
        int fengji2 = Y.getInt(ed2danFengji.getText().toString());
        float youbeng2 = Y.getFloat(ed2danYoubeng.getText().toString());


        if (fengji1 < 3000 || fengji1 > 8000) {
            Y.t("请输入1档正确的风机转速");
            return;
        }

        if (youbeng1 < 0.5 || youbeng1 > 16) {
            Y.t("请输入1档正确的油泵频率");
            return;
        }

        if (fengji2 < 3000 || fengji2 > 8000) {
            Y.t("请输入2档正确的风机转速");
            return;
        }

        if (youbeng2 < 0.5 || youbeng2 > 16) {
            Y.t("请输入2档正确的油泵频率");
            return;
        }

        String feng1 = "0" + fengji1;
        String feng2 = "0" + fengji2;

        String you1 = Y.getMoney(youbeng1).replace(".", "");
        String you2 = Y.getMoney(youbeng2).replace(".", "");

        if (you1.length() == 3) {
            you1 = "0" + you1;
        }

        if (you2.length() == 3) {
            you2 = "0" + you2;
        }


        String mingling = "M_s12" + feng1 + you1 + feng2 + you2 + ".";
        //向水暖加热器发送获取实时数据
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg(mingling)
                .setQos(2).setRetained(false)
                .setTopic(SN_Send), new IMqttActionListener() {
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
}
