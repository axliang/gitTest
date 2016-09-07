package com.cmcc.hy.bigdata.weijifen.jobs.guizhousy.userinfomonth;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.constants.ConfigConstants;
import com.cmcc.hy.bigdata.weijifen.extractor.ValueExtractor;
import com.cmcc.hy.bigdata.weijifen.extractor.ValueExtractorFactory;
import com.cmcc.hy.bigdata.weijifen.model.UserMonthInfo;
import com.cmcc.hy.bigdata.weijifen.util.ConvertUtil;
import com.cmcc.hy.bigdata.weijifen.util.DateUtil;

/**
 * 用户月结账单Map阶段处理过程
 *
 * @Project: credit-collection-hivedata
 * @File: UserMonthBillMapper.java
 * @Date: 2016年3月30日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2016 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public class UserMonthBillMapper extends Mapper<LongWritable, Text, Text, UserMonthInfo> {

    private static final Logger logger = LoggerFactory.getLogger(UserMonthBillMapper.class);

    private static final String USER_MONTH_BILL_COLUMN = "userMonthBill.column";
    private static final String USER_MONTH_BILL_COLUMN_FILTER_RULE = "userMonthBill.column.filterRule";
    private static final String USER_MONTH_BILL_COLUMN_MAPPING = "userMonthBill.column.mapping";

    private static final String OP_TIME = "op_time";
    private static final String PRODUCT_NO = "product_no";
    private static final String FACT_FEE = "fact_fee";

    private String columnSeparator;
    private String ruleSeparator;
    private String mappingSeparator;
    private String hiveColumn;
    private String filterRule;
    private String mapping;

    private List<ValueExtractor> extractors;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        columnSeparator = context.getConfiguration().get(ConfigConstants.COLUMN_SEPARATOR);
        ruleSeparator = context.getConfiguration().get(ConfigConstants.RULE_SEPARATOR);
        mappingSeparator = context.getConfiguration().get(ConfigConstants.MAPPING_SEPARATOR);
        hiveColumn = context.getConfiguration().get(USER_MONTH_BILL_COLUMN);
        filterRule = context.getConfiguration().get(USER_MONTH_BILL_COLUMN_FILTER_RULE);
        mapping = context.getConfiguration().get(USER_MONTH_BILL_COLUMN_MAPPING);
        String[] columns = hiveColumn.split(columnSeparator);
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
        String converted = ConvertUtil.convertEncoding(value, "UTF-8");
        if (converted == null) {
            return;
        }
        String[] values = converted.split("\t");

        Map<String, String> map = new HashMap<String, String>();
        for (ValueExtractor extractor : extractors) {
            String result = extractor.validateAndExtract(values);
            map.put(extractor.getMapping(), result);
        }

        UserMonthInfo valueout = new UserMonthInfo();
        valueout.setOpTime(DateUtil.convertYMDFormat(map.get(OP_TIME), DateUtil.YM_PATTERN));
        valueout.setPhoneNo(ConvertUtil.convertMobilePhoneNumber(map.get(PRODUCT_NO)));
        valueout.setFactFee(map.get(FACT_FEE));
        if (valueout.getPhoneNo() == null || valueout.getOpTime() == null
                || valueout.getFactFee() == null) {
            return;
        }

        Text keyout = new Text();
        keyout.set(valueout.getPhoneNo());
        context.write(keyout, valueout);
    }

}
