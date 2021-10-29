package com.youjiate.cn.xuexi.moxie;

public class MeiZiAPerson2 extends Observer {

    @Override
    public void sheZhiGaiBianThing(String string) {
        System.out.println("MeiZiPerson2-我收到了" + string);
    }
}
