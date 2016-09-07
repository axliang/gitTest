package com.cmcc.hy.bigdata.weijifen.jobs.guizhousy.cdr;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.constants.ConfigConstants;
import com.cmcc.hy.bigdata.weijifen.constants.HBaseTableSchema;
import com.cmcc.hy.bigdata.weijifen.converter.EnumConverter;
import com.cmcc.hy.bigdata.weijifen.converter.GuizhouEnumConverter;
import com.cmcc.hy.bigdata.weijifen.enums.BusinessType;
import com.cmcc.hy.bigdata.weijifen.extractor.ValueExtractor;
import com.cmcc.hy.bigdata.weijifen.extractor.ValueExtractorFactory;
import com.cmcc.hy.bigdata.weijifen.model.CommunicationDetailInfo;
import com.cmcc.hy.bigdata.weijifen.util.ConvertUtil;
import com.cmcc.hy.bigdata.weijifen.util.DateUtil;
import com.cmcc.hy.bigdata.weijifen.util.HBaseUtil;

/**
 * @Project: credit-collection-hivedata
 * @File: SmsInfoMapper.java
 * @Date: 2016年07月22日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 */
public class SmsInfoMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {

    private static final Logger logger = LoggerFactory.getLogger(SmsInfoMapper.class);

    private static final double DEFAULT_CHARGE_INFO = 0;

    private static final String SMS_INFO_COLUMN = "smsInfo.column";
    private static final String SMS_INFO_COLUMN_FILTER_RULE = "smsInfo.column.filterRule";
    private static final String SMS_INFO_COLUMN_MAPPING = "smsInfo.column.mapping";
    private static final String PHONE_MAPPING_FILE = "phoneMappingFilePath";

    //	private static final String TIME_FORMAT = "yyyy-MM-dd-HH.mm.ss";// 贵州特殊的时间格式

    private static final String MSISDN = "msisdn";
    private static final String CALLTYPE_ID = "calltype_id";
    private static final String OPPOSITE_NO = "opposite_no";
    private static final String START_DATETIME = "start_datetime";
    private static final String SVCITEM_ID = "svcitem_id";

    private String columnSeparator;
    private String ruleSeparator;
    private String mappingSeparator;
    private String hiveColumn;
    private String filterRule;
    private String mapping;

    private List<ValueExtractor> extractors;
    private Map<String, String> phoneMap;

    private EnumConverter converter = new GuizhouEnumConverter();

