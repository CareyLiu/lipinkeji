package com.lipinkeji.cn.activity.device_shuinuan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.UIHelper;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.DingShiResultModel;
import com.lipinkeji.cn.util.Y;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShuinuanDingshiActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_kaiji_time)
    TextView tvKaijiTime;
    @BindView(R.id.iv_swich_kaiji)
    ImageView ivSwichKaiji;
    @BindView(R.id.ll_kaiji_tiem)
    LinearLayout llKaijiTiem;
    @BindView(R.id.cb_monday)
    CheckBox cbMonday;
    @BindView(R.id.cb_tuesday)
    CheckBox cbTuesday;
    @BindView(R.id.cb_wednesday)
    CheckBox cbWednesday;
    @BindView(R.id.cb_thursday)
    CheckBox cbThursday;
    @BindView(R.id.cb_friday)
    CheckBox cbFriday;
    @BindView(R.id.cb_saturday)
    CheckBox cbSaturday;
    @BindView(R.id.cb_sunday)
    CheckBox cbSunday;
    @BindView(R.id.tv_guanji_time)
    TextView tvGuanjiTime;
    @BindView(R.id.iv_swich_guanji)
    ImageView ivSwichGuanji;
    @BindView(R.id.ll_guanji_time)
    LinearLayout llGuanjiTime;
    @BindView(R.id.cb_monday_guan)
    CheckBox cbMondayGuan;
    @BindView(R.id.cb_tuesday_guan)
    CheckBox cbTuesdayGuan;
    @BindView(R.id.cb_wednesday_guan)
    CheckBox cbWednesdayGuan;
    @BindView(R.id.cb_thursday_guan)
    CheckBox cbThursdayGuan;
    @BindView(R.id.cb_friday_guan)
    CheckBox cbFridayGuan;
    @BindView(R.id.cb_saturday_guan)
    CheckBox cbSaturdayGuan;
    @BindView(R.id.cb_sunday_guan)
    CheckBox cbSundayGuan;
    @BindView(R.id.tv_queren)
    TextView tvQueren;

    private String ccid;
    private String chooseHourKaiji = "00";//小时
    private String chooseMinKaiji = "00";//分钟

    private String chooseHourGuanji = "00";//小时
    private String chooseMinGuanji = "00";//分钟

    private String weekTimes;
    private String jinriShijian;
    private TimePickerView timePickerKaiji;
    private TimePickerView timePickerGuanji;
    private String g_weeks_time;
    private String g_shifen_time;

    private boolean isKaiji;
    private boolean isGuanji;


    @Override
    public int getContentViewResId() {
        return R.layout.a_fengnuan_act_dingshi;
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
        Intent intent = new Intent(context, ShuinuanDingshiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ccid = PreferenceHelper.getInstance(this).getString("ccid", "");
        clickKaiji();
        clickGuanji();
        chaXunDingShi();
    }

    //设置定时
    public void setDingShi() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03205");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("ccid", ccid);
        map.put("type", "1");

        String kaijiTime = getKaijiTime();
        String guanjiTime = getGuanjiTime();

        map.put("time", kaijiTime + guanjiTime);
        Log.i("日期", kaijiTime + guanjiTime);

        Gson gson = new Gson();
        OkGo.<AppResponse>post(Urls.DINGSHI)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse>() {
                    @Override
                    public void onSuccess(final Response<AppResponse> response) {
                        String msg_code = response.body().msg_code;
                        if (msg_code.equals("0000")) {
                            UIHelper.ToastMessage(mContext, "定时成功");
                        } else {
                            Y.t(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse> response) {
                        Y.tError(response);
                    }
                });
    }

    private String getKaijiTime() {

        String strKaiji;
        if (cbSunday.isChecked()) {
            strKaiji = "1";
        } else {
            strKaiji = "0";
        }

        if (cbMonday.isChecked()) {
            strKaiji = strKaiji + "1";
        } else {
            strKaiji = strKaiji + "0";
        }

        if (cbTuesday.isChecked()) {
            strKaiji = strKaiji + "1";
        } else {
            strKaiji = strKaiji + "0";
        }

        if (cbWednesday.isChecked()) {
            strKaiji = strKaiji + "1";
        } else {
            strKaiji = strKaiji + "0";
        }
        if (cbThursday.isChecked()) {
            strKaiji = strKaiji + "1";
        } else {
            strKaiji = strKaiji + "0";
        }

        if (cbFriday.isChecked()) {
            strKaiji = strKaiji + "1";
        } else {
            strKaiji = strKaiji + "0";
        }

        if (cbSaturday.isChecked()) {
            strKaiji = strKaiji + "1";
        } else {
            strKaiji = strKaiji + "0";
        }

        strKaiji = strKaiji + chooseHourKaiji + chooseMinKaiji;

        return strKaiji;
    }

    private String getGuanjiTime() {
        String strGuanji;
        if (cbSundayGuan.isChecked()) {
            strGuanji = "1";
        } else {
            strGuanji = "0";
        }

        if (cbMondayGuan.isChecked()) {
            strGuanji = strGuanji + "1";
        } else {
            strGuanji = strGuanji + "0";
        }

        if (cbTuesdayGuan.isChecked()) {
            strGuanji = strGuanji + "1";
        } else {
            strGuanji = strGuanji + "0";
        }

        if (cbWednesdayGuan.isChecked()) {
            strGuanji = strGuanji + "1";
        } else {
            strGuanji = strGuanji + "0";
        }
        if (cbThursdayGuan.isChecked()) {
            strGuanji = strGuanji + "1";
        } else {
            strGuanji = strGuanji + "0";
        }

        if (cbFridayGuan.isChecked()) {
            strGuanji = strGuanji + "1";
        } else {
            strGuanji = strGuanji + "0";
        }

        if (cbSaturdayGuan.isChecked()) {
            strGuanji = strGuanji + "1";
        } else {
            strGuanji = strGuanji + "0";
        }

        strGuanji = strGuanji + chooseHourGuanji + chooseMinGuanji;

        return strGuanji;
    }


    @Override
    public boolean showToolBar() {
        return false;
    }

    public void chaXunDingShi() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03201");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("ccid", ccid);
        Gson gson = new Gson();
        OkGo.<AppResponse<DingShiResultModel.DataBean>>post(Urls.DINGSHI)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<DingShiResultModel.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<DingShiResultModel.DataBean>> response) {
                        DingShiResultModel.DataBean dataBean = response.body().data.get(0);

                        weekTimes = dataBean.getWeeks_time();
                        jinriShijian = dataBean.getShifen_time();

                        g_weeks_time = dataBean.getG_weeks_time();
                        g_shifen_time = dataBean.getG_shifen_time();

                        setWeekTimeKaiji();
                        setWeekTimeGuanji();
                    }

                    private void setWeekTimeGuanji() {
                        if (g_weeks_time.length() == 7) {
                            if (g_weeks_time.equals("0000000")) {
                                clickGuanji();
                            } else {
                                isGuanji = true;

                                String zhoutian = String.valueOf(g_weeks_time.charAt(0));
                                String zhouyi = String.valueOf(g_weeks_time.charAt(1));
                                String zhouer = String.valueOf(g_weeks_time.charAt(2));
                                String zhousan = String.valueOf(g_weeks_time.charAt(3));
                                String zhousi = String.valueOf(g_weeks_time.charAt(4));
                                String zhouwu = String.valueOf(g_weeks_time.charAt(5));
                                String zhouliu = String.valueOf(g_weeks_time.charAt(6));

                                if (zhoutian.equals("1")) {
                                    cbSundayGuan.setChecked(true);
                                } else {
                                    cbSundayGuan.setChecked(false);
                                }

                                if (zhouyi.equals("1")) {
                                    cbMondayGuan.setChecked(true);
                                } else {
                                    cbMondayGuan.setChecked(false);
                                }

                                if (zhouer.equals("1")) {
                                    cbTuesdayGuan.setChecked(true);
                                } else {
                                    cbTuesdayGuan.setChecked(false);
                                }

                                if (zhousan.equals("1")) {
                                    cbWednesdayGuan.setChecked(true);
                                } else {
                                    cbWednesdayGuan.setChecked(false);
                                }

                                if (zhousi.equals("1")) {
                                    cbThursdayGuan.setChecked(true);
                                } else {
                                    cbThursdayGuan.setChecked(false);
                                }

                                if (zhouwu.equals("1")) {
                                    cbFridayGuan.setChecked(true);
                                } else {
                                    cbFridayGuan.setChecked(false);
                                }

                                if (zhouliu.equals("1")) {
                                    cbSaturdayGuan.setChecked(true);
                                } else {
                                    cbSaturdayGuan.setChecked(false);
                                }

                                if (!TextUtils.isEmpty(g_shifen_time)) {
                                    String[] shijian = g_shifen_time.split(":");
                                    if (shijian.length >= 2) {
                                        chooseHourGuanji = shijian[0];
                                        chooseMinGuanji = shijian[1];
                                    }
                                    tvGuanjiTime.setText(g_shifen_time);
                                }

                                ivSwichGuanji.setImageResource(R.mipmap.wd_btn_kaiqi);
                            }
                        } else {
                            clickGuanji();
                        }
                    }

                    private void setWeekTimeKaiji() {
                        if (weekTimes.length() == 7) {
                            if (weekTimes.equals("0000000")) {
                                clickKaiji();
                            } else {
                                isKaiji = true;

                                String zhoutian = String.valueOf(weekTimes.charAt(0));
                                String zhouyi = String.valueOf(weekTimes.charAt(1));
                                String zhouer = String.valueOf(weekTimes.charAt(2));
                                String zhousan = String.valueOf(weekTimes.charAt(3));
                                String zhousi = String.valueOf(weekTimes.charAt(4));
                                String zhouwu = String.valueOf(weekTimes.charAt(5));
                                String zhouliu = String.valueOf(weekTimes.charAt(6));

                                if (zhoutian.equals("1")) {
                                    cbSunday.setChecked(true);
                                } else {
                                    cbSunday.setChecked(false);
                                }

                                if (zhouyi.equals("1")) {
                                    cbMonday.setChecked(true);
                                } else {
                                    cbMonday.setChecked(false);
                                }

                                if (zhouer.equals("1")) {
                                    cbTuesday.setChecked(true);
                                } else {
                                    cbTuesday.setChecked(false);
                                }

                                if (zhousan.equals("1")) {
                                    cbWednesday.setChecked(true);
                                } else {
                                    cbWednesday.setChecked(false);
                                }

                                if (zhousi.equals("1")) {
                                    cbThursday.setChecked(true);
                                } else {
                                    cbThursday.setChecked(false);
                                }

                                if (zhouwu.equals("1")) {
                                    cbFriday.setChecked(true);
                                } else {
                                    cbFriday.setChecked(false);
                                }

                                if (zhouliu.equals("1")) {
                                    cbSaturday.setChecked(true);
                                } else {
                                    cbSaturday.setChecked(false);
                                }

                                if (!TextUtils.isEmpty(jinriShijian)) {
                                    String[] shijian = jinriShijian.split(":");
                                    if (shijian.length >= 2) {
                                        chooseHourKaiji = shijian[0];
                                        chooseMinKaiji = shijian[1];
                                    }
                                    tvKaijiTime.setText(jinriShijian);
                                }

                                ivSwichKaiji.setImageResource(R.mipmap.wd_btn_kaiqi);
                            }
                        } else {
                            clickKaiji();
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<DingShiResultModel.DataBean>> response) {
                        UIHelper.ToastMessage(mContext, response.body().msg);
                    }
                });
    }

    @OnClick({R.id.rl_back, R.id.iv_swich_kaiji, R.id.ll_kaiji_tiem, R.id.iv_swich_guanji, R.id.ll_guanji_time, R.id.tv_queren})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_swich_kaiji:
                kaijiBt();
                break;
            case R.id.ll_kaiji_tiem:
                selectDataKaiji();
                break;
            case R.id.iv_swich_guanji:
                guanjiBt();
                break;
            case R.id.ll_guanji_time:
                selectDataGuanji();
                break;
            case R.id.tv_queren:
                setDingShi();
                break;
        }
    }

    private void kaijiBt() {
        if (isKaiji) {
            clickKaiji();
        } else {
            clickKaijiKai();
        }
    }

    private void clickKaijiKai() {
        isKaiji = true;
        tvKaijiTime.setText(chooseHourKaiji + ":" + chooseMinKaiji);
        ivSwichKaiji.setImageResource(R.mipmap.wd_btn_kaiqi);
    }

    private void clickKaiji() {
        isKaiji = false;

        cbSunday.setChecked(false);
        cbMonday.setChecked(false);
        cbTuesday.setChecked(false);
        cbWednesday.setChecked(false);
        cbThursday.setChecked(false);
        cbFriday.setChecked(false);
        cbSaturday.setChecked(false);

        chooseHourKaiji = "00";
        chooseMinKaiji = "00";

        tvKaijiTime.setText("--:--");

        ivSwichKaiji.setImageResource(R.mipmap.wd_btn_gianbi);
    }

    private void selectDataKaiji() {
        if (timePickerKaiji == null) {
            //时间选择器
            boolean[] select = {false, false, false, true, true, false};
            timePickerKaiji = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    int hours = date.getHours();
                    if (hours < 10) {
                        chooseHourKaiji = "0" + hours;
                    } else {
                        chooseHourKaiji = "" + hours;
                    }

                    int minutes = date.getMinutes();
                    if (minutes < 10) {
                        chooseMinKaiji = "0" + minutes;
                    } else {
                        chooseMinKaiji = "" + minutes;
                    }

                    clickKaijiKai();
                }
            }).setType(select).build();
        }
        timePickerKaiji.show();
    }

    private void guanjiBt() {
        if (isGuanji) {
            clickGuanji();
        } else {
            clickGuanjiKai();
        }
    }

    private void clickGuanjiKai() {
        isGuanji = true;
        tvGuanjiTime.setText(chooseHourGuanji + ":" + chooseMinGuanji);
        ivSwichGuanji.setImageResource(R.mipmap.wd_btn_kaiqi);
    }

    private void clickGuanji() {
        isGuanji = false;

        cbSundayGuan.setChecked(false);
        cbMondayGuan.setChecked(false);
        cbTuesdayGuan.setChecked(false);
        cbWednesdayGuan.setChecked(false);
        cbThursdayGuan.setChecked(false);
        cbFridayGuan.setChecked(false);
        cbSaturdayGuan.setChecked(false);

        chooseHourGuanji = "00";
        chooseMinGuanji = "00";

        tvGuanjiTime.setText("--:--");

        ivSwichGuanji.setImageResource(R.mipmap.wd_btn_gianbi);
    }

    private void selectDataGuanji() {
        if (timePickerGuanji == null) {
            //时间选择器
            boolean[] select = {false, false, false, true, true, false};
            timePickerGuanji = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    int hours = date.getHours();
                    if (hours < 10) {
                        chooseHourGuanji = "0" + hours;
                    } else {
                        chooseHourGuanji = "" + hours;
                    }

                    int minutes = date.getMinutes();
                    if (minutes < 10) {
                        chooseMinGuanji = "0" + minutes;
                    } else {
                        chooseMinGuanji = "" + minutes;
                    }

                    clickGuanjiKai();
                }
            }).setType(select).build();
        }
        timePickerGuanji.show();
    }
}
