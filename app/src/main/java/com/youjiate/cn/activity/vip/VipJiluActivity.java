package com.youjiate.cn.activity.vip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.youjiate.cn.R;
import com.youjiate.cn.activity.vip.adapter.VipJiluAdapter;
import com.youjiate.cn.app.BaseActivity;
import com.youjiate.cn.callback.JsonCallback;
import com.youjiate.cn.config.AppResponse;
import com.youjiate.cn.config.UserManager;
import com.youjiate.cn.get_net.Urls;
import com.youjiate.cn.model.SheBeiLieBieListModel;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VipJiluActivity extends BaseActivity {
    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private VipJiluAdapter adapter;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_vip_jilu;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, VipJiluActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("缴费记录");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initAdaper();
        getNet();
    }

    private void initAdaper() {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add("");
        }
        adapter = new VipJiluAdapter(R.layout.item_vip_jilu, strings);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        rv_list.setAdapter(adapter);
    }

    public void getNet() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03515");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        Gson gson = new Gson();
        OkGo.<AppResponse<SheBeiLieBieListModel.DataBean>>post(Urls.SERVER_URL + "wit/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<SheBeiLieBieListModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<SheBeiLieBieListModel.DataBean>> response) {

                    }

                    @Override
                    public void onError(Response<AppResponse<SheBeiLieBieListModel.DataBean>> response) {
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }
}
