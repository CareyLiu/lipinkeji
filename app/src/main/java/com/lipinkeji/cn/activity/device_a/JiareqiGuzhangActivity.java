package com.lipinkeji.cn.activity.device_a;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.activity.device_fengnuan.LipinFengnuanActivity;
import com.lipinkeji.cn.app.App;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.app.ConstanceValue;
import com.lipinkeji.cn.app.Notice;
import com.lipinkeji.cn.app.UIHelper;
import com.lipinkeji.cn.callback.JsonCallback;
import com.lipinkeji.cn.config.AppResponse;
import com.lipinkeji.cn.config.MyApplication;
import com.lipinkeji.cn.config.PreferenceHelper;
import com.lipinkeji.cn.config.UserManager;
import com.lipinkeji.cn.dialog.LordingDialog;
import com.lipinkeji.cn.dialog.MyCarCaoZuoDialog_CaoZuoTIshi_Clear;
import com.lipinkeji.cn.get_net.Urls;
import com.lipinkeji.cn.model.AlarmClass;
import com.lipinkeji.cn.model.ChuLiGuZhangMa;
import com.lipinkeji.cn.model.HeaterDetails;
import com.lipinkeji.cn.model.ServiceModel;
import com.lipinkeji.cn.util.DoMqttValue;
import com.lipinkeji.cn.util.Y;
import com.lipinkeji.cn.view.CustomBaseDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imlib.model.Conversation;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.lipinkeji.cn.config.MyApplication.CARBOX_GETNOW;
import static com.lipinkeji.cn.config.MyApplication.CAR_CTROL;


/**
 * Created by Sl on 2019/6/25.
 * ????????????
 */

