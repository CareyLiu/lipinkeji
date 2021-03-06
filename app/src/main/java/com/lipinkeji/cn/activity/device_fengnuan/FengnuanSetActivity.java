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
import com.lipinkeji.cn.activity.device_a.DriveinfoActivity;
import com.lipinkeji.cn.activity.device_a.UserInfoActivity;
import com.lipinkeji.cn.activity.device_a.JiareqiGuzhangActivity;
import com.lipinkeji.cn.activity.device_a.gongxiang.GongxiangActivity;
import com.lipinkeji.cn.activity.device_fengnuan.set.DianHuoSaiActivity_FengNuan;
import com.lipinkeji.cn.activity.device_fengnuan.set.FengnuanGaojiSetActiviy;
import com.lipinkeji.cn.activity.device_fengnuan.set.YouBengActivity_FengNuan;
import com.lipinkeji.cn.util.Y;
import com.lipinkeji.cn.activity.device_a.gongxiang.GongxiangModel;
import com.lipinkeji.cn.activity.device_a.vip.dialog.XufeiDialog;
import com.lipinkeji.cn.activity.device_a.vip.model.XufeiModel;
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
     * ????????????Activty????????????Activity
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

    @Override
    protected void onResume() {
        super.onResume();
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

        initXufei(true);
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
     * ????????????
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

//    private void getXufei() {
//        xufeiModels = new ArrayList<>();
//        XufeiModel.DataBean bean = new XufeiModel.DataBean();
//        bean.setMoney("5");
//        bean.setYear("1");
//        xufeiModels.add(bean);
//    }

    private void getXufei() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03521");
        map.put("key", Urls.key);
        map.put("device_type", "1");
        map.put("token", UserManager.getManager(mContext).getAppToken());
        Gson gson = new Gson();
        OkGo.<AppResponse<XufeiModel.DataBean>>post(Urls.SERVER_URL + "wit/app")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<XufeiModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<XufeiModel.DataBean>> response) {
                        xufeiModels = response.body().data;
                    }
                });
    }


    private void clickXufei() {
        if (xufeiModels != null) {
            xufeiDialog = new XufeiDialog(mContext);
            xufeiDialog.setModels(xufeiModels);
            xufeiDialog.setTv_shebei_youxiaoqi(validdate);
            xufeiDialog.setXufeiClick(new XufeiDialog.XufeiClick() {
                @Override
                public void xufei(XufeiModel.DataBean xufeiBean) {
                    payWX(xufeiBean);
                }
            });
            xufeiDialog.show();
        } else {
            getXufei();
        }
    }

    private void payWX(XufeiModel.DataBean xufeiBean) {
        String pay_m_y_type = xufeiBean.getPay_m_y_type();

        Map<String, String> map = new HashMap<>();
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("pay_id", "2");
        map.put("pay_type", "4");
        map.put("operate_type", "60");
        map.put("operate_id", "15");
        map.put("ccid", ccid);
        map.put("project_type", "lp");
        map.put("pay_m_y_type", pay_m_y_type);

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
                        xufeiDialog.dismiss();
                    }

                    @Override
                    public void onError(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                        super.onError(response);
                        Y.tError(response);
                    }
                });
    }

    /**
     * ????????????
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
        if (xufeiDialog != null) {
            xufeiDialog.dismiss();
        }
    }


    @OnClick({R.id.rl_back, R.id.ll_gaojishezhi,
            R.id.rl_dingshi, R.id.rl_jiareqizhuangtai,
            R.id.rl_wendushezhi, R.id.rl_chezhuxinxi,
            R.id.rl_lingpeijianxinxi, R.id.rl_gongxiang,
            R.id.rl_gongxiang_jie, R.id.rl_jiebangshebei,
            R.id.rl_jiareqizhenduan, R.id.rl_shebeixufei,
            R.id.rl_dianhuosai_shezhi, R.id.rl_youbeng_shezhi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_dianhuosai_shezhi:
                DianHuoSaiActivity_FengNuan.actionStart(mContext);
                break;
            case R.id.rl_youbeng_shezhi:
                YouBengActivity_FengNuan.actionStart(mContext);
                break;
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
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            case R.id.rl_lingpeijianxinxi:
                DriveinfoActivity.actionStart(this);
                break;
            case R.id.rl_gongxiang:
                GongxiangActivity.actionStart(mContext, ccid);
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
                                Y.t("????????????");
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
        dialog.setTextContent("???????????????????????????");
        dialog.show();
    }


}
