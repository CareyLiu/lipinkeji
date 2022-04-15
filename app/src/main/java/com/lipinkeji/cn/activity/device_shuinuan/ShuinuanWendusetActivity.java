package com.lipinkeji.cn.activity.device_shuinuan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_a.dialog.JiareqiWenduDialog;
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

public class ShuinuanWendusetActivity extends ShuinuanBaseNewActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.tv_wendu_shangxian)
    TextView tv_wendu_shangxian;
    @BindView(R.id.tv_wendu_xiaxian)
    TextView tv_wendu_xiaxian;
    @BindView(R.id.iv_wendu_hengwen)
    ImageView iv_wendu_hengwen;
    @BindView(R.id.tv_wendu_kaiji)
    TextView tv_wendu_kaiji;
    @BindView(R.id.tv_wendu_guanji)
    TextView tv_wendu_guanji;
    @BindView(R.id.iv_wendu_shangxian)
    ImageView iv_wendu_shangxian;
    @BindView(R.id.iv_wendu_xiaxian)
    ImageView iv_wendu_xiaxian;
    @BindView(R.id.iv_wendu_kaiji)
    ImageView iv_wendu_kaiji;
    @BindView(R.id.iv_wendu_guanji)
    ImageView iv_wendu_guanji;
    @BindView(R.id.bt_save)
    TextView bt_save;
    @BindView(R.id.ll_wendu_shangxian)
    LinearLayout ll_wendu_shangxian;
    @BindView(R.id.ll_wendu_xiaxian)
    LinearLayout ll_wendu_xiaxian;
    @BindView(R.id.ll_wendu_kaiji)
    LinearLayout ll_wendu_kaiji;
    @BindView(R.id.ll_wendu_guanji)
    LinearLayout ll_wendu_guanji;


    private String wenduShangxian;
    private String isShangxian;

    private String wenduXiaxian;
    private String isXiaxian;

    private String hengwen;

    private String wenduKaiji;
    private String kaijiLing;
    private String isKaiji;

    private String wenduGuanji;
    private String guanjiLing;
    private String isGuanji;


    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.a_shuinuan_act_set_wendu;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShuinuanWendusetActivity.class);
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
        wenduShangxian = "aa";
        wenduXiaxian = "aaa";
        hengwen = "1";
        wenduKaiji = "aa";
        wenduGuanji = "aa";

        isShangxian = "0";
        isXiaxian = "2";

        isKaiji = "1";
        kaijiLing = "1";

        isGuanji = "1";
        guanjiLing = "0";

        setWendu();
    }

    private void setWendu() {


//        tv_wendu_shangxian.setText("温度范围50-85，当前" + wenduShangxian);
//        if (isShangxian.equals("1")) {
//            iv_wendu_shangxian.setImageResource(R.mipmap.wd_btn_kaiqi);
//        } else {
//            iv_wendu_shangxian.setImageResource(R.mipmap.wd_btn_gianbi);
//        }
//
//        tv_wendu_xiaxian.setText("温度范围45-80，当前" + wenduXiaxian);
//        if (isXiaxian.equals("1")) {
//            iv_wendu_xiaxian.setImageResource(R.mipmap.wd_btn_kaiqi);
//        } else {
//            iv_wendu_xiaxian.setImageResource(R.mipmap.wd_btn_gianbi);
//        }
//
//        if (hengwen.equals("1")) {
//            iv_wendu_hengwen.setImageResource(R.mipmap.wd_btn_kaiqi);
//        } else {
//            iv_wendu_hengwen.setImageResource(R.mipmap.wd_btn_gianbi);
//        }
//
//        if (isKaiji.equals("1")) {
//            tv_wendu_kaiji.setText(wenduKaiji);
//            iv_wendu_kaiji.setImageResource(R.mipmap.wd_btn_kaiqi);
//            if (kaijiLing.equals("1")) {
//                tv_wendu_kaiji.setText(wenduKaiji);
//            } else {
//                tv_wendu_kaiji.setText("-" + wenduKaiji);
//            }
//        } else {
//            tv_wendu_kaiji.setText("---");
//            iv_wendu_kaiji.setImageResource(R.mipmap.wd_btn_gianbi);
//        }
//
//        if (isGuanji.equals("1")) {
//            if (guanjiLing.equals("1")) {
//                tv_wendu_guanji.setText(wenduGuanji);
//            } else {
//                tv_wendu_guanji.setText("-" + wenduGuanji);
//            }
//            iv_wendu_guanji.setImageResource(R.mipmap.wd_btn_kaiqi);
//        } else {
//            tv_wendu_guanji.setText("---");
//            iv_wendu_guanji.setImageResource(R.mipmap.wd_btn_gianbi);
//        }
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
        if (msg.contains("a_s")) {
            dismissProgressDialog();
            handlerStart.removeMessages(1);
            qiDongMoShi = msg.substring(3, 4);//启动模式
            wenduShangxian = msg.substring(4, 6);
            wenduXiaxian = msg.substring(6, 9);
            wenduKaiji = msg.substring(9, 11);
            wenduGuanji = msg.substring(11, 13);

            iv_wendu_shangxian.setImageResource(R.mipmap.wd_btn_gianbi);
            iv_wendu_xiaxian.setImageResource(R.mipmap.wd_btn_gianbi);
            iv_wendu_kaiji.setImageResource(R.mipmap.wd_btn_gianbi);
            iv_wendu_guanji.setImageResource(R.mipmap.wd_btn_gianbi);

            if (qiDongMoShi.equals("0")) {

                iv_wendu_shangxian.setImageResource(R.mipmap.wd_btn_kaiqi);
                tv_wendu_shangxian.setText("当前设置温度：" + wenduShangxian);
            } else if (qiDongMoShi.equals("1")) {
                iv_wendu_xiaxian.setImageResource(R.mipmap.wd_btn_kaiqi);
                tv_wendu_xiaxian.setText("当前设置时间：" + wenduXiaxian);
            } else if (qiDongMoShi.equals("2")) {

                tv_wendu_kaiji.setText("当前恒温上限：" + wenduKaiji);
                tv_wendu_guanji.setText("当前恒温下限" + wenduGuanji);
                iv_wendu_kaiji.setImageResource(R.mipmap.wd_btn_kaiqi);
                iv_wendu_guanji.setImageResource(R.mipmap.wd_btn_kaiqi);
            }
//            isShangxian = msg.substring(3, 4);
//            String shangxianLing = msg.substring(4, 5);
//            wenduShangxian = msg.substring(5, 7);
//
//            isXiaxian = msg.substring(7, 8);
//            String xiaxianLing = msg.substring(8, 9);
//            wenduXiaxian = msg.substring(9, 11);
//
//            hengwen = msg.substring(11, 12);
//
//            isKaiji = msg.substring(12, 13);
//            kaijiLing = msg.substring(13, 14);
//            wenduKaiji = msg.substring(14, 16);
//
//            isGuanji = msg.substring(16, 17);
//            guanjiLing = msg.substring(17, 18);
//            wenduGuanji = msg.substring(18, 20);

            setWendu();
        }
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
        dialog.setTextContent("暂无温度参数信息");
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
                .setMsg("M_s117.")
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

    @OnClick({R.id.bt_save, R.id.rl_back, R.id.iv_wendu_shangxian, R.id.iv_wendu_xiaxian, R.id.iv_wendu_hengwen, R.id.iv_wendu_kaiji, R.id.iv_wendu_guanji, R.id.ll_wendu_shangxian, R.id.ll_wendu_xiaxian, R.id.ll_wendu_kaiji, R.id.ll_wendu_guanji})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_save:
                save();
                break;
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_wendu_shangxian:
                clickShangxian();
                break;
            case R.id.iv_wendu_xiaxian:
                clickXiaxian();
                break;
            case R.id.iv_wendu_hengwen:
                clickHenwen();
                break;
            case R.id.iv_wendu_kaiji:
                clickKaiji();
                break;
            case R.id.iv_wendu_guanji:
                clickGuanji();
                break;
            case R.id.ll_wendu_shangxian:
                showWenduDialog("3", wenduShangxian);
                break;
            case R.id.ll_wendu_xiaxian:
                showWenduDialog("4", wenduXiaxian);
                break;
            case R.id.ll_wendu_kaiji:
                showWenduDialog("5", wenduKaiji);
                break;
            case R.id.ll_wendu_guanji:
                showWenduDialog("6", wenduGuanji);
                break;
        }
    }


    private void save() {

//        if (qiDongMoShi.equals("0")) {//限温
//            wendushangxian = tv_wendu_shangxian.getText().toString();
//        } else if (qiDongMoShi.equals("1")) {//限时
//            sheDingShiJian = tv_wendu_xiaxian.getText().toString();
//        } else if (qiDongMoShi.equals("2")) {//恒温
//            hengWenShang = tv_wendu_kaiji.getText().toString();//恒温上
//            hengWenXia = tv_wendu_guanji.getText().toString();//恒温下
//        }


        if (wenduXiaxian.length() == 2) {
            wenduXiaxian = "0" + wenduXiaxian;
        }
        String zhiLing = "M_s19" + qiDongMoShi + wenduShangxian + wenduXiaxian + wenduKaiji + wenduGuanji + ".";


        Y.e("我发送的数据是什么啊啊啊  " + zhiLing);

        //向水暖加热器发送获取实时数据
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg(zhiLing)
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

    private void clickShangxian() {
        if (isShangxian.equals("1")) {
            isShangxian = "2";
            iv_wendu_shangxian.setImageResource(R.mipmap.wd_btn_gianbi);
        } else {
            showWenduDialog("3", wenduShangxian);
        }
    }

    private void clickXiaxian() {
        if (isXiaxian.equals("1")) {
            isXiaxian = "2";
            iv_wendu_xiaxian.setImageResource(R.mipmap.wd_btn_gianbi);
        } else {
            showWenduDialog("4", wenduXiaxian);
        }
    }

    private void clickHenwen() {
        if (hengwen.equals("1")) {
            hengwen = "2";
            iv_wendu_hengwen.setImageResource(R.mipmap.wd_btn_gianbi);
        } else {
            hengwen = "1";
            iv_wendu_hengwen.setImageResource(R.mipmap.wd_btn_kaiqi);
        }
    }

    private void clickKaiji() {
        if (isKaiji.equals("1")) {
            isKaiji = "2";
            tv_wendu_kaiji.setText("---");
            iv_wendu_kaiji.setImageResource(R.mipmap.wd_btn_gianbi);
        } else {
            showWenduDialog("5", wenduKaiji);
        }
    }

    private void clickGuanji() {
        if (isGuanji.equals("1")) {
            isGuanji = "2";
            tv_wendu_guanji.setText("---");
            iv_wendu_guanji.setImageResource(R.mipmap.wd_btn_gianbi);
        } else {
            showWenduDialog("6", wenduGuanji);
        }
    }

    private String qiDongMoShi = "";

    //温度上限 限温
    //温度下限 限时
    //温度开机 恒温温度上限
    //温度关机 恒温温度下限

    private void showWenduDialog(String type, String wendu) {
        JiareqiWenduDialog dialog = new JiareqiWenduDialog(mContext, new JiareqiWenduDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, JiareqiWenduDialog dialog) {
                if (type.equals("3")) {
                    iv_wendu_shangxian.setImageResource(R.mipmap.wd_btn_gianbi);
                } else if (type.equals("4")) {
                    iv_wendu_xiaxian.setImageResource(R.mipmap.wd_btn_gianbi);//限时
                } else if (type.equals("5")) {
                    tv_wendu_kaiji.setText("---");//恒温上限
                    iv_wendu_kaiji.setImageResource(R.mipmap.wd_btn_gianbi);
                } else if (type.equals("6")) {
                    tv_wendu_guanji.setText("---");//恒温下限
                    iv_wendu_guanji.setImageResource(R.mipmap.wd_btn_gianbi);
                }
                dialog.dismiss();
            }

            @Override
            public void onClickConfirm(View v, JiareqiWenduDialog dialog) {
                String wenduNow = dialog.getWendu();
                iv_wendu_shangxian.setImageResource(R.mipmap.wd_btn_gianbi);
                iv_wendu_xiaxian.setImageResource(R.mipmap.wd_btn_gianbi);
                iv_wendu_kaiji.setImageResource(R.mipmap.wd_btn_gianbi);
                iv_wendu_guanji.setImageResource(R.mipmap.wd_btn_gianbi);


                if (type.equals("3")) {
                    isShangxian = "1";
                    qiDongMoShi = "0";
                    wenduShangxian = wenduNow;
                    tv_wendu_shangxian.setText("温度范围50-85，当前" + wenduShangxian);
                    iv_wendu_shangxian.setImageResource(R.mipmap.wd_btn_kaiqi);
                } else if (type.equals("4")) {
                    qiDongMoShi = "1";
                    isXiaxian = "1";
                    wenduXiaxian = wenduNow;
                    tv_wendu_xiaxian.setText("温度范围45-80，当前" + wenduXiaxian);
                    iv_wendu_xiaxian.setImageResource(R.mipmap.wd_btn_kaiqi);
                } else if (type.equals("5")) {
                    qiDongMoShi = "2";
                    isKaiji = "1";
                    wenduKaiji = Math.abs(Y.getInt(wenduNow)) + "";
                    tv_wendu_kaiji.setText(wenduKaiji);
                    iv_wendu_kaiji.setImageResource(R.mipmap.wd_btn_kaiqi);

                    if (Y.getInt(wenduNow) < 0) {
                        kaijiLing = "0";
                    } else {
                        kaijiLing = "1";
                    }
                } else if (type.equals("6")) {
                    qiDongMoShi = "2";
                    isGuanji = "1";
                    wenduGuanji = Math.abs(Y.getInt(wenduNow)) + "";
                    tv_wendu_guanji.setText(wenduGuanji);
                    iv_wendu_guanji.setImageResource(R.mipmap.wd_btn_kaiqi);

                    if (Y.getInt(wenduNow) < 0) {
                        guanjiLing = "0";
                    } else {
                        guanjiLing = "1";
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.setType(type, wendu);
        dialog.show();
    }
}
