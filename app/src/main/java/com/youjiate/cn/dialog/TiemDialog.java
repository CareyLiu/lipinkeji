package com.youjiate.cn.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.youjiate.cn.R;

public class TiemDialog extends Dialog {

    public TiemDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.layout_time_dialog);
    }
}
