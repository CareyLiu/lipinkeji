package com.lipinkeji.cn.activity.device_shuinuan;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.config.MyApplication;
import com.lipinkeji.cn.dialog.newdia.TishiDialog;

import static com.lipinkeji.cn.config.MyApplication.getServer_id;

public class FengNuanBaseNewActivity extends BaseActivity {

    public static String FN_Send = "wit/cbox/hardware/" + getServer_id() + MyApplication.getCcid();
    public static String FN_Accept = "wit/cbox/app/" + getServer_id() + MyApplication.getCcid();
    public static String ccid;
    public static String msgData;

    public final String dianHuoSai = "M56";//点火塞加热命令
    public final String youbeng = "M57";//油泵加热命令


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
