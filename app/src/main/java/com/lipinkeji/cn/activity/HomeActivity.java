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
            //初始化client
            locationClient = new AMapLocationClient(this);
            locationOption = getDefaultOption();
            //设置定位参数
            locationClient.setLocationOption(locationOption);
            // 设置定位监听
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
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        //根据控件的选择，重新设置定位参数
        //resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener gaodeDingWeiListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    sb.append("提供者    : " + location.getProvider() + "\n");

                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + location.getBearing() + "\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : " + location.getSatellites() + "\n");
                    sb.append("国    家    : " + location.getCountry() + "\n");
                    sb.append("省            : " + location.getProvince() + "\n");
                    sb.append("市            : " + location.getCity() + "\n");
                    sb.append("城市编码 : " + location.getCityCode() + "\n");
                    sb.append("区            : " + location.getDistrict() + "\n");
                    sb.append("区域 码   : " + location.getAdCode() + "\n");
                    sb.append("地    址    : " + location.getAddress() + "\n");
                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
                    //定位完成的时间
                    sb.append("定位时间: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");


                    PreferenceHelper.getInstance(mContext).putString("WEIDU", location.getLatitude() + "");
                    PreferenceHelper.getInstance(mContext).putString("JINGDU", location.getLongitude() + "");
                    stopLocation();
                    locationClient.stopAssistantLocation();
                    gaodeDingWeiListener = null;

                } else {


                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                sb.append("***定位质量报告***").append("\n");
                sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
                sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
                sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
                sb.append("* 网络类型：" + location.getLocationQualityReport().getNetworkType()).append("\n");
                sb.append("* 网络耗时：" + location.getLocationQualityReport().getNetUseTime()).append("\n");
                sb.append("****************").append("\n");
                //定位之后的回调时间
                sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //解析定位结果，
                String result = sb.toString();
                Log.i("Location_result", result);
            } else {

                Log.i("Location_result", "定位失败");
            }

        }
    };

    /**
     * 获取GPS状态的字符串
     *
     * @param statusCode GPS状态码
     * @return
     */
    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
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
                //要做的事情
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

                myCarCaoZuoDialog_notify.setTextTitle("故障推送");
                myCarCaoZuoDialog_notify.setTextContent("您的加热器出现故障，请及时处理!");
                myCarCaoZuoDialog_notify.setTextCancel("忽略");

                Y.e("放假啦空手道解放大师傅但是");
                // myCarCaoZuoDialog_notify.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG);
                myCarCaoZuoDialog_notify.show();
                Y.e("罚款决定书分厘卡士大夫大师傅但是扩大寄生蜂");
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
                AppToast.makeShortToast(this, "再按一次返回键退出");
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
