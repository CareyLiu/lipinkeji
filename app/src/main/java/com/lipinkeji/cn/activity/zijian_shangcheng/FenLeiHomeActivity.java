package com.lipinkeji.cn.activity.zijian_shangcheng;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.adapter.FenLeiHomeLeftListAdapter;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.fragment.zijian_shopmall.FenLeiRightFragment;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.ZiJianFenLeiBean;
import com.lipinkeji.cn.project_A.zijian_interface.FenLeiInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.lipinkeji.cn.get_net.Urls.ZIYINGFENLEI;

public class FenLeiHomeActivity extends BaseActivity implements FenLeiInterface {

    @BindView(R.id.rlv_list)
    RecyclerView rlvList;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    FenLeiHomeLeftListAdapter leftListAdapter;
    List<Object> objects = new ArrayList<>();
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNet();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    @Override
//    public boolean showToolBar() {
//        return true;
//    }
//
//    @Override
//    protected void initToolbar() {
//        super.initToolbar();
//        tv_title.setText("????????????");
//        tv_title.setTextSize(17);
//        tv_title.setTextColor(getResources().getColor(R.color.black));
//        mToolbar.setNavigationIcon(R.mipmap.backbutton);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //imm.hideSoftInputFromWindow(findViewById(R.id.cl_layout).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                finish();
//            }
//        });

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, FenLeiHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

//    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_zijian_fenlei_home;
    }

    List<String> oneItemList = new ArrayList<>();//????????????
    List<ZiJianFenLeiBean.DataBean> dataBean;//?????????
    List<ZiJianFenLeiBean.DataBean.ItemBeanX> itemBeanXES;//?????????bean

    @Override
    public void getNet() {
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "04103");
        map.put("key", Urls.key);
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<ZiJianFenLeiBean.DataBean>>post(ZIYINGFENLEI)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZiJianFenLeiBean.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ZiJianFenLeiBean.DataBean>> response) {

                        //    objects.addAll(response.body().data);
                        //    leftListAdapter.setNewData(objects);
                        //1????????????
                        dataBean = response.body().data;
                        itemBeanXES = new ArrayList<>();
                        itemBeanXES.addAll(dataBean.get(0).getItem());
                        if (dataBean != null) {

                            for (int i = 0; i < dataBean.size(); i++) {
                                oneItemList.add(dataBean.get(i).getItem_name());
                            }

                            leftList();
                            RightFragMent();
                            leftListAdapter.notifyDataSetChanged();
                        }

                    }
                });
    }

    @Override
    public void leftList() {
        //?????????
        leftListAdapter = new FenLeiHomeLeftListAdapter(R.layout.item_fenlei_left_list, oneItemList, "0");
        rlvList.setAdapter(leftListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvList.setLayoutManager(linearLayoutManager);
        RightFragMent();
    }

    @Override
    public void RightFragMent() {

        //??????Fragment??????
        FenLeiRightFragment fenLeiRightFragment = new FenLeiRightFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_container, fenLeiRightFragment);

        //???????????????Fragment
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("info", (Serializable) itemBeanXES);
        mBundle.putSerializable("one_item", dataBean.get(0).getItem_id());
        fenLeiRightFragment.setArguments(mBundle);
        fragmentTransaction.commit();

        LeftAndRightAction();
    }

    @Override
    public void LeftAndRightAction() {


        leftListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.constrain:
//        //????????????
                        leftListAdapter.strPosition = position;
                        //????????????
                        adapter.notifyDataSetChanged();
                        itemBeanXES = new ArrayList<>();
                        itemBeanXES.addAll(dataBean.get(position).getItem());
                        //??????Fragment??????
                        FenLeiRightFragment fenLeiRightFragment = new FenLeiRightFragment();
                        //???????????????Fragment
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("info", (Serializable) itemBeanXES);
                        mBundle.putString("one_item",dataBean.get(position).getItem_id());
                        fenLeiRightFragment.setArguments(mBundle);
                        replaceFragment(fenLeiRightFragment);

                        break;
                }
            }
        });


    }

    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    //Fragment???????????????

    private void replaceFragment(Fragment fragment) {
// 1.??????FragmentManager???????????????????????????????????????getFragmentManager()????????????
        fragmentManager = getSupportFragmentManager();
// 2.?????????????????????????????????beginTransaction()????????????
        transaction = fragmentManager.beginTransaction();
// 3.????????????????????????????????????????????????replace()????????????????????????????????????id???????????????????????????
        transaction.replace(R.id.fl_container, fragment);  //fr_container?????????fragment????????????????????????????????????????????????
// 4.??????addToBackStack()??????????????????????????????????????????????????????????????????????????????????????????
        transaction.addToBackStack(null);
// 5.????????????,??????commit()???????????????
        transaction.commit();
    }


}
