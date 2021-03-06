package com.lipinkeji.cn.adapter.gaiban;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.lipinkeji.cn.model.Home;
import com.lipinkeji.cn.model.Home_NewBean;
import com.lipinkeji.cn.view.CornerTransform;

import java.util.List;

public class HomeReMenAdapter extends BaseQuickAdapter<Home_NewBean.DataBean.IndexShowListBean, com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder> {
    public HomeReMenAdapter(int layoutResId, @Nullable List<Home_NewBean.DataBean.IndexShowListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder helper, Home_NewBean.DataBean.IndexShowListBean item) {
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
