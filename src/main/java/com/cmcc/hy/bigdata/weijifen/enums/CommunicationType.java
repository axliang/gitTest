package com.cmcc.hy.bigdata.weijifen.enums;

/**
 * 通话往来类型
 * 
 * @Project: credit-collection-hivedata
 * @File: CommunicationType.java
 * @Date: 2015年11月12日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum CommunicationType {

    /**
     * 当业务类型是通话的时候表示主叫，否则表示发
     */
    S {
        @Override
        public String getCode() {
            return "S";
        }
    },

    /**
     * 当业务类型是通话的时候表示被叫，否则表示收
     */
    R {
        @Override
        public String getCode() {
            return "R";
        }
    },

    /**
     * 呼转
     */
    T {
        @Override
        public String getCode() {
            return "T";
        }
    },

    /**
     * 其他
     */
    O {
        @Override
        public String getCode() {
            return "O";
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
    public static CommunicationType getType(String communicationType) {
        if (communicationType == null || "".equals(communicationType)) {
            return null;
        }
        communicationType = communicationType.trim();
        switch (communicationType) {
            case "S":
                return S;
            case "R":
                return R;
            case "T":
                return T;
            case "O":
                return O;
            default:
                return UNDEFINED;
        }
    }
}
