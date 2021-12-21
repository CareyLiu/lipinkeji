package com.lipinkeji.cn.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.MessageActivity;
import com.lipinkeji.cn.activity.SettingActivity;
import com.lipinkeji.cn.activity.dingdan.MyOrderActivity;
import com.lipinkeji.cn.activity.wode_page.AboutUsActivity;
import com.lipinkeji.cn.activity.wode_page.DianPuListActivity;
import com.lipinkeji.cn.activity.wode_page.MyQianBaoActivity;
import com.lipinkeji.cn.activity.wode_page.ShangPinShouCangActivity;
import com.lipinkeji.cn.app.App;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.basicmvp.BaseFragment;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.config.AppEvent;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.MineModel;
import com.lipinkeji.cn.util.phoneview.sample.ImageShowActivity;

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

    @BindView(R.id.rl_xiaoxi)
    RelativeLayout rlXiaoxi;
    @BindView(R.id.rl_set)
    RelativeLayout rlSet;
    @BindView(R.id.riv_image)
    CircleImageView rivImage;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_tab_shoucang)
    TextView tvTabShoucang;
    @BindView(R.id.ll_tab_shoucang)
    LinearLayout llTabShoucang;
    @BindView(R.id.tv_tab_guanzhu)
    TextView tvTabGuanzhu;
    @BindView(R.id.ll_tab_guanzhu)
    LinearLayout llTabGuanzhu;
    @BindView(R.id.tv_tab_jifen)
    TextView tvTabJifen;
    @BindView(R.id.ll_tab_jifen)
    LinearLayout llTabJifen;
    @BindView(R.id.tv_tab_kaquan)
    TextView tvTabKaquan;
    @BindView(R.id.ll_tab_kaquan)
    LinearLayout llTabKaquan;
    @BindView(R.id.ll_qianbao)
    LinearLayout llQianbao;
    @BindView(R.id.ll_dingdan_quan)
    LinearLayout llDingdanQuan;
    @BindView(R.id.ll_dingdan_daifukuan)
    LinearLayout llDingdanDaifukuan;
    @BindView(R.id.ll_dingdan_daifahuo)
    LinearLayout llDingdanDaifahuo;
    @BindView(R.id.ll_dingdan_daishouhuo)
    LinearLayout llDingdanDaishouhuo;
    @BindView(R.id.ll_dingdan_pingjia)
    LinearLayout llDingdanPingjia;
    @BindView(R.id.ll_dingdan_tuikuang)
    LinearLayout llDingdanTuikuang;
    @BindView(R.id.ll_dianpushoucang)
    LinearLayout llDianpushoucang;
    @BindView(R.id.ll_shangpinshoucang)
    LinearLayout llShangpinshoucang;
    @BindView(R.id.ll_guanyuwomen)
    LinearLayout llGuanyuwomen;
    @BindView(R.id.srL_smart)
    SmartRefreshLayout srLSmart;

    private Unbinder unbinder;
    private MineModel.DataBean dataBean;
    private String agentUrl = "";

    @Override
    protected void initLogic() {
        srLSmart.setEnableLoadMore(false);
        srLSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getNet();
            }
        });
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
        unbinder = ButterKnife.bind(this, rootView);
        AppEvent.getClassEvent().addObserver(this);
        initData();
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
                        srLSmart.finishRefresh();
                        dataBean = response.body().data.get(0);
                        Glide.with(getActivity()).load(response.body().data.get(0).user_h_img_url).into(rivImage);
                        tvName.setText(dataBean.getUser_name());
                        tvPhone.setText(dataBean.getUser_phone());

                        PreferenceHelper.getInstance(getActivity()).putString(App.CUNCHUBIND_ALIPAY, response.body().data.get(0).getAlipay_number_check());
                        PreferenceHelper.getInstance(getActivity()).putString(App.CUNCHU_ZHIFUMIMA, response.body().data.get(0).getPay_pwd_check());//1 已经设置 2 未设置
                        PreferenceHelper.getInstance(getActivity()).putString(App.CUNCHUBIND_WEIXINPAY, response.body().data.get(0).getWx_pay_number_check());//1 已经设置 2 未设置
                        PreferenceHelper.getInstance(getActivity()).putString(App.CUN_GEREN_TOUXIANG, response.body().data.get(0).getUser_img_url());

                        agentUrl = response.body().data.get(0).getAgent_url();

                        tvTabShoucang.setText(dataBean.getCollect_ware_count());
                        tvTabGuanzhu.setText(dataBean.getCollect_shop_count());
                        tvTabJifen.setText("0");
                        tvTabKaquan.setText(dataBean.getVoucher_count());
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

    @Override
    protected void immersionInit(ImmersionBar mImmersionBar) {
        mImmersionBar.with(this).init();
    }

    @Override
    protected boolean immersionEnabled() {
        return true;
    }

    @OnClick({R.id.rl_xiaoxi, R.id.rl_set, R.id.riv_image, R.id.ll_tab_shoucang, R.id.ll_tab_guanzhu, R.id.ll_tab_jifen, R.id.ll_tab_kaquan, R.id.ll_qianbao, R.id.ll_dingdan_quan, R.id.ll_dingdan_daifukuan, R.id.ll_dingdan_daifahuo, R.id.ll_dingdan_daishouhuo, R.id.ll_dingdan_pingjia, R.id.ll_dingdan_tuikuang, R.id.ll_dianpushoucang, R.id.ll_shangpinshoucang, R.id.ll_guanyuwomen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_xiaoxi:
                MessageActivity.actionStart(getContext());
                break;
            case R.id.rl_set:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.riv_image:
                ArrayList<String> strings = new ArrayList<>();
                strings.add(dataBean.getUser_img_url());
                ImageShowActivity.actionStart(getActivity(), strings);
                break;
            case R.id.ll_tab_shoucang:
                break;
            case R.id.ll_tab_guanzhu:
                break;
            case R.id.ll_tab_jifen:
                break;
            case R.id.ll_tab_kaquan:
                break;
            case R.id.ll_qianbao:
                MyQianBaoActivity.actionStart(getActivity());
                break;
            case R.id.ll_dingdan_quan:
                MyOrderActivity.actionStart(getActivity(), "");
                break;
            case R.id.ll_dingdan_daifukuan:
                MyOrderActivity.actionStart(getActivity(), "待付款");
                break;
            case R.id.ll_dingdan_daifahuo:
                MyOrderActivity.actionStart(getActivity(), "待发货");
                break;
            case R.id.ll_dingdan_daishouhuo:
                MyOrderActivity.actionStart(getActivity(), "待收货");
                break;
            case R.id.ll_dingdan_pingjia:
                MyOrderActivity.actionStart(getActivity(), "待评价");
                break;
            case R.id.ll_dingdan_tuikuang:
                MyOrderActivity.actionStart(getActivity(), "到店");
                break;
            case R.id.ll_dianpushoucang:
                DianPuListActivity.actionStart(getActivity());
                break;
            case R.id.ll_shangpinshoucang:
                ShangPinShouCangActivity.actionStart(getActivity());
                break;
            case R.id.ll_guanyuwomen:
                AboutUsActivity.actionStart(getActivity());
                break;
        }
    }
}
