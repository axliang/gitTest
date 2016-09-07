package com.cmcc.hy.bigdata.weijifen.jobs.guizhousy.userinfoday;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

import com.cmcc.hy.bigdata.weijifen.constants.HBaseTableSchema;
import com.cmcc.hy.bigdata.weijifen.enums.CertType;
import com.cmcc.hy.bigdata.weijifen.enums.SexType;
import com.cmcc.hy.bigdata.weijifen.model.UserBasicInfo;
import com.cmcc.hy.bigdata.weijifen.util.DateUtil;
import com.cmcc.hy.bigdata.weijifen.util.HBaseUtil;
import com.cmcc.hy.bigdata.weijifen.util.StringUtil;
import com.cmcc.hy.bigdata.weijifen.util.ValidationUtil;

/**
 * 用户基本信息导入任务Reducer类
 *
 * @Project: credit-collection-hivedata
 * @File: UserInfoReducer.java
 * @Date: 2016年3月23日
 * @Author: hechan
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 */
public class UserInfoDayReducer extends TableReducer<Text, UserBasicInfo, ImmutableBytesWritable> {

    @Override
    protected void reduce(Text key, Iterable<UserBasicInfo> values, Context context)
            throws IOException, InterruptedException {

        UserBasicInfo info = new UserBasicInfo();
        ArrayList<UserBasicInfo> infoList = new ArrayList<UserBasicInfo>();
        info.setAge(-1);
        for (UserBasicInfo val : values) {
            if (StringUtil.strIsNotNull(val.getIdNumber())) {
                String id = val.getIdNumber();
                info.setIdNumber(id);
                if (ValidationUtil.isValidIdentityCard(id)) {
                    info.setRegistrationType(CertType.IdCard.getCode());
                    String birthday = id.substring(6, 14);
                    info.setBirthday(birthday);
                    info.setAge(DateUtil.ageCalculation(birthday, ""));
                    int gender = Integer.parseInt(id.substring(16, 17));
                    if (gender % 2 == 0) {
                        info.setGender(SexType.FEMALE.getCode());
                    } else {
                        info.setGender(SexType.MALE.getCode());
                    }
                } else {
                    if (StringUtil.strIsNotNull(val.getRegistrationType())) {
                        info.setRegistrationType(val.getRegistrationType());
                    }
                    if (StringUtil.strIsNotNull(val.getBirthday())) {
                        info.setBirthday(val.getBirthday());
                    }
                    if (StringUtil.strIsNotNull(val.getGender())) {
                        info.setGender(val.getGender());
                    }
                    if (val.getAge() >= 0 && info.getAge() < 0) {
                        info.setAge(val.getAge());
                    }
                }

                if (StringUtil.strIsNotNull(val.getAddress())) {
                    info.setAddress(val.getAddress());
                }
                if (StringUtil.strIsNotNull(val.getName())) {
                    info.setName(val.getName());
                }
            } else {
                UserBasicInfo temp = new UserBasicInfo();
                if (StringUtil.strIsNotNull(val.getUid())) {
                    temp.setUid(val.getUid());
                } else {
                    continue;
                }
                if (StringUtil.strIsNotNull(val.getImsi())) {
                    temp.setImsi(val.getImsi());
                }
                if (val.getAge() >= 0 && info.getAge() < 0) {
                    temp.setAge(val.getAge());
                }
                if (StringUtil.strIsNotNull(val.getGender())) {
                    temp.setGender(val.getGender());
                }
                if (StringUtil.strIsNotNull(val.getUserstatus())) {
                    temp.setUserstatus(val.getUserstatus());
                }
                if (StringUtil.strIsNotNull(val.getRegistrationTime())) {
                    temp.setRegistrationTime(val.getRegistrationTime());
                }

                infoList.add(temp);
            }
        }

        for (UserBasicInfo ubi : infoList) {
            String phone = ubi.getUid();
            byte[] rowkey = Bytes.toBytes(phone);
            Put output = new Put(rowkey);

            HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_UID,
                    phone);
            if (StringUtil.strIsNotNull(ubi.getImsi())) {
                HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_IMSI,
                        ubi.getImsi());
            }

            if (StringUtil.strIsNotNull(ubi.getUserstatus())) {
                HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO,
                        HBaseTableSchema.QL_USERSTATUS, ubi.getUserstatus());
            }

            if (StringUtil.strIsNotNull(ubi.getRegistrationTime())) {
                HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO,
                        HBaseTableSchema.QL_REGISTRATIONTIME, ubi.getUserstatus());
            }

            if (info.getAge() >= 0) {
                HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_AGE,
                        info.getAge());
            } else if (ubi.getAge() >= 0) {
                HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_AGE,
                        ubi.getAge());
            }
            if (StringUtil.strIsNotNull(info.getGender())) {
                HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO,
                        HBaseTableSchema.QL_GENDER, info.getGender());
            } else if (StringUtil.strIsNotNull(ubi.getGender())) {
                HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO,
                        HBaseTableSchema.QL_GENDER, ubi.getGender());
            }

            if (StringUtil.strIsNotNull(info.getBirthday())) {
                HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO,
                        HBaseTableSchema.QL_BIRTHDAY, info.getBirthday());
            }

            if (StringUtil.strIsNotNull(info.getRegistrationType())) {
                HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO,
                        HBaseTableSchema.QL_REGISTRATIONTYPE, info.getRegistrationType());
            }

            if (StringUtil.strIsNotNull(info.getAddress())) {
                HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO,
                        HBaseTableSchema.QL_ADDRESS, info.getAddress());
            }

            if (StringUtil.strIsNotNull(info.getName())) {
                HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_NAME,
                        info.getName());
            }

            if (StringUtil.strIsNotNull(info.getIdNumber())) {
                HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO,
                        HBaseTableSchema.QL_IDNUMBER, info.getIdNumber());
            }

            if (!output.isEmpty()) {
                context.write(new ImmutableBytesWritable(rowkey), output);
            }
        }
    }

}
