package com.lipinkeji.cn.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.adapter.NewsFragmentPagerAdapter;
import com.lipinkeji.cn.basicmvp.BaseFragment;
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
import java.util.Observable;
import java.util.Observer;


/**
 * Created by Administrator on 2018/3/29 0029.
 */

public class MessagerFragment extends BaseFragment implements Observer {
    private static final String TAG = "MessageFragment";
    MagicIndicator magicIndicator;

    List<String> tagList;
    CustomViewPager viewPager;
    ArrayList<Fragment> messageListFragments = new ArrayList<>();
    List<MessageListBean> listBeans = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagList = new ArrayList<>();
        tagList.add("全部");
        tagList.add("未读");
        tagList.add("已读");
        tagList.add("已处理");
        tagList.add("未处理");
    }


    @Override
    protected void initLogic() {

    }

    public void initData() {


    }

    @Override
    protected void immersionInit(ImmersionBar mImmersionBar) {
        mImmersionBar.with(this).statusBarDarkFont(true)    .fitsSystemWindows(true).statusBarColor(R.color.white).init();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(View rootView) {
        magicIndicator = (MagicIndicator) rootView.findViewById(R.id.magic_indicator4);
        viewPager = rootView.findViewById(R.id.view_pager);
        rootView.setClickable(true);// 防止点击穿透，底层的fragment响应上层点击触摸事件
        setThisAdapter();
        initMagicIndicator1(rootView, tagList);
    }


    @Override
    protected boolean immersionEnabled() {
        return true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void update(Observable o, Object arg) {

        if (arg.equals("update")) {
            initData();
        }
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initMagicIndicator1(View view, final List<String> list) {
        MagicIndicator magicIndicator = view.findViewById(R.id.magic_indicator4);
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(getActivity().getResources().getColor(R.color.black_666666));
                simplePagerTitleView.setSelectedColor(getActivity().getResources().getColor(R.color.black_111111));
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
                linePagerIndicator.setColors(getActivity().getResources().getColor(R.color.black_111111));
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
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getFragmentManager(), messageListFragments);
        viewPager.setAdapter(mAdapetr);

    }

}
