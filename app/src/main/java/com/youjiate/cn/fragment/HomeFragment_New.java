package com.youjiate.cn.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.flyco.roundview.RoundRelativeLayout;
import com.github.jdsjlzx.ItemDecoration.GridItemDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youjiate.cn.R;
import com.youjiate.cn.activity.DefaultX5WebView_HaveNameActivity;
import com.youjiate.cn.activity.SheBeiLieBiaoActivity;
import com.youjiate.cn.activity.TuanYouWebView;
import com.youjiate.cn.activity.gouwuche.GouWuCheActivity;
import com.youjiate.cn.activity.homepage.DaLiBaoActivity;

import com.youjiate.cn.activity.saoma.ScanActivity;

import com.youjiate.cn.activity.tongcheng58.GongJiangLieBiaoNewActivity;
import com.youjiate.cn.activity.tongcheng58.TongChengMainActivity;
import com.youjiate.cn.activity.tuangou.TuanGouShangJiaListActivity;
import com.youjiate.cn.activity.xin_tuanyou.TuanYouList;
import com.youjiate.cn.activity.xiupeichang.XiuPeiChangHomeActivity;
import com.youjiate.cn.activity.zijian_shangcheng.FenLeiThirdActivity;
import com.youjiate.cn.activity.zijian_shangcheng.ZiJianShopMallActivity;
import com.youjiate.cn.activity.zijian_shangcheng.ZiJianShopMallDetailsActivity;
import com.youjiate.cn.adapter.ChiHeWanLeListAdapter;
import com.youjiate.cn.adapter.DirectAdapter;
import com.youjiate.cn.adapter.HotGoodsAdapter;
import com.youjiate.cn.adapter.ShengHuoListAdapter;
import com.youjiate.cn.adapter.ZhiKongListAdapter;
import com.youjiate.cn.adapter.gaiban.HomeReMenAdapter;
import com.youjiate.cn.app.App;
import com.youjiate.cn.app.AppConfig;
import com.youjiate.cn.app.ConstanceValue;
import com.youjiate.cn.app.Notice;
import com.youjiate.cn.app.RxBus;
import com.youjiate.cn.app.UIHelper;
import com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.youjiate.cn.basicmvp.BaseFragment;
import com.youjiate.cn.callback.JsonCallback;
import com.youjiate.cn.common.StringUtils;
import com.youjiate.cn.config.AppResponse;
import com.youjiate.cn.config.PreferenceHelper;
import com.youjiate.cn.config.Radius_GlideImageLoader;

import com.youjiate.cn.config.Radius_XiuPeiChangImageLoader;
import com.youjiate.cn.config.UserManager;
import com.youjiate.cn.dialog.LordingDialog;
import com.youjiate.cn.get_net.Urls;
import com.youjiate.cn.model.AccessListModel;
import com.youjiate.cn.model.Home_NewBean;
import com.youjiate.cn.model.TuiGuangMaModel;
import com.youjiate.cn.util.AlertUtil;
import com.youjiate.cn.util.GlideShowImageUtils;
import com.youjiate.cn.util.GridAverageUIDecoration;
import com.youjiate.cn.util.Utils;
import com.youjiate.cn.view.ObservableScrollView;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.youjiate.cn.app.App.JINGDU;
import static com.youjiate.cn.app.App.WEIDU;
import static com.youjiate.cn.get_net.Urls.HOME_PICTURE;

/**
 * Created by Administrator on 2018/3/29 0029.
 */
public class HomeFragment_New extends BaseFragment implements ObservableScrollView.ScrollViewListener, View.OnClickListener {
    private static final String TAG = "HomeFragment";


    ImageView ivGouwuche;
    @BindView(R.id.tv_city_name)
    TextView tvCityName;


