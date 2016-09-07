package com.cmcc.hy.bigdata.weijifen.converter;

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
public class GuizhouEnumConverter extends EnumConverter {

    @Override
    public CommunicationType getCommunicationType(BusinessType businessType,
                                                  String communicationType) {
        if (businessType == BusinessType.CALL) {
            if ("1".equalsIgnoreCase(communicationType)) {
                return CommunicationType.S;
            } else if ("2".equalsIgnoreCase(communicationType)) {
                return CommunicationType.R;
            } else if ("3".equalsIgnoreCase(communicationType)) {
                return CommunicationType.T;
            } else {
                return CommunicationType.O;
            }
        } else if (businessType == BusinessType.SMS || businessType == BusinessType.MMS) {
            if ("1".equalsIgnoreCase(communicationType)) {
                return CommunicationType.S;
            } else if ("2".equalsIgnoreCase(communicationType)) {
                return CommunicationType.R;
            } else {
                return CommunicationType.O;
            }
        } else {
            return CommunicationType.UNDEFINED;
        }
    }

    @Override
    public BusinessType getBusinessType(String businessType) {
        return BusinessType.UNDEFINED;
    }

    @Override
    public UserStatusType getUserStatus(String status) {
        if (status == null || status.isEmpty() || "0".equals(status)) {
            return UserStatusType.UNDEFINED;
        }
        status = status.trim();
        if ("1".equals(status)) {
            return UserStatusType.NORMAL_ONLINE;
        } else if ("5".equals(status) || "6".equals(status) || "11".equals(status)
                || "12".equals(status)) {
            return UserStatusType.OUT_OF_SERVICE;
        } else if ("2".equals(status) || "3".equals(status) || "4".equals(status)) {
            return UserStatusType.LOG_OUT;
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
        if ("1".equalsIgnoreCase(sexType)) {
            return SexType.MALE;
        } else if ("2".equalsIgnoreCase(sexType)) {
            return SexType.FEMALE;
        } else {
            return SexType.OTHER;
        }
    }

    @Override
    public CertType getCertType(String certtype) {
        if ("1".equals(certtype)) {
            return CertType.IdCard;
        } else if ("4".equals(certtype)) {
            return CertType.PLA;
        } else if ("5".equals(certtype)) {
            return CertType.PolicePaper;
        } else if ("2".equals(certtype)) {
            return CertType.Passport;
        } else if ("9".equals(certtype)) {
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
        } else if ("2".equals(roamType) || "7".equals(roamType)) {
            return RoamType.PROVINCE;
        } else if ("3".equals(roamType) || "4".equals(roamType) || "8".equals(roamType)
                || "9".equals(roamType)) {
            return RoamType.INTERPROVINCIAL;
        } else if ("5".equals(roamType) || "6".equals(roamType)) {
            return RoamType.INTERNATIONAL;
        } else if ("10".equals(roamType) || "11".equals(roamType)) {
            return RoamType.HONGKONG_MACAO_TAIWAN;
        } else {
            return RoamType.OTHER;
        }
    }
}
