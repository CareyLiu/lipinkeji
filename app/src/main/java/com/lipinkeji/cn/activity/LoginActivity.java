package com.lipinkeji.cn.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lipinkeji.cn.R;
import com.lipinkeji.cn.util.Y;
import com.lipinkeji.cn.app.AppConfig;
import com.lipinkeji.cn.app.AppManager;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.app.RxBus;
import com.lipinkeji.cn.app.UIHelper;
import com.lipinkeji.cn.callback.DialogCallback;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.common.StringUtils;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.dialog.FuWuDialog;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.LoginUser;
import com.lipinkeji.cn.model.Message;
import com.lipinkeji.cn.util.TimeCount;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jakewharton.rxbinding.view.RxView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import pub.devrel.easypermissions.EasyPermissions;
import rx.functions.Action1;

import static com.lipinkeji.cn.get_net.Urls.SERVER_URL;


public class LoginActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ed_phone)
    EditText mEtPhone;
    @BindView(R.id.ed_pwd)
    EditText mEtPwdCode;
    @BindView(R.id.get_code)
    TextView mTvGetCode;
    @BindView(R.id.ll_pwd)
    LinearLayout llPwd;
    @BindView(R.id.tv_switch)
    TextView tv_switch;
    @BindView(R.id.tv_zhaohui)
    TextView tvZhaohui;
    @BindView(R.id.ll_qiehuan)
    LinearLayout llQiehuan;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.tv_yinsi)
    TextView tvYinsi;
    @BindView(R.id.tv_yonghu)
    TextView tvYonghushiyong;
    @BindView(R.id.iv_tongyi)
    ImageView ivTongyi;
    @BindView(R.id.ll_tongyi)
    LinearLayout llTongyi;

    private boolean isExit;
    private TimeCount timeCount;
    private String req_type = "2";
    private String smsId;
    private FuWuDialog fuWuDialog;

    public static List<LoginUser.DataBean> userlist = new ArrayList<>();
    private Response<AppResponse<LoginUser.DataBean>> response;
    private String shifoutongyi = "0";//???????????????

    @Override
    public int getContentViewResId() {
        return R.layout.act_login;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
        mImmersionBar.statusBarDarkFont(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
        ivTongyi.setBackgroundResource(R.mipmap.login_select_nor);
        llTongyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shifoutongyi.equals("0")) {
                    ivTongyi.setBackgroundResource(R.mipmap.login_select_sel);
                    shifoutongyi = "1";
                } else {
                    ivTongyi.setBackgroundResource(R.mipmap.login_select_nor);
                    shifoutongyi = "0";
                }
            }
        });

        mTvGetCode.setVisibility(View.VISIBLE);
        mEtPwdCode.setHint("??????????????????");
        mEtPwdCode.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        req_type = "2";