    private View view;
    private Unbinder unbinder;
    private ImageView iv_taoke;
    HotGoodsAdapter hotGoodsAdapter = null;//热门商品适配器
    LRecyclerViewAdapter hotLRecyclerViewAdapter = null;
    List<Home_NewBean.DataBean.IndexShowListBean> remenListBean = new ArrayList<>();
    //List<Home_NewBean.DataBean.ShopListBean> groupList = new ArrayList<>();
    DirectAdapter directAdapter = null;//直供商品适配器
    LRecyclerViewAdapter directLRecyclerViewAdapter = null;
    // List<Home_NewBean.DataBean.ProShowListBean> ziYingListBean = new ArrayList<>();
    public int choosePostion = 99999;

    LinearLayout llMain;
    // LRecyclerView groupRecyclerView;

    Banner banner;


    private View topPanel, middlePanel;
    private int topHeight;

    private LinearLayout llBackground_Top, llBackground_Middle;
    private TextView tvZiYingTop, tvRemTop, tvZiYingZhiGongTop, tvReMenShangPinTop;
    // private TextView tvZiYingMiddle, tvReMenMiddle, tvZiYingZhiGongMiddle, tvReMenShangPinMiddle;
    private RelativeLayout rlXiuPeiChang;
    private Banner bannerXiuPeiChang;
    private ImageView ivEnterXiuPeiChang;


    private View viewLineTop, viewLineMiddle, remenViewLineTop;
    RecyclerView zhiKongList, shengHuoList;//吃喝玩乐和智控
    private RelativeLayout llShengHuo;
    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 10;


    //  List<Home_NewBean.DataBean.IconListBean> chiHeWanLeListBeans;


    ZhiKongListAdapter zhiKongListAdapter;
    ShengHuoListAdapter shengHuoListAdapter;
    List<Home_NewBean.DataBean.YrListBean> intellectListBeanList;

    List<Home_NewBean.DataBean.CarServiceListBean> lifeListBeans;
    ImageView ivDaLiBao;//大礼包
    ImageView ivZiJian;
    ImageView tianMaoOrTaoBao;

    ImageView ziYingZhiGon;
    ImageView reMenShangPin;
    ConstraintLayout ll_shagnchengzhuanqu;

    private String rimenOrZiYing = "0"; //0 自营直供 1 热门商品
    ConstraintLayout clQuanBu;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getYaoQingNet(getActivity());
        //初始化定位
        initLocation();
        startLocation();

