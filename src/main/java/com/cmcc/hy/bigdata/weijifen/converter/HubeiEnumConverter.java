package com.cmcc.hy.bigdata.weijifen.converter;

import com.cmcc.hy.bigdata.weijifen.enums.BlacklistState;
import com.cmcc.hy.bigdata.weijifen.enums.BusinessType;
import com.cmcc.hy.bigdata.weijifen.enums.CertType;
import com.cmcc.hy.bigdata.weijifen.enums.CommunicationType;
import com.cmcc.hy.bigdata.weijifen.enums.ListType;
import com.cmcc.hy.bigdata.weijifen.enums.PhoneBrand;
import com.cmcc.hy.bigdata.weijifen.enums.PhoneSystem;
import com.cmcc.hy.bigdata.weijifen.enums.RoamType;
import com.cmcc.hy.bigdata.weijifen.enums.SexType;
import com.cmcc.hy.bigdata.weijifen.enums.UserStatusType;

/**
 * 湖北枚举转换器
 * 
 * @Project: credit-collection-hivedata
 * @File: HubeiEnumConverter.java
 * @Date: 2015年12月08日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public class HubeiEnumConverter extends EnumConverter {

    @Override
    public CommunicationType getCommunicationType(BusinessType businessType,
                                                  String communicationType) {
        if (businessType == BusinessType.SMS) {
            if ("10".equalsIgnoreCase(communicationType)) {
                return CommunicationType.S;
            } else if ("11".equalsIgnoreCase(communicationType)) {
                return CommunicationType.R;
            } else if ("12".equalsIgnoreCase(communicationType)) {
                return CommunicationType.T;
            } else {
                return CommunicationType.O;
            }
        } else if (businessType == BusinessType.MMS) {
            if ("10".equalsIgnoreCase(communicationType)) {
                return CommunicationType.S;
            } else {
                return CommunicationType.R;
            }
        } else if (businessType == BusinessType.CALL) {
            if ("10".equalsIgnoreCase(communicationType)) {
                return CommunicationType.S;
            } else if ("11".equalsIgnoreCase(communicationType)) {
                return CommunicationType.R;
            } else if ("12".equalsIgnoreCase(communicationType)) {
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
        if ("US10".equals(status)) {
            return UserStatusType.NORMAL_ONLINE;
        } else if ("US344".equals(status) || "US40".equals(status) || "US534".equals(status)
                || "US536".equals(status) || "US53".equals(status) || "US301".equals(status)
                || "US302".equals(status) || "US42".equals(status) || "US50".equals(status)
                || "US30".equals(status)) {
            return UserStatusType.OUT_OF_SERVICE;
        } else if ("US24".equals(status) || "US23".equals(status) || "US531".equals(status)
                || "US20".equals(status) || "US22".equals(status) || "US528".equals(status)
                || "US537".equals(status) || "US221".equals(status)) {
            return UserStatusType.LOG_OUT;
        } else if ("US62".equals(status) || "US61".equals(status) || "US346".equals(status)
                || "US26".equals(status) || "US532".equals(status) || "US347".equals(status)
                || "US348".equals(status) || "US539".equals(status) || "US2".equals(status)
                || "US27".equals(status) || "US3".equals(status) || "US34".equals(status)
                || "US64".equals(status) || "US63".equals(status) || "US28".equals(status)
                || "US21".equals(status) || "US25".equals(status)) {
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
        if ("1".equals(sexType)) {
            return SexType.MALE;
        } else if ("0".equals(sexType)) {
            return SexType.FEMALE;
        } else {
            return SexType.OTHER;
        }
    }

    @Override
    public CertType getCertType(String certType) {
        if ("IdCard".equals(certType)) {
            return CertType.IdCard;
        } else if ("TaiBaoZheng".equals(certType)) {
            return CertType.TaiBaoZheng;
        } else if ("PLA".equals(certType)) {
            return CertType.PLA;
        } else if ("PolicePaper".equals(certType)) {
            return CertType.PolicePaper;
        } else if ("Passport".equals(certType)) {
            return CertType.Passport;
        } else if ("HKIdCard".equals(certType)) {
            return CertType.HKIdCard;
        } else if ("HuKouBu".equals(certType)) {
            return CertType.HuKouBu;
        } else {
            return CertType.other;
        }
    }

    @Override
    public RoamType getRoamType(String roamType) {
        if ("10".equals(roamType)) {
            return RoamType.LOCAL;
        } else if ("11".equals(roamType) || "14".equals(roamType)) {
            return RoamType.PROVINCE;
        } else if ("12".equals(roamType) || "13".equals(roamType) || "15".equals(roamType)
                || "16".equals(roamType)) {
            return RoamType.INTERPROVINCIAL;
        } else if ("17".equals(roamType) || "18".equals(roamType) || "22".equals(roamType)) {
            return RoamType.INTERNATIONAL;
        } else if ("19".equals(roamType) || "20".equals(roamType) || "21".equals(roamType)) {
            return RoamType.HONGKONG_MACAO_TAIWAN;
        } else if ("99".equals(roamType)) {
            return RoamType.OTHER;
        } else {
            return RoamType.UNDEFINED;
        }
    }

    @Override
    public BlacklistState getBlacklistState(String state) {
        if ("BLS00".equalsIgnoreCase(state)) {
            return BlacklistState.ENTER;
        } else if ("BLS01".equalsIgnoreCase(state)) {
            return BlacklistState.EXIT;
        } else {
            return BlacklistState.UNDEFINED;
        }
    }

    public PhoneSystem getPhoneSystem(String system) {
        if (system == null || system.isEmpty()) {
            return null;
        }
        if (system.toLowerCase().contains("ios")) {
            return PhoneSystem.IOS;
        } else if (system.contains("安卓") || system.toLowerCase().contains("android")) {
            return PhoneSystem.ANDROID;
        } else if (system.toLowerCase().contains("windows phone")
                || system.toLowerCase().contains("wp")) {
            return PhoneSystem.WINDOWS_PHONE;
        } else if (system.contains("塞班") || system.toLowerCase().contains("symbian")) {
            return PhoneSystem.SYMBIAN;
        } else {
            return PhoneSystem.OTHERS;
        }
    }

    public PhoneBrand getPhoneBrand(String brand) {
        if (brand == null || brand.isEmpty()) {
            return null;
        }
        if (brand.contains("苹果") || brand.toLowerCase().contains("apple")) {
            return PhoneBrand.APPLE;
        } else if (brand.contains("三星") || brand.toLowerCase().contains("samsung")) {
            return PhoneBrand.SAMSUNG;
        } else if (brand.contains("宏达电") || brand.toLowerCase().contains("htc")) {
            return PhoneBrand.HTC;
        } else if (brand.contains("索尼") || brand.toLowerCase().contains("sony")) {
            return PhoneBrand.SONY;
        } else if (brand.toLowerCase().contains("lg")) {
            return PhoneBrand.LG;
        } else if (brand.contains("诺基亚") || brand.toLowerCase().contains("nokia")) {
            return PhoneBrand.NOKIA;
        } else if (brand.contains("华为") || brand.toLowerCase().contains("huawei")) {
            return PhoneBrand.HUAWEI;
        } else if (brand.contains("中兴") || brand.toLowerCase().contains("zte")) {
            return PhoneBrand.ZTE;
        } else if (brand.contains("酷派") || brand.toLowerCase().contains("coolpad")) {
            return PhoneBrand.COOLPAD;
        } else if (brand.contains("联想") || brand.toLowerCase().contains("lenovo")) {
            return PhoneBrand.LENOVO;
        } else if (brand.contains("魅族") || brand.toLowerCase().contains("meizu")) {
            return PhoneBrand.MEIZU;
        } else if (brand.contains("小米") || brand.toLowerCase().contains("mi")) {
            return PhoneBrand.MI;
        } else if (brand.toLowerCase().contains("oppo")) {
            return PhoneBrand.OPPO;
        } else {
            return PhoneBrand.OTHERS;
        }
    }

    public ListType getListType(String type) {
        if ("GREY".equalsIgnoreCase(type)) {
            return ListType.GREY;
        } else if ("BLACK".equalsIgnoreCase(type)) {
            return ListType.BLACK;
        } else {
            return ListType.UNDEFINED;
        }

    }
}
