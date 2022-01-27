package com.lipinkeji.cn.activity.device_fengnuan.set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_a.DaqiyaShoumingActivty;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.UIHelper;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.AtmosBean;
import com.lipinkeji.cn.util.Y;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FengnuanDaqiyaActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.iv_daqiya_shuoming)
    LinearLayout ivDaqiyaShuoming;
    @BindView(R.id.seekBar_daqiya)
    SeekBar seekBarDaqiya;
    @BindView(R.id.tv_daqiya)
    TextView tvDaqiya;
    @BindView(R.id.bt_save)
    TextView btSave;

    private String user_car_id;
    private String daqiyacanshu;

    @Override
    public int getContentViewResId() {
        return R.layout.a_fengnuan_act_set_daqiya;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, FengnuanDaqiyaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        user_car_id = PreferenceHelper.getInstance(this).getString("user_car_id", "");
        requestGetData();

        seekBarDaqiya.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                daqiyacanshu = progress + "";
                tvDaqiya.setText(daqiyacanshu + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @OnClick({R.id.rl_back, R.id.iv_daqiya_shuoming, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.bt_save:
                requestData();
                break;
            case R.id.iv_daqiya_shuoming:
                DaqiyaShoumingActivty.actionStart(mContext);
                break;
        }
    }

    public void requestGetData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03111");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("user_car_id", user_car_id);
        Gson gson = new Gson();
        OkGo.<AppResponse<AtmosBean.DataBean>>post(Urls.SERVER_URL + "wit/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<AtmosBean.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<AtmosBean.DataBean>> response) {
                        AtmosBean.DataBean atmosBean = response.body().data.get(0);
                        System.out.println("大气压" + atmosBean.getZhu_apc());
                        if (atmosBean.getZhu_apc() != null) {
                            if (atmosBean.getZhu_apc().equals("aaa")) {
                                daqiyacanshu = "100";
                            } else if (atmosBean.getZhu_apc().indexOf('0') == 0) {
                                daqiyacanshu = atmosBean.getZhu_apc().substring(1);
                            } else {
                                daqiyacanshu = atmosBean.getZhu_apc();
                            }
                            seekBarDaqiya.setProgress(Y.getInt(daqiyacanshu));
                            tvDaqiya.setText(daqiyacanshu + "");
                        }

                    }

                    @Override
                    public void onError(Response<AppResponse<AtmosBean.DataBean>> response) {
                        Y.tError(response);
                    }
                });

    }

    public void requestData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03112");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("user_car_id", user_car_id);
        map.put("zhu_apc", "0" + daqiyacanshu);
        Gson gson = new Gson();
        OkGo.<AppResponse>post(Urls.SERVER_URL + "wit/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse>() {
                    @Override
                    public void onSuccess(final Response<AppResponse> response) {
                        if (response.body().msg_code.equals("0000")) {
                            Y.t("大气压参数输入成功");
                        } else if (response.body().msg_code.equals("0001")) {
                            UIHelper.ToastMessage(mContext, response.body().msg, Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse> response) {
                        Y.tError(response);
                    }
                });
    }
}
