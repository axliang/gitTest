package com.cmcc.hy.bigdata.weijifen.jobs.hubei.score.test;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.mapreduce.KeyValueSerialization;
import org.apache.hadoop.hbase.mapreduce.MutationSerialization;
import org.apache.hadoop.hbase.mapreduce.ResultSerialization;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.jobs.hubei.score.ScoreInfoDayMapper;
import com.cmcc.hy.bigdata.weijifen.model.ScoreInfo;
import com.cmcc.hy.bigdata.weijifen.util.FileSystemUtil;

public class TestScoreMapper {
	/*
    //    BytesWritable, Text, Text, ScoreInfo
    MapDriver<BytesWritable, Text, Text, ScoreInfo> mapDriver;   
	*//**
	 * 系统日志类实例logger
	 *//*
	private static final Logger logger = LoggerFactory
			.getLogger(TestScoreMapper.class);


	*//**
	 * MapReduce任务名称
	 *//*
	private static final String NAME = "UserPositionAnalysis";

	// 维护输出字符串
	protected static String END_DATE = "endDate";
	protected static String START_DATE = "startDate";
	protected static String EFFECT_DATE = "effectDate";

	private static String REDUCE_NUMBER = "reduceNumber";
	private static String SCAN_CACHE_NUMBER = "scanCacheNumber";

	private static String FIXED_ADDRESS_STATISTIC_DAYS = "fixedAddressStatisticDays";
	private static String FIXED_SAVE_DAYS = "fixedSaveDays";
	private static String ACTIVE_SAVE_DAYS = "activeSaveDays";

	protected static String ANALYSIS_MODE = "analysisMode";
	// 设置统计日期范围内的双休日天数和工作日天数
	protected static String NUM_OF_WEEKEND = "numOfWeekend";
	protected static String NUM_OF_WEEKDAY = "numOfWeekday";

	private static String SCAN_CACHE_BLOCKS = "scanCacheBlocks";
	
	

    @Before  
    public void setup()   
    {   
    	ScoreMapper mapper = new ScoreMapper();   
//        mapDriver = MapDriver.newMapDriver(mapper);   
        mapDriver = MultipleInputsMapDriver. 
        Configuration conf = mapDriver.getConfiguration();
		conf.setStrings("io.serializations", conf.get("io.serializations"), MutationSerialization.class.getName(),
				ResultSerialization.class.getName(), KeyValueSerialization.class.getName());
		
    	conf.set("hbase.zookeeper.quorum", "cdhlocal");
    //        conf.set("hbase.zookeeper.quorum", "192.168.12.66,192.168.12.67,192.168.12.68,192.168.12.69,192.168.12.70");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        
    }   
	
	
	
	
	   @Test  
	    public void testMapper() throws IOException   
	    {   
	        Path billInfoPath = new Path("");
	      
	        boolean scoreInfoExists = true;
	        if (FileSystemUtil.exists(billInfoPath)) {
	            scoreInfoExists = true;
	            // MultipleInputs.addInputPath(job, billInfoPath,
	            // SequenceFileInputFormat.class, ScoreMapper.class);
	            MultipleInputs.addInputPath(mapDriver, billInfoPath, SequenceFileInputFormat.class,
	                    ScoreMapper.class);
	            logger.info("SocreInfoPath is " + billInfoInput);
	        } else {
	            logger.error("Path [{}] not exist!", billInfoPath);
	        }
	        job.setMapOutputKeyClass(Text.class);
	        job.setMapOutputValueClass(ScoreInfo.class);
//	        job.setNumReduceTasks(conf.getInt(REDUCE_NUMBER, 1));
	        return (job.waitForCompletion(true) ? 0 : 1);
		        
		        mapDriver.withInput(key, result);
		      
		        mapDriver.withOutput(new Text("1E5C2F0788C4831A8EC4C0D655E7AD48"), new Text("20160502110806-121.532427,30.940250-532"));
		        mapDriver.runTest();   
	    }   
*/
}