        // getZhuJiNet();
    }

    SmartRefreshLayout smartRefreshLayout;
    ObservableScrollView nestedScrollView;
    RelativeLayout rl_bottom;

    private ImageView ivClose;

    RecyclerView rlv_ziYing;
    //HomeZiYingAdapter homeZiYingAdapter;
    RecyclerView rlvRemen;
    HomeReMenAdapter homeReMenAdapter;
    private ImageView iv_home_xiaoxi;

    RoundRelativeLayout rrlYuYinMianBan;
    TextView tvResult;

    @Override
    protected void initLogic() {

    }

    private void getZhuJiNet() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "16076");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(getActivity()).getAppToken());
        map.put("family_id", PreferenceHelper.getInstance(getActivity()).getString(AppConfig.FAMILY_ID, ""));
        Gson gson = new Gson();
        OkGo.<AppResponse<AccessListModel.DataBean>>post(Urls.ZHINENGJIAJU)
                .tag(this)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<AccessListModel.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<AccessListModel.DataBean>> response) {

                    }

                    @Override
                    public void onError(Response<AppResponse<AccessListModel.DataBean>> response) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_homefragment_new;
    }

    LottieAnimationView animationView;
    Banner banner2;
    List<Integer> itemList;

    @Override
    protected void initView(View view) {
//        rrlYuYinMianBan = view.findViewById(R.id.rrl_yuyin_mianban);
//        tvResult = view.findViewById(R.id.tv_result);
        itemList = new ArrayList<>();
        itemList.add(R.mipmap.qiche_bannner_1);
        itemList.add(R.mipmap.qiche_bannner_2);
        itemList.add(R.mipmap.qiche_bannner_3);


        banner2 = view.findViewById(R.id.banner2);
        nestedScrollView = view.findViewById(R.id.scrollView);
        topPanel = view.findViewById(R.id.topPanel);
        middlePanel = view.findViewById(R.id.middlePanel);

        tvZiYingTop = topPanel.findViewById(R.id.tv_ziying);
        tvRemTop = topPanel.findViewById(R.id.tv_remen);
        llMain = view.findViewById(R.id.ll_main);

        banner = view.findViewById(R.id.banner);
        ivDaLiBao = view.findViewById(R.id.iv_dalibao);
        clQuanBu = view.findViewById(R.id.cl_quanbu);
        ll_shagnchengzhuanqu = view.findViewById(R.id.ll_shagnchengzhuanqu);

        ziYingZhiGon = view.findViewById(R.id.iv_ziying);
        reMenShangPin = view.findViewById(R.id.iv_remen);
        ivGouwuche = view.findViewById(R.id.iv_gouwuche);
        animationView = view.findViewById(R.id.animation_view);
        bannerXiuPeiChang = view.findViewById(R.id.banner_xiupeichang);
        animationView.setAnimation("freec3_data.json");
        animationView.setImageAssetsFolder("gifs/");
        animationView.loop(true);
        animationView.playAnimation();

        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaLiBaoActivity.actionStart(getActivity());
            }
        });
        rl_bottom = view.findViewById(R.id.rl_bottom);

        ivClose = view.findViewById(R.id.iv_close);
        clQuanBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rimenOrZiYing.equals("0")) {
                    FenLeiThirdActivity.actionStart(getActivity(), "品牌直供", "2");
                } else {
                    FenLeiThirdActivity.actionStart(getActivity(), "热门商品", "1");
                }
            }
        });

        /**
         *     private TextView tvZiYingTop, tvRemTop, tvZiYingZhiGongTop, tvReMenShangPinTop;
         *     private TextView tvZiYingMiddle, tvReMenMiddle, tvZiYingZhiGongMiddle, tvReMenShangPinMiddle;
         */


        tvZiYingZhiGongTop = topPanel.findViewById(R.id.ziyingzhigong);
        tvReMenShangPinTop = topPanel.findViewById(R.id.remenshangpin);


        zhiKongList = view.findViewById(R.id.zhikong_list);
        zhiKongList.setFocusable(false);

        shengHuoList = view.findViewById(R.id.rlv_shenghuo);
        shengHuoList.setFocusable(false);

        llShengHuo = view.findViewById(R.id.ll_shenghuo1);

        ivZiJian = view.findViewById(R.id.iv_zijian);
        tianMaoOrTaoBao = view.findViewById(R.id.iv_tianmao_or_taobao);
        nestedScrollView.setScrollViewListener(this);
        rlv_ziYing = view.findViewById(R.id.rlv_ziying_or_remen);
        rlv_ziYing.setFocusable(false);
        viewLineTop = topPanel.findViewById(R.id.view_line);
        // viewLineMiddle = middlePanel.findViewById(R.id.view_line);
        rlvRemen = view.findViewById(R.id.rlv_remen);
        rlvRemen.setFocusable(false);
        remenViewLineTop = topPanel.findViewById(R.id.view_line_remen);
        rlXiuPeiChang = view.findViewById(R.id.rl_xiupeichang);
        rlXiuPeiChang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UIHelper.ToastMessage(getActivity(), "点击了修配厂");
                //XiuPeiChangHomeActivity.actionStart(getActivity());
                //      TuanGouShangJiaListActivity.actionStart(getActivity(), "7");
            }
        });


