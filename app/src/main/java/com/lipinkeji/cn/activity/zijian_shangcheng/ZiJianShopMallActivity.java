package com.lipinkeji.cn.activity.zijian_shangcheng;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.WebViewActivity;
import com.lipinkeji.cn.adapter.ZiJianAdapter;
import com.lipinkeji.cn.adapter.ZiJian_HeaderAdapter;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.GlideImageLoader;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.FenLeiContentModel;
import com.lipinkeji.cn.model.ZiJianHomeModel;
import com.lipinkeji.cn.project_A.zijian_interface.ZiJianHomeInterface;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.lipinkeji.cn.app.App.JINGDU;
import static com.lipinkeji.cn.app.App.WEIDU;
import static com.lipinkeji.cn.get_net.Urls.ZIJIANHOME;


public class ZiJianShopMallActivity extends BaseActivity implements ZiJianHomeInterface {
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    ZiJianAdapter ziJianAdapter;

    ZiJian_HeaderAdapter ziJian_headerAdapter;
    List<ZiJianHomeModel.DataBean.WaresListBean> objects;//δΈι’εε
    List<ZiJianHomeModel.DataBean.IconListBean> iconListBeans;//δΈ­ι΄ζ¨ͺηηitem


    private Banner banner;
    private RecyclerView rvcList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objects = new ArrayList<>();
        View view = View.inflate(this, R.layout.layout_zijian_header, null);
        rvcList = view.findViewById(R.id.rcv_list);
        banner = view.findViewById(R.id.banner);
        implHortialList();//δΈ­ι΄εθ‘¨
        ziJianAdapter = new ZiJianAdapter(R.layout.item_zijian, objects);

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        //   initHeaderView(view);

        //εε§εδΈδΈ
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        swipeTarget.setLayoutManager(gridLayoutManager);

        //  taoKeListAdapter = new TaoKeListAdapter(R.layout.layout_taokeshop, dataBeanList);
        ziJianAdapter.openLoadAnimation();//ι»θ?€δΈΊζΈζΎζζ
        swipeTarget.setAdapter(ziJianAdapter);
        //   refreshLayout.setEnableAutoLoadMore(true);
        //  smartRefreshLayout.setEnableRefresh(true);
        // refreshLayout.setEnableLoadMore(true);
        ziJianAdapter.addHeaderView(view);

        getNet();

        onClickFooterList();
        onClickHeaderList();

    }


    @Override
    public int getContentViewResId() {
        return R.layout.layout_zijian_shopmall;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();

        tv_title.setText("θͺε»Ίεε");
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

    /**
     * η¨δΊεΆδ»Activtyθ·³θ½¬ε°θ―₯Activity
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ZiJianShopMallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    @Override
    public void getNet() {

        //θ?Ώι?η½η»θ·εζ°ζ? δΈι’ηεθ‘¨ζ°ζ?

        Map<String, String> map = new HashMap<>();
        map.put("code", "04136");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("gps_x", PreferenceHelper.getInstance(ZiJianShopMallActivity.this).getString(WEIDU, ""));
        map.put("gps_y", PreferenceHelper.getInstance(ZiJianShopMallActivity.this).getString(JINGDU, ""));

//        NetUtils<TaoKeDetailList.DataBean> netUtils = new NetUtils<>();
//        netUtils.requestData(map, TAOKELIST, getActivity(), new JsonCallback<AppResponse<TaoKeDetailList.DataBean>>() {
//            @Override
//            public void onSuccess(Response<AppResponse<TaoKeDetailList.DataBean>> response) {
//               // Log.i("response_data", new Gson().toJson(response.body()));
//                dataBeanList.addAll(response.body().data);
//                taoKeListAdapter.setNewData(dataBeanList);
//                taoKeListAdapter.notifyDataSetChanged();
//
//
//            }
//        });

        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<ZiJianHomeModel.DataBean>>post(ZIJIANHOME)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZiJianHomeModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ZiJianHomeModel.DataBean>> response) {

                        ZiJianShopMallActivity.this.response = response;
                        objects.addAll(response.body().data.get(0).getWaresList());
                        iconListBeans = new ArrayList<>();
                        iconListBeans.addAll(response.body().data.get(0).getIconList());
                        ziJianAdapter.setNewData(objects);
                        implBanner();
                        //  implBanner();
                        //    ziJianAdapter.setNewData(response.body().data.get(0).getIconList());
                        ziJian_headerAdapter.setNewData(iconListBeans);


                    }
                });
    }


    private Response<AppResponse<ZiJianHomeModel.DataBean>> response;

    @Override
    public void implBanner() {
        List<String> items = new ArrayList<>();
        if (response.body().data != null) {
            for (int i = 0; i < response.body().data.get(0).getBannerList().size(); i++) {
                items.add(response.body().data.get(0).getBannerList().get(i).getImg_url());
            }
        }

        //θ?Ύη½?εΎηε θ½½ε¨
        banner.setImageLoader(new GlideImageLoader());
        if (banner != null) {
            //θ?Ύη½?εΎηιε
            banner.setImages(items);
            //bannerθ?Ύη½?ζΉζ³ε¨ι¨θ°η¨ε?ζ―ζΆζεθ°η¨
            banner.start();
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    startActivity(new Intent(ZiJianShopMallActivity.this, WebViewActivity.class).putExtra("url", response.body().data.get(0).getBannerList().get(position).getHtml_url()));
                }
            });
        }
    }

    @Override
    public void implHortialList() {
        ziJian_headerAdapter = new ZiJian_HeaderAdapter(R.layout.item_zijian_header, iconListBeans);

        //εε§εδΈδΈ
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvcList.setLayoutManager(gridLayoutManager);

        //  taoKeListAdapter = new TaoKeListAdapter(R.layout.layout_taokeshop, dataBeanList);
        ziJian_headerAdapter.openLoadAnimation();//ι»θ?€δΈΊζΈζΎζζ
        rvcList.setAdapter(ziJian_headerAdapter);
    }

    @Override
    public void implBottomList() {

    }

    @Override
    public void implRefresh() {

    }

    @Override
    public void implLoadMore() {

    }

    @Override
    public void onClickHeaderList() {

        ziJian_headerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.constrain:

                        String id = ziJian_headerAdapter.getData().get(position).getId();

                        if (ziJian_headerAdapter.getData().get(position).getId().equals("5")) {
                            FenLeiHomeActivity.actionStart(ZiJianShopMallActivity.this);
                        } else {


                            FenLeiContentModel fenLeiContentModel = new FenLeiContentModel(true, "dfjdslkfj");

                            fenLeiContentModel.setItem_id(ziJian_headerAdapter.getData().get(position).getItem_id());
                            fenLeiContentModel.setItem_name(ziJian_headerAdapter.getData().get(position).getName());
                            fenLeiContentModel.setImg_url(ziJian_headerAdapter.getData().get(position).getImg_url());


                            FenLeiThirdActivity.actionStart(ZiJianShopMallActivity.this, fenLeiContentModel);
                        }

                        break;
                }
            }
        });

    }

    @Override
    public void onClickFooterList() {

        ziJianAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.constrain:
                        ZiJianShopMallDetailsActivity.actionStart(ZiJianShopMallActivity.this, objects.get(position).getShop_product_id(), objects.get(position).getWares_id());
                        break;
                }
            }
        });


    }
}
