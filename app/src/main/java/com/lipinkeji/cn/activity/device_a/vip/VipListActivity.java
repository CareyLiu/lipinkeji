package com.lipinkeji.cn.activity.device_a.vip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.util.Y;
import com.lipinkeji.cn.activity.device_a.vip.adapter.ViplistAdapter;
import com.lipinkeji.cn.activity.device_a.vip.dialog.XufeiDialog;
import com.lipinkeji.cn.activity.device_a.vip.model.XufeiModel;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.SheBeiLieBieListModel;
import com.lipinkeji.cn.model.SheBeiModel;
import com.lipinkeji.cn.model.YuZhiFuModel;
import com.lipinkeji.cn.model.YuZhiFuModel_AliPay;
import com.lipinkeji.cn.pay_about.alipay.PayResult;
import com.lipinkeji.cn.util.AlertUtil;
import com.google.gson.Gson;
import com.lipinkeji.cn.util.PaySuccessUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VipListActivity extends BaseActivity {
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    @BindView(R.id.ll_jilu)
    LinearLayout ll_jilu;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private List<SheBeiModel> mDatas = new ArrayList<>();
    private ViplistAdapter adapter;
    private YuZhiFuModel.DataBean dataBean_pay;
    private IWXAPI api;
    private List<XufeiModel.DataBean> xufeiModels;
    private XufeiDialog xufeiDialog;
    private String appId;
    private String formId;


    @Override
    public int getContentViewResId() {
        return R.layout.activity_vip_list;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, VipListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("续费列表");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initAdapter();
        initSM();
        getSheBeiData();
        getXufei();
    }

    private void getXufei() {
        xufeiModels = new ArrayList<>();
        XufeiModel.DataBean bean = new XufeiModel.DataBean();
        bean.setMoney("5");
        bean.setYear("1");
        xufeiModels.add(bean);
    }

    private void initAdapter() {
        adapter = new ViplistAdapter(R.layout.item_vip_list, mDatas);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        rv_list.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SheBeiModel sheBeiModel = mDatas.get(position);
                xufeiDialog = new XufeiDialog(mContext);
                xufeiDialog.setModels(xufeiModels);
                xufeiDialog.setTv_shebei_youxiaoqi(sheBeiModel.validity_time);
                xufeiDialog.setXufeiClick(new XufeiDialog.XufeiClick() {
                    @Override
                    public void xufei() {
//                        payZfb(sheBeiModel, xufeiBean);
                        payWX(sheBeiModel);
                    }
                });
                xufeiDialog.show();
            }
        });
    }

    private void payWX(SheBeiModel sheBeiModel) {
        String ccid = sheBeiModel.ccid;
        Map<String, String> map = new HashMap<>();
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("pay_id", "2");
        map.put("pay_type", "4");
        map.put("operate_type", "52");
        map.put("operate_id", "11");
        map.put("ccid", ccid);
        map.put("project_type", "btfn");
        String myHeaderLog = new Gson().toJson(map);
        String myHeaderInfo = StringEscapeUtils.unescapeJava(myHeaderLog);
        OkGo.<AppResponse<YuZhiFuModel.DataBean>>post(Urls.DALIBAO_PAY)
                .tag(mContext)//
                .upJson(myHeaderInfo)
                .execute(new JsonCallback<AppResponse<YuZhiFuModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                        Y.t(response.body().msg);
                        dataBean_pay = response.body().data.get(0);
                        api = WXAPIFactory.createWXAPI(mContext, dataBean_pay.getPay().getAppid());
                        goToWeChatPay(dataBean_pay);
                    }

                    @Override
                    public void onError(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                        super.onError(response);
                        Y.tError(response);
                    }
                });
    }

    /**
     * 微信支付
     */
    private void goToWeChatPay(YuZhiFuModel.DataBean out) {
        api = WXAPIFactory.createWXAPI(mContext, dataBean_pay.getPay().getAppid());
        api.registerApp(out.getPay().getAppid());
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

    private void payZfb(SheBeiModel sheBeiModel, XufeiModel.DataBean xufeiBean) {
        String ccid = sheBeiModel.ccid;
        Map<String, String> map = new HashMap<>();
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("pay_id", "1");
        map.put("pay_type", "1");
        map.put("operate_type", "33");
        map.put("operate_id", "13");
        map.put("ccid", ccid);
        map.put("project_type", "zdjh_card");

        String myHeaderLog = new Gson().toJson(map);
        String myHeaderInfo = StringEscapeUtils.unescapeJava(myHeaderLog);
        OkGo.<AppResponse<YuZhiFuModel_AliPay.DataBean>>post(Urls.DALIBAO_PAY)
                .tag(mContext)//
                .upJson(myHeaderInfo)
                .execute(new JsonCallback<AppResponse<YuZhiFuModel_AliPay.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<YuZhiFuModel_AliPay.DataBean>> response) {
                        appId = response.body().data.get(0).getPay();
                        formId = response.body().data.get(0).getOut_trade_no();
                        payV2(appId);
                    }

                    @Override
                    public void onError(Response<AppResponse<YuZhiFuModel_AliPay.DataBean>> response) {
                        super.onError(response);
                        Y.tError(response);
                    }
                });
    }


    private static final int SDK_PAY_FLAG = 1;

    /**
     * 支付宝支付业务
     */
    public void payV2(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(VipListActivity.this);
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
                        PaySuccessUtils.getNet(mContext, formId);
                        Y.t("支付成功");
                    } else {
                        Y.t("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void initSM() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getSheBeiData();
            }
        });
    }

    @OnClick(R.id.ll_jilu)
    public void onViewClicked() {
        VipJiluActivity.actionStart(mContext);
    }

    public void getSheBeiData() {
        mDatas.clear();
        Map<String, String> map = new HashMap<>();
        map.put("code", "03522");
        map.put("key", Urls.key);
        map.put("user_car_type", "1");
        map.put("token", UserManager.getManager(mContext).getAppToken());
        Gson gson = new Gson();
        OkGo.<AppResponse<SheBeiLieBieListModel.DataBean>>post(Urls.SERVER_URL + "wit/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<SheBeiLieBieListModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<SheBeiLieBieListModel.DataBean>> response) {
                        for (int i = 0; i < response.body().data.size(); i++) {
                            for (int j = 0; j < response.body().data.get(i).getControl_device_list().size(); j++) {
                                SheBeiModel sheBeiModel1 = new SheBeiModel(false, response.body().data.get(i).getControl_device_name());
                                SheBeiLieBieListModel.DataBean.ControlDeviceListBean bean = response.body().data.get(i).getControl_device_list().get(j);
                                sheBeiModel1.ccid = bean.getCcid();
                                sheBeiModel1.device_img_url = bean.getDevice_img_url();
                                sheBeiModel1.device_name = bean.getDevice_name();
                                sheBeiModel1.validity_state = bean.getValidity_state();
                                sheBeiModel1.validity_term = bean.getValidity_term();
                                sheBeiModel1.validity_time = bean.getValidity_time();
                                sheBeiModel1.device_type = response.body().data.get(i).getControl_type_id();
                                sheBeiModel1.sim_ccid_save_type = bean.sim_ccid_save_type;
                                sheBeiModel1.share_type = bean.getShare_type();
                                mDatas.add(sheBeiModel1);
                            }
                        }

                        adapter.setNewData(mDatas);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<AppResponse<SheBeiLieBieListModel.DataBean>> response) {
                        AlertUtil.t(mContext, response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        smartRefreshLayout.finishRefresh();
                    }
                });
    }
}
