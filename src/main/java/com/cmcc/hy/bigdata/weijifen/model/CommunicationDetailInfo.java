package com.cmcc.hy.bigdata.weijifen.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import com.cmcc.hy.bigdata.weijifen.enums.BusinessType;
import com.cmcc.hy.bigdata.weijifen.enums.CommunicationType;
import com.cmcc.hy.bigdata.weijifen.enums.RoamType;

/**
 * 话单对象
 * 
 */
public class CommunicationDetailInfo implements Writable {

    private BusinessType businessType;
    private CommunicationType communicationType;
    private String otherPhoneNumber;
    private String otherName;
    private String otherCity;
    private String otherCityNo;
    private String startTime;
    private double chargeInfo;
    private String roamPlace;
    private String roamPlaceNo;
    private RoamType roamType;

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(CommunicationType communicationType) {
        this.communicationType = communicationType;
    }

    public String getOtherPhoneNumber() {
        return otherPhoneNumber;
    }

    public void setOtherPhoneNumber(String otherPhoneNumber) {
        this.otherPhoneNumber = otherPhoneNumber;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getOtherCity() {
        return otherCity;
    }

    public void setOtherCity(String otherCity) {
        this.otherCity = otherCity;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public double getChargeInfo() {
        return chargeInfo;
    }

    public void setChargeInfo(double chargeInfo) {
        this.chargeInfo = chargeInfo;
    }

    public String getRoamPlace() {
        return roamPlace;
    }

    public void setRoamPlace(String roamPlace) {
        this.roamPlace = roamPlace;
    }

    public RoamType getRoamType() {
        return roamType;
    }

    public void setRoamType(RoamType roamType) {
        this.roamType = roamType;
    }

    public String getOtherCityNo() {
        return otherCityNo;
    }

    public void setOtherCityNo(String otherCityNo) {
        this.otherCityNo = otherCityNo;
    }

    public String getRoamPlaceNo() {
        return roamPlaceNo;
    }

    public void setRoamPlaceNo(String roamPlaceNo) {
        this.roamPlaceNo = roamPlaceNo;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.businessType == null ? "" : this.businessType.name());
        out.writeUTF(this.communicationType == null ? "" : this.communicationType.name());
        out.writeUTF(getStr(this.otherPhoneNumber));
        out.writeUTF(getStr(this.otherName));
        out.writeUTF(getStr(this.otherCity));
        out.writeUTF(getStr(this.otherCityNo));
        out.writeUTF(getStr(this.startTime));
        out.writeDouble(this.chargeInfo);
        out.writeUTF(getStr(this.roamPlace));
        out.writeUTF(getStr(this.roamPlaceNo));
        out.writeUTF(this.roamType == null ? "" : this.roamType.name());
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.businessType = BusinessType.valueOf(in.readUTF());
        this.communicationType = CommunicationType.valueOf(in.readUTF());
        this.otherPhoneNumber = in.readUTF();
        this.otherName = in.readUTF();
        this.otherCity = in.readUTF();
        this.otherCityNo = in.readUTF();
        this.startTime = in.readUTF();
        this.chargeInfo = in.readDouble();
        this.roamPlace = in.readUTF();
        this.roamPlaceNo = in.readUTF();
        this.roamType = RoamType.valueOf(in.readUTF());
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
