package com.falaer.cn.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.falaer.cn.R;
import com.falaer.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.falaer.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.falaer.cn.model.ZiJianHomeModel;

import java.util.List;

public class ZiJian_HeaderAdapter extends BaseQuickAdapter<ZiJianHomeModel.DataBean.IconListBean, BaseViewHolder> {
    public ZiJian_HeaderAdapter(int layoutResId, @Nullable List<ZiJianHomeModel.DataBean.IconListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZiJianHomeModel.DataBean.IconListBean item) {
        Glide.with(mContext).load(item.getImg_url()).into((ImageView) helper.getView(R.id.iv_image));
        helper.setText(R.id.tv_title, item.getName());
        helper.addOnClickListener(R.id.constrain);
    }

}
