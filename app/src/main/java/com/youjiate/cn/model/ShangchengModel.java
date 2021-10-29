package com.youjiate.cn.model;

import java.util.List;

public class ShangchengModel {

    /**
     * next : 1
     * msg_code : 0000
     * msg : ok
     * row_num : 3
     * data : [{"bannerList":[{"wares_id":"0","html_id":"6","img_url":"https://shop.hljsdkj.com/Frame/uploadFile/showImg?file_id=11407","html_url":"https://shop.hljsdkj.com/manage/subsystem/h5/article?html_id=6?user_id=4332","shop_product_id":"0","rotation_img_type":"2"}],"indexShowList":[{"wares_sales_volume":"已售0件","wares_id":"285","shop_brand":"法拉尔佳和","red_packet_money":"","money_now":"0.15","shop_product_id":"211","money_lower":"0.00","money_old":"0.15","index_photo_url":"http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20210505084823000001.jpg","wares_name":"法拉尔佳和预热器","product_title":"套餐一"},{"wares_sales_volume":"已售0件","wares_id":"284","shop_brand":"法拉尔佳和","red_packet_money":"","money_now":"0.15","shop_product_id":"210","money_lower":"0.00","money_old":"0.15","index_photo_url":"http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20210505084807000001.jpg","wares_name":"法拉尔佳和预热器","product_title":"套餐一"},{"wares_sales_volume":"已售0件","wares_id":"283","shop_brand":"法拉尔佳和","red_packet_money":"","money_now":"0.15","shop_product_id":"209","money_lower":"0.00","money_old":"0.15","index_photo_url":"http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20210505084740000001.jpg","wares_name":"法拉尔佳和水暖","product_title":"套餐一"},{"wares_sales_volume":"已售0件","wares_id":"282","shop_brand":"法拉尔佳和","red_packet_money":"","money_now":"0.15","shop_product_id":"208","money_lower":"0.00","money_old":"0.15","index_photo_url":"http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20210505084704000001.jpg","wares_name":"法拉尔佳和风暖","product_title":"套餐一"},{"wares_sales_volume":"已售0件","wares_id":"281","shop_brand":"法拉尔佳和","red_packet_money":"","money_now":"0.15","shop_product_id":"207","money_lower":"0.00","money_old":"0.15","index_photo_url":"http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20210505084639000001.jpg","wares_name":"法拉尔佳和风暖","product_title":"套餐一"},{"wares_sales_volume":"已售0件","wares_id":"280","shop_brand":"法拉尔佳和","red_packet_money":"","money_now":"0.15","shop_product_id":"206","money_lower":"0.00","money_old":"0.15","index_photo_url":"http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20210505084547000001.jpg","wares_name":"法拉尔佳和一体机","product_title":"套餐一"}],"is_display_shop":"1"}]
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
         * bannerList : [{"wares_id":"0","html_id":"6","img_url":"https://shop.hljsdkj.com/Frame/uploadFile/showImg?file_id=11407","html_url":"https://shop.hljsdkj.com/manage/subsystem/h5/article?html_id=6?user_id=4332","shop_product_id":"0","rotation_img_type":"2"}]
         * indexShowList : [{"wares_sales_volume":"已售0件","wares_id":"285","shop_brand":"法拉尔佳和","red_packet_money":"","money_now":"0.15","shop_product_id":"211","money_lower":"0.00","money_old":"0.15","index_photo_url":"http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20210505084823000001.jpg","wares_name":"法拉尔佳和预热器","product_title":"套餐一"},{"wares_sales_volume":"已售0件","wares_id":"284","shop_brand":"法拉尔佳和","red_packet_money":"","money_now":"0.15","shop_product_id":"210","money_lower":"0.00","money_old":"0.15","index_photo_url":"http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20210505084807000001.jpg","wares_name":"法拉尔佳和预热器","product_title":"套餐一"},{"wares_sales_volume":"已售0件","wares_id":"283","shop_brand":"法拉尔佳和","red_packet_money":"","money_now":"0.15","shop_product_id":"209","money_lower":"0.00","money_old":"0.15","index_photo_url":"http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20210505084740000001.jpg","wares_name":"法拉尔佳和水暖","product_title":"套餐一"},{"wares_sales_volume":"已售0件","wares_id":"282","shop_brand":"法拉尔佳和","red_packet_money":"","money_now":"0.15","shop_product_id":"208","money_lower":"0.00","money_old":"0.15","index_photo_url":"http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20210505084704000001.jpg","wares_name":"法拉尔佳和风暖","product_title":"套餐一"},{"wares_sales_volume":"已售0件","wares_id":"281","shop_brand":"法拉尔佳和","red_packet_money":"","money_now":"0.15","shop_product_id":"207","money_lower":"0.00","money_old":"0.15","index_photo_url":"http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20210505084639000001.jpg","wares_name":"法拉尔佳和风暖","product_title":"套餐一"},{"wares_sales_volume":"已售0件","wares_id":"280","shop_brand":"法拉尔佳和","red_packet_money":"","money_now":"0.15","shop_product_id":"206","money_lower":"0.00","money_old":"0.15","index_photo_url":"http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20210505084547000001.jpg","wares_name":"法拉尔佳和一体机","product_title":"套餐一"}]
         * is_display_shop : 1
         */

