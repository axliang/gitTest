package com.cmcc.hy.bigdata.weijifen.extractor;

import java.util.regex.Pattern;

import org.apache.hadoop.hive.serde2.columnar.BytesRefArrayWritable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 值提取器
 * 
 * @author Lucifer
 *
 */
public class ValueExtractor {

    private static final Logger logger = LoggerFactory.getLogger(ValueExtractor.class);

    private int index;
    private Pattern rule;
    private String mapping;

    public ValueExtractor(int index, Pattern rule, String mapping) {
        this.index = index;
        this.rule = rule;
        this.mapping = mapping;
    }

    /**
     * 验证并提取
     * 
     * @param input 源数据
     * @return 返回符合规则的列，否则返回null
     */
    public String validateAndExtract(String[] input) {
        if (index >= input.length) {
            return null;
        }
        String value = input[index];
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (rule.matcher(value).matches()) {
            return value;
        } else {
            logger.info(String.format("%s:[%s] is not matched rule", this.mapping, value));
            return null;
        }
    }

    /**
     * 验证并提取(暂未实现)
     * 
     * @param input 源数据
     * @return 返回符合规则的列，否则返回null
     */
    public byte[] validateAndExtract(BytesRefArrayWritable input) {
        return null;
    }

    public int getIndex() {
        return index;
    }

    public Pattern getRule() {
        return rule;
    }

    public String getMapping() {
        return mapping;
    }

}
