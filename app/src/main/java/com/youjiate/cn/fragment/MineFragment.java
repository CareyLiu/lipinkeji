package com.youjiate.cn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.youjiate.cn.R;
import com.youjiate.cn.activity.MessageActivity;
import com.youjiate.cn.activity.SettingActivity;
import com.youjiate.cn.activity.dingdan.MyOrderActivity;
import com.youjiate.cn.activity.vip.VipListActivity;
import com.youjiate.cn.activity.wode_page.AboutUsActivity;
import com.youjiate.cn.activity.wode_page.DianPuListActivity;
import com.youjiate.cn.activity.wode_page.ShangPinShouCangActivity;
import com.youjiate.cn.app.App;
import com.youjiate.cn.app.ConstanceValue;
import com.youjiate.cn.app.Notice;
import com.youjiate.cn.basicmvp.BaseFragment;
import com.youjiate.cn.callback.JsonCallback;
import com.youjiate.cn.config.AppEvent;
import com.youjiate.cn.config.AppResponse;
import com.youjiate.cn.config.PreferenceHelper;
import com.youjiate.cn.config.UserManager;
import com.youjiate.cn.get_net.Urls;
import com.youjiate.cn.model.MineModel;
import com.youjiate.cn.util.phoneview.sample.ImageShowActivity;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.roundview.RoundRelativeLayout;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class MineFragment extends BaseFragment implements Observer {


    @BindView(R.id.riv_image)
    CircleImageView rivImage;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_dingdan)
    TextView tvDingdan;
    @BindView(R.id.iv_daifukuan)
    ImageView ivDaifukuan;
    @BindView(R.id.iv_daifahuo)
    ImageView ivDaifahuo;
    @BindView(R.id.iv_daishouhuo)
    ImageView ivDaishouhuo;
    @BindView(R.id.iv_pingjia)
    ImageView ivPingjia;
    @BindView(R.id.iv_daodian)
    ImageView ivDaodian;
    @BindView(R.id.tv_daifukuan)
    TextView tvDaifukuan;
    @BindView(R.id.tv_daifahuo)
    TextView tvDaifahuo;
    @BindView(R.id.tv_daishouhuo)
    TextView tvDaishouhuo;
    @BindView(R.id.tv_pingjia)
    TextView tvPingjia;
    @BindView(R.id.tv_daodian)
    TextView tvDaodian;
    @BindView(R.id.constrain1)
    ConstraintLayout constrain1;
    @BindView(R.id.rlv_dingdan)
    RoundRelativeLayout rlvDingdan;
    @BindView(R.id.tv_ershou_che)
    TextView tvErshouChe;
    @BindView(R.id.iv_che_yuyue)
    ImageView ivCheYuyue;
    @BindView(R.id.iv_chejindu)
    ImageView ivChejindu;
    @BindView(R.id.iv_maiche_jindu)
    ImageView ivMaicheJindu;
    @BindView(R.id.iv_xiaofeijilu)
    ImageView ivXiaofeijilu;
    @BindView(R.id.iv_quanbu_dingdan)
    ImageView ivQuanbuDingdan;
    @BindView(R.id.tv_chejindu)
    TextView tvChejindu;
    @BindView(R.id.tv_maiche_jindu)
    TextView tvMaicheJindu;
    @BindView(R.id.tv_shouhou)
    TextView tvShouhou;
    @BindView(R.id.constrain2)
    ConstraintLayout constrain2;
    @BindView(R.id.rlv_ershouche)
    RoundRelativeLayout rlvErshouche;
    @BindView(R.id.tv_zhanghu_chongzhi)
    TextView tvZhanghuChongzhi;
    @BindView(R.id.iv_zhanghu_chongzhi)
    ImageView ivZhanghuChongzhi;
    @BindView(R.id.iv_kapianchongzhi)
    ImageView ivKapianchongzhi;
    @BindView(R.id.iv_chongzhi_jilu)
    ImageView ivChongzhiJilu;
    @BindView(R.id.iv_shouhou_fuwu)
    ImageView ivShouhouFuwu;
    @BindView(R.id.iv_xiche_erwei)
    ImageView ivXicheErwei;
    @BindView(R.id.tv_kapianchongzhi)
    TextView tvKapianchongzhi;
    @BindView(R.id.tv_chongzhi_jilu)
    TextView tvChongzhiJilu;
    @BindView(R.id.tv_xiaofeijilu)
    TextView tvXiaofeijilu;
    @BindView(R.id.constrain3)
    ConstraintLayout constrain3;
    @BindView(R.id.rlv_xiche)
    RoundRelativeLayout rlvXiche;
    @BindView(R.id.iv_tongji)
    LinearLayout ivTongji;
    @BindView(R.id.iv_tuiguangma)
    LinearLayout ivTuiguangma;
    @BindView(R.id.iv_about_us)
    LinearLayout ivAboutUs;
    @BindView(R.id.rlv_about_us)
    RoundRelativeLayout rlvAboutUs;
    @BindView(R.id.srL_smart)
    SmartRefreshLayout srLSmart;
    @BindView(R.id.rl_xiaoxi)
    RelativeLayout rlXiaoxi;
    @BindView(R.id.rl_set)
    RelativeLayout rlSet;
    @BindView(R.id.ll_dianpushoucang)
    LinearLayout llDianpushoucang;
    @BindView(R.id.ll_shangpinshoucang)
    LinearLayout llShangpinshoucang;
    @BindView(R.id.cls_header)
    ClassicsHeader clsHeader;
    @BindView(R.id.view_zhanwei)
    View viewZhanwei;
    private Unbinder unbinder;

    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_fragment_wode;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

    }

    @Override
    protected void initView(View rootView) {
        rootView.setClickable(true);// 防止点击穿透，底层的fragment响应上层点击触摸事件
        AppEvent.getClassEvent().addObserver(this);
        initData();
        unbinder = ButterKnife.bind(this, rootView);

//        ivTuiguangma.setVisibility(View.VISIBLE);
    }

    public void initData() {
        getNet();

        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_PAY_SUCCESS_REFRESH_WODE) {
                    getNet();
                }
            }
        }));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        clsHeader.setAccentColor(getResources().getColor(R.color.white));
        srLSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getNet();
            }
        });

        srLSmart.setEnableLoadMore(false);
    }


    private MineModel.DataBean dataBean;
    private String agentUrl = "";

    private void getNet() {
        Map<String, String> map = new HashMap<>();
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(getActivity()).getAppToken());
        map.put("code", "04201");

        Gson gson = new Gson();
        OkGo.<AppResponse<MineModel.DataBean>>post(Urls.HOME_PICTURE_HOME)
                .tag(getActivity())//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<MineModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<MineModel.DataBean>> response) {
                        Log.i("MineFragment", "success");
                        srLSmart.finishRefresh();
                        dataBean = response.body().data.get(0);
                        Glide.with(getActivity()).load(response.body().data.get(0).getUser_img_url()).into(rivImage);
                        tvName.setText(dataBean.getUser_name());
                        tvPhone.setText(dataBean.getUser_phone());

                        PreferenceHelper.getInstance(getActivity()).putString(App.CUNCHUBIND_ALIPAY, response.body().data.get(0).getAlipay_number_check());
                        PreferenceHelper.getInstance(getActivity()).putString(App.CUNCHU_ZHIFUMIMA, response.body().data.get(0).getPay_pwd_check());//1 已经设置 2 未设置
                        PreferenceHelper.getInstance(getActivity()).putString(App.CUNCHUBIND_WEIXINPAY, response.body().data.get(0).getWx_pay_number_check());//1 已经设置 2 未设置
                        PreferenceHelper.getInstance(getActivity()).putString(App.CUN_GEREN_TOUXIANG, response.body().data.get(0).getUser_img_url());

                        agentUrl = response.body().data.get(0).getAgent_url();


                    }

                    @Override
                    public void onError(Response<AppResponse<MineModel.DataBean>> response) {
                        super.onError(response);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals("update")) {
            initData();
        }
    }

    @OnClick({R.id.ll_dianpushoucang, R.id.ll_shangpinshoucang, R.id.iv_tongji, R.id.iv_tuiguangma, R.id.iv_about_us, R.id.riv_image, R.id.rl_xiaoxi, R.id.rl_set,
            R.id.tv_all, R.id.iv_daifukuan, R.id.tv_daifukuan, R.id.iv_daishouhuo, R.id.tv_daishouhuo, R.id.iv_daifahuo, R.id.tv_daifahuo,
            R.id.iv_pingjia, R.id.tv_pingjia, R.id.iv_daodian, R.id.tv_daodian
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_tongji://续费
                VipListActivity.actionStart(getActivity());
                break;
            case R.id.iv_tuiguangma://清理缓存
                clickHuancun();
                break;
            case R.id.iv_about_us:
                AboutUsActivity.actionStart(getActivity());
                break;
            case R.id.riv_image:
                ArrayList<String> strings = new ArrayList<>();
                strings.add(dataBean.getUser_img_url());
                ImageShowActivity.actionStart(getActivity(), strings);
                break;
            case R.id.rl_set://设置
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.rl_xiaoxi://消息
                MessageActivity.actionStart(getContext());
                break;
            case R.id.tv_all:
                MyOrderActivity.actionStart(getActivity(), "");
                break;
            case R.id.iv_daifukuan:
            case R.id.tv_daifukuan:
                MyOrderActivity.actionStart(getActivity(), "待付款");
                break;
            case R.id.iv_daifahuo:
            case R.id.tv_daifahuo:
                MyOrderActivity.actionStart(getActivity(), "待发货");
                break;
            case R.id.iv_daishouhuo:
            case R.id.tv_daishouhuo:
                MyOrderActivity.actionStart(getActivity(), "待收货");
                break;
            case R.id.iv_pingjia:
            case R.id.tv_pingjia:
                MyOrderActivity.actionStart(getActivity(), "待评价");
                break;
            case R.id.iv_daodian:
            case R.id.tv_daodian:
                MyOrderActivity.actionStart(getActivity(), "到店");
                break;
            case R.id.ll_dianpushoucang:
                DianPuListActivity.actionStart(getActivity());
                break;
            case R.id.ll_shangpinshoucang:
                ShangPinShouCangActivity.actionStart(getActivity());
                break;
        }
    }

    private BaseAnimatorSet mBasIn = new BounceBottomEnter();
    private BaseAnimatorSet mBasOut = new SlideBottomExit();

    private void clickHuancun() {
        VipListActivity.actionStart(getContext());
    }

    @Override
    protected void immersionInit(ImmersionBar mImmersionBar) {
        mImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.shenlanhei).init();
    }


    @Override
    protected boolean immersionEnabled() {
        return true;
    }

}
