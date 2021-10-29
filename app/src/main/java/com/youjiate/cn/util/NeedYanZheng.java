package com.youjiate.cn.util;

import android.content.Context;

import com.youjiate.cn.app.App;
import com.youjiate.cn.config.PreferenceHelper;
import com.youjiate.cn.model.GouWuCheZhengHeModel;

import java.util.List;

public class NeedYanZheng {
//新增逻辑,判断wares_id在{179,188,189,190,191,192,193,194,195,196,197,198,199,200}之内,则必须有上一级的邀请码信息

    public static String shiFouYanzheng = "0";//没验证过

    public static boolean yanZheng(Context cnt, String id, List<String> strings) {

        shiFouYanzheng = PreferenceHelper.getInstance(cnt).getString(App.SHIFOUYOUSHANGJI, "0x");

        if (shiFouYanzheng.equals("1")) {

            return false;
        }


        if (strings.contains(id)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean yanZheng(Context cnt, List<GouWuCheZhengHeModel> mDatas, List<String> strings) {

        shiFouYanzheng = PreferenceHelper.getInstance(cnt).getString(App.SHIFOUYOUSHANGJI, "0x");

        if (shiFouYanzheng.equals("1")) {
            return false;
        }

        boolean flag = false;

        for (int i = 0; i < mDatas.size(); i++) {
            if (!mDatas.get(i).isHeader){
                for (int j = 0; j < strings.size(); j++) {
                    if (mDatas.get(i).getWares_id().equals(strings.get(j))) {
                        flag = true;
                        break;
                    }
                }
            }

        }
        return flag;
    }


}






