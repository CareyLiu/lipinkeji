package com.lipinkeji.cn.activity;

import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lipinkeji.cn.R;
import com.lipinkeji.cn.app.BaseActivity;

import butterknife.BindView;

public class WifiSettingActivity extends BaseActivity {

    @BindView(R.id.tv_kaiqi)
    TextView tvKaiqi;
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.tv_wifi_state)
    TextView tvWifiState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView();

        tvKaiqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvWifiState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_wifi_setting;
    }



    WifiInfo mInfo;


}