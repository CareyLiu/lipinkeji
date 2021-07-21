package com.falaer.cn.activity.device_jg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gyf.barlibrary.ImmersionBar;
import com.falaer.cn.R;
import com.falaer.cn.activity.device_jg.adapter.WeixiujiluAdapter;
import com.falaer.cn.app.BaseActivity;
import com.falaer.cn.config.PreferenceHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class JinggongWeixiuActivity extends BaseActivity {

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private List<String> models = new ArrayList<>();
    private WeixiujiluAdapter adapter;

    private String ccid;

    @Override
    public int getContentViewResId() {
        return R.layout.a_jinggong_act_weixiujilu;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
        mImmersionBar.statusBarDarkFont(true);
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, JinggongWeixiuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        ccid = PreferenceHelper.getInstance(this).getString("ccid", "");
        initAdapter();
        getData();
    }

    private void initAdapter() {
        for (int i = 0; i < 1; i++) {
            models.add("");
        }

        adapter = new WeixiujiluAdapter(R.layout.a_jinggong_item_weixiujilu, models);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        rv_list.setAdapter(adapter);
    }

    private void getData() {

    }
}
