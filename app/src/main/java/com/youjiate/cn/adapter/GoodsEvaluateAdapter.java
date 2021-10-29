package com.youjiate.cn.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.youjiate.cn.R;
import com.youjiate.cn.model.GoodsDetails_f;

public class GoodsEvaluateAdapter extends ListBaseAdapter<GoodsDetails_f.DataBean.AssListBean> {

    public GoodsEvaluateAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_hot_goods_evaluate;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        RoundedImageView rvHeader = holder.getView(R.id.rv_header);
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvEvaluate = holder.getView(R.id.tv_evaluate);

        Glide.with(mContext).load(getDataList().get(position).getUser_img_url()).into(rvHeader);
        tvName.setText(getDataList().get(position).getUser_name());
        tvEvaluate.setText(getDataList().get(position).getUser_to_text());




    }


}
