package com.cmcc.hy.bigdata.weijifen.jobs.guizhousy.userinfomonth;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.constants.ConfigConstants;
import com.cmcc.hy.bigdata.weijifen.converter.EnumConverter;
import com.cmcc.hy.bigdata.weijifen.converter.GuizhouEnumConverter;
import com.cmcc.hy.bigdata.weijifen.extractor.ValueExtractor;
import com.cmcc.hy.bigdata.weijifen.extractor.ValueExtractorFactory;
import com.cmcc.hy.bigdata.weijifen.model.UserMonthInfo;
import com.cmcc.hy.bigdata.weijifen.util.ConvertUtil;
import com.cmcc.hy.bigdata.weijifen.util.DateUtil;

/**
 * 用户资料属性月表Map阶段处理过程
 *
 * @Project: credit-collection-hivedata
 * @File: UserMonthInfoMapper.java
 * @Date: 2016年3月30日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2016 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public class UserMonthInfoMapper extends Mapper<LongWritable, Text, Text, UserMonthInfo> {

    private static final Logger logger = LoggerFactory.getLogger(UserMonthBillMapper.class);

    private static final String USER_MONTH_INFO_COLUMN = "userMonthInfo.column";
    private static final String USER_MONTH_INFO_COLUMN_FILTER_RULE = "userMonthInfo.column.filterRule";
    private static final String USER_MONTH_INFO_COLUMN_MAPPING = "userMonthInfo.column.mapping";

    private static final String PRODUCT_NO = "product_no";// 手机号
    private static final String AGE = "age";// 年龄
    private static final String SEX_ID = "sex_id";// 客户性别
    private static final String CUST_NAME = "cust_name";// 客户姓名
    private static final String BIRTH_DAY = "birth_day";// 客户生日
    private static final String PLAN_ID = "plan_id";// 资费套餐
    private static final String TERM_BRAND = "term_brand";// 手机品牌
    private static final String TERM_MODEL = "term_model";// 手机型号
    private static final String USER_OPENTIME = "user_opentime";// 入网时间
    private static final String USERSTATUS_ID = "userstatus_id";// 用户状态

    private static final String OP_TIME = "op_time";// 账期
    private static final String LOCAL_CALL_FEE = "local_call_fee";// 本地通话费
    private static final String TOLL_FEE = "toll_fee";// 长途费
    private static final String LOCAL_OUT_CALL_DUR = "local_out_call_dur";// 本地主叫通话时长
    private static final String LOCAL_INT_CALL_DUR = "local_int_call_dur";// 本地被叫通话时长
    private static final String TOLL_DUR = "toll_dur";// 长途通话时长
    private static final String SN_ROAM_DUR = "sn_roam_dur";// 省内长途通话时长
    private static final String SJ_ROAM_DUR = "sj_roam_dur";// 省际长途通话时长
    private static final String GJ_ROAM_DUR = "gj_roam_dur";// 国际长途通话时长
    private static final String GPRS_FLOWS = "gprs_flows";// GPRS使用流量

    private String columnSeparator;
    private String ruleSeparator;
    private String mappingSeparator;
    private String hiveColumn;
    private String filterRule;
    private String mapping;

    private List<ValueExtractor> extractors;

    private EnumConverter converter = new GuizhouEnumConverter();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        columnSeparator = context.getConfiguration().get(ConfigConstants.COLUMN_SEPARATOR);
        ruleSeparator = context.getConfiguration().get(ConfigConstants.RULE_SEPARATOR);
        mappingSeparator = context.getConfiguration().get(ConfigConstants.MAPPING_SEPARATOR);
        hiveColumn = context.getConfiguration().get(USER_MONTH_INFO_COLUMN);
        filterRule = context.getConfiguration().get(USER_MONTH_INFO_COLUMN_FILTER_RULE);
        mapping = context.getConfiguration().get(USER_MONTH_INFO_COLUMN_MAPPING);
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
        valueout.setPhoneNo(ConvertUtil.convertMobilePhoneNumber(map.get(PRODUCT_NO)));
        valueout.setOpTime(DateUtil.convertYMDFormat(map.get(OP_TIME), DateUtil.YM_PATTERN));
        if (valueout.getPhoneNo() == null || valueout.getOpTime() == null) {
            return;
        }
        valueout.setAge(map.get(AGE));
        valueout.setSex(converter.getSexType(map.get(SEX_ID)).getCode());
        valueout.setName(map.get(CUST_NAME));
        valueout.setBirthday(DateUtil.convertYMDFormat(map.get(BIRTH_DAY), DateUtil.YMD_PATTERN));
        valueout.setPlanId(map.get(PLAN_ID));
        valueout.setTermBrand(map.get(TERM_BRAND));
        valueout.setTermModel(map.get(TERM_MODEL));
        Date tmp = DateUtil.getDateFromStr(map.get(USER_OPENTIME),
                DateUtil.YMD_HYPHEN_HMS_COLON_PATTERN);
        valueout.setUserOpentime(DateUtil.formatDateToStr(tmp, DateUtil.YMD_PATTERN));
        valueout.setUserStatus(converter.getUserStatus(map.get(USERSTATUS_ID)).getCode());

        valueout.setLocalCallFee(map.get(LOCAL_CALL_FEE));
        valueout.setTollFee(map.get(TOLL_FEE));
        valueout.setLocalOutCallDur(map.get(LOCAL_OUT_CALL_DUR));
        valueout.setLocalIntCallDur(map.get(LOCAL_INT_CALL_DUR));
        valueout.setTollDur(map.get(TOLL_DUR));
        valueout.setSnRoamDur(map.get(SN_ROAM_DUR));
        valueout.setSjRoamDur(map.get(SJ_ROAM_DUR));
        valueout.setGjRoamDur(map.get(GJ_ROAM_DUR));
        valueout.setGprsFlows(map.get(GPRS_FLOWS));

        Text keyout = new Text();
        keyout.set(valueout.getPhoneNo());
        context.write(keyout, valueout);
    }

}
