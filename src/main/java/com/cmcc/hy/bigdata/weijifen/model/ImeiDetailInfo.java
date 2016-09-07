package com.cmcc.hy.bigdata.weijifen.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * 终端详细信息对象
 * 
 */
public class ImeiDetailInfo implements Writable {

    private String imei;
    private String phoneNo;
    private String phoneBrand;
    private String phoneModel;
    private String phoneSystem;
    private String firstTime;
    private String lastTime;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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

    public String getPhoneSystem() {
        return phoneSystem;
    }

    public void setPhoneSystem(String phoneSystem) {
        this.phoneSystem = phoneSystem;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.imei == null ? "" : this.imei);
        out.writeUTF(this.phoneNo == null ? "" : this.phoneNo);
        out.writeUTF(this.phoneBrand == null ? "" : this.phoneBrand);
        out.writeUTF(this.phoneModel == null ? "" : this.phoneModel);
        out.writeUTF(this.phoneSystem == null ? "" : this.phoneSystem);
        out.writeUTF(this.firstTime == null ? "" : this.firstTime);
        out.writeUTF(this.lastTime == null ? "" : this.lastTime);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.imei = in.readUTF();
        this.phoneNo = in.readUTF();
        this.phoneBrand = in.readUTF();
        this.phoneModel = in.readUTF();
        this.phoneSystem = in.readUTF();
        this.firstTime = in.readUTF();
        this.lastTime = in.readUTF();
    }
}
