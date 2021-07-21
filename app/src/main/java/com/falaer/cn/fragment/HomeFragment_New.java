package com.falaer.cn.fragment;

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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.falaer.cn.activity.dingdan.MyOrderActivity;
import com.falaer.cn.activity.fenxiang_tuisong.TuanYouTuiGuangActivity;
import com.falaer.cn.activity.saoma.ScanActivity;
import com.falaer.cn.adapter.FunctionListAdapter;
import com.falaer.cn.config.Radius_GlideImageLoader;
import com.falaer.cn.model.FunctionListBean;
import com.flyco.roundview.RoundRelativeLayout;
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
import com.falaer.cn.R;
import com.falaer.cn.activity.SheBeiLieBiaoActivity;
import com.falaer.cn.activity.gouwuche.GouWuCheActivity;
import com.falaer.cn.activity.zijian_shangcheng.FenLeiThirdActivity;
import com.falaer.cn.activity.zijian_shangcheng.ZiJianShopMallDetailsActivity;
import com.falaer.cn.adapter.DirectAdapter;
import com.falaer.cn.adapter.HotGoodsAdapter;
import com.falaer.cn.adapter.ZhiKongListAdapter;
import com.falaer.cn.adapter.gaiban.HomeReMenAdapter;
import com.falaer.cn.app.App;
import com.falaer.cn.app.AppConfig;
import com.falaer.cn.app.ConstanceValue;
import com.falaer.cn.app.Notice;
import com.falaer.cn.app.RxBus;
import com.falaer.cn.app.UIHelper;
import com.falaer.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.falaer.cn.basicmvp.BaseFragment;
import com.falaer.cn.callback.JsonCallback;
import com.falaer.cn.config.AppResponse;
import com.falaer.cn.config.PreferenceHelper;
import com.falaer.cn.config.UserManager;
import com.falaer.cn.dialog.LordingDialog;
import com.falaer.cn.get_net.Urls;
import com.falaer.cn.model.Home;
import com.falaer.cn.model.TuiGuangMaModel;
import com.falaer.cn.util.AlertUtil;
import com.falaer.cn.util.GridAverageUIDecoration;
import com.falaer.cn.util.Utils;
import com.falaer.cn.view.ObservableScrollView;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;

import static com.falaer.cn.app.App.JINGDU;
import static com.falaer.cn.app.App.WEIDU;
import static com.falaer.cn.get_net.Urls.HOME_PICTURE;

/**
 * Created by Administrator on 2018/3/29 0029.
 */
public class HomeFragment_New extends BaseFragment implements ObservableScrollView.ScrollViewListener, View.OnClickListener {
    private static final String TAG = "HomeFragment";
    private ImageView ivGouwuche;
    private Unbinder unbinder;
    private ImageView iv_taoke;
    private HotGoodsAdapter hotGoodsAdapter = null;//热门商品适配器
    private LRecyclerViewAdapter hotLRecyclerViewAdapter = null;
    private List<Home.DataBean.IndexShowListBean> remenListBean = new ArrayList<>();
    private List<Home.DataBean.ShopListBean> groupList = new ArrayList<>();
    private DirectAdapter directAdapter = null;//直供商品适配器
    private LRecyclerViewAdapter directLRecyclerViewAdapter = null;
    private List<Home.DataBean.ProShowListBean> ziYingListBean = new ArrayList<>();
    private LinearLayout llMain;
    private View topPanel, middlePanel;
    private ConstraintLayout clZiYing_Middle, clReMen_Middle;
    private ConstraintLayout clZiYing_Top, clReMen_Top;
    private TextView tvZiYingTop, tvRemTop, tvZiYingZhiGongTop, tvReMenShangPinTop;
    private TextView tvZiYingMiddle, tvReMenMiddle, tvZiYingZhiGongMiddle, tvReMenShangPinMiddle;
    private View viewLineTop, viewLineMiddle, remenViewLineTop;
    private RecyclerView zhiKongList;//吃喝玩乐和智控
    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 10;
    private ZhiKongListAdapter zhiKongListAdapter;
    private FunctionListAdapter functionListAdapter;
    private List<Home.DataBean.IntellectListBean> intellectListBeanList;
    private List<FunctionListBean> functionListBeans;
    private String rimenOrZiYing = "0"; //0 自营直供 1 热门商品
    private ConstraintLayout clQuanBu;
    private SmartRefreshLayout smartRefreshLayout;
    private ObservableScrollView nestedScrollView;
    private RelativeLayout rl_bottom;


    private RecyclerView rlvRemen;
    private HomeReMenAdapter homeReMenAdapter;
    private RoundRelativeLayout rrlYuYinMianBan;
    private TextView tvResult;
    private int topHeight;

