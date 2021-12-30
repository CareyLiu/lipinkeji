package com.lipinkeji.cn.activity.device_shuinuan.gongxiang;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lipinkeji.cn.R;

import java.util.List;

public class GongxiangAdapter extends BaseQuickAdapter<GongxiangModel.DataBean, BaseViewHolder> {

    public GongxiangAdapter(int layoutResId, @Nullable List<GongxiangModel.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GongxiangModel.DataBean item) {
        helper.setText(R.id.tv_name, item.getUser_name());
        helper.setText(R.id.tv_phone, item.getUser_phone());
    }
}
