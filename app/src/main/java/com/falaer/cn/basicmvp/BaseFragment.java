package com.falaer.cn.basicmvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.billy.android.loading.Gloading;
import com.falaer.cn.app.Notice;
import com.falaer.cn.app.RxBus;
import com.falaer.cn.app.RxUtils;
import com.lzy.okgo.OkGo;

import butterknife.ButterKnife;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseFragment<T extends BasicPresenter, E extends BasicModel> extends BasicFragment<T, E> {

    protected CompositeSubscription _subscriptions = new CompositeSubscription();

    protected Gloading.Holder mHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
        return rootView;
    }

    /**
     * make a Gloading.Holder wrap with current activity by default
     * override this method in subclass to do special initialization
     */
    protected void initLoadingStatusViewIfNeed() {
        if (mHolder == null) {
            //bind status view to activity root view by default
            mHolder = Gloading.getDefault().wrap(getActivity()).withRetry(new Runnable() {
                @Override
                public void run() {
                    onLoadRetry();
                }
            });
        }
    }

    protected void onLoadRetry() {
        // override this method in subclass to do retry task
    }

    public void showLoading() {
        initLoadingStatusViewIfNeed();
        mHolder.showLoading();
    }

    public void showLoadSuccess() {
        initLoadingStatusViewIfNeed();
        mHolder.showLoadSuccess();
    }

    public void showLoadFailed() {
        initLoadingStatusViewIfNeed();
        mHolder.showLoadFailed();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!_subscriptions.isUnsubscribed()) {
            _subscriptions.unsubscribe();
        }
        // ButterKnife.unbind(this);
    }

    /**
     * 注册事件通知
     */
    public Observable<Notice> toObservable() {
        return RxBus.getDefault().toObservable(Notice.class);
    }

    /**
     * 发送消息
     */
    public void sendRx(Notice msg) {
        RxBus.getDefault().sendRx(msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
    }
}
