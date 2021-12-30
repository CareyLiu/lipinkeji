package com.lipinkeji.cn.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lipinkeji.cn.R;
import com.lipinkeji.cn.util.Y;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseSectionQuickAdapter;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.lipinkeji.cn.model.SheBeiModel;

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
        helper.setText(R.id.tv_name, item.device_name);

        TextView tv_state = helper.getView(R.id.tv_state);
        if (item.validity_state.equals("1")) {
            tv_state.setText("<使用中>");
            tv_state.setTextColor(Y.getColor(R.color.color_main_fu));
        } else if (item.validity_state.equals("2")) {
            tv_state.setText("<已失效>");
            tv_state.setTextColor(Y.getColor(R.color.color_A6A6A6));
        }

        helper.setText(R.id.tv_youxiaoqi, "设备有效期：" + item.validity_time);
        helper.setText(R.id.tv_ccid, "设备码: " + item.ccid);

        TextView tv_share_name = helper.getView(R.id.tv_share_name);
        ImageView iv_share_icon = helper.getView(R.id.iv_share_icon);
        if (!TextUtils.isEmpty(item.share_type)) {
            if (item.share_type.equals("2")) {
                tv_share_name.setVisibility(View.VISIBLE);
                iv_share_icon.setVisibility(View.VISIBLE);
            } else {
                tv_share_name.setVisibility(View.GONE);
                iv_share_icon.setVisibility(View.GONE);
            }
        } else {
            tv_share_name.setVisibility(View.GONE);
            iv_share_icon.setImageResource(R.mipmap.youjaite_wugongxiangshebei);
        }

//        ImageView iv_icon = helper.getView(R.id.iv_icon);
//        Glide.with(mContext).load(item.device_img_url).into(iv_icon);

        helper.addOnClickListener(R.id.constrain);
    }
}
