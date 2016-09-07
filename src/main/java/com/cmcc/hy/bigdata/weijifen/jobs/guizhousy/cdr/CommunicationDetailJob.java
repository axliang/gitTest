package com.cmcc.hy.bigdata.weijifen.jobs.guizhousy.cdr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.IdentityTableReducer;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.constants.HBaseTableSchema;
import com.cmcc.hy.bigdata.weijifen.util.ConfigurationUtil;
import com.cmcc.hy.bigdata.weijifen.util.DateUtil;
import com.cmcc.hy.bigdata.weijifen.util.FileSystemUtil;

/**
 * @Project: credit-collection-hivedata
 * @File: CommunicationDetailJob.java
 * @Date: 2016年07月22日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 */
public class CommunicationDetailJob extends Configured implements Tool {
    /**
     * 系统日志类实例
     */
    private static final Logger logger = LoggerFactory.getLogger(CommunicationDetailJob.class);

    private static final String JOB_NAME = "HY_CommunicationDetailImport";

    private static final String SPECIFIC_CONFIG_FILE_NAME = "CommunicationDetail.xml";

    // 源数据hdfs地址
    private static final String CALL_INFO_INPUT_PATH = "callInfo.inputPath";
    private static final String SMS_INFO_INPUT_PATH = "smsInfo.inputPath";

    // reduce个数设定
    protected static final String REDUCE_NUMBER = "reduceNumber";

    public static void main(String[] args) throws Exception {
        ToolRunner.run(HBaseConfiguration.create(), new CommunicationDetailJob(), args);
    }

    @Override
    public int run(String[] args) throws Exception {
        // 登陆验证
        Configuration conf = ConfigurationUtil.loginAuthentication(args, SPECIFIC_CONFIG_FILE_NAME,
                getConf());
        String filterDate = DateUtil.getFilterDate(args);
        if (filterDate == null) {
            System.exit(1);
        }
        // 创建job
        Job job = Job.getInstance(conf, JOB_NAME + ":" + filterDate);
        job.setJarByClass(CommunicationDetailJob.class);
        // 创建日历类对象
        boolean callExist = false;
        String callInfoMapperInput = String.format(conf.get(CALL_INFO_INPUT_PATH), filterDate);
        Path callInfoPath = new Path(callInfoMapperInput);
        if (FileSystemUtil.exists(callInfoPath)) {
            MultipleInputs.addInputPath(job, callInfoPath, TextInputFormat.class,
                    CallInfoMapper.class);
            logger.info("CallInfoMapperInput is " + callInfoMapperInput);
            callExist = true;
        } else {
            logger.error("Path [{}] not exist.", callInfoMapperInput);
        }

        // 判断文件路径是否存在
        boolean smsExist = false;
        String smsInfoMapperInput = String.format(conf.get(SMS_INFO_INPUT_PATH), filterDate);
        Path smsInfoPath = new Path(smsInfoMapperInput);
        if (FileSystemUtil.exists(smsInfoPath)) {
            MultipleInputs.addInputPath(job, smsInfoPath, TextInputFormat.class,
                    SmsInfoMapper.class);
            logger.info("SmsInfoMapperInput is " + smsInfoMapperInput);
            callExist = true;
        } else {
            logger.error("Path [{}] not exist.", smsInfoMapperInput);
        }

        if (!callExist && !smsExist) {
            logger.info("Both call and sms not exist, stop task");
            System.exit(1);
        }

        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);
        TableMapReduceUtil.initTableReducerJob(HBaseTableSchema.USER_COMMUNICATION_DETAIL_TABLE,
                IdentityTableReducer.class, job);
        job.setNumReduceTasks(conf.getInt(REDUCE_NUMBER, 0));

        return (job.waitForCompletion(true) ? 0 : 1);
    }

}