    private ImmutableBytesWritable keyout = new ImmutableBytesWritable();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        columnSeparator = context.getConfiguration().get(ConfigConstants.COLUMN_SEPARATOR);
        ruleSeparator = context.getConfiguration().get(ConfigConstants.RULE_SEPARATOR);
        mappingSeparator = context.getConfiguration().get(ConfigConstants.MAPPING_SEPARATOR);
        hiveColumn = context.getConfiguration().get(SMS_INFO_COLUMN);
        filterRule = context.getConfiguration().get(SMS_INFO_COLUMN_FILTER_RULE);
        mapping = context.getConfiguration().get(SMS_INFO_COLUMN_MAPPING);
        String[] columns = hiveColumn.split(columnSeparator);
        String[] rules = filterRule.split(ruleSeparator);
        String[] mappings = mapping.split(mappingSeparator);
        try {
            extractors = ValueExtractorFactory.makeInstances(columns, rules, mappings);
        } catch (Exception e) {
            logger.error("construct extractors fail : {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        phoneMap = ConvertUtil
                .getMapping(context.getConfiguration().get(PHONE_MAPPING_FILE, "phoneMapping.txt"));
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] values = value.toString().split("\t");
        Map<String, String> map = new HashMap<String, String>();
        for (ValueExtractor extractor : extractors) {
            String result = extractor.validateAndExtract(values);
            map.put(extractor.getMapping(), result);
        }
        String phoneNo = ConvertUtil.convertMobilePhoneNumber(map.get(MSISDN));
        if (phoneNo == null || phoneNo.isEmpty()) {
            return;
        }
        BusinessType businessType = determinBusiness(map.get(SVCITEM_ID));
        if (businessType == BusinessType.UNDEFINED) {
            // 过滤非点对点短信和彩信
            return;
        }
        Date dt = DateUtil.getDateFromStr(map.get(START_DATETIME),
                DateUtil.YMD_HYPHEN_HMSS_DOT_PATTEN);
        if (dt == null) {
            dt = DateUtil.getDateFromStr(map.get(START_DATETIME),
                    DateUtil.YMD_HYPHEN_HMS_COLON_PATTERN);
            if (dt == null) {
                return;
            }
        }
        String ts = DateUtil.formatDateToStr(dt, DateUtil.YMDHMS_PATTERN);

        String oppositeNo = map.get(OPPOSITE_NO);
        if (oppositeNo == null) {
            return;
        }

        CommunicationDetailInfo info = new CommunicationDetailInfo();
        info.setBusinessType(businessType);
        info.setCommunicationType(
                converter.getCommunicationType(businessType, map.get(CALLTYPE_ID)));
        setOppositeInfo(info, oppositeNo);
        info.setStartTime(ts);
        info.setChargeInfo(DEFAULT_CHARGE_INFO);

        byte[] rowkey = Bytes.toBytes(phoneNo + "-" + ts);
        keyout.set(rowkey);
        Put put = getMutation(rowkey, info);
        if (!put.isEmpty()) {
            context.write(keyout, put);
        }
    }

    private Put getMutation(byte[] rowkey, CommunicationDetailInfo info) {
        Put put = new Put(rowkey);
        HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                HBaseTableSchema.QL_BUSINESSTYPE, info.getBusinessType().name());
        HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                HBaseTableSchema.QL_COMMUNICATIONTYPE, info.getCommunicationType().name());
        HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                HBaseTableSchema.QL_OTHERPHONENUMBER, info.getOtherPhoneNumber());
        HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                HBaseTableSchema.QL_STARTTIME, info.getStartTime());

        if (info.getOtherCity() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                    HBaseTableSchema.QL_OTHERCITY, info.getOtherCity());
        }
        if (info.getOtherCityNo() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                    HBaseTableSchema.QL_OTHERCITYNO, info.getOtherCityNo());
        }
        HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                HBaseTableSchema.QL_CHARGEINFO, info.getChargeInfo());
        return put;
    }

    private BusinessType determinBusiness(String svcItemId) {
        if (svcItemId == null || svcItemId.isEmpty()) {
            return BusinessType.UNDEFINED;
        }
        // 200001 , 200006 ,200016 ,200018 ,200100,--短信 400301--彩信
        if ("200001".equals(svcItemId) || "200006".equals(svcItemId) || "200016".equals(svcItemId)
                || "200018".equals(svcItemId) || "200100".equals(svcItemId)) {
            return BusinessType.SMS;
        }
        if ("400301".equals(svcItemId)) {
            return BusinessType.MMS;
        }
        return BusinessType.UNDEFINED;
    }

    private void setOppositeInfo(CommunicationDetailInfo info, String oppositeNo) {
        String otherPhoneNo = ConvertUtil.convertMobilePhoneNumber(oppositeNo);
        if (otherPhoneNo == null) {
            info.setOtherPhoneNumber(oppositeNo);
        } else {
            info.setOtherPhoneNumber(otherPhoneNo);
            String cityInfo = phoneMap.get(otherPhoneNo.substring(0, 7));
            if (cityInfo != null) {
                String[] cityInfos = cityInfo.split(";");
                if (cityInfos.length == 2) {
                    info.setOtherCity(cityInfos[0]);
                    info.setOtherCityNo(cityInfos[1]);
                }
            }
        }
    }
}
