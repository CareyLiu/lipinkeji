package com.lipinkeji.cn.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_a.JiareqiGuzhangActivity;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.model.HostModel;
import com.lipinkeji.cn.model.SerializableMap;
import com.lipinkeji.cn.service.HeaterMqttService;
import com.lipinkeji.cn.util.ConstantUtil;
import com.lipinkeji.cn.util.DialogManager;
import com.lipinkeji.cn.view.CircleMenuView;

import butterknife.BindView;
import butterknife.ButterKnife;



public class ControCarActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_windmill)
    ImageView ivWindmill;
    @BindView(R.id.sb_speed)
    SeekBar sbSpeed;
    @BindView(R.id.tv_wd)
    TextView tvWd;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_car)
    ImageView ivCar;
    @BindView(R.id.iv_left_head_door)
    ImageView ivLeftHeadDoor;
    @BindView(R.id.iv_right_head_door)
    ImageView ivRightHeadDoor;
    @BindView(R.id.iv_left_rear_door)
    ImageView ivLeftRearDoor;
    @BindView(R.id.iv_right_rear_door)
    ImageView ivRightRearDoor;
    @BindView(R.id.iv_trunk_covers)
    ImageView ivTrunkCovers;
    @BindView(R.id.iv_head_light)
    ImageView ivHeadLight;
    @BindView(R.id.iv_back_light)
    ImageView ivBackLight;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.circle_menu)
    CircleMenuView circleMenu;
    @BindView(R.id.iv_tyre)
    ImageView ivTyre;
    @BindView(R.id.iv_fuel)
    ImageView ivFuel;
    @BindView(R.id.tv_fuel)
    TextView tvFuel;
    @BindView(R.id.iv_coolant)
    ImageView ivCoolant;
    @BindView(R.id.tv_coolant)
    TextView tvCoolant;
    @BindView(R.id.iv_glass_water)
    ImageView ivGlassWater;
    @BindView(R.id.tv_glass_water)
    TextView tvGlassWater;
    @BindView(R.id.iv_speed)
    ImageView ivSpeed;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    private Boolean isStart = false;
    private Animation rotate_wind, rotate_tyre,alpha_light;
    public static Handler stateHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_control);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        @SuppressLint("ResourceType") ColorStateList csl = getResources().getColorStateList(R.drawable.menu_item_color);
        navigationView.setItemTextColor(csl);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        toolbarTitle.setText(PreferenceHelper.getInstance(this).getString("name",""));
        rotate_wind = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        rotate_tyre = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        alpha_light = AnimationUtils.loadAnimation(this,R.anim.alpha_anim);


        initHandler();
        HeaterMqttService.handler = stateHandler;
        HeaterMqttService.subscribe();
        //??????????????????
        HeaterMqttService.mqttService.publish("N9.", HeaterMqttService.TOPIC_SERVER_ORDER, 2, false);
        DialogManager.getManager((Activity) ControCarActivity.this).showMessage("????????????????????????");



        sbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTime.setText("???????????????"+(progress+5)+"(min)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        circleMenu.setOnMenuItemClickListener(new CircleMenuView.OnMenuItemClickListener() {
            @Override
            public void itemClick(int pos) {
                switch (pos) {
                    case 0://???????????????
                        break;
                    case 1://???????????????
                        break;
                    case 2://????????????
                        break;
                    case 3://????????????
                        break;
                    case 4://??????
                        break;
                    case 5://??????
                        break;
                    case 6://??????
                        break;
                    case 7://???????????????
                        break;
                }
            }

            @Override
            public void itemCenterClick() {
                if (circleMenu.isStart()){
                    ivWindmill.startAnimation(rotate_wind);
                    ivTyre.startAnimation(rotate_tyre);
                    ivHeadLight.setVisibility(View.VISIBLE);
                    ivBackLight.setVisibility(View.VISIBLE);
                    ivBackLight.startAnimation(alpha_light);
                }else {
                    ivWindmill.clearAnimation();
                    ivTyre.clearAnimation();
                    ivHeadLight.setVisibility(View.GONE);
                    ivBackLight.setVisibility(View.GONE);
                    ivBackLight.clearAnimation();
                }


            }
        });


    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        stateHandler = new Handler() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void handleMessage(Message msg) {

                Bundle b = msg.getData();
                switch (msg.what) {
                    case ConstantUtil.MSG_HEATER_ACTUAL_DATA:
                        //??????????????????
                        DialogManager.getManager((Activity) ControCarActivity.this).dismiss();
                        SerializableMap serializableMap = (SerializableMap) b.get("map");
                        assert serializableMap != null;
                        Log.d("version", serializableMap.getMap().get("version_feng"));
                        HostModel.version = serializableMap.getMap().get("version_feng");
                        tvSpeed.setText(serializableMap.getMap().get("mph")+"km/h");
                        tvCoolant.setText(serializableMap.getMap().get("coolant"));
                        tvFuel.setText(serializableMap.getMap().get("benzin"));

                        break;


                }
                super.handleMessage(msg);
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.heater_menu_option, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings:
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_bluetooth:
                //??????????????????
                showToast("????????????");
                break;
            case R.id.nav_trajectory:
                //????????????
                startActivity(new Intent(this, HistoryLocusActivity.class));
                break;
            case R.id.nav_location:
                //??????
//                startActivity(new Intent(this, LocationActivity.class));
                break;
            case R.id.nav_timing:
                //??????
                startActivity(new Intent(this, AppointmentActivity.class));
                break;
            case R.id.nav_record:
                //????????????
                startActivity(new Intent(this, RepairOrderAcitivity.class));
                break;
            case R.id.nav_state:
                //???????????????
                startActivity(new Intent(this, DeviceStateActivity.class));
                break;
            case R.id.nav_report:
                //????????????
                startActivity(new Intent(this, JiareqiGuzhangActivity.class));
                break;
            case R.id.nav_corral:
                //????????????
                startActivity(new Intent(this, WebViewActivity.class).
                        putExtra("url", PreferenceHelper.getInstance(this).getString("fence_url", "")).putExtra("title", "????????????"));
                break;
            case R.id.nav_setting:
                startActivity(new Intent(this, HeaterSettingActivity.class));
                //??????
                break;
        }
        return false;
    }
}
