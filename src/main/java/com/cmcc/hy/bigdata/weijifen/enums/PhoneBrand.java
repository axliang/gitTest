package com.cmcc.hy.bigdata.weijifen.enums;

/**
 * 手机品牌
 *
 * @Project: credit-collection-hivedata
 * @File: PhoneBrand.java
 * @Date: 2015年12月1日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum PhoneBrand {

    APPLE {
        @Override
        public String getCode() {
            return "苹果";
        }
    },

    SAMSUNG {
        @Override
        public String getCode() {
            return "三星";
        }
    },

    HTC {
        @Override
        public String getCode() {
            return "HTC";
        }
    },

    SONY {
        @Override
        public String getCode() {
            return "索尼";
        }
    },

    LG {
        @Override
        public String getCode() {
            return "LG";
        }
    },

    NOKIA {
        @Override
        public String getCode() {
            return "诺基亚";
        }
    },

    HUAWEI {
        @Override
        public String getCode() {
            return "华为";
        }
    },

    ZTE {
        @Override
        public String getCode() {
            return "中兴";
        }
    },

    COOLPAD {
        @Override
        public String getCode() {
            return "酷派";
        }
    },

    LENOVO {
        @Override
        public String getCode() {
            return "联想";
        }
    },

    MEIZU {
        @Override
        public String getCode() {
            return "魅族";
        }
    },

    MI {
        @Override
        public String getCode() {
            return "小米";
        }
    },

    OPPO {
        @Override
        public String getCode() {
            return "OPPO";
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
    public static PhoneBrand getType(String brand) {
        if (brand == null || brand.isEmpty()) {
            return null;
        }
        switch (brand) {
            case "苹果":
                return APPLE;
            case "三星":
                return SAMSUNG;
            case "宏达电":
                return HTC;
            case "索尼":
                return SONY;
            case "LG":
                return LG;
            case "诺基亚":
                return NOKIA;
            case "华为":
                return HUAWEI;
            case "中兴":
                return ZTE;
            case "酷派":
                return COOLPAD;
            case "联想":
                return LENOVO;
            case "魅族":
                return MEIZU;
            case "小米":
                return MI;
            case "OPPO":
                return OPPO;
            default:
                return OTHERS;
        }
    }
}
