package com.lipinkeji.cn.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.lipinkeji.cn.app.App;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.app.RxBus;
import com.lipinkeji.cn.common.StringUtils;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.config.Wetch_S;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import rx.Observable;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Wetch_S.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.e("shouquan_huidiao：", "" + req.toString());
    }

    @Override
    public void onResp(BaseResp resp) {


        Log.e("微信支付回调：", "" + resp.errCode);
        String dalibao = PreferenceHelper.getInstance(this).getString(App.DALIBAO_PAY, "");

        String tuangou = PreferenceHelper.getInstance(this).getString(App.TUANGOU_PAY, "");

        String maidan = PreferenceHelper.getInstance(this).getString(App.MAIDAN_PAY, "");

        String xinTuanYouPay = PreferenceHelper.getInstance(this).getString(App.XINTUANYOU_PAY, "");

        String saomaPay = PreferenceHelper.getInstance(this).getString(App.SAOMA_PAY, "");

        String ziYingPay = PreferenceHelper.getInstance(this).getString(App.ZIYING_PAY, "");

        String wodeDingDanZhiFu = PreferenceHelper.getInstance(this).getString(App.DINGDANZHIFU, "");
        String baziPay = PreferenceHelper.getInstance(this).getString(App.BAZI_PAY, "");

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (resp.errCode == 0) {

                if (!StringUtils.isEmpty(dalibao)) {
                    // 通过商品购买正常确认订单支付回调  包括常规商品 大礼包  抢货拼手快商品
                    Notice n = new Notice();
                    n.type = ConstanceValue.MSG_WETCHSUCCESS;
                    //  n.content = message.toString();
                    RxBus.getDefault().sendRx(n);
                    finish();

                    Notice n1 = new Notice();
                    n1.type = ConstanceValue.MSG_DALIBAO_SUCCESS;
                    //  n.content = message.toString();
                    RxBus.getDefault().sendRx(n1);
                    finish();
                    PreferenceHelper.getInstance(this).removeKey(App.DALIBAO_PAY);
                } else if (!StringUtils.isEmpty(tuangou)) {
                    Notice n1 = new Notice();
                    n1.type = ConstanceValue.MSG_TUANGOUPAY;
                    //  n.content = message.toString();
                    RxBus.getDefault().sendRx(n1);
                    WXPayEntryActivity.this.finish();

                    PreferenceHelper.getInstance(this).removeKey(App.TUANGOU_PAY);
                } else if (!StringUtils.isEmpty(maidan)) {
                    Notice n1 = new Notice();
                    n1.type = ConstanceValue.MSG_TUANGOUPAY;
                    //  n.content = message.toString();
                    RxBus.getDefault().sendRx(n1);


                    PreferenceHelper.getInstance(this).removeKey(App.MAIDAN_PAY);
                    WXPayEntryActivity.this.finish();

                } else if (!StringUtils.isEmpty(xinTuanYouPay)) {


                    Notice n1 = new Notice();
                    n1.type = ConstanceValue.MSG_XINTUANYOU_PAY;
                    //  n.content = message.toString();
                    RxBus.getDefault().sendRx(n1);


                    PreferenceHelper.getInstance(this).removeKey(App.XINTUANYOU_PAY);
                    WXPayEntryActivity.this.finish();

                } else if (!StringUtils.isEmpty(saomaPay)) {
                    // UIHelper.ToastMessage(mContext, "支付成功", Toast.LENGTH_SHORT);
                    //finish();
                    //finish();
                    Notice n = new Notice();
                    n.type = ConstanceValue.MSG_SAOMASUCCESS;
                    //  n.content = message.toString();
                    RxBus.getDefault().sendRx(n);
                    PreferenceHelper.getInstance(this).removeKey(App.SAOMA_PAY);
                    WXPayEntryActivity.this.finish();
                } else if (!StringUtils.isEmpty(ziYingPay)) {
                    //finish();
                    Notice n = new Notice();
                    n.type = ConstanceValue.MSG_ZIYING_PAY;
                    //  n.content = message.toString();
                    RxBus.getDefault().sendRx(n);
                    PreferenceHelper.getInstance(this).removeKey(App.ZIYING_PAY);


                    WXPayEntryActivity.this.finish();

                } else if (!StringUtils.isEmpty(wodeDingDanZhiFu)) {

                    PreferenceHelper.getInstance(this).removeKey(App.DINGDANZHIFU);
                    Notice n = new Notice();
                    n.type = ConstanceValue.MSG_DINGDAN_PAY;
                    //  n.content = message.toString();
                    RxBus.getDefault().sendRx(n);

                    WXPayEntryActivity.this.finish();
                } else if (!StringUtils.isEmpty(baziPay)) {
                    //八紫支付成功
                    PreferenceHelper.getInstance(this).removeKey(App.BAZI_PAY);
                    Notice n = new Notice();
                    n.type = ConstanceValue.MSG_SAOMASUCCESS;
                    RxBus.getDefault().sendRx(n);
                    WXPayEntryActivity.this.finish();
                } else {
                    WXPayEntryActivity.this.finish();
                }
            } else {
                if (!StringUtils.isEmpty(xinTuanYouPay)) {
                    Notice n1 = new Notice();
                    n1.type = ConstanceValue.MSG_XINTUANYOU_PAY_FAIL;
                    //  n.content = message.toString();
                    RxBus.getDefault().sendRx(n1);
                    PreferenceHelper.getInstance(this).removeKey(App.XINTUANYOU_PAY);
                    WXPayEntryActivity.this.finish();
                } else if (!StringUtils.isEmpty(saomaPay)) {
                    WXPayEntryActivity.this.finish();
                    // UIHelper.ToastMessage(this, "支付失败");
                    Notice n = new Notice();
                    n.type = ConstanceValue.MSG_SAOMAFAILE;
                    //  n.content = message.toString();
                    RxBus.getDefault().sendRx(n);
                    PreferenceHelper.getInstance(this).removeKey(App.SAOMA_PAY);
                    WXPayEntryActivity.this.finish();
                } else if (!StringUtils.isEmpty(ziYingPay)) {
                    Notice n = new Notice();
                    n.type = ConstanceValue.MSG_ZIYING_PAY_FAIL;
                    //  n.content = message.toString();
                    RxBus.getDefault().sendRx(n);
                    PreferenceHelper.getInstance(this).removeKey(App.ZIYING_PAY);
                    WXPayEntryActivity.this.finish();
                } else if (!StringUtils.isEmpty(baziPay)) {
                    //八紫支付失败
                    PreferenceHelper.getInstance(this).removeKey(App.BAZI_PAY);
                    Notice n = new Notice();
                    n.type = ConstanceValue.MSG_SAOMAFAILE;
                    RxBus.getDefault().sendRx(n);
                    WXPayEntryActivity.this.finish();
                } else {
                    WXPayEntryActivity.this.finish();
                }


            }


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
}