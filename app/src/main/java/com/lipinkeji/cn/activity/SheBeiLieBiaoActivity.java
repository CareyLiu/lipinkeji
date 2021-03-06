package com.lipinkeji.cn.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.activity.device_a.BindBoxActivity;
import com.lipinkeji.cn.activity.device_fengnuan.LipinFengnuanActivity;
import com.lipinkeji.cn.activity.device_fengnuan.LipinFengnuanActivityNew;
import com.lipinkeji.cn.activity.device_shuinuan.LipinDashuiActivity;
import com.lipinkeji.cn.activity.device_shuinuan.LipinDashuiActivityNew;
import com.lipinkeji.cn.activity.device_shuinuan.set.ShuiNuanKaiJiMoShiSheZhiActivity;
import com.lipinkeji.cn.util.Y;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_shuinuan.LipinXiaoshuiActivity;
import com.lipinkeji.cn.activity.zckt.AirConditionerActivity;
import com.lipinkeji.cn.adapter.SheBeiListAdapter;
import com.lipinkeji.cn.app.AppManager;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.UIHelper;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.MyApplication;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.dialog.BangdingFailDialog;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.SheBeiLieBieListModel;
import com.lipinkeji.cn.model.SheBeiModel;
import com.lipinkeji.cn.tools.NetworkUtils;
import com.lipinkeji.cn.util.AlertUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.lipinkeji.cn.config.MyApplication.getServer_id;

