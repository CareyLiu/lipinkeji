package com.lipinkeji.cn.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.model.GoodsDetails_f;

import java.util.List;

public class SongZhuangServerAdapter extends BaseQuickAdapter<GoodsDetails_f.DataBean.InstallationTypeBean, BaseViewHolder> {


    public SongZhuangServerAdapter(int layoutResId, @Nullable List<GoodsDetails_f.DataBean.InstallationTypeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsDetails_f.DataBean.InstallationTypeBean item) {
        helper.setText(R.id.tv_text, item.installation_type_name);
        helper.addOnClickListener(R.id.constrain);

        ConstraintLayout constraintLayout = helper.getView(R.id.constrain);
        if (item.install_default.equals("1")) {//xuanzhong
            TextView tv = helper.getView(R.id.tv_text);
            constraintLayout.setBackgroundResource(R.drawable.background_select);
            tv.setTextColor(mContext.getResources().getColor(R.color.FC0100));
        } else {
            TextView tv = helper.getView(R.id.tv_text);
            constraintLayout.setBackgroundResource(R.drawable.background_noselect);
            tv.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
        }


    }
}
