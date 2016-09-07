package com.cmcc.hy.bigdata.weijifen.jobs.hubei.score.test;

import java.io.IOException;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.KeyValueSerialization;
import org.apache.hadoop.hbase.mapreduce.MutationSerialization;
import org.apache.hadoop.hbase.mapreduce.ResultSerialization;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.constants.HBaseTableSchema;
import com.cmcc.hy.bigdata.weijifen.jobs.hubei.score.ScoreInfoDayMapper;
import com.cmcc.hy.bigdata.weijifen.model.ScoreInfo;
import com.cmcc.hy.bigdata.weijifen.util.DateUtil;

public class TestScoreJob {
	
    /*    //    BytesWritable, Text, Text, ScoreInfo
		MapDriver<BytesWritable, Text, Text, ScoreInfo> mapDriver;   
	    MapReduceDriver<ImmutableBytesWritable, Result, Text, Text, ImmutableBytesWritable, Mutation> mapReduceDriver;   
	    
	    
		*//**
		 * 系统日志类实例logger
		 *//*
		private static final Logger logger = LoggerFactory
				.getLogger(TestScoreJob.class);

		private static final String SPECIFIC_CONFIG_FILE_NAME = "UserPosition.xml";

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
	    	UserPositionReducer reducer = new UserPositionReducer();   
	    	
	        mapDriver = MapDriver.newMapDriver(mapper);   
	        
	        Configuration conf = mapReduceDriver.getConfiguration();
	        conf.set("hbase.zookeeper.quorum", "cdhlocal");
//	        conf.set("hbase.zookeeper.quorum", "192.168.12.66,192.168.12.67,192.168.12.68,192.168.12.69,192.168.12.70");
	        conf.set("hbase.zookeeper.property.clientPort", "2181");
			conf.setStrings("io.serializations", conf.get("io.serializations"), MutationSerialization.class.getName(),
					ResultSerialization.class.getName(), KeyValueSerialization.class.getName());

			String analysisMode = "fixed";
			String dateAnalysis = "20160509";
			String endDate = "";
			String startDate = "";
			String effectDate = "";
			String[] date = DateUtil.calculateStartDayAndEndDay(dateAnalysis,
					conf.getInt(FIXED_ADDRESS_STATISTIC_DAYS, 60));
			endDate = date[0];
			startDate = date[1];// active模式计算不需要此参数
			if (analysisMode.equals("fixed")) {
				date = DateUtil.calculateStartDayAndEndDay(null,
						conf.getInt(FIXED_SAVE_DAYS, 365));
			} else {
				date = DateUtil.calculateStartDayAndEndDay(null,
						conf.getInt(ACTIVE_SAVE_DAYS, 60));
			}
			effectDate = date[1];
	
			if (endDate.compareTo(effectDate) < 0) {
				logger.error("The input date is not belong to compute section!");
				return ;
			}
	
			conf.set(ANALYSIS_MODE, analysisMode);
			conf.set(END_DATE, endDate);
			conf.set(START_DATE, startDate);
			conf.set(EFFECT_DATE, effectDate);
	
			String weekCalResult = DateUtil.calculateWeekendAndWeekday(startDate,
					endDate);
			String numOfWeekend = weekCalResult.split(";")[0];
			String numOfWeekday = weekCalResult.split(";")[1];
			conf.set(NUM_OF_WEEKEND, numOfWeekend);
			conf.set(NUM_OF_WEEKDAY, numOfWeekday);
	        
	    }   
	    
	    @Test
	    public void testSampleHBaseMapperReducer() {
	        
	    	String test_key = "13501631113-20160502110806";
	        
	        // Setup Test Record(key, result list)
	        ImmutableBytesWritable key = new ImmutableBytesWritable(Bytes.toBytes(test_key));
	        TreeSet<KeyValue> set = new TreeSet<KeyValue>(KeyValue.COMPARATOR);

	        byte[] row = Bytes.toBytes(test_key);
	        byte[] cf = HBaseTableSchema.CF_POSITION;
	        set.add(new KeyValue(row, cf, HBaseTableSchema.QL_LACCOORDINATE, Bytes.toBytes("121.532427,30.940250")));
	        set.add(new KeyValue(row, cf, HBaseTableSchema.QL_DURATION, Bytes.toBytes("532")));

	        KeyValue[] kvs = new KeyValue[set.size()];
	        set.toArray(kvs);
	        Result result = new Result(kvs);
	      
	 
	       
	        try {
			    mapReduceDriver.withInput(key, result);
//			    mapDriver.withOutput(new Text("1E5C2F0788C4831A8EC4C0D655E7AD48"), new Text("20160502110806-121.532427,30.940250-532"));
//			    mapDriver.runTest();
			    mapReduceDriver.run();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	       */
	
	

}
