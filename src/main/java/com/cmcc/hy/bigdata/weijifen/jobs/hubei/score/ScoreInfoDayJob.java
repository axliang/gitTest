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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.constants.HBaseTableSchema;
import com.cmcc.hy.bigdata.weijifen.model.ScoreInfo;
import com.cmcc.hy.bigdata.weijifen.util.ConfigurationUtil;
import com.cmcc.hy.bigdata.weijifen.util.DateUtil;
import com.cmcc.hy.bigdata.weijifen.util.FileSystemUtil;

/**
 * @Type ScoreJob.java
 * @Desc  用户积分日表接入
 * @author alex
 * @date 2016年8月17日 下午4:32:49
 * @version 
 */
public class ScoreInfoDayJob  extends Configured implements Tool {
    
    
    /**
     * 系统日志类实例
     */
    private static final Logger logger = LoggerFactory.getLogger(ScoreInfoDayJob.class);
    /**
     * 任务名称
     */
    private static final String JOB_NAME = "HY_ScoreJobImport";
    /**
     * 特定配置文件名
     */
    private static final String SEPCIFIC_CONFIG_NAME = "ScoreInfo.xml";
    private static final String STAT_DAY = "StatDay";


    // 源数据hdfs地址
    private static final String SCORE_INFO_INPUT_PATH = "scoreInfo.inputPath";
    private static final String ACCT_PHONE_MAP_INPUT_PATH = "acctPhoneMap.inputPath";
    

    // 设置列的过滤规则的变量名
    protected static final String SCORE_INFO_COLUMN_FILTER_RULE = "scoreInfo.column.filterRule";
    // 设置hive表需要处理的列位数的变量名
    protected static final String SCORE_INFO_COLUMN = "scoreInfo.column";
    // 设置列所对应的处理方法或者hbase列名的变量名
    protected static final String SCORE_INFO_COLUMN_MAPPING = "scoreInfo.column.mapping";
    
    
    // 设置列的过滤规则的变量名
    protected static final String ACCT_PHONE_COLUMN_FILTER_RULE = "acctPhoneMap.column.filterRule";
    // 设置hive表需要处理的列位数的变量名
    protected static final String ACCT_PHONE_COLUMN = "acctPhoneMap.column";
    // 设置列所对应的处理方法或者hbase列名的变量名
    protected static final String ACCT_PHONE_COLUMN_MAPPING = "acctPhoneMap.column.mapping";

    // 分隔符定义
    protected static final String COLUMN_SEPARATOR = "columnSeparator";
    protected static final String RULE_SEPARATOR = "ruleSeparator";
    protected static final String MAPPING_SEPARATOR = "mappingSeparator";

    // reduce任务梳理
    protected static final String REDUCE_NUMBER = "reduceNumber";

    

    public static void main(String[] args) throws Exception {
        ToolRunner.run(HBaseConfiguration.create(), new ScoreInfoDayJob(), args);
    }

    
    
    @Override
    public int run(String[] args) throws Exception {
        // TODO Auto-generated method stub
        
        Configuration conf = ConfigurationUtil.loginAuthentication(args, SEPCIFIC_CONFIG_NAME,
                getConf());
        
        // 获取过滤时间(年月日)
        String statDate = DateUtil.getFilterDate(args);
        if (statDate == null) {
            System.exit(1);
        }
        
        conf.set(STAT_DAY, statDate);

        // 启动job
        Job job = Job.getInstance(conf, JOB_NAME + ":" + statDate);
        job.setJarByClass(ScoreInfoDayJob.class);
        String scoreInfoInput = conf.get(SCORE_INFO_INPUT_PATH);
        Path scoreInfoPath = new Path(scoreInfoInput);
        
        String acctPhoneMapInfoInput = conf.get(ACCT_PHONE_MAP_INPUT_PATH);
        Path accPhoneMapInfoPath = new Path(acctPhoneMapInfoInput);
        
        // 判断是否存在日表文件
        if (FileSystemUtil.exists(scoreInfoPath)) {
            MultipleInputs.addInputPath(job, scoreInfoPath, SequenceFileInputFormat.class,
                    ScoreInfoDayMapper.class);
            logger.info("SocreInfoPath is " + scoreInfoInput);
        } else {
            logger.error("Path [{}] not exist!", scoreInfoInput);
        }
        
        // 判断是否存在账户和手机号的映射文件
//        if (FileSystemUtil.exists(accPhoneMapInfoPath)) {
//            MultipleInputs.addInputPath(job, accPhoneMapInfoPath, TextInputFormat.class,
//                    AcctPhoneMapper.class);
//            logger.info("AccPhoneMapInfoPath is " + acctPhoneMapInfoInput);
//        } else {
//            logger.error("Path [{}] not exist!", acctPhoneMapInfoInput);
//        }
        // 创建job
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(ScoreInfo.class);
        job.setNumReduceTasks(conf.getInt(REDUCE_NUMBER, 40));
        job.setOutputFormatClass(NullOutputFormat.class);

//        TableMapReduceUtil.initTableReducerJob(HBaseTableSchema.USER_INFO_TABLE2,
//                ScoreInfoDayReducer.class, job);

        
        
        return (job.waitForCompletion(true) ? 0 : 1);
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