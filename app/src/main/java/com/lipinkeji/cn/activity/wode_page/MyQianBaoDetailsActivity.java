package com.lipinkeji.cn.activity.wode_page;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lipinkeji.cn.R;
import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.model.MyQianBaoXianFeiMingXiModel;

import butterknife.BindView;

public class MyQianBaoDetailsActivity extends BaseActivity {

    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_tixian)
    TextView tvTixian;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_tixian_dangqianzhuangtai)
    TextView tvTixianDangqianzhuangtai;
    @BindView(R.id.tv_tixian_tixianjine)
    TextView tvTixianTixianjine;
    @BindView(R.id.tv_tixian_fuwufei)
    TextView tvTixianFuwufei;
    @BindView(R.id.tv_tixian_shenqingshijian)
    TextView tvTixianShenqingshijian;
    @BindView(R.id.tv_tixian_daozhangshijian)
    TextView tvTixianDaozhangshijian;
    @BindView(R.id.tv_tixian_tixianfangshi)
    TextView tvTixianTixianfangshi;
    @BindView(R.id.tv_tixian_tixiandanhao)
    TextView tvTixianTixiandanhao;
    @BindView(R.id.ll_tixian)
    LinearLayout llTixian;
    @BindView(R.id.tv_zaixian_dangqianzhuangtai)
    TextView tvZaixianDangqianzhuangtai;
    @BindView(R.id.tv_zaixian_youhuiquan)
    TextView tvZaixianYouhuiquan;
    @BindView(R.id.tv_zaixian_shangpin)
    TextView tvZaixianShangpin;
    @BindView(R.id.tv_zaixian_shanghuquancheng)
    TextView tvZaixianShanghuquancheng;
    @BindView(R.id.tv_zaixian_zhifu)
    TextView tvZaixianZhifu;
    @BindView(R.id.tv_zaixian_jiaoyidanhao)
    TextView tvZaixianJiaoyidanhao;
    @BindView(R.id.ll_zaixian_zhifu)
    LinearLayout llZaixianZhifu;
    @BindView(R.id.tv_fenrun_dangqianzhuangtai)
    TextView tvFenrunDangqianzhuangtai;
    @BindView(R.id.tv_fenrun_shoukuanfangshi)
    TextView tvFenRunShouKuanFangShi;
    @BindView(R.id.tv_zaixian_shoukuanshijian)
    TextView tvZaixianShoukuanshijian;
    @BindView(R.id.ll_fenrun)
    LinearLayout llFenrun;
    @BindView(R.id.tv_tuikuan_dangqianzhuangtai)
    TextView tvTuikuanDangqianzhuangtai;
    @BindView(R.id.tv_tuikuan_shangpin)
    TextView tvTuikuanShangpin;
    @BindView(R.id.tv_tuikuan_zhifushijian)
    TextView tvTuikuanZhifushijian;
    @BindView(R.id.tv_tuikuan_zhifufangshi)
    TextView tvTuikuanZhifufangshi;
    @BindView(R.id.tv_tuikuan_tuikuandanhao)
    TextView tvTuikuanTuikuandanhao;
    @BindView(R.id.ll_tuikuan)
    LinearLayout llTuikuan;

    MyQianBaoXianFeiMingXiModel.DataBean.CunsumerListBean cunsumerListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_qian_bao_details);
        cunsumerListBean = (MyQianBaoXianFeiMingXiModel.DataBean.CunsumerListBean) getIntent().getSerializableExtra("cunsumerListBean");

        //???????????????1.?????? 2.?????? 3.??????
        //9.??????
        setHeaderInfo(cunsumerListBean.getEr_type(), cunsumerListBean.getMoney(), cunsumerListBean);

    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_my_qian_bao_details;
    }

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, MyQianBaoXianFeiMingXiModel.DataBean.CunsumerListBean cunsumerListBean) {
        Intent intent = new Intent(context, MyQianBaoDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("cunsumerListBean", cunsumerListBean);
        context.startActivity(intent);
    }


    public void setHeaderInfo(String type, String price, MyQianBaoXianFeiMingXiModel.DataBean.CunsumerListBean cunsumerListBean) {


        if (type.equals("1")) {

            llFenrun.setVisibility(View.VISIBLE);
            Glide.with(MyQianBaoDetailsActivity.this).load(R.mipmap.mingxi_icon_dailifenrun).into(ivImg);
            tvTixian.setText("??????");
            tvPrice.setText(price);

            tvFenrunDangqianzhuangtai.setText(cunsumerListBean.getForm_state_name());
            tvFenRunShouKuanFangShi.setText(cunsumerListBean.getPay_detail());


        } else if (type.equals("2")) {
            llZaixianZhifu.setVisibility(View.VISIBLE);
            Glide.with(MyQianBaoDetailsActivity.this).load(R.mipmap.mingxi_icon_zhifu).into(ivImg);
            tvTixian.setText("????????????");
            tvPrice.setText(price);


            tvZaixianDangqianzhuangtai.setText(cunsumerListBean.getForm_state_name());
            tvZaixianShangpin.setText(cunsumerListBean.getPay_detail());
            tvZaixianShanghuquancheng.setText(cunsumerListBean.getInst_name());
            tvZaixianShoukuanshijian.setText(cunsumerListBean.getPay_time());



            //tvZaixianZhifu.setText(cunsumerListBean.);

            tvZaixianJiaoyidanhao.setText(cunsumerListBean.getForm_no());

        } else if (type.equals("3")) {


            /**
             * form_state_name	????????????????????????????????????
             * user_money	????????????
             * create_time	????????????
             * create_time	????????????
             * pay_type_name	????????????
             * form_no	????????????
             */
            llTixian.setVisibility(View.VISIBLE);
            Glide.with(MyQianBaoDetailsActivity.this).load(R.mipmap.mingxi_icon_tixian).into(ivImg);
            tvTixian.setText("??????");
            tvPrice.setText(price);
            tvTixianDangqianzhuangtai.setText(cunsumerListBean.getForm_state_name());
            tvTixianTixianjine.setText(cunsumerListBean.getUser_money());
            tvTixianFuwufei.setText("??????");
            tvTixianShenqingshijian.setText(cunsumerListBean.getCreate_time());
            tvTixianDaozhangshijian.setText(cunsumerListBean.getCreate_time());
            tvTixianTixiandanhao.setText(cunsumerListBean.getForm_no());


        } else if (type.equals("9")) {
            llTuikuan.setVisibility(View.VISIBLE);
            Glide.with(MyQianBaoDetailsActivity.this).load(R.mipmap.mingxi_icon_tuikuan).into(ivImg);
            tvTixian.setText("??????");
            tvPrice.setText(price);


            /**
             * form_state_name	??????????????????????????????
             * pay_detail	??????
             * refund_time	????????????
             * pay_type_name	????????????
             * rollback_no	????????????
             */
            tvTuikuanDangqianzhuangtai.setText(cunsumerListBean.getForm_state_name());
            tvTuikuanShangpin.setText(cunsumerListBean.getPay_detail());
            tvTuikuanZhifushijian.setText(cunsumerListBean.getRefund_time());
            //tvTuikuanZhifufangshi.setText(cunsumerListBean. );
            tvTuikuanTuikuandanhao.setText(cunsumerListBean.getRollback_no());
        }


    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("??????");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imm.hideSoftInputFromWindow(findViewById(R.id.cl_layout).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });
    }

}
