package com.cmcc.hy.bigdata.weijifen.enums;

/**
 * 漫游类型
 * 
 * @Project: credit-collection-hivedata
 * @File: RoamType.java
 * @Date: 2015年11月12日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum RoamType {

    /**
     * 本地漫游
     */
    LOCAL {
        @Override
        public String getCode() {
            return "1";
        }
    },

    /**
     * 省内漫游
     */
    PROVINCE {
        @Override
        public String getCode() {
            return "2";
        }
    },

    /**
     * 省际漫游
     */
    INTERPROVINCIAL {
        @Override
        public String getCode() {
            return "3";
        }
    },

    /**
     * 国际漫游
     */
    INTERNATIONAL {
        @Override
        public String getCode() {
            return "4";
        }
    },

    /**
     * 港澳台漫游
     */
    HONGKONG_MACAO_TAIWAN {
        @Override
        public String getCode() {
            return "5";
        }
    },

    /**
     * 其他
     */
    OTHER {
        @Override
        public String getCode() {
            return "0";
        }
    },

    /**
     * 未知
     */
    UNDEFINED {
        @Override
        public String getCode() {
            return "-1";
        }
    };

    // 获取标准映射代码，与存入hbase的值对应
    public abstract String getCode();

    //输入code,映射到相应的枚举类型
    public static RoamType getType(String type) {
        switch (type) {
            case "1":
                return LOCAL;
            case "2":
                return PROVINCE;
            case "3":
                return INTERPROVINCIAL;
            case "4":
                return INTERNATIONAL;
            case "5":
                return HONGKONG_MACAO_TAIWAN;
            case "0":
                return OTHER;
            default:
                return UNDEFINED;
        }
    }
}
