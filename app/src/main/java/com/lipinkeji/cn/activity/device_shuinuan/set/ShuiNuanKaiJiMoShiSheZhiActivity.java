package com.lipinkeji.cn.activity.device_shuinuan.set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.ShuRuInterTwoView;
import com.lipinkeji.cn.activity.ShuRuInterView;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.UIHelper;
import com.lipinkeji.cn.common.StringUtils;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.dialog.TanChuangDialog;
import com.lipinkeji.cn.dialog.TongYongShuRuDIalog;
import com.lipinkeji.cn.dialog.TongYongShuRuDIalog_two;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import butterknife.BindView;

public class ShuiNuanKaiJiMoShiSheZhiActivity extends BaseActivity {

    @BindView(R.id.et_wendu)
    EditText etWendu;
    @BindView(R.id.rl_wendushangxian)
    RelativeLayout rlWendushangxian;
    @BindView(R.id.et_shijian)
    EditText etShijian;
    @BindView(R.id.rl_xianshishezhi)
    RelativeLayout rlXianshishezhi;
    @BindView(R.id.et_hengwenshangxian)
    EditText etHengwenshangxian;
    @BindView(R.id.rl_hengwenshangxian)
    RelativeLayout rlHengwenshangxian;
    @BindView(R.id.et_hengwenxiaxian)
    EditText etHengwenxiaxian;
    @BindView(R.id.rl_hengwenxiaxian)
    RelativeLayout rlHengwenxiaxian;
    @BindView(R.id.ll_hengwengongnengzhankai)
    LinearLayout llHengwengongnengzhankai;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_baocun)
    TextView tvBaocun;
    @BindView(R.id.iv_xianwenkaiguan)
    ImageView ivXianwenkaiguan;
    @BindView(R.id.iv_xianshimoshigongneng)
    ImageView ivXianshimoshigongneng;
    @BindView(R.id.iv_hengwenmoshigongneng)
    ImageView ivHengwenmoshigongneng;


    String moshi = "a";//模式
    String xianwenmoshi = "aa";//限温模式
    String xianshimoshi = "aaa";//限时模式
    String hengwenmoshi_wendushang = "aa";//恒温模式温度上
    String hengwenmoshi_wenduxia = "aa";//恒温目送hi温度下



    TongYongShuRuDIalog_two tongYongShuRuDIalog;

    TongYongShuRuDIalog tongYongShuRuDIalog_one;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ivHengwenmoshigongneng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ivXianwenkaiguan.isSelected() || ivXianshimoshigongneng.isSelected()) {
                    TanChuangDialog tanChuangDialog = new TanChuangDialog(mContext);
                    tanChuangDialog.show();
                    return;
                } else {
                    moshi = "2";

                }

                if (ivHengwenmoshigongneng.isSelected()) {
                    ivHengwenmoshigongneng.setSelected(false);
                    llHengwengongnengzhankai.setVisibility(View.GONE);
                } else {
                    ivHengwenmoshigongneng.setSelected(true);
                    llHengwengongnengzhankai.setVisibility(View.VISIBLE);

                    tongYongShuRuDIalog = new TongYongShuRuDIalog_two(mContext, new ShuRuInterTwoView() {

                        @Override
                        public void cannel() {

                        }

                        @Override
                        public void submit(String str) {

                            etHengwenshangxian.setText(str);
                        }

                        @Override
                        public void submit1(String str) {

                            etHengwenxiaxian.setText(str);
                        }

                        @Override
                        public void baocun() {
                            sendCanShu();
                        }
                    }, "设置恒温温度上限下限");
                    tongYongShuRuDIalog.show();
                }
            }
        });

        etWendu.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                int value = Integer.valueOf(v.getText().toString().trim());

                if (value < 40) {

                    etWendu.setText("40");
                    //return;

                } else if (value > 85) {
                    etWendu.setText("80");
                    //return;
                }
                return false;

            }
        });
        etWendu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s + "";
                if (value != null) {
                    if (!StringUtils.isEmpty(value)) {
                        int value1 = Integer.valueOf(value);
                        if (value1 < 40) {

                            return;
                        } else if (value1 > 85) {

                            return;
                        }
                    } else {

                    }
                } else {

                }
            }
        });

        etWendu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!StringUtils.isEmpty(etWendu.getText().toString())) {
                        int value1 = Integer.valueOf(etWendu.getText().toString().trim());

                        if (value1 < 40) {
                            etWendu.setText("40");
                            return;
                        } else if (value1 > 85) {
                            etWendu.setText("85");
                            return;
                        }
                    } else {
                        etWendu.setText("40");
                    }


                }
            }
        });

        ivXianshimoshigongneng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ivXianwenkaiguan.isSelected() || ivHengwenmoshigongneng.isSelected()) {
                    TanChuangDialog tanChuangDialog = new TanChuangDialog(mContext);
                    tanChuangDialog.show();
                    return;
                } else {
                    moshi = "1";


                }
                if (ivXianshimoshigongneng.isSelected()) {
                    ivXianshimoshigongneng.setSelected(false);
                    rlXianshishezhi.setVisibility(View.GONE);
                } else {
                    ivXianshimoshigongneng.setSelected(true);
                    rlXianshishezhi.setVisibility(View.VISIBLE);

                    tongYongShuRuDIalog_one = new TongYongShuRuDIalog(mContext, new ShuRuInterView() {
                        @Override
                        public void cannel() {

                        }

                        @Override
                        public void submit(String str) {
                            etShijian.setText(str);

                            sendCanShu();
                        }
                    }, "请输入限时时间");

                    tongYongShuRuDIalog_one.show();
                }


            }
        });

        etShijian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s + "";
                if (value != null) {
                    if (!StringUtils.isEmpty(value)) {
                        int value1 = Integer.valueOf(value);
                        if (value1 < 10) {

                            return;
                        } else if (value1 > 120) {

                            return;
                        }
                    } else {

                    }
                } else {

                }
            }
        });

        etShijian.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!StringUtils.isEmpty(etShijian.getText().toString())) {
                        int value1 = Integer.valueOf(etShijian.getText().toString().trim());

                        if (value1 < 10) {
                            etShijian.setText("10");
                            return;
                        } else if (value1 > 120) {
                            etShijian.setText("120");
                            return;
                        }
                    } else {
                        etShijian.setText("10");
                    }


                }
            }
        });
        ivXianwenkaiguan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ivXianshimoshigongneng.isSelected() || ivHengwenmoshigongneng.isSelected()) {
                    TanChuangDialog tanChuangDialog = new TanChuangDialog(mContext);
                    tanChuangDialog.show();
                    return;
                } else {
                    moshi = "0";


                }
                if (ivXianwenkaiguan.isSelected()) {
                    ivXianwenkaiguan.setSelected(false);
                    rlWendushangxian.setVisibility(View.GONE);
                } else {
                    ivXianwenkaiguan.setSelected(true);
                    rlWendushangxian.setVisibility(View.VISIBLE);

                    tongYongShuRuDIalog_one = new TongYongShuRuDIalog(mContext, new ShuRuInterView() {
                        @Override
                        public void cannel() {

                        }

                        @Override
                        public void submit(String str) {
                            etWendu.setText(str);

                            sendCanShu();
                        }
                    }, "请输入限制的温度");

                    tongYongShuRuDIalog_one.show();
                }

            }
        });

        etHengwenshangxian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s + "";
                if (value != null) {
                    if (!StringUtils.isEmpty(value)) {
                        int value1 = Integer.valueOf(value);
                        if (value1 < 35) {

                            return;
                        } else if (value1 > 85) {

                            return;
                        }
                    } else {

                    }
                } else {

                }
            }
        });

        etHengwenshangxian.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!StringUtils.isEmpty(etHengwenshangxian.getText().toString())) {
                        int value1 = Integer.valueOf(etHengwenshangxian.getText().toString().trim());

                        if (value1 < 35) {
                            etHengwenshangxian.setText("35");
                            return;
                        } else if (value1 > 85) {
                            etHengwenshangxian.setText("85");
                            return;
                        }
                    } else {
                        etHengwenshangxian.setText("35");
                    }

                }
            }
        });

        etHengwenxiaxian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s + "";
                if (value != null) {
                    if (!StringUtils.isEmpty(value)) {
                        int value1 = Integer.valueOf(value);
                        if (value1 < 5) {

                            return;
                        } else if (value1 > 75) {

                            return;
                        }
                    } else {

                    }
                } else {

                }
            }
        });

        etHengwenxiaxian.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!StringUtils.isEmpty(etHengwenxiaxian.getText().toString())) {
                        int value1 = Integer.valueOf(etHengwenxiaxian.getText().toString().trim());

                        if (value1 < 40) {
                            etHengwenxiaxian.setText("5");
                            return;
                        } else if (value1 > 85) {
                            etHengwenxiaxian.setText("75");
                            return;
                        }
                    } else {
                        etHengwenxiaxian.setText("5");
                    }


                }
            }
        });
    }

    String ccid = PreferenceHelper.getInstance(mContext).getString("ccid", "");
    String topic = "wh/app/" + ccid;

    public void sendCanShu() {
        xianwenmoshi = "aa";//限温模式
        xianshimoshi = "aaa";//限时模式
        hengwenmoshi_wendushang = "aa";//恒温模式温度上
        hengwenmoshi_wenduxia = "aa";//恒温目送温度下


        if (moshi.equals("0")) {

            xianwenmoshi = etWendu.getText().toString().trim();
        } else if (moshi.equals("1")) {

            xianshimoshi = etShijian.getText().toString().trim();
        } else if (moshi.equals("2")) {

            hengwenmoshi_wendushang = etHengwenshangxian.getText().toString().trim();
            hengwenmoshi_wenduxia = etHengwenxiaxian.getText().toString().trim();

        }
        String zhiling = "a_s" + moshi + xianwenmoshi + xianshimoshi + hengwenmoshi_wendushang + hengwenmoshi_wenduxia;


        MqttPublish mqttPublish = new MqttPublish();
        mqttPublish.setQos(2);
        mqttPublish.setMsg(zhiling);
        mqttPublish.setTopic(topic);
        mqttPublish.setRetained(false);

//                AndMqtt.getInstance().publish(mqttPublish, new IMqttActionListener() {
//                    @Override
//                    public void onSuccess(IMqttToken asyncActionToken) {
//
//                    }
//
//                    @Override
//                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//
//                    }
//                });

        Log.i("rair_zhiling", zhiling);
    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_shuinuan_kaijishezhi;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    /**
     * 用于其他Activty跳转到该Activity
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShuiNuanKaiJiMoShiSheZhiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
