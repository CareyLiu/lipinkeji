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
        helper.addOnClickListener(R.id.constrain);

//        String substring = item.ccid.substring(11);
//        helper.setText(R.id.tv_ccid, "设备码: " + substring);
        helper.setText(R.id.tv_ccid, "设备码: " + item.ccid);


        helper.setText(R.id.tv_youxiaoqi, "设备有效期至：" + item.validity_time);
        helper.setText(R.id.tv_shiyong_zhuangtai, item.validity_term);



        Glide.with(mContext).load(item.device_img_url).into((ImageView) helper.getView(R.id.iv_image));
        //1.使用中 2.已过期 3.长久有效

        if (item.validity_state.equals("1")) {
            helper.getView(R.id.tv_shiyong_zhuangtai).setBackgroundResource(R.drawable.bg_shebei_shiyongzhuangtai_red);
            TextView tv = (TextView) helper.getView(R.id.tv_shiyong_zhuangtai);
            tv.setTextColor(Color.parseColor("#FF5A00"));
            tv.setText("<使用中>");

        } else if (item.validity_state.equals("2")) {
            helper.getView(R.id.tv_shiyong_zhuangtai).setBackgroundResource(R.drawable.bg_shebei_shiyongzhuangtai_gray);
            TextView tv = (TextView) helper.getView(R.id.tv_shiyong_zhuangtai);
            tv.setTextColor(mContext.getResources().getColor(R.color.gray_999999));
            tv.setText("<已失效>");
        }

        TextView tv_gongxiang = helper.getView(R.id.tv_gongxiang);
        if (!TextUtils.isEmpty(item.share_type)) {
            if (item.share_type.equals("2")) {
                tv_gongxiang.setVisibility(View.VISIBLE);
                helper.setVisible(R.id.tv_gongxiang, true);
                helper.setVisible(R.id.iv_biaozhi, true);
            } else {
                tv_gongxiang.setVisibility(View.GONE);
                helper.setVisible(R.id.tv_gongxiang, false);
                helper.setVisible(R.id.iv_biaozhi, false);
            }
        } else {
            tv_gongxiang.setVisibility(View.GONE);
            helper.setVisible(R.id.tv_gongxiang, false);
            helper.setVisible(R.id.iv_biaozhi, false);
        }

        helper.setText(R.id.tv_name, item.device_name);
    }
}
