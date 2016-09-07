package com.cmcc.hy.bigdata.weijifen.jobs.guizhousy.userinfoday;

import java.util.Calendar;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.constants.HBaseTableSchema;
import com.cmcc.hy.bigdata.weijifen.converter.EnumConverter;
import com.cmcc.hy.bigdata.weijifen.converter.GuizhouEnumConverter;
import com.cmcc.hy.bigdata.weijifen.model.UserBasicInfo;
import com.cmcc.hy.bigdata.weijifen.util.ConfigurationUtil;
import com.cmcc.hy.bigdata.weijifen.util.DateUtil;
import com.cmcc.hy.bigdata.weijifen.util.FileSystemUtil;

/**
 * 用户日数据导入任务启动类
 *
 * @Project: credit-collection-hivedata
 * @File: UserInfoDayJob.java
 * @Date: 2016年3月13日
 * @Author: hechan
 * @Copyright: 版权所有 (C) 2016 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public class UserInfoDayJob extends Configured implements Tool {
    /**
     * 系统日志类实例logger
     */
    private static final Logger logger = LoggerFactory.getLogger(UserInfoDayJob.class);

    private static final String JOB_NAME = "HY_UserInfoDayImport";

    private static final String SPECIFIC_CONFIG_FILE_NAME = "UserInfoDay.xml";

    // 源数据hdfs地址
    private static final String USER_INFO_INPUT_PATH = "userInfo.inputPath";
    private static final String CUST_INFO_INPUT_PATH = "custInfo.inputPath";

    // 设置列的过滤规则的变量名
    protected static final String USER_INFO_COLUMN_FILTER_RULE = "userInfo.column.filterRule";
    protected static final String CUST_INFO_COLUMN_FILTER_RULE = "custInfo.column.filterRule";
    // 设置hive表需要处理的列位数的变量名
    protected static final String USER_INFO_COLUMN = "userInfo.column";
    protected static final String CUST_INFO_COLUMN = "custInfo.column";
    // 设置列所对应的处理方法或者hbase列名的变量名
    protected static final String USER_INFO_COLUMN_MAPPING = "userInfo.column.mapping";
    protected static final String CUST_INFO_COLUMN_MAPPING = "custInfo.column.mapping";

    // 分隔符定义
    protected static final String COLUMN_SEPARATOR = "columnSeparator";
    protected static final String RULE_SEPARATOR = "ruleSeparator";
    protected static final String MAPPING_SEPARATOR = "mappingSeparator";

    // reduce任务梳理
    protected static final String REDUCE_NUMBER = "reduceNumber";
    // // 统计日期
    // protected static final String STAT_DATE = "statDate";
    //
    // 回溯源文件最大天数
    private static final int BACK_DAYS = 30;

    // 贵州值归一化转换器
    protected static final EnumConverter CONVERTER = new GuizhouEnumConverter();

    public static void main(String[] args) throws Exception {
        ToolRunner.run(HBaseConfiguration.create(), new UserInfoDayJob(), args);
    }

    @Override
    public int run(String[] args) throws Exception {
        // 登陆验证
        Configuration conf = ConfigurationUtil.loginAuthentication(args, SPECIFIC_CONFIG_FILE_NAME,
                getConf());

        // 获取过滤时间(年月日)
        String filterDate = DateUtil.getFilterDate(args);
        if (filterDate == null) {
            System.exit(1);
        }

        // 创建job
        Job job = Job.getInstance(conf, JOB_NAME + ":" + filterDate);
        job.setJarByClass(UserInfoDayJob.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(UserBasicInfo.class);
        job.setNumReduceTasks(conf.getInt(REDUCE_NUMBER, 40));

        TableMapReduceUtil.initTableReducerJob(HBaseTableSchema.USER_INFO_TABLE,
                UserInfoDayReducer.class, job);

        // 判断文件路径是否存在
        String userInfoMapperInput = conf.get(USER_INFO_INPUT_PATH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStr(filterDate, DateUtil.YMD_PATTERN));
        int backDays = BACK_DAYS;
        while (backDays > 0) {
            String statDate = DateUtil.formatDateToStr(calendar.getTime(), DateUtil.YMD_PATTERN);
            Path userInfoPath = new Path(String.format(userInfoMapperInput, statDate));
            if (FileSystemUtil.exists(userInfoPath)) {
                MultipleInputs.addInputPath(job, userInfoPath, TextInputFormat.class,
                        UserInfoMapper.class);
                logger.info("userInfo:" + userInfoPath);
                break;
            } else {
                calendar.add(Calendar.DATE, -1);
                backDays--;
            }
        }
        if (backDays == 0) {
            logger.error("In the past [{}] days, no exists user information!", BACK_DAYS);
            System.exit(1);
        }

        String custInfoMapperInput = conf.get(CUST_INFO_INPUT_PATH);
        calendar.setTime(DateUtil.getDateFromStr(filterDate, DateUtil.YMD_PATTERN));
        backDays = BACK_DAYS;
        while (backDays > 0) {
            String statDate = DateUtil.formatDateToStr(calendar.getTime(), DateUtil.YMD_PATTERN);
            Path custInfoPath = new Path(String.format(custInfoMapperInput, statDate));
            if (FileSystemUtil.exists(custInfoPath)) {
                MultipleInputs.addInputPath(job, custInfoPath, TextInputFormat.class,
                        CustInfoMapper.class);
                logger.info("userInfo:" + custInfoPath);
                break;
            } else {
                calendar.add(Calendar.DATE, -1);
                backDays--;
            }
        }
        if (backDays == 0) {
            logger.error("In the past [{}] days, no exists cust information!", BACK_DAYS);
            System.exit(1);
        }

        return (job.waitForCompletion(true) ? 0 : 1);
    }
}
