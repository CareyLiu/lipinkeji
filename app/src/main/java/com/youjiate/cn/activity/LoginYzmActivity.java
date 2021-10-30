package com.youjiate.cn.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.youjiate.cn.R;
import com.youjiate.cn.activity.shuinuan.Y;
import com.youjiate.cn.app.BaseActivity;
import com.youjiate.cn.callback.JsonCallback;
import com.youjiate.cn.config.AppResponse;
import com.youjiate.cn.config.PreferenceHelper;
import com.youjiate.cn.get_net.Urls;
import com.youjiate.cn.model.Message;
import com.youjiate.cn.util.TimeCount;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginYzmActivity extends BaseActivity {


    @BindView(R.id.ed_phone)
    EditText ed_phone;
    @BindView(R.id.ed_code)
    EditText ed_code;
    @BindView(R.id.tv_yzm)
    TextView tv_yzm;
    @BindView(R.id.bt_ok)
    Button bt_ok;

    private TimeCount timeCount;
    private String smsId;//短信验证码id

    @Override
    public int getContentViewResId() {
        return R.layout.act_login_yzm;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.black);
        mImmersionBar.init();
        mImmersionBar.statusBarDarkFont(true);
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        mToolbar.setBackgroundColor(Y.getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.back_white);
        tv_title.setTextColor(Y.getColor(R.color.text_blue));
        tv_title.setText("手机验证");

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginYzmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        timeCount = new TimeCount(60000, 1000, tv_yzm);
    }

    @OnClick({R.id.tv_yzm, R.id.bt_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_yzm:
                getCode();
                break;
            case R.id.bt_ok:
                requestData();
                break;
        }
    }


    /**
     * 获取短信验证码
     */
    private void getCode() {
        if (TextUtils.isEmpty(ed_phone.getText().toString())) {
            Y.t("请输入手机号");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("code", "00001");
        map.put("key", Urls.key);
        map.put("user_phone", ed_phone.getText().toString());
        map.put("mod_id", "0354");//法拉尔修改密码
        Gson gson = new Gson();
        OkGo.<AppResponse<Message.DataBean>>post(Urls.MSG)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Message.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Message.DataBean>> response) {
                        Y.t("验证码发送成功");
                        timeCount.start();
                        smsId = response.body().data.get(0).getSms_id();
                    }

                    @Override
                    public void onError(Response<AppResponse<Message.DataBean>> response) {
                        Y.tError(response);
                        timeCount.cancel();
                        timeCount.onFinish();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 提交验证
     */
    private void requestData() {
        String sms_code = ed_code.getText().toString();
        if (TextUtils.isEmpty(sms_code)) {
            Y.t("请输入验证码");
            return;
        }

        if (TextUtils.isEmpty(smsId)) {
            Y.t("请发送验证码");
            return;
        }

        PreferenceHelper.getInstance(mContext).putString("SMS_ID", smsId);
        PreferenceHelper.getInstance(mContext).putString("SMS_CODE", sms_code);
        LoginPwdActivity.actionStart(mContext, "0001");
        finish();
    }
}