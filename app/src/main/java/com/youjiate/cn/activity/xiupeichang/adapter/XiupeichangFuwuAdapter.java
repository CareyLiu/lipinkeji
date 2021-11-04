package com.youjiate.cn.activity.xiupeichang.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.youjiate.cn.R;
import com.youjiate.cn.activity.xiupeichang.model.XpcDetailsModel;
import com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.youjiate.cn.util.GlideShowImageUtils;

import java.util.List;

public class XiupeichangFuwuAdapter extends BaseQuickAdapter<XpcDetailsModel.DataBean.TaocanListBean, BaseViewHolder> {
    public XiupeichangFuwuAdapter(int layoutResId, @Nullable List<XpcDetailsModel.DataBean.TaocanListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, XpcDetailsModel.DataBean.TaocanListBean item) {
        ImageView iv_img = (ImageView) helper.getView(R.id.iv_img);
        Glide.with(mContext).load(item.getImg_url()).apply(GlideShowImageUtils.showZhengFangXing()).into(iv_img);
        helper.setText(R.id.tv_name, item.getShop_title());
        helper.setText(R.id.tv_content, item.getShop_detail());
        helper.setText(R.id.tv_money, "¥" + item.getShop_money_now());
        helper.setText(R.id.tv_money_yuan, "¥" + item.getShop_money_old());

        if (item.isHideLine()){
            helper.getView(R.id.view_line).setVisibility(View.GONE);
        }else {
            helper.getView(R.id.view_line).setVisibility(View.VISIBLE);
        }
    }
}