        private String is_display_shop;
        private List<BannerListBean> bannerList;
        private List<IndexShowListBean> indexShowList;

        public String getIs_display_shop() {
            return is_display_shop;
        }

        public void setIs_display_shop(String is_display_shop) {
            this.is_display_shop = is_display_shop;
        }

        public List<BannerListBean> getBannerList() {
            return bannerList;
        }

        public void setBannerList(List<BannerListBean> bannerList) {
            this.bannerList = bannerList;
        }

        public List<IndexShowListBean> getIndexShowList() {
            return indexShowList;
        }

        public void setIndexShowList(List<IndexShowListBean> indexShowList) {
            this.indexShowList = indexShowList;
        }

        public static class BannerListBean {
            /**
             * wares_id : 0
             * html_id : 6
             * img_url : https://shop.hljsdkj.com/Frame/uploadFile/showImg?file_id=11407
             * html_url : https://shop.hljsdkj.com/manage/subsystem/h5/article?html_id=6?user_id=4332
             * shop_product_id : 0
             * rotation_img_type : 2
             */

            private String wares_id;
            private String html_id;
            private String img_url;
            private String html_url;
            private String shop_product_id;
            private String rotation_img_type;

            public String getWares_id() {
                return wares_id;
            }

            public void setWares_id(String wares_id) {
                this.wares_id = wares_id;
            }

            public String getHtml_id() {
                return html_id;
            }

            public void setHtml_id(String html_id) {
                this.html_id = html_id;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getHtml_url() {
                return html_url;
            }

            public void setHtml_url(String html_url) {
                this.html_url = html_url;
            }

            public String getShop_product_id() {
                return shop_product_id;
            }

            public void setShop_product_id(String shop_product_id) {
                this.shop_product_id = shop_product_id;
            }

            public String getRotation_img_type() {
                return rotation_img_type;
            }

            public void setRotation_img_type(String rotation_img_type) {
                this.rotation_img_type = rotation_img_type;
            }
        }

        public static class IndexShowListBean {
            /**
             * wares_sales_volume : 已售0件
             * wares_id : 285
             * shop_brand : 法拉尔佳和
             * red_packet_money :
             * money_now : 0.15
             * shop_product_id : 211
             * money_lower : 0.00
             * money_old : 0.15
             * index_photo_url : http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20210505084823000001.jpg
             * wares_name : 法拉尔佳和预热器
             * product_title : 套餐一
             */

            private String wares_sales_volume;
            private String wares_id;
            private String shop_brand;
            private String red_packet_money;
            private String money_now;
            private String shop_product_id;
            private String money_lower;
            private String money_old;
            private String index_photo_url;
            private String wares_name;
            private String product_title;

            public String getWares_sales_volume() {
                return wares_sales_volume;
            }

            public void setWares_sales_volume(String wares_sales_volume) {
                this.wares_sales_volume = wares_sales_volume;
            }

            public String getWares_id() {
                return wares_id;
            }

            public void setWares_id(String wares_id) {
                this.wares_id = wares_id;
            }

            public String getShop_brand() {
                return shop_brand;
            }

            public void setShop_brand(String shop_brand) {
                this.shop_brand = shop_brand;
            }

            public String getRed_packet_money() {
                return red_packet_money;
            }

            public void setRed_packet_money(String red_packet_money) {
                this.red_packet_money = red_packet_money;
            }

            public String getMoney_now() {
                return money_now;
            }

            public void setMoney_now(String money_now) {
                this.money_now = money_now;
            }

            public String getShop_product_id() {
                return shop_product_id;
            }

            public void setShop_product_id(String shop_product_id) {
                this.shop_product_id = shop_product_id;
            }

            public String getMoney_lower() {
                return money_lower;
            }

            public void setMoney_lower(String money_lower) {
                this.money_lower = money_lower;
            }

            public String getMoney_old() {
                return money_old;
            }

            public void setMoney_old(String money_old) {
                this.money_old = money_old;
            }

            public String getIndex_photo_url() {
                return index_photo_url;
            }

            public void setIndex_photo_url(String index_photo_url) {
                this.index_photo_url = index_photo_url;
            }

            public String getWares_name() {
                return wares_name;
            }

            public void setWares_name(String wares_name) {
                this.wares_name = wares_name;
            }

            public String getProduct_title() {
                return product_title;
            }

            public void setProduct_title(String product_title) {
                this.product_title = product_title;
            }
        }
    }
}
