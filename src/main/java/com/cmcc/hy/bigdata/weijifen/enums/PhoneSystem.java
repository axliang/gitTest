package com.cmcc.hy.bigdata.weijifen.enums;

/**
 * 手机系统
 *
 * @Project: credit-collection-hivedata
 * @File: PhoneSystem.java
 * @Date: 2015年12月1日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum PhoneSystem {

    IOS {
        @Override
        public String getCode() {
            return "IOS";
        }
    },

    ANDROID {
        @Override
        public String getCode() {
            return "安卓";
        }
    },

    WINDOWS_PHONE {
        @Override
        public String getCode() {
            return "Windows Phone";
        }
    },

    SYMBIAN {
        @Override
        public String getCode() {
            return "塞班";
        }
    },

    OTHERS {
        @Override
        public String getCode() {
            return "其他";
        }
    };

    // 获取标准映射代码，与存入hbase的值对应
    public abstract String getCode();

    //输入code,映射到相应的枚举类型
    public static PhoneSystem getType(String system) {
        if (system == null || system.isEmpty()) {
            return null;
        }
        switch (system) {
            case "IOS":
                return IOS;
            case "安卓":
                return ANDROID;
            case "塞班":
                return SYMBIAN;
            case "Windows Phone":
                return WINDOWS_PHONE;
            default:
                return OTHERS;
        }
    }
}
