package com.falaer.cn.activity.zijian_shangcheng;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.falaer.cn.R;
import com.falaer.cn.app.App;
import com.falaer.cn.app.AppConfig;
import com.falaer.cn.app.BaseActivity;
import com.falaer.cn.app.ConstanceValue;
import com.falaer.cn.app.Notice;
import com.falaer.cn.app.RxBus;
import com.falaer.cn.app.UIHelper;
import com.falaer.cn.callback.JsonCallback;
import com.falaer.cn.config.AppResponse;
import com.falaer.cn.config.PreferenceHelper;
import com.falaer.cn.config.UserManager;
import com.falaer.cn.dialog.newdia.TishiDialog;
import com.falaer.cn.get_net.Urls;
import com.falaer.cn.model.FenLeiContentModel;
import com.falaer.cn.model.FenLeiThirdModel;
import com.falaer.cn.project_A.zijian_interface.FenLeiListInterface;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.falaer.cn.get_net.Urls.HOME_PICTURE_HOME;
import static com.falaer.cn.get_net.Urls.MESSAGE_URL;
import static com.falaer.cn.get_net.Urls.ZIYINGFENLEI;


public class FenLeiThirdActivity extends BaseActivity implements FenLeiListInterface {
    @BindView(R.id.rl_1)
    RelativeLayout rl1;
    @BindView(R.id.rl_2)
    RelativeLayout rl2;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    String item_id;
    String item_name;//名
    String oneitem;
    String twoitem;
    //  String threeitem;
    @BindView(R.id.tv_zonghe)
    TextView tvZonghe;
    @BindView(R.id.tv_xiaoliang)
    TextView tvXiaoliang;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;

    @BindView(R.id.ll_zonghe_xiaoliang)
    LinearLayout llZongheXiaoliang;
    @BindView(R.id.tv_zhekou)
    TextView tvZhekou;
    @BindView(R.id.view_1)
    View view1;
    @BindView(R.id.tv_jiagesheng)
    TextView tvJiagesheng;
    @BindView(R.id.view_2)
    View view2;
    @BindView(R.id.tv_jiagejiang)
    TextView tvJiagejiang;
    @BindView(R.id.view_4)
    View view4;
    @BindView(R.id.tv_xiaoliagnjiang)
    TextView tvXiaoliagnjiang;
    @BindView(R.id.constrain)
    ConstraintLayout constrain;


    ImageView ivZhiDiSheZhi;


    @Override
    public int getContentViewResId() {
        return R.layout.layout_fenlei_third;
    }

//    @Override
    //  public boolean showToolBar() {
//        return true;
    //}

    //@Override
    //protected void initToolbar() {
    //  super.initToolbar();
    // tv_title.setText(item_name);
//        tv_title.setTextSize(17);
//        tv_title.setTextColor(getResources().getColor(R.color.black));
//        mToolbar.setNavigationIcon(R.mipmap.backbutton);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imm.hideSoftInputFromWindow(findViewById(R.id.cl_layout).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                finish();
//            }
//        });
//    }
    FenLeiContentModel fenLeiContentModel;
    /**
     * intent.putExtra("strTitle", strTitle);
     * intent.putExtra("shouYeId", shouYeId);
     */
    private String strTitle;
    private String shouYeId;
    private TishiDialog tishiDialog;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ivZhiDiSheZhi = findViewById(R.id.iv_zhidishezhi);

        ivZhiDiSheZhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
                    @Override
                    public void onClickCancel(View v, TishiDialog dialog) {
                        setNet("2");
                    }

                    @Override
                    public void onClickConfirm(View v, TishiDialog dialog) {
                        setNet("1");
                    }

                    @Override
                    public void onDismiss(TishiDialog dialog) {

                    }
                });
                tishiDialog.setTextConfirm("开启");
                tishiDialog.setTextCancel("关闭");
                tishiDialog.setTextContent("请选择商城展示方式");
                tishiDialog.show();
            }
        });

        fenLeiContentModel = (FenLeiContentModel) getIntent().getSerializableExtra("fenleiContent");
        strTitle = getIntent().getStringExtra("strTitle");
        shouYeId = getIntent().getStringExtra("shouYeId");

        if (fenLeiContentModel != null) {
            item_name = fenLeiContentModel.getItem_name();
            oneitem = fenLeiContentModel.one_item;
            twoitem = fenLeiContentModel.two_item;
            item_id = fenLeiContentModel.getItem_id();
            tvTitle.setText(item_name);
        } else {
            tvTitle.setText(strTitle);
        }

        tvZonghe.setTextColor(getResources().getColor(R.color.red_61832));
        tvXiaoliang.setTextColor(getResources().getColor(R.color.black_111111));

        FenLeiThirdFragment fenLeiThirdFragment = new FenLeiThirdFragment();
        //传递数据到Fragment
        Bundle mBundle = new Bundle();
        //  mBundle.putSerializable("info", (Serializable) itemBeanXES);
        mBundle.putSerializable("page", "1");
        mBundle.putString("item_id", item_id);
        mBundle.putString("one_item", oneitem);
        mBundle.putString("two_item", twoitem);

        mBundle.putString("strTitle", strTitle);
        mBundle.putString("shouYeId", shouYeId);

        fenLeiThirdFragment.setArguments(mBundle);
        replaceFragment(fenLeiThirdFragment);
