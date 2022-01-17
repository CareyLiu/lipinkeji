package com.lipinkeji.cn.activity.device_a.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lipinkeji.cn.R;
import com.lipinkeji.cn.util.Y;

public class JiareqiWenduDialog extends Dialog implements View.OnClickListener {

    public TextView tv_content;
    public TextView tv_cancel;
    public TextView tv_confirm;
    public TextView tv_title;
    public SeekBar seekBar;
    private String wendu;
    private String type;//1 风暖上限  2风暖下限   3水暖上限   4水暖下限  5监测温度开机  6监测温度关机

    private TishiDialogListener mListener;

    public JiareqiWenduDialog setmListener(TishiDialogListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public JiareqiWenduDialog(Context context, TishiDialogListener mListener) {
        this(context, R.style.dialogBaseBlur);
        this.mListener = mListener;
    }

    public JiareqiWenduDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.a_jiareqi_dialog_wendu);
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
                if (type.equals("1")) {
                    tv_content.setText("温度范围0-35，当前" + wendu);
                } else if (type.equals("3")) {
                    tv_content.setText("温度范围50-85，当前" + wendu);
                } else if (type.equals("4")) {
                    tv_content.setText("温度范围45-80，当前" + wendu);
                } else if (type.equals("5")) {
                    tv_content.setText("当温度为" + wendu + "的时候开启加热器");
                } else if (type.equals("6")) {
                    tv_content.setText("当温度为" + wendu + "的时候关闭加热器");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public JiareqiWenduDialog setTextContent(String text) {
        tv_content.setText(text);
        return this;
    }

    public JiareqiWenduDialog setTextInput(int inputType) {
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
            mListener.onClickCancel(v, JiareqiWenduDialog.this);
        }
    }

    private void clickConfirm(View v) {
        if (mListener != null) {
            mListener.onClickConfirm(v, JiareqiWenduDialog.this);
        }
    }

    public interface TishiDialogListener {
        void onClickCancel(View v, JiareqiWenduDialog dialog);

        void onClickConfirm(View v, JiareqiWenduDialog dialog);
    }

    public String getTextContent() {
        return tv_content.getText().toString();
    }

    public String getWendu() {
        return wendu;
    }

    @SuppressLint("NewApi")
    public void setType(String type, String wendu) {
        this.type = type;
        this.wendu = wendu;
        if (type.equals("3")) {
            seekBar.setMin(50);
            seekBar.setMax(85);
            tv_content.setText("温度范围50-85，当前" + wendu);
            tv_title.setText("温度上限设置");
        } else if (type.equals("4")) {
            seekBar.setMin(45);
            seekBar.setMax(80);
            tv_content.setText("温度范围45-80，当前" + wendu);
            tv_title.setText("温度下限设置");
        } else if (type.equals("5")) {
            seekBar.setMin(-99);
            seekBar.setMax(99);
            tv_content.setText("当温度为" + wendu + "的时候开启加热器");
            tv_title.setText("监测温度开机");
        } else if (type.equals("6")) {
            seekBar.setMin(-99);
            seekBar.setMax(99);
            tv_content.setText("当温度为" + wendu + "的时候关闭加热器");
            tv_title.setText("监测温度关机");
        }

        seekBar.setProgress(Y.getInt(wendu));
    }
}
