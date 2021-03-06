package com.lipinkeji.cn.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_shuinuan.LipinDashuiActivity;
import com.lipinkeji.cn.activity.device_shuinuan.LipinXiaoshuiActivity;
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
        TextView tv_state = helper.getView(R.id.tv_state);
        if (item.validity_state.equals("1")) {
            tv_state.setText("使用中");
        } else if (item.validity_state.equals("2")) {
            tv_state.setText("已失效");
        }

        helper.setText(R.id.tv_youxiaoqi, item.validity_time);

        String ccidNew = item.ccid.replace("a", "");
        helper.setText(R.id.tv_ccid, ccidNew);

        RelativeLayout tv_share_name = helper.getView(R.id.tv_share_name);
        if (!TextUtils.isEmpty(item.share_type)) {
            if (item.share_type.equals("2")) {
                tv_share_name.setVisibility(View.VISIBLE);
            } else {
                tv_share_name.setVisibility(View.GONE);
            }
        } else {
            tv_share_name.setVisibility(View.GONE);
        }


        helper.addOnClickListener(R.id.constrain);

        String device_type = item.device_type;
        if (device_type.equals("6")) {
            String xinghao = item.ccid.substring(22, 23);
            if (xinghao.equals("2")) {
                helper.setText(R.id.tv_name, item.device_name + "（小水）");
            } else if (xinghao.equals("3")) {
                helper.setText(R.id.tv_name, item.device_name + "（中水）");
            } else if (xinghao.equals("4")) {
                helper.setText(R.id.tv_name, item.device_name + "（大水）");
            } else {
                helper.setText(R.id.tv_name, item.device_name);
            }
        } else {
            helper.setText(R.id.tv_name, item.device_name);
        }
    }
}
