package com.lipinkeji.cn.activity.device_shuinuan;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;

public class ShuinuanBaseNewActivity extends BaseActivity {

    public static String SN_Send = "wh/hardware/";//"wh/hardware/";
    public static String SN_Accept;//"wh/app/";
    public static String ccid;
    public static String msgData;

    public final String dianHuoSai = "M_s03";//点火塞加热命令
    public final String youbeng = "M_s04";//油泵加热命令



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void showTiDialog(String msg) {
        TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_XIAOXI, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {

            }

            @Override
            public void onDismiss(TishiDialog dialog) {

            }
        });
        dialog.setTextContent(msg);
        dialog.show();
    }
}
