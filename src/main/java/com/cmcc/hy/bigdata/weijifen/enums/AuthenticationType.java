package com.cmcc.hy.bigdata.weijifen.enums;

/**
 * 用户认证类型
 * 
 * @Project: credit-collection-hivedata
 * @File: UserStatusType.java
 * @Date: 2015年11月12日
 * @Author: JinYibin
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum AuthenticationType {
    /**
     * 未验证
     */
    NO_VALIDATION {
        @Override
        public String getCode() {
            return "0";
        }
    },
    /**
     * 预验证
     */
    PRE_VALIDATION {
        @Override
        public String getCode() {
            return "1";
        }
    },
    /**
     * 实名验证
     */
    VALIDATION {
        @Override
        public String getCode() {
            return "2";
        }
    },
    /**
     * 不详
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
    public static AuthenticationType getType(String authenticationType) {
        if (authenticationType == null || "".equals(authenticationType)) {
            return null;
        }
        authenticationType = authenticationType.trim();
        switch (authenticationType) {
            case "0":
                return NO_VALIDATION;
            case "1":
                return PRE_VALIDATION;
            case "2":
                return VALIDATION;
            default:
                return UNDEFINED;
        }
    }
}
