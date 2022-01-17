package com.lipinkeji.cn.activity.device_a.shengmingmodel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.util.Y;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShengmingtestActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.rl3)
    RelativeLayout rl3;
    @BindView(R.id.rl4)
    RelativeLayout rl4;
    @BindView(R.id.rl5)
    RelativeLayout rl5;
    @BindView(R.id.rl6)
    RelativeLayout rl6;

    private String customerCode = "RSD1389948105";
    private String ytoken = "fca76f960fe5ecd7447edbcc4ea799b2";
    private String mac = "149C7656FFB1";
    private String sessionId;
    private Device deviceModel;
    private SleepReportDate tizhengData;
    private ReportSummary summaryModel;
    private int pos;


    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.a_a_shentijiance;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShengmingtestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        getNet();
    }


    public void getNet() {
        String timestamp = System.currentTimeMillis() + "";
        String url = "http://iot.52soft.top/api/createSession?";
        String jiami = customerCode + timestamp + ytoken;
        String ltoken = md5(jiami);
        ltoken = ltoken.toLowerCase();

        String urlSet = url + "customerCode=" + customerCode + "&timestamp=" + timestamp + "&ltoken=" + ltoken;
        OkGo.<CreateSession>get(urlSet)
                .tag(this)//
                .execute(new JsonCallback<CreateSession>() {
                    @Override
                    public void onSuccess(Response<CreateSession> response) {
                        sessionId = response.body().getData();
                        Y.e("我是多少啊啊" + sessionId);
                    }
                });
    }


    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @OnClick({R.id.rl1, R.id.rl2, R.id.rl3, R.id.rl4, R.id.rl5, R.id.rl6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl1:
                getDevice();
                break;
            case R.id.rl2:
                getSleepReportDate();
                break;
            case R.id.rl3:
                getReportSummary();
                break;
            case R.id.rl4:
                getMacSleepReport();
                break;
            case R.id.rl5:
                getValidReportInfo();
                break;
            case R.id.rl6:
                getReport();
                break;
            case R.id.rl7:
                setAlarm();
                break;
        }
    }

    private void setAlarm() {
        String url = "http://iot.52soft.top/api/setAlarm?";
        String timestamp = System.currentTimeMillis() + "";
        String jiami = sessionId + customerCode + "_" + timestamp + ytoken;
        String ltoken = md5(jiami);
        ltoken = ltoken.toLowerCase();
        String time = "20220113";

        String urlSet = url +
                "sessionId=" + sessionId +
                "&timestamp=" + timestamp +
                "&ltoken=" + ltoken +
                "&mac=" + mac +
                "&flag=" + pos +
                "&time=" + time;
        OkGo.<Report>get(urlSet)
                .tag(this)//
                .execute(new JsonCallback<Report>() {
                    @Override
                    public void onSuccess(Response<Report> response) {

                    }
                });
    }

    private void getReport() {
        pos = 0;
        pos++;
        if (pos>2){
            pos=0;
        }


        String url = "http://iot.52soft.top/api/getReport?";
        String timestamp = System.currentTimeMillis() + "";
        String jiami = sessionId + customerCode + "_" + timestamp + ytoken;
        String ltoken = md5(jiami);
        ltoken = ltoken.toLowerCase();
        String time = "20220113";

        String urlSet = url +
                "sessionId=" + sessionId +
                "&timestamp=" + timestamp +
                "&ltoken=" + ltoken +
                "&mac=" + mac +
                "&flag=" + pos +
                "&time=" + time;
        OkGo.<Report>get(urlSet)
                .tag(this)//
                .execute(new JsonCallback<Report>() {
                    @Override
                    public void onSuccess(Response<Report> response) {

                    }
                });
    }

    private void getValidReportInfo() {
        String url = "http://iot.52soft.top/api/getValidReportInfo?";
        String timestamp = System.currentTimeMillis() + "";
        String jiami = sessionId + customerCode + "_" + timestamp + ytoken;
        String ltoken = md5(jiami);
        ltoken = ltoken.toLowerCase();
        String date = "20220113";

        String urlSet = url +
                "sessionId=" + sessionId +
                "&timestamp=" + timestamp +
                "&ltoken=" + ltoken +
                "&mac=" + mac +
                "&date=" + date;
        OkGo.<ValidReportInfo>get(urlSet)
                .tag(this)//
                .execute(new JsonCallback<ValidReportInfo>() {
                    @Override
                    public void onSuccess(Response<ValidReportInfo> response) {

                    }
                });

    }

    private void getMacSleepReport() {
        String url = "http://iot.52soft.top/api/getMacSleepReport?";
        String timestamp = System.currentTimeMillis() + "";
        String jiami = sessionId + customerCode + "_" + timestamp + ytoken;
        String ltoken = md5(jiami);
        ltoken = ltoken.toLowerCase();
        String date = "20220113";

        String urlSet = url +
                "sessionId=" + sessionId +
                "&timestamp=" + timestamp +
                "&ltoken=" + ltoken +
                "&mac=" + mac +
                "&date=" + date;
        OkGo.<MacSleepReport>get(urlSet)
                .tag(this)//
                .execute(new JsonCallback<MacSleepReport>() {
                    @Override
                    public void onSuccess(Response<MacSleepReport> response) {

                    }
                });
    }

    private void getReportSummary() {
        String url = "http://iot.52soft.top/api/getReportSummary?";
        String timestamp = System.currentTimeMillis() + "";
        String jiami = sessionId + customerCode + "_" + timestamp + ytoken;
        String ltoken = md5(jiami);
        ltoken = ltoken.toLowerCase();
        String date = "20220113";

        String urlSet = url +
                "sessionId=" + sessionId +
                "&timestamp=" + timestamp +
                "&ltoken=" + ltoken +
                "&mac=" + mac +
                "&date=" + date;
        OkGo.<ReportSummary>get(urlSet)
                .tag(this)//
                .execute(new JsonCallback<ReportSummary>() {
                    @Override
                    public void onSuccess(Response<ReportSummary> response) {
                        summaryModel = response.body();
                    }
                });
    }

    private void getSleepReportDate() {
        String url = "http://iot.52soft.top/api/getSleepReportDate?";
        String timestamp = System.currentTimeMillis() + "";
        String jiami = sessionId + customerCode + "_" + timestamp + ytoken;
        String ltoken = md5(jiami);
        ltoken = ltoken.toLowerCase();
        String date = "2020,2021,2022";

        String urlSet = url +
                "sessionId=" + sessionId +
                "&timestamp=" + timestamp +
                "&ltoken=" + ltoken +
                "&mac=" + mac +
                "&date=" + date;
        OkGo.<SleepReportDate>get(urlSet)
                .tag(this)//
                .execute(new JsonCallback<SleepReportDate>() {
                    @Override
                    public void onSuccess(Response<SleepReportDate> response) {
                        tizhengData = response.body();
                    }
                });
    }

    private void getDevice() {
        String url = "http://iot.52soft.top/api/getDevice?";
        String timestamp = System.currentTimeMillis() + "";
        String jiami = sessionId + customerCode + "_" + timestamp + ytoken;
        String ltoken = md5(jiami);
        ltoken = ltoken.toLowerCase();
        String urlSet = url + "sessionId=" + sessionId + "&timestamp=" + timestamp + "&ltoken=" + ltoken + "&mac=" + mac;
        OkGo.<Device>get(urlSet)
                .tag(this)//
                .execute(new JsonCallback<Device>() {
                    @Override
                    public void onSuccess(Response<Device> response) {
                        deviceModel = response.body();

                    }
                });
    }
}
