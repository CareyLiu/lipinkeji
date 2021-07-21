package com.falaer.cn.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.falaer.cn.R;
import com.falaer.cn.activity.BindBoxActivity;
import com.falaer.cn.activity.SheBeiLieBiaoActivity;
import com.falaer.cn.activity.chelianwang.ScanAddCarActivity;
import com.falaer.cn.activity.dingdan.MyOrderActivity;
import com.falaer.cn.activity.fenxiang_tuisong.TuanYouTuiGuangActivity;
import com.falaer.cn.activity.saoma.ScanActivity;
import com.falaer.cn.activity.shuinuan.Y;
import com.falaer.cn.activity.vip.VipListActivity;
import com.falaer.cn.activity.zijian_shangcheng.FenLeiThirdActivity;
import com.falaer.cn.activity.zijian_shangcheng.ZiJianShopMallDetailsActivity;
import com.falaer.cn.adapter.gaiban.HomeReMenAdapter;
import com.falaer.cn.app.App;
import com.falaer.cn.app.AppConfig;
import com.falaer.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.falaer.cn.basicmvp.BaseFragment;
import com.falaer.cn.callback.JsonCallback;
import com.falaer.cn.config.AppResponse;
import com.falaer.cn.config.PreferenceHelper;
import com.falaer.cn.config.Radius_GlideImageLoader;
import com.falaer.cn.config.UserManager;
import com.falaer.cn.dialog.LordingDialog;
import com.falaer.cn.get_net.Urls;
import com.falaer.cn.model.Home;
import com.falaer.cn.model.TuiGuangMaModel;
import com.falaer.cn.util.AlertUtil;
import com.falaer.cn.util.GridAverageUIDecoration;
import com.falaer.cn.util.Utils;
import com.falaer.cn.view.ObservableScrollView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions.RxPermissions;
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
import rx.functions.Action1;

import static com.falaer.cn.app.App.JINGDU;
import static com.falaer.cn.app.App.WEIDU;
import static com.falaer.cn.get_net.Urls.HOME_PICTURE;

