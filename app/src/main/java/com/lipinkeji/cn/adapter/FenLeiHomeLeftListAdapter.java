package com.lipinkeji.cn.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lipinkeji.cn.R;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;

import java.util.List;

public class FenLeiHomeLeftListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public int strPosition;

    public FenLeiHomeLeftListAdapter(int layoutResId, @Nullable List<String> data, String strPosition) {
        super(layoutResId, data);
        this.strPosition = Integer.parseInt(strPosition);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (helper.getLayoutPosition() == strPosition) {
            //  helper.getView(R.id.tv_title).setSelected(true);
            TextView tvTitle = helper.getView(R.id.tv_title);
            tvTitle.setTextColor(mContext.getResources().getColor(R.color.red_61832));
            helper.getView(R.id.constrain).setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            TextView tvTitle = helper.getView(R.id.tv_title);
            tvTitle.setTextColor(mContext.getResources().getColor(R.color.black_ff232323));
            helper.getView(R.id.constrain).setBackgroundColor(mContext.getResources().getColor(R.color.grayfff5f5f5));
        }
        helper.setText(R.id.tv_title, item);
        helper.addOnClickListener(R.id.constrain);
    }
}
