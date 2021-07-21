package com.falaer.cn.activity.vip.model;

import java.util.List;

public class XufeiModel {

    /**
     * msg_code : 0000
     * msg : 添加成功
     * data : [{"money":"1","year":"一年","pay_m_y_type":"1"},{"money":"2","year":"两年","pay_m_y_type":"2"},{"money":"3","year":"三年","pay_m_y_type":"3"}]
     */

    private String msg_code;
    private String msg;
    private List<DataBean> data;

    public String getMsg_code() {
        return msg_code;
    }

    public void setMsg_code(String msg_code) {
        this.msg_code = msg_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * money : 1
         * year : 一年
         * pay_m_y_type : 1
         */
        private boolean isSelect;
        private String money;
        private String year;
        private String pay_m_y_type;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getPay_m_y_type() {
            return pay_m_y_type;
        }

        public void setPay_m_y_type(String pay_m_y_type) {
            this.pay_m_y_type = pay_m_y_type;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