//        mTvGetCode.setVisibility(View.GONE);
//        mEtPwdCode.setHint("?????????????????????");
//        mEtPwdCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
//        req_type = "1";
    }

    private void init() {
        timeCount = new TimeCount(60000, 1000, mTvGetCode);
        mEtPhone.setText(PreferenceHelper.getInstance(this).getString("user_phone", ""));
        String strToken = getIntent().getStringExtra("token_guoqi");
        if (strToken != null) {
            TishiDialog tishiDialog = new TishiDialog(LoginActivity.this, 3, new TishiDialog.TishiDialogListener() {
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
            tishiDialog.setTextCancel("");
            tishiDialog.setTextConfirm("?????????");
            tishiDialog.setTextContent("????????????????????????????????????????????????????????????????????????????????????");
            tishiDialog.show();
        }

        RxView.clicks(mTvGetCode)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        get_code();
                    }
                });

        tvYonghushiyong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DefaultX5WebViewActivity.actionStart(LoginActivity.this, "https://mqrn.hljsdkj.com/shop_new/lp_user_agreements");
            }
        });
        tvYinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DefaultX5WebViewActivity.actionStart(LoginActivity.this, "https://mqrn.hljsdkj.com/shop_new/lp_privacy_clause");
            }
        });

        String str = PreferenceHelper.getInstance(this).getString(AppConfig.TANCHUFUWUTANKUANG, "");

        if (StringUtils.isEmpty(str)) {
            fuWuDialog = new FuWuDialog(mContext, new FuWuDialog.FuWuDiaLogClikListener() {
                @Override
                public void onClickCancel() {

                }

                @Override
                public void onClickConfirm() {

                    String[] perms = {
                            Manifest.permission.WRITE_SETTINGS,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    EasyPermissions.requestPermissions(LoginActivity.this, "????????????app???????????????", 0, perms);
                    fuWuDialog.dismiss();
                }

                @Override
                public void onDismiss(FuWuDialog dialog) {

                }

                @Override
                public void fuwu() {
                    DefaultX5WebViewActivity.actionStart(LoginActivity.this, "https://mqrn.hljsdkj.com/shop_new/lp_user_agreements");
                }

                @Override
                public void yinsixieyi() {
                    DefaultX5WebViewActivity.actionStart(LoginActivity.this, "https://mqrn.hljsdkj.com/shop_new/lp_privacy_clause");
                }
            });

            fuWuDialog.setCancelable(false);
            fuWuDialog.show();

        }
    }

    // ??????????????????????????????????????????
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                UIHelper.ToastMessage(this, "???????????????????????????");
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

    @OnClick({R.id.tv_switch, R.id.bt_login, R.id.tv_zhaohui})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_zhaohui:
                LoginYzmActivity.actionStart(this);
                break;
            case R.id.tv_switch:
                String items[] = {getString(R.string.sms_login), getString(R.string.pwd_login)};
                final ActionSheetDialog dialog = new ActionSheetDialog(this, items, null);
                dialog.isTitleShow(false).show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mTvGetCode.setVisibility(View.VISIBLE);
                                mEtPwdCode.setHint("??????????????????");
                                mEtPwdCode.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                req_type = "2";
                                break;
                            case 1:
                                mTvGetCode.setVisibility(View.GONE);
                                mEtPwdCode.setHint("?????????????????????");
                                mEtPwdCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                req_type = "1";
                                break;
                        }
                        dialog.dismiss();

                    }
                });

                break;
            case R.id.bt_login:
                if (shifoutongyi.equals("0")) {
                    UIHelper.ToastMessage(mContext, "?????????????????????????????????????????????????????????");
                    return;
                }
                beforehand_login();
                break;
        }
    }

    /**
     * ?????????????????????
     */
    private void get_code() {
        if (mEtPhone.getText().equals("") || mEtPhone.getText() == null) {
            UIHelper.ToastMessage(this, "????????????????????????");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("code", "00001");
            map.put("key", Urls.key);
            map.put("user_phone", mEtPhone.getText().toString());
            map.put("mod_id", "0367");
            Gson gson = new Gson();
            OkGo.<AppResponse<Message.DataBean>>post(SERVER_URL + "msg")
                    .tag(this)//
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<AppResponse<Message.DataBean>>() {
                        @Override
                        public void onSuccess(Response<AppResponse<Message.DataBean>> response) {
                            Y.t("?????????????????????");
                            timeCount.start();
                            if (response.body().data.size() > 0)
                                smsId = response.body().data.get(0).getSms_id();
                        }

                        @Override
                        public void onError(Response<AppResponse<Message.DataBean>> response) {
                            Y.tError(response);
                            timeCount.cancel();
                            timeCount.onFinish();
                        }
                    });
        }
    }

    /**
     * ?????????
     */
    private void beforehand_login() {
        if (mEtPhone.getText().equals("")) {
            UIHelper.ToastMessage(this, "????????????????????????");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("code", "00075");
            map.put("key", Urls.key);
            map.put("req_type", req_type);
            map.put("user_phone", mEtPhone.getText().toString());
            map.put("log_type", "1");
            switch (req_type) {
                case "1"://????????????
                    map.put("user_phone", mEtPhone.getText().toString());
                    map.put("user_pwd", mEtPwdCode.getText().toString());
                    break;
                case "2"://?????????????????????
                    map.put("sms_id", smsId);
                    map.put("sms_code", mEtPwdCode.getText().toString());
                    break;
            }

            Gson gson = new Gson();
            OkGo.<AppResponse<LoginUser.DataBean>>post(SERVER_URL + "index/login")
                    .tag(this)//
                    .upJson(gson.toJson(map))
                    .execute(new DialogCallback<AppResponse<LoginUser.DataBean>>(this) {
                        @Override
                        public void onSuccess(Response<AppResponse<LoginUser.DataBean>> response) {
                            userlist.clear();
                            LoginActivity.this.response = response;
                            loginXiayibu();
                        }

                        @Override
                        public void onError(Response<AppResponse<LoginUser.DataBean>> response) {
                            Y.tError(response);
                        }
                    });
        }
    }

    private void loginXiayibu() {
        //????????????????????????
        PreferenceHelper.getInstance(LoginActivity.this).putString("user_phone", mEtPhone.getText().toString() + "");

        String accid = LoginActivity.this.response.body().data.get(0).getAccid();
        JPushInterface.setAlias(mContext, 0, accid);
        Set<String> tags = new HashSet<>();
        tags.add(accid);
        JPushInterface.setTags(mContext, 0, tags);

        if (response.body().data.size() == 1) {
            //????????????????????????<=1???????????????
            UserManager.getManager(LoginActivity.this).saveUser(LoginActivity.this.response.body().data.get(0));
            if (response.body().data.get(0).getPower_state().equals("1")) {
                UserManager.getManager(LoginActivity.this).saveUser(response.body().data.get(0));
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                //??????mqtt
                Notice n = new Notice();
                n.type = ConstanceValue.MSG_CONNET_MQTT;
                RxBus.getDefault().sendRx(n);
                finish();
            } else {
                UserManager.getManager(LoginActivity.this).saveUser(response.body().data.get(0));
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                //??????mqtt
                Notice n = new Notice();
                n.type = ConstanceValue.MSG_CONNET_MQTT;
                RxBus.getDefault().sendRx(n);
                finish();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (fuWuDialog.isShowing()) {
            fuWuDialog.dismiss();
        }
        PreferenceHelper.getInstance(this).putString(AppConfig.TANCHUFUWUTANKUANG, "1");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void goHuaWeiMainager() {
        try {
            Intent intent = new Intent("com.lipinkeji.cn");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext, "????????????", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            goIntentSetting();
        }
    }

    private void goIntentSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        try {
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
