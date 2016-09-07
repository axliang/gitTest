package com.cmcc.hy.bigdata.weijifen.enums;

/**
 * 名单类型
 * 
 * @Project: credit-collection-hivedata
 * @File: ListType.java
 * @Date: 2015年10月27日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum ListType {

    /**
     * 未定义
     */
    UNDEFINED {
        @Override
        public String getCode() {
            return "0";
        }
    },

    /**
     * 灰名单
     */
    GREY {
        @Override
        public String getCode() {
            return "2";
        }
    },

    /**
     * 黑名单
     */
    BLACK {
        @Override
        public String getCode() {
            return "1";
        }
    };

    // 获取标准映射代码，与存入hbase的值对应
    public abstract String getCode();

    //输入code,映射到相应的枚举类型
    public static ListType getType(String listType) {
        if (listType == null || "".equals(listType)) {
            return null;
        }
        listType = listType.trim();
        switch (listType) {
            case "2":
                return GREY;
            case "1":
                return BLACK;
            default:
                return UNDEFINED;
        }
    }
}
