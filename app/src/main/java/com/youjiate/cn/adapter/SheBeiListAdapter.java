package com.youjiate.cn.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youjiate.cn.R;
import com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseSectionQuickAdapter;
import com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.youjiate.cn.model.SheBeiModel;

import java.util.List;

public class SheBeiListAdapter extends BaseSectionQuickAdapter<SheBeiModel, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public SheBeiListAdapter(int layoutResId, int sectionHeadResId, List<SheBeiModel> data) {
        super(layoutResId, sectionHeadResId, data);

    }


    @Override
    protected void convertHead(BaseViewHolder helper, SheBeiModel item) {
        helper.setText(R.id.tv_name, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, SheBeiModel item) {
        TextView tv_share_name = helper.getView(R.id.tv_share_name);
        TextView tv_state = helper.getView(R.id.tv_state);
        ImageView iv_share_icon = helper.getView(R.id.iv_share_icon);
        ImageView iv_icon = helper.getView(R.id.iv_icon);


        if (!TextUtils.isEmpty(item.share_type)) {
            if (item.share_type.equals("2")) {
                tv_share_name.setVisibility(View.VISIBLE);
                iv_share_icon.setImageResource(R.mipmap.youjaite_gongxiangshebei);
            } else {
                tv_share_name.setVisibility(View.GONE);
                iv_share_icon.setImageResource(R.mipmap.youjaite_wugongxiangshebei);
            }
        } else {
            tv_share_name.setVisibility(View.GONE);
            iv_share_icon.setImageResource(R.mipmap.youjaite_wugongxiangshebei);
        }

        Glide.with(mContext).load(item.device_img_url).into(iv_icon);

        helper.setText(R.id.tv_name, item.device_name);

        if (item.validity_state.equals("1")) {
            tv_state.setText("使用中");
            tv_state.setBackgroundResource(R.drawable.bg_shebei_shiyong_sel);
        } else if (item.validity_state.equals("2")) {
            tv_state.setText("已失效");
            tv_state.setBackgroundResource(R.drawable.bg_shebei_shiyong_nor);
        }

        helper.setText(R.id.tv_ccid, "设备码: " + item.ccid);
        helper.setText(R.id.tv_youxiaoqi, "设备有效期至：" + item.validity_time);

        helper.addOnClickListener(R.id.constrain);
    }
}
