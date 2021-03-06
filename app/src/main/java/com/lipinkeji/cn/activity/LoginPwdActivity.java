package com.lipinkeji.cn.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.Message;
import com.lipinkeji.cn.util.Y;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginPwdActivity extends BaseActivity {


    @BindView(R.id.tv_pwd)
    TextView tv_pwd;
    @BindView(R.id.ed_pwd)
    EditText ed_pwd;
    @BindView(R.id.tv_pwd_two)
    TextView tv_pwd_two;
    @BindView(R.id.ed_pwd_two)
    EditText ed_pwd_two;
    @BindView(R.id.bt_ok)
    Button bt_ok;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;

    private String sms_id;
    private String sms_code;
    private String mod_id;

    @Override
    public int getContentViewResId() {
        return R.layout.act_login_pwd;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public boolean showToolBar() {
        return false;
    }


    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context, String mod_id) {
        Intent intent = new Intent();
        intent.setClass(context, LoginPwdActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mod_id", mod_id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        sms_id = PreferenceHelper.getInstance(this).getString("SMS_ID", "");
        sms_code = PreferenceHelper.getInstance(this).getString("SMS_CODE", "");
        mod_id = getIntent().getStringExtra("mod_id");
        ed_pwd.setHint("请输入登录密码");
        ed_pwd_two.setHint("请确认登录密码");
    }

    @OnClick({R.id.rl_back, R.id.bt_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.bt_ok:
                clickOk();
                break;
        }
    }

    private void clickOk() {
        String pwd = ed_pwd.getText().toString();
        String pwd_two = ed_pwd_two.getText().toString();

        if (TextUtils.isEmpty(pwd)) {
            Y.t("请输入登录密码");
            return;
        }

        if (TextUtils.isEmpty(pwd_two)) {
            Y.t("请确认登录密码");
            return;
        }

        if (!pwd.equals(pwd_two)) {
            Y.t("两次密码输入不一致");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("code", "03009");
        map.put("key", Urls.key);
        map.put("password", pwd);
        map.put("sms_id", sms_id);
        map.put("sms_code", sms_code);
        Gson gson = new Gson();
        OkGo.<AppResponse<Message.DataBean>>post(Urls.WIT_APP)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Message.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Message.DataBean>> response) {
                        TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_SUCESS, new TishiDialog.TishiDialogListener() {
                            @Override
                            public void onClickCancel(View v, TishiDialog dialog) {

                            }

                            @Override
                            public void onClickConfirm(View v, TishiDialog dialog) {

                            }

                            @Override
                            public void onDismiss(TishiDialog dialog) {
                                finish();
                            }
                        });
                        dialog.setTextContent("恭喜您修改密码成功");
                        dialog.show();
                    }

                    @Override
                    public void onError(Response<AppResponse<Message.DataBean>> response) {
                        Y.tError(response);
                    }
                });
    }
}
