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
public class ScoreInfo implements Writable {

    private String ScoreCycle;
    private String PhoneNo;
    private String EnttId;
    private String ScoreType;
    private String ScoreBalance;
    private String StatMonth;
    private String StatDate;
   

    public String getScoreCycle() {
        return ScoreCycle;
    }

    public void setScoreCycle(String scoreCycle) {
        ScoreCycle = scoreCycle;
    }

    public String getEnttId() {
        return EnttId;
    }

    public void setEnttId(String enttId) {
        EnttId = enttId;
    }

    public String getScoreType() {
        return ScoreType;
    }

    public void setScoreType(String scoreType) {
        ScoreType = scoreType;
    }

    public String getScoreBalance() {
        return ScoreBalance;
    }

    public void setScoreBalance(String scoreBalance) {
        ScoreBalance = scoreBalance;
    }

    public String getStatMonth() {
        return StatMonth;
    }

    public void setStatMonth(String statMonth) {
        StatMonth = statMonth;
    }

    public String getStatDate() {
        return StatDate;
    }

    public void setStatDate(String statDate) {
        StatDate = statDate;
    }
    
    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public ScoreInfo() {
    }

    public ScoreInfo(ScoreInfo obj) {
        this.ScoreCycle = obj.ScoreCycle;
        this.EnttId = obj.EnttId;
        this.ScoreType = obj.ScoreType;
        this.ScoreBalance = obj.ScoreBalance;
        this.StatMonth = obj.StatMonth;
        this.StatDate = obj.StatDate;
        this.PhoneNo = obj.PhoneNo;
     

    }

    /**
     * 初始化
     */
    public void init() {
        this.ScoreCycle = null;
        this.EnttId = null;
        this.ScoreType = null;
        this.ScoreBalance = null;
        this.StatMonth = null;
        this.StatDate = null;
        this.PhoneNo = null;
       
    }


    @Override
    public void write(DataOutput out) throws IOException {
       
        out.writeUTF(getStr(ScoreCycle));
        out.writeUTF(getStr(EnttId));
        out.writeUTF(getStr(ScoreType));
        out.writeUTF(getStr(ScoreBalance));
        out.writeUTF(getStr(StatMonth));
        out.writeUTF(getStr(StatDate));
        out.writeUTF(getStr(PhoneNo));
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.ScoreCycle = in.readUTF();
        this.EnttId = in.readUTF();
        this.ScoreType = in.readUTF();
        this.ScoreBalance = in.readUTF();
        this.StatMonth = in.readUTF();
        this.StatDate = in.readUTF();
        this.PhoneNo = in.readUTF();
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

    @Override
    public String toString() {
        return "ScoreInfo [ScoreCycle=" + ScoreCycle + ", PhoneNo=" + PhoneNo + ", EnttId="
                + EnttId + ", ScoreType=" + ScoreType + ", ScoreBalance=" + ScoreBalance
                + ", StatMonth=" + StatMonth + ", StatDate=" + StatDate + "]";
    }
    
    
    
    
}
