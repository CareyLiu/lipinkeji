package com.lipinkeji.cn.activity.homepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.adapter.DaLiBaoAdapter;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.common.StringUtils;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.DaLiBaoListModel;
import com.lipinkeji.cn.util.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.lipinkeji.cn.get_net.Urls.LIBAOLIST;

public class DaLiBaoActivity extends BaseActivity {
    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    DaLiBaoAdapter daLiBaoAdapter;
    List<DaLiBaoListModel.DataBean> dataBeanList;
    List<DaLiBaoListModel.DataBean.TicketListBean> ticketListBeans;
    @BindView(R.id.tv_liji_buy)
    TextView tvLijiBuy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //  linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvList.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        // rvList.setLayoutManager(linearLayoutManager);
        daLiBaoAdapter = new DaLiBaoAdapter(R.layout.item_dalibao, ticketListBeans);
        daLiBaoAdapter.openLoadAnimation();//?????????????????????
        rvList.setAdapter(daLiBaoAdapter);

        Map<String, String> map = new HashMap<>();
        map.put("code", "08026");
        map.put("key", Urls.key);
        NetUtils<DaLiBaoListModel.DataBean> netUtils = new NetUtils<>();
        netUtils.requestData(map, LIBAOLIST, this, new JsonCallback<AppResponse<DaLiBaoListModel.DataBean>>() {
            @Override
            public void onSuccess(Response<AppResponse<DaLiBaoListModel.DataBean>> response) {
                Log.i("response_it", new Gson().toJson(response.body()));
                dataBeanList = new ArrayList<>();
                dataBeanList.addAll(response.body().data);
                daLiBaoAdapter.setNewData(dataBeanList.get(0).getTicket_list());
                daLiBaoAdapter.notifyDataSetChanged();

            }
        });
        tvLijiBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DaLiBaoZhiFuActivity.actionStart(DaLiBaoActivity.this);
                LiBaoPageActivity.actionStart(DaLiBaoActivity.this);
            }
        });
        daLiBaoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.constrain:
                        String str = daLiBaoAdapter.getData().get(position).getCoupon_two_img_url();
                        String name = daLiBaoAdapter.getData().get(position).getCoupon_two_name();
                        if (!StringUtils.isEmpty(str)) {
                            DaLiBaoErJiActivity.actionStart(DaLiBaoActivity.this, str, name);
                        }
                        break;
                }
            }
        });

    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_dalibao;
    }

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DaLiBaoActivity.class);
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
        tv_title.setText("????????????19998????????????");
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
}
