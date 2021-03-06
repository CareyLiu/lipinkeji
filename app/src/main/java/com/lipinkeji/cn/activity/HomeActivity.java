package com.lipinkeji.cn.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.services.core.ServiceSettings;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jaeger.library.StatusBarUtil;
import com.lipinkeji.cn.activity.device_a.JiareqiGuzhangActivity;
import com.lipinkeji.cn.activity.device_fengnuan.LipinFengnuanActivity;
import com.lipinkeji.cn.app.AppConfig;
import com.lipinkeji.cn.app.UIHelper;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.util.Utils;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.util.Y;
import com.lipinkeji.cn.app.AppManager;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.config.MyApplication;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;
import com.lipinkeji.cn.fragment.HomeFragment;
import com.lipinkeji.cn.fragment.MineFragment;
import com.lipinkeji.cn.fragment.OnlineFragment;
import com.lipinkeji.cn.fragment.ShuoMingFragment;
import com.lipinkeji.cn.model.AlarmClass;
import com.lipinkeji.cn.util.AppToast;
import com.lipinkeji.cn.util.SoundPoolUtils;
import com.lipinkeji.cn.view.NoScrollViewPager;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.lipinkeji.cn.app.ConfigValue.JIESHOUP;
import static com.lipinkeji.cn.config.MyApplication.CAR_NOTIFY;

