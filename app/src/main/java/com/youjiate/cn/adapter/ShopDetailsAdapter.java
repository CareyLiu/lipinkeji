package com.youjiate.cn.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.youjiate.cn.R;
import com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.youjiate.cn.model.DianPuXiangQingModel;
import com.youjiate.cn.util.GlideShowImageUtils;

import java.util.List;

public class ShopDetailsAdapter extends BaseQuickAdapter<DianPuXiangQingModel.DataBean.WaresListBean, BaseViewHolder> {
    public ShopDetailsAdapter(int layoutResId, @Nullable List<DianPuXiangQingModel.DataBean.WaresListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DianPuXiangQingModel.DataBean.WaresListBean item) {

        Glide.with(mContext).applyDefaultRequestOptions(GlideShowImageUtils.showZhengFangXing()).load(item.getWares_photo_url()).into((ImageView) helper.getView(R.id.iv_img));
        helper.setText(R.id.tv_name, item.getShop_product_title());
        helper.setText(R.id.tv_price, item.getMoney_begin());
        helper.setText(R.id.tv_fukuanrenshu, item.getWares_sales_volume() + "人付款");

    }
}
