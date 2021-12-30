package com.lipinkeji.cn.activity.device_fengnuan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_falaer.gongxiang.GongxiangFalaerActivity;
import com.lipinkeji.cn.util.Y;
import com.lipinkeji.cn.activity.device_shuinuan.gongxiang.GongxiangModel;
import com.lipinkeji.cn.activity.vip.dialog.XufeiDialog;
import com.lipinkeji.cn.activity.vip.model.XufeiModel;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.app.RxBus;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.YuZhiFuModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class FengnuanSetActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.ll_gaojishezhi)
    LinearLayout ll_gaojishezhi;
    @BindView(R.id.rl_dingshi)
    RelativeLayout rl_dingshi;
    @BindView(R.id.rl_jiareqizhuangtai)
    RelativeLayout rl_jiareqizhuangtai;
    @BindView(R.id.rl_wendushezhi)
    RelativeLayout rl_wendushezhi;
    @BindView(R.id.rl_chezhuxinxi)
    RelativeLayout rl_chezhuxinxi;
    @BindView(R.id.rl_lingpeijianxinxi)
    RelativeLayout rl_lingpeijianxinxi;
    @BindView(R.id.rl_gongxiang)
    RelativeLayout rl_gongxiang;
    @BindView(R.id.rl_gongxiang_jie)
    RelativeLayout rl_gongxiang_jie;
    @BindView(R.id.rl_jiebangshebei)
    RelativeLayout rl_jiebangshebei;
    @BindView(R.id.rl_jiareqizhenduan)
    RelativeLayout rl_jiareqizhenduan;
    @BindView(R.id.rl_shebeixufei)
    RelativeLayout rl_shebeixufei;

    private String shebeima;
    private String ccid;
    private String share_type;

    @Override
    public int getContentViewResId() {
        return R.layout.a_fengnuan_act_set;
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
        Intent intent = new Intent(context, FengnuanSetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        ccid = PreferenceHelper.getInstance(this).getString("ccid", "");
        share_type = PreferenceHelper.getInstance(mContext).getString("share_type", "");
        initHuidiao();
        initView();
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_JIEBANG) {
                    finish();
                }
            }
        }));

    }

    private void initView() {
        if (share_type.equals("2")) {
            rl_jiebangshebei.setVisibility(View.GONE);
            rl_gongxiang.setVisibility(View.GONE);
            rl_gongxiang_jie.setVisibility(View.VISIBLE);
        } else {
            rl_jiebangshebei.setVisibility(View.VISIBLE);
            rl_gongxiang.setVisibility(View.VISIBLE);
            rl_gongxiang_jie.setVisibility(View.GONE);
        }

        initXufei(false);
    }

    private void initXufei(boolean isKai) {
        if (isKai) {
            rl_shebeixufei.setVisibility(View.VISIBLE);
            initShuju();
        } else {
            rl_shebeixufei.setVisibility(View.GONE);
        }
    }

    /**
     * 续费相关
     */
    private XufeiDialog xufeiDialog;
    private String validdate;
    private String validdate_state;
    private String sim_ccid;
    private List<XufeiModel.DataBean> xufeiModels;
    private YuZhiFuModel.DataBean dataBean_pay;

    private void initShuju() {
        validdate = PreferenceHelper.getInstance(mContext).getString("validdate", "0");
        validdate_state = PreferenceHelper.getInstance(mContext).getString("validdate_state", "0");
        sim_ccid = PreferenceHelper.getInstance(mContext).getString("sim_ccid", "0");
        getXufei();
    }

    private void getXufei() {
        xufeiModels = new ArrayList<>();
        XufeiModel.DataBean bean = new XufeiModel.DataBean();
        bean.setMoney("5");
        bean.setYear("1");
        xufeiModels.add(bean);
    }

    private void clickXufei() {
        xufeiDialog = new XufeiDialog(mContext);
        xufeiDialog.setModels(xufeiModels);
        xufeiDialog.setTv_shebei_youxiaoqi(validdate);
        xufeiDialog.setXufeiClick(new XufeiDialog.XufeiClick() {
            @Override
            public void xufei() {
                payWX();
            }
        });
        xufeiDialog.show();
    }

    private void payWX() {
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
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, dataBean_pay.getPay().getAppid());
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


    @OnClick({R.id.rl_back, R.id.ll_gaojishezhi, R.id.rl_dingshi, R.id.rl_jiareqizhuangtai, R.id.rl_wendushezhi, R.id.rl_chezhuxinxi, R.id.rl_lingpeijianxinxi, R.id.rl_gongxiang, R.id.rl_gongxiang_jie, R.id.rl_jiebangshebei, R.id.rl_jiareqizhenduan, R.id.rl_shebeixufei})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.ll_gaojishezhi:
                FengnuanGaojiSetActiviy.actionStart(mContext);
                break;
            case R.id.rl_dingshi:
                FengnuanDingshiActivity.actionStart(mContext);
                break;
            case R.id.rl_jiareqizhuangtai:
                FengnuanStateActivity.actionStart(mContext);
                break;
            case R.id.rl_wendushezhi:
                FengnuanWendusetActivity.actionStart(mContext);
                break;
            case R.id.rl_chezhuxinxi:
                break;
            case R.id.rl_lingpeijianxinxi:
                break;
            case R.id.rl_gongxiang:
                GongxiangFalaerActivity.actionStart(mContext, ccid);
                break;
            case R.id.rl_gongxiang_jie:
                clickGongxiangJie();
                break;
            case R.id.rl_jiebangshebei:
                FengnuanJieActivity.actionStart(mContext);
                break;
            case R.id.rl_jiareqizhenduan:
                JiareqiGuzhangActivity.actionStart(mContext);
                break;
            case R.id.rl_shebeixufei:
                clickXufei();
                break;
        }
    }

    private void clickGongxiangJie() {
        TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {
                Map<String, String> map = new HashMap<>();
                map.put("code", "03514");
                map.put("key", Urls.key);
                map.put("token", UserManager.getManager(mContext).getAppToken());
                map.put("ccid", ccid);
                Gson gson = new Gson();
                OkGo.<AppResponse<GongxiangModel.DataBean>>post(Urls.SERVER_URL + "wit/app/user")
                        .tag(this)//
                        .upJson(gson.toJson(map))
                        .execute(new JsonCallback<AppResponse<GongxiangModel.DataBean>>() {
                            @Override
                            public void onSuccess(final Response<AppResponse<GongxiangModel.DataBean>> response) {
                                Y.t("退出成功");
                                Notice n = new Notice();
                                n.type = ConstanceValue.MSG_JIEBANG;
                                RxBus.getDefault().sendRx(n);
                            }

                            @Override
                            public void onError(Response<AppResponse<GongxiangModel.DataBean>> response) {
                                Y.tError(response);
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                dismissProgressDialog();
                            }

                            @Override
                            public void onStart(Request<AppResponse<GongxiangModel.DataBean>, ? extends Request> request) {
                                super.onStart(request);
                                showProgressDialog();
                            }
                        });
            }

            @Override
            public void onDismiss(TishiDialog dialog) {

            }
        });
        dialog.setTextContent("是否退出该共享设备");
        dialog.show();
    }


}
