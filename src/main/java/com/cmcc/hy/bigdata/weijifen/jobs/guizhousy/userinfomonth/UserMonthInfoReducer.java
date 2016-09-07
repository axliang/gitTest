package com.cmcc.hy.bigdata.weijifen.jobs.guizhousy.userinfomonth;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.constants.HBaseTableSchema;
import com.cmcc.hy.bigdata.weijifen.model.MonthBillInfo;
import com.cmcc.hy.bigdata.weijifen.model.UserBasicInfo;
import com.cmcc.hy.bigdata.weijifen.model.UserMonthInfo;
import com.cmcc.hy.bigdata.weijifen.util.HBaseUtil;

/**
 * 用户资料月表导入reduce阶段
 *
 * @Project: credit-collection-hivedata
 * @File: UserMonthInfoReducer.java
 * @Date: 2016年3月31日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2016 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public class UserMonthInfoReducer
        extends TableReducer<Text, UserMonthInfo, ImmutableBytesWritable> {

    private static final Logger logger = LoggerFactory.getLogger(UserMonthInfoReducer.class);

    private static final ImmutableBytesWritable USER_MONTH_BILL_TABLE = new ImmutableBytesWritable(
            Bytes.toBytes(HBaseTableSchema.USER_MONTH_BILL_TABLE));
    private static final ImmutableBytesWritable USER_INFO_TABLE = new ImmutableBytesWritable(
            Bytes.toBytes(HBaseTableSchema.USER_INFO_TABLE));

    private DecimalFormat df = new DecimalFormat("#0.00");

    @Override
    protected void reduce(Text key, Iterable<UserMonthInfo> values, Context context)
            throws IOException, InterruptedException {
        MonthBillInfo bill = new MonthBillInfo();
        UserBasicInfo user = new UserBasicInfo();
        double totalFee = 0;
        double voiceFee = 0;
        int localCallDur = 0;
        int totalDur = 0;
        int nationRoamDur = 0;
        int interRoamDur = 0;
        for (UserMonthInfo info : values) {
            if ((info.getFactFee() == null || info.getFactFee().isEmpty())
                    && (info.getStarLevel() == null || info.getStarLevel().isEmpty())) {
                bill.setPhoneNo(info.getPhoneNo());
                bill.setAcctNo(info.getOpTime());
                voiceFee += convertToDouble(info.getLocalCallFee());
                voiceFee += convertToDouble(info.getTollFee());
                localCallDur += convertToInt(info.getLocalOutCallDur())
                        + convertToInt(info.getLocalIntCallDur());
                totalDur += localCallDur + convertToInt(info.getTollDur());
                nationRoamDur += convertToInt(info.getSnRoamDur())
                        + convertToInt(info.getSjRoamDur());
                interRoamDur += convertToInt(info.getGjRoamDur());
                // valueout.setGprsFlows(map.get(GPRS_FLOWS));
                user.setUid(info.getPhoneNo());
                try {
                    user.setAge(Integer.parseInt(info.getAge()));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                user.setName(info.getName());
                user.setGender(info.getSex());
                user.setBirthday(info.getBirthday());
                user.setOrders(info.getPlanId());
                user.setPhoneBrand(info.getTermBrand());
                user.setPhoneModel(info.getTermModel());
                user.setRegistrationTime(info.getUserOpentime());
                user.setUserstatus(info.getUserStatus());

            } else if (info.getFactFee() != null && !info.getFactFee().isEmpty()) {
                bill.setPhoneNo(info.getPhoneNo());
                bill.setAcctNo(info.getOpTime());
                totalFee += convertToDouble(info.getFactFee());
            } else if (info.getStarLevel() != null && !info.getStarLevel().isEmpty()) {
                user.setUid(info.getPhoneNo());
                try {
                    int level = Integer.parseInt(info.getStarLevel());
                    if (level > 1) {
                        level--;
                    }
                    user.setStarLevel(String.valueOf(level));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        if (bill.getPhoneNo() != null && bill.getAcctNo() != null) {
            bill.setTotalFee(df.format(totalFee));
            bill.setVoiceFee(df.format(voiceFee));
            bill.setTotalDur(String.valueOf(totalDur));
            bill.setLocCallDur(String.valueOf(localCallDur));
            bill.setNationRoamDur(String.valueOf(nationRoamDur));
            bill.setInterRoamDur(String.valueOf(interRoamDur));
        }

        writeToHBase(bill, context);
        writeToHBase(user, context);
    }

    private double convertToDouble(String doubleVal) {
        if (doubleVal == null || doubleVal.isEmpty()) {
            return 0;
        }
        try {
            return Double.valueOf(doubleVal);
        } catch (Exception e) {
            logger.error(String.format("convert str to double error:%s", e.getMessage()), e);
        }
        return 0;
    }

    private int convertToInt(String intVal) {
        if (intVal == null || intVal.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(intVal);
        } catch (Exception e) {
            logger.error(String.format("convert str to int error:%s", e.getMessage()), e);
        }
        return 0;
    }

    private void writeToHBase(MonthBillInfo bill, Context context)
            throws IOException, InterruptedException {
        if (bill.getPhoneNo() == null && bill.getAcctNo() == null) {
            return;
        }
        byte[] rowkey = Bytes.toBytes(bill.getPhoneNo() + "-" + bill.getAcctNo());
        Put put = new Put(rowkey);

        HBaseUtil.addColumn(put, HBaseTableSchema.CF_BILL, HBaseTableSchema.QL_ACCTNO,
                bill.getAcctNo());
        if (bill.getTotalFee() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BILL, HBaseTableSchema.QL_TOTALFEE,
                    bill.getTotalFee());
        }
        if (bill.getVoiceFee() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BILL, HBaseTableSchema.QL_VOICEFEE,
                    bill.getVoiceFee());
        }
        if (bill.getTotalDur() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BILL, HBaseTableSchema.QL_TOTALDUR,
                    bill.getTotalDur());
        }
        if (bill.getLocCallDur() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BILL, HBaseTableSchema.QL_LOCCALLDUR,
                    bill.getLocCallDur());
        }
        if (bill.getNationRoamDur() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BILL, HBaseTableSchema.QL_NATIONROAMDUR,
                    bill.getNationRoamDur());
        }
        if (bill.getInterRoamDur() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BILL, HBaseTableSchema.QL_INTERROAMDUR,
                    bill.getInterRoamDur());
        }
        context.write(USER_MONTH_BILL_TABLE, put);
    }

    private void writeToHBase(UserBasicInfo user, Context context)
            throws IOException, InterruptedException {
        if (user.getUid() == null) {
            return;
        }
        byte[] rowkey = Bytes.toBytes(user.getUid());
        Put put = new Put(rowkey);
        HBaseUtil.addColumn(put, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_UID, rowkey);
        if (user.getAge() > 0) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_AGE,
                    user.getAge());
        }
        if (user.getName() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_NAME,
                    user.getName());
        }
        if (user.getGender() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_GENDER,
                    user.getGender());
        }
        if (user.getBirthday() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_BIRTHDAY,
                    user.getBirthday());
        }
        if (user.getOrders() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_ORDERS,
                    user.getOrders());
        }
        if (user.getPhoneBrand() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_PHONEBRAND,
                    user.getPhoneBrand());
        }
        if (user.getPhoneModel() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_PHONEMODEL,
                    user.getPhoneModel());
        }

        if (user.getRegistrationTime() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BASICINFO,
                    HBaseTableSchema.QL_REGISTRATIONTIME, user.getRegistrationTime());
        }
        if (user.getUserstatus() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_USERSTATUS,
                    user.getUserstatus());
        }
        if (user.getStarLevel() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_STARLEVEL,
                    user.getStarLevel());
        }
        context.write(USER_INFO_TABLE, put);
    }

}
