package com.youjiate.cn.activity.vip.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youjiate.cn.R;
import com.youjiate.cn.model.SheBeiModel;

import java.util.List;

import androidx.annotation.Nullable;

public class ViplistAdapter extends BaseQuickAdapter<SheBeiModel, BaseViewHolder> {
    public ViplistAdapter(int layoutResId, @Nullable List<SheBeiModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SheBeiModel item) {
        helper.setText(R.id.tv_name, item.device_name);
        helper.setText(R.id.tv_ccid, "ccid："+item.ccid);
        helper.setText(R.id.tv_data, "设备有效期至："+item.validity_time);
    }
}
