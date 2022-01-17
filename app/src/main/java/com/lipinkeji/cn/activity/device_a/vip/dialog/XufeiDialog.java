package com.lipinkeji.cn.activity.device_a.vip.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_a.vip.adapter.XufeiAdapter;
import com.lipinkeji.cn.activity.device_a.vip.model.XufeiModel;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class XufeiDialog extends Dialog implements View.OnClickListener {

    private TextView tv_shebei_youxiaoqi;
    private TextView bt_xufei;
    private RecyclerView rv_list;
    private Context mContext;
    private XufeiClick xufeiClick;

    private List<XufeiModel.DataBean> xufeiModels = new ArrayList<>();
    private XufeiModel.DataBean selectBeen;
    private XufeiAdapter xufeiAdapter;

    public XufeiDialog(Context context) {
        this(context, R.style.dialogBaseBlur);
    }

    public XufeiDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        init();
    }

    public void setXufeiClick(XufeiClick xufeiClick) {
        this.xufeiClick = xufeiClick;
    }

    private void init() {
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        setContentView(R.layout.dialog_xufei);
        tv_shebei_youxiaoqi = findViewById(R.id.tv_shebei_youxiaoqi);
        bt_xufei = findViewById(R.id.bt_xufei);
        rv_list = findViewById(R.id.rv_list);
        bt_xufei.setOnClickListener(this);

        xufeiAdapter = new XufeiAdapter(xufeiModels, mContext);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        rv_list.setAdapter(xufeiAdapter);
        xufeiAdapter.setXufeiClick(new XufeiAdapter.XufeiClick() {
            @Override
            public void xufei(int pos) {
                xufeiAdapter.setCount(pos);
                selectBeen = xufeiModels.get(pos);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == bt_xufei) {
            xufeiClick.xufei();
        }
    }

    public XufeiDialog setTv_shebei_youxiaoqi(String msg) {
        tv_shebei_youxiaoqi.setText("设备有效期至：" + msg);
        return this;
    }

    public XufeiDialog setModels(List<XufeiModel.DataBean> xufeiModels) {
        this.xufeiModels = xufeiModels;
        selectBeen = null;
        xufeiAdapter.setXufeiModels(xufeiModels);
        return this;
    }

    public interface XufeiClick {
        void xufei();
    }
}
