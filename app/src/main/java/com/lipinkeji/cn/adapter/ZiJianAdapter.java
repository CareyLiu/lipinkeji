package com.lipinkeji.cn.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.lipinkeji.cn.model.ZiJianHomeModel;

import java.util.List;

public class ZiJianAdapter extends BaseQuickAdapter<ZiJianHomeModel.DataBean.WaresListBean, BaseViewHolder> {

    public ZiJianAdapter(int layoutResId, @Nullable List<ZiJianHomeModel.DataBean.WaresListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZiJianHomeModel.DataBean.WaresListBean item) {
        helper.setText(R.id.tv_1, item.getWares_name());
        helper.setText(R.id.tv_zhekoujia, item.getMoney_now());
        Glide.with(mContext).load(item.getIndex_photo_url()).into((ImageView) helper.getView(R.id.iv_image));
        helper.setText(R.id.tv_fukuanrenshu, item.getWares_sales_volume());
        helper.addOnClickListener(R.id.constrain);
        helper.setText(R.id.ll_mid, "返" + item.getRed_packet_money() + "红包");


    }

}
