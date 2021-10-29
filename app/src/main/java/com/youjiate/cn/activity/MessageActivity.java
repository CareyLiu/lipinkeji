package com.youjiate.cn.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.youjiate.cn.R;
import com.youjiate.cn.adapter.NewsFragmentPagerAdapter;
import com.youjiate.cn.app.BaseActivity;
import com.youjiate.cn.fragment.MessageListFragment;
import com.youjiate.cn.model.MessageListBean;
import com.youjiate.cn.view.CustomViewPager;
import com.youjiate.cn.view.magicindicator.MagicIndicator;
import com.youjiate.cn.view.magicindicator.ViewPagerHelper;
import com.youjiate.cn.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.youjiate.cn.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.youjiate.cn.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.youjiate.cn.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.youjiate.cn.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.youjiate.cn.view.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import com.youjiate.cn.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

public class MessageActivity extends BaseActivity {

    private static final String TAG = "MessageFragment";
    MagicIndicator magicIndicator;
    private View rl_finish;

    List<String> tagList;
    CustomViewPager viewPager;
    ArrayList<Fragment> messageListFragments = new ArrayList<>();
    List<MessageListBean> listBeans = new ArrayList<>();

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public void initImmersion() {
        mImmersionBar.with(this).statusBarDarkFont(true).fitsSystemWindows(true).statusBarColor(R.color.white).init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        tagList = new ArrayList<>();
        tagList.add("全部");
        tagList.add("未读");
        tagList.add("已读");
        tagList.add("已处理");
        tagList.add("未处理");

        magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator4);
        rl_finish = findViewById(R.id.rl_back);
        viewPager = findViewById(R.id.view_pager);
        setThisAdapter();
        initMagicIndicator1(tagList);

        rl_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initMagicIndicator1(final List<String> list) {
        MagicIndicator magicIndicator = findViewById(R.id.magic_indicator4);
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.black_666666));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.black_111111));
                simplePagerTitleView.setText(list.get(index));
                //   App.scaleScreenHelper.loadViewSize(simplePagerTitleView, 35);
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
                linePagerIndicator.setColors(getResources().getColor(R.color.black_111111));
                return linePagerIndicator;
            }
        });
        commonNavigator.setAdjustMode(true);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);

    }


    private void setThisAdapter() {
        int count = tagList.size();
        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
            data.putString("title", tagList.get(i));
            MessageListFragment newfragment = new MessageListFragment();
            newfragment.setArguments(data);
            messageListFragments.add(newfragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), messageListFragments);
        viewPager.setAdapter(mAdapetr);

    }
}
