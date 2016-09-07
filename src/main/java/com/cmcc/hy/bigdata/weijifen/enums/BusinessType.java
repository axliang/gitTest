package com.cmcc.hy.bigdata.weijifen.enums;

/**
 * 业务类型
 * 
 * @Project: credit-collection-hivedata
 * @File: BusinessType.java
 * @Date: 2015年11月12日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum BusinessType {

    /**
     * 短信
     */
    SMS {
        @Override
        public String getCode() {
            return "SMS";
        }
    },

    /**
     * 彩信
     */
    MMS {
        @Override
        public String getCode() {
            return "MMS";
        }
    },

    /**
     * 电话
     */
    CALL {
        @Override
        public String getCode() {
            return "CALL";
        }
    },

    /**
     * 未定义
     */
    UNDEFINED {
        @Override
        public String getCode() {
            return "UNDEFINED";
        }
    };

    // 获取标准映射代码，与存入hbase的值对应
    public abstract String getCode();

    //输入code,映射到相应的枚举类型
    public static BusinessType getType(String businessType) {
        if (businessType == null || "".equals(businessType)) {
            return null;
        }
        businessType = businessType.trim();
        switch (businessType) {
            case "SMS":
                return SMS;
            case "MMS":
                return MMS;
            case "CALL":
                return CALL;
            default:
                return UNDEFINED;
        }
    }
}
