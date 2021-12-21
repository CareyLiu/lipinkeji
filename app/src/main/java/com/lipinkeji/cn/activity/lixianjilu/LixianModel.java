package com.lipinkeji.cn.activity.lixianjilu;

import java.util.List;

public class LixianModel {


    /**
     * next : 1
     * msg_code : 0000
     * msg : ok
     * row_num : 12
     * data : [{"offline_id":"61","time":"2020-10-28 10:17:24","create_time":"2020-10-28","ccid":"aaaaaaaaaaaaaaaa50070018"},{"offline_id":"60","time":"2020-10-28 10:17:18","create_time":"2020-10-28","ccid":"aaaaaaaaaaaaaaaa50070018"},{"offline_id":"59","time":"2020-10-28 09:06:04","create_time":"2020-10-28","ccid":"aaaaaaaaaaaaaaaa50070018"},{"offline_id":"58","time":"2020-10-28 09:06:02","create_time":"2020-10-28","ccid":"aaaaaaaaaaaaaaaa50070018"},{"offline_id":"57","time":"2020-10-28 09:06:00","create_time":"2020-10-28","ccid":"aaaaaaaaaaaaaaaa50070018"},{"offline_id":"56","time":"2020-10-28 09:05:48","create_time":"2020-10-28","ccid":"aaaaaaaaaaaaaaaa50070018"},{"offline_id":"55","time":"2020-10-28 08:03:40","create_time":"2020-10-28","ccid":"aaaaaaaaaaaaaaaa50070018"},{"offline_id":"54","time":"2020-10-28 08:03:36","create_time":"2020-10-28","ccid":"aaaaaaaaaaaaaaaa50070018"},{"offline_id":"53","time":"2020-10-28 08:03:24","create_time":"2020-10-28","ccid":"aaaaaaaaaaaaaaaa50070018"},{"offline_id":"52","time":"2020-10-28 08:03:22","create_time":"2020-10-28","ccid":"aaaaaaaaaaaaaaaa50070018"},{"offline_id":"51","time":"2020-10-28 07:57:22","create_time":"2020-10-28","ccid":"aaaaaaaaaaaaaaaa50070018"},{"offline_id":"50","time":"2020-10-28 07:57:10","create_time":"2020-10-28","ccid":"aaaaaaaaaaaaaaaa50070018"}]
     */

    private String next;
    private String msg_code;
    private String msg;
    private String row_num;
    private List<DataBean> data;

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

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

    public String getRow_num() {
        return row_num;
    }

    public void setRow_num(String row_num) {
        this.row_num = row_num;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * offline_id : 61
         * time : 2020-10-28 10:17:24
         * create_time : 2020-10-28
         * ccid : aaaaaaaaaaaaaaaa50070018
         */

        private String offline_id;
        private String time;
        private String create_time;
        private String ccid;

        public String getOffline_id() {
            return offline_id;
        }

        public void setOffline_id(String offline_id) {
            this.offline_id = offline_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getCcid() {
            return ccid;
        }

        public void setCcid(String ccid) {
            this.ccid = ccid;
        }
    }
}
