package com.cmcc.hy.bigdata.weijifen.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hive.serde2.columnar.BytesRefWritable;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.enums.CityType;
import com.cmcc.hy.bigdata.weijifen.enums.RoamType;

/**
 * 转换工具类
 * 
 * @author Lucifer
 *
 */
public class ConvertUtil {
    /**
     * 系统日志类实例
     */
    private static final Logger logger = LoggerFactory.getLogger(ConvertUtil.class);

    /**
     * 手机号码格式正则表达式
     */
    private static final String MOBILE_PHONE_NUMBER_FORMAT = "^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";

    /**
     * 将以RCFile文件格式存储的hive timestamp列转换成指定时间格式的字符串，目前仅支持timestamp到秒级
     * 
     * @param bytesRefWritable
     *            hive timestamp列数据
     * @param outputPattern
     *            时间输出格式(yyyyMMddHHmmss,etc)
     * @return
     */
    public static String convertHiveTimestamp(BytesRefWritable bytesRefWritable,
                                              String outputPattern) {
        try {
            if (bytesRefWritable.getData() != null) {
                long timestamp = (long) Bytes.toInt(bytesRefWritable.getData(),
                        bytesRefWritable.getStart(), bytesRefWritable.getLength()) * 1000L;
                Date dt = new Date();
                dt.setTime(timestamp);
                return DateUtil.formatDateToStr(dt, outputPattern);
            }
        } catch (Exception e) {
            logger.error("convert hive timestamp column error : " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将Text编码转换成指定编码格式
     * 
     * @param value
     *            内容
     * @param encoding
     *            指定编码
     * @return
     */
    public static String convertEncoding(Text value, String encoding) {
        try {
            String decoded = new String(value.getBytes(), 0, value.getLength(), encoding);
            return decoded;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将双精度的字符串转换成指定格式的字符串
     * 
     * @param doubleStr
     *            源字符串
     * @param df
     *            指定格式
     * @return
     */
    public static String convertDoubleStrFormat(String doubleStr, DecimalFormat df) {
        try {
            if (doubleStr != null) {
                double doubleVal = Double.valueOf(doubleStr);
                return df.format(doubleVal);
            }
        } catch (Exception e) {
            logger.error("Convert String {} to Double error", doubleStr);
        }
        return null;
    }

    /**
     * 十进制数字字符串转换成double型数值
     * 
     * @param number
     * @return
     */
    public static double decimalStringToDouble(String number) {
        if (StringUtil.strIsNull(number)) {
            return 0.0;
        }
        double value;
        try {
            value = Double.parseDouble(number);
            return value;
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return 0.0;
        }
    }

    /**
     * 根据城市名称获取区号
     * 
     * @param cityMap
     * @param roamPlace
     * @return
     */
    public static String cityToAreaNumber(Map<String, String> cityMap, String roamPlace) {
        if (cityMap.containsKey(roamPlace)) {
            String code = cityMap.get(roamPlace);
            return StringUtil.strIsNull(code) ? "" : code;
        } else {
            return "";
        }
    }

    /**
     * 根据漫游地区号判断上海地区的号码的漫游类型
     * 
     * @param code
     * @return
     */
    public static RoamType getShanghaiRoamType(String code) {
        if (StringUtil.strIsNull(code)) {
            return RoamType.UNDEFINED;
        }
        if ("021".equals(code)) {
            return RoamType.LOCAL;
        } else if (Pattern.matches("0[1-9]\\d+", code)) {
            return RoamType.INTERPROVINCIAL;
        } else if (Pattern.matches("00[1-9]\\d*", code)) {
            if ("00852".equals(code) || "00853".equals(code) || "00886".equals(code)) {
                return RoamType.HONGKONG_MACAO_TAIWAN;
            }
            return RoamType.INTERNATIONAL;
        } else {
            return RoamType.UNDEFINED;
        }
    }

    // /**
    // * 根据漫游地区号判断中国大陆的号码的漫游类型
    // *
    // * @param city
    // * @param code
    // * @param otherCity
    // * @param otherCode
    // * @return
    // */
    // public static RoamType getMainLandRoamType(String city, String code,
    // String otherCity, String otherCode) {
    // if (StringUtil.strIsNull(code) || StringUtil.strIsNull(otherCode)
    // || StringUtil.strIsNull(city)
    // || StringUtil.strIsNull(otherCity)) {
    // return RoamType.UNDEFINED;
    // }
    // if (code.equals(otherCode)) {
    // return RoamType.LOCAL;
    // } else {
    // if (city.length() < 2 || otherCity.length() < 2) {
    // return RoamType.UNDEFINED;
    // }
    // if (city.substring(0, 2).equals(otherCity.substring(0, 2))) {
    // return RoamType.PROVINCE;
    // } else if (Pattern.matches("0[1-9]\\d+", otherCode)) {
    // return RoamType.INTERPROVINCIAL;
    // } else if (Pattern.matches("00[1-9]\\d*", otherCode)) {
    // if ("00852".equals(otherCode) || "00853".equals(otherCode)
    // || "00886".equals(otherCode)) {
    // return RoamType.HONGKONG_MACAO_TAIWAN;
    // }
    // return RoamType.INTERNATIONAL;
    // } else {
    // return RoamType.UNDEFINED;
    // }
    // }
    // }

    /**
     * 根据用户归属地和通话时所在城市的对比来确定漫游类型(本地暂定为大陆城市)
     * 
     * @param here
     * @param other
     * @return
     */
    public static RoamType findRoamType(String local, String other, Map<String, String> cityMap) {
        if (StringUtil.strIsNull(local) || StringUtil.strIsNull(other)) {
            return RoamType.UNDEFINED;
        }
        String localCode = cityMap.get(local);
        String otherCode = cityMap.get(other);
        if (localCode == null) {
            return RoamType.UNDEFINED;
        }
        CityType localCityType = CityType.getTypeByCityName(local);
        CityType otherCityType = CityType.getTypeByCityName(other);
        // 先判断是否为港澳台漫游
        if (otherCityType == CityType.HONGKONG_MACAO_TAIWAN) {
            return RoamType.HONGKONG_MACAO_TAIWAN;
        }
        if (null == otherCode) {
            return RoamType.UNDEFINED;
        }
        if (localCityType == CityType.MUNICIPALITY) {
            if (localCode.equals(otherCode)) {
                return RoamType.LOCAL;
            } else if (Pattern.matches("0[1-9]\\d+", otherCode)) {
                return RoamType.INTERPROVINCIAL;
            } else if (Pattern.matches("00[1-9]\\d*", otherCode)) {
                return RoamType.INTERNATIONAL;
            } else {
                return RoamType.UNDEFINED;
            }
        } else if (localCityType == CityType.OTHER) {
            if (localCode.equals(otherCode)) {
                return RoamType.LOCAL;
            } else if (local.startsWith(other.substring(0, 2))) {
                return RoamType.PROVINCE;
            } else if (Pattern.matches("0[1-9]\\d+", otherCode)) {
                return RoamType.INTERPROVINCIAL;
            } else if (Pattern.matches("00[1-9]\\d*", otherCode)) {
                return RoamType.INTERNATIONAL;
            } else {
                return RoamType.UNDEFINED;
            }
        } else {
            return RoamType.UNDEFINED;
        }
    }

    /**
     * 将上海的地址格式（如北京，浙江-杭州）转换为标准地址格式
     * 
     * @param city
     * @param set
     * @return
     */
    public static String cityFormatTrans(String city, Set<String> set,
                                         Map<String, String> cityMap) {
        if (StringUtil.strIsNull(city)) {
            return "";
        }
        CityType ct = CityType.getTypeByCityName(city);
        if (ct == CityType.MUNICIPALITY || ct == CityType.HONGKONG_MACAO_TAIWAN) {
            if ("台北".equals(city)) {
                return "台湾";
            }
            return city.substring(0, 2);
        }
        if ("海南省".equals(city) || "吉林省".equals(city)) {
            return city.substring(0, 2);
        }
        if (city.contains("省") || city.contains("市") || city.contains("区") || city.contains("县")) {
            city = city.replaceAll("[省|市|区|县]", "");
        }
        // 如果只有省份名称，就返回省份名。有两个特殊情况，吉林省有吉林市，还有个海南藏族自治州，简称海南，与海南省同名，这两种情况都假定表示的是城市名称。
        if (set.contains(city)) {
            if ("吉林".equals(city)) {
                return "吉林-吉林";
            } else if ("海南".equals(city)) {
                return "青海-海南";
            }
            return city;
        }
        Iterator<String> iter = set.iterator();
        while (iter.hasNext()) {
            String province = iter.next();
            // 若是以省份名称开头，直接在省份名和城市名之间加"-"
            if (city.startsWith(province)) {
                return city.replace(province, province + "-");
            }
        }
        return cityToProvinceCity(city, cityMap);
    }

    /**
     * 将只有城市名的非直辖市的名称转换成省份-城市名的格式
     * 
     * @param city
     * @param cityMap
     * @return
     */
    public static String cityToProvinceCity(String city, Map<String, String> cityMap) {
        if (StringUtil.strIsNull(city)) {
            return null;
        }
        String code = cityMap.get(city);
        // 如果没有对应省份名称，则直接返回该城市名。
        if (null == code) {
            return city;
        }
        for (Entry<String, String> entry : cityMap.entrySet()) {
            String key = entry.getKey();
            if (city.equals(key)) {
                continue;
            }
            if (code.equals(entry.getValue()) && key.endsWith(city)) {
                return key.replace(city, "-" + city);
            }
        }
        return city;
    }

    /**
     * 将号码转化成正确的手机号码格式，不正确返回null
     * 
     * @description
     * @author hechan
     * @date 2016年3月30日
     * @param number
     * @return 正确的number
     */
    public static String convertMobilePhoneNumber(String number) {
        if (number == null) {
            return null;
        }
        if (number.length() == 11) {
            if (Pattern.matches(MOBILE_PHONE_NUMBER_FORMAT, number)) {
                return number;
            }
        } else if (number.length() > 11) {
            String number11 = number.substring(number.length() - 11);
            if (Pattern.matches(MOBILE_PHONE_NUMBER_FORMAT, number11)) {
                return number11;
            }
        }
        return null;
    }

    /**
     * 将地区名与区号映射文件内容读入Map中
     * 
     * @param filePath
     * @param seperator
     * @return
     */
    public static Map<String, String> getNumberMap(String filePath, String seperator) {
        Map<String, String> dangerMap = new HashMap<String, String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                if (tempString.isEmpty()) {
                    continue;
                }
                // 获取一行的记录
                String[] temp = tempString.split(seperator);
                if (temp.length != 2) {
                    continue;
                }
                if (dangerMap.containsKey(temp[0])) {
                    dangerMap.put(temp[0], dangerMap.get(temp[0]) + ";" + temp[1]);
                } else {
                    dangerMap.put(temp[0], temp[1]);
                }
            }
            reader.close();
            return dangerMap;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return dangerMap;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("Can not close the reader!");
                }
            }
        }
    }

    /**
     * 手机号段到归属地的集合（仅适用于上海）
     * 
     * @param input
     * @return
     */
    public static Map<String, String> phoneSegToAreaCollection(String input) {
        Map<String, String> map = new TreeMap<String, String>();
        BufferedReader br = null;
        String line = null;
        try {
            br = new BufferedReader(new FileReader(input));
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(";");
                if (lines.length != 2) {
                    logger.info("Line {[]} format error!", line);
                    continue;
                }
                if (!Pattern.matches("\\d{7}", lines[0])) {
                    logger.error("Phone segment format error!");
                    continue;
                }
                map.put(lines[0], lines[1]);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取省份名称的集合
     * 
     * @param input
     * @return
     */
    public static Set<String> getProvinceSet(String input) {
        BufferedReader br = null;
        String line = null;
        Set<String> set = new TreeSet<String>();
        try {
            br = new BufferedReader(new FileReader(input));
            while ((line = br.readLine()) != null) {
                if (!"".equals(line)) {
                    set.add(line);
                }
            }
            br.close();
            return set;
        } catch (Exception e) {
            e.printStackTrace();
            return set;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 通过区号获取城市名或国家地区名，如果一个区号对应多个城市，则对应到其中某一个城市（不区分哪一个，因为仅凭区号也无法区分）。
     * 
     * @param code
     * @param cityMap
     * @return
     */
    public static String getCityNameByCode(String code, Map<String, String> cityMap) {
        if (StringUtil.strIsNull(code)) {
            return null;
        }
        for (Entry<String, String> entry : cityMap.entrySet()) {
            if (code.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 将映射文件的内容转成Map，其中映射文件的格式为key	value;value(注分割符一定为\t)
     * 
     * @param input
     * @return Map<String, String>
     */
    public static Map<String, String> getMapping(String input) {
        Map<String, String> map = new HashMap<String, String>();
        BufferedReader br = null;
        String line = null;
        try {
            br = new BufferedReader(new FileReader(input));
            while ((line = br.readLine()) != null) {
                String[] lines = line.split("\t");
                if (lines.length != 2) {
                    logger.info("Line {[]} format error!", line);
                    continue;
                }
                map.put(lines[0], lines[1]);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
