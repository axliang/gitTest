package com.cmcc.hy.bigdata.weijifen.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * 用户信息和用户账单信息混合对象 
 *
 */
public class UserMonthInfo implements Writable {

    private String phoneNo;
    private String factFee;
    // user info
    private String age;
    private String sex;
    private String name;
    private String birthday;
    private String planId;
    private String termBrand;
    private String termModel;
    private String userOpentime;
    private String userStatus;
    // user bill
    private String opTime;
    private String localCallFee;
    private String tollFee;
    private String localOutCallDur;
    private String localIntCallDur;
    private String tollDur;
    private String snRoamDur;
    private String sjRoamDur;
    private String gjRoamDur;
    private String gprsFlows;
    // star level
    private String starLevel;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getFactFee() {
        return factFee;
    }

    public void setFactFee(String factFee) {
        this.factFee = factFee;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getTermBrand() {
        return termBrand;
    }

    public void setTermBrand(String termBrand) {
        this.termBrand = termBrand;
    }

    public String getTermModel() {
        return termModel;
    }

    public void setTermModel(String termModel) {
        this.termModel = termModel;
    }

    public String getUserOpentime() {
        return userOpentime;
    }

    public void setUserOpentime(String userOpentime) {
        this.userOpentime = userOpentime;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getOpTime() {
        return opTime;
    }

    public void setOpTime(String opTime) {
        this.opTime = opTime;
    }

    public String getLocalCallFee() {
        return localCallFee;
    }

    public void setLocalCallFee(String localCallFee) {
        this.localCallFee = localCallFee;
    }

    public String getTollFee() {
        return tollFee;
    }

    public void setTollFee(String tollFee) {
        this.tollFee = tollFee;
    }

    public String getLocalOutCallDur() {
        return localOutCallDur;
    }

    public void setLocalOutCallDur(String localOutCallDur) {
        this.localOutCallDur = localOutCallDur;
    }

    public String getLocalIntCallDur() {
        return localIntCallDur;
    }

    public void setLocalIntCallDur(String localIntCallDur) {
        this.localIntCallDur = localIntCallDur;
    }

    public String getTollDur() {
        return tollDur;
    }

    public void setTollDur(String tollDur) {
        this.tollDur = tollDur;
    }

    public String getSnRoamDur() {
        return snRoamDur;
    }

    public void setSnRoamDur(String snRoamDur) {
        this.snRoamDur = snRoamDur;
    }

    public String getSjRoamDur() {
        return sjRoamDur;
    }

    public void setSjRoamDur(String sjRoamDur) {
        this.sjRoamDur = sjRoamDur;
    }

    public String getGjRoamDur() {
        return gjRoamDur;
    }

    public void setGjRoamDur(String gjRoamDur) {
        this.gjRoamDur = gjRoamDur;
    }

    public String getGprsFlows() {
        return gprsFlows;
    }

    public void setGprsFlows(String gprsFlows) {
        this.gprsFlows = gprsFlows;
    }

    public String getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.phoneNo == null ? "" : this.phoneNo);
        out.writeUTF(this.factFee == null ? "" : this.factFee);
        out.writeUTF(this.age == null ? "" : this.age);
        out.writeUTF(this.sex == null ? "" : this.sex);
        out.writeUTF(this.name == null ? "" : this.name);
        out.writeUTF(this.birthday == null ? "" : this.birthday);
        out.writeUTF(this.planId == null ? "" : this.planId);
        out.writeUTF(this.termBrand == null ? "" : this.termBrand);
        out.writeUTF(this.termModel == null ? "" : this.termModel);
        out.writeUTF(this.userOpentime == null ? "" : this.userOpentime);
        out.writeUTF(this.userStatus == null ? "" : this.userStatus);
        out.writeUTF(this.opTime == null ? "" : this.opTime);
        out.writeUTF(this.localCallFee == null ? "" : this.localCallFee);
        out.writeUTF(this.tollFee == null ? "" : this.tollFee);
        out.writeUTF(this.localOutCallDur == null ? "" : this.localOutCallDur);
        out.writeUTF(this.localIntCallDur == null ? "" : this.localIntCallDur);
        out.writeUTF(this.tollDur == null ? "" : this.tollDur);
        out.writeUTF(this.snRoamDur == null ? "" : this.snRoamDur);
        out.writeUTF(this.sjRoamDur == null ? "" : this.sjRoamDur);
        out.writeUTF(this.gjRoamDur == null ? "" : this.gjRoamDur);
        out.writeUTF(this.gprsFlows == null ? "" : this.gprsFlows);
        out.writeUTF(this.starLevel == null ? "" : this.starLevel);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.phoneNo = in.readUTF();
        this.factFee = in.readUTF();
        this.age = in.readUTF();
        this.sex = in.readUTF();
        this.name = in.readUTF();
        this.birthday = in.readUTF();
        this.planId = in.readUTF();
        this.termBrand = in.readUTF();
        this.termModel = in.readUTF();
        this.userOpentime = in.readUTF();
        this.userStatus = in.readUTF();
        this.opTime = in.readUTF();
        this.localCallFee = in.readUTF();
        this.tollFee = in.readUTF();
        this.localOutCallDur = in.readUTF();
        this.localIntCallDur = in.readUTF();
        this.tollDur = in.readUTF();
        this.snRoamDur = in.readUTF();
        this.sjRoamDur = in.readUTF();
        this.gjRoamDur = in.readUTF();
        this.gprsFlows = in.readUTF();
        this.starLevel = in.readUTF();
    }

}