public class HomeActivity extends BaseActivity {
    @BindView(R.id.vp)
    NoScrollViewPager mVp;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.iv_device)
    ImageView ivDevice;
    @BindView(R.id.tv_device)
    TextView tvDevice;
    @BindView(R.id.ll_device)
    LinearLayout llDevice;
    @BindView(R.id.iv_mine)
    ImageView ivMine;
    @BindView(R.id.tv_mine)
    TextView tvMine;
    @BindView(R.id.ll_mine)
    LinearLayout llMine;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.iv_shebei)
    ImageView ivShebei;
    @BindView(R.id.tv_shebei)
    TextView tvShebei;
    @BindView(R.id.ll_shebei)
    LinearLayout llShebei;

    private AlarmClass alarmClass;
    private Handler handler;
    private Runnable runnable;
    private boolean isExit;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_main_new;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        StatusBarUtil.setLightMode(this);
        init();
        initData();
        initHuidiao();
        initHandler();
        dingWei();
    }

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private void dingWei() {
        try {
            ServiceSettings.updatePrivacyShow(mContext, true, true);
            ServiceSettings.updatePrivacyAgree(mContext, true);
            //?????????client
            locationClient = new AMapLocationClient(this);
            locationOption = getDefaultOption();
            //??????????????????
            locationClient.setLocationOption(locationOption);
            // ??????????????????
            locationClient.setLocationListener(gaodeDingWeiListener);

            startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    /**
     * ????????????
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        //????????????????????????????????????????????????
        //resetOption();
        // ??????????????????
        locationClient.setLocationOption(locationOption);
        // ????????????
        locationClient.startLocation();
    }

    /**
     * ????????????
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // ????????????
        locationClient.stopLocation();
    }

    /**
     * ?????????????????????
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//????????????????????????????????????????????????????????????????????????????????????????????????????????????
        mOption.setGpsFirst(false);//?????????????????????gps??????????????????????????????????????????????????????
        mOption.setHttpTimeOut(30000);//???????????????????????????????????????????????????30?????????????????????????????????
        mOption.setInterval(2000);//???????????????????????????????????????2???
        mOption.setNeedAddress(true);//????????????????????????????????????????????????????????????true
        mOption.setOnceLocation(false);//?????????????????????????????????????????????false
        mOption.setOnceLocationLatest(false);//???????????????????????????wifi??????????????????false.???????????????true,?????????????????????????????????????????????????????????
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//????????? ????????????????????????????????????HTTP??????HTTPS????????????HTTP
        mOption.setSensorEnable(false);//????????????????????????????????????????????????false
        mOption.setWifiScan(true); //???????????????????????????wifi??????????????????true??????????????????false??????????????????????????????????????????????????????????????????????????????????????????????????????
        mOption.setLocationCacheEnable(true); //???????????????????????????????????????????????????true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//??????????????????????????????????????????????????????????????????????????????????????????????????????
        return mOption;
    }

    /**
     * ????????????
     */
    AMapLocationListener gaodeDingWeiListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode??????0????????????????????????????????????????????????????????????????????????????????????????????????
                if (location.getErrorCode() == 0) {
                    sb.append("????????????" + "\n");
                    sb.append("????????????: " + location.getLocationType() + "\n");
                    sb.append("???    ???    : " + location.getLongitude() + "\n");
                    sb.append("???    ???    : " + location.getLatitude() + "\n");
                    sb.append("???    ???    : " + location.getAccuracy() + "???" + "\n");
                    sb.append("?????????    : " + location.getProvider() + "\n");

                    sb.append("???    ???    : " + location.getSpeed() + "???/???" + "\n");
                    sb.append("???    ???    : " + location.getBearing() + "\n");
                    // ?????????????????????????????????????????????
                    sb.append("???    ???    : " + location.getSatellites() + "\n");
                    sb.append("???    ???    : " + location.getCountry() + "\n");
                    sb.append("???            : " + location.getProvince() + "\n");
                    sb.append("???            : " + location.getCity() + "\n");
                    sb.append("???????????? : " + location.getCityCode() + "\n");
                    sb.append("???            : " + location.getDistrict() + "\n");
                    sb.append("?????? ???   : " + location.getAdCode() + "\n");
                    sb.append("???    ???    : " + location.getAddress() + "\n");
                    sb.append("?????????    : " + location.getPoiName() + "\n");
                    //?????????????????????
                    sb.append("????????????: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");


                    PreferenceHelper.getInstance(mContext).putString("WEIDU", location.getLatitude() + "");
                    PreferenceHelper.getInstance(mContext).putString("JINGDU", location.getLongitude() + "");
                    stopLocation();
                    locationClient.stopAssistantLocation();
                    gaodeDingWeiListener = null;

                } else {


                    //????????????
                    sb.append("????????????" + "\n");
                    sb.append("?????????:" + location.getErrorCode() + "\n");
                    sb.append("????????????:" + location.getErrorInfo() + "\n");
                    sb.append("????????????:" + location.getLocationDetail() + "\n");
                }
                sb.append("***??????????????????***").append("\n");
                sb.append("* WIFI?????????").append(location.getLocationQualityReport().isWifiAble() ? "??????" : "??????").append("\n");
                sb.append("* GPS?????????").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
                sb.append("* GPS?????????").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
                sb.append("* ???????????????" + location.getLocationQualityReport().getNetworkType()).append("\n");
                sb.append("* ???????????????" + location.getLocationQualityReport().getNetUseTime()).append("\n");
                sb.append("****************").append("\n");
                //???????????????????????????
                sb.append("????????????: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //?????????????????????
                String result = sb.toString();
                Log.i("Location_result", result);
            } else {

                Log.i("Location_result", "????????????");
            }

        }
    };

    /**
     * ??????GPS??????????????????
     *
     * @param statusCode GPS?????????
     * @return
     */
    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS????????????";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "???????????????GPS Provider???????????????GPS??????";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS?????????????????????GPS?????????????????????";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "?????????????????????????????????GPS???????????????????????????GPS????????????????????????????????????";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "??????GPS???????????????????????????gps????????????";
                break;
        }
        return str;
    }


    private void initHandler() {
        if (JIESHOUP.equals("1")) {
            return;
        }
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //???????????????
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("O.")
                        .setQos(2).setRetained(false)
                        .setTopic(CAR_NOTIFY), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });
                handler.postDelayed(this, 5000);
            }
        };

        handler.postDelayed(runnable, 5000);
    }

    private void init() {
        Notice notice = new Notice();
        notice.type = ConstanceValue.TONGYI;
        sendRx(notice);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        AppManager.getAppManager().addActivity(this);
    }

    private void initData() {
        List<Fragment> fragments = new ArrayList<>(3);
        HomeFragment youJiaTeShouYeFragment = new HomeFragment();
        OnlineFragment onlineFragment = new OnlineFragment();
        MineFragment mineFragment = new MineFragment();
        ShuoMingFragment shuoMingFragment = new ShuoMingFragment();

        fragments.add(youJiaTeShouYeFragment);
        fragments.add(onlineFragment);
        fragments.add(shuoMingFragment);
        fragments.add(mineFragment);

        VpAdapter adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        mVp.setOffscreenPageLimit(4);
        mVp.setScroll(false);
        mVp.setAdapter(adapter);

        select(0);
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice notice) {
                if (notice.type == ConstanceValue.MSG_GUZHANG_SHOUYE) {
                    tuiSongTanChuang(notice);
                } else if (notice.type == ConstanceValue.MSG_P) {
                    if (handler != null) {
                        JIESHOUP = "1";
                        handler.removeCallbacks(runnable);
                        handler = null;
                    }

                } else if (notice.type == ConstanceValue.MSG_ZHINENGJIAJU) {
                    select(1);
                }
            }
        }));
    }

    private void tuiSongTanChuang(Notice notice) {
        String message = (String) notice.content;
        alarmClass = new Gson().fromJson(message, AlarmClass.class);
        if (MyApplication.activity_main.getClass().getSimpleName().equals(LipinFengnuanActivity.class.getSimpleName())) {
            return;
        } else if (MyApplication.activity_main.getClass().getSimpleName().equals(JiareqiGuzhangActivity.class.getSimpleName())) {
            return;
        }

        if (alarmClass.type.equals("1")) {
            switch (alarmClass.sound) {
                case "chSound1.mp3":
                    playMusic(R.raw.ch_sound1);
                    break;
                case "chSound2.mp3":
                    playMusic(R.raw.ch_sound2);
                    break;
                case "chSound3.mp3":
                    playMusic(R.raw.ch_sound3);
                    break;
                case "chSound4.mp3":
                    playMusic(R.raw.ch_sound4);
                    break;
                case "chSound5.mp3":
                    playMusic(R.raw.ch_sound5);
                    break;
                case "chSound6.mp3":
                    playMusic(R.raw.ch_sound6);
                    break;
                case "chSound8.mp3":
                    playMusic(R.raw.ch_sound8);
                    break;
                case "chSound9.mp3":
                    playMusic(R.raw.ch_sound9);
                    break;
                case "chSound10.mp3":
                    playMusic(R.raw.ch_sound10);
                    break;
                case "chSound11.mp3":
                    playMusic(R.raw.ch_sound11);
                    break;
                case "chSound18.mp3":
                    playMusic(R.raw.ch_sound18);
                    break;
            }
        }
    }

    public void playMusic(int res) {
        boolean flag = false;
        Activity currentActivity = AppManager.getAppManager().currentActivity();
        if (currentActivity != null) {
            if (!currentActivity.getClass().getSimpleName().equals(JiareqiGuzhangActivity.class.getSimpleName())) {
                TishiDialog myCarCaoZuoDialog_notify = new TishiDialog(HomeActivity.this, 1, new TishiDialog.TishiDialogListener() {

                    @Override
                    public void onClickCancel(View v, TishiDialog dialog) {
                        if (SoundPoolUtils.soundPool != null) {
                            SoundPoolUtils.soundPool.release();
                        }
                    }

                    @Override
                    public void onClickConfirm(View v, TishiDialog dialog) {
                        JiareqiGuzhangActivity.actionStart(HomeActivity.this, alarmClass);
                        if (SoundPoolUtils.soundPool != null) {
                            SoundPoolUtils.soundPool.release();
                        }
                    }

                    @Override
                    public void onDismiss(TishiDialog dialog) {

                    }
                });

                myCarCaoZuoDialog_notify.setTextTitle("????????????");
                myCarCaoZuoDialog_notify.setTextContent("?????????????????????????????????????????????!");
                myCarCaoZuoDialog_notify.setTextCancel("??????");

                Y.e("???????????????????????????????????????");
                // myCarCaoZuoDialog_notify.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG);
                myCarCaoZuoDialog_notify.show();
                Y.e("???????????????????????????????????????????????????????????????");
                myCarCaoZuoDialog_notify.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (SoundPoolUtils.soundPool != null) {
                            SoundPoolUtils.soundPool.release();
                        }
                    }
                });
            } else {
                flag = true;
            }
        }

        if (flag) {
            return;
        }

        SoundPoolUtils.soundPool(mContext, res);
    }

    @OnClick({R.id.ll_home, R.id.ll_shebei, R.id.ll_device, R.id.ll_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                select(0);
                break;
            case R.id.ll_shebei:
                select(1);
                break;
            case R.id.ll_device:
                select(2);
                break;
            case R.id.ll_mine:
                select(3);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void select(int pos) {
//        ShengmingtestActivity.actionStart(mContext);
        mVp.setCurrentItem(pos);
        ivHome.setImageResource(R.mipmap.jiareqi_shangcheng_nor);
        ivShebei.setImageResource(R.mipmap.jiareqi_shebei_nor);
        ivDevice.setImageResource(R.mipmap.jiareqi_shuoming_nor);
        ivMine.setImageResource(R.mipmap.jiareqi_wd_nor);

        tvHome.setTextColor(Y.getColor(R.color.color_A6A6A6));
        tvShebei.setTextColor(Y.getColor(R.color.color_A6A6A6));
        tvDevice.setTextColor(Y.getColor(R.color.color_A6A6A6));
        tvMine.setTextColor(Y.getColor(R.color.color_A6A6A6));
        if (pos == 0) {
            ivHome.setImageResource(R.mipmap.jiareqi_shangcheng_sel);
            tvHome.setTextColor(Y.getColor(R.color.color_main_fu));
        } else if (pos == 1) {
            ivShebei.setImageResource(R.mipmap.jiareqi_shebei_sel);
            tvShebei.setTextColor(Y.getColor(R.color.color_main_fu));
        } else if (pos == 2) {
            ivDevice.setImageResource(R.mipmap.jiareqi_shuoming_sel);
            tvDevice.setTextColor(Y.getColor(R.color.color_main_fu));
        } else if (pos == 3) {
            ivMine.setImageResource(R.mipmap.jiareqi_wd_sel);
            tvMine.setTextColor(Y.getColor(R.color.color_main_fu));
        }
    }

    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> data;

        VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    public static HomeActivity getInstance() {
        return new HomeActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                AppToast.makeShortToast(this, "???????????????????????????");
                isExit = true;
                new Thread() {
                    public void run() {
                        SystemClock.sleep(3000);
                        isExit = false;
                    }

                }.start();
                return true;
            }
            AppManager.getAppManager().finishAllActivity();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
