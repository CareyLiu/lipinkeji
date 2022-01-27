package com.lipinkeji.cn.activity.device_fengnuan.set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.lipinkeji.cn.config.MyApplication.CARBOX_GETNOW;
import static com.lipinkeji.cn.config.MyApplication.CAR_CTROL;
import static com.lipinkeji.cn.config.MyApplication.CAR_NOTIFY;

public class FengnuanHoutaiActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.tv_huoyan_redianou)
    TextView tv_huoyan_redianou;
    @BindView(R.id.tv_huoyan_pt1000)
    TextView tv_huoyan_pt1000;
    @BindView(R.id.tv_huoyan_wu)
    TextView tv_huoyan_wu;
    @BindView(R.id.tv_fengji_dan)
    TextView tv_fengji_dan;
    @BindView(R.id.tv_fengji_shuang)
    TextView tv_fengji_shuang;
    @BindView(R.id.tv_fengji_si)
    TextView tv_fengji_si;
    @BindView(R.id.tv_fengji_wu)
    TextView tv_fengji_wu;
    @BindView(R.id.tv_jinshuikou_ntc)
    TextView tv_jinshuikou_ntc;
    @BindView(R.id.tv_jinshuikou_pt1000)
    TextView tv_jinshuikou_pt1000;
    @BindView(R.id.tv_jinshuikou_guanbi)
    TextView tv_jinshuikou_guanbi;
    @BindView(R.id.tv_chushuikou_ntc)
    TextView tv_chushuikou_ntc;
    @BindView(R.id.tv_chushuikou_pt1000)
    TextView tv_chushuikou_pt1000;
    @BindView(R.id.tv_chushuikou_guanbi)
    TextView tv_chushuikou_guanbi;
    @BindView(R.id.tv_shuibeng_shineng)
    TextView tv_shuibeng_shineng;
    @BindView(R.id.tv_shuibeng_guanbi)
    TextView tv_shuibeng_guanbi;
    @BindView(R.id.tv_dianya_12v)
    TextView tv_dianya_12v;
    @BindView(R.id.tv_dianya_24v)
    TextView tv_dianya_24v;
    @BindView(R.id.tv_dianya_moren)
    TextView tv_dianya_moren;
    @BindView(R.id.tv_you_chaiyou)
    TextView tv_you_chaiyou;
    @BindView(R.id.tv_you_qiyou)
    TextView tv_you_qiyou;
    @BindView(R.id.tv_you_qita)
    TextView tv_you_qita;
    @BindView(R.id.bt_save)
    TextView bt_save;
    @BindView(R.id.tv_huoyan_neizu)
    TextView tv_huoyan_neizu;

    private String cgq_huoyan;
    private String cgq_fengji;
    private String cgq_jinshui;
    private String cgq_chushui;
    private String shuibengguzhang;
    private String dianya;
    private String ranyouleixing;

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.a_fengnuan_act_set_houtai;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, FengnuanHoutaiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initData();
        initHuidiao();
        registerKtMqtt();
    }

    private void initData() {
        cgq_huoyan = "1";
        cgq_fengji = "1";
        cgq_jinshui = "1";
        cgq_chushui = "1";
        shuibengguzhang = "1";
        dianya = "1";
        ranyouleixing = "1";

        setView();
    }

    private void setView() {
        clickHuoyan(cgq_huoyan);
        clickfengji(cgq_fengji);
        clickJinshuikou(cgq_jinshui);
        clickChushuikou(cgq_chushui);
        clickShuibeng(shuibengguzhang);
        clickDianya(dianya);
        clickYou(ranyouleixing);
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
        if (msg.contains("n_s")) {
            dismissProgressDialog();
            handlerStart.removeMessages(1);

            //火焰传感器类型  1：热电偶 2：PT1000 3：内阻 4、无
            cgq_huoyan = msg.substring(3, 4);
            //风机转速传感器  1：热电偶 2：PT1000 3：内阻 4、无
            cgq_fengji = msg.substring(4, 5);
            //进水温度传感器  1:NTC50K 2: PT1000 3:关闭
            cgq_jinshui = msg.substring(5, 6);
            //出水温度传感器  1:NTC50K 2: PT1000 3:关闭
            cgq_chushui = msg.substring(6, 7);
            //水泵故障检测    1、使能  2、关闭
            shuibengguzhang = msg.substring(7, 8);
            //电压            1：12V  2：24V  3：自动
            dianya = msg.substring(8, 9);
            //燃油类型        1：柴油（默认）2:汽油 3:其他
            ranyouleixing = msg.substring(9, 10);

            setView();
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
                .setMsg("M_s116.")
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
                    showNodata();
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
        dialog.setTextContent("暂无主机参数信息");
        dialog.setTextConfirm("关闭");
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerStart.removeMessages(1);
    }


    @OnClick({R.id.rl_back, R.id.tv_huoyan_redianou, R.id.tv_huoyan_pt1000, R.id.tv_huoyan_neizu, R.id.tv_huoyan_wu, R.id.tv_fengji_dan, R.id.tv_fengji_shuang, R.id.tv_fengji_si, R.id.tv_fengji_wu, R.id.tv_jinshuikou_ntc, R.id.tv_jinshuikou_pt1000, R.id.tv_jinshuikou_guanbi, R.id.tv_chushuikou_ntc, R.id.tv_chushuikou_pt1000, R.id.tv_chushuikou_guanbi, R.id.tv_shuibeng_shineng, R.id.tv_shuibeng_guanbi, R.id.tv_dianya_12v, R.id.tv_dianya_24v, R.id.tv_dianya_moren, R.id.tv_you_chaiyou, R.id.tv_you_qiyou, R.id.tv_you_qita, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_huoyan_redianou:
                clickHuoyan("1");
                break;
            case R.id.tv_huoyan_pt1000:
                clickHuoyan("2");
                break;
            case R.id.tv_huoyan_neizu:
                clickHuoyan("3");
                break;
            case R.id.tv_huoyan_wu:
                clickHuoyan("4");
                break;
            case R.id.tv_fengji_dan:
                clickfengji("1");
                break;
            case R.id.tv_fengji_shuang:
                clickfengji("2");
                break;
            case R.id.tv_fengji_si:
                clickfengji("3");
                break;
            case R.id.tv_fengji_wu:
                clickfengji("4");
                break;
            case R.id.tv_jinshuikou_ntc:
                clickJinshuikou("1");
                break;
            case R.id.tv_jinshuikou_pt1000:
                clickJinshuikou("2");
                break;
            case R.id.tv_jinshuikou_guanbi:
                clickJinshuikou("3");
                break;
            case R.id.tv_chushuikou_ntc:
                clickChushuikou("1");
                break;
            case R.id.tv_chushuikou_pt1000:
                clickChushuikou("2");
                break;
            case R.id.tv_chushuikou_guanbi:
                clickChushuikou("3");
                break;
            case R.id.tv_shuibeng_shineng:
                clickShuibeng("1");
                break;
            case R.id.tv_shuibeng_guanbi:
                clickShuibeng("2");
                break;
            case R.id.tv_dianya_12v:
                clickDianya("1");
                break;
            case R.id.tv_dianya_24v:
                clickDianya("2");
                break;
            case R.id.tv_dianya_moren:
                clickDianya("3");
                break;
            case R.id.tv_you_chaiyou:
                clickYou("1");
                break;
            case R.id.tv_you_qiyou:
                clickYou("2");
                break;
            case R.id.tv_you_qita:
                clickYou("3");
                break;
            case R.id.bt_save:
                clickSave();
                break;
        }
    }

    private void clickSave() {
        String mingling = "M_s18" + cgq_huoyan + cgq_fengji + cgq_jinshui + cgq_chushui + shuibengguzhang + dianya + ranyouleixing + ".";
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

    private void clickYou(String type) {
        ranyouleixing = type;
        tv_you_chaiyou.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_you_qiyou.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_you_qita.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tv_you_chaiyou.setTextColor(Color.BLACK);
        tv_you_qiyou.setTextColor(Color.BLACK);
        tv_you_qita.setTextColor(Color.BLACK);
        switch (type) {
            case "1":
                tv_you_chaiyou.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_you_chaiyou.setTextColor(Color.WHITE);
                break;
            case "2":
                tv_you_qiyou.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_you_qiyou.setTextColor(Color.WHITE);
                break;
            case "3":
                tv_you_qita.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_you_qita.setTextColor(Color.WHITE);
                break;
        }
    }

    private void clickDianya(String type) {
        dianya = type;
        tv_dianya_12v.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_dianya_24v.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_dianya_moren.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tv_dianya_12v.setTextColor(Color.BLACK);
        tv_dianya_24v.setTextColor(Color.BLACK);
        tv_dianya_moren.setTextColor(Color.BLACK);
        switch (type) {
            case "1":
                tv_dianya_12v.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_dianya_12v.setTextColor(Color.WHITE);
                break;
            case "2":
                tv_dianya_24v.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_dianya_24v.setTextColor(Color.WHITE);
                break;
            case "3":
                tv_dianya_moren.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_dianya_moren.setTextColor(Color.WHITE);
                break;
        }
    }

    private void clickShuibeng(String type) {
        shuibengguzhang = type;
        tv_shuibeng_shineng.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_shuibeng_guanbi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tv_shuibeng_shineng.setTextColor(Color.BLACK);
        tv_shuibeng_guanbi.setTextColor(Color.BLACK);

        switch (type) {
            case "1":
                tv_shuibeng_shineng.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_shuibeng_shineng.setTextColor(Color.WHITE);
                break;
            case "2":
                tv_shuibeng_guanbi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_shuibeng_guanbi.setTextColor(Color.WHITE);
                break;
        }
    }

    private void clickChushuikou(String type) {
        cgq_chushui = type;
        tv_chushuikou_ntc.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_chushuikou_pt1000.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_chushuikou_guanbi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tv_chushuikou_ntc.setTextColor(Color.BLACK);
        tv_chushuikou_pt1000.setTextColor(Color.BLACK);
        tv_chushuikou_guanbi.setTextColor(Color.BLACK);

        switch (type) {
            case "1":
                tv_chushuikou_ntc.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_chushuikou_ntc.setTextColor(Color.WHITE);
                break;
            case "2":
                tv_chushuikou_pt1000.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_chushuikou_pt1000.setTextColor(Color.WHITE);
                break;
            case "3":
                tv_chushuikou_guanbi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_chushuikou_guanbi.setTextColor(Color.WHITE);
                break;
        }
    }

    private void clickJinshuikou(String type) {
        cgq_jinshui = type;
        tv_jinshuikou_ntc.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_jinshuikou_pt1000.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_jinshuikou_guanbi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tv_jinshuikou_ntc.setTextColor(Color.BLACK);
        tv_jinshuikou_pt1000.setTextColor(Color.BLACK);
        tv_jinshuikou_guanbi.setTextColor(Color.BLACK);

        switch (type) {
            case "1":
                tv_jinshuikou_ntc.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_jinshuikou_ntc.setTextColor(Color.WHITE);
                break;
            case "2":
                tv_jinshuikou_pt1000.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_jinshuikou_pt1000.setTextColor(Color.WHITE);
                break;
            case "3":
                tv_jinshuikou_guanbi.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_jinshuikou_guanbi.setTextColor(Color.WHITE);
                break;
        }
    }

    private void clickfengji(String type) {
        cgq_fengji = type;
        tv_fengji_dan.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_fengji_shuang.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_fengji_si.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_fengji_wu.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tv_fengji_dan.setTextColor(Color.BLACK);
        tv_fengji_shuang.setTextColor(Color.BLACK);
        tv_fengji_si.setTextColor(Color.BLACK);
        tv_fengji_wu.setTextColor(Color.BLACK);

        switch (type) {
            case "1":
                tv_fengji_dan.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_fengji_dan.setTextColor(Color.WHITE);
                break;
            case "2":
                tv_fengji_shuang.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_fengji_shuang.setTextColor(Color.WHITE);
                break;
            case "3":
                tv_fengji_si.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_fengji_si.setTextColor(Color.WHITE);
                break;
            case "4":
                tv_fengji_wu.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_fengji_wu.setTextColor(Color.WHITE);
                break;
        }
    }

    private void clickHuoyan(String type) {
        cgq_huoyan = type;
        tv_huoyan_redianou.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_huoyan_pt1000.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_huoyan_neizu.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_huoyan_wu.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tv_huoyan_redianou.setTextColor(Color.BLACK);
        tv_huoyan_pt1000.setTextColor(Color.BLACK);
        tv_huoyan_neizu.setTextColor(Color.BLACK);
        tv_huoyan_wu.setTextColor(Color.BLACK);

        switch (type) {
            case "1":
                tv_huoyan_redianou.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_huoyan_redianou.setTextColor(Color.WHITE);
                break;
            case "2":
                tv_huoyan_pt1000.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_huoyan_pt1000.setTextColor(Color.WHITE);
                break;
            case "3":
                tv_huoyan_neizu.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_huoyan_neizu.setTextColor(Color.WHITE);
                break;
            case "4":
                tv_huoyan_wu.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_huoyan_wu.setTextColor(Color.WHITE);
                break;
        }
    }
}
