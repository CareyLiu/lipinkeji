package com.lipinkeji.cn.adapter.xin_tuanyou;

import androidx.annotation.Nullable;

import com.lipinkeji.cn.R;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.lipinkeji.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.lipinkeji.cn.model.YouZhanDetailsModel;

import java.util.List;

//多少千米
public class DetailsAllProductAdapter extends BaseQuickAdapter<YouZhanDetailsModel.DataBean.OilPriceListBean, BaseViewHolder> {


    public DetailsAllProductAdapter(int layoutResId, @Nullable List<YouZhanDetailsModel.DataBean.OilPriceListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, YouZhanDetailsModel.DataBean.OilPriceListBean item) {

        helper.setText(R.id.tv_text, item.getOilName());
        if (item.getIsSelect().equals("0")) {
            helper.setBackgroundRes(R.id.tv_text, R.drawable.item_youzhan_details_back);


        } else if (item.getIsSelect().equals("1")) {
            helper.setBackgroundRes(R.id.tv_text, R.drawable.item_youzhan_details_pink);
        }

        helper.addOnClickListener(R.id.constrain);
    }
}
