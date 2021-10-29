package com.youjiate.cn.xuexi.moxie;

import java.util.ArrayList;
import java.util.List;

public class TongZhiClass {
    String state;

    List<Observer> observerList = new ArrayList<>();

    public TongZhiClass(List<Observer> observerList) {
        this.observerList = observerList;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void notifyAllThis() {
        for (Observer observer : observerList) {
            observer.sheZhiGaiBianThing(state);
        }
    }
}