public class JiareqiGuzhangActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_message)
    TextView mTvMessage;
    @BindView(R.id.tv_factory)
    TextView mTvFactory;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_voltage)
    TextView mTvVoltage;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tv_addr)
    TextView mTvAddr;
    @BindView(R.id.tv_rate)
    TextView mTvRate;
    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.layout_message)
    LinearLayout layoutMessage;
    @BindView(R.id.layout_info)
    ConstraintLayout layoutInfo;
    @BindView(R.id.btn_clean)
    Button btnClean;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.iv_jiareqi)
    ImageView iv_jiareqi;

    private BaseAnimatorSet mBasIn;
    private BaseAnimatorSet mBasOut;
    private CustomBaseDialog dialog;
    private ArrayList<DialogMenuItem> mMenuItems = new ArrayList<>();
    private List<ServiceModel.DataBean> list = new ArrayList<>();
    AlarmClass alarmClass;

    String whatUWant = "";

    private LordingDialog lordingDialog;
    private String user_car_id;
    private String zhu_car_stoppage_no;
    private String jiareqizhuangtai;

    @Override
    public int getContentViewResId() {
        return R.layout.a_jiareqi_act_guzhang;
    }

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, JiareqiGuzhangActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, AlarmClass alarmClass) {
        Intent intent = new Intent(context, JiareqiGuzhangActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("alarmClass", alarmClass);
        context.startActivity(intent);
    }

    String guZhangMa = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        PreferenceHelper.getInstance(mContext).putString(App.CHOOSE_KONGZHI_XIANGMU, DoMqttValue.FENGNUAN);

        if (!AndMqtt.getInstance().isConneect()) {
            mTvTitle.setText("?????????????????????????????????");
            return;
        }


        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); //
        mIvIcon.setAnimation(animation);
        mBasIn = new BounceTopEnter();
        mBasOut = new SlideBottomExit();
        // requestData();
        requestData2();
        alarmClass = (AlarmClass) getIntent().getSerializableExtra("alarmClass");
        if (alarmClass != null) {
            Log.i("alarmClass", alarmClass.changjia_name + alarmClass.sell_phone);

            mTvTitle.setText("??????????????????");
            layoutInfo.setVisibility(View.VISIBLE);
            layoutMessage.setVisibility(View.VISIBLE);
            btnClean.setVisibility(View.VISIBLE);
            mTvMessage.setText(alarmClass.failure_name);
            mTvAddr.setText(alarmClass.install_addr);
            mTvDate.setText(alarmClass.install_time);
            mTvFactory.setText(alarmClass.changjia_name);
            mTvPhone.setText(alarmClass.sell_phone);
            mTvType.setText(alarmClass.xinghao);

            CARBOX_GETNOW = "wit/cbox/app/" + MyApplication.getServer_id() + alarmClass.ccid;
            CAR_CTROL = "wit/cbox/hardware/" + MyApplication.getServer_id() + alarmClass.ccid;
            AndMqtt.getInstance().subscribe(new MqttSubscribe()
                    .setTopic(CARBOX_GETNOW)
                    .setQos(2), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("Rair", "(MainActivity.java:63)-onSuccess:-&gt;????????????" + CARBOX_GETNOW);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("Rair", "(MainActivity.java:68)-onFailure:-&gt;????????????");
                }
            });
            getNData();

        } else {
            requestData();
            getNData();
        }

        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_GUZHANG_SHOUYE) {
                    Gson gson = new Gson();
                    try {
                        AlarmClass alarmClass = gson.fromJson(message.content.toString(), AlarmClass.class);
                        Log.i("alarmClass", alarmClass.changjia_name + alarmClass.sell_phone);
                        mTvTitle.setText("??????????????????");
                        layoutInfo.setVisibility(View.VISIBLE);
                        layoutMessage.setVisibility(View.VISIBLE);
                        btnClean.setVisibility(View.VISIBLE);
                        mTvMessage.setText(alarmClass.failure_name);
                        mTvAddr.setText(alarmClass.install_addr);
                        mTvDate.setText(alarmClass.install_time);
                        mTvFactory.setText(alarmClass.changjia_name);
                        mTvPhone.setText(alarmClass.sell_phone);
                        mTvType.setText(alarmClass.xinghao);

                        //????????????ccid
//                        CAR_CTROL = "wit/cbox/hardware/" + MyApplication.getServer_id() + alarmClass.ccid;
                    } catch (Exception e) {
                        System.out.println("????????????" + e.getMessage());
                    }
                } else if (message.type == ConstanceValue.MSG_CLEARGUZHANGSUCCESS) {


                } else if (message.type == ConstanceValue.MSG_CAR_J_M) {
                    //???????????????
                    Log.i("msg_car_j_m", message.content.toString());

                    String messageData = message.content.toString().substring(1, message.content.toString().length() - 1);
                    Log.i("msg_car_j_m_data", messageData);

//                    jiareqizhuangtai = messageData.substring(3, 4);
//                    if (jiareqizhuangtai.equals("1")) {
//                        Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji).into(iv_jiareqi);
//                    } else if (jiareqizhuangtai.equals("2")) {
//                        Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji).into(iv_jiareqi);
//                    } else if (jiareqizhuangtai.equals("3")) {
//                        Glide.with(mContext).load(R.mipmap.fegnnuan_guanji).into(iv_jiareqi);
//                    } else {
//                        Glide.with(mContext).load(R.mipmap.fegnnuan_guanji).into(iv_jiareqi);
//                    }


                    // ????????????????????????->01???18	2	 ???????????????
                    String zhu_car_stoppage_no = messageData.substring(35, 37);
                    zhu_car_stoppage_no = 0 <= zhu_car_stoppage_no.indexOf("a") ? "" : String.valueOf(Integer.parseInt(zhu_car_stoppage_no));
                    Y.e("???????????????????????????  " + zhu_car_stoppage_no);

                    if (!StringUtils.isEmpty(zhu_car_stoppage_no)) {
                        layoutMessage.setVisibility(View.VISIBLE);
                        btnClean.setVisibility(View.VISIBLE);
                        layoutInfo.setVisibility(View.VISIBLE);

                        String ccid = PreferenceHelper.getInstance(mContext).getString("ccid", "");
                        guZhangMa = zhu_car_stoppage_no;
                        guZhangMa = ChuLiGuZhangMa.getGuZhangMa(ccid, zhu_car_stoppage_no);
                        mTvMessage.setText(ChuLiGuZhangMa.codeXinXiShow(ccid, guZhangMa));
                    } else {
                        Activity activity = new Activity();
                        if (StringUtils.isEmpty(zhu_car_stoppage_no)) {
                            whatUWant = "";
                            layoutInfo.setVisibility(View.GONE);
                            layoutMessage.setVisibility(View.GONE);
                            btnClean.setVisibility(View.GONE);
                            mTvTitle.setText("??????????????????");
                        }
                    }
                }
            }
        }));

        init();
    }

    private void init() {
        if (TextUtils.isEmpty(LipinFengnuanActivity.messageData)) {
            Glide.with(mContext).load(R.mipmap.fegnnuan_guanji).into(iv_jiareqi);
        } else {
            jiareqizhuangtai = LipinFengnuanActivity.messageData.substring(3, 4);
            if (jiareqizhuangtai.equals("1")) {
                Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji).into(iv_jiareqi);
            } else if (jiareqizhuangtai.equals("2")) {
                Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji).into(iv_jiareqi);
            } else if (jiareqizhuangtai.equals("3")) {
                Glide.with(mContext).load(R.mipmap.fegnnuan_guanji).into(iv_jiareqi);
            } else {
                Glide.with(mContext).load(R.mipmap.fegnnuan_guanji).into(iv_jiareqi);
            }
        }
    }


    public void requestData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03225");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());

        Log.i("it_token", UserManager.getManager(this).getAppToken());
        map.put("user_car_id", PreferenceHelper.getInstance(this).getString("of_user_id", ""));
        map.put("ccid", PreferenceHelper.getInstance(this).getString("ccid", ""));
        map.put("type", "1");
        map.put("type_msg", "2");
        Gson gson = new Gson();
        OkGo.<AppResponse<HeaterDetails.DataBean>>post(Urls.SERVER_URL + "wit/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<HeaterDetails.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<HeaterDetails.DataBean>> response) {
                        if (response.body().data.get(0).getZhu_car_stoppage_no() == null) {
                            response.body().data.get(0).setZhu_car_stoppage_no("");
                        }
                        if (response.body().data.get(0).getZhu_car_stoppage_no().equals("")) {
                            //UIHelper.ToastMessage(DiagnosisActivity.this, "??????????????????", Toast.LENGTH_LONG);
                            mTvTitle.setText("??????????????????");

                        } else {

                            //????????????ccid
                            // CAR_CTROL = "wit/cbox/hardware/" + MyApplication.getServer_id() + response.body().data.get(0).getCcid();
                            CARBOX_GETNOW = "wit/cbox/app/" + MyApplication.getServer_id() + response.body().data.get(0).getCcid();

                            AndMqtt.getInstance().subscribe(new MqttSubscribe()
                                    .setTopic(CARBOX_GETNOW)
                                    .setQos(2), new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    Log.i("Rair", "(MainActivity.java:63)-onSuccess:-&gt;????????????" + CARBOX_GETNOW);
                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                    Log.i("Rair", "(MainActivity.java:68)-onFailure:-&gt;????????????");
                                }
                            });

                            mTvTitle.setText("??????????????????");
                            layoutInfo.setVisibility(View.VISIBLE);
                            layoutMessage.setVisibility(View.VISIBLE);
                            btnClean.setVisibility(View.VISIBLE);
                            mTvMessage.setText(response.body().data.get(0).getFailure_name());
                            mTvAddr.setText(response.body().data.get(0).getInstall_addr());
                            mTvDate.setText(response.body().data.get(0).getInstall_time());
                            mTvFactory.setText(response.body().data.get(0).getChangjia_name());
                            mTvPhone.setText(response.body().data.get(0).getSell_phone());
                            mTvType.setText(response.body().data.get(0).getXinghao());
                            mTvVoltage.setText(response.body().data.get(0).getMachine_voltage());
                            mTvRate.setText(response.body().data.get(0).getFrequency());
                        }

                        user_car_id = response.body().data.get(0).getUser_car_id();
                        zhu_car_stoppage_no = response.body().data.get(0).getZhu_car_stoppage_no();
                    }

                    @Override
                    public void onError(Response<AppResponse<HeaterDetails.DataBean>> response) {
                        UIHelper.ToastMessage(JiareqiGuzhangActivity.this, response.getException().getMessage());
                    }
                });
    }

    public void requestData2() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03311");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("user_car_id", PreferenceHelper.getInstance(this).getString("of_user_id", ""));
        map.put("ccid", PreferenceHelper.getInstance(this).getString("ccid", ""));
        map.put("type", "1");
        map.put("type_msg", "2");
        Gson gson = new Gson();
        OkGo.<AppResponse<ServiceModel.DataBean>>post(Urls.SERVER_URL + "wit/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ServiceModel.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<ServiceModel.DataBean>> response) {
                        list = response.body().data;
                        for (int i = 0; i < response.body().data.size(); i++) {
                            mMenuItems.add(new DialogMenuItem(response.body().data.get(i).getSub_user_name(), R.drawable.zixun_on));
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<ServiceModel.DataBean>> response) {
                        UIHelper.ToastMessage(JiareqiGuzhangActivity.this, response.getException().getMessage());
                    }
                });
    }

    @OnClick({R.id.rl_back, R.id.btn_clean, R.id.rl_consult})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_consult:
                final NormalListDialog dialog = new NormalListDialog(this, mMenuItems);
                dialog.title("?????????")//
                        .showAnim(mBasIn)//
                        .dismissAnim(mBasOut)//
                        .show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //??????title?????????????????????????????????????????????
                        ServiceModel.DataBean dataBean = list.get(position);
                        Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
                        String sub_accid = dataBean.getSub_accid();
                        String sub_user_name = dataBean.getSub_user_name();
                        String inst_id = dataBean.getInst_id();
                        String sub_user_id = dataBean.getSub_user_id();
                        Bundle bundle = new Bundle();
                        bundle.putString("sub_user_name", sub_user_name);
                        bundle.putString("sub_accid", sub_accid);
                        bundle.putString("sub_user_id", sub_user_id);
                        bundle.putString("inst_id", inst_id);
                        bundle.putString("user_car_id", user_car_id);
                        bundle.putString("zhu_car_stoppage_no", zhu_car_stoppage_no);
