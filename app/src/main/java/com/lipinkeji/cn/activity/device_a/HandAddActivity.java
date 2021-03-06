package com.lipinkeji.cn.activity.device_a;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.app.UIHelper;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.common.StringUtils;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.dialog.BangdingFailDialog;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.CarBrand;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sl on 2019/6/18.
 */

public class HandAddActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.et_number)
    EditText mEtNumber;
    @BindView(R.id.bt_submit)
    Button mBtSubmit;


    @Override
    public void onCreate(Bundle savedInstanceStateundle) {
        super.onCreate(savedInstanceStateundle);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("手动添加设备");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationIcon(R.mipmap.back_white);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.app_bg));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notice notice = new Notice();
                notice.type = ConstanceValue.MSG_ADD_CHELIANG_SUCCESS;
                sendRx(notice);
                finish();
            }
        });
    }


    @Override
    public int getContentViewResId() {
        return R.layout.activity_hand_add;
    }

    private void initView() {

    }

    @OnClick({R.id.bt_submit})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_submit:
                if (!StringUtils.isEmpty(mEtNumber.getText().toString().trim())) {
                    addSheBei(mEtNumber.getText().toString());
                } else {
                    UIHelper.ToastMessage(mContext, "请输入设备码后重新尝试");
                }
                break;
        }
    }


    public void addSheBei(String ccid) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03532");
        map.put("key", Urls.key);
        String jingdu = PreferenceHelper.getInstance(mContext).getString("JINGDU", "");
        String weidu = PreferenceHelper.getInstance(mContext).getString("WEIDU", "");
        map.put("gps_x", weidu);
        map.put("gps_y", jingdu);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("ccid", ccid);
        Gson gson = new Gson();
        OkGo.<AppResponse<CarBrand.DataBean>>post(Urls.SERVER_URL + "wit/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<CarBrand.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<CarBrand.DataBean>> response) {
                        mEtNumber.setText("");
                        BangdingFailDialog dialog = new BangdingFailDialog(mContext);
                        dialog.setClick(new BangdingFailDialog.BangdingClick() {
                            @Override
                            public void close() {
                                Notice notice = new Notice();
                                notice.type = ConstanceValue.MSG_ADD_CHELIANG_SUCCESS;
                                sendRx(notice);
                                finish();
                            }

                            @Override
                            public void jixu() {

                            }
                        });
                        dialog.bt_jixusao.setText("继续添加");
                        dialog.setTextContent("设备添加成功");
                        dialog.show();
                    }

                    @Override
                    public void onError(Response<AppResponse<CarBrand.DataBean>> response) {
                        String msg = response.getException().getMessage();
                        BangdingFailDialog dialog = new BangdingFailDialog(mContext);
                        dialog.setClick(new BangdingFailDialog.BangdingClick() {
                            @Override
                            public void close() {
                                Notice notice = new Notice();
                                notice.type = ConstanceValue.MSG_ADD_CHELIANG_SUCCESS;
                                sendRx(notice);
                                finish();
                            }

                            @Override
                            public void jixu() {

                            }
                        });
                        dialog.bt_jixusao.setText("继续添加");
                        dialog.setTextContent(msg);
                        dialog.show();
                    }
                });
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.app_bg).init();
    }
}
