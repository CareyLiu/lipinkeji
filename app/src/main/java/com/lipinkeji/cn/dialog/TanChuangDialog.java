package com.lipinkeji.cn.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lipinkeji.cn.R;

import butterknife.BindView;

public class TanChuangDialog extends Dialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tanchuang_chooseone);

        TextView tvQueding = findViewById(R.id.tv_queding);
        tvQueding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }



    public TanChuangDialog(@NonNull Context context) {
        super(context);

    }
}
