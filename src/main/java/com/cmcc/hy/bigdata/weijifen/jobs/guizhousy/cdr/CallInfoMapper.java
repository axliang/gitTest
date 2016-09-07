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
 * @File: CallInfoMapper.java
 * @Date: 2016年07月22日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 */
public class CallInfoMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {

    private static final Logger logger = LoggerFactory.getLogger(CallInfoMapper.class);

    private static final String CALL_INFO_COLUMN = "callInfo.column";
    private static final String CALL_INFO_COLUMN_FILTER_RULE = "callInfo.column.filterRule";
    private static final String CALL_INFO_COLUMN_MAPPING = "callInfo.column.mapping";
    private static final String PHONE_MAPPING_FILE = "phoneMappingFilePath";
    private static final String REGION_MAPPING_FILE = "regionMappingFilePath";

    //	private static final String TIME_FORMAT = "yyyy-MM-dd-HH.mm.ss";// 贵州特殊的时间格式

    private static final String MSISDN = "msisdn";
    private static final String CALLTYPE_ID = "calltype_id";
    private static final String OPPOSITE_NO = "opposite_no";
    private static final String START_DATETIME = "start_datetime";
    private static final String CALL_DURATION = "call_duration";
    private static final String ROAMTYPE_ID = "roamtype_id";
    private static final String ROAM_CITY_ID = "roam_city_id";
    private static final String OPPOSITE_CITY_ID = "opposite_city_id";

    private String columnSeparator;
    private String ruleSeparator;
    private String mappingSeparator;
    private String hiveColumn;
    private String filterRule;
    private String mapping;

    private List<ValueExtractor> extractors;
    private Map<String, String> phoneMap;
    private Map<String, String> regionMap;

    private EnumConverter converter = new GuizhouEnumConverter();

    private ImmutableBytesWritable keyout = new ImmutableBytesWritable();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        columnSeparator = context.getConfiguration().get(ConfigConstants.COLUMN_SEPARATOR);
        ruleSeparator = context.getConfiguration().get(ConfigConstants.RULE_SEPARATOR);
        mappingSeparator = context.getConfiguration().get(ConfigConstants.MAPPING_SEPARATOR);
        hiveColumn = context.getConfiguration().get(CALL_INFO_COLUMN);
        filterRule = context.getConfiguration().get(CALL_INFO_COLUMN_FILTER_RULE);
        mapping = context.getConfiguration().get(CALL_INFO_COLUMN_MAPPING);
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
        regionMap = ConvertUtil.getMapping(
                context.getConfiguration().get(REGION_MAPPING_FILE, "regionMapping.txt"));
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
        info.setBusinessType(BusinessType.CALL);
        info.setCommunicationType(
                converter.getCommunicationType(BusinessType.CALL, map.get(CALLTYPE_ID)));
        setOppositeInfo(info, oppositeNo, map.get(OPPOSITE_CITY_ID));
        double chargeInfo;
        try {
            chargeInfo = Double.parseDouble(map.get(CALL_DURATION));
        } catch (NumberFormatException e) {
            chargeInfo = 0;
        }
        info.setChargeInfo(chargeInfo);
        info.setStartTime(ts);
        info.setRoamType(converter.getRoamType(map.get(ROAMTYPE_ID)));

        String regionInfo = regionMap.get(map.get(ROAM_CITY_ID));
        if (regionInfo != null) {
            String[] array = regionInfo.split(";");
            if (array.length == 2) {
                info.setRoamPlace(array[0]);
                info.setRoamPlaceNo(array[1]);
            }
        }

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
        if (info.getOtherCity() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                    HBaseTableSchema.QL_OTHERCITY, info.getOtherCity());
        }
        if (info.getOtherCityNo() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                    HBaseTableSchema.QL_OTHERCITYNO, info.getOtherCityNo());
        }
        HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                HBaseTableSchema.QL_STARTTIME, info.getStartTime());
        HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                HBaseTableSchema.QL_ROAMTYPE, info.getRoamType().getCode());
        if (info.getRoamPlace() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                    HBaseTableSchema.QL_ROAMPLACE, info.getRoamPlace());
        }
        if (info.getRoamPlaceNo() != null) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                    HBaseTableSchema.QL_ROAMINGPLACENO, info.getRoamPlaceNo());
        }
        if (info.getChargeInfo() >= 0) {
            HBaseUtil.addColumn(put, HBaseTableSchema.CF_COMMUNICATIONINFO,
                    HBaseTableSchema.QL_CHARGEINFO, info.getChargeInfo());
        }
        return put;
    }

    private void setOppositeInfo(CommunicationDetailInfo info, String oppositeNo,
                                 String oppositeCityId) {
        String otherPhoneNo = ConvertUtil.convertMobilePhoneNumber(oppositeNo);
        if (otherPhoneNo == null) {
            info.setOtherPhoneNumber(oppositeNo);
            if (oppositeCityId != null) {
                String cityInfo = regionMap.get(oppositeCityId);
                if (cityInfo != null) {
                    String[] cityInfos = cityInfo.split(";");
                    if (cityInfos.length == 2) {
                        info.setOtherCity(cityInfos[0]);
                        info.setOtherCityNo(cityInfos[1]);
                    }
                }
            }
        } else {
            info.setOtherPhoneNumber(otherPhoneNo);
            String cityInfo = phoneMap.get(otherPhoneNo.substring(0, 7));
            if (cityInfo != null) {
                String[] cityInfos = cityInfo.split(";");
                if (cityInfos.length == 2) {
                    info.setOtherCity(cityInfos[0]);
                    info.setOtherCityNo(cityInfos[1]);
                }
            } else if (oppositeCityId != null) {
                cityInfo = regionMap.get(oppositeCityId);
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

}
