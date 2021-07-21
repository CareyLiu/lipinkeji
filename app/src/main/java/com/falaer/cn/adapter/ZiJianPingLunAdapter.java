package com.falaer.cn.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.falaer.cn.R;
import com.falaer.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.falaer.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.falaer.cn.model.GoodsDetails_f;

import java.util.List;

public class ZiJianPingLunAdapter extends BaseQuickAdapter<GoodsDetails_f.DataBean.AssListBean, BaseViewHolder> {
    public ZiJianPingLunAdapter(@Nullable List<GoodsDetails_f.DataBean.AssListBean> data) {
        super(R.layout.item_zijian_pinglun, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsDetails_f.DataBean.AssListBean item) {

        if (item.getNonDataShow().equals("1")) {// 1 证明数据为空了
            helper.setVisible(R.id.constrain, false);
            helper.setVisible(R.id.tv_access, true);

        } else {

            helper.setVisible(R.id.constrain, true);
            helper.setVisible(R.id.tv_access, false);

            Glide.with(mContext).load(item.getUser_img_url()).into((ImageView) helper.getView(R.id.clv_image));
            helper.setText(R.id.tv_title, item.getUser_name());
            helper.setText(R.id.tv_pinglun_content, item.getUser_to_text());
        }

    }


}
