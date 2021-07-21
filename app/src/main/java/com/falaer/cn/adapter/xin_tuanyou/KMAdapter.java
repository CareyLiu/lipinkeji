package com.falaer.cn.adapter.xin_tuanyou;

import androidx.annotation.Nullable;

import com.falaer.cn.R;
import com.falaer.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.falaer.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.falaer.cn.model.JiaYouFirstModel;

import java.util.List;

//多少千米
public class KMAdapter extends BaseQuickAdapter<JiaYouFirstModel.DataBean.DiatanceListBean, BaseViewHolder> {


    public KMAdapter(int layoutResId, @Nullable List<JiaYouFirstModel.DataBean.DiatanceListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JiaYouFirstModel.DataBean.DiatanceListBean item) {
        if (item.getIsSelect().equals("0")) {
            helper.setBackgroundRes(R.id.tv_text, R.drawable.item_km_back);

        } else if (item.getIsSelect().equals("1")) {
            helper.setBackgroundRes(R.id.tv_text, R.drawable.item_km_pink);
        }


        helper.setText(R.id.tv_text, item.getName());
        helper.addOnClickListener(R.id.constrain);
    }
}
