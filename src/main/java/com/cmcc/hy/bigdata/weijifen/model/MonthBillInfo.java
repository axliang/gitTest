package com.cmcc.hy.bigdata.weijifen.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * 用户月账单信息
 *
 * @Project: credit-collection-hivedata
 * @File: MonthBillInfo.java
 * @Date: 2015年12月10日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public class MonthBillInfo implements Writable {

    private String phoneNo;
    private String acctNo;
    private String totalFee;
    private String voiceFee;
    private String totalDur;
    private String locCallCnt;
    private String locCallDur;
    private String nationRoamCnt;
    private String nationRoamDur;
    private String interRoamCnt;
    private String interRoamDur;
    private String phoneRoamChargeKb;
    private String phoneInterRoamChargeKb;

    public MonthBillInfo() {
    }

    public MonthBillInfo(MonthBillInfo obj) {
        this.phoneNo = obj.phoneNo;
        this.acctNo = obj.acctNo;
        this.totalFee = obj.totalFee;
        this.voiceFee = obj.voiceFee;
        this.totalDur = obj.totalDur;
        this.locCallCnt = obj.locCallCnt;
        this.locCallDur = obj.locCallDur;
        this.nationRoamCnt = obj.nationRoamCnt;
        this.nationRoamDur = obj.nationRoamDur;
        this.interRoamCnt = obj.interRoamCnt;
        this.interRoamDur = obj.interRoamDur;
        this.phoneRoamChargeKb = obj.phoneRoamChargeKb;
        this.phoneInterRoamChargeKb = obj.phoneInterRoamChargeKb;
    }

    /**
     * 初始化
     */
    public void init() {
        this.phoneNo = null;
        this.acctNo = null;
        this.totalFee = null;
        this.voiceFee = null;
        this.totalDur = null;
        this.locCallCnt = null;
        this.locCallDur = null;
        this.nationRoamCnt = null;
        this.nationRoamDur = null;
        this.interRoamCnt = null;
        this.interRoamDur = null;
        this.phoneRoamChargeKb = null;
        this.phoneInterRoamChargeKb = null;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getVoiceFee() {
        return voiceFee;
    }

    public void setVoiceFee(String voiceFee) {
        this.voiceFee = voiceFee;
    }

    public String getTotalDur() {
        return totalDur;
    }

    public void setTotalDur(String totalDur) {
        this.totalDur = totalDur;
    }

    public String getLocCallCnt() {
        return locCallCnt;
    }

    public void setLocCallCnt(String locCallCnt) {
        this.locCallCnt = locCallCnt;
    }

    public String getLocCallDur() {
        return locCallDur;
    }

    public void setLocCallDur(String locCallDur) {
        this.locCallDur = locCallDur;
    }

    public String getNationRoamCnt() {
        return nationRoamCnt;
    }

    public void setNationRoamCnt(String nationRoamCnt) {
        this.nationRoamCnt = nationRoamCnt;
    }

    public String getNationRoamDur() {
        return nationRoamDur;
    }

    public void setNationRoamDur(String nationRoamDur) {
        this.nationRoamDur = nationRoamDur;
    }

    public String getInterRoamCnt() {
        return interRoamCnt;
    }

    public void setInterRoamCnt(String interRoamCnt) {
        this.interRoamCnt = interRoamCnt;
    }

    public String getInterRoamDur() {
        return interRoamDur;
    }

    public void setInterRoamDur(String interRoamDur) {
        this.interRoamDur = interRoamDur;
    }

    public String getPhoneRoamChargeKb() {
        return phoneRoamChargeKb;
    }

    public void setPhoneRoamChargeKb(String phoneRoamChargeKb) {
        this.phoneRoamChargeKb = phoneRoamChargeKb;
    }

    public String getPhoneInterRoamChargeKb() {
        return phoneInterRoamChargeKb;
    }

    public void setPhoneInterRoamChargeKb(String phoneInterRoamChargeKb) {
        this.phoneInterRoamChargeKb = phoneInterRoamChargeKb;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(getStr(phoneNo));
        out.writeUTF(getStr(acctNo));
        out.writeUTF(getStr(totalFee));
        out.writeUTF(getStr(voiceFee));
        out.writeUTF(getStr(totalDur));
        out.writeUTF(getStr(locCallCnt));
        out.writeUTF(getStr(locCallDur));
        out.writeUTF(getStr(nationRoamCnt));
        out.writeUTF(getStr(nationRoamDur));
        out.writeUTF(getStr(interRoamCnt));
        out.writeUTF(getStr(interRoamDur));
        out.writeUTF(getStr(phoneRoamChargeKb));
        out.writeUTF(getStr(phoneInterRoamChargeKb));
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.phoneNo = in.readUTF();
        this.acctNo = in.readUTF();
        this.totalFee = in.readUTF();
        this.voiceFee = in.readUTF();
        this.totalDur = in.readUTF();
        this.locCallCnt = in.readUTF();
        this.locCallDur = in.readUTF();
        this.nationRoamCnt = in.readUTF();
        this.nationRoamDur = in.readUTF();
        this.interRoamCnt = in.readUTF();
        this.interRoamDur = in.readUTF();
        this.phoneRoamChargeKb = in.readUTF();
        this.phoneInterRoamChargeKb = in.readUTF();
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
