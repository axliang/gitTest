/*
 * Project: alex-credit-collection-hivedata
 * 
 * File Created at 2016年8月17日
 * 
 * Copyright 2016 CMCC Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * ZYHY Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license.
 */
package com.cmcc.hy.bigdata.weijifen.jobs.hubei.score;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.extractor.ValueExtractor;
import com.cmcc.hy.bigdata.weijifen.extractor.ValueExtractorFactory;
import com.cmcc.hy.bigdata.weijifen.model.ScoreInfo;
import com.cmcc.hy.bigdata.weijifen.util.ConvertUtil;

/**
 * @Type ScoreMapper.java
 * @Desc 
 * @author alex
 * @date 2016年8月17日 下午4:29:21
 * @version 
 */
public class ScoreInfoDayMapper  extends Mapper<BytesWritable, Text, Text, ScoreInfo> {

    
    /**
     * 系统日志类实例
     */
    private static final Logger logger = LoggerFactory.getLogger(ScoreInfoDayMapper.class);

    private static final String ENTT_ID = "entt_id";
    private static final String SCORE_TYPE = "score_type";
    private static final String SCORE_BALANCE = "score_balance";
    private static final String STAT_MONTH = "stat_month";
    private static final String STAT_DATE = "stat_date";   

    
    private List<ValueExtractor> extractors;
    
    
    @Override
    protected void map(BytesWritable key, Text value,
                       Mapper<BytesWritable, Text, Text, ScoreInfo>.Context context)
            throws IOException, InterruptedException {
        // TODO Auto-generated method stub
        String converted = ConvertUtil.convertEncoding(value, "GBK");
        if (converted == null) {
            return;
        }
        String[] values = converted.toString().split("\t");
        for(int i=0;i<values.length;i++){
            logger.info("the i="+i+" value="+values[i]);
            
        }

       Map<String, String> map = new HashMap<String, String>();
        for (ValueExtractor extractor : extractors) {
            String result = extractor.validateAndExtract(values);
            map.put(extractor.getMapping(), result);
        }
        String enttid = map.get(ENTT_ID);
        if (enttid == null) {
            // 丢弃没有客户编号的数据
            return;
        }
        
        
        ScoreInfo valueout = new ScoreInfo();

        valueout.setEnttId(map.get(ENTT_ID));
        valueout.setScoreType(map.get(SCORE_TYPE));
        valueout.setScoreBalance(map.get(SCORE_BALANCE));
        valueout.setStatDate(context.getConfiguration().get(STAT_MONTH));
        valueout.setStatMonth(context.getConfiguration().get(STAT_MONTH));
        logger.info("ScoreInfo tostr:"+valueout.toString());
        Text keyout = new Text();
        keyout.set(enttid);
        /*   context.write(keyout, valueout);*/
 
    }

    @Override
    protected void setup(Mapper<BytesWritable, Text, Text, ScoreInfo>.Context context)
            throws IOException, InterruptedException {
        // TODO Auto-generated method stub
        super.setup(context);
        String columnSeparator = context.getConfiguration().get(ScoreInfoDayJob.COLUMN_SEPARATOR);
        String ruleSeparator = context.getConfiguration().get(ScoreInfoDayJob.RULE_SEPARATOR);
        String mappingSeparator = context.getConfiguration().get(ScoreInfoDayJob.MAPPING_SEPARATOR);
        String column = context.getConfiguration().get(ScoreInfoDayJob.SCORE_INFO_COLUMN);
        String filterRule = context.getConfiguration()
                .get(ScoreInfoDayJob.SCORE_INFO_COLUMN_FILTER_RULE);
        String mapping = context.getConfiguration().get(ScoreInfoDayJob.SCORE_INFO_COLUMN_MAPPING);
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

}


/**
 * Revision history
 * -------------------------------------------------------------------------
 * 
 * Date Author Note
 * -------------------------------------------------------------------------
 * 2016年8月17日 alex creat
 */