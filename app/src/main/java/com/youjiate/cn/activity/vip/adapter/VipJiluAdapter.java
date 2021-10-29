package com.youjiate.cn.activity.vip.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

public class VipJiluAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public VipJiluAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