//        initToolbar();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changePage();
        //order_type	排序类型：1.折扣2.价格升序
        //3.价格降序4.销量降序		否
        tvJiagejiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notice n = new Notice();
                n.type = ConstanceValue.MSG_SETFZONGHE;
                n.content = "3";
                //  n.content = message.toString();
                RxBus.getDefault().sendRx(n);
                constrain.setVisibility(View.GONE);
            }
        });
        tvJiagesheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notice n = new Notice();
                n.type = ConstanceValue.MSG_SETFZONGHE;
                n.content = "2";
                //  n.content = message.toString();
                RxBus.getDefault().sendRx(n);
                constrain.setVisibility(View.GONE);
            }
        });
        tvXiaoliagnjiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notice n = new Notice();
                n.type = ConstanceValue.MSG_SETFZONGHE;
                n.content = "4";
                //  n.content = message.toString();
                RxBus.getDefault().sendRx(n);
                constrain.setVisibility(View.GONE);
            }
        });
        tvZhekou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notice n = new Notice();
                n.type = ConstanceValue.MSG_SETFZONGHE;
                n.content = "1";
                //  n.content = message.toString();
                RxBus.getDefault().sendRx(n);
                constrain.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void getNet() {


    }

    //1显示 2隐藏
    private void setNet(String str) {
        //访问网络获取数据 下面的列表数据
        Map<String, String> map = new HashMap<>();
        map.put("code", "04269");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("csdh_state", str);
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<FenLeiThirdModel.DataBean>>post(HOME_PICTURE_HOME)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<FenLeiThirdModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<FenLeiThirdModel.DataBean>> response) {
                        if (str.equals("1")) {
                            UIHelper.ToastMessage(mContext, "知迪商城已开启");
                            PreferenceHelper.getInstance(mContext).putString(App.ZHIDISHANGCHEGN, "1");

                            Notice n = new Notice();
                            n.type = ConstanceValue.MSG_HAVE_HOMEFRAGMETN;
                            //  n.content = message.toString();
                            RxBus.getDefault().sendRx(n);
                        } else if (str.equals("2")) {
                            UIHelper.ToastMessage(mContext, "知迪商城已关闭");
                            PreferenceHelper.getInstance(mContext).putString(App.ZHIDISHANGCHEGN, "2");

                            Notice n = new Notice();
                            n.type = ConstanceValue.MSG_NONE_HOMEFRAGMENT;
                            //  n.content = message.toString();
                            RxBus.getDefault().sendRx(n);
                        }
                    }
                });
    }

    @Override
    public void initList() {

    }

    String str = "0";
    String tanchuCon = "0";//0否 1 是

    @Override
    public void changePage() {

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (str.equals("0")) {
                    //弹出con
                    if (tanchuCon.equals("0")) {
                        constrain.setVisibility(View.VISIBLE);
                        tanchuCon = "1";
                        tvZonghe.setTextColor(getResources().getColor(R.color.red_61832));
                        tvXiaoliang.setTextColor(getResources().getColor(R.color.black_111111));
                    } else if (tanchuCon.equals("1")) {
                        constrain.setVisibility(View.GONE);
                        tanchuCon = "0";
                    }
                    return;
                }


                FenLeiThirdFragment fenLeiThirdFragment = new FenLeiThirdFragment();
                //传递数据到Fragment
                Bundle mBundle = new Bundle();
                //  mBundle.putSerializable("info", (Serializable) itemBeanXES);
                mBundle.putSerializable("page", "1");
                mBundle.putString("item_id", item_id);
                mBundle.putString("one_item", oneitem);
                mBundle.putString("two_item", twoitem);
                fenLeiThirdFragment.setArguments(mBundle);
                replaceFragment(fenLeiThirdFragment);
                str = "1";
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str.equals("1")) {
                    return;
                }
                constrain.setVisibility(View.GONE);
                tanchuCon = "0";
                tvZonghe.setTextColor(getResources().getColor(R.color.black_111111));
                tvXiaoliang.setTextColor(getResources().getColor(R.color.red_61832));

                FenLeiThirdFragment fenLeiThirdFragment = new FenLeiThirdFragment();
                //传递数据到Fragment
                Bundle mBundle = new Bundle();
                //  mBundle.putSerializable("info", (Serializable) itemBeanXES);
                mBundle.putSerializable("page", "2");
                mBundle.putString("item_id", item_id);
                mBundle.putString("one_item", oneitem);
                mBundle.putString("two_item", twoitem);
                fenLeiThirdFragment.setArguments(mBundle);
                replaceFragment(fenLeiThirdFragment);
                str = "0";
            }
        });

    }


    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    //Fragment启动方法：

    private void replaceFragment(Fragment fragment) {
// 1.获取FragmentManager，在活动中可以直接通过调用getFragmentManager()方法得到
        fragmentManager = getSupportFragmentManager();
// 2.开启一个事务，通过调用beginTransaction()方法开启
        transaction = fragmentManager.beginTransaction();
// 3.向容器内添加或替换碎片，一般使用replace()方法实现，需要传入容器的id和待添加的碎片实例
        transaction.replace(R.id.fl_container, fragment);  //fr_container不能为fragment布局，可使用线性布局相对布局等。
// 4.使用addToBackStack()方法，将事务添加到返回栈中，填入的是用于描述返回栈的一个名字
        transaction.addToBackStack(null);
// 5.提交事物,调用commit()方法来完成
        transaction.commit();
    }

    public static void actionStart(Context context, FenLeiContentModel fenLeiContentModel) {
        Intent intent = new Intent(context, FenLeiThirdActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("fenleiContent", fenLeiContentModel);
        context.startActivity(intent);

    }

    public static void actionStart(Context context, String strTitle, String shouYeId) {
        Intent intent = new Intent(context, FenLeiThirdActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra("strTitle", strTitle);
        intent.putExtra("shouYeId", shouYeId);
        context.startActivity(intent);

    }

}
