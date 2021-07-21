package com.falaer.cn.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.falaer.cn.R;
import com.falaer.cn.adapter.ExpandableRecyclerAdapter;
import com.falaer.cn.adapter.ShopCartExpandAdapter;
import com.falaer.cn.callback.JsonCallback;
import com.falaer.cn.config.AppResponse;
import com.falaer.cn.config.UserManager;
import com.falaer.cn.get_net.Urls;
import com.falaer.cn.model.GoodsItem;
import com.falaer.cn.model.ShopCartModel;
import com.falaer.cn.util.AlertUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopCartActivity extends BaseActivity {
    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.list)
    LRecyclerView list;

    ShopCartExpandAdapter shopCartExpandAdapter;
    LRecyclerViewAdapter lRecyclerViewAdapter;
    LinearLayoutManager manager;
    List<ShopCartModel.DataBean> dataBeans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);
        ButterKnife.bind(this);

        shopCartExpandAdapter = new ShopCartExpandAdapter(this,list);
        shopCartExpandAdapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(shopCartExpandAdapter);
        list.setAdapter(lRecyclerViewAdapter);
        manager = new LinearLayoutManager(this,LinearLayout.VERTICAL,false);
        list.setLayoutManager(manager);
        requestData();
        list.setPullRefreshEnabled(false);
        //全选
        shopCartExpandAdapter.setOnItemClickListener(new ShopCartExpandAdapter.OnViewItemClickListener() {
            @Override
            public void onItemClick(boolean isFlang, View view, int position) {
                List<GoodsItem> items = shopCartExpandAdapter.getList(dataBeans);
                items.get(position).isCheck = isFlang;
                shopCartExpandAdapter.setItems(items);

            }
        });

    }

    public void requestData(){
        Map<String, String> map = new HashMap<>();
        map.put("code", "04152");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(ShopCartActivity.this).getAppToken());
        map.put("page_number","0");
        Gson gson = new Gson();
        OkGo.<AppResponse<ShopCartModel.DataBean>>post(Urls.SERVER_URL + "shop_new/app/user ")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ShopCartModel.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<ShopCartModel.DataBean>> response) {
                        dataBeans = response.body().data;
                        shopCartExpandAdapter.setItems(shopCartExpandAdapter.getList(dataBeans));
                        shopCartExpandAdapter.notifyDataSetChanged();
                        list.refreshComplete(10);
                        shopCartExpandAdapter.expandAll();
                    }


                    @Override
                    public void onError(Response<AppResponse<ShopCartModel.DataBean>> response) {
                        AlertUtil.t(ShopCartActivity.this,response.getException().getMessage());
                    }

                });

    }

    @OnClick(R.id.layout_back)
    public void onViewClicked() {
        finish();
    }
}
