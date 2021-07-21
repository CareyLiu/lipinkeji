package com.falaer.cn.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.falaer.cn.R;
import com.falaer.cn.callback.JsonCallback;
import com.falaer.cn.common.StringUtils;
import com.falaer.cn.common.UIHelper;
import com.falaer.cn.config.AppEvent;
import com.falaer.cn.config.AppResponse;
import com.falaer.cn.config.UserManager;
import com.falaer.cn.get_net.Urls;
import com.falaer.cn.util.AlertUtil;
import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.widget.BottomDialog;
import com.smarttop.library.widget.OnAddressSelectedListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAddressActivity extends BaseActivity implements OnAddressSelectedListener {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_region)
    TextView tvRegion;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.sw_default)
    Switch swDefault;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_1)
    RelativeLayout rl1;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    //    private SQLiteDatabase db;
    private BottomDialog dialog;
    private String province_id, province_name, city_id, city_name, area_id, area_name, request_code, address_id;
    private NormalDialog normalDialog;

    private String user_addr_state = "1";//1正常 2删除

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        tvTitle.setText(getIntent().getStringExtra("title"));
        request_code = getIntent().getStringExtra("code");
        province_id = getIntent().getStringExtra("province_id");
        province_name = getIntent().getStringExtra("province_name");
        city_id = getIntent().getStringExtra("city_id");
        city_name = getIntent().getStringExtra("city_name");
        area_id = getIntent().getStringExtra("area_id");
        area_name = getIntent().getStringExtra("area_name");
        etName.setText(getIntent().getStringExtra("user_name"));
        etPhone.setText(getIntent().getStringExtra("user_phone"));
        etAddress.setText(getIntent().getStringExtra("address"));

        if (tvTitle.getText().toString().equals("添加收货地址")) {
            tvDelete.setVisibility(View.GONE);

        } else if (tvTitle.getText().toString().equals("编辑收货地址")) {
            tvDelete.setVisibility(View.VISIBLE);
        }

        if (province_name != null && !getIntent().getStringExtra("isDefault").equals("")) {
            tvRegion.setText(String.format("%s-%s-%s", province_name, city_name, area_name));
            swDefault.setChecked(getIntent().getStringExtra("isDefault").equals("1"));

        }


        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_addr_state = "2";
                requestData("04130");
            }
        });

//        requestData();

    }

    @OnClick({R.id.rl_back, R.id.tv_save, R.id.tv_region})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
//                if (normalDialog == null){
//                    normalDialog = new NormalDialog(this);
//                    normalDialog.content("是否保存本次编辑结果?").showAnim(new BounceBottomEnter()).dismissAnim(new SlideBottomExit());
//                }else {
//                    normalDialog.show();
//                }
//                normalDialog.setOnBtnClickL(
//                        new OnBtnClickL() {
//                            @Override
//                            public void onBtnClick() {
//                                normalDialog.dismiss();
//                                AddAddressActivity.this.finish();
//                            }
//                        },
//                        new OnBtnClickL() {
//                            @Override
//                            public void onBtnClick() {
//                                requestData(request_code);
//                                normalDialog.dismiss();
//                            }
//                        });
                finish();
                break;
            case R.id.tv_save:
                if (StringUtils.isEmpty(area_id)) {
                    UIHelper.ToastMessage(AddAddressActivity.this, "请点击此处添加所在区域");
                    return;
                }
                requestData(request_code);
                break;
            case R.id.tv_region:
                dialog = new BottomDialog(this);
                dialog.setOnAddressSelectedListener(this);
                dialog.show();
                break;
        }
    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        dialog.dismiss();
        tvRegion.setText(String.format("%s-%s-%s", province.name, city.name, county.name));
        province_id = province.code;
        province_name = province.name;
        city_id = city.code;
        city_name = city.name;
        area_id = county.code;
        area_name = county.name;
    }


    public void requestData(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(AddAddressActivity.this).getAppToken());
        if (code.equals("04130"))
            map.put("users_addr_id", getIntent().getStringExtra("address_id"));
        map.put("user_name", etName.getText().toString());
        map.put("user_phone", etPhone.getText().toString());
        map.put("province_id", province_id);
        map.put("province_name", province_name);
        map.put("city_id", city_id);
        map.put("city_name", city_name);
        map.put("area_id", area_id);
        map.put("area_name", area_name);
        map.put("addr", etAddress.getText().toString());
        map.put("addr_check", swDefault.isChecked() ? "1" : "2");
        //user_addr_state  状态:1.正常2.锁定(删除该地址传2)
        map.put("user_addr_state", user_addr_state);

        Gson gson = new Gson();
        OkGo.<AppResponse>post(Urls.SERVER_URL + "/shop_new/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse>() {
                    @Override
                    public void onSuccess(final Response<AppResponse> response) {
                        AppEvent.setMessage("address_update");
                        AlertUtil.t(AddAddressActivity.this, response.body().msg);
                        finish();
                    }

                    @Override
                    public void onError(Response<AppResponse> response) {
                        AlertUtil.t(AddAddressActivity.this, response.getException().getMessage());
                    }
                });
    }
}
