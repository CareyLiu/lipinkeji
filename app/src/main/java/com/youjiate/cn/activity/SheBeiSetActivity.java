package com.youjiate.cn.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.youjiate.cn.activity.device_falaer.FalaerDingshiActivity;
import com.youjiate.cn.activity.device_falaer.FalaerJiebangActivity;
import com.youjiate.cn.activity.device_falaer.gongxiang.GongxiangFalaerActivity;
import com.youjiate.cn.activity.vip.dialog.XufeiDialog;
import com.youjiate.cn.activity.vip.model.XufeiModel;
import com.youjiate.cn.model.YuZhiFuModel;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.youjiate.cn.activity.lixianjilu.LixianjiluActivity;
import com.youjiate.cn.activity.shuinuan_wzw.gongxiang.GongxiangWzwActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.youjiate.cn.R;
import com.youjiate.cn.activity.shuinuan.ShuinuanBaseNewActivity;
import com.youjiate.cn.activity.shuinuan.ShuinuanHostNewActivity;
import com.youjiate.cn.activity.shuinuan.ShuinuanZhuangtaiActivity;
import com.youjiate.cn.activity.shuinuan.Y;
import com.youjiate.cn.activity.shuinuan.gongxiang.GongxiangModel;
import com.youjiate.cn.activity.zckt.ZcktStateActivity;
import com.youjiate.cn.app.ConstanceValue;
import com.youjiate.cn.app.Notice;
import com.youjiate.cn.app.RxBus;
import com.youjiate.cn.callback.JsonCallback;
import com.youjiate.cn.config.AppResponse;
import com.youjiate.cn.config.PreferenceHelper;
import com.youjiate.cn.config.UserManager;
import com.youjiate.cn.dialog.newdia.TishiDialog;
import com.youjiate.cn.get_net.Urls;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SheBeiSetActivity extends ShuinuanBaseNewActivity {
    @BindView(R.id.rl_dingshi)
    RelativeLayout rlDingshi;
    @BindView(R.id.rl_jiareqicanshu)
    RelativeLayout rlJiareqicanshu;
    @BindView(R.id.rl_jiebangshebei)
    RelativeLayout rlJiebangshebei;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rl_guzhang)
    RelativeLayout rlGuzhang;
    @BindView(R.id.tv_shebeima)
    TextView tvShebeima;
    @BindView(R.id.rl_zhujicanshu)
    RelativeLayout rlZhujicanshu;
    @BindView(R.id.rl_gongxiang)
    RelativeLayout rlGongxiang;
    @BindView(R.id.rl_gongxiang_jie)
    RelativeLayout rlGongxiangJie;
    @BindView(R.id.rl_kongtiao_state)
    RelativeLayout rlKongtiaoState;
    @BindView(R.id.rl_shebeixufei)
    RelativeLayout rl_shebeixufei;
    private String ccid;

    public static final int TYPE_FENGNUAN = 1;
    public static final int TYPE_SHUINUAN = 2;
    public static final int TYPE_KONGTISO = 3;
    private int type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getIntent().getIntExtra("type", 0);

        if (type == TYPE_SHUINUAN) {
            rlGuzhang.setVisibility(View.GONE);
            rlZhujicanshu.setVisibility(View.GONE);
            rlKongtiaoState.setVisibility(View.GONE);
        } else if (type == TYPE_FENGNUAN) {
            rlGuzhang.setVisibility(View.VISIBLE);
            rlZhujicanshu.setVisibility(View.GONE);
            rlKongtiaoState.setVisibility(View.GONE);
        } else if (type == TYPE_KONGTISO) {
            rlGuzhang.setVisibility(View.GONE);
            rlZhujicanshu.setVisibility(View.GONE);
            rlJiareqicanshu.setVisibility(View.GONE);
            rlKongtiaoState.setVisibility(View.VISIBLE);
        }

        String share_type = PreferenceHelper.getInstance(mContext).getString("share_type", "");
        if (share_type.equals("2")) {
            rlGongxiang.setVisibility(View.GONE);
            rlJiebangshebei.setVisibility(View.GONE);
            rlGongxiangJie.setVisibility(View.VISIBLE);
        } else {
            rlGongxiang.setVisibility(View.VISIBLE);
            rlJiebangshebei.setVisibility(View.VISIBLE);
            rlGongxiangJie.setVisibility(View.GONE);
        }

        ccid = PreferenceHelper.getInstance(this).getString("ccid", "");
        tvShebeima.setText(ccid);

        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_JIEBANG) {
                    finish();
                }
            }
        }));

        rlDingshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FengnuandishiActivity.actionStart(mContext);
            }
        });
        rlJiareqicanshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == TYPE_SHUINUAN) {
                    ShuinuanZhuangtaiActivity.actionStart(mContext);
                } else if (type == TYPE_FENGNUAN) {
                    JiaReQiCanShuActivity.actionStart(mContext);
                }
            }
        });
        rlJiebangshebei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FengnuanJieActivity.actionStart(mContext);
            }
        });
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlGuzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiagnosisActivity.actionStart(mContext);
            }
        });

        rlKongtiaoState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZcktStateActivity.actionStart(mContext);
            }
        });

        rlZhujicanshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShuinuanHostNewActivity.actionStart(mContext);
            }
        });

        rlGongxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GongxiangWzwActivity.actionStart(mContext, ccid);
            }
        });

        rlGongxiangJie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        rl_shebeixufei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickXufei();
            }
        });

        initShuju();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_shebei_set;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context, int type) {
        Intent intent = new Intent(context, SheBeiSetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type", type);
        context.startActivity(intent);
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

    @Override
    protected void initToolbar() {
        super.initToolbar();

        tv_title.setText("设置");
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

}
