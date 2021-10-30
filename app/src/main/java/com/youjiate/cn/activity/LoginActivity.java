package com.youjiate.cn.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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

import com.youjiate.cn.R;
import com.youjiate.cn.activity.shuinuan.Y;
import com.youjiate.cn.app.AppConfig;
import com.youjiate.cn.app.AppManager;
import com.youjiate.cn.app.BaseActivity;
import com.youjiate.cn.app.ConstanceValue;
import com.youjiate.cn.app.Notice;
import com.youjiate.cn.app.RxBus;
import com.youjiate.cn.app.UIHelper;
import com.youjiate.cn.callback.DialogCallback;
import com.youjiate.cn.callback.JsonCallback;
import com.youjiate.cn.common.StringUtils;
import com.youjiate.cn.config.AppResponse;
import com.youjiate.cn.config.PreferenceHelper;
import com.youjiate.cn.config.UserManager;
import com.youjiate.cn.dialog.FuWuDialog;
import com.youjiate.cn.dialog.newdia.TishiDialog;
import com.youjiate.cn.get_net.Urls;
import com.youjiate.cn.model.LoginUser;
import com.youjiate.cn.model.Message;
import com.youjiate.cn.util.TimeCount;
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

import static com.youjiate.cn.get_net.Urls.SERVER_URL;


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
    private String shifoutongyi = "0";//默认不同意

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
        ivTongyi.setBackgroundResource(R.mipmap.gouxuankuang_1);
        llTongyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shifoutongyi.equals("0")) {
                    ivTongyi.setBackgroundResource(R.mipmap.tuya_faxian_icon_selector_sel_1);
                    shifoutongyi = "1";
                } else {
                    ivTongyi.setBackgroundResource(R.mipmap.gouxuankuang_1);
                    shifoutongyi = "0";
                }

            }
        });
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
            tishiDialog.setTextConfirm("知道了");
            tishiDialog.setTextContent("您的账号近期在其他设备登录，如非本人操作，请及时修改密码");
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
                DefaultX5WebViewActivity.actionStart(LoginActivity.this, "https://shop.hljsdkj.com/shop_new/f_user_agreements");
            }
        });
        tvYinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DefaultX5WebViewActivity.actionStart(LoginActivity.this, "https://shop.hljsdkj.com/shop_new/f_privacy_clause");
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
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    EasyPermissions.requestPermissions(LoginActivity.this, "申请开启app需要的权限", 0, perms);
                    fuWuDialog.dismiss();
                }

                @Override
                public void onDismiss(FuWuDialog dialog) {

                }

                @Override
                public void fuwu() {
                    DefaultX5WebViewActivity.actionStart(LoginActivity.this, "https://shop.hljsdkj.com/shop_new/f_user_agreements");
                }

                @Override
                public void yinsixieyi() {
                    DefaultX5WebViewActivity.actionStart(LoginActivity.this, "https://shop.hljsdkj.com/shop_new/f_privacy_clause");
                }
            });

            fuWuDialog.setCancelable(false);
            fuWuDialog.show();

        }
    }

    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                UIHelper.ToastMessage(this, "再按一次返回键退出");
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
                                mEtPwdCode.setHint("请输入验证码");
                                mEtPwdCode.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                req_type = "2";
                                break;
                            case 1:
                                mTvGetCode.setVisibility(View.GONE);
                                mEtPwdCode.setHint("请输入登录密码");
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
                    UIHelper.ToastMessage(mContext, "请您先点击勾选同意按钮，再进行后续操作");
                    return;
                }
                beforehand_login();
                break;
        }
    }

    /**
     * 获取短信验证码
     */
    private void get_code() {
        if (mEtPhone.getText().equals("") || mEtPhone.getText() == null) {
            UIHelper.ToastMessage(this, "手机号码不能为空");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("code", "00001");
            map.put("key", Urls.key);
            map.put("user_phone", mEtPhone.getText().toString());
            map.put("mod_id", "0355");//法拉尔登录
            Gson gson = new Gson();
            OkGo.<AppResponse<Message.DataBean>>post(SERVER_URL + "msg")
                    .tag(this)//
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<AppResponse<Message.DataBean>>() {
                        @Override
                        public void onSuccess(Response<AppResponse<Message.DataBean>> response) {
                            Y.t("验证码获取成功");
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
     * 预登陆
     */
    private void beforehand_login() {
        if (mEtPhone.getText().equals("")) {
            UIHelper.ToastMessage(this, "手机号码不能为空");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("code", "00065");
            map.put("key", Urls.key);
            map.put("req_type", req_type);
            map.put("user_phone", mEtPhone.getText().toString());
            map.put("log_type", "1");
            switch (req_type) {
                case "1"://密码登录
                    map.put("user_phone", mEtPhone.getText().toString());
                    map.put("user_pwd", mEtPwdCode.getText().toString());
                    break;
                case "2"://手机验证码登录
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
        //保存用户手机号码
        PreferenceHelper.getInstance(LoginActivity.this).putString("user_phone", mEtPhone.getText().toString() + "");

        String accid = LoginActivity.this.response.body().data.get(0).getAccid();
        JPushInterface.setAlias(mContext, 0, accid);
        Set<String> tags = new HashSet<>();
        tags.add(accid);
        JPushInterface.setTags(mContext, 0, tags);

        if (response.body().data.size() == 1) {
            //如果登录角色数量<=1则直接登录
            UserManager.getManager(LoginActivity.this).saveUser(LoginActivity.this.response.body().data.get(0));
            if (response.body().data.get(0).getPower_state().equals("1")) {
                UserManager.getManager(LoginActivity.this).saveUser(response.body().data.get(0));
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                //重连mqtt
                Notice n = new Notice();
                n.type = ConstanceValue.MSG_CONNET_MQTT;
                RxBus.getDefault().sendRx(n);
                finish();
            } else {
                UserManager.getManager(LoginActivity.this).saveUser(response.body().data.get(0));
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                //重连mqtt
                Notice n = new Notice();
                n.type = ConstanceValue.MSG_CONNET_MQTT;
                RxBus.getDefault().sendRx(n);
                finish();
            }
        }

//        String rongYunTouken = UserManager.getManager(mContext).getRongYun();
//        if (!StringUtils.isEmpty(rongYunTouken)) {
//            Notice notice = new Notice();
//            notice.type = ConstanceValue.MSG_RONGYUN_CHONGZHI;
//            RxBus.getDefault().sendRx(notice);
//        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.i("LoginActivity_xx", "通过了......");
        if (fuWuDialog.isShowing()) {
            fuWuDialog.dismiss();
        }
        PreferenceHelper.getInstance(this).putString(AppConfig.TANCHUFUWUTANKUANG, "1");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //UIHelper.ToastMessage(mContext, "拒绝了");
        Log.i("LoginActivity_xx", "拒绝了......");
        // fuWuDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            Log.i("LoginActivity_xx", "onRequestPermissionsResult granted");
//        } else {
//            Log.i("LoginActivity_xx", "onRequestPermissionsResult denied");
//            //弹出框 让用户去应用详情页手动设置权限
//
//            showWaringDialog();
//            return;
//        }

        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void showWaringDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage("请前往设置->应用->PermissionDemo->权限中打开相关权限，否则功能无法正常运行！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goHuaWeiMainager();
                    }

                }).show();
    }

    private void goHuaWeiMainager() {
        try {
            Intent intent = new Intent("com.youjiate.cn");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext, "跳转失败", Toast.LENGTH_LONG).show();
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