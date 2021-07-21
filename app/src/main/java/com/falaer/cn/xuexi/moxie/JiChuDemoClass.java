package com.falaer.cn.xuexi.moxie;

import java.util.ArrayList;
import java.util.List;

public class JiChuDemoClass {
    public static void main(String[] args) {

        List<Observer> observers = new ArrayList<>();
        MeiZiAPerson meiZiAPerson = new MeiZiAPerson();
        MeiZiAPerson2 meiZiAPerson2 = new MeiZiAPerson2();
        observers.add(meiZiAPerson);
        observers.add(meiZiAPerson2);

        TongZhiClass tongZhiClass = new TongZhiClass(observers);
        tongZhiClass.setState("来吧宝贝");
        tongZhiClass.notifyAllThis();

    }
}
