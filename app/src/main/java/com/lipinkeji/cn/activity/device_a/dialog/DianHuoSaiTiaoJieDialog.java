package com.lipinkeji.cn.activity.device_a.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lipinkeji.cn.R;
import com.lipinkeji.cn.util.Y;

public class DianHuoSaiTiaoJieDialog extends Dialog implements View.OnClickListener {

    public TextView tv_content;
    public TextView tv_cancel;
    public TextView tv_confirm;
    public TextView tv_title;
    public SeekBar seekBar;
    private String wendu;
    private String type;//1 风暖上限  2风暖下限   3水暖上限   4水暖下限  5监测温度开机  6监测温度关机

    private TishiDialogListener mListener;

    public DianHuoSaiTiaoJieDialog setmListener(TishiDialogListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public DianHuoSaiTiaoJieDialog(Context context, TishiDialogListener mListener) {
        this(context, R.style.dialogBaseBlur);
        this.mListener = mListener;
    }

    public DianHuoSaiTiaoJieDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dianhuosai_tiaojie);
        tv_content = findViewById(R.id.tv_content);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_title = findViewById(R.id.tv_title);
        seekBar = findViewById(R.id.seekBar);

        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                wendu = progress + "";
                tv_content.setText("当前加热时间为" + wendu + "秒");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public DianHuoSaiTiaoJieDialog setTextContent(String text) {
        tv_content.setText(text);
        return this;
    }

    public DianHuoSaiTiaoJieDialog setTextInput(int inputType) {
        tv_content.setInputType(inputType);
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v == tv_cancel) {
            clickCancel(v);
        } else if (v == tv_confirm) {
            clickConfirm(v);
        }
    }


    private void clickCancel(View v) {
        if (mListener != null) {
            mListener.onClickCancel(v, DianHuoSaiTiaoJieDialog.this);
        }
    }

    private void clickConfirm(View v) {
        if (mListener != null) {
            mListener.onClickConfirm(v, DianHuoSaiTiaoJieDialog.this, wendu);
        }
    }

    public interface TishiDialogListener {
        void onClickCancel(View v, DianHuoSaiTiaoJieDialog dialog);

        void onClickConfirm(View v, DianHuoSaiTiaoJieDialog dialog, String wendu);
    }

    public String getTextContent() {
        return tv_content.getText().toString();
    }

    public String getWendu() {
        return wendu;
    }

}
