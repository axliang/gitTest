package com.cmcc.hy.bigdata.weijifen.converter;

import com.cmcc.hy.bigdata.weijifen.enums.BusinessType;
import com.cmcc.hy.bigdata.weijifen.enums.CertType;
import com.cmcc.hy.bigdata.weijifen.enums.CommunicationType;
import com.cmcc.hy.bigdata.weijifen.enums.RoamType;
import com.cmcc.hy.bigdata.weijifen.enums.SexType;
import com.cmcc.hy.bigdata.weijifen.enums.UserStatusType;

/**
 * 重庆枚举转换器
 * 
 * @Project: credit-collection-hivedata
 * @File: ChongqingEnumConverter.java
 * @Date: 2016年06月15日
 * @Author: hechan
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public class ChongqingEnumConverter extends EnumConverter {

    @Override
    public CommunicationType getCommunicationType(BusinessType businessType,
                                                  String communicationType) {
        if (businessType == BusinessType.SMS) {
            if ("1".equalsIgnoreCase(communicationType)) {
                return CommunicationType.S;
            } else if ("2".equalsIgnoreCase(communicationType)) {
                return CommunicationType.R;
            } else {
                return CommunicationType.O;
            }
        } else if (businessType == BusinessType.CALL) {
            if ("1".equalsIgnoreCase(communicationType)) {
                return CommunicationType.S;
            } else if ("2".equalsIgnoreCase(communicationType)) {
                return CommunicationType.R;
            } else if ("3".equalsIgnoreCase(communicationType)) {
                return CommunicationType.T;
            } else {
                return CommunicationType.O;
            }
        } else {
            return CommunicationType.UNDEFINED;
        }
    }

    @Override
    public BusinessType getBusinessType(String businessType) {
        if ("SMS".equalsIgnoreCase(businessType)) {
            return BusinessType.SMS;
        } else if ("MMS".equalsIgnoreCase(businessType)) {
            return BusinessType.MMS;
        } else if ("CALL".equalsIgnoreCase(businessType)) {
            return BusinessType.CALL;
        } else {
            return BusinessType.UNDEFINED;
        }
    }

    @Override
    public UserStatusType getUserStatus(String status) {
        if (status == null) {
            return UserStatusType.UNDEFINED;
        }
        status = status.trim();
        if ("19".equals(status)) {
            return UserStatusType.NORMAL_ONLINE;
        } else if ("9".equals(status) || "12".equals(status) || "17".equals(status)
                || "18".equals(status) || "20".equals(status) || "21".equals(status)
                || "22".equals(status) || "24".equals(status) || "25".equals(status)
                || "26".equals(status) || "27".equals(status) || "28".equals(status)) {
            return UserStatusType.OUT_OF_SERVICE;
        } else if ("1".equals(status) || "2".equals(status) || "3".equals(status) || "4".equals(status)
                    || "5".equals(status) || "7".equals(status) || "23".equals(status)) {
            return UserStatusType.LOG_OUT;
        } else if ("8".equals(status) || "10".equals(status) || "11".equals(status)
                || "13".equals(status) || "14".equals(status) || "15".equals(status)
                || "16".equals(status)) {
            return UserStatusType.ARREARAGE;
        } else if ("6".equals(status)) {
            return UserStatusType.OTHER;
        } else {
            return UserStatusType.UNDEFINED;
        }
    }

    @Override
    public SexType getSexType(String sexType) {
        if (sexType == null) {
            return SexType.OTHER;
        }
        sexType = sexType.trim();
        if ("1".equals(sexType) || "先生".equals(sexType)) {
            return SexType.MALE;
        } else if ("2".equals(sexType) || "女士".equals(sexType)) {
            return SexType.FEMALE;
        } else {
            return SexType.OTHER;
        }
    }

    @Override
    public CertType getCertType(String certType) {
        if ("身份证".equals(certType) || "idcard".equalsIgnoreCase(certType)) {
            return CertType.IdCard;
        } else if ("TaiBaoZheng".equalsIgnoreCase(certType)) {
            return CertType.TaiBaoZheng;
        } else if ("PLA".equalsIgnoreCase(certType) || "SoldierID".equalsIgnoreCase(certType)) {
            return CertType.PLA;
        } else if ("PolicePaper".equalsIgnoreCase(certType)) {
            return CertType.PolicePaper;
        } else if ("Passport".equalsIgnoreCase(certType)) {
            return CertType.Passport;
        } else if ("HKIdCard".equalsIgnoreCase(certType)) {
            return CertType.HKIdCard;
        } else if ("HuKouBu".equalsIgnoreCase(certType)) {
            return CertType.HuKouBu;
        } else {
            return CertType.other;
        }
    }

    @Override
    public RoamType getRoamType(String roamType) {
        if (null == roamType || "".equals(roamType)) {
            return RoamType.UNDEFINED;
        }

        if ("1".equals(roamType)) {
            return RoamType.LOCAL;
        } else if ("2".equals(roamType) || "5".equals(roamType)) {
            return RoamType.PROVINCE;
        } else if ("3".equals(roamType)) {
            return RoamType.INTERPROVINCIAL;
        } else if ("4".equals(roamType) || "6".equals(roamType)) {
            return RoamType.INTERNATIONAL;
        } else if ("7".equals(roamType) || "8".equals(roamType)) {
            return RoamType.HONGKONG_MACAO_TAIWAN;
        } else {
            return RoamType.OTHER;
        }
    }

}
