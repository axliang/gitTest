package com.cmcc.hy.bigdata.weijifen.enums;

import com.cmcc.hy.bigdata.weijifen.util.StringUtil;

/**
 * 城市类型枚举类
 * 
 * @author JinYibin
 *
 */
public enum CityType {
    /**
     * 直辖市
     */
    MUNICIPALITY,
    /**
     * 港澳台
     */
    HONGKONG_MACAO_TAIWAN,
    /**
     * 其它
     */
    OTHER,
    /**
     * 未定义
     */
    UNDEFINED;

    //城市划分
    public static CityType getTypeByCityName(String str) {
        if (StringUtil.strIsNull(str)) {
            return UNDEFINED;
        }
        if (str.contains("北京") || str.contains("上海") || str.contains("天津") || str.contains("重庆")) {
            return MUNICIPALITY;
        }
        if (str.contains("香港") || str.contains("澳门") || str.contains("台湾") || str.contains("台北")) {
            return HONGKONG_MACAO_TAIWAN;
        }
        return OTHER;
    }
}
