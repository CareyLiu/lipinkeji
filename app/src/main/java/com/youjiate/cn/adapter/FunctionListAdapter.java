package com.youjiate.cn.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.youjiate.cn.R;
import com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.youjiate.cn.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.youjiate.cn.model.FunctionListBean;

import java.util.List;

public class FunctionListAdapter extends BaseQuickAdapter<FunctionListBean, BaseViewHolder> {
    public FunctionListAdapter(int layoutResId, @Nullable List<FunctionListBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, FunctionListBean item) {
        helper.setText(R.id.tv_text, item.textName);
        //Glide.with(mContext).load(item.image).into((ImageView) helper.getView(R.id.iv_image));

        ImageView ivImage = helper.getView(R.id.iv_image);
        ivImage.setBackgroundResource(item.image);
//        if (helper.getAdapterPosition() == 0) {
//            setMargins(helper.getView(R.id.constrain), DensityUtil.dp2px(13), DensityUtil.dp2px(10), 0, 0);
//        }
//        setMargins(helper.getView(R.id.constrain), DensityUtil.dp2px(13), DensityUtil.dp2px(10), 0, 0);


        Log.i("getPosition   ：   ", helper.getPosition() + "");
        Log.i("getAdapterPosition ：  ", helper.getAdapterPosition() + "");
        Log.i("getLayoutPosition  ： ", helper.getLayoutPosition() + "");
        //    Log.i("getOldPosition  ： ",helper.getOldPosition()+"");


        helper.addOnClickListener(R.id.constrain);
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
