package com.cmcc.hy.bigdata.weijifen.jobs.guizhousy.userinfoday;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.extractor.ValueExtractor;
import com.cmcc.hy.bigdata.weijifen.extractor.ValueExtractorFactory;
import com.cmcc.hy.bigdata.weijifen.model.UserBasicInfo;
import com.cmcc.hy.bigdata.weijifen.util.ConvertUtil;
import com.cmcc.hy.bigdata.weijifen.util.DateUtil;

/**
 * 用户日数据导入任务Mapper类
 *
 * @Project: credit-collection-hivedata
 * @File: UserInfoMapper.java
 * @Date: 2016年3月22日
 * @Author: 何婵
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 */
public class UserInfoMapper extends Mapper<LongWritable, Text, Text, UserBasicInfo> {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoMapper.class);

    private static final String CUST_ID = "cust_id";
    private static final String AGE_ID = "age_id";
    private static final String SEX_ID = "sex_id";
    private static final String IMSI = "imsi";
    private static final String PRODUCT_NO = "product_no";
    private static final String USER_OPENTIME = "user_opentime";
    private static final String USERSTATUS_ID = "userstatus_id";

    private List<ValueExtractor> extractors;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        String columnSeparator = context.getConfiguration().get(UserInfoDayJob.COLUMN_SEPARATOR);
        String ruleSeparator = context.getConfiguration().get(UserInfoDayJob.RULE_SEPARATOR);
        String mappingSeparator = context.getConfiguration().get(UserInfoDayJob.MAPPING_SEPARATOR);
        String column = context.getConfiguration().get(UserInfoDayJob.USER_INFO_COLUMN);
        String filterRule = context.getConfiguration()
                .get(UserInfoDayJob.USER_INFO_COLUMN_FILTER_RULE);
        String mapping = context.getConfiguration().get(UserInfoDayJob.USER_INFO_COLUMN_MAPPING);
        String[] columns = column.split(columnSeparator);
        String[] rules = filterRule.split(ruleSeparator);
        String[] mappings = mapping.split(mappingSeparator);
        try {
            extractors = ValueExtractorFactory.makeInstances(columns, rules, mappings);
        } catch (Exception e) {
            logger.error("construct extractors fail : {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String converted = ConvertUtil.convertEncoding(value, "GBK");
        if (converted == null) {
            return;
        }
        String[] values = converted.toString().split("\t");

        Map<String, String> map = new HashMap<String, String>();
        for (ValueExtractor extractor : extractors) {
            String result = extractor.validateAndExtract(values);
            map.put(extractor.getMapping(), result);
            logger.debug(extractor.getMapping() + "-" + result);
        }

        String custId = map.get(CUST_ID);
        if (custId == null) {
            // 丢弃没有客户编号的数据
            return;
        }
        UserBasicInfo valueout = new UserBasicInfo();
        valueout.setAge(-1);
        try {
            if (map.containsKey(AGE_ID) && map.get(AGE_ID) != null) {
                valueout.setAge(Integer.parseInt(map.get(AGE_ID)));
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        valueout.setGender(UserInfoDayJob.CONVERTER.getSexType(map.get(SEX_ID)).getCode());
        valueout.setImsi(map.get(IMSI));
        valueout.setUserstatus(
                UserInfoDayJob.CONVERTER.getUserStatus(map.get(USERSTATUS_ID)).getCode());
        String phoneNumber = ConvertUtil.convertMobilePhoneNumber(map.get(PRODUCT_NO));
        if (phoneNumber == null) {
            return;
        }
        valueout.setUid(phoneNumber);
        String registrationTime = DateUtil.formatDateToStr(DateUtil
                .getDateFromStr(map.get(USER_OPENTIME), DateUtil.YMD_HYPHEN_HMS_COLON_PATTERN),
                DateUtil.YMD_PATTERN);
        valueout.setRegistrationTime(registrationTime);

        Text keyout = new Text();
        keyout.set(custId);
        context.write(keyout, valueout);
        logger.debug(valueout.getUid());
    }
}
