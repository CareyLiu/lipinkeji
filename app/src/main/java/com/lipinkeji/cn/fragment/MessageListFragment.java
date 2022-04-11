package com.lipinkeji.cn.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.dingdan.XiaoXiEnterDingDanActivity;
import com.lipinkeji.cn.adapter.MessageListAdapter;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.lipinkeji.cn.basicmvp.BaseFragment;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.common.StringUtils;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.dialog.MyCarCaoZuoDialog_Delete;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.MessageModel;
import com.lipinkeji.cn.util.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

import static com.lipinkeji.cn.config.MyApplication.getApp;
import static com.lipinkeji.cn.get_net.Urls.MESSAGE_URL;


public class MessageListFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private List<MessageModel.DataBean> mDatas = new ArrayList<>();
    private MessageListAdapter messageListAdapter;

    private SmartRefreshLayout srLSmart;
    private String notifyId = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getNet() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03003");
        map.put("key", Urls.key);
        map.put("token", PreferenceHelper.getInstance(getActivity()).getString("app_token", "0"));
        map.put("notify_id", notifyId);
        if (strTitle.equals("全部")) {

        } else if (strTitle.equals("未读")) {
            map.put("notify_read", "1");
        } else if (strTitle.equals("已读")) {
            map.put("notify_read", "2");

        } else if (strTitle.equals("已处理")) {
            map.put("notify_state", "2");
        } else if (strTitle.equals("未处理")) {
            map.put("notify_state", "2");
        }
        NetUtils<MessageModel.DataBean> netUtils = new NetUtils<>();
        netUtils.requestData(map, Urls.MESSAGE_URL, getActivity(), new JsonCallback<AppResponse<MessageModel.DataBean>>() {
            @Override
            public void onSuccess(Response<AppResponse<MessageModel.DataBean>> response) {
                if (response.body().data.size() > 0) {
                    if (StringUtils.isEmpty(notifyId)) {
                        mDatas.clear();
                        mDatas.addAll(response.body().data);
                        messageListAdapter.setNewData(mDatas);

                    } else {
                        srLSmart.setEnableLoadMore(true);
                        mDatas.addAll(response.body().data);
                    }

                    notifyId = mDatas.get(mDatas.size() - 1).getNotify_id();
                    messageListAdapter.notifyDataSetChanged();

                    recyclerView.setVisibility(View.VISIBLE);
                    ivNone.setVisibility(View.GONE);
                    srLSmart.setEnableLoadMore(true);
                } else {
                    srLSmart.setEnableLoadMore(false);
                }

                if (mDatas.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    ivNone.setVisibility(View.VISIBLE);
                }
                srLSmart.finishLoadMore();
                srLSmart.finishRefresh();
            }

            @Override
            public void onError(Response<AppResponse<MessageModel.DataBean>> response) {
                super.onError(response);

            }

            @Override
            public void onStart(Request<AppResponse<MessageModel.DataBean>, ? extends Request> request) {
                super.onStart(request);
            }
        });
    }

    String strTitle;
    LinearLayout ivNone;

    @Override
    protected void initLogic() {
        Bundle args = getArguments();
        strTitle = args.getString("title");
        getNet();
    }

    RelativeLayout rlMain;
    View view;



    @Override
    protected int getLayoutRes() {
        return R.layout.messagelist;
    }

    @Override
    protected void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        rlMain = rootView.findViewById(R.id.rl_main);
        ivNone = rootView.findViewById(R.id.iv_none);
        view = rootView.findViewById(R.id.view);
        srLSmart = rootView.findViewById(R.id.srL_smart);
        initAdapter();
        srLSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getNet();
            }
        });
        srLSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                notifyId = "";
                getNet();
            }
        });
        messageListAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                MyCarCaoZuoDialog_Delete myCarCaoZuoDialog_delete = new MyCarCaoZuoDialog_Delete(getActivity(), new MyCarCaoZuoDialog_Delete.OnDialogItemClickListener() {
                    @Override
                    public void clickLeft() {

                    }

                    @Override
                    public void clickRight() {

                        MessageModel.DataBean dataBean = (MessageModel.DataBean) adapter.getData().get(position);
                        String notifyId = dataBean.getNotify_id();
                        String of_user_id = PreferenceHelper.getInstance(getActivity()).getString("of_user_id", "");
                        deleteMessageNet(notifyId, of_user_id);
                    }
                });
                myCarCaoZuoDialog_delete.show();
                return false;
            }
        });
        messageListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                switch (view.getId()) {
                    case R.id.constrain:
                        /**
                         * 通知类型：1.风暖加热器报警
                         * 2.水暖加热器故障报警3.汽车报警
                         * 8.普通消息 9.广告消息 11.商城消息
                         * 12.商城订单消息 13.拼单消息
                         */
                        MessageModel.DataBean dataBean = (MessageModel.DataBean) adapter.getData().get(position);
                        switch (mDatas.get(position).getNotify_type()) {
                            case "1":
                            case "2":
                            case "3":
                                //CheLianWangNoticeActvity.actionStart(getActivity(), dataBean.getNotify_id());
                                break;
                            case "8":
                                break;
                            case "9":
                                break;
                            case "11":
                                break;
                            case "12":
                                XiaoXiEnterDingDanActivity.actionStart(getActivity(), mDatas.get(position).getOper_id());
                                break;
                            case "13":
                                break;
                        }
                        break;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        messageListAdapter = new MessageListAdapter(R.layout.item_messagelist, mDatas);
        messageListAdapter.openLoadAnimation();//默认为渐显效果
        recyclerView.setAdapter(messageListAdapter);
    }

    public void deleteMessageNet(String notifyId, String of_user_id) {
        //访问网络获取数据 下面的列表数据
        Map<String, Object> map = new HashMap<>();
        map.put("code", "03002");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(getApp()).getAppToken());
        map.put("notify_id", notifyId);
        map.put("of_user_id", of_user_id);
        Gson gson = new Gson();
        OkGo.<AppResponse>post(MESSAGE_URL)
                .tag(this)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse>() {
                    @Override
                    public void onSuccess(Response<AppResponse> response) {
                        showLoadSuccess();
                        srLSmart.autoRefresh();
                    }

                    @Override
                    public void onStart(Request<AppResponse, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }
                });
    }
}

