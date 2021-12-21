package com.lipinkeji.cn.activity.tuangou;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lipinkeji.cn.app.BaseActivity;
import com.lipinkeji.cn.project_A.tuangou.TuanGouShangJiaDetailsInter;

public abstract class AbStracTuanGouShangJiaDetails extends BaseActivity implements TuanGouShangJiaDetailsInter {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void getTurn() {
        setList();
        setHeader();

    }
}
