package com.falaer.cn.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.falaer.cn.R;
import com.falaer.cn.basicmvp.BaseFragment;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

public class ShuoMingFragment extends BaseFragment {

    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_main_shuoming;
    }

    @Override
    protected void initView(View rootView) {

    }

    public static ShuoMingFragment newInstance() {
        Bundle args = new Bundle();
        ShuoMingFragment fragment = new ShuoMingFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void immersionInit(ImmersionBar mImmersionBar) {
        mImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.shenlanhei).init();
    }


    @Override
    protected boolean immersionEnabled() {
        return true;
    }

}
