package com.falaer.cn.xuexi.moxie;

public class MeiZiAPerson extends Observer {
    @Override
    public void sheZhiGaiBianThing(String string) {
        System.out.println("MeiZiPerson-我收到了" + string);
    }
}
