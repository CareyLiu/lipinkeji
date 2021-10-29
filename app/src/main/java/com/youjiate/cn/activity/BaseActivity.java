package com.youjiate.cn.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.youjiate.cn.R;
import com.youjiate.cn.app.AppManager;
import com.youjiate.cn.app.Notice;
import com.youjiate.cn.app.RxBus;
import com.youjiate.cn.app.RxUtils;
import com.youjiate.cn.config.MyApplication;
import com.youjiate.cn.util.DialogManager;
import com.lzy.okgo.OkGo;

import rx.Observable;
import rx.subscriptions.CompositeSubscription;


public abstract class BaseActivity extends AppCompatActivity {

	/** Application基类对象 **/
	protected MyApplication ac;
	protected Toast toast;
	protected CompositeSubscription _subscriptions = new CompositeSubscription();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		ac = (MyApplication) getApplication();
		StatusBarUtil.setTransparent(this);
		_subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
		getdata();
		getView();
	}

	protected void getView() {

	}

	protected  void getdata(){

	}
	public void showDialog(String msg){
		DialogManager.getManager(this).showMessage(msg);
	}
	public void dismissDialog(){
		DialogManager.getManager(this).dismiss();
	}

	protected void showToast(String msg) {
//		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		if (toast != null)
	    {
	        toast.setText(msg);
	        toast.setDuration(Toast.LENGTH_SHORT);
	        toast.show();
	    } else
	    {
	        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
	        toast.show();
	    }
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}



	/** 带有右进右出动画的退出 **/
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	protected void onDestroy() {
		AppManager.getAppManager().finishActivity(this);
		View currentFocus = getCurrentFocus();
		if (currentFocus != null) {
			currentFocus.clearFocus();
		}
		if(!_subscriptions.isUnsubscribed()) {
			_subscriptions.unsubscribe();
		}
		//根据 Tag 取消请求
		OkGo.getInstance().cancelTag(this);
		super.onDestroy();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	public void onConfigurationChanged(Configuration newConfig) {
		try {
			super.onConfigurationChanged(newConfig);
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				Log.v("yant", "onConfigurationChanged_ORIENTATION_LANDSCAPE");
			} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				Log.v("yant", "onConfigurationChanged_ORIENTATION_PORTRAIT");
			}
		} catch (Exception ex) {
			Log.d("exception",ex.getMessage());
		}
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
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
		return res;
	}
}
