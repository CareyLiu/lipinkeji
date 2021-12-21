package com.lipinkeji.cn.view.freshenview;

/**
 * Created by Aspsine on 2015/8/17.
 */
public interface SwipeTrigger {
    void onPrepare();

    void onMove(int y, boolean isComplete, boolean automatic);

    void onRelease();

    void onComplete();

    void onReset();
}