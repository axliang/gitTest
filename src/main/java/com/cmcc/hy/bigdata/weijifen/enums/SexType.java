package com.cmcc.hy.bigdata.weijifen.enums;

/**
 * 性别类型
 * 
 * @Project: credit-collection-hivedata
 * @File: SexType.java
 * @Date: 2015年11月10日
 * @Author: JinYibin
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */

public enum SexType {
    /**
     * 男
     */
    MALE {
        @Override
        public String getCode() {
            return "M";
        }
    },
    /**
     * 女
     */
    FEMALE {
        @Override
        public String getCode() {
            return "F";
        }
    },
    /**
     * 资料不详
     */
    OTHER {
        @Override
        public String getCode() {
            return "U";
        }
    };

    /**
     * 获取性别标识
     * 
     * @return
     */
    // 获取标准映射代码，与存入hbase的值对应
    public abstract String getCode();

    //输入code,映射到相应的枚举类型
    public static SexType getType(String code) {
        switch (code) {
            case "M":
                return MALE;
            case "F":
                return FEMALE;
            default:
                return OTHER;
        }
    }
}