//                        RongIM.getInstance().startConversation(mContext, conversationType, targetId, instName, bundle);
                        startConversation(mContext, conversationType, sub_accid, sub_user_name, bundle);
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.btn_clean:
                MyCarCaoZuoDialog_CaoZuoTIshi_Clear clear = new MyCarCaoZuoDialog_CaoZuoTIshi_Clear(JiareqiGuzhangActivity.this, new MyCarCaoZuoDialog_CaoZuoTIshi_Clear.OnDialogItemClickListener() {
                    @Override
                    public void clickLeft() {

                    }

                    @Override
                    public void clickRight() {

//                        lordingDialog = new LordingDialog(mContext);
//                        lordingDialog.setTextMsg("????????????????????????");
//                        lordingDialog.show();


                        AndMqtt.getInstance().publish(new MqttPublish()
                                .setMsg("M691.").setRetained(false)
                                .setQos(2)
                                .setTopic(CAR_CTROL), new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i("Rair", "(???????????? --- ????????????");
                                //      UIHelper.ToastMessage(DiagnosisActivity.this, "???????????????????????????", Toast.LENGTH_SHORT);
                                // dialog.dismiss();
                                UIHelper.ToastMessage(JiareqiGuzhangActivity.this, "???????????????", Toast.LENGTH_SHORT);
                                //  mTvTitle.setText("??????????????????");
//                                layoutInfo.setVisibility(View.VISIBLE);
//                                layoutMessage.setVisibility(View.VISIBLE);
//                                btnClean.setVisibility(View.VISIBLE);
                                //whatUWant = "qingchuguzhang";
                                //finish();
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
                            }
                        });
                    }
                });
                clear.show();
        }
    }


    public void startConversation(Context context, Conversation.ConversationType conversationType, String targetId, String title, Bundle bundle) {
        if (context != null && !TextUtils.isEmpty(targetId) && conversationType != null) {
            Uri uri = Uri.parse("rong://" + context.getApplicationInfo().processName).buildUpon().appendPath("conversationGuzhang").appendPath(conversationType.getName().toLowerCase(Locale.US)).appendQueryParameter("targetId", targetId).appendQueryParameter("title", title).build();
            Intent intent = new Intent("android.intent.action.VIEW", uri);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.with(this).statusBarColor(R.color.black).init();
    }

    private void getNData() {
        //??????????????????????????????????????????
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("N.")
                .setQos(2)
                .setTopic(CAR_CTROL)
                .setRetained(false), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }
}