package com.cmcc.hy.bigdata.weijifen.util;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.constants.HBaseTableSchema;

/**
 * HBase工具类，主要包含HBase操作的一些公用方法
 *
 * @Project: SHZA-mapreduce-analysis
 * @File: HBaseUtil.java
 * @Date: 2015年9月17日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public class HBaseUtil {

    private static final Logger logger = LoggerFactory.getLogger(HBaseUtil.class);

    public static String getStringColumnValue(Result value, byte[] family, byte[] qualifier) {
        byte[] result = getColumnValueIfExist(value, family, qualifier);
        return result == null ? null : Bytes.toString(result);
    }

    public static Double getDoubleColumnValue(Result value, byte[] family, byte[] qualifier) {
        byte[] result = getColumnValueIfExist(value, family, qualifier);
        try {
            return result == null ? null : Bytes.toDouble(result);
        } catch (Exception e) {
            logger.error("Error cannot convert [{}] to double : ", Bytes.toString(result));
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据列族和列判定结果集中是否含有此列，若有则返回最新结果，否则返回null
     * 
     * @param value
     *            结果集
     * @param family
     *            列族
     * @param qualifier
     *            列名
     * @return 返回单元结果
     */
    private static byte[] getColumnValueIfExist(Result value, byte[] family, byte[] qualifier) {
        if (value.containsColumn(family, qualifier)) {
            return value.getValue(family, qualifier);
        }
        return null;
    }

    /**
     * 添加字符串值到Put
     * 
     * @param put
     * @param family 列族
     * @param qualifier 列名
     * @param value 要添加的字符串值
     */
    public static void addColumn(Put put, String family, String qualifier, String value) {
        put.add(HBaseTableSchema.toBytes(family), HBaseTableSchema.toBytes(qualifier),
                Bytes.toBytes(value));
    }

    /**
     * 添加整型值到Put
     * 
     * @param put
     * @param family 列族
     * @param qualifier 列名
     * @param value 要添加的整型
     */
    public static void addColumn(Put put, String family, String qualifier, int value) {
        put.add(HBaseTableSchema.toBytes(family), HBaseTableSchema.toBytes(qualifier),
                Bytes.toBytes(value));
    }

    /**
     * 添加双精度浮点值到Put
     * 
     * @param put
     * @param family 列族
     * @param qualifier 列名
     * @param value 要添加的双精度浮点值
     */
    public static void addColumn(Put put, String family, String qualifier, double value) {
        put.add(HBaseTableSchema.toBytes(family), HBaseTableSchema.toBytes(qualifier),
                Bytes.toBytes(value));
    }

    /**
     * 添加已转换的字节数组到Put
     * 
     * @param put
     * @param family 列族
     * @param qualifier 列名
     * @param value 已转换成字节数组的值
     */
    public static void addColumn(Put put, String family, String qualifier, byte[] value) {
        put.add(HBaseTableSchema.toBytes(family), HBaseTableSchema.toBytes(qualifier), value);
    }

    /**
     * 添加查询限定条件
     * 
     * @param get
     * @param family 列族
     * @param qualifier 列名
     */
    public static void addColumn(Get get, String family, String qualifier) {
        get.addColumn(HBaseTableSchema.toBytes(family), HBaseTableSchema.toBytes(qualifier));
    }

    /**
     * 判定结果集是否包含指定列族的列
     * 
     * @param result
     * @param family 列族
     * @param qualifier 列名
     * @return
     */
    public static boolean containsColumn(Result result, String family, String qualifier) {
        return result.containsColumn(HBaseTableSchema.toBytes(family),
                HBaseTableSchema.toBytes(qualifier));
    }
}
