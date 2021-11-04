package com.youjiate.cn.adapter;


import androidx.annotation.Nullable;

import com.youjiate.cn.R;
import com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.youjiate.cn.model.FenLeiThirdModel;

import java.util.List;

public class GongJiangListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public GongJiangListAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.addOnClickListener(R.id.rl_item);
    }
}