    private RecyclerView functionList;
    Banner woshibfaanfe;
    private Banner bannerXiuPeiChang;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getYaoQingNet(getActivity());
        //初始化定位
        initLocation();
        startLocation();
    }


    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_homefragment_new;
    }

    @Override
    protected void initView(View view) {

        nestedScrollView = view.findViewById(R.id.scrollView);
        functionList = view.findViewById(R.id.function_list);
        topPanel = view.findViewById(R.id.topPanel);
        middlePanel = view.findViewById(R.id.middlePanel);
        clZiYing_Top = topPanel.findViewById(R.id.cl_ziying);
        clReMen_Top = topPanel.findViewById(R.id.cl_remen);
        clZiYing_Middle = middlePanel.findViewById(R.id.cl_ziying);
        clReMen_Middle = middlePanel.findViewById(R.id.cl_remen);
        tvZiYingTop = topPanel.findViewById(R.id.tv_ziying);
        tvRemTop = topPanel.findViewById(R.id.tv_remen);
        llMain = view.findViewById(R.id.ll_main);
        clQuanBu = view.findViewById(R.id.cl_quanbu);
        ivGouwuche = view.findViewById(R.id.iv_gouwuche);
        rl_bottom = view.findViewById(R.id.rl_bottom);
        woshibfaanfe = view.findViewById(R.id.banner);

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


        //设置图片加载器
        woshibfaanfe.setImageLoader(new Radius_GlideImageLoader());
        tvZiYingZhiGongTop = topPanel.findViewById(R.id.ziyingzhigong);
        tvReMenShangPinTop = topPanel.findViewById(R.id.remenshangpin);
        tvZiYingMiddle = middlePanel.findViewById(R.id.tv_ziying);
        tvReMenMiddle = middlePanel.findViewById(R.id.tv_remen);
        tvZiYingZhiGongMiddle = middlePanel.findViewById(R.id.ziyingzhigong);
        tvReMenShangPinMiddle = middlePanel.findViewById(R.id.remenshangpin);
        //吃喝玩乐相关列表
        zhiKongList = view.findViewById(R.id.zhikong_list);
        nestedScrollView.setScrollViewListener(this);
        viewLineTop = topPanel.findViewById(R.id.view_line);
        rlvRemen = view.findViewById(R.id.rlv_remen);
        remenViewLineTop = topPanel.findViewById(R.id.view_line_remen);



        rlvRemen.setVisibility(View.VISIBLE);
        setZiYingOrReMenLine("1");
        rimenOrZiYing = "1";

//        clReMen_Middle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rlv_ziYing.setVisibility(View.GONE);
//                rlvRemen.setVisibility(View.VISIBLE);
//                setZiYingOrReMenLine("1");
//                rimenOrZiYing = "1";
//            }
//        });

//        clZiYing_Middle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rlv_ziYing.setVisibility(View.VISIBLE);
//                rlvRemen.setVisibility(View.GONE);
//                setZiYingOrReMenLine("0");
//                rimenOrZiYing = "0";
//            }
//        });

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 2);
        rlvRemen.addItemDecoration(new GridAverageUIDecoration(9, 10));
        rlvRemen.setLayoutManager(layoutManager1);
        homeReMenAdapter = new HomeReMenAdapter(R.layout.item_home_remen, remenListBean);
        rlvRemen.setAdapter(homeReMenAdapter);
        ivGouwuche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        GridLayoutManager gridLayoutManagerZHiKong = new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false);
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
                        Home.DataBean.IntellectListBean intellectListBean = (Home.DataBean.IntellectListBean) adapter.getData().get(position);
                        if (intellectListBean.getId().equals("1")) {
                            Notice n = new Notice();
                            n.type = ConstanceValue.MSG_ZHINENGJIAJU;
                            RxBus.getDefault().sendRx(n);
                        } else if (intellectListBean.getId().equals("2")) {
                            SheBeiLieBiaoActivity.actionStart(getActivity(), "1");
                        } else if (intellectListBean.getId().equals("3")) {
                            SheBeiLieBiaoActivity.actionStart(getActivity(), "6");
                        } else if (intellectListBean.getId().equals("4")) {//空调
                            SheBeiLieBiaoActivity.actionStart(getActivity(), "5");
                        } else if (intellectListBean.getId().equals("5")) {//神灯控车
                            UIHelper.ToastMessage(getActivity(), "开发中,敬请期待");
                        }
                        break;
                }
            }
        });

        hotLRecyclerViewAdapter = new LRecyclerViewAdapter(hotGoodsAdapter);
        directLRecyclerViewAdapter = new LRecyclerViewAdapter(directAdapter);
        getData();
        homeReMenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ZiJianShopMallDetailsActivity.actionStart(getActivity(), remenListBean.get(position).getShop_product_id(), remenListBean.get(position).getWares_id());

            }
        });
        setZiYingOrReMenLine("1");


        initFuncTionList();
    }

    private void initFuncTionList() {

        FunctionListBean functionListBean = new FunctionListBean();
        functionListBean.id = "1";
        functionListBean.image = R.mipmap.home_nav_icon_saoyisao;
        functionListBean.textName = "扫一扫";


        FunctionListBean functionListBean1 = new FunctionListBean();
        functionListBean1.id = "2";
        functionListBean1.image = R.mipmap.home_icon_saomayanzheng;
        functionListBean1.textName = "续费";

        FunctionListBean functionListBean2 = new FunctionListBean();
        functionListBean2.id = "3";
        functionListBean2.textName = "订单";
        functionListBean2.image = R.mipmap.home_icon_dingdanguanli;

        FunctionListBean functionListBean3 = new FunctionListBean();
        functionListBean3.id = "4";
        functionListBean3.textName = "推广统计";
        functionListBean3.image = R.mipmap.home_icon_dingdanguanli1;


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        GridLayoutManager gridLayoutManagerZHiKong = new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false);
        functionList.setLayoutManager(gridLayoutManagerZHiKong);
        functionListAdapter = new FunctionListAdapter(R.layout.item_function, functionListBeans);
        functionListAdapter.openLoadAnimation();//默认为渐显效果
        functionList.setAdapter(zhiKongListAdapter);
        functionListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.constrain:

                        if (functionListBeans.get(0).id.equals("1")) {
                            //进入扫一扫
                            RxPermissions rxPermissions = new RxPermissions(getActivity());
                            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean granted) {
                                    if (granted) { // 在android 6.0之前会默认返回true
                                        ScanActivity.actionStart(getActivity());
                                    } else {
                                        Toast.makeText(getActivity(), "该应用需要赋予访问相机的权限，不开启将无法正常工作！", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else if (functionListBeans.get(1).id.equals("2")) {
                            //续费
                        } else if (functionListBeans.get(2).id.equals("3")) {
                            //订单
                            MyOrderActivity.actionStart(getActivity(), "");
                        } else if (functionListBeans.get(3).id.equals("4")) {
                            //推广统计
                            TuanYouTuiGuangActivity.actionStart(getActivity());
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
    public static String JiaMiToken;
    public static List<String> items = new ArrayList<>();
    public static List<Object> items_xiupeichang = new ArrayList<>();
    LordingDialog lordingDialog;

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04131");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(getActivity()).getAppToken());
        map.put("gps_x", PreferenceHelper.getInstance(getActivity()).getString(WEIDU, ""));
        map.put("gps_y", PreferenceHelper.getInstance(getActivity()).getString(JINGDU, ""));
        Gson gson = new Gson();
        OkGo.<AppResponse<Home.DataBean>>post(HOME_PICTURE)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Home.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Home.DataBean>> response) {
                        Logger.d(gson.toJson(response.body()));
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.setEnableRefresh(true);
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.setEnableLoadMore(false);
                        }
                        remenListBean = response.body().data.get(0).getIndexShowList();
                        groupList = response.body().data.get(0).getShopList();
                        ziYingListBean = response.body().data.get(0).getProShowList();
                        JiaMiToken = response.body().data.get(0).getI();
                        homeReMenAdapter.setNewData(remenListBean);
                        homeReMenAdapter.notifyDataSetChanged();
                        hotLRecyclerViewAdapter.notifyDataSetChanged();

                        if (woshibfaanfe != null) {
                            //设置图片集合
                            woshibfaanfe.setImages(items);
                            //banner设置方法全部调用完毕时最后调用
                            woshibfaanfe.start();
                            woshibfaanfe.setOnBannerListener(new OnBannerListener() {
                                @Override
                                public void OnBannerClick(int position) {
                                    if (response.body().data.get(0).getBannerList().get(position).getRotation_img_type().equals("1")) {
                                        ZiJianShopMallDetailsActivity.actionStart(getActivity(), response.body().data.get(0).getBannerList().get(position).getShop_product_id(), response.body().data.get(0).getBannerList().get(position).getWares_id());
                                    } else if (response.body().data.get(0).getBannerList().get(position).getRotation_img_type().equals("2")) {
                                        // startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("url", response.body().data.get(0).getBannerList().get(position).getHtml_url()));
                                        // DefaultX5WebView_HaveNameActivity.actionStart(getActivity(), response.body().data.get(0).getBannerList().get(position).getHtml_url(), "产品简介");
                                    } else if (response.body().data.get(0).getBannerList().get(position).getRotation_img_type().equals("3")) {
                                        //DaLiBaoActivity.actionStart(getActivity());
                                    }
                                }
                            });
                        }


                        items = new ArrayList<>();
                        if (response.body().data != null) {
                            for (int i = 0; i < response.body().data.get(0).getBannerList().size(); i++) {
                                items.add(response.body().data.get(0).getBannerList().get(i).getImg_url());
                            }
                        }
                        items_xiupeichang = new ArrayList<>();
                        items_xiupeichang.add(R.mipmap.banner_xiupeichang_1);
                        items_xiupeichang.add(R.mipmap.banner_xiupeichang_2);
                        intellectListBeanList = new ArrayList<>();
                        //下面展示首页顶部图片
                        intellectListBeanList.addAll(response.body().data.get(0).getIntellectList());
                        zhiKongListAdapter.setNewData(intellectListBeanList);
                        zhiKongListAdapter.notifyDataSetChanged();
                        RoundedCorners roundedCorners = new RoundedCorners(1);
                        taobaoPicture = response.body().data.get(0).getTao_shop_img();
                        jingdongPicture = response.body().data.get(0).getJindong_shop_img();
                        pinduoduoPicture = response.body().data.get(0).getPin_shop_img();
                        if (response.body().data.get(0).is_activity == null) {
                            return;
                        }
                        if (response.body().data.get(0).is_activity.equals("1")) {
                            return;
                        }
                        setHuoDong(response.body().data.get(0).getActivity());
                    }

                    @Override
                    public void onError(Response<AppResponse<Home.DataBean>> response) {

                        AlertUtil.t(getActivity(), response.getException().getMessage());
                    }

                    @Override
                    public void onStart(Request<AppResponse<Home.DataBean>, ? extends Request> request) {
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


    public void getYaoQingNet(Context cnt) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04341");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(cnt).getAppToken());
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
                    stopLocation();
                } else {
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

    private void setHuoDong(List<Home.DataBean.activity> activity) {
        if (activity.size() == 0) {
            return;
        }
        if (strFirst.equals("1")) {
            return;
        }
        strFirst = "1";
    }

    //是否展示viewline
    private String strViewLine; // 0不展示 1展示

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        int[] location = new int[2];
        clZiYing_Middle.getLocationOnScreen(location);
        int locationY = location[1];
        if (locationY <= topHeight && (topPanel.getVisibility() == View.GONE || topPanel.getVisibility() == View.INVISIBLE)) {
            topPanel.setVisibility(View.VISIBLE);
            tvRemTop.setVisibility(View.GONE);
            tvZiYingTop.setVisibility(View.GONE);
            clReMen_Top.setBackgroundResource(R.color.white);
            clZiYing_Top.setBackgroundResource(R.color.white);
            strViewLine = "1";
            rl_bottom.setVisibility(View.VISIBLE);

        }

        if (locationY > topHeight && topPanel.getVisibility() == View.VISIBLE) {
            topPanel.setVisibility(View.GONE);
            tvRemTop.setVisibility(View.VISIBLE);
            tvZiYingTop.setVisibility(View.VISIBLE);
            clReMen_Top.setBackgroundResource(R.color.grayfff5f5f5);
            clZiYing_Top.setBackgroundResource(R.color.grayfff5f5f5);
            strViewLine = "0";
            // viewLineTop.setVisibility(View.GONE);
            rl_bottom.setVisibility(View.GONE);

        }
    }

    /**
     * private TextView tvZiYingTop, tvRemTop, tvZiYingZhiGongTop, tvReMenShangPinTop;
     * private TextView tvZiYingMiddle, tvReMenMiddle, tvZiYingZhiGongMiddle, tvReMenShangPinMiddle;
     */
    //0 自营 1 热门
    private void setZiYingOrReMenLine(String remenOrZiYing) {

            tvZiYingTop.setTextColor(getActivity().getResources().getColor(R.color.black_666666));
            tvZiYingZhiGongTop.setTextColor(getActivity().getResources().getColor(R.color.text_color_3));
            tvZiYingMiddle.setTextColor(getActivity().getResources().getColor(R.color.black_666666));
            tvZiYingZhiGongMiddle.setTextColor(getActivity().getResources().getColor(R.color.text_color_3));
            tvRemTop.setTextColor(getActivity().getResources().getColor(R.color.color_FFFC0100));
            tvReMenShangPinTop.setTextColor(getActivity().getResources().getColor(R.color.color_FFFC0100));
            tvReMenMiddle.setTextColor(getActivity().getResources().getColor(R.color.color_FFFC0100));
            tvReMenShangPinMiddle.setTextColor(getActivity().getResources().getColor(R.color.color_FFFC0100));
            tvReMenMiddle.setBackgroundResource(R.drawable.bg_color_fc0100_1a);
            tvZiYingMiddle.setBackgroundResource(0);
            viewLineTop.setVisibility(View.GONE);
            remenViewLineTop.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {

    }
}


