package com.cmcc.hy.bigdata.weijifen.jobs.guizhousy.userinfomonth;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.MultiTableOutputFormat;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.model.UserMonthInfo;
import com.cmcc.hy.bigdata.weijifen.util.ConfigurationUtil;
import com.cmcc.hy.bigdata.weijifen.util.DateUtil;
import com.cmcc.hy.bigdata.weijifen.util.FileSystemUtil;

/**
 * 用户资料月表导入任务启动类
 *
 * @Project: credit-collection-hivedata
 * @File: UserInfoMonthJob.java
 * @Date: 2016年3月29日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2016 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public class UserInfoMonthJob extends Configured implements Tool {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoMonthJob.class);

    private static final String SPECIFIC_CONFIG_FILE_NAME = "UserInfoMonth.xml";

    private static final String JOB_NAME = "HY_UserInfoMonthImport";
    private static final String REDUCE_NUMBER = "reduceNumber";

    // 源数据hdfs地址
    private static final String USER_MONTH_INFO_INPUT_PATH = "userMonthInfo.inputPath";
    private static final String USER_MONTH_BILL_INPUT_PATH = "userMonthBill.inputPath";
    private static final String USER_STAR_INPUT_PATH = "userStar.inputPath";

    public static void main(String[] args) throws Exception {
        ToolRunner.run(HBaseConfiguration.create(), new UserInfoMonthJob(), args);
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = ConfigurationUtil.loginAuthentication(args, SPECIFIC_CONFIG_FILE_NAME,
                getConf());

        String filterMonth = DateUtil.getFilterMonth(args);
        if (filterMonth == null) {
            System.exit(1);
        }
        Job job = Job.getInstance(conf, JOB_NAME + ":" + filterMonth);
        job.setJarByClass(UserInfoMonthJob.class);
        // 用户资料月表
        String userMonthInfoInput = String.format(conf.get(USER_MONTH_INFO_INPUT_PATH),
                filterMonth.substring(0, 4), filterMonth.substring(4));
        Path userMonthInfoPath = new Path(userMonthInfoInput);
        boolean monthInfoDataExist = false;
        if (FileSystemUtil.exists(userMonthInfoPath)) {
            MultipleInputs.addInputPath(job, userMonthInfoPath, TextInputFormat.class,
                    UserMonthInfoMapper.class);
            logger.info("UserMonthInfoPath is " + userMonthInfoInput);
            monthInfoDataExist = true;
        } else {
            logger.error("Path [{}] not exist.", userMonthInfoInput);
        }
        // 用户月结账单
        String userMonthBillInput = String.format(conf.get(USER_MONTH_BILL_INPUT_PATH),
                filterMonth);
        Path userMonthBillPath = new Path(userMonthBillInput);
        boolean monthBillDataExist = false;
        if (FileSystemUtil.exists(userMonthBillPath)) {
            MultipleInputs.addInputPath(job, userMonthBillPath, TextInputFormat.class,
                    UserMonthBillMapper.class);
            logger.info("UserMonthBillPath is " + userMonthBillInput);
            monthBillDataExist = true;
        } else {
            logger.error("Path [{}] not exist.", userMonthBillInput);
        }
        // 用户星级
        String userStarInput = String.format(conf.get(USER_STAR_INPUT_PATH), filterMonth);
        Path userStarPath = new Path(userStarInput);
        boolean userStarDataExist = false;
        if (FileSystemUtil.exists(userStarPath)) {
            MultipleInputs.addInputPath(job, userStarPath, TextInputFormat.class,
                    UserStarMapper.class);
            logger.info("UserStarPath is " + userStarInput);
            userStarDataExist = true;
        } else {
            logger.error("Path [{}] not exist.", userStarInput);
        }
        if (!monthInfoDataExist && !monthBillDataExist && !userStarDataExist) {
            logger.info("All data is absent,stop task");
            System.exit(1);
        }

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(UserMonthInfo.class);

        job.setOutputFormatClass(MultiTableOutputFormat.class);
        job.setReducerClass(UserMonthInfoReducer.class);
        job.setNumReduceTasks(conf.getInt(REDUCE_NUMBER, 1));

        return (job.waitForCompletion(true) ? 0 : 1);
    }

}
