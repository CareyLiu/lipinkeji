package com.youjiate.cn.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.youjiate.cn.callback.DialogCallback;
import com.youjiate.cn.model.LoginUser;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.youjiate.cn.app.UIHelper;
import com.youjiate.cn.model.FenLeiThirdModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.youjiate.cn.R;
import com.youjiate.cn.app.App;
import com.youjiate.cn.app.AppConfig;
import com.youjiate.cn.app.ConstanceValue;
import com.youjiate.cn.app.Notice;
import com.youjiate.cn.app.RxBus;
import com.youjiate.cn.callback.JsonCallback;
import com.youjiate.cn.config.AppEvent;
import com.youjiate.cn.config.AppResponse;
import com.youjiate.cn.config.MyApplication;
import com.youjiate.cn.config.PreferenceHelper;
import com.youjiate.cn.config.UserManager;
import com.youjiate.cn.dialog.MyCarCaoZuoDialog_CaoZuo_Base;
import com.youjiate.cn.get_net.Urls;
import com.youjiate.cn.model.Upload;
import com.youjiate.cn.model.UserInfo;
import com.youjiate.cn.util.AlertUtil;
import com.youjiate.cn.util.CleanMessageUtil;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;
import org.jaaksi.pickerview.picker.TimePicker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.youjiate.cn.get_net.Urls.HOME_PICTURE_HOME;
import static com.youjiate.cn.get_net.Urls.SERVER_URL;

