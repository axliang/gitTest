package com.cmcc.hy.bigdata.weijifen.converter;

import com.cmcc.hy.bigdata.weijifen.enums.AuthenticationType;
import com.cmcc.hy.bigdata.weijifen.enums.BusinessType;
import com.cmcc.hy.bigdata.weijifen.enums.CertType;
import com.cmcc.hy.bigdata.weijifen.enums.CommunicationType;
import com.cmcc.hy.bigdata.weijifen.enums.RoamType;
import com.cmcc.hy.bigdata.weijifen.enums.SexType;
import com.cmcc.hy.bigdata.weijifen.enums.UserStatusType;

/**
 * 上海枚举转换器
 * 
 * @Project: credit-collection-hivedata
 * @File: ShanghaiEnumConverter.java
 * @Date: 2015年12月08日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public class ShanghaiEnumConverter extends EnumConverter {

    @Override
    public CommunicationType getCommunicationType(BusinessType businessType,
                                                  String communicationType) {
        if (businessType == BusinessType.CALL) {
            if ("0".equalsIgnoreCase(communicationType)) {
                return CommunicationType.R;
            } else if ("1".equalsIgnoreCase(communicationType)) {
                return CommunicationType.S;
            } else {
                return CommunicationType.UNDEFINED;
            }
        } else if (businessType == BusinessType.SMS || businessType == BusinessType.MMS) {
            if ("0".equalsIgnoreCase(communicationType)) {
                return CommunicationType.S;
            } else if ("1".equalsIgnoreCase(communicationType)) {
                return CommunicationType.R;
            } else {
                return CommunicationType.UNDEFINED;
            }
        } else {
            return CommunicationType.UNDEFINED;
        }
    }

    @Override
    public BusinessType getBusinessType(String businessType) {
        if ("0".equals(businessType)) {
            return BusinessType.SMS;
        } else if ("1".equals(businessType)) {
            return BusinessType.MMS;
        } else {
            return BusinessType.UNDEFINED;
        }
    }

    @Override
    public UserStatusType getUserStatus(String status) {
        if (status == null || status.isEmpty() || "0".equals(status)) {
            return UserStatusType.UNDEFINED;
        }
        status = status.trim();
        if ("100".equals(status)) {
            return UserStatusType.NORMAL_ONLINE;
        } else if ("101".equals(status) || "109".equals(status) || "111".equals(status)
                || "112".equals(status) || "114".equals(status)) {
            return UserStatusType.OUT_OF_SERVICE;
        } else if ("104".equals(status) || "105".equals(status)) {
            return UserStatusType.LOG_OUT;
        } else if ("103".equals(status) || "113".equals(status)) {
            return UserStatusType.UNACTIVE;
        } else {
            return UserStatusType.OTHER;
        }
    }

    @Override
    public SexType getSexType(String type) {
        if (type == null) {
            return SexType.OTHER;
        }
        type = type.trim();
        if ("男".equalsIgnoreCase(type)) {
            return SexType.MALE;
        } else if ("女".equalsIgnoreCase(type)) {
            return SexType.FEMALE;
        } else {
            return SexType.OTHER;
        }
    }

    @Override
    public CertType getCertType(String type) {
        if ("身份证".equals(type)) {
            return CertType.IdCard;
        } else if ("台胞证".equals(type)) {
            return CertType.TaiBaoZheng;
        } else if ("军人证".equals(type)) {
            return CertType.PLA;
        } else if ("护照".equals(type)) {
            return CertType.Passport;
        } else if ("港澳证".equals(type)) {
            return CertType.HKIdCard;
        } else {
            return CertType.other;
        }
    }

    @Override
    public RoamType getRoamType(String type) {
        return RoamType.LOCAL;
    }

    @Override
    public AuthenticationType getAuthenticationType(String type) {
        if (type == null) {
            return AuthenticationType.UNDEFINED;
        }
        type = type.trim();
        if ("1".equals(type)) {
            return AuthenticationType.PRE_VALIDATION;
        } else if ("2".equals(type)) {
            return AuthenticationType.VALIDATION;
        } else {
            return AuthenticationType.NO_VALIDATION;
        }
    }
}
