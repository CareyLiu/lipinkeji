package com.lipinkeji.cn.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.DefaultX5WebViewActivity;
import com.lipinkeji.cn.activity.dingdan.AccessActivity;
import com.lipinkeji.cn.activity.dingdan.DaiFuKuanDingDanActivity;
import com.lipinkeji.cn.activity.dingdan.DingDanShenQingTuikuanActivity;
import com.lipinkeji.cn.activity.dingdan.OrderTuiKuanDetailsActivity;
import com.lipinkeji.cn.activity.dingdan.ShenQingTuiKuanActivity;
import com.lipinkeji.cn.activity.zijian_shangcheng.ZiJianShopMallDetailsActivity;
import com.lipinkeji.cn.adapter.dingdan.OrderListAdapter;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.app.UIHelper;
import com.lipinkeji.cn.basicmvp.BaseFragment;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.OrderListModel;
import com.lipinkeji.cn.model.YuZhiFuModel;
import com.lipinkeji.cn.model.YuZhiFuModel_AliPay;
import com.lipinkeji.cn.pay_about.alipay.PayResult;
import com.lipinkeji.cn.util.PaySuccessUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.lipinkeji.cn.app.App.DINGDANZHIFU;
import static com.lipinkeji.cn.app.ConstanceValue.MSG_DINGDAN_PAY;

public class OrderListFragment extends BaseFragment {

    private RecyclerView recyclerView;
    List<OrderListModel.DataBean> mDatas = new ArrayList<>();
    private OrderListAdapter orderListAdapter;
    OrderListModel.DataBean dataBean = new OrderListModel.DataBean();
    String user_pay_check = "0";
    String str;
    Response<AppResponse<OrderListModel.DataBean>> response;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        progressDialog = new ProgressDialog(getActivity());
        str = bundle.getString("title");

