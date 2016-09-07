package com.cmcc.hy.bigdata.weijifen.enums;

/**
 * 结婚类型（待数据确定后再修改）
 * 
 * @Project: credit-collection-hivedata
 * @File: MarriedType.java
 * @Date: 2015年11月13日
 * @Author: JinYibin
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum MarriedType {
    /**
     * 未婚
     */
    UNMARRIED {
        @Override
        public String getCode() {
            return "1";
        }
    },
    /**
     * 已婚
     */
    MARRIED {
        @Override
        public String getCode() {
            return "2";
        }
    },
    /**
     * 离异
     */
    DIVORCE {
        @Override
        public String getCode() {
            return "3";
        }
    },
    /**
     * 丧偶
     */
    WIDOWED {
        @Override
        public String getCode() {
            return "4";
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
    public static MarriedType getType(String code) {
        if (code == null || "".equals(code.trim())) {
            return UNDEFINED;
        }
        code = code.trim();
        switch (code) {
            case "1":
                return UNMARRIED;
            case "2":
                return MARRIED;
            case "3":
                return DIVORCE;
            case "4":
                return WIDOWED;
            case "0":
                return OTHER;
            default:
                return UNDEFINED;
        }
    }
}
