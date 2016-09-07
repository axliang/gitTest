package com.cmcc.hy.bigdata.weijifen.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * 用户基本信息模型类
 *
 * @Project: credit-collection-hivedata
 * @File: UserBasicInfo.java
 * @Date: 2015年11月15日
 * @Author: JinYibin
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 * @author JinYibin
 *
 */
public class UserBasicInfo implements Writable {
    private int age;
    private String gender;
    private double salary;
    private String uid;
    private String idNumber;
    private String name;
    private String birthday;
    private String address;
    private String job;
    private String accountBalance;
    private String orders;
    private String hobby;
    private String isMarried;
    private String channeled;
    private String authentication;
    private String phoneBrand;
    private String phoneModel;
    private String imei;
    private String imsi;
    private String registrationTime;
    private String userstatus;
    private String is5warn;
    private String registrationType;
    private String blacklistType;
    private String starLevel;
    private String groupType;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getIsMarried() {
        return isMarried;
    }

    public void setIsMarried(String isMarried) {
        this.isMarried = isMarried;
    }

    public String getChanneled() {
        return channeled;
    }

    public void setChanneled(String channeled) {
        this.channeled = channeled;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getPhoneBrand() {
        return phoneBrand;
    }

    public void setPhoneBrand(String phoneBrand) {
        this.phoneBrand = phoneBrand;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus;
    }

    public String getIs5warn() {
        return is5warn;
    }

    public void setIs5warn(String is5warn) {
        this.is5warn = is5warn;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getBlacklistType() {
        return blacklistType;
    }

    public void setBlacklistType(String blacklistType) {
        this.blacklistType = blacklistType;
    }

    public String getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public void setGroupType(boolean isGroup) {
        if (isGroup) {
            this.groupType = "1";
        } else {
            this.groupType = "0";
        }
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.age = in.readInt();
        this.gender = in.readUTF();
        this.salary = in.readDouble();
        this.uid = in.readUTF();
        this.idNumber = in.readUTF();
        this.name = in.readUTF();
        this.birthday = in.readUTF();
        this.address = in.readUTF();
        this.job = in.readUTF();
        this.accountBalance = in.readUTF();
        this.orders = in.readUTF();
        this.hobby = in.readUTF();
        this.isMarried = in.readUTF();
        this.channeled = in.readUTF();
        this.authentication = in.readUTF();
        this.phoneBrand = in.readUTF();
        this.phoneModel = in.readUTF();
        this.imei = in.readUTF();
        this.imsi = in.readUTF();
        this.registrationTime = in.readUTF();
        this.userstatus = in.readUTF();
        this.is5warn = in.readUTF();
        this.registrationType = in.readUTF();
        this.blacklistType = in.readUTF();
        this.starLevel = in.readUTF();
        this.groupType = in.readUTF();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.age);
        out.writeUTF(getStr(this.gender));
        out.writeDouble(this.salary);
        out.writeUTF(getStr(this.uid));
        out.writeUTF(getStr(this.idNumber));
        out.writeUTF(getStr(this.name));
        out.writeUTF(getStr(this.birthday));
        out.writeUTF(getStr(this.address));
        out.writeUTF(getStr(this.job));
        out.writeUTF(getStr(this.accountBalance));
        out.writeUTF(getStr(this.orders));
        out.writeUTF(getStr(this.hobby));
        out.writeUTF(getStr(this.isMarried));
        out.writeUTF(getStr(this.channeled));
        out.writeUTF(getStr(this.authentication));
        out.writeUTF(getStr(this.phoneBrand));
        out.writeUTF(getStr(this.phoneModel));
        out.writeUTF(getStr(this.imei));
        out.writeUTF(getStr(this.imsi));
        out.writeUTF(getStr(this.registrationTime));
        out.writeUTF(getStr(this.userstatus));
        out.writeUTF(getStr(this.is5warn));
        out.writeUTF(getStr(this.registrationType));
        out.writeUTF(getStr(this.blacklistType));
        out.writeUTF(getStr(this.starLevel));
        out.writeUTF(getStr(this.groupType));
    }

    /**
     * 获取写入流的字符串
     * 
     * @param str
     * @return
     */
    private String getStr(String str) {
        return (str == null ? "" : str);
    }
}
