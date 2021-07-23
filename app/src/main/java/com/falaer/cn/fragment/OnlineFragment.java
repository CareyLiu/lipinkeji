package com.falaer.cn.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.falaer.cn.activity.device_falaer.FalaerMainActivity;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.falaer.cn.R;
import com.falaer.cn.activity.BindBoxActivity;
import com.falaer.cn.activity.ShangchengActivity;
import com.falaer.cn.activity.shuinuan_wzw.ShuinuanWzwMainActivityNew;
import com.falaer.cn.activity.zckt.AirConditionerActivity;
import com.falaer.cn.adapter.SheBeiListAdapter;
import com.falaer.cn.app.AppManager;
import com.falaer.cn.app.UIHelper;
import com.falaer.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.falaer.cn.basicmvp.BaseFragment;
import com.falaer.cn.callback.JsonCallback;
import com.falaer.cn.config.AppResponse;
import com.falaer.cn.config.MyApplication;
import com.falaer.cn.config.PreferenceHelper;
import com.falaer.cn.config.UserManager;
import com.falaer.cn.dialog.BangdingFailDialog;
import com.falaer.cn.get_net.Urls;
import com.falaer.cn.model.SheBeiLieBieListModel;
import com.falaer.cn.model.SheBeiModel;
import com.falaer.cn.tools.NetworkUtils;
import com.falaer.cn.util.AlertUtil;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.falaer.cn.config.MyApplication.getServer_id;


/**
 * Created by Administrator on 2018/3/29 0029.
 */

