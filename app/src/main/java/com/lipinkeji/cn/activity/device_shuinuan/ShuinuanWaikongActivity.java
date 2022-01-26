package com.lipinkeji.cn.activity.device_shuinuan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShuinuanWaikongActivity extends ShuinuanBaseNewActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.tv_waikong_duankai)
    TextView tv_waikong_duankai;
    @BindView(R.id.tv_waikong_shuchu)
    TextView tv_waikong_shuchu;

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.a_shuinuan_act_set_waikong;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShuinuanWaikongActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        init();
        selectWaikong(0);
    }

    private void init() {
        if (!TextUtils.isEmpty(msgData)) {
            if (msgData.length() >= 62) {
                String waidian = msgData.substring(60, 61);
                if (waidian.equals("a")) {
                    showTiDialog("当前版本暂不支持外接装置功能");
                } else {
                    if (waidian.equals("1")) {
                        selectWaikong(1);
                    } else if (waidian.equals("2")) {
                        selectWaikong(0);
                    } else {
                        showTiDialog("当前版本暂不支持外接装置功能");
                    }
                }
            } else {
                showTiDialog("当前版本暂不支持外接装置功能");
            }
        } else {
            showTiDialog("设备未链接");
        }
    }

    private void selectWaikong(int pos) {
        tv_waikong_duankai.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);
        tv_waikong_shuchu.setBackgroundResource(R.drawable.jiareqi_dingshi_select_nor);

        tv_waikong_duankai.setTextColor(Color.BLACK);
        tv_waikong_shuchu.setTextColor(Color.BLACK);
        switch (pos) {
            case 0:
                tv_waikong_duankai.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_waikong_shuchu.setTextColor(Color.WHITE);
                break;
            case 1:
                tv_waikong_shuchu.setBackgroundResource(R.drawable.jiareqi_dingshi_select_sel);
                tv_waikong_shuchu.setTextColor(Color.WHITE);
                break;
        }
    }

    @OnClick({R.id.rl_back, R.id.tv_waikong_duankai, R.id.tv_waikong_shuchu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_waikong_duankai:
                clickDuankai();
                break;
            case R.id.tv_waikong_shuchu:
                clickShuchu();
                break;
        }
    }

    private void clickDuankai() {
        selectWaikong(0);
        seneMing("M_s142.");
    }

    private void clickShuchu() {
        selectWaikong(1);
        seneMing("M_s141.");
    }

    private void seneMing(String msg) {
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg(msg)
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
