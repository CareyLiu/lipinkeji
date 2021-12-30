package com.lipinkeji.cn.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lipinkeji.cn.R;

public class JiareqiMimaDialog extends Dialog implements View.OnClickListener {

    public EditText tv_content;
    public TextView tv_cancel;
    public TextView tv_confirm;

    private TishiDialogListener mListener;

    public JiareqiMimaDialog setmListener(TishiDialogListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public JiareqiMimaDialog(Context context, TishiDialogListener mListener) {
        this(context, R.style.dialogBaseBlur);
        this.mListener = mListener;
    }

    public JiareqiMimaDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_jiareqi_mima);


        tv_content = findViewById(R.id.tv_content);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_confirm = findViewById(R.id.tv_confirm);


        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }


    public JiareqiMimaDialog setTextContent(String text) {
        tv_content.setText(text);
        return this;
    }

    public JiareqiMimaDialog setTextInput(int inputType) {
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
            mListener.onClickCancel(v, JiareqiMimaDialog.this);
        }
    }

    private void clickConfirm(View v) {
        if (mListener != null) {
            mListener.onClickConfirm(v, JiareqiMimaDialog.this);
        }
    }

    public interface TishiDialogListener {
        void onClickCancel(View v, JiareqiMimaDialog dialog);

        void onClickConfirm(View v, JiareqiMimaDialog dialog);
    }

    public String getTextContent() {
        return tv_content.getText().toString();
    }
}
