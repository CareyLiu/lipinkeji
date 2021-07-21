package com.falaer.cn.adapter;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.falaer.cn.R;
import com.falaer.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.falaer.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.falaer.cn.model.GuanZhuDianPuListModel;
import com.falaer.cn.util.GlideShowImageUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GuanZhuDianPuListAdapter extends BaseQuickAdapter<GuanZhuDianPuListModel.DataBean, BaseViewHolder> {
    public GuanZhuDianPuListAdapter(int layoutResId, @Nullable List<GuanZhuDianPuListModel.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GuanZhuDianPuListModel.DataBean item) {
        helper.setText(R.id.tv_title, item.getInst_name());
        Glide.with(mContext).applyDefaultRequestOptions(GlideShowImageUtils.showZhengFangXing()).load(item.getInst_photo_url()).into((CircleImageView) helper.getView(R.id.iv_image));

        helper.addOnClickListener(R.id.constrain);

    }
}
