package com.falaer.cn.activity.tuangou;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.falaer.cn.app.BaseActivity;
import com.falaer.cn.project_A.tuangou.TuanGouShangJiaList;
public abstract class AbStractTuanGouShangJia extends BaseActivity implements TuanGouShangJiaList {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //    setHeader();
        getNet();//访问网络
     //   shaixuan();
     //   getTurn();
    }


    public void getTurn() {
        getBanner();
        getHortialList();

    }


}
