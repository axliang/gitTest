package com.cmcc.hy.bigdata.weijifen.jobs.guizhousy.userinfomonth;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.constants.ConfigConstants;
import com.cmcc.hy.bigdata.weijifen.extractor.ValueExtractor;
import com.cmcc.hy.bigdata.weijifen.extractor.ValueExtractorFactory;
import com.cmcc.hy.bigdata.weijifen.model.UserMonthInfo;
import com.cmcc.hy.bigdata.weijifen.util.ConvertUtil;

/**
 * 用户星级导入的map过程
 * 
 * @author Lucifer
 *
 */
public class UserStarMapper extends Mapper<LongWritable, Text, Text, UserMonthInfo> {

    private static final Logger logger = LoggerFactory.getLogger(UserStarMapper.class);

    private static final String USER_STAR_COLUMN = "userStar.column";
    private static final String USER_STAR_COLUMN_FILTER_RULE = "userStar.column.filterRule";
    private static final String USER_STAR_COLUMN_MAPPING = "userStar.column.mapping";

    private static final String PRODUCT_NO = "product_no";
    private static final String STAR_LEV = "star_lev";

    private String columnSeparator;
    private String ruleSeparator;
    private String mappingSeparator;
    private String hiveColumn;
    private String filterRule;
    private String mapping;

    private List<ValueExtractor> extractors;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        columnSeparator = context.getConfiguration().get(ConfigConstants.COLUMN_SEPARATOR);
        ruleSeparator = context.getConfiguration().get(ConfigConstants.RULE_SEPARATOR);
        mappingSeparator = context.getConfiguration().get(ConfigConstants.MAPPING_SEPARATOR);
        hiveColumn = context.getConfiguration().get(USER_STAR_COLUMN);
        filterRule = context.getConfiguration().get(USER_STAR_COLUMN_FILTER_RULE);
        mapping = context.getConfiguration().get(USER_STAR_COLUMN_MAPPING);
        String[] columns = hiveColumn.split(columnSeparator);
        String[] rules = filterRule.split(ruleSeparator);
        String[] mappings = mapping.split(mappingSeparator);
        try {
            extractors = ValueExtractorFactory.makeInstances(columns, rules, mappings);
        } catch (Exception e) {
            logger.error("construct extractors fail : {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String converted = ConvertUtil.convertEncoding(value, "UTF-8");
        if (converted == null) {
            return;
        }
        String[] values = converted.split("\t");

        Map<String, String> map = new HashMap<String, String>();
        for (ValueExtractor extractor : extractors) {
            String result = extractor.validateAndExtract(values);
            map.put(extractor.getMapping(), result);
        }
        UserMonthInfo valueout = new UserMonthInfo();
        valueout.setPhoneNo(ConvertUtil.convertMobilePhoneNumber(map.get(PRODUCT_NO)));
        valueout.setStarLevel(map.get(STAR_LEV));
        if (valueout.getPhoneNo() == null || valueout.getStarLevel() == null) {
            return;
        }
        Text keyout = new Text();
        keyout.set(valueout.getPhoneNo());
        context.write(keyout, valueout);
    }
}
