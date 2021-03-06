package com.lipinkeji.cn.activity.dingdan;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.lipinkeji.cn.R;
import com.lipinkeji.cn.adapter.NewsFragmentPagerAdapter;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.fragment.OrderListFragment;
import com.lipinkeji.cn.model.MessageListBean;
import com.lipinkeji.cn.view.CustomViewPager;
import com.lipinkeji.cn.view.magicindicator.MagicIndicator;
import com.lipinkeji.cn.view.magicindicator.ViewPagerHelper;
import com.lipinkeji.cn.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.lipinkeji.cn.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.lipinkeji.cn.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.lipinkeji.cn.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.lipinkeji.cn.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.lipinkeji.cn.view.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import com.lipinkeji.cn.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MyOrderActivity extends BaseActivity {
    MagicIndicator magicIndicator;

    List<String> tagList;
    CustomViewPager viewPager;
    ArrayList<Fragment> messageListFragments = new ArrayList<>();
    List<MessageListBean> listBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        chooseXiangMu = getIntent().getStringExtra("chooseXiangMu");
        tagList = new ArrayList<>();
        tagList.add("全部");
        tagList.add("待付款");
        tagList.add("待分享");
        tagList.add("待发货");
        tagList.add("待收货");
        tagList.add("到店");
        tagList.add("待评价");
        tagList.add("已完成");
        tagList.add("退货/退款");
        tagList.add("订单失效");
        magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator4);
        viewPager = findViewById(R.id.view_pager);
        setThisAdapter();
        initMagicIndicator1(tagList);

        if (chooseXiangMu.equals("全部")) {
            viewPager.setCurrentItem(0);
        } else if (chooseXiangMu.equals("待付款")) {
            viewPager.setCurrentItem(1);
        } else if (chooseXiangMu.equals("待发货")) {
            viewPager.setCurrentItem(3);
        } else if (chooseXiangMu.equals("待收货")) {
            viewPager.setCurrentItem(4);
        } else if (chooseXiangMu.equals("到店")) {
            viewPager.setCurrentItem(5);
        } else if (chooseXiangMu.equals("待评价")) {
            viewPager.setCurrentItem(6);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_my_order);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initMagicIndicator1(final List<String> list) {
        MagicIndicator magicIndicator = findViewById(R.id.magic_indicator4);
        CommonNavigator commonNavigator = new CommonNavigator(MyOrderActivity.this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(MyOrderActivity.this.getResources().getColor(R.color.black_666666));
                simplePagerTitleView.setSelectedColor(MyOrderActivity.this.getResources().getColor(R.color.text_color_main));
                simplePagerTitleView.setText(list.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(MyOrderActivity.this.getResources().getColor(R.color.text_color_main));
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }


    private void setThisAdapter() {
        int count = tagList.size();
        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
            data.putString("title", tagList.get(i));
            OrderListFragment newfragment = new OrderListFragment();
            newfragment.setArguments(data);
            messageListFragments.add(newfragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), messageListFragments);
        viewPager.setAdapter(mAdapetr);
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("我的订单");
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

    private String chooseXiangMu;

    /**
     * 用于其他Activty跳转到该Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String chooseXiangMu) {
        Intent intent = new Intent(context, MyOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("chooseXiangMu", chooseXiangMu);
        context.startActivity(intent);
    }
}
