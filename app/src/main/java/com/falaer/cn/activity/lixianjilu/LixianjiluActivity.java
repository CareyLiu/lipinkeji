package com.falaer.cn.activity.lixianjilu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.falaer.cn.R;
import com.falaer.cn.app.BaseActivity;
import com.falaer.cn.callback.JsonCallback;
import com.falaer.cn.config.AppResponse;
import com.falaer.cn.config.PreferenceHelper;
import com.falaer.cn.config.UserManager;
import com.falaer.cn.get_net.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class LixianjiluActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private List<LixianModel.DataBean> lixianModel = new ArrayList<>();
    private int page_number;
    private LixianAdapter adapter;


    @Override
    public int getContentViewResId() {
        return R.layout.lixianjilu_act;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LixianjiluActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
        initSM();
        getSheBeiData();
    }

    private void initSM() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getSheBeiData();
            }


        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                lordShebeiData();
            }
        });
    }

    private void lordShebeiData() {
        page_number++;
        Map<String, String> map = new HashMap<>();
        map.put("code", "03518");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("ccid", PreferenceHelper.getInstance(mContext).getString("ccid", ""));
        map.put("page_number", page_number + "");
        Gson gson = new Gson();
        OkGo.<AppResponse<LixianModel.DataBean>>post(Urls.SERVER_URL + "wit/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<LixianModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<LixianModel.DataBean>> response) {
                        List<LixianModel.DataBean> data = response.body().data;
                        lixianModel.addAll(data);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<AppResponse<LixianModel.DataBean>> response) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.finishLoadMore();
                    }
                });
    }

    private void initAdapter() {
        adapter = new LixianAdapter(R.layout.lixianjilu_item, lixianModel);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        rvList.setAdapter(adapter);
    }

    public void getSheBeiData() {
        page_number = 0;
        Map<String, String> map = new HashMap<>();
        map.put("code", "03518");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("ccid", PreferenceHelper.getInstance(mContext).getString("ccid", ""));
        map.put("page_number", page_number + "");
        Gson gson = new Gson();
        OkGo.<AppResponse<LixianModel.DataBean>>post(Urls.SERVER_URL + "wit/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<LixianModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<LixianModel.DataBean>> response) {
                        lixianModel = response.body().data;
                        adapter.setNewData(lixianModel);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<AppResponse<LixianModel.DataBean>> response) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.finishLoadMore();
                    }
                });
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }
}
