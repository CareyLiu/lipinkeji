package com.falaer.cn.util;

import android.app.ActivityManager;
import android.content.Context;

import com.falaer.cn.config.MyApplication;

import java.util.Iterator;



public class JinChengUtils {
    /**
     * 获取运行该方法的进程的进程名
     *
     * @return 进程名称
     */
    public static String getProcessName() {
        int processId = android.os.Process.myPid();
        String processName = null;
        ActivityManager manager = (ActivityManager) MyApplication.context.getSystemService(Context.ACTIVITY_SERVICE);
        Iterator iterator = manager.getRunningAppProcesses().iterator();
        while (iterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo processInfo = (ActivityManager.RunningAppProcessInfo) (iterator.next());
            try {
                if (processInfo.pid == processId) {
                    processName = processInfo.processName;
                    return processName;
                }
            } catch (Exception e) {
//                LogD(e.getMessage())
            }
        }
        return processName;
    }

}