//        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
//        rlv_ziYing.addItemDecoration(new GridAverageUIDecoration(14, 10));
//        rlv_ziYing.setLayoutManager(layoutManager);
        //homeZiYingAdapter = new HomeZiYingAdapter(R.layout.item_home_ziying, ziYingListBean);
        //rlv_ziYing.setAdapter(homeZiYingAdapter);


        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 2);
        rlvRemen.addItemDecoration(new GridAverageUIDecoration(9, 10));
        rlvRemen.setLayoutManager(layoutManager1);
        homeReMenAdapter = new HomeReMenAdapter(R.layout.item_home_remen, remenListBean);
        rlvRemen.setAdapter(homeReMenAdapter);

        ivGouwuche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UIHelper.ToastMessage(getActivity(),"功能开发中");
                GouWuCheActivity.actionStart(getActivity());
            }
        });

        view.setClickable(true);// 防止点击穿透，底层的fragment响应上层点击触摸事件
        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });
        smartRefreshLayout.setEnableLoadMore(false);
        unbinder = ButterKnife.bind(this, view);
        iv_home_xiaoxi = view.findViewById(R.id.iv_home_xiaoxi);

        iv_home_xiaoxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.ToastMessage(getActivity(), "暂未开放此功能，敬请期待");

            }
        });

        ImageView ivSaoMa = view.findViewById(R.id.iv_saoma);
        ivSaoMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CrashReport.testJavaCrash();
                RxPermissions rxPermissions = new RxPermissions(getActivity());
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) { // 在android 6.0之前会默认返回true
                            ScanActivity.actionStart(getActivity());
                            //YanShiActivity.actionStart(getActivity());
                            //GongJiangLieBiaoNewActivity.actionStart(getActivity());
                            //BianMinFaBuActivity.actionStart(getActivity());
                            //GongJiangRuZhuActivity.actionStart(getActivity());
//                            Intent intent = new Intent(getActivity(), PoiKeywordSearchActivity.class);
//                            startActivity(intent);
                            // KongQiJianCe_NewActvity.actionStart(getActivity(),"0x1100033");
//                            Intent intent = new Intent(getActivity(), TuBiaoActivity.class);
//                            startActivity(intent);
//                            Intent intent = new Intent(getActivity(), ShiJianFenFaDemoActivity.class);
//                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "该应用需要赋予访问相机的权限，不开启将无法正常工作！", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                //FenXiangTuiSongActivity.actionStart(getActivity());

            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        GridLayoutManager gridLayoutManagerChi_He = new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false);
        // gridLayoutManagerChi_He.setOrientation(LinearLayoutManager.VERTICAL);
        //   revisionRecycler.setLayoutManager(gridLayoutManager);


        shengHuoListFuc();
        // ivJd = header.findViewById(R.id.iv_jd);
        //  LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        // linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        GridLayoutManager gridLayoutManagerZHiKong = new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
        //    gridLayoutManagerZHiKong.setOrientation(LinearLayoutManager.VERTICAL);
        zhiKongList.setLayoutManager(gridLayoutManagerZHiKong);

        zhiKongListAdapter = new ZhiKongListAdapter(R.layout.item_zhikong, intellectListBeanList);
        zhiKongListAdapter.openLoadAnimation();//默认为渐显效果
        zhiKongList.setAdapter(zhiKongListAdapter);
        zhiKongListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.constrain:
                        /**
                         *  1.智能家居 2.风暖3.水暖 4.空调 5.神灯控车
                         */
                        Home_NewBean.DataBean.YrListBean intellectListBean = (Home_NewBean.DataBean.YrListBean) adapter.getData().get(position);
                        if (intellectListBean.getId().equals("4")) {
                            Notice n = new Notice();
                            n.type = ConstanceValue.MSG_ZHINENGJIAJU;
                            RxBus.getDefault().sendRx(n);
                        } else if (intellectListBean.getId().equals("1")) {
                            SheBeiLieBiaoActivity.actionStart(getActivity(), "1");
                        } else if (intellectListBean.getId().equals("2")) {
                            SheBeiLieBiaoActivity.actionStart(getActivity(), "6");
                        } else if (intellectListBean.getId().equals("3")) {//空调
                            SheBeiLieBiaoActivity.actionStart(getActivity(), "5");
                            break;
                        }
                        break;
                }
            }
        });


        GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                .setHorizontal(R.dimen.default_divider_padding_5dp)
                .setVertical(R.dimen.default_divider_padding_5dp)
                .setColorResource(R.color.white)
                .build();


        hotLRecyclerViewAdapter = new LRecyclerViewAdapter(hotGoodsAdapter);
        directLRecyclerViewAdapter = new LRecyclerViewAdapter(directAdapter);


        //设置图片加载器
        banner.setImageLoader(new Radius_GlideImageLoader());
        banner2.setImageLoader(new Radius_GlideImageLoader());

        bannerXiuPeiChang.setImageLoader(new Radius_XiuPeiChangImageLoader());
        getData();
        ///getYaoQingNet(getActivity());
        //   getNet();
        hotLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                // ZiJianShopMallDetailsActivity.actionStart(getActivity(), remenListBean.get(position).getShop_product_id(), remenListBean.get(position).getWares_id());
                //  TuanGouShangJiaListActivity.actionStart(getActivity(), "7");
                // startActivity(new Intent(getActivity(), GoosDetailsActivity.class)
                //       .putExtra("shop_product_id", hotList.get(position).getShop_product_id())
                //     .putExtra("wares_id", hotList.get(position).getWares_id()));
            }
        });


//        homeZiYingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ZiJianShopMallDetailsActivity.actionStart(getActivity(), ziYingListBean.get(position).getShop_product_id(), ziYingListBean.get(position).getWares_id());
//
//            }
//        });

        homeReMenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ZiJianShopMallDetailsActivity.actionStart(getActivity(), remenListBean.get(position).getShop_product_id(), remenListBean.get(position).getWares_id());

            }
        });

        tianMaoOrTaoBao.setOnClickListener(this);
        ivDaLiBao.setOnClickListener(this);
        // ivJd.setOnClickListener(this);

        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_DALIBAO_SUCCESS) {
                    ivDaLiBao.setVisibility(View.GONE);
                }
            }
        }));

        // mImmersionBar.with(this).statusBarDarkFont(true).fitsSystemWindows(true).statusBarColor(R.color.white).init();
        // setZiYingOrReMenLine("0");


    }

    private void shengHuoListFuc() {
        GridLayoutManager gridLayoutManagerZHiKong = new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
        //    gridLayoutManagerZHiKong.setOrientation(LinearLayoutManager.VERTICAL);
        shengHuoList.setLayoutManager(gridLayoutManagerZHiKong);

        shengHuoListAdapter = new ShengHuoListAdapter(R.layout.item_zhikong, lifeListBeans);
        shengHuoListAdapter.openLoadAnimation();//默认为渐显效果
        shengHuoList.setAdapter(shengHuoListAdapter);
        shengHuoListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.constrain:
                        //UIHelper.ToastMessage(getActivity(), "生活");
                        String service_type = lifeListBeans.get(position).getId();
                        switch (service_type) {

                            case "6":
                            case "11":
                            case "8":
                            case "3":
                                GongJiangLieBiaoNewActivity.actionStart(getActivity(), service_type);
                                break;

                            case "32":
                                GongJiangLieBiaoNewActivity.actionStart(getActivity(), service_type);
                                break;
                            case "33":
                                GongJiangLieBiaoNewActivity.actionStart(getActivity(), service_type);
                                break;
                            case "34":
                                GongJiangLieBiaoNewActivity.actionStart(getActivity(), service_type);
                                break;
                            case "31"://
                                TongChengMainActivity.actionStart(getActivity());
                                break;

                        }
                        break;
                }
            }
        });
    }


    @Override
    protected boolean immersionEnabled() {
        return true;
    }

    @Override
    protected void immersionInit(ImmersionBar mImmersionBar) {
        mImmersionBar.with(this).statusBarDarkFont(true).fitsSystemWindows(true).statusBarColor(R.color.white).init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    String taobaoPicture, jingdongPicture, pinduoduoPicture;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_tianmao_or_taobao:
                //  TaoBao_Jd_PinDuoDuoActivity.actionStart(getActivity(), taobaoPicture, jingdongPicture, pinduoduoPicture);
                break;
            case R.id.iv_dalibao:
                DaLiBaoActivity.actionStart(getActivity());
                break;
        }
    }

    public static String JiaMiToken;
    public static List<String> items = new ArrayList<>();
    public static List<Object> items_xiupeichang = new ArrayList<>();
    LordingDialog lordingDialog;

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04435");
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

                        Logger.d(gson.toJson(response.body()));
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.setEnableRefresh(true);
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.setEnableLoadMore(false);
                        }

                        remenListBean = response.body().data.get(0).getIndexShowList();
                        // groupList = response.body().data.get(0).getShopList();
                        // ziYingListBean = response.body().data.get(0).getProShowList();
                        // JiaMiToken = response.body().data.get(0).getI();
                        // intellectListBeanList = response.body().data.get(0).getIntellectList();
                        //  chiHeWanLeListBeans = response.body().data.get(0).getIconList();

                        // homeZiYingAdapter.setNewData(ziYingListBean);
                        // homeZiYingAdapter.notifyDataSetChanged();


                        homeReMenAdapter.setNewData(remenListBean);
                        homeReMenAdapter.notifyDataSetChanged();


                        //  groupRecyclerView.refreshComplete(REQUEST_COUNT);

                        hotLRecyclerViewAdapter.notifyDataSetChanged();


                        items = new ArrayList<>();
                        if (response.body().data != null) {
                            for (int i = 0; i < response.body().data.get(0).getBannerList().size(); i++) {
                                items.add(response.body().data.get(0).getBannerList().get(i).getImg_url());
                            }
                        }

                        if (banner != null) {
                            //设置图片集合
                            banner.setImages(items);
                            //banner设置方法全部调用完毕时最后调用
                            banner.start();
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
                        }

                        if (banner2 != null) {
                            //设置图片集合
                            banner2.setImages(itemList);
                            //banner设置方法全部调用完毕时最后调用
                            banner2.start();
                            banner2.setOnBannerListener(new OnBannerListener() {
                                @Override
                                public void OnBannerClick(int position) {
                                    TuanGouShangJiaListActivity.actionStart(getActivity(), "7");
                                    // XiuPeiChangHomeActivity.actionStart(getActivity());
//                                    if (response.body().data.get(0).getBannerList().get(position).getRotation_img_type().equals("1")) {
//                                        ZiJianShopMallDetailsActivity.actionStart(getActivity(), response.body().data.get(0).getBannerList().get(position).getShop_product_id(), response.body().data.get(0).getBannerList().get(position).getWares_id());
//                                    } else if (response.body().data.get(0).getBannerList().get(position).getRotation_img_type().equals("2")) {
//                                        // startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("url", response.body().data.get(0).getBannerList().get(position).getHtml_url()));
//                                        DefaultX5WebView_HaveNameActivity.actionStart(getActivity(), response.body().data.get(0).getBannerList().get(position).getHtml_url(), "产品简介");
//                                    } else if (response.body().data.get(0).getBannerList().get(position).getRotation_img_type().equals("3")) {
//                                        DaLiBaoActivity.actionStart(getActivity());
//                                    }
                                }
                            });
                        }


                        items_xiupeichang = new ArrayList<>();

                        items_xiupeichang.add(R.mipmap.banner_xiupeichang_1);
                        items_xiupeichang.add(R.mipmap.banner_xiupeichang_2);

                        if (bannerXiuPeiChang != null) {
                            //设置图片集合
                            bannerXiuPeiChang.setImages(items_xiupeichang);
                            //banner设置方法全部调用完毕时最后调用
                            bannerXiuPeiChang.start();
                            bannerXiuPeiChang.setOnBannerListener(new OnBannerListener() {
                                @Override
                                public void OnBannerClick(int position) {
                                    //   TuanGouShangJiaListActivity.actionStart(getActivity(), "7");
                                }
                            });
                        }


                        intellectListBeanList = new ArrayList<>();
                        lifeListBeans = new ArrayList<>();
                        //  chiHeWanLeListBeans = new ArrayList<>();
                        //下面展示首页顶部图片
                        intellectListBeanList.addAll(response.body().data.get(0).getYrList());
                        if (response.body().data.get(0).getCarServiceList() != null) {
                            lifeListBeans.addAll(response.body().data.get(0).getCarServiceList());
                        } else {
                            llShengHuo.setVisibility(View.GONE);
                        }

                        zhiKongListAdapter.setNewData(intellectListBeanList);
                        shengHuoListAdapter.setNewData(lifeListBeans);


                        zhiKongListAdapter.notifyDataSetChanged();


                        RoundedCorners roundedCorners = new RoundedCorners(1);
                        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);

                        ivZiJian.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ZiJianShopMallActivity.actionStart(getActivity());
                            }
                        });

                        ziYingZhiGon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                FenLeiThirdActivity.actionStart(getActivity(), "品牌直供", "2");
                            }
                        });
                        reMenShangPin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FenLeiThirdActivity.actionStart(getActivity(), "热门商品", "1");

                            }
                        });

                    }

                    @Override
                    public void onError(Response<AppResponse<Home_NewBean.DataBean>> response) {

                        AlertUtil.t(getActivity(), response.getException().getMessage());
                    }

                    @Override
                    public void onStart(Request<AppResponse<Home_NewBean.DataBean>, ? extends Request> request) {
                        super.onStart(request);
                        lordingDialog = new LordingDialog(getActivity());
                        lordingDialog.setTextMsg("正在加载请稍后...");
                        lordingDialog.show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        lordingDialog.dismiss();
                    }
                });


    }

    // public String shiFouYanzheng;

    public void getYaoQingNet(Context cnt) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04341");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(cnt).getAppToken());
        // map.put("shop_product_id", productId);
        //map.put("wares_id", warseId);

        Log.i("taoken_gg", UserManager.getManager(cnt).getAppToken());
        Gson gson = new Gson();
        OkGo.<AppResponse<TuiGuangMaModel.DataBean>>post(Urls.SERVER_URL + "shop_new/app/user")
                .tag(cnt)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<TuiGuangMaModel.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<TuiGuangMaModel.DataBean>> response) {

                        if (response.body().data.get(0).getDisplay().equals("0")) {
                            //没有上级
                            //shiFouYanzheng = "0";
                            PreferenceHelper.getInstance(getActivity()).putString(App.SHIFOUYOUSHANGJI, "0");
                        } else {
                            //有上级
                            //shiFouYanzheng = "1";
                            PreferenceHelper.getInstance(getActivity()).putString(App.SHIFOUYOUSHANGJI, "1");
                        }

                    }

                    @Override
                    public void onError(Response<AppResponse<TuiGuangMaModel.DataBean>> response) {
                        AlertUtil.t(cnt, response.getException().getMessage());
                    }
                });
    }


    /**
     * 定位监听
     */
    AMapLocationListener gaodeDingWeiListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    sb.append("提供者    : " + location.getProvider() + "\n");

                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + location.getBearing() + "\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : " + location.getSatellites() + "\n");
                    sb.append("国    家    : " + location.getCountry() + "\n");
                    sb.append("省            : " + location.getProvince() + "\n");
                    sb.append("市            : " + location.getCity() + "\n");
                    sb.append("城市编码 : " + location.getCityCode() + "\n");
                    sb.append("区            : " + location.getDistrict() + "\n");
                    sb.append("区域 码   : " + location.getAdCode() + "\n");
                    sb.append("地    址    : " + location.getAddress() + "\n");
                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
                    //定位完成的时间
                    sb.append("定位时间: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");

                    PreferenceHelper.getInstance(getActivity()).putString(JINGDU, String.valueOf(location.getLongitude()));
                    PreferenceHelper.getInstance(getActivity()).putString(WEIDU, String.valueOf(location.getLatitude()));
                    PreferenceHelper.getInstance(getActivity()).putString(AppConfig.LOCATION_CITY_NAME, location.getCity());
                    PreferenceHelper.getInstance(getActivity()).putString(AppConfig.ADDRESS, location.getAddress());

                    stopLocation();
                } else {

                    //"gps_x=45.666043" + "&" + "gps_y=126.605713";
                    // x 纬度 y 经度
                    PreferenceHelper.getInstance(getActivity()).putString(WEIDU, "45.666043");
                    PreferenceHelper.getInstance(getActivity()).putString(JINGDU, "126.605713");
                    PreferenceHelper.getInstance(getActivity()).putString(AppConfig.LOCATION_CITY_NAME, "哈尔滨");
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                sb.append("***定位质量报告***").append("\n");
                sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
                sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
                sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
                sb.append("* 网络类型：" + location.getLocationQualityReport().getNetworkType()).append("\n");
                sb.append("* 网络耗时：" + location.getLocationQualityReport().getNetUseTime()).append("\n");
                sb.append("****************").append("\n");
                //定位之后的回调时间
                sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //解析定位结果，
                String result = sb.toString();
                Log.i("Location_result", result);
            } else {
                PreferenceHelper.getInstance(getActivity()).putString(WEIDU, "45.666043");
                PreferenceHelper.getInstance(getActivity()).putString(JINGDU, "126.605713");
                PreferenceHelper.getInstance(getActivity()).putString(AppConfig.LOCATION_CITY_NAME, "哈尔滨");
                Log.i("Location_result", "定位失败");
            }

            String cityName = PreferenceHelper.getInstance(getActivity()).getString(AppConfig.LOCATION_CITY_NAME, "");
            Log.i("location_city", cityName);
            if (!StringUtils.isEmpty(cityName)) {
                tvCityName.setText(cityName);
            }

        }
    };

    /**
     * 获取GPS状态的字符串
     *
     * @param statusCode GPS状态码
     * @return
     */
    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(getActivity().getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(gaodeDingWeiListener);
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        //根据控件的选择，重新设置定位参数
        //resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     * is_activity	是否显示活动 1.没活动 2.有活动
     * img_url	图url
     * img_width	图宽
     * img_height	图高
     * html_url	跳转地址url（app用）
     * activity_type_id	类型 1.商品 2.广告
     * wares_id	商品id
     * shop_product_id	套餐id
     * is_share	是否分享 1.分享 2.不分享
     * share_title	分享标题
     * share_detail	分享描述
     * share_url	分享链接
     * share_img	分享图片
     */
    private String strFirst = "0";//0第一次 1第二次


    //是否展示viewline
    private String strViewLine; // 0不展示 1展示

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        int[] location = new int[2];
        // clZiYing_Middle.getLocationOnScreen(location);
        int locationY = location[1];
        // Log.e("locationY", locationY + " " + "topHeight的值是：" + topHeight);

        if (locationY <= topHeight && (topPanel.getVisibility() == View.GONE || topPanel.getVisibility() == View.INVISIBLE)) {
            // topPanel.setVisibility(View.VISIBLE);
            // tvRemTop.setVisibility(View.GONE);
            // tvZiYingTop.setVisibility(View.GONE);
            //  llBackground_Top.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            // clReMen_Top.setBackgroundResource(R.color.white);
            // clZiYing_Top.setBackgroundResource(R.color.white);
            //strViewLine = "1";
            //  viewLineTop.setVisibility(View.VISIBLE);
            rl_bottom.setVisibility(View.VISIBLE);

        }

        if (locationY > topHeight && topPanel.getVisibility() == View.VISIBLE) {
            // topPanel.setVisibility(View.GONE);
            // tvRemTop.setVisibility(View.VISIBLE);
            //tvZiYingTop.setVisibility(View.VISIBLE);
            //llBackground_Top.setBackgroundColor(getActivity().getResources().getColor(R.color.grayfff5f5f5));
            // clReMen_Top.setBackgroundResource(R.color.grayfff5f5f5);
            // clZiYing_Top.setBackgroundResource(R.color.grayfff5f5f5);
            // strViewLine = "0";
            // viewLineTop.setVisibility(View.GONE);
            rl_bottom.setVisibility(View.GONE);

        }
    }


//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//
//        Rect frame = new Rect();
//        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        int statusBarHeight = frame.top;//状态栏高度
//
//        int titleBarHeight = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();//标题栏高度
//        topHeight = titleBarHeight + statusBarHeight;
//    }


    /**
     * private TextView tvZiYingTop, tvRemTop, tvZiYingZhiGongTop, tvReMenShangPinTop;
     * private TextView tvZiYingMiddle, tvReMenMiddle, tvZiYingZhiGongMiddle, tvReMenShangPinMiddle;
     */

}


