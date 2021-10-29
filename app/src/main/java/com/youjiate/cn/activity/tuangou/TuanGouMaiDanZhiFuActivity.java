package com.youjiate.cn.activity.tuangou;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alipay.sdk.app.PayTask;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.youjiate.cn.R;
import com.youjiate.cn.activity.shuinuan.Y;
import com.youjiate.cn.app.App;
import com.youjiate.cn.app.AppManager;
import com.youjiate.cn.app.BaseActivity;
import com.youjiate.cn.app.ConstanceValue;
import com.youjiate.cn.app.Notice;
import com.youjiate.cn.app.UIHelper;
import com.youjiate.cn.callback.JsonCallback;
import com.youjiate.cn.common.StringUtils;
import com.youjiate.cn.config.AppResponse;
import com.youjiate.cn.config.PreferenceHelper;
import com.youjiate.cn.config.UserManager;
import com.youjiate.cn.get_net.Urls;
import com.youjiate.cn.model.YuZhiFuModel;
import com.youjiate.cn.model.YuZhiFuModel_AliPay;
import com.youjiate.cn.pay_about.alipay.PayResult;
import com.youjiate.cn.util.PaySuccessUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class TuanGouMaiDanZhiFuActivity extends BaseActivity {

    @BindView(R.id.view_weixin)
    View viewWeixin;
    @BindView(R.id.view_zhifubao)
    View viewZhifubao;
    @BindView(R.id.tv_choose_zhifufangshi)
    TextView tvChooseZhifufangshi;
    @BindView(R.id.iv_icon_1)
    ImageView ivIcon1;
    @BindView(R.id.iv_weixin_choose)
    ImageView ivWeixinChoose;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.iv_icon_2)
    ImageView ivIcon2;
    @BindView(R.id.iv_zhifubao_choose)
    ImageView ivZhifubaoChoose;
    @BindView(R.id.frtv_pay)
    RoundTextView frtvPay;

    private String appId;//支付id 给支付宝
    private IWXAPI api;
    private String pay_id = "2";//支付方式-- 1 支付宝 2 微信
    private String payType = "4";//1 支付宝 4 微信

    private String jine;
    private String inst_id;
    private String diYongQuanID;
    private String userHongBao;//0什么也没用  1 没用 2用了

    private YuZhiFuModel.DataBean dataBean;
    private String formId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initHuidiao();
    }

    private void initData() {
        inst_id = getIntent().getStringExtra("inst_id");
        jine = getIntent().getStringExtra("jine");
        userHongBao = getIntent().getStringExtra("userHongBao");
        diYongQuanID = getIntent().getStringExtra("diYongQuanID");

        frtvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNet();
            }
        });

        viewWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_id = "2";//支付方式-- 1 支付宝 2 微信
                payType = "4";//1 支付宝 4 微信
                ivZhifubaoChoose.setVisibility(View.INVISIBLE);
                ivWeixinChoose.setVisibility(View.VISIBLE);
            }
        });

        viewZhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_id = "1";//支付方式-- 1 支付宝 2 微信
                payType = "1";//1 支付宝 4 微信
                ivZhifubaoChoose.setVisibility(View.VISIBLE);
                ivWeixinChoose.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_TUANGOUPAY) {
                    UIHelper.ToastMessage(TuanGouMaiDanZhiFuActivity.this, "支付成功");
                    PaySuccessUtils.getNet(TuanGouMaiDanZhiFuActivity.this, formId);
                    finish();

                    AppManager.getAppManager().finishActivity(TuanGouMaiDanDingDanActivity.class);
                    AppManager.getAppManager().finishActivity(TuanGouMaiDanActivity.class);
                    AppManager.getAppManager().finishActivity(TuanGouShangJiaDetailsActivity.class);
                }
            }
        }));
    }

    @Override
    public int getContentViewResId() {
        return R.layout.tuangou_zaixianfu_pay;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("在线支付");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imm.hideSoftInputFromWindow(findViewById(R.id.cl_layout).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });
    }

    //支付宝支付

    /**
     * 支付宝支付业务
     */
    public void payV2(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(TuanGouMaiDanZhiFuActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 微信支付
     */
    private void goToWeChatPay(YuZhiFuModel.DataBean out) {
        api.registerApp(out.getPay().getAppid());
        if (api == null) {
            UIHelper.ToastMessage(TuanGouMaiDanZhiFuActivity.this, "创建订单失败");
            return;
        }

        PayReq req = new PayReq();
        req.appId = out.getPay().getAppid();
        req.partnerId = out.getPay().getPartnerid();
        req.prepayId = out.getPay().getPrepayid();
        req.timeStamp = out.getPay().getTimestamp();
        req.nonceStr = out.getPay().getNoncestr();
        req.sign = out.getPay().getSign();
        req.packageValue = out.getPay().getPackageX();
        api.sendReq(req);
    }

    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        finish();
                        PaySuccessUtils.getNet(TuanGouMaiDanZhiFuActivity.this, formId);
                        AppManager.getAppManager().finishActivity(TuanGouMaiDanDingDanActivity.class);
                        AppManager.getAppManager().finishActivity(TuanGouMaiDanActivity.class);
                        //AppManager.getAppManager().finishActivity(TuanGouShangJiaDetailsActivity.class);
                        UIHelper.ToastMessage(TuanGouMaiDanZhiFuActivity.this, "支付成功", Toast.LENGTH_SHORT);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(TuanGouMaiDanZhiFuActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context, String inst_id, String jine, String userHongBao, String diYongQuanID) {
        Intent intent = new Intent(context, TuanGouMaiDanZhiFuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("inst_id", inst_id);
        intent.putExtra("jine", jine);
        intent.putExtra("userHongBao", userHongBao);
        intent.putExtra("diYongQuanID", diYongQuanID);
        context.startActivity(intent);
    }

    private void getNet() {
        if (pay_id.equals("1")) {//1支付宝
            //获得后台的支付信息\
            /**
             * {
             *   "key":"20180305124455yu",
             *  "token":"1234353453453456",
             *  "operate_id":"12",
             *  "operate_type":"1",
             *  "pay_id":"1"
             * }
             */
            Map<String, String> map = new HashMap<>();
            map.put("key", Urls.key);
            map.put("token", UserManager.getManager(TuanGouMaiDanZhiFuActivity.this).getAppToken());
            map.put("operate_id", "1");
            map.put("operate_type", "29");
            map.put("pay_id", pay_id);
            map.put("pay_type", payType);
            map.put("inst_id", inst_id);
            map.put("non_deduction_money", jine);
            if (!StringUtils.isEmpty(diYongQuanID)) {
                map.put("user_agio_id", diYongQuanID);
            } else {
                map.put("deduction_type", userHongBao);
            }
            Gson gson = new Gson();
            OkGo.<AppResponse<YuZhiFuModel_AliPay.DataBean>>post(Urls.DALIBAO_PAY)
                    .tag(TuanGouMaiDanZhiFuActivity.this)//
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<AppResponse<YuZhiFuModel_AliPay.DataBean>>() {
                        @Override
                        public void onSuccess(Response<AppResponse<YuZhiFuModel_AliPay.DataBean>> response) {
                            appId = response.body().data.get(0).getPay();
                            formId = response.body().data.get(0).getOut_trade_no();
                            dismissProgressDialog();
                            pay();
                        }

                        @Override
                        public void onStart(Request<AppResponse<YuZhiFuModel_AliPay.DataBean>, ? extends Request> request) {
                            super.onStart(request);
                            showProgressDialog();
                        }

                        @Override
                        public void onError(Response<AppResponse<YuZhiFuModel_AliPay.DataBean>> response) {
                            super.onError(response);
                            Y.tError(response);
                            dismissProgressDialog();
                        }
                    });
        } else {
            /**
             * {
             *   "key":"20180305124455yu",
             *  "token":"1234353453453456",
             *  "operate_id":"12",
             *  "operate_type":"1",
             *  "pay_id":"1"
             * }
             */
            Map<String, String> map = new HashMap<>();
            map.put("key", Urls.key);
            map.put("token", UserManager.getManager(TuanGouMaiDanZhiFuActivity.this).getAppToken());
            map.put("operate_id", "1");
            map.put("operate_type", "29");
            map.put("pay_id", pay_id);
            map.put("pay_type", payType);
            map.put("inst_id", inst_id);
            map.put("non_deduction_money", jine);
            map.put("deduction_type", userHongBao);

            if (!StringUtils.isEmpty(diYongQuanID)) {
                map.put("user_agio_id", diYongQuanID);
            } else {
                map.put("deduction_type", userHongBao);
            }
            Gson gson = new Gson();
            OkGo.<AppResponse<YuZhiFuModel.DataBean>>post(Urls.DALIBAO_PAY)
                    .tag(TuanGouMaiDanZhiFuActivity.this)//
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<AppResponse<YuZhiFuModel.DataBean>>() {
                        @Override
                        public void onSuccess(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                            dataBean = response.body().data.get(0);
                            api = WXAPIFactory.createWXAPI(TuanGouMaiDanZhiFuActivity.this, dataBean.getPay().getAppid());
                            formId = response.body().data.get(0).getPay().getOut_trade_no();
                            dismissProgressDialog();
                            pay();
                        }

                        @Override
                        public void onError(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                            super.onError(response);
                            Y.tError(response);
                            dismissProgressDialog();
                        }

                        @Override
                        public void onStart(Request<AppResponse<YuZhiFuModel.DataBean>, ? extends Request> request) {
                            super.onStart(request);
                            showProgressDialog();
                        }
                    });
        }
    }

    private void pay() {
        PreferenceHelper.getInstance(TuanGouMaiDanZhiFuActivity.this).putString(App.MAIDAN_PAY, "MAIDAN");
        if (pay_id.equals("2")) {
            goToWeChatPay(dataBean);
        } else if (pay_id.equals("1")) {
            payV2(appId);//这里填写后台返回的支付信息
        }
    }
}
