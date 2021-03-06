package com.lipinkeji.cn.model;

import java.util.List;

public class ZhiNengWeiYuBean {


    /**
     * msg_code : 0000
     * msg : ok
     * row_num : 1
     * data : [{"room_id":"0","family_id":"19","noti_info":"开启自动喂鱼，设置完相关参数后，点击提交按钮，提示设置成功，表示设置自动喂鱼已成功","device_type_name":"自动喂鱼","device_id":"74","device_ccid":"0333","kids_mode":"2","fj_time":"18:04","device_type":"03","server_id":"1/","fj_interval":"01","room_name":"默认房间","timing_state":"1","device_name":"自动喂鱼","device_type_pic":"https://shop.hljsdkj.com/Frame/uploadFile/showImg?file_id=10532","online_state":"1","work_state":"2","warn_state":"3","fj_times":"01c","auto_state":"2","family_name":"心爱的小窝","device_ccid_up":"jjjjjjjjjjjjjjjjjjjjjj21"}]
     */

    private String msg_code;
    private String msg;
    private String row_num;
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
         * room_id : 0
         * family_id : 19
         * noti_info : 开启自动喂鱼，设置完相关参数后，点击提交按钮，提示设置成功，表示设置自动喂鱼已成功
         * device_type_name : 自动喂鱼
         * device_id : 74
         * device_ccid : 0333
         * kids_mode : 2
         * fj_time : 18:04
         * device_type : 03
         * server_id : 1/
         * fj_interval : 01
         * room_name : 默认房间
         * timing_state : 1
         * device_name : 自动喂鱼
         * device_type_pic : https://shop.hljsdkj.com/Frame/uploadFile/showImg?file_id=10532
         * online_state : 1
         * work_state : 2
         * warn_state : 3
         * fj_times : 01c
         * auto_state : 2
         * family_name : 心爱的小窝
         * device_ccid_up : jjjjjjjjjjjjjjjjjjjjjj21
         */

        private String room_id;
        private String family_id;
        private String noti_info;
        private String device_type_name;
        private String device_id;
        private String device_ccid;
        private String kids_mode;
        private String fj_time;
        private String device_type;
        private String server_id;
        private String fj_interval;
        private String room_name;
        private String timing_state;
        private String device_name;
        private String device_type_pic;
        private String online_state;
        private String work_state;
        private String warn_state;
        private String fj_times;
        private String auto_state;
        private String family_name;
        private String device_ccid_up;
        public String is_voice;

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getFamily_id() {
            return family_id;
        }

        public void setFamily_id(String family_id) {
            this.family_id = family_id;
        }

        public String getNoti_info() {
            return noti_info;
        }

        public void setNoti_info(String noti_info) {
            this.noti_info = noti_info;
        }

        public String getDevice_type_name() {
            return device_type_name;
        }

        public void setDevice_type_name(String device_type_name) {
            this.device_type_name = device_type_name;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getDevice_ccid() {
            return device_ccid;
        }

        public void setDevice_ccid(String device_ccid) {
            this.device_ccid = device_ccid;
        }

        public String getKids_mode() {
            return kids_mode;
        }

        public void setKids_mode(String kids_mode) {
            this.kids_mode = kids_mode;
        }

        public String getFj_time() {
            return fj_time;
        }

        public void setFj_time(String fj_time) {
            this.fj_time = fj_time;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }

        public String getServer_id() {
            return server_id;
        }

        public void setServer_id(String server_id) {
            this.server_id = server_id;
        }

        public String getFj_interval() {
            return fj_interval;
        }

        public void setFj_interval(String fj_interval) {
            this.fj_interval = fj_interval;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public String getTiming_state() {
            return timing_state;
        }

        public void setTiming_state(String timing_state) {
            this.timing_state = timing_state;
        }

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
        }

        public String getDevice_type_pic() {
            return device_type_pic;
        }

        public void setDevice_type_pic(String device_type_pic) {
            this.device_type_pic = device_type_pic;
        }

        public String getOnline_state() {
            return online_state;
        }

        public void setOnline_state(String online_state) {
            this.online_state = online_state;
        }

        public String getWork_state() {
            return work_state;
        }

        public void setWork_state(String work_state) {
            this.work_state = work_state;
        }

        public String getWarn_state() {
            return warn_state;
        }

        public void setWarn_state(String warn_state) {
            this.warn_state = warn_state;
        }

        public String getFj_times() {
            return fj_times;
        }

        public void setFj_times(String fj_times) {
            this.fj_times = fj_times;
        }

        public String getAuto_state() {
            return auto_state;
        }

        public void setAuto_state(String auto_state) {
            this.auto_state = auto_state;
        }

        public String getFamily_name() {
            return family_name;
        }

        public void setFamily_name(String family_name) {
            this.family_name = family_name;
        }

        public String getDevice_ccid_up() {
            return device_ccid_up;
        }

        public void setDevice_ccid_up(String device_ccid_up) {
            this.device_ccid_up = device_ccid_up;
        }
    }
}
