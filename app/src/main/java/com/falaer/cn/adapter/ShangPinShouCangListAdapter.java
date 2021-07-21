package com.falaer.cn.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.falaer.cn.R;
import com.falaer.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.falaer.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.falaer.cn.model.ShangJiaListModel;
import com.falaer.cn.util.GlideShowImageUtils;

import java.util.List;

public class ShangPinShouCangListAdapter extends BaseQuickAdapter<ShangJiaListModel.DataBean, BaseViewHolder> {
    public ShangPinShouCangListAdapter(int layoutResId, @Nullable List<ShangJiaListModel.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShangJiaListModel.DataBean item) {
        Glide.with(mContext).applyDefaultRequestOptions(GlideShowImageUtils.showZhengFangXing()).load(item.getIndex_photo_url()).into((ImageView) helper.getView(R.id.iv_image));
        helper.setText(R.id.tv_title, item.getProduct_title());
        helper.setText(R.id.tv_price, "¥" + item.getMoney_begin());
        helper.addOnClickListener(R.id.constrain);

    }
}
