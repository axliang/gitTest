package com.cmcc.hy.bigdata.weijifen.enums;

/**
 * 证件类型
 * 
 * @Project: credit-collection-hivedata
 * @File: CertType.java
 * @Date: 2015年11月10日
 * @author JinYibin
 * 
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum CertType {
    /**
     * 身份证
     */
    IdCard {
        @Override
        public String getCode() {
            return "身份证";
        }
    },
    /**
     * 台胞证
     */
    TaiBaoZheng {
        @Override
        public String getCode() {
            return "台胞证";
        }
    },
    /**
     * 军人证
     */
    PLA {
        @Override
        public String getCode() {
            return "军人证";
        }
    },
    /**
     * 警察证
     */
    PolicePaper {
        @Override
        public String getCode() {
            return "警察证";
        }
    },
    /**
     * 护照
     */
    Passport {
        @Override
        public String getCode() {
            return "护照";
        }
    },
    /**
     * 港澳证
     */
    HKIdCard {
        @Override
        public String getCode() {
            return "港澳证";
        }
    },
    /**
     * 户口薄
     */
    HuKouBu {
        @Override
        public String getCode() {
            return "户口薄";
        }
    },
    /**
     * 其它
     */
    other {
        @Override
        public String getCode() {
            return "其他";
        }
    };

    // 获取标准映射代码，与存入hbase的值对应
    public abstract String getCode();

    //输入code,映射到相应的枚举类型
    public static CertType getType(String certType) {
        if (certType == null || "".equals(certType)) {
            return null;
        }
        certType = certType.trim();
        switch (certType) {
            case "身份证":
                return IdCard;
            case "台胞证":
                return TaiBaoZheng;
            case "军人证":
                return PLA;
            case "警察证":
                return PolicePaper;
            case "护照":
                return Passport;
            case "港澳证":
                return HKIdCard;
            case "户口簿":
                return HuKouBu;
            default:
                return other;
        }
    }
}