public class OnlineFragment extends BaseFragment implements Observer {
    @BindView(R.id.list)
    RecyclerView mList;
    @BindView(R.id.iv_add)
    View ivAdd;
    @BindView(R.id.rl_main)
    LinearLayout rlMain;
    @BindView(R.id.srL_smart)
    SmartRefreshLayout srLSmart;
    @BindView(R.id.iv_shangcheng)
    RelativeLayout ivShangcheng;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.cls_header)
    ClassicsHeader clsHeader;
    private Unbinder unbinder;

    private SheBeiListAdapter sheBeiListAdapter;
    private List<SheBeiModel> mDatas = new ArrayList<>();
    private String mqtt_connect_state;
    private String mqtt_connect_prompt;

    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_online_list;
    }

    @Override
    protected void initView(View view) {
        view.setClickable(true);// 防止点击穿透，底层的fragment响应上层点击触摸事件
        unbinder = ButterKnife.bind(this, view);
        //  sheBeiListAdapter = new SheBeiListAdapter(R.layout.item_shebei, R.layout.item_shebei_header, mDatas);
        //  mList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        clsHeader.setAccentColor(getResources().getColor(R.color.white));
        sheBeiListAdapter = new SheBeiListAdapter(R.layout.item_shebei, R.layout.item_shebei_header, mDatas);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.setAdapter(sheBeiListAdapter);
        sheBeiListAdapter.notifyDataSetChanged();
        sheBeiListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mqtt_connect_state.equals("1")) {
                    if (mDatas.get(position).validity_state.equals("2")) {
                        UIHelper.ToastMessage(getActivity(), "当前设备已过期");
                        return;
                    }
                    switch (view.getId()) {
                        case R.id.constrain:
                            PreferenceHelper.getInstance(getContext()).putString("is_platform_bendi", mDatas.get(position).is_platform);
                            if (mDatas.get(position).device_type.equals("1")) {
                                // mDatas.get(position).ccid = "aaaaaaaaaaaaaa1010010018";
                                int i = mDatas.get(position).ccid.length() - 1;
                                String str = String.valueOf(mDatas.get(position).ccid.charAt(i));

                                PreferenceHelper.getInstance(getActivity()).putString("car_server_id", str + "/");
                                PreferenceHelper.getInstance(getContext()).putString("share_type", mDatas.get(position).share_type);
                                PreferenceHelper.getInstance(getActivity()).putString("ccid", mDatas.get(position).ccid);
                                PreferenceHelper.getInstance(getContext()).putString("sim_ccid_save_type", mDatas.get(position).sim_ccid_save_type);
                                MyApplication.CARBOX_GETNOW = "wit/cbox/app/" + getServer_id() + mDatas.get(position).ccid;
                                MyApplication.CAR_NOTIFY = "wit/server/" + getServer_id() + MyApplication.getUser_id();
                                MyApplication.CAR_CTROL = "wit/cbox/hardware/" + getServer_id() + MyApplication.getCcid();

                                if (NetworkUtils.isConnected(getActivity())) {
                                    Activity currentActivity = AppManager.getAppManager().currentActivity();
                                    if (currentActivity != null) {
                                        FalaerMainActivity.actionStart(getContext());
                                    }
                                } else {
                                    UIHelper.ToastMessage(getActivity(), "请连接网络后重新尝试");
                                }
                            } else if (mDatas.get(position).device_type.equals("6")) {
                                String ccid = mDatas.get(position).ccid;
                                int pos = ccid.length() - 1;
                                String count = ccid.charAt(pos) + "/";
                                PreferenceHelper.getInstance(getContext()).putString("ccid", mDatas.get(position).ccid);
                                PreferenceHelper.getInstance(getContext()).putString("car_server_id", count);
                                PreferenceHelper.getInstance(getContext()).putString("share_type", mDatas.get(position).share_type);
                                PreferenceHelper.getInstance(getContext()).putString("sim_ccid_save_type", mDatas.get(position).sim_ccid_save_type);
                                if (NetworkUtils.isConnected(getActivity())) {
                                    Activity currentActivity = AppManager.getAppManager().currentActivity();
                                    if (currentActivity != null) {
                                        ShuinuanWzwMainActivityNew.actionStart(getActivity(), ccid, count, mDatas.get(position).validity_time);
                                    }
                                } else {
                                    UIHelper.ToastMessage(getActivity(), "请连接网络后重新尝试");
                                }
                            } else if (mDatas.get(position).device_type.equals("5")) {
                                String ccid = mDatas.get(position).ccid;
                                PreferenceHelper.getInstance(getContext()).putString("ccid", mDatas.get(position).ccid);
                                PreferenceHelper.getInstance(getContext()).putString("share_type", mDatas.get(position).share_type);
                                PreferenceHelper.getInstance(getContext()).putString("sim_ccid_save_type", mDatas.get(position).sim_ccid_save_type);
                                if (NetworkUtils.isConnected(getActivity())) {
                                    Activity currentActivity = AppManager.getAppManager().currentActivity();
                                    if (currentActivity != null) {
                                        AirConditionerActivity.actionStart(getActivity(), ccid, "驻车空调");
                                    }
                                } else {
                                    UIHelper.ToastMessage(getActivity(), "请连接网络后重新尝试");
                                }
                            }
                            break;
                    }
                } else {
                    BangdingFailDialog dialog = new BangdingFailDialog(getContext());
                    dialog.setTextContent(mqtt_connect_prompt);
                    dialog.show();
                }
            }
        });

        getSheBeiData();

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindBoxActivity.actionStart(getActivity());
            }
        });

        srLSmart.setEnableRefresh(true);
        srLSmart.setEnableLoadMore(false);
        srLSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getSheBeiData();
            }
        });

        ivShangcheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShangchengActivity.actionStart(getContext());
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivShangcheng.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void immersionInit(ImmersionBar mImmersionBar) {
        mImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.shenlanhei).init();
    }


    @Override
    protected boolean immersionEnabled() {
        return true;
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        getSheBeiData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals("update")) {

        }
    }

    public void getSheBeiData() {
        Map<String, String> map = new HashMap<>();
//        map.put("code", "03510");//我们自己的
        map.put("code", "03517");//老温的
        map.put("key", Urls.key);
        map.put("user_car_type", "1");
        map.put("token", UserManager.getManager(getActivity()).getAppToken());
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
//                            SheBeiModel sheBeiModel = new SheBeiModel(true, response.body().data.get(i).getControl_device_name());
//                            mDatas.add(sheBeiModel);
                            for (int j = 0; j < response.body().data.get(i).getControl_device_list().size(); j++) {
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
                                sheBeiModel1.is_platform = bean.getIs_platform();
                                mDatas.add(sheBeiModel1);
                            }
                        }

                        if (mDatas.size() == 0) {
                            View view = View.inflate(getActivity(), R.layout.online_empty_view, null);
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
                        AlertUtil.t(getActivity(), response.getException().getMessage());
                    }
                });
    }
}
