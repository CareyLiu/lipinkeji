package com.lipinkeji.cn.lanya_fengnuan;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lipinkeji.cn.R;
import com.lipinkeji.cn.lanya_fengnuan.inter.OnDriverOnClickListener;

import java.util.List;

/**
 * Created by JiangYY on 2017/8/4
 *
 * @author JiangYY
 */

public class DriverDialog {

    private Context mContext;
    private Dialog mDialog;
    private View mView;
    private DisplayMetrics dm;
    private WindowManager windowManager;
    private int mStyle = R.style.UserinfoDialogStyle;
    private MyNumberPicker mp_first, mp_second, mp_third;
    private List<DriverData.DataBean.ClBeanXX> clBean;
    private String[] fristList;
    private String[] secondList;
    private String[] thirdList;
    private int fristIndex = 0;
    private int secondIndex = 0;
    private int thirdIndex = 0;
    private LinearLayout ll_first, ll_second, ll_third;
    private TextView tv_title, tv_first, tv_second, tv_third, tv_ensure, tv_clean;
    private OnDriverOnClickListener mOnClickListner;

    public DriverDialog(@NonNull Context context) {
        mContext = context;
        initView();
    }

    public void setOnClickLitener(OnDriverOnClickListener mOnClickListner) {
        this.mOnClickListner = mOnClickListner;
    }

    private void initView() {
        mDialog = new Dialog(mContext, mStyle);
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_driver_dialog, null);
        dm = new DisplayMetrics();
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        Window window = mDialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        //?????????????????????????????????
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //?????????????????????????????????
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
        mDialog.setContentView(mView);
        ll_first = mView.findViewById(R.id.ll_first);
        ll_second = mView.findViewById(R.id.ll_second);
        ll_third = mView.findViewById(R.id.ll_third);
        mp_first = mView.findViewById(R.id.mp_first);
        mp_second = mView.findViewById(R.id.mp_second);
        mp_third = mView.findViewById(R.id.mp_third);
        tv_title = mView.findViewById(R.id.tv_title);
        tv_first = mView.findViewById(R.id.tv_first);
        tv_second = mView.findViewById(R.id.tv_second);
        tv_third = mView.findViewById(R.id.tv_third);
        tv_ensure = mView.findViewById(R.id.tv_ensure);
        tv_clean = mView.findViewById(R.id.tv_clean);
        tv_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListner.onClick(tv_ensure, fristIndex, secondIndex, thirdIndex);
                dismiss();
            }
        });
        tv_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show(List<DriverData.DataBean> list, int type) {
        setData(list, type);
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private void setData(List<DriverData.DataBean> list, int type) {
        switch (type) {
            case 0:
                tv_title.setText("?????????");
                tv_first.setText("??????");
                tv_second.setText("??????");
                two_initLinkage(list, type);
                break;
            case 1:
                tv_title.setText("??????????????????");
                tv_first.setText("??????");
                tv_second.setText("??????");
                two_initLinkage(list, type);
                break;
            case 2:
                tv_title.setText("??????");
                tv_first.setText("??????");
                tv_second.setText("??????");
                tv_third.setText("??????");
                three_initLinkage(list, type);
                break;
            case 3:
                tv_title.setText("??????");
                tv_first.setText("??????");
                tv_second.setText("??????(??????)");
                tv_third.setText("??????");
                three_initLinkage(list, type);
                break;
            case 4:
                tv_title.setText("?????????");
                tv_first.setText("??????");
                tv_second.setText("??????");
                two_initLinkage(list, type);
                break;
            case 5:
                tv_title.setText("?????????");
                tv_first.setText("??????");
                ont_initLinkage(list, type);
                break;
            case 6:
                tv_title.setText("????????????");
                tv_first.setText("??????");
                ont_initLinkage(list, type);
                break;
            case 7:
                tv_title.setText("?????????");
                tv_first.setText("??????");
                tv_second.setText("??????");
                two_initLinkage(list, type);
                break;
            case 8:
                tv_title.setText("??????");
                tv_first.setText("??????");
                tv_second.setText("??????");
                two_initLinkage(list, type);
                break;
            case 9:
                tv_title.setText("????????????(???)");
                tv_first.setText("??????");
                ont_initLinkage(list, type);
                break;
            case 10:
                tv_title.setText("?????????");
                tv_first.setText("??????");
                ont_initLinkage(list, type);
                break;
        }
    }

    //????????????
    private void ont_initLinkage(List<DriverData.DataBean> list, int type) {
        clBean = list.get(type).getCl();
        fristList = new String[clBean.size()];
        for (int i = 0; i < clBean.size(); i++) {
            fristList[i] = (clBean.get(i).getPar_name());
        }
        mp_first.setDisplayedValuesAndPickedIndex(fristList, fristIndex, true);
        ll_first.setVisibility(View.VISIBLE);
        ll_second.setVisibility(View.GONE);
        ll_third.setVisibility(View.GONE);
    }

    //????????????
    private void two_initLinkage(List<DriverData.DataBean> list, int type) {
        clBean = list.get(type).getCl();
        fristList = new String[clBean.size()];
        for (int i = 0; i < clBean.size(); i++) {
            fristList[i] = (clBean.get(i).getPar_name());
            secondList = new String[clBean.get(i).getCl().size()];
            for (int j = 0; j < clBean.get(i).getCl().size(); j++) {
                secondList[j] = (clBean.get(i).getCl().get(j).getPar_name());
            }
        }
        mp_first.setDisplayedValuesAndPickedIndex(fristList, fristIndex, true);
        mp_second.setDisplayedValuesAndPickedIndex(secondList, secondIndex, true);
        ll_first.setVisibility(View.VISIBLE);
        ll_second.setVisibility(View.VISIBLE);
        ll_third.setVisibility(View.GONE);

        mp_first.setOnValueChangedListener(new MyNumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(MyNumberPicker picker, int oldVal, int newVal) {
                fristIndex = newVal;
                secondIndex = 0;
                for (int j = 0; j < clBean.get(newVal).getCl().size(); j++) {
                    secondList[j] = (clBean.get(newVal).getCl().get(j).getPar_name());
                }
                mp_second.setDisplayedValuesAndPickedIndex(secondList, secondIndex, true);
            }
        });
        mp_second.setOnValueChangedListener(new MyNumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(MyNumberPicker picker, int oldVal, int newVal) {
                secondIndex = newVal;
            }
        });
    }

    //????????????
    private void three_initLinkage(List<DriverData.DataBean> list, int type) {
        clBean = list.get(type).getCl();
        fristList = new String[clBean.size()];
        for (int i = 0; i < fristList.length; i++) {
            fristList[i] = (clBean.get(i).getPar_name());
            secondList = new String[clBean.get(i).getCl().size()];
            for (int j = 0; j < secondList.length; j++) {
                secondList[j] = (clBean.get(i).getCl().get(j).getPar_name());
                thirdList = new String[clBean.get(i).getCl().get(j).getCl().size()];
                for (int k = 0; k < thirdList.length; k++) {
                    thirdList[k] = clBean.get(i).getCl().get(j).getCl().get(k).getPar_name();
                }
            }
        }
        mp_first.setDisplayedValuesAndPickedIndex(fristList, fristIndex, true);
        mp_second.setDisplayedValuesAndPickedIndex(secondList, secondIndex, true);
        mp_third.setDisplayedValuesAndPickedIndex(thirdList, thirdIndex, true);
        ll_first.setVisibility(View.VISIBLE);
        ll_second.setVisibility(View.VISIBLE);
        ll_third.setVisibility(View.VISIBLE);

        mp_first.setOnValueChangedListener(new MyNumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(MyNumberPicker picker, int oldVal, int newVal) {
                fristIndex = newVal;
                secondIndex = 0;
                for (int j = 0; j < clBean.get(fristIndex).getCl().size(); j++) {
                    secondList[j] = (clBean.get(fristIndex).getCl().get(j).getPar_name());
                }
                mp_second.setDisplayedValuesAndPickedIndex(secondList, secondIndex, true);
                mp_third.setDisplayedValuesAndPickedIndex(thirdList, thirdIndex, true);
            }
        });
        mp_second.setOnValueChangedListener(new MyNumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(MyNumberPicker picker, int oldVal, int newVal) {
                secondIndex = newVal;
                thirdIndex = 0;
                for (int k = 0; k < clBean.get(fristIndex).getCl().get(secondIndex).getCl().size(); k++) {
                    thirdList[k] = clBean.get(fristIndex).getCl().get(secondIndex).getCl().get(k).getPar_name();
                }
                mp_third.setDisplayedValuesAndPickedIndex(thirdList, thirdIndex, true);
            }
        });
    }
}