public class HomeFragment extends BaseFragment implements ObservableScrollView.ScrollViewListener {
    @BindView(R.id.ll_dingwei)
    LinearLayout ll_dingwei;
    @BindView(R.id.rl_msg)
    RelativeLayout rl_msg;
    @BindView(R.id.ll_tab_saoyisao)
    LinearLayout ll_tab_saoyisao;
    @BindView(R.id.ll_tab_vip)
    LinearLayout ll_tab_vip;
    @BindView(R.id.ll_tab_dingdan)
    LinearLayout ll_tab_dingdan;
    @BindView(R.id.ll_tab_tuiguang)
    LinearLayout ll_tab_tuiguang;
    @BindView(R.id.ll_tab_fengnuan)
    LinearLayout ll_tab_fengnuan;
    @BindView(R.id.ll_tab_shuinuan)
    LinearLayout ll_tab_shuinuan;
    @BindView(R.id.ll_tab_kongtiao)
    LinearLayout ll_tab_kongtiao;
    @BindView(R.id.ll_tab_dianping)
    LinearLayout ll_tab_dianping;
    @BindView(R.id.ll_shangcheng)
    LinearLayout ll_shangcheng;
    @BindView(R.id.rv_shangpin)
    RecyclerView rlvRemen;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.banner)
    Banner banner;

    private List<Home.DataBean.IndexShowListBean> remenListBean = new ArrayList<>();
    private HomeReMenAdapter homeReMenAdapter;
    private LordingDialog lordingDialog;


    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_homefragment;
    }

    @Override
    protected void initView(View rootView) {
        rootView.setClickable(true);// 防止点击穿透，底层的fragment响应上层点击触摸事件
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
//        initLocation();
//        startLocation();
        initAdapter();
        initSM();
        getYaoQingNet(getActivity());
        getData();
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

    private void initAdapter() {
        rlvRemen.addItemDecoration(new GridAverageUIDecoration(9, 10));
        rlvRemen.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        homeReMenAdapter = new HomeReMenAdapter(R.layout.item_home_remen, remenListBean);
        rlvRemen.setAdapter(homeReMenAdapter);
        homeReMenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ZiJianShopMallDetailsActivity.actionStart(getActivity(), remenListBean.get(position).getShop_product_id(), remenListBean.get(position).getWares_id());
            }
        });
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {

    }

    @Override
    protected void immersionInit(ImmersionBar mImmersionBar) {
        mImmersionBar.with(this).statusBarDarkFont(true).fitsSystemWindows(true).statusBarColor(R.color.white).init();
    }

    @Override
    protected boolean immersionEnabled() {
        return true;
    }

    @OnClick({R.id.ll_dingwei, R.id.rl_msg, R.id.ll_tab_saoyisao, R.id.ll_tab_vip, R.id.ll_tab_dingdan, R.id.ll_tab_tuiguang, R.id.ll_tab_fengnuan, R.id.ll_tab_shuinuan, R.id.ll_tab_kongtiao, R.id.ll_tab_dianping, R.id.ll_shangcheng})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_dingwei:
                break;
            case R.id.rl_msg:
            case R.id.ll_tab_saoyisao:
                clickSaoyisao();
                break;
            case R.id.ll_tab_vip://续费相关
                VipListActivity.actionStart(getActivity());
                break;
            case R.id.ll_tab_dingdan:
                MyOrderActivity.actionStart(getActivity(), "");
                break;
            case R.id.ll_tab_tuiguang:
                TuanYouTuiGuangActivity.actionStart(getActivity());
                break;
            case R.id.ll_tab_fengnuan:
                SheBeiLieBiaoActivity.actionStart(getActivity(), "1");//风暖列表
                break;
            case R.id.ll_tab_shuinuan:
                SheBeiLieBiaoActivity.actionStart(getActivity(), "6");//水暖列表
                break;
            case R.id.ll_tab_kongtiao:
                SheBeiLieBiaoActivity.actionStart(getActivity(), "5");//空调列表
                break;
            case R.id.ll_tab_dianping:
                Y.t("研发中");
                break;
            case R.id.ll_shangcheng:
                FenLeiThirdActivity.actionStart(getActivity(), "热门商品", "1");
                break;
        }
    }

    private void clickSaoyisao() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (granted) { // 在android 6.0之前会默认返回true
                    ScanAddCarActivity.actionStart(getContext());
                } else {
                    Toast.makeText(getActivity(), "该应用需要赋予访问相机的权限，不开启将无法正常工作！", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

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
                        remenListBean = response.body().data.get(0).getIndexShowList();
                        homeReMenAdapter.setNewData(remenListBean);
                        homeReMenAdapter.notifyDataSetChanged();

                        List<Home.DataBean.BannerListBean> bannerList = response.body().data.get(0).getBannerList();
                        List<String> items = new ArrayList<>();
                        if (response.body().data != null) {
                            if (bannerList.size() > 0) {
                                for (int i = 0; i < bannerList.size(); i++) {
                                    items.add(bannerList.get(i).getImg_url());
                                }
                            }

                            banner.setImageLoader(new Radius_GlideImageLoader());
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
                                        // DefaultX5WebView_HaveNameActivity.actionStart(getActivity(), response.body().data.get(0).getBannerList().get(position).getHtml_url(), "产品简介");
                                    } else if (response.body().data.get(0).getBannerList().get(position).getRotation_img_type().equals("3")) {
                                        //DaLiBaoActivity.actionStart(getActivity());
                                    }
                                }
                            });
                        }
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
                        smartRefreshLayout.finishRefresh();
                    }
                });
    }

    public void getYaoQingNet(Context cnt) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04341");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(cnt).getAppToken());
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
}
