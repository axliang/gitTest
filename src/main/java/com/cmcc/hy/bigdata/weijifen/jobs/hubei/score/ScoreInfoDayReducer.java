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
import java.util.ArrayList;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.constants.HBaseTableSchema;
import com.cmcc.hy.bigdata.weijifen.enums.CertType;
import com.cmcc.hy.bigdata.weijifen.enums.SexType;
import com.cmcc.hy.bigdata.weijifen.model.ScoreInfo;
import com.cmcc.hy.bigdata.weijifen.model.UserBasicInfo;
import com.cmcc.hy.bigdata.weijifen.util.DateUtil;
import com.cmcc.hy.bigdata.weijifen.util.HBaseUtil;
import com.cmcc.hy.bigdata.weijifen.util.StringUtil;
import com.cmcc.hy.bigdata.weijifen.util.ValidationUtil;

/**
 * @Type ScoreReducer.java
 * @Desc 
 * @author alex
 * @date 2016年8月17日 下午4:33:37
 * @version 
 */
public class ScoreInfoDayReducer extends TableReducer<Text, ScoreInfo, ImmutableBytesWritable> {

    /**
     * 系统日志类实例
     */
    private static final Logger logger = LoggerFactory.getLogger(ScoreInfoDayReducer.class);
    
    @Override
    protected void reduce(Text key, Iterable<ScoreInfo> values, Context context)
            throws IOException, InterruptedException {

        String phoneNo="";
        String entt_Id= "";
        long points = 0l; 
        int count_tmp =0;
        for (ScoreInfo val : values) {
            if (StringUtil.strIsNotNull(val.getPhoneNo())) {
                phoneNo = val.getPhoneNo();
            } else {
                points = points + Long.valueOf(val.getScoreBalance());
                if( count_tmp == 0){
                    entt_Id =val.getEnttId();
                }
                count_tmp ++;
                logger.info(" entt_Id："+entt_Id+" points:"+points +" after reduce time:"+count_tmp);
            }
        }
        
        if (!"".equals(phoneNo)) {
            
            byte[] rowkeyPhoneNO = Bytes.toBytes(phoneNo);
            Put output = new Put(rowkeyPhoneNO);
            
            HBaseUtil.addColumn(output, HBaseTableSchema.CF_BASICINFO, HBaseTableSchema.QL_POINTS,
                    points);
            context.write(new ImmutableBytesWritable(Bytes.toBytes(phoneNo)), output);
        }else{
            logger.error(" entt_Id："+entt_Id+" didnt map a phoneNumber");
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