package com.cmcc.hy.bigdata.weijifen.jobs.hubei.score.test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.KeyValueSerialization;
import org.apache.hadoop.hbase.mapreduce.MutationSerialization;
import org.apache.hadoop.hbase.mapreduce.ResultSerialization;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.constants.HBaseTableSchema;
import com.cmcc.hy.bigdata.weijifen.util.DateUtil;

public class TestUserPositionReducer {
	
    /*
	ReduceDriver<Text, Text, ImmutableBytesWritable,Mutation> reduceDriver;
	Configuration conf;
	*//**
	 * 系统日志类实例logger
	 *//*
	private static final Logger logger = LoggerFactory
			.getLogger(TestUserPositionReducer.class);


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
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    @Before  
    public void setup()   
    {   
    	UserPositionReducer reducer = new UserPositionReducer();   
    	reduceDriver = ReduceDriver.newReduceDriver(reducer);   
    	conf = reduceDriver.getConfiguration();
//        conf.set("hbase.zookeeper.quorum", "192.168.12.66,192.168.12.67,192.168.12.68,192.168.12.69,192.168.12.70");
        conf.set("hbase.zookeeper.quorum", "cdhlocal");
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
	    public void testReducer() throws IOException, ParseException   
	    {   
			   List<Text> values = new ArrayList<Text>();
			   Text key =new Text("20160502110806-121.532427,30.940250-532");
			   values.add(key);
		     
			    reduceDriver.withInput(key, values);
			    
		    	byte[] workValue = Bytes.toBytes("20160509:121.532427,30.94025");
		    	byte[] expectedrowKey = Bytes.toBytes(key.toString());
				ImmutableBytesWritable expectedKey = new ImmutableBytesWritable(expectedrowKey);
		    	
		    	Put put = new Put(expectedrowKey);
				put.add(HBaseTableSchema.CF_TEMPINFO,
						HBaseTableSchema.QL_WORKCOORDINATELIST, workValue);
				reduceDriver.withOutput(expectedKey, put);
				reduceDriver.runTest();
		        
	    }
	   
	   
	   @Test  
	    public void testReducer2() throws IOException, ParseException   
	    {   
			   List<Text> values = new ArrayList<Text>();
			   Text key =new Text("20160502110806-121.532427,30.940250-532");
			   values.add(key);
		     
			    reduceDriver.withInput(key, values);
				ImmutableBytesWritable expectedKey = new ImmutableBytesWritable(Bytes.toBytes(key.toString()));
				
				List<Pair<ImmutableBytesWritable, Mutation>> result =reduceDriver.run();  
				Pair<ImmutableBytesWritable, Mutation> resultPair = result.get(0);
				
				Assert.assertEquals(expectedKey, resultPair.getFirst());
				Mutation actualPut = resultPair.getSecond();
				CellScanner cs = actualPut.cellScanner();
				while (cs.advance()) {
					Cell actualCell = cs.current();
					//20160502110806-121.532427,30.940250-532/tempInfo:workCoordinateList/LATEST_TIMESTAMP/Put/vlen=28/mvcc=0
					if (Arrays.equals(CellUtil.cloneFamily(actualCell), HBaseTableSchema.CF_TEMPINFO)
							&& Arrays.equals(CellUtil.cloneQualifier(actualCell), HBaseTableSchema.QL_WORKCOORDINATELIST)) {
//						System.out.println(Bytes.toString(CellUtil.cloneValue(actualCell))) ;
						Assert.assertArrayEquals(Bytes.toBytes("20160509:121.532427,30.94025"),CellUtil.cloneValue(actualCell));
					}  else {
						Assert.fail("Not expected qualifier");
					}
				}
		        
		        
	    }   */

}
