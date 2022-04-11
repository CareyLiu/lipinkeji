package com.lipinkeji.cn.fragment;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.activity.ShangchengActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.DefaultX5WebView_HaveNameActivity;
import com.lipinkeji.cn.activity.HomeActivity;
import com.lipinkeji.cn.activity.SheBeiLieBiaoActivity;
import com.lipinkeji.cn.activity.gouwuche.GouWuCheActivity;
import com.lipinkeji.cn.activity.homepage.DaLiBaoActivity;
import com.lipinkeji.cn.activity.zijian_shangcheng.ZiJianShopMallDetailsActivity;
import com.lipinkeji.cn.adapter.ShangchengAdapter;
import com.lipinkeji.cn.adapter.gaiban.HomeReMenAdapter;
import com.lipinkeji.cn.app.AppConfig;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.app.RxBus;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.lipinkeji.cn.basicmvp.BaseFragment;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.common.StringUtils;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.GlideImageLoader;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.Home_NewBean;
import com.lipinkeji.cn.util.AlertUtil;
import com.lipinkeji.cn.util.GridAverageUIDecoration;
import com.lipinkeji.cn.util.Utils;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.lipinkeji.cn.app.App.JINGDU;
import static com.lipinkeji.cn.app.App.WEIDU;
import static com.lipinkeji.cn.get_net.Urls.HOME_PICTURE;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.tv_city_name)
    TextView tvCityName;
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.ll_fengnuan)
    RelativeLayout llFengnuan;
    @BindView(R.id.ll_shuinuan)
    RelativeLayout llShuinuan;
    @BindView(R.id.ll_kongtiao)
    RelativeLayout llKongtiao;
    @BindView(R.id.ll_shangcheng)
    LinearLayout ll_shangcheng;
    @BindView(R.id.rv_shangcheng)
    RecyclerView rvShangcheng;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private List<Home_NewBean.DataBean.IndexShowListBean> remenListBean = new ArrayList<>();
    private HomeReMenAdapter homeReMenAdapter;
    private List<Home_NewBean.DataBean.BannerListBean> bannerList = new ArrayList<>();

    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_homefragment;
    }

    @Override
    protected void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        initSM();
        getData();

//        initAdapter();
//        initBanner();
    }

    private void initBanner() {
        List<Integer> items = new ArrayList<>();
        items.add(R.mipmap.banner_1);
        items.add(R.mipmap.banner_2);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(items);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void initAdapter() {
        rvShangcheng.setVisibility(View.GONE);
        rvShangcheng.setFocusable(false);
        rvShangcheng.addItemDecoration(new GridAverageUIDecoration(9, 10));
        rvShangcheng.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        homeReMenAdapter = new HomeReMenAdapter(R.layout.item_home_remen, remenListBean);
        rvShangcheng.setAdapter(homeReMenAdapter);

        homeReMenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ZiJianShopMallDetailsActivity.actionStart(getActivity(), remenListBean.get(position).getShop_product_id(), remenListBean.get(position).getWares_id());
            }
        });
    }

    private void initSM() {
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });
    }



    @OnClick({R.id.ll_fengnuan, R.id.ll_shuinuan, R.id.ll_kongtiao, R.id.ll_shangcheng})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_fengnuan:
                SheBeiLieBiaoActivity.actionStart(getActivity(), "1");
                break;
            case R.id.ll_shuinuan:
                SheBeiLieBiaoActivity.actionStart(getActivity(), "6");
                break;
            case R.id.ll_kongtiao:
                SheBeiLieBiaoActivity.actionStart(getActivity(), "5");
                break;
            case R.id.ll_shangcheng:
                ShangchengActivity.actionStart(getActivity());
                break;
        }
    }

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04436");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(getActivity()).getAppToken());
        map.put("gps_x", PreferenceHelper.getInstance(getActivity()).getString(WEIDU, ""));
        map.put("gps_y", PreferenceHelper.getInstance(getActivity()).getString(JINGDU, ""));
        map.put("page_number", "0");
        Gson gson = new Gson();
        OkGo.<AppResponse<Home_NewBean.DataBean>>post(HOME_PICTURE)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Home_NewBean.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Home_NewBean.DataBean>> response) {
                        Home_NewBean.DataBean dataBean = response.body().data.get(0);
//                        remenListBean = dataBean.getIndexShowList();
//                        homeReMenAdapter.setNewData(remenListBean);
//                        homeReMenAdapter.notifyDataSetChanged();

                        bannerList = dataBean.getBannerList();
                        if (bannerList != null) {
                            List<String> items = new ArrayList<>();
                            for (int i = 0; i < bannerList.size(); i++) {
                                items.add(bannerList.get(i).getImg_url());
                            }
                            banner.setImageLoader(new GlideImageLoader());
                            banner.setImages(items);
                            banner.setOnBannerListener(new OnBannerListener() {
                                @Override
                                public void OnBannerClick(int position) {
                                    if (response.body().data.get(0).getBannerList().get(position).getRotation_img_type().equals("1")) {
                                        ZiJianShopMallDetailsActivity.actionStart(getActivity(), response.body().data.get(0).getBannerList().get(position).getShop_product_id(), response.body().data.get(0).getBannerList().get(position).getWares_id());
                                    } else if (response.body().data.get(0).getBannerList().get(position).getRotation_img_type().equals("2")) {
                                        // startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("url", response.body().data.get(0).getBannerList().get(position).getHtml_url()));
                                        DefaultX5WebView_HaveNameActivity.actionStart(getActivity(), response.body().data.get(0).getBannerList().get(position).getHtml_url(), "产品简介");
                                    } else if (response.body().data.get(0).getBannerList().get(position).getRotation_img_type().equals("3")) {
                                        DaLiBaoActivity.actionStart(getActivity());
                                    }
                                }
                            });
                            banner.start();
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<Home_NewBean.DataBean>> response) {
                        AlertUtil.t(getActivity(), response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        smartRefreshLayout.finishRefresh();
                    }
                });
    }

    @Override
    protected boolean immersionEnabled() {
        return true;
    }

    @Override
    protected void immersionInit(ImmersionBar mImmersionBar) {
        super.immersionInit(mImmersionBar);
        mImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(false);
        mImmersionBar.init();
    }
}
