package com.cmcc.hy.bigdata.weijifen.converter;

import com.cmcc.hy.bigdata.weijifen.enums.AuthenticationType;
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
 * 枚举转换器
 * 
 * 相同的枚举转换可以定义在此类，不同的转换请定义抽象方法并于具体子类中实现
 * 
 * @Project: credit-collection-hivedata
 * @File: EnumConverter.java
 * @Date: 2015年12月08日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public abstract class EnumConverter {
    // 获取通信类型
    public abstract CommunicationType getCommunicationType(BusinessType businessType,
                                                           String communicationType);

    // 获取业务类型
    public abstract BusinessType getBusinessType(String businessType);

    /**
     * 将不同省公司的用户状态归一化成统一的用户状态
     * 
     * @param status
     *            各省公司不同的用户状态表示
     * @return 归一化后的用户状态
     */
    public abstract UserStatusType getUserStatus(String status);

    /**
     * 将不同省公司的性别表示归一化成统一的性别
     * 
     * @param sexType
     *            各省公司不同的性别表示
     * @return 归一化后的性别
     */
    public abstract SexType getSexType(String sexType);

    /**
     * 将不同省公司的证件类型归一化成统一的证件类型
     * 
     * @param certType
     *            各省公司不同的证件类型
     * @return 归一化后的证件类型
     */
    public abstract CertType getCertType(String certType);

    /**
     * 将不同省公司的漫游类型归一化成统一的漫游类型
     * 
     * @param roamType
     *            各省公司不同的漫游类型
     * @return 归一化后的漫游类型
     */
    public abstract RoamType getRoamType(String roamType);

    /**
     * 将不同省公司的实名认证值归一化成统一的认证值
     * (上海)
     * 
     * @param authenticationType
     *            各省公司不同的实名认证
     * @return 归一化后的实名认证值
     */
    public AuthenticationType getAuthenticationType(String authenticationType) {
        return AuthenticationType.UNDEFINED;
    }

    /**
     * 将省公司的黑名单变化状态归一化成统一的黑名单变化状态
     * (湖北)
     * 
     *            各省公司不同的黑名单变化状态
     * @return 归一化后的黑名单变化状态
     */
    public BlacklistState getBlacklistState(String state) {
        return BlacklistState.UNDEFINED;
    }

    /**
     * 将省公司的名单值(黑名单、灰名单)归一化成统一的名单值
     * (湖北)
     * 
     * @param type
     *            各省公司不同的名单值
     * @return 归一化后的名单值
     */
    public ListType getListType(String type) {
        return ListType.UNDEFINED;
    }

    /**
     * 将省公司的不同的手机操作系统归一化成统一的操作系统
     * (湖北)
     * 
     * @param system
     *            各省公司不同的手机操作系统
     * @return 归一化后的手机操作系统值
     */
    public PhoneSystem getPhoneSystem(String system) {
        return PhoneSystem.OTHERS;
    }

    /**
     * 将省公司的不同的手机品牌归一化成统一的手机品牌
     * (湖北)
     * 
     * @param brand
     *            各省公司不同的手机品牌
     * @return 归一化后的手机品牌
     */
    public PhoneBrand getPhoneBrand(String brand) {
        return PhoneBrand.OTHERS;
    }
}
