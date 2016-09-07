package com.cmcc.hy.bigdata.weijifen.enums;

/**
 * 用户使用状态类型
 * 
 * @Project: credit-collection-hivedata
 * @File: UserStatusType.java
 * @Date: 2015年11月12日
 * @Author: JinYibin
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum UserStatusType {
    /**
     * 正常在网
     */
    NORMAL_ONLINE {
        @Override
        public String getCode() {
            return "1";
        }
    },
    /**
     * 停机
     */
    OUT_OF_SERVICE {
        @Override
        public String getCode() {
            return "2";
        }
    },
    /**
     * 销户
     */
    LOG_OUT {
        @Override
        public String getCode() {
            return "3";
        }
    },
    /**
     * 未激活
     */
    UNACTIVE {
        @Override
        public String getCode() {
            return "4";
        }
    },
    /**
     * 欠费
     */
    ARREARAGE {
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
    public static UserStatusType getType(String code) {
        if (code == null) {
            return UNDEFINED;
        }
        code = code.trim();
        switch (code) {
            case "1":
                return NORMAL_ONLINE;
            case "2":
                return OUT_OF_SERVICE;
            case "3":
                return LOG_OUT;
            case "4":
                return UNACTIVE;
            case "0":
                return OTHER;
            default:
                return UNDEFINED;
        }
    }
    //		if (code == null) {
    //			return UNDEFINED;
    //		}
    //		code = code.trim();
    //		if ("US10".equals(code)) {
    //			return NORMAL_ONLINE;
    //		} else if ("US344".equals(code) || "US40".equals(code)
    //				|| "US534".equals(code) || "US536".equals(code)
    //				|| "US53".equals(code) || "US301".equals(code)
    //				|| "US302".equals(code) || "US42".equals(code)
    //				|| "US50".equals(code) || "US30".equals(code)) {
    //			return OUT_OF_SERVICE;
    //		} else if ("US24".equals(code) || "US23".equals(code)
    //				|| "US531".equals(code) || "US20".equals(code)
    //				|| "US22".equals(code) || "US528".equals(code)
    //				|| "US537".equals(code) || "US221".equals(code)) {
    //			return LOG_OUT;
    //		} else if ("US62".equals(code) || "US61".equals(code)
    //				|| "US346".equals(code) || "US26".equals(code)
    //				|| "US532".equals(code) || "US347".equals(code)
    //				|| "US348".equals(code) || "US539".equals(code)
    //				|| "US2".equals(code) || "US27".equals(code)
    //				|| "US3".equals(code) || "US34".equals(code)
    //				|| "US64".equals(code) || "US63".equals(code)
    //				|| "US28".equals(code) || "US21".equals(code)
    //				|| "US25".equals(code)) {
    //			return OTHER;
    //		} else {
    //			return UNDEFINED;
    //		}
    //	}
}
