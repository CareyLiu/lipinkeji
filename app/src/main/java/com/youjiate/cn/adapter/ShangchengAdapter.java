package com.youjiate.cn.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youjiate.cn.R;
import com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.youjiate.cn.model.ShangchengModel;
import com.youjiate.cn.view.CornerTransform;

import java.util.List;

import androidx.annotation.Nullable;

public class ShangchengAdapter extends BaseQuickAdapter<ShangchengModel.DataBean.IndexShowListBean, BaseViewHolder> {
    public ShangchengAdapter(int layoutResId, @Nullable List<ShangchengModel.DataBean.IndexShowListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder helper, ShangchengModel.DataBean.IndexShowListBean item) {
        CornerTransform transformation = new CornerTransform(mContext, 15);
        //只是绘制左上角和右上角圆角
        transformation.setExceptCorner(false, false, true, true);

        Glide.with(mContext)
                .asBitmap()
                .skipMemoryCache(true)
                .load(item.getIndex_photo_url())
                .transform(transformation)
                .into((ImageView) helper.getView(R.id.iv_product));
        helper.setText(R.id.tv_title, item.getWares_name());
        helper.setText(R.id.tv_hongbao, "直降" + item.getMoney_lower() + "元");
        helper.setText(R.id.tv_jiage, "¥" + item.getMoney_now());
    }
}
