package com.falaer.cn.activity.device_falaer.gongxiang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.falaer.cn.R;
import com.falaer.cn.activity.shuinuan.ShuinuanBaseNewActivity;
import com.falaer.cn.activity.shuinuan.Y;
import com.falaer.cn.activity.shuinuan.gongxiang.GongxiangModel;
import com.falaer.cn.app.ConstanceValue;
import com.falaer.cn.app.Notice;
import com.falaer.cn.app.RxBus;
import com.falaer.cn.callback.JsonCallback;
import com.falaer.cn.config.AppResponse;
import com.falaer.cn.config.UserManager;
import com.falaer.cn.dialog.newdia.TishiDialog;
import com.falaer.cn.get_net.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GongxiangFalaerDelectActivity extends ShuinuanBaseNewActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_delect)
    TextView tv_delect;
    private String ccid;
    private String name;
    private String phone;
    private String of_user_id_beShared;

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_gongxiangfalaer_delete;
    }


    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context, String ccid, String name, String phone, String of_user_id_beShared) {
        Intent intent = new Intent(context, GongxiangFalaerDelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("ccid", ccid);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        intent.putExtra("of_user_id_beShared", of_user_id_beShared);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        ccid = getIntent().getStringExtra("ccid");
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        of_user_id_beShared = getIntent().getStringExtra("of_user_id_beShared");

        tv_name.setText(name);
        tv_phone.setText(phone);
    }

    @OnClick({R.id.rl_back, R.id.tv_delect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_delect:
                showTishi();
                break;
        }
    }

    private void showTishi() {
        TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_DELETE, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {
                delete();
            }

            @Override
            public void onDismiss(TishiDialog dialog) {

            }
        });
        dialog.setTextContent("是否删除该共享成员");
        dialog.show();
    }

    private void delete() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03513");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("ccid", ccid);
        map.put("of_user_id_beShared", of_user_id_beShared);
        Gson gson = new Gson();
        OkGo.<AppResponse<GongxiangModel.DataBean>>post(Urls.SERVER_URL + "wit/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<GongxiangModel.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<GongxiangModel.DataBean>> response) {
                        Y.t("删除成功");
                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_GONGXIANG_PEOPLE;
                        RxBus.getDefault().sendRx(n);
                        finish();
                    }

                    @Override
                    public void onError(Response<AppResponse<GongxiangModel.DataBean>> response) {
                        Y.tError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }

                    @Override
                    public void onStart(Request<AppResponse<GongxiangModel.DataBean>, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog();
                    }
                });
    }
}
