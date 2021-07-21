package com.falaer.cn.activity.device_ty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.falaer.cn.R;
import com.falaer.cn.activity.DiagnosisActivity;
import com.falaer.cn.activity.lixianjilu.LixianjiluActivity;
import com.falaer.cn.activity.shuinuan.Y;
import com.falaer.cn.activity.shuinuan.gongxiang.GongxiangActivity;
import com.falaer.cn.activity.shuinuan.gongxiang.GongxiangModel;
import com.falaer.cn.app.BaseActivity;
import com.falaer.cn.app.ConstanceValue;
import com.falaer.cn.app.Notice;
import com.falaer.cn.app.RxBus;
import com.falaer.cn.callback.JsonCallback;
import com.falaer.cn.config.AppResponse;
import com.falaer.cn.config.PreferenceHelper;
import com.falaer.cn.config.UserManager;
import com.falaer.cn.dialog.newdia.TishiDialog;
import com.falaer.cn.get_net.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class TongyongSetActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.tv_shebeima)
    TextView tv_shebeima;
    @BindView(R.id.ll_guzhangdaima)
    LinearLayout ll_guzhangdaima;
    @BindView(R.id.ll_dingshi)
    LinearLayout ll_dingshi;
    @BindView(R.id.ll_jiareqizhuangtai)
    LinearLayout ll_jiareqizhuangtai;
    @BindView(R.id.ll_weixiujilu)
    LinearLayout ll_weixiujilu;
    @BindView(R.id.ll_jiebang)
    LinearLayout ll_jiebang;
    @BindView(R.id.ll_guzhang)
    LinearLayout ll_guzhang;
    @BindView(R.id.rl_gongxiang)
    LinearLayout rlGongxiang;
    @BindView(R.id.rl_gongxiang_jie)
    LinearLayout rlGongxiangJie;
    @BindView(R.id.ll_lixianjilu)
    LinearLayout llLixianjilu;

    private String shebeima;
    private String ccid;


    @Override
    public int getContentViewResId() {
        return R.layout.a_tongyong_act_set;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
        mImmersionBar.statusBarDarkFont(true);
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, TongyongSetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        ccid = PreferenceHelper.getInstance(this).getString("ccid", "");
        tv_shebeima.setText(ccid);
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_JIEBANG) {
                    finish();
                }
            }
        }));

        String share_type = PreferenceHelper.getInstance(mContext).getString("share_type", "");
        if (share_type.equals("2")) {
            rlGongxiang.setVisibility(View.GONE);
            ll_jiebang.setVisibility(View.GONE);
            rlGongxiangJie.setVisibility(View.VISIBLE);
        } else {
            rlGongxiang.setVisibility(View.VISIBLE);
            ll_jiebang.setVisibility(View.VISIBLE);
            rlGongxiangJie.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.ll_lixianjilu, R.id.rl_gongxiang, R.id.rl_gongxiang_jie, R.id.ll_guzhang, R.id.rl_back, R.id.ll_guzhangdaima, R.id.ll_dingshi, R.id.ll_jiareqizhuangtai, R.id.ll_weixiujilu, R.id.ll_jiebang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.ll_guzhangdaima:
                break;
            case R.id.ll_dingshi:
                TongyongDingshiActivity.actionStart(mContext);
                break;
            case R.id.ll_jiareqizhuangtai:
                TongyongStateActivity.actionStart(mContext);
                break;
            case R.id.ll_weixiujilu:
                break;
            case R.id.ll_jiebang:
                TongyongJiebangActivity.actionStart(mContext);
                break;
            case R.id.ll_guzhang:
                DiagnosisActivity.actionStart(mContext);
                break;
            case R.id.rl_gongxiang:
                GongxiangActivity.actionStart(mContext, ccid);
                break;
            case R.id.rl_gongxiang_jie:
                clickGongxiangJie();
                break;
            case R.id.ll_lixianjilu:
                LixianjiluActivity.actionStart(mContext);
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
