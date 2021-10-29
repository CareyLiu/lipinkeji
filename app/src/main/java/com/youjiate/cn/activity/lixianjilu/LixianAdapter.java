package com.youjiate.cn.activity.lixianjilu;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youjiate.cn.R;

import java.util.List;

import androidx.annotation.Nullable;

public class LixianAdapter extends BaseQuickAdapter<LixianModel.DataBean, BaseViewHolder> {


    public LixianAdapter(int layoutResId, @Nullable List<LixianModel.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LixianModel.DataBean item) {
        helper.setText(R.id.tv_shebeima,item.getTime());
    }
}
