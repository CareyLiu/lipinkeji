package com.falaer.cn.activity.device_ty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.falaer.cn.R;
import com.falaer.cn.activity.FengnuanJieActivity;
import com.falaer.cn.activity.shuinuan.Y;
import com.falaer.cn.app.BaseActivity;
import com.falaer.cn.app.ConstanceValue;
import com.falaer.cn.app.Notice;
import com.falaer.cn.app.RxBus;
import com.falaer.cn.callback.JsonCallback;
import com.falaer.cn.config.AppResponse;
import com.falaer.cn.config.PreferenceHelper;
import com.falaer.cn.config.UserManager;
import com.falaer.cn.get_net.Urls;
import com.falaer.cn.model.Message;
import com.falaer.cn.util.TimeCount;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TongyongJiebangActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.tv_shebeima)
    TextView tv_shebeima;
    @BindView(R.id.ed_pwd)
    EditText ed_pwd;
    @BindView(R.id.tv_send_code)
    TextView tv_send_code;
    @BindView(R.id.ll_jiebang)
    LinearLayout ll_jiebang;

    private TimeCount timeCount;
    private String user_phone;
    private String ccid;
    private String smsId;


    @Override
    public boolean showToolBar() {
        return false;
    }

    @Override
    public int getContentViewResId() {
        return R.layout.a_tongyong_act_jiebang;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, TongyongJiebangActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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
        user_phone = PreferenceHelper.getInstance(mContext).getString("user_phone", "");
        ccid = PreferenceHelper.getInstance(this).getString("ccid", "");
        tv_shebeima.setText(ccid);
        timeCount = new TimeCount(60000, 1000, tv_send_code,1);
    }

    private void jiebang() {
        String code = ed_pwd.getText().toString();

        if (StringUtils.isEmpty(smsId)) {
            Y.t("请获取验证码");
            return;
        }

        if (StringUtils.isEmpty(code)) {
            Y.t("请输入验证码");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("code", "03204");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("ccid", ccid);
        map.put("sms_id", smsId);
        map.put("sms_code", code);
        map.put("is_platform", PreferenceHelper.getInstance(mContext).getString("is_platform_bendi", ""));
        Gson gson = new Gson();
        OkGo.<AppResponse<Message.DataBean>>post(Urls.SERVER_URL + "fn/common")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Message.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Message.DataBean>> response) {
                        Y.t("解绑成功");
                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_JIEBANG;
                        RxBus.getDefault().sendRx(n);
                        finish();
                    }

                    @Override
                    public void onError(Response<AppResponse<Message.DataBean>> response) {
                        Y.tError(response);
                    }
                });

    }

    private void get_code() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "00001");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("user_phone", user_phone);
        map.put("mod_id", "0344");
        Gson gson = new Gson();
        OkGo.<AppResponse<Message.DataBean>>post(Urls.SERVER_URL + "msg")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Message.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Message.DataBean>> response) {
                        Y.t("验证码获取成功");
                        timeCount.start();
                        if (response.body().data.size() > 0) {
                            smsId = response.body().data.get(0).getSms_id();
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<Message.DataBean>> response) {
                        timeCount.start();
                        Y.tError(response);
                    }
                });
    }

    @OnClick({R.id.rl_back, R.id.tv_send_code, R.id.ll_jiebang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_send_code:
                get_code();
                break;
            case R.id.ll_jiebang:
                jiebang();
                break;
        }
    }
}
