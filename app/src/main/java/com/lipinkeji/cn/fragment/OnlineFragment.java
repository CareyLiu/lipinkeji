package com.lipinkeji.cn.fragment;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_a.BindBoxActivity;
import com.lipinkeji.cn.activity.device_fengnuan.LipinFengnuanActivity;
import com.lipinkeji.cn.activity.device_fengnuan.LipinFengnuanActivityNew;
import com.lipinkeji.cn.activity.device_shuinuan.LipinDashuiActivityNew;
import com.lipinkeji.cn.activity.zckt.AirConditionerActivity;
import com.lipinkeji.cn.adapter.SheBeiListAdapter;
import com.lipinkeji.cn.app.AppManager;
import com.lipinkeji.cn.app.UIHelper;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.lipinkeji.cn.basicmvp.BaseFragment;
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
import com.lipinkeji.cn.util.DensityUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.lipinkeji.cn.config.MyApplication.getServer_id;


/**
 * Created by Administrator on 2018/3/29 0029.
 */

public class OnlineFragment extends BaseFragment implements Observer {
    @BindView(R.id.list)
    RecyclerView mList;
    @BindView(R.id.iv_add)
    View ivAdd;
    @BindView(R.id.srL_smart)
    SmartRefreshLayout srLSmart;
    @BindView(R.id.iv_xiaochengxu)
    ImageView ivXiaoChengXu;
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
        sheBeiListAdapter = new SheBeiListAdapter(R.layout.item_shebei_new, R.layout.item_shebei_header, mDatas);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
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
                            if (mDatas.get(position).device_type.equals("1")) {//1
                                int i = mDatas.get(position).ccid.length() - 1;
//                                mDatas.get(position).ccid = "aaaaaaaaaaaaa88880070018";
                                mDatas.get(position).ccid = "aaaaaaaaaaaaa88880020018";
                                String str = String.valueOf(mDatas.get(position).ccid.charAt(i));
                                PreferenceHelper.getInstance(getActivity()).putString("car_server_id", str + "/");
                                PreferenceHelper.getInstance(getContext()).putString("share_type", mDatas.get(position).share_type);
                                PreferenceHelper.getInstance(getActivity()).putString("ccid", mDatas.get(position).ccid);
                                PreferenceHelper.getInstance(getContext()).putString("sim_ccid_save_type", mDatas.get(position).sim_ccid_save_type);
                                MyApplication.CARBOX_GETNOW = "wit/cbox/app/" + getServer_id() + MyApplication.getCcid();
                                MyApplication.CAR_NOTIFY = "wit/server/" + getServer_id() + MyApplication.getUser_id();
                                MyApplication.CAR_CTROL = "wit/cbox/hardware/" + getServer_id() + MyApplication.getCcid();

                                PreferenceHelper.getInstance(getContext()).putString("validdate", mDatas.get(position).validdate);
                                PreferenceHelper.getInstance(getContext()).putString("validdate_state", mDatas.get(position).validdate_state);
                                PreferenceHelper.getInstance(getContext()).putString("sim_ccid", mDatas.get(position).sim_ccid);
                                PreferenceHelper.getInstance(getContext()).putString("user_car_id", mDatas.get(position).user_car_id);

                                if (NetworkUtils.isConnected(getActivity())) {
                                    Activity currentActivity = AppManager.getAppManager().currentActivity();
                                    if (currentActivity != null) {
//                                        LipinFengnuanActivity.actionStart(getContext());
                                        LipinFengnuanActivityNew.actionStart(getContext());
                                    }
                                } else {
                                    UIHelper.ToastMessage(getActivity(), "请连接网络后重新尝试");
                                }
                            } else if (mDatas.get(position).device_type.equals("6")) {//6
//                                mDatas.get(position).ccid = "aaaaaaaaaaaaa11910040018";
                                String ccid = mDatas.get(position).ccid;
                                int pos = ccid.length() - 1;
                                String count = ccid.charAt(pos) + "/";
                                PreferenceHelper.getInstance(getContext()).putString("ccid", mDatas.get(position).ccid);
                                PreferenceHelper.getInstance(getContext()).putString("car_server_id", count);
                                PreferenceHelper.getInstance(getContext()).putString("share_type", mDatas.get(position).share_type);
                                PreferenceHelper.getInstance(getContext()).putString("sim_ccid_save_type", mDatas.get(position).sim_ccid_save_type);
                                PreferenceHelper.getInstance(getContext()).putString("validdate", mDatas.get(position).validity_time);
                                PreferenceHelper.getInstance(getContext()).putString("validdate_state", mDatas.get(position).validdate_state);
                                PreferenceHelper.getInstance(getContext()).putString("sim_ccid", mDatas.get(position).sim_ccid);
                                if (NetworkUtils.isConnected(getActivity())) {
                                    Activity currentActivity = AppManager.getAppManager().currentActivity();
                                    if (currentActivity != null) {
                                        LipinDashuiActivityNew.actionStart(getActivity(), ccid, count);
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

        initAddView();
    }

    private String pingMuHeight;
    private int lastX;
    private int lastY;

    private boolean isIntercept = false;
    /**
     * 按下时的位置控件相对屏幕左上角的位置X
     */
    private int startDownX;
    /**
     * 按下时的位置控件距离屏幕左上角的位置Y
     */
    private int startDownY;
    /**
     * 控件相对屏幕左上角移动的位置X
     */
    private int lastMoveX;
    /**
     * 控件相对屏幕左上角移动的位置Y
     */
    private int lastMoveY;


    private void initAddView() {
        pingMuHeight = DensityUtils.getScreenHeight(getActivity()) + "";

        ivXiaoChengXu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindBoxActivity.actionStart(getActivity());
            }
        });

        ivXiaoChengXu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Log.i("ImageButton-00", "x" + String.valueOf(x));
                Log.i("ImageButton-00", "y" + String.valueOf(y));
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = x;
                        lastY = y;

                        startDownX = lastMoveX = (int) event.getRawX();
                        startDownY = lastMoveY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int offsetX = x - lastX;
                        int offsetY = y - lastY;
                        Log.i("view_00", "差值" + (Float.valueOf(pingMuHeight) - v.getTop()) + "");
                        Log.i("view_00", "底部栏和控件高度" + DensityUtils.dp2px(getActivity(), 110) + "");

                        v.layout(v.getLeft() + offsetX,
                                v.getTop() + offsetY,
                                v.getRight() + offsetX,
                                v.getBottom() + offsetY);
                        break;
                    case MotionEvent.ACTION_UP:
                        // adsorbAnim(event.getRawX(), event.getRawY());
                        if (v.getTop() < 0) {
                            RelativeLayout.LayoutParams lpFeedback = new RelativeLayout.LayoutParams(
                                    DensityUtils.dp2px(getActivity(), 100), DensityUtils.dp2px(getActivity(), 100));
                            lpFeedback.leftMargin = v.getLeft();
                            lpFeedback.topMargin = v.getTop();
                            lpFeedback.setMargins(v.getLeft(), 0, 0, 0);
                            v.setLayoutParams(lpFeedback);

                        } else if ((Float.valueOf(pingMuHeight) - v.getTop()) < DensityUtils.dp2px(getActivity(), 110)) {
                            RelativeLayout.LayoutParams lpFeedback = new RelativeLayout.LayoutParams(
                                    DensityUtils.dp2px(getActivity(), 100), DensityUtils.dp2px(getActivity(), 100));
                            lpFeedback.leftMargin = v.getLeft();
                            lpFeedback.topMargin = v.getTop();
                            lpFeedback.setMargins(v.getLeft(), Integer.valueOf(pingMuHeight) - DensityUtils.dp2px(getActivity(), 140), 0, 0);
                            v.setLayoutParams(lpFeedback);

                        } else {
                            RelativeLayout.LayoutParams lpFeedback = new RelativeLayout.LayoutParams(
                                    DensityUtils.dp2px(getActivity(), 100), DensityUtils.dp2px(getActivity(), 100));
                            lpFeedback.leftMargin = v.getLeft();
                            lpFeedback.topMargin = v.getTop();
                            lpFeedback.setMargins(v.getLeft(), v.getTop(), 0, 0);
                            v.setLayoutParams(lpFeedback);
                            int lastMoveDx = Math.abs((int) event.getRawX() - startDownX);
                            int lastMoveDy = Math.abs((int) event.getRawY() - startDownY);
                            if (0 != lastMoveDx || 0 != lastMoveDy) {
                                isIntercept = true;
                            } else {
                                isIntercept = false;
                            }
                        }

                        adsorbAnim(v, event.getRawX(), event.getRawY());

                        break;
                }
                return isIntercept;
            }
        });
    }

    private void adsorbAnim(View view, float rawX, float rawY) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;

        view.animate().setDuration(400)
                .setInterpolator(new OvershootInterpolator())
                .xBy(screenWidth - view.getX() - view.getWidth())
                .start();

    }

    @Override
    protected void immersionInit(ImmersionBar mImmersionBar) {
        mImmersionBar.with(this).init();
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
        map.put("code", "03533");
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

                                sheBeiModel1.sim_ccid = bean.getSim_ccid();
                                sheBeiModel1.validdate = bean.getValiddate();
                                sheBeiModel1.validdate_state = bean.getValiddate_state();

                                sheBeiModel1.user_car_id = bean.getUser_car_id();
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
