package com.youjiate.cn.activity.vip.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youjiate.cn.R;
import com.youjiate.cn.activity.vip.model.XufeiModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class XufeiAdapter extends RecyclerView.Adapter<XufeiAdapter.XufeiViewHolder> {

    private List<XufeiModel.DataBean> xufeiModels;
    private Context mContext;
    private int count = 0;
    private XufeiClick xufeiClick;

    public XufeiAdapter(List<XufeiModel.DataBean> xufeiModels, Context mContext) {
        this.xufeiModels = xufeiModels;
        this.mContext = mContext;
    }

    public void setXufeiModels(List<XufeiModel.DataBean> xufeiModels) {
        this.xufeiModels = xufeiModels;
        notifyDataSetChanged();
    }

    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    public void setXufeiClick(XufeiClick xufeiClick) {
        this.xufeiClick = xufeiClick;
    }

    @NonNull
    @Override
    public XufeiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_xufei_dialog, parent, false);
        XufeiViewHolder holder = new XufeiViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull XufeiViewHolder holder, int position) {
        if (xufeiModels != null) {
            XufeiModel.DataBean item = xufeiModels.get(position);
            if (count == position) {
                holder.rl_main.setBackgroundResource(R.drawable.bg_xufei_sel);
                holder.tv_shiyong_time.setTextColor(Color.parseColor("#FFBC0D"));
                holder.iv_select.setImageResource(R.mipmap.btn_sel);
            } else {
                holder.rl_main.setBackgroundResource(R.drawable.bg_xufei_nor);
                holder.tv_shiyong_time.setTextColor(Color.parseColor("#000000"));
                holder.iv_select.setImageResource(R.mipmap.btn_nor);
            }

            holder.tv_money.setText(item.getMoney() + "元");
            holder.tv_shiyong_time.setText("(" + item.getYear() + "年使用权)");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (xufeiClick != null) {
                        xufeiClick.xufei(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return xufeiModels.size();
    }

    public class XufeiViewHolder extends RecyclerView.ViewHolder {
        View rl_main;
        TextView tv_money;
        TextView tv_shiyong_time;
        ImageView iv_select;


        public XufeiViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_main = itemView.findViewById(R.id.rl_main);
            tv_money = itemView.findViewById(R.id.tv_money);
            tv_shiyong_time = itemView.findViewById(R.id.tv_shiyong_time);
            iv_select = itemView.findViewById(R.id.iv_select);
        }
    }

    public interface XufeiClick {
        void xufei(int pos);
    }
}
