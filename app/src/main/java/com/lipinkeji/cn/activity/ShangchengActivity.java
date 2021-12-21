package com.lipinkeji.cn.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.zijian_shangcheng.ZiJianShopMallDetailsActivity;
import com.lipinkeji.cn.adapter.ShangchengAdapter;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.Radius_GlideImageLoader;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.ShangchengModel;
import com.lipinkeji.cn.util.GridAverageUIDecoration;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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

import static com.lipinkeji.cn.get_net.Urls.HOME_PICTURE;

public class ShangchengActivity extends BaseActivity {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv_shangpin)
    RecyclerView rvShangpin;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;

    private int page_number;
    private List<ShangchengModel.DataBean.BannerListBean> bannerList = new ArrayList<>();
    private List<ShangchengModel.DataBean.IndexShowListBean> indexShowList = new ArrayList<>();
    private ShangchengAdapter shangchengAdapter;

    @Override
    public int getContentViewResId() {
        return R.layout.act_shangcheng;
    }

    @Override
    public void initImmersion() {
        mImmersionBar.with(this).statusBarDarkFont(true).fitsSystemWindows(true).statusBarColor(R.color.white).init();
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShangchengActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initAdapter();
        initSM();
        getData();
    }

    private void initAdapter() {
        shangchengAdapter = new ShangchengAdapter(R.layout.item_home_remen, indexShowList);
        rvShangpin.addItemDecoration(new GridAverageUIDecoration(9, 10));
        rvShangpin.setLayoutManager(new GridLayoutManager(mContext, 2));
        rvShangpin.setAdapter(shangchengAdapter);
        shangchengAdapter.setNewData(indexShowList);
        shangchengAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ZiJianShopMallDetailsActivity.actionStart(mContext, indexShowList.get(position).getShop_product_id(), indexShowList.get(position).getWares_id());
            }
        });
    }

    private void initSM() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }


        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                lordData();
            }
        });
    }

    private void getData() {
        indexShowList.clear();
        page_number = 0;
        Map<String, String> map = new HashMap<>();
        map.put("code", "04433");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("page_number", page_number + "");
        Gson gson = new Gson();
        OkGo.<AppResponse<ShangchengModel.DataBean>>post(HOME_PICTURE)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ShangchengModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ShangchengModel.DataBean>> response) {
                        ShangchengModel.DataBean dataBean = response.body().data.get(0);
                        bannerList = dataBean.getBannerList();
                        List<ShangchengModel.DataBean.IndexShowListBean> shangpinModel = dataBean.getIndexShowList();
                        indexShowList.addAll(shangpinModel);
                        shangchengAdapter.notifyDataSetChanged();

                      setBanner();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        smartRefreshLayout.finishRefresh();
                    }
                });
    }

    private void setBanner() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < bannerList.size(); i++) {
            items.add(bannerList.get(i).getImg_url());
        }
        banner.setImageLoader(new Radius_GlideImageLoader());
        //设置图片集合
        banner.setImages(items);
        //banner设置方法全部调用完毕时最后调用
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String html_url = bannerList.get(position).getHtml_url();
                DefaultX5WebViewActivity.actionStart(mContext, html_url);
            }
        });
        banner.start();
    }

    private void lordData() {
        page_number++;
        Map<String, String> map = new HashMap<>();
        map.put("code", "04431");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("page_number", page_number + "");
        Gson gson = new Gson();
        OkGo.<AppResponse<ShangchengModel.DataBean>>post(HOME_PICTURE)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ShangchengModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ShangchengModel.DataBean>> response) {
                        ShangchengModel.DataBean dataBean = response.body().data.get(0);
                        List<ShangchengModel.DataBean.IndexShowListBean> shangpinModels = dataBean.getIndexShowList();
                        indexShowList.addAll(shangpinModels);
                        shangchengAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        smartRefreshLayout.finishLoadMore();
                    }
                });
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }
}
