package com.lipinkeji.cn.activity.device_shuinuan.set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.activity.device_a.dialog.JiareqiMimaDialog;
import com.lipinkeji.cn.util.Y;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShuinuanGaojiSetActiviy extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rl_fengyoubicanshu)
    RelativeLayout rlFengyoubicanshu;
    @BindView(R.id.rl_jingxiaoshang)
    RelativeLayout rlJingxiaoshang;
    @BindView(R.id.rl_houtaifuwu)
    RelativeLayout rlHoutaifuwu;

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.a_shuinuan_act_set_gaoji;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShuinuanGaojiSetActiviy.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
//        showMimaDialog();
    }

    private void showMimaDialog() {
        JiareqiMimaDialog mimaDialog = new JiareqiMimaDialog(mContext, new JiareqiMimaDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, JiareqiMimaDialog dialog) {
                finish();
            }

            @Override
            public void onClickConfirm(View v, JiareqiMimaDialog dialog) {
                String textContent = dialog.getTextContent();
                if (textContent.equals("123456")) {
                    dialog.dismiss();
                } else {
                    Y.t("密码错误");
                }
            }
        });
        mimaDialog.show();
    }


    @OnClick({R.id.rl_back, R.id.rl_fengyoubicanshu, R.id.rl_jingxiaoshang, R.id.rl_houtaifuwu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_fengyoubicanshu:
                break;
            case R.id.rl_jingxiaoshang:
                ShuinuanJingxiaoshangActivity.actionStart(mContext);
                break;
            case R.id.rl_houtaifuwu:
                ShuinuanHoutaiActivity.actionStart(mContext);
                break;
        }
    }
}