public class SettingActivity extends BaseActivity implements Observer, TakePhoto.TakeResultListener, InvokeListener {
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.layout_header)
    LinearLayout layoutHeader;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.layout_nickname)
    LinearLayout layoutNickname;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.layout_gender)
    LinearLayout layoutGender;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.layout_birthday)
    LinearLayout layoutBirthday;
    @BindView(R.id.layout_address)
    LinearLayout layoutAddress;
    @BindView(R.id.layout_cash_account)
    LinearLayout layoutCashAccount;
    @BindView(R.id.layout_login_password)
    LinearLayout layoutLoginPassword;
    @BindView(R.id.layout_pay_password)
    LinearLayout layoutPayPassword;
    @BindView(R.id.layout_clear_cache)
    LinearLayout layoutClearCache;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    private static final String TAG = "SettingActivity";
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.shebei_peiwang)
    LinearLayout shebeiPeiwang;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_zhifubao_ming)
    TextView tvZhifubaoMing;
    @BindView(R.id.tv_weixin_ming)
    TextView tvWeixinMing;
    @BindView(R.id.ll_shezhi_weixin)
    LinearLayout llShezhiWeixin;
    private BaseAnimatorSet mBasIn = new BounceBottomEnter();
    private BaseAnimatorSet mBasOut = new SlideBottomExit();
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private NormalDialog normalDialog;
    private OptionsPickerView pvOptions;
    private TimePicker timePicker;
    private int index = 0;

    private String yongHuMing = "";
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        aSwitch = findViewById(R.id.shangchegn_switch);


        String str = PreferenceHelper.getInstance(SettingActivity.this).getString(App.ZHIDISHANGCHEGN, "1");
        if (str.equals("1")) {
            aSwitch.setChecked(true);
        } else if (str.equals("2")) {
            aSwitch.setChecked(false);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setNet("1");
                } else {
                    setNet("2");
                }
            }
        });
        ButterKnife.bind(this);
        StatusBarUtil.setLightMode(this);
        getTakePhoto().onCreate(savedInstanceState);
        requestData();
        AppEvent.getClassEvent().addObserver(this);
        try {
            tvCache.setText(CleanMessageUtil.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //1显示 2隐藏
    private void setNet(String str) {
        //访问网络获取数据 下面的列表数据
        Map<String, String> map = new HashMap<>();
        map.put("code", "04269");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(SettingActivity.this).getAppToken());
        map.put("csdh_state", str);
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<FenLeiThirdModel.DataBean>>post(HOME_PICTURE_HOME)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<FenLeiThirdModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<FenLeiThirdModel.DataBean>> response) {
                        if (str.equals("1")) {
                            UIHelper.ToastMessage(SettingActivity.this, "法拉尔商城已开启");
                            PreferenceHelper.getInstance(SettingActivity.this).putString(App.ZHIDISHANGCHEGN, "1");

                            Notice n = new Notice();
                            n.type = ConstanceValue.MSG_HAVE_HOMEFRAGMETN;
                            //  n.content = message.toString();
                            RxBus.getDefault().sendRx(n);
                        } else if (str.equals("2")) {
                            UIHelper.ToastMessage(SettingActivity.this, "法拉尔商城已关闭");
                            PreferenceHelper.getInstance(SettingActivity.this).putString(App.ZHIDISHANGCHEGN, "2");


                            Notice n = new Notice();
                            n.type = ConstanceValue.MSG_NONE_HOMEFRAGMENT;
                            //  n.content = message.toString();
                            RxBus.getDefault().sendRx(n);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    public void requestData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04201");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        Gson gson = new Gson();
        OkGo.<AppResponse<UserInfo.DataBean>>post(Urls.SERVER_URL + "shop_new/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<UserInfo.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<UserInfo.DataBean>> response) {
                        Glide.with(MyApplication.getAppContext()).load(response.body().data.get(0).getUser_img_url()).into(ivHeader);
                        tvNickname.setText(response.body().data.get(0).getUser_name());
                        tvBirthday.setText(response.body().data.get(0).getUser_birthday());

                        if (response.body().data.get(0).getUser_sex().equals("1")) {
                            tvGender.setText("男");
                        } else {
                            tvGender.setText("女");
                        }
                        tvZhifubaoMing.setText(response.body().data.get(0).getAlipay_uname());
                        tvWeixinMing.setText(response.body().data.get(0).getWx_user_name());
                        yongHuMing = response.body().data.get(0).getUser_name();
                    }

                    @Override
                    public void onError(Response<AppResponse<UserInfo.DataBean>> response) {
                        AlertUtil.t(SettingActivity.this, response.getException().getMessage());
                    }
                });
    }


    public void updateInfo(String updateType) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "00701");
        map.put("key", Urls.key);
        map.put("of_user_id", UserManager.getManager(this).getUserId());
        map.put("update_type", updateType);
        switch (updateType) {
            case "2":
                map.put("user_sex", index + 1 + "");
                break;
            case "3":
                map.put("user_birthday", tvBirthday.getText().toString());
                break;
        }
        Gson gson = new Gson();
        OkGo.<AppResponse>post(Urls.SERVER_URL + "msg")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse>() {
                    @Override
                    public void onSuccess(final Response<AppResponse> response) {

                    }

                    @Override
                    public void onError(Response<AppResponse> response) {
                        AlertUtil.t(SettingActivity.this, response.getException().getMessage());
                    }
                });
    }


    @OnClick({R.id.rl_back, R.id.layout_header, R.id.layout_nickname, R.id.layout_gender, R.id.layout_birthday,
            R.id.layout_address, R.id.layout_cash_account, R.id.layout_login_password, R.id.layout_pay_password, R.id.layout_clear_cache,
            R.id.tv_exit, R.id.ll_shezhi_weixin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.layout_header:
                String[] items = {"拍照", "相册"};
                final ActionSheetDialog dialog = new ActionSheetDialog(this, items, null);
                dialog.isTitleShow(false).show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        Uri imageUri = Uri.fromFile(file);
                        switch (position) {
                            case 0:
                                takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                                break;
                            case 1:
                                takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                                break;
                        }
                        dialog.dismiss();

                    }
                });

                break;
            case R.id.layout_nickname:
                startActivity(new Intent(SettingActivity.this, NickActivity.class));
                break;
            case R.id.layout_gender:
                final List<String> item = new ArrayList<>();
                pvOptions = new OptionsPickerBuilder(SettingActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        tvGender.setText(item.get(options1));
                        index = options1;
                        updateInfo("2");

                    }
                }).build();
                item.clear();
                item.add("男");
                item.add("女");
                pvOptions.setTitleText("请选择性别");
                pvOptions.setPicker(item);
                pvOptions.setSelectOptions(index);
                pvOptions.show();
                break;
            case R.id.layout_birthday:
                timePicker = new TimePicker.Builder(this, TimePicker.TYPE_DATE, new TimePicker.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(TimePicker picker, Date date) {
                        tvBirthday.setText(getTime(date));
                        updateInfo("3");
                    }
                }).setSelectedDate(System.currentTimeMillis()).create();
                timePicker.show();
                break;
            case R.id.layout_address:
                startActivity(new Intent(SettingActivity.this, AddressActivity.class));
                break;
            case R.id.layout_cash_account:
                PhoneCheckActivity.actionStart(SettingActivity.this, "0008", "1");
                break;
            case R.id.layout_login_password:
                PhoneCheckActivity.actionStart(SettingActivity.this, "0007");

                break;
            case R.id.layout_pay_password:
                PhoneCheckActivity.actionStart(SettingActivity.this, "0006");
                break;
            case R.id.ll_shezhi_weixin:

                String str = PreferenceHelper.getInstance(SettingActivity.this).getString(App.CUNCHUBIND_WEIXINPAY, "");

                String huashu1 = "";
                String huashu2 = "";
                //1 已经设置 2 未设置
                if (str.equals("1")) {
                    huashu1 = "您已绑定" + tvWeixinMing.getText().toString().trim() + "微信" + "需要解绑吗？";

                } else if (str.equals("2")) {
                    huashu1 = "您还未绑定微信，去绑定吗？";
                }

                MyCarCaoZuoDialog_CaoZuo_Base base = new MyCarCaoZuoDialog_CaoZuo_Base(this, "操作", huashu1, new MyCarCaoZuoDialog_CaoZuo_Base.OnDialogItemClickListener() {
                    @Override
                    public void clickLeft() {
                        // base.dismiss();

                    }

                    @Override
                    public void clickRight() {
                        if (str.equals("1")) {
                            PhoneCheckActivity.actionStart_WeiBind(SettingActivity.this, "0320", false);
                        } else if (str.equals("2")) {
                            PhoneCheckActivity.actionStart_WeiBind(SettingActivity.this, "0320", true);
                        }
                    }
                });


                if (str.equals("1")) {
                    base.show();
                } else if (str.equals("2")) {
                    base.show();
                }

                break;
            case R.id.layout_clear_cache:
                normalDialog = new NormalDialog(this);
                normalDialog.content("确定清除当前应用的缓存数据吗?").showAnim(mBasIn).dismissAnim(mBasOut).show();
                normalDialog.setOnBtnClickL(
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                normalDialog.dismiss();
                            }
                        },
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                CleanMessageUtil.clearAllCache(SettingActivity.this);
                                normalDialog.dismiss();
                                AlertUtil.t(SettingActivity.this, "清除完毕！");
                                try {
                                    tvCache.setText(CleanMessageUtil.getTotalCacheSize(SettingActivity.this));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                break;
            case R.id.tv_exit:
                normalDialog = new NormalDialog(this);
                normalDialog.content("确定要注销当前账户吗?").showAnim(mBasIn).dismissAnim(mBasOut).show();
                normalDialog.setOnBtnClickL(
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {

                                normalDialog.dismiss();
                            }
                        },
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                loginOut();
                                UserManager.getManager(SettingActivity.this).removeUser();
                                PreferenceHelper.getInstance(SettingActivity.this).removeKey(AppConfig.SERVERID);
                                PreferenceHelper.getInstance(SettingActivity.this).removeKey(AppConfig.DEVICECCID);
                                normalDialog.dismiss();

                                //发送通知 -- 关闭所有订阅的mqtt
                                Notice n = new Notice();
                                n.type = ConstanceValue.MSG_UNSUB_MQTT;
                                RxBus.getDefault().sendRx(n);

                                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                            }
                        });
                break;
        }
    }


    /**
     * 退出登陆
     */
    private void loginOut() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03528");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        Gson gson = new Gson();
        OkGo.<AppResponse<LoginUser.DataBean>>post(SERVER_URL + "index/login")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new DialogCallback<AppResponse<LoginUser.DataBean>>(this) {
                    @Override
                    public void onSuccess(Response<AppResponse<LoginUser.DataBean>> response) {

                    }

                    @Override
                    public void onError(Response<AppResponse<LoginUser.DataBean>> response) {

                    }
                });
    }

    @Override
    public void takeSuccess(TResult result) {
        //此处使用原图路径，不压缩
        File file = new File(result.getImage().getOriginalPath());
        OkGo.<AppResponse<Upload.DataBean>>post(Urls.SERVER_URL + "msg/upload")
                .tag(this)//
                .isSpliceUrl(true)
                .params("key", Urls.key)
                .params("token", UserManager.getManager(SettingActivity.this).getAppToken())
                .params("type", "1")
                .params("file", file)
                .execute(new JsonCallback<AppResponse<Upload.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<Upload.DataBean>> response) {
                        Glide.with(SettingActivity.this).load(response.body().data.get(0).getFile_all_url()).into(ivHeader);
                    }

                    @Override
                    public void onError(Response<AppResponse<Upload.DataBean>> response) {
                        AlertUtil.t(SettingActivity.this, response.getException().getMessage());
                    }
                });

    }

    @Override
    public void takeFail(TResult result, String msg) {
        showToast(msg);
    }

    @Override
    public void takeCancel() {
        showToast("取消选择");
    }


    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    /**
     * 配置takerPhoto参数
     */
    public CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(800).setAspectY(800);
        return builder.create();
    }

    @SuppressLint("SimpleDateFormat")
    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format;
        format = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = format.format(date);
        return strDate;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals("update")) {
            requestData();
        }
    }

}