public class SheBeiLieBiaoActivity extends BaseActivity {
    @BindView(R.id.rlv_list)
    RecyclerView rlvList;
    @BindView(R.id.srL_smart)
    SmartRefreshLayout srLSmart;
    private SheBeiListAdapter sheBeiListAdapter;
    private List<SheBeiModel> mDatas = new ArrayList<>();
    private String device_type;
    private String mqtt_connect_state;
    private String mqtt_connect_prompt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device_type = getIntent().getStringExtra("device_type");
        sheBeiListAdapter = new SheBeiListAdapter(R.layout.item_shebei_new, R.layout.item_shebei_header, mDatas);
        rlvList.setLayoutManager(new LinearLayoutManager(mContext));
        rlvList.setAdapter(sheBeiListAdapter);
        srLSmart.setEnableLoadMore(false);
        sheBeiListAdapter.notifyDataSetChanged();
        sheBeiListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mqtt_connect_state.equals("1")) {
                    if (mDatas.get(position).validity_state.equals("2")) {
                        UIHelper.ToastMessage(mContext, "?????????????????????");
                        return;
                    }
                    switch (view.getId()) {
                        case R.id.constrain:
                            PreferenceHelper.getInstance(mContext).putString("is_platform_bendi", mDatas.get(position).is_platform);
                            if (mDatas.get(position).device_type.equals("1")) {
                                int i = mDatas.get(position).ccid.length() - 1;
                               // mDatas.get(position).ccid="aaaaaaaaaaaaaaaa90170018";
                                String str = String.valueOf(mDatas.get(position).ccid.charAt(i));
                                PreferenceHelper.getInstance(mContext).putString("car_server_id", str + "/");
                                PreferenceHelper.getInstance(mContext).putString("share_type", mDatas.get(position).share_type);
                                PreferenceHelper.getInstance(mContext).putString("ccid", mDatas.get(position).ccid);
                                PreferenceHelper.getInstance(mContext).putString("sim_ccid_save_type", mDatas.get(position).sim_ccid_save_type);
                                MyApplication.CARBOX_GETNOW = "wit/cbox/app/" + getServer_id() + MyApplication.getCcid();
                                MyApplication.CAR_NOTIFY = "wit/server/" + getServer_id() + MyApplication.getUser_id();
                                MyApplication.CAR_CTROL = "wit/cbox/hardware/" + getServer_id() + MyApplication.getCcid();

                                PreferenceHelper.getInstance(mContext).putString("validdate", mDatas.get(position).validdate);
                                PreferenceHelper.getInstance(mContext).putString("validdate_state", mDatas.get(position).validdate_state);
                                PreferenceHelper.getInstance(mContext).putString("sim_ccid", mDatas.get(position).sim_ccid);

                                PreferenceHelper.getInstance(mContext).putString("user_car_id", mDatas.get(position).user_car_id);


                                if (NetworkUtils.isConnected(mContext)) {
                                    Activity currentActivity = AppManager.getAppManager().currentActivity();
                                    if (currentActivity != null) {
                                        LipinFengnuanActivityNew.actionStart(mContext);
                                    }
                                } else {
                                    UIHelper.ToastMessage(mContext, "??????????????????????????????");
                                }
                            } else if (mDatas.get(position).device_type.equals("6")) {
                                String ccid = mDatas.get(position).ccid;
                                int pos = ccid.length() - 1;
                                String count = ccid.charAt(pos) + "/";
                                PreferenceHelper.getInstance(mContext).putString("ccid", mDatas.get(position).ccid);
                                PreferenceHelper.getInstance(mContext).putString("car_server_id", count);
                                PreferenceHelper.getInstance(mContext).putString("share_type", mDatas.get(position).share_type);
                                PreferenceHelper.getInstance(mContext).putString("sim_ccid_save_type", mDatas.get(position).sim_ccid_save_type);
                                PreferenceHelper.getInstance(mContext).putString("validdate", mDatas.get(position).validity_time);
                                PreferenceHelper.getInstance(mContext).putString("validdate_state", mDatas.get(position).validdate_state);
                                PreferenceHelper.getInstance(mContext).putString("sim_ccid", mDatas.get(position).sim_ccid);
                                PreferenceHelper.getInstance(mContext).putString("user_car_id", mDatas.get(position).user_car_id);
                                if (NetworkUtils.isConnected(mContext)) {
                                    Activity currentActivity = AppManager.getAppManager().currentActivity();
                                    if (currentActivity != null) {
                                        LipinDashuiActivityNew.actionStart(mContext, ccid, count);
                                    }
                                } else {
                                    UIHelper.ToastMessage(mContext, "??????????????????????????????");
                                }
                            } else if (mDatas.get(position).device_type.equals("5")) {
                                String ccid = mDatas.get(position).ccid;
                                PreferenceHelper.getInstance(mContext).putString("ccid", mDatas.get(position).ccid);
                                PreferenceHelper.getInstance(mContext).putString("share_type", mDatas.get(position).share_type);
                                PreferenceHelper.getInstance(mContext).putString("sim_ccid_save_type", mDatas.get(position).sim_ccid_save_type);
                                if (NetworkUtils.isConnected(mContext)) {
                                    Activity currentActivity = AppManager.getAppManager().currentActivity();
                                    if (currentActivity != null) {
                                        AirConditionerActivity.actionStart(mContext, ccid, "????????????");
                                    }
                                } else {
                                    UIHelper.ToastMessage(mContext, "??????????????????????????????");
                                }
                            }


                            break;
                    }
                } else {
                    BangdingFailDialog dialog = new BangdingFailDialog(mContext);
                    dialog.setTextContent(mqtt_connect_prompt);
                    dialog.show();
                }
            }
        });
        getSheBeiData(device_type);
    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_shebei_liebiao;
    }

    public void getSheBeiData(String str) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03533");
        map.put("key", Urls.key);
        map.put("user_car_type", "1");
        map.put("device_type", str);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        Gson gson = new Gson();
        OkGo.<AppResponse<SheBeiLieBieListModel.DataBean>>post(Urls.SERVER_URL + "wit/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<SheBeiLieBieListModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<SheBeiLieBieListModel.DataBean>> response) {
                        mqtt_connect_state = response.body().mqtt_connect_state;
                        mqtt_connect_prompt = response.body().mqtt_connect_prompt;
                        mDatas.clear();
                        for (int i = 0; i < response.body().data.size(); i++) {
                            SheBeiModel sheBeiModel = new SheBeiModel(true, response.body().data.get(i).getControl_device_name());
                            mDatas.add(sheBeiModel);
                            for (int j = 0; j < response.body().data.get(i).getControl_device_list().size(); j++) {
                                // Log.i("name--shebei", response.body().data.get(i).getControl_device_list().get(j).getDevice_name());
                                SheBeiModel sheBeiModel1 = new SheBeiModel(false, response.body().data.get(i).getControl_device_name());
                                SheBeiLieBieListModel.DataBean.ControlDeviceListBean bean = response.body().data.get(i).getControl_device_list().get(j);
                                sheBeiModel1.ccid = bean.getCcid();
                                sheBeiModel1.device_img_url = bean.getDevice_img_url();
                                sheBeiModel1.device_name = bean.getDevice_name();
                                sheBeiModel1.validity_state = bean.getValidity_state();
                                sheBeiModel1.validity_term = bean.getValidity_term();
                                sheBeiModel1.validity_time = bean.getValidity_time();
                                sheBeiModel1.device_type = response.body().data.get(i).getControl_type_id();
                                sheBeiModel1.sim_ccid_save_type = bean.sim_ccid_save_type;
                                sheBeiModel1.share_type = bean.getShare_type();

                                sheBeiModel1.sim_ccid = bean.getSim_ccid();
                                sheBeiModel1.validdate = bean.getValiddate();
                                sheBeiModel1.validdate_state = bean.getValiddate_state();

                                sheBeiModel1.user_car_id = bean.getUser_car_id();

                                mDatas.add(sheBeiModel1);
                            }
                        }

                        if (mDatas.size() == 0) {
                            View view = View.inflate(mContext, R.layout.online_empty_view, null);
                            sheBeiListAdapter.setHeaderView(view);
                        } else {
                            sheBeiListAdapter.removeAllHeaderView();
                        }
                        sheBeiListAdapter.setNewData(mDatas);
                        sheBeiListAdapter.notifyDataSetChanged();
                        srLSmart.finishRefresh();
                    }

                    @Override
                    public void onError(Response<AppResponse<SheBeiLieBieListModel.DataBean>> response) {
                        AlertUtil.t(mContext, response.getException().getMessage());
                    }
                });
    }

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String device_type) {
        Intent intent = new Intent(context, SheBeiLieBiaoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("device_type", device_type);
        context.startActivity(intent);
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("????????????");
        tv_title.setTextColor(getResources().getColor(R.color.white));
        mToolbar.setBackgroundColor(Y.getColor(R.color.bg_app_lipin));
        mToolbar.setNavigationIcon(R.mipmap.back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_rightTitle.setVisibility(View.VISIBLE);
        iv_rightTitle.setImageResource(R.mipmap.tinjia);
        iv_rightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindBoxActivity.actionStart(mContext);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSheBeiData(device_type);
    }


    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.bg_app_lipin).init();
    }

}
