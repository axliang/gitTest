package com.cmcc.hy.bigdata.weijifen.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 值提取器工厂
 * 
 * @author Lucifer
 *
 */
public class ValueExtractorFactory {

    /**
     * 初始化值提取器列表
     * 
     * @param columns 列序号定义
     * @param rules 规则定义
     * @param mappings 映射定义
     * @return 提取器列表
     * @throws Exception
     */
    public static List<ValueExtractor> makeInstances(String[] columns, String[] rules,
                                                     String[] mappings) throws Exception {
        if (columns == null || columns.length == 0 || rules == null || rules.length == 0
                || mappings == null || mappings.length == 0) {
            throw new Exception(
                    "Construct SequenceFileValueExtractor list fail : columns,rules and mappings configuration at lease one is empty");
        }
        int size = columns.length;
        if (size != rules.length || size != mappings.length) {
            throw new Exception(
                    "Construct SequenceFileValueExtractor list fail : columns,rules and mappings configuration size not match");
        }
        List<ValueExtractor> extractors = new ArrayList<ValueExtractor>(size);
        for (int i = 0; i < size; i++) {
            try {
                int index = Integer.parseInt(columns[i]);
                Pattern rule = Pattern.compile(rules[i]);
                String mapping = mappings[i];
                extractors.add(new ValueExtractor(index, rule, mapping));
            } catch (Exception e) {
                throw new Exception(
                        "Construct SequenceFileValueExtractor list fail : " + e.getMessage(), e);
            }
        }
        return extractors;
    }
}