        /**
         *         tagList.add("??????");
         *         tagList.add("?????????");
         *         tagList.add("?????????");
         *         tagList.add("?????????");
         *         tagList.add("?????????");
         *         tagList.add("??????");
         *         tagList.add("?????????");
         *         tagList.add("?????????");
         *         tagList.add("??????/??????");
         *         tagList.add("????????????");
         *
         *         ???????????????0.??????1.????????? 3.?????????
         * 4.?????????5.????????????6.?????????7.????????? 8.9.10.??????/??????
         */
        if (str.equals("??????")) {
            user_pay_check = "0";
        } else if (str.equals("?????????")) {
            user_pay_check = "1";
        } else if (str.equals("?????????")) {
            user_pay_check = "3";
        } else if (str.equals("?????????")) {
            user_pay_check = "4";
        } else if (str.equals("??????")) {
            user_pay_check = "5";
        } else if (str.equals("?????????")) {
            user_pay_check = "6";
        } else if (str.equals("?????????")) {
            user_pay_check = "2";
        } else if (str.equals("??????/??????")) {
            user_pay_check = "10";
        } else if (str.equals("????????????")) {
            user_pay_check = "11";
        } else if (str.equals("?????????")) {
            user_pay_check = "7";
        }

        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == MSG_DINGDAN_PAY) {
                    pageNumber = 0;
                    getNet();//??????????????????????????????
                }
            }
        }));
    }

    public int pageNumber = 0;

    public void getNet() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04161");
        map.put("key", Urls.key);
        map.put("token", PreferenceHelper.getInstance(getActivity()).getString("app_token", "0"));
        map.put("user_pay_check", user_pay_check);//??????
        map.put("page_number", String.valueOf(pageNumber));

        Gson gson = new Gson();
        OkGo.<AppResponse<OrderListModel.DataBean>>post(Urls.HOME_PICTURE_HOME)
                .tag(getActivity())//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<OrderListModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<OrderListModel.DataBean>> response) {
                        smartRefreshLayout.finishLoadMore();
                        smartRefreshLayout.finishRefresh();
                        OrderListFragment.this.response = response;
                        if (pageNumber == 0) {
                            mDatas.clear();
                            mDatas.addAll(response.body().data);
                            orderListAdapter.setNewData(mDatas);
                            orderListAdapter.notifyDataSetChanged();
                        } else {
                            mDatas.addAll(response.body().data);
                            orderListAdapter.notifyDataSetChanged();
                        }

                        if (mDatas.size() == 0) {
                            View view = View.inflate(getActivity(), R.layout.layout_orderllist_empty, null);
                            orderListAdapter.setEmptyView(view);
                            orderListAdapter.notifyDataSetChanged();
                        }

                        if (response.body().next.equals("0")) {//????????????
                            smartRefreshLayout.setEnableLoadMore(false);
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<OrderListModel.DataBean>> response) {
                        super.onError(response);
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
    protected void immersionInit(ImmersionBar mImmersionBar) {
        mImmersionBar
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected boolean immersionEnabled() {
        return true;
    }

    private SmartRefreshLayout smartRefreshLayout;

    @Override
    protected int getLayoutRes() {
        return R.layout.order_list;
    }

    @Override
    protected void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        rlMain = rootView.findViewById(R.id.rl_main);
        ivNone = rootView.findViewById(R.id.iv_none);
        view = rootView.findViewById(R.id.view);
        smartRefreshLayout = rootView.findViewById(R.id.srL_smart);
        initSmartRefresh();
        initAdapter();
    }

    public void initSmartRefresh() {
        smartRefreshLayout.setEnableAutoLoadMore(true);
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++pageNumber;
                getNet();

            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNumber = 0;
                getNet();
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
        orderListAdapter = new OrderListAdapter(R.layout.item_order_list, mDatas);
        orderListAdapter.openLoadAnimation();//?????????????????????
        recyclerView.setAdapter(orderListAdapter);

        orderListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderListModel.DataBean dataBean = orderListAdapter.getData().get(position);
                switch (view.getId()) {

                    case R.id.constrain:
                        /**
                         * ???????????????1.??????2.?????? 3.???????????? 4.????????????????????????
                         */
                        if (dataBean.getWares_type().equals("1")) {

                            if (dataBean.getUser_pay_check().equals("8") || dataBean.getUser_pay_check().equals("9") || dataBean.getUser_pay_check().equals("10")) {

                                OrderTuiKuanDetailsActivity.actionStart(getActivity(), dataBean.getShop_form_id());

                            } else {
                                DaiFuKuanDingDanActivity.actionStart(getActivity(), dataBean);
                            }

                        } else if (dataBean.getWares_type().equals("3")) {
                            if (dataBean.getUser_pay_check().equals("8") || dataBean.getUser_pay_check().equals("9") || dataBean.getUser_pay_check().equals("10")) {

                                OrderTuiKuanDetailsActivity.actionStart(getActivity(), dataBean.getShop_form_id());

                            } else if (dataBean.getUser_pay_check().equals("11") || dataBean.getUser_pay_check().equals("7")) {
                                DaiFuKuanDingDanActivity.actionStart(getActivity(), dataBean);

                            } else {

                                DaiFuKuanDingDanActivity.actionStart(getActivity(), dataBean);
                            }

                        }

                        break;
                    case R.id.tv_caozuo:
                        doCaoZuo(dataBean, position);
                        break;
                    case R.id.tv_caozuo1:
                        doCaoZuo1(dataBean, position);
                        break;
                    case R.id.tv_caozuo2:
                        doCaoZuo2(dataBean, position);
                        break;
                }
            }
        });

    }

    /**
     * user_pay_check	????????????:1.????????? 2.?????????(???)3.????????? 4.????????? 5.????????????6.????????? 7.?????? 8.???????????? 9.????????? 10.??????/?????? 11 ????????????
     */

    public void doCaoZuo(OrderListModel.DataBean dataBean, int position) {
        /**
         * user_pay_check	????????????:1.????????? 2.?????????(???)3.????????? 4.????????? 5.????????????6.????????? 7.?????? 8.???????????? 9.????????? 10.??????/?????? 11 ????????????
         */
        switch (dataBean.getUser_pay_check()) {
            case "1":
                showSingSelect(dataBean);
                break;
            case "2":
                // helper.setText(R.id.tv_caozuo, "????????????");
                break;
            case "3":
                ShenQingTuiKuanActivity.actionStart(getActivity(), dataBean.getShop_form_id(), dataBean.getTotal_money(), dataBean.getUser_pay_check());
                break;
            case "4":
                // helper.setText(R.id.tv_caozuo, "????????????");
                // wares_go_type	???????????????2.??????3.??????
                if (dataBean.getWares_go_type().equals("2")) {
                    showDngDanCaoZuo(dataBean, position, "??????????????????", "04164");
                } else if (dataBean.getWares_go_type().equals("3")) {
                    showDngDanCaoZuo(dataBean, position, "?????????????????????", "04164");
                }

                break;
            case "5":
                //helper.setText(R.id.tv_caozuo, "????????????");
                UIHelper.ToastMessage(getActivity(), "????????????");
                Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
                String targetId = response.body().data.get(0).getInst_accid();
                String instName = response.body().data.get(0).getInst_name();
                Bundle bundle = new Bundle();
                bundle.putString("dianpuming", instName);
                bundle.putString("inst_accid", response.body().data.get(0).getInst_accid());
                bundle.putString("shoptype", "2");
                RongIM.getInstance().startConversation(getActivity(), conversationType, targetId, instName);
                break;
            case "6":
                showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                // helper.setText(R.id.tv_caozuo, "????????????");
                // helper.setVisible(R.id.tv_caozuo, false);
                break;
            case "7":
                // helper.setText(R.id.tv_caozuo, "????????????");
                break;
            case "8":
                // helper.setText(R.id.tv_yipingjia, "????????????");
                //  helper.setText(R.id.tv_caozuo, "????????????");
                break;
            case "9":
                //  helper.setText(R.id.tv_yipingjia, "?????????");
                //  helper.setText(R.id.tv_caozuo, "????????????");
                break;
            case "10":
                //  helper.setText(R.id.tv_yipingjia, "??????/??????");
                //   helper.setText(R.id.tv_caozuo, "????????????");
                showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                break;
            case "11":
                //  helper.setText(R.id.tv_yipingjia, "????????????");
                // helper.setText(R.id.tv_caozuo, "????????????");
                // showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                break;

        }
    }

    public void doCaoZuo1(OrderListModel.DataBean dataBean, int position) {
        /**
         * user_pay_check	????????????:1.????????? 2.?????????(???)3.????????? 4.????????? 5.????????????6.????????? 7.?????? 8.???????????? 9.????????? 10.??????/?????? 11 ????????????
         */
        switch (dataBean.getUser_pay_check()) {
            case "1":
                // helper.setText(R.id.tv_caozuo1, "????????????");
                //getNet_QUXIAO(dataBean.getShop_form_id());
                showDngDanCaoZuo(dataBean, position, "??????????????????", "04156");
                break;
            case "2":
                //helper.setText(R.id.tv_caozuo1, "????????????");
                showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                break;
            case "3":
                //helper.setText(R.id.tv_caozuo1, "?????????");
                //getNet_CaoZuo(dataBean.getShop_form_id(), "04167", position);
                //?????????
                getNetCuiFaHuo(dataBean, position);
                break;
            case "4":
                DefaultX5WebViewActivity.actionStart(getActivity(), dataBean.getExpress_url());
                //helper.setText(R.id.tv_caozuo1, "????????????");
                break;
            case "5":
//                helper.setText(R.id.tv_caozuo1, "????????????");
                DingDanShenQingTuikuanActivity.actionStart(getActivity(), "????????????(????????????)", dataBean.getShop_form_id(), dataBean.getPay_money());
                break;
            case "6":
                // helper.setText(R.id.tv_caozuo1, "????????????");
                //  DefaultX5WebViewActivity.actionStart(getActivity(), dataBean.getExpress_url());
                // showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                //?????????
                AccessActivity.actionStart(getActivity(), dataBean.getIndex_photo_url(), dataBean.getShop_form_id());
                break;
            case "7":
                showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                //helper.setText(R.id.tv_caozuo1, "????????????");
                break;
            case "8":
            case "10":
            case "9":
                getActivity().finish();
                /**
                 * ???????????????1.??????2.?????? 3.???????????? 4.????????????????????????
                 */
                if (orderListAdapter.getData().get(position).getWares_type().equals("1")) {
                    ZiJianShopMallDetailsActivity.actionStart(getActivity(), dataBean.getShop_product_id(), dataBean.getWares_id());
                } else if (orderListAdapter.getData().get(position).getWares_type().equals("3")) {

                }
                // helper.setText(R.id.tv_caozuo1, "????????????");
                break;
            // helper.setText(R.id.tv_caozuo1, "????????????");
            //  helper.setText(R.id.tv_caozuo1, "????????????");
            //  helper.setVisible(R.id.tv_caozuo1, false);
            case "11":
                showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                break;
        }
    }

    public void doCaoZuo2(OrderListModel.DataBean dataBean, int position) {
        /**
         * user_pay_check	????????????:1.????????? 2.?????????(???)3.????????? 4.????????? 5.????????????6.????????? 7.?????? 8.???????????? 9.????????? 10.??????/?????? 11 ????????????
         */
        switch (dataBean.getUser_pay_check()) {
            case "1":

                break;
            case "2":

                break;
            case "3":

                break;
            case "4":
                ShenQingTuiKuanActivity.actionStart(getActivity(), dataBean.getShop_form_id(), dataBean.getTotal_money(), dataBean.getUser_pay_check());
                break;

            case "6":
                // helper.setText(R.id.tv_caozuo1, "????????????");
                //  DefaultX5WebViewActivity.actionStart(getActivity(), dataBean.getExpress_url());
                // showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                //?????????
                //AccessActivity.actionStart(getActivity(), dataBean.getInst_img_url(), dataBean.getShop_form_id());
                break;
            case "7":
                // showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                //helper.setText(R.id.tv_caozuo1, "????????????");
                break;
            case "8":
            case "10":
            case "9":
                // getActivity().finish();
                // ZiJianShopMallDetailsActivity.actionStart(getActivity(), dataBean.getShop_product_id(), dataBean.getWares_id());
                // helper.setText(R.id.tv_caozuo1, "????????????");
                break;
            // helper.setText(R.id.tv_caozuo1, "????????????");
            //  helper.setText(R.id.tv_caozuo1, "????????????");
            //  helper.setVisible(R.id.tv_caozuo1, false);
            case "11":
                //  showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                break;
        }
    }

    private void getNetCuiFaHuo(OrderListModel.DataBean dataBean, int position) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04167");
        map.put("key", Urls.key);
        map.put("token", PreferenceHelper.getInstance(getActivity()).getString("app_token", "0"));
        map.put("shop_form_id", dataBean.getShop_form_id());
        Gson gson = new Gson();
        OkGo.<AppResponse<Object>>post(Urls.HOME_PICTURE_HOME)
                .tag(getActivity())//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Object>> response) {
                        UIHelper.ToastMessage(getActivity(), response.body().msg);
                    }

                    @Override
                    public void onError(Response<AppResponse<Object>> response) {
                        super.onError(response);
                        String str = response.getException().getMessage();
                        String[] str1 = str.split("???");
                        if (str1.length == 3) {
                            UIHelper.ToastMessage(getActivity(), str1[2]);
                        }
                    }

                    @Override
                    public void onCacheSuccess(Response<AppResponse<Object>> response) {
                        super.onCacheSuccess(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });

    }

    String payType = "4";//1 ????????? 4 ??????
    private String appId;//??????id ????????????
    ProgressDialog progressDialog;
    private IWXAPI api;
    private String form_id;//??????id
    List<ProductDetails> productDetailsForJava = new ArrayList<>();

    public class ProductDetails {
        private String form_product_id;
        private String shop_product_id;
        private String pay_count;
        private String shop_form_text;
        private String wares_go_type;
        private String installation_type_id;
    }

    private void getWeiXinOrZhiFuBao(String pay_id, OrderListModel.DataBean dataBean) {
        ProductDetails productDetails = new ProductDetails();
        productDetails.shop_product_id = dataBean.getShop_product_id();
        productDetails.pay_count = dataBean.getPay_count();
        productDetails.shop_form_text = dataBean.getShop_form_text();
        productDetails.wares_go_type = dataBean.getWares_go_type();
        productDetails.form_product_id = dataBean.getInstallation_type_id();
        productDetailsForJava.add(productDetails);

        if (pay_id.equals("1")) {//1?????????
            Map<String, Object> map = new HashMap<>();
            map.put("key", Urls.key);
            map.put("token", UserManager.getManager(getActivity()).getAppToken());
            map.put("operate_type", dataBean.getOperate_type());
            map.put("pay_id", pay_id);
            map.put("pay_type", "1");
            map.put("shop_form_id", dataBean.getShop_form_id());
            String myHeaderLog = new Gson().toJson(map);
            String myHeaderInfo = StringEscapeUtils.unescapeJava(myHeaderLog);
            OkGo.<AppResponse<YuZhiFuModel_AliPay.DataBean>>post(Urls.DALIBAO_PAY)
                    .tag(getActivity())//
                    .upJson(myHeaderInfo)
                    .execute(new JsonCallback<AppResponse<YuZhiFuModel_AliPay.DataBean>>() {
                        @Override
                        public void onSuccess(Response<AppResponse<YuZhiFuModel_AliPay.DataBean>> response) {
                            appId = response.body().data.get(0).getPay();
                            form_id = response.body().data.get(0).getOut_trade_no();
                            payV2(appId);//???????????????????????????????????????
                        }
                    });

        } else {//2??????
            //???????????????????????????\
            /**
             * {
             *   "key":"20180305124455yu",
             *  "token":"1234353453453456",
             *  "operate_id":"12",
             *  "operate_type":"1",
             *  "pay_id":"1"
             * }
             */

            Map<String, Object> map = new HashMap<>();
            map.put("key", Urls.key);
            map.put("token", UserManager.getManager(getActivity()).getAppToken());
            map.put("operate_type", dataBean.getOperate_type());
            map.put("pay_id", pay_id);
            map.put("pay_type", "4");
            map.put("shop_form_id", dataBean.getShop_form_id());
            String myHeaderLog = new Gson().toJson(map);
            String myHeaderInfo = StringEscapeUtils.unescapeJava(myHeaderLog);
            OkGo.<AppResponse<YuZhiFuModel.DataBean>>post(Urls.DALIBAO_PAY)
                    .tag(getActivity())//
                    .upJson(myHeaderInfo)
                    .execute(new JsonCallback<AppResponse<YuZhiFuModel.DataBean>>() {
                        @Override
                        public void onSuccess(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                            api = WXAPIFactory.createWXAPI(getActivity(), response.body().data.get(0).getPay().getAppid());
                            form_id = response.body().data.get(0).getPay().getOut_trade_no();
                            goToWeChatPay(response.body().data.get(0));
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                            super.onError(response);
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    /**
     * ????????????
     *
     * @param out
     */
    private void goToWeChatPay(YuZhiFuModel.DataBean out) {
        api = WXAPIFactory.createWXAPI(getActivity(), out.getPay().getAppid());
        api.registerApp(out.getPay().getAppid());
        PayReq req = new PayReq();
        req.appId = out.getPay().getAppid();
        req.partnerId = out.getPay().getPartnerid();
        req.prepayId = out.getPay().getPrepayid();
        req.timeStamp = out.getPay().getTimestamp();
        req.nonceStr = out.getPay().getNoncestr();
        req.sign = out.getPay().getSign();
        req.packageValue = out.getPay().getPackageX();
        api.sendReq(req);
    }

    private static final int SDK_PAY_FLAG = 1;

    /**
     * ?????????????????????
     */
    public void payV2(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                     */
                    String resultInfo = payResult.getResult();// ?????????????????????????????????
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        PaySuccessUtils.getNet(getActivity(), form_id);
                        UIHelper.ToastMessage(getActivity(), "????????????", Toast.LENGTH_SHORT);
                        pageNumber = 0;
                        getNet();//??????????????????????????????
                    } else {
                        // ???????????????????????????????????????????????????????????????????????????
                        Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        PaySuccessUtils.getNetFail(getActivity(), form_id);
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    private int choice;
    private AlertDialog.Builder builder;

    /**
     * ?????? dialog
     */
    private void showSingSelect(OrderListModel.DataBean dataBean) {

        //?????????????????????
        final String[] items = {"??????", "?????????"};
        choice = -1;
        builder = new AlertDialog.Builder(getActivity()).setIcon(R.mipmap.logi_icon).setTitle("????????????")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        choice = i;
                    }
                }).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (choice == -1) {
                            Toast.makeText(getActivity(), "????????????????????????????????????", Toast.LENGTH_LONG).show();
                        } else if (items[choice].equals("??????")) {
                            //??????
                            String pay_id = "2";
                            PreferenceHelper.getInstance(getActivity()).putString(DINGDANZHIFU, DINGDANZHIFU);
                            getWeiXinOrZhiFuBao(pay_id, dataBean);
                            dialogInterface.dismiss();
                        } else {

                            String pay_id = "1";
                            getWeiXinOrZhiFuBao(pay_id, dataBean);
                            dialogInterface.dismiss();
                        }
                    }
                });
        builder.create().show();
    }


    public void getNet_CaoZuo(String form_id, String code, int position) {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("key", Urls.key);
        map.put("token", PreferenceHelper.getInstance(getActivity()).getString("app_token", "0"));
        map.put("shop_form_id", form_id);//??????

        Gson gson = new Gson();
        OkGo.<AppResponse<Object>>post(Urls.HOME_PICTURE_HOME)
                .tag(getActivity())//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Object>> response) {
                        //orderListAdapter.remove(position);
                        UIHelper.ToastMessage(getActivity(), "????????????");
                        //pageNumber = 0;
                        //getNet();
                        smartRefreshLayout.autoRefresh();
                    }

                    @Override
                    public void onError(Response<AppResponse<Object>> response) {
                        super.onError(response);
                        String str = response.getException().getMessage();
                        Log.i("cuifahuo", str);

                        String[] str1 = str.split("???");

                        if (str1.length == 3) {
                            UIHelper.ToastMessage(getActivity(), str1[2]);
                        }
                    }
                });

    }

    /**
     * ??????????????? dialog
     */
    private void showDngDanCaoZuo(OrderListModel.DataBean dataBean, int position, String quXiaoDingDanHuaShu, String code) {

        builder = new AlertDialog.Builder(getActivity()).setIcon(R.mipmap.logi_icon).setTitle("????????????")
                .setMessage(quXiaoDingDanHuaShu).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: ??????????????????
                        //Toast.makeText(getActivity(), "????????????", Toast.LENGTH_LONG).show();
                        getNet_CaoZuo(dataBean.getShop_form_id(), code, position);


                    }
                }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: ??????????????????
                        // Toast.makeText(getActivity(), "????????????", Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}

