package com.youjiate.cn.activity.tongcheng58;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youjiate.cn.R;
import com.youjiate.cn.activity.tongcheng58.adapter.TcHomeTagAdapter;
import com.youjiate.cn.activity.tongcheng58.model.TcHomeModel;
import com.youjiate.cn.basicmvp.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TongchengTagItemFragment extends BaseFragment {

    @BindView(R.id.rv_tag_list)
    RecyclerView rv_tag_list;

    private List<TcHomeModel.DataBean.IconListBean> iconList;
    private TcHomeTagAdapter adapter;

    public TongchengTagItemFragment(List<TcHomeModel.DataBean.IconListBean> iconList) {
        this.iconList = iconList;
    }

    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.tongcheng_frag_home_tag;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        initStart();
        return rootView;
    }

    private void initStart() {
        adapter = new TcHomeTagAdapter(R.layout.tongcheng_item_home_tag, iconList);
        rv_tag_list.setLayoutManager(new GridLayoutManager(getContext(), 5));
        rv_tag_list.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TcHomeModel.DataBean.IconListBean listBean = iconList.get(position);
                GongJiangLieBiaoNewActivity.actionStart(getContext(), listBean.getService_type());
            }
        });
    }
}
