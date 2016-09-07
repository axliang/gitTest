package com.cmcc.hy.bigdata.weijifen.constants;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * HBase table schema
 *
 * @Project: credit-mapreduce-analysis
 * @File: HBaseTableSchema.java
 * @Date: 2015年5月8日
 * @Author: hechan
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public class HBaseTableSchema {

    /**
     * 应用层
     */
    // shza_result_table表
    public static final String VERIFY_RESULT_TABLE = "hy_verify_result_table";
    public static final String CF_CREDITMODEL = "creditModel";
    // 与user_info_table共用
    public static final String QL_UID = "uid";
    public static final String QL_SALARY = "salary";
    public static final String QL_JOB = "job";
    public static final String QL_AGE = "age";
    public static final String QL_GENDER = "gender";
    public static final String QL_ACCOUNTBALANCE = "accountBalance";
    public static final String QL_ORDERS = "orders";
    public static final String QL_BIRTHDAY = "birthday";
    public static final String QL_HOBBY = "hobby";
    public static final String QL_ISMARRIED = "isMarried";
    public static final String QL_CHANNELED = "channeled";
    public static final String QL_IMSI = "imsi";
    public static final String QL_INTERNETBEHAVIOR = "internetBehavior";
    public static final String QL_NAME = "name";
    public static final String QL_IDNUMBER = "idNumber";
    public static final String QL_HOMEADDRESS = "homeAddress";
    public static final String QL_COMPANYADDRESS = "companyAddress";
    public static final String QL_ADDRESS = "address";
    public static final String QL_REGISTRATIONTIME = "registrationTime";
    public static final String QL_USERSTATUS = "userstatus";
    public static final String QL_IS5WARN = "is5warn";
    public static final String QL_AUTHENTICATION = "authentication";
    public static final String QL_REGISTRATIONTYPE = "registrationType";
    public static final String QL_BLACKLISTTYPE = "blacklistType";
    public static final String QL_FEEOWELEVEL = "feeOweLevel";
    public static final String QL_STARLEVEL = "starLevel";
    public static final String QL_GROUPTYPE = "groupType";
    public static final String QL_POINTS = "points";

    public static final String CF_TEMPINFO = "groupType";
    public static final String QL_RECENTCONTACTCITY = "recentContactCity";
    public static final String QL_CITYCALCULATEDDATE = "cityCalculatedDate";
    public static final String QL_RECENTCONTACTPERSON = "recentContactPerson";
    public static final String QL_CONTACTORCALCULATEDDATE = "contactorCalculatedDate";
    public static final String QL_RECENTCONTACTRISK = "recentContactRisk";
    public static final String QL_WORKANDHOMECOORDINATE = "workAndHomeCoordinate";
    public static final String QL_RESIDENTCOORDINATELIST = "residentCoordinateList";
    public static final String QL_RESIDENTADDRESSCONFIDENCE = "residentAddressConfidence";
    public static final String QL_WORKCOORDINATELIST = "workCoordinateList";
    public static final String QL_WORKADDRESSCONFIDENCE = "workAddressConfidence";
    public static final String QL_MORNINGCOORDINATELIST = "morningCoordinateList";
    public static final String QL_AFTERNOONCOORDINATELIST = "afternoonCoordinateList";
    public static final String QL_NIGHTCOORDINATELIST = "nightCoordinateList";
    public static final String QL_RECENTABNORMALBEHAVIOR = "recentAbnormalBehavior";

    public static final String QL_RECENT5WARNSTATISTICS = "recent5warnStatistics";
    public static final String QL_RECENTCALLSTATISTICS = "recentCallStatistics";
    public static final String QL_RECENTMSGSTATISTICS = "recentMsgStatistics";
    public static final String QL_RECENTGPRSSTATISTICS = "recentGPRSStatistics";
    public static final String QL_RECENTTOTALFEESTATISTICS = "recentTotalFeeStatistics";

    /**
     * 基础层
     */
    // user_info_table表
    public static final String USER_INFO_TABLE = "hy_user_info_table";
    public static final String USER_INFO_TABLE2 = "hy_user_info_table2";
    public static final String CF_BASICINFO = "basicInfo";
    // user_imei_detail_table表
    public static final String USER_IMEI_DETAIL_TABLE = "hy_user_imei_detail_table";
    public static final String CF_IMEIINFO = "imeiInfo";
    public static final String QL_IMEI = "imei";
    public static final String QL_PHONEBRAND = "phoneBrand";
    public static final String QL_PHONEMODEL = "phoneModel";
    public static final String QL_PHONESYSTEM = "phoneSystem";
    public static final String QL_FIRSTTIME = "firstTime";
    public static final String QL_LASTTIME = "lastTime";

    // user_behavior_table表
    public static final String USER_BEHAVIOR_TABLE = "hy_user_behavior_table";
    public static final String CF_BEHAVIOR = "behavior";

    // user_position_table表
    public static final String USER_POSITION_TABLE = "hy_user_position_table";
    public static final String CF_POSITION = "position";
    public static final String QL_LACCOORDINATE = "lacCoordinate";
    public static final String QL_DURATION = "duration";

    // user_communication_detail_table表
    public static final String USER_COMMUNICATION_DETAIL_TABLE = "hy_user_communication_detail_table";
    public static final String CF_COMMUNICATIONINFO = "communicationInfo";
    public static final String QL_BUSINESSTYPE = "businessType";
    public static final String QL_OTHERCITY = "otherCity";
    public static final String QL_OTHERCITYNO = "otherCityNo";
    public static final String QL_COMMUNICATIONTYPE = "communicationType";
    public static final String QL_OTHERPHONENUMBER = "otherPhoneNumber";
    public static final String QL_OTHERNAME = "otherName";
    public static final String QL_CHARGEINFO = "chargeInfo";
    public static final String QL_STARTTIME = "startTime";
    public static final String QL_ROAMPLACE = "roamPlace";
    public static final String QL_ROAMINGPLACENO = "roamingPlaceNo";
    public static final String QL_ROAMTYPE = "roamType";

    // user_month_bill_table表
    public static final String USER_MONTH_BILL_TABLE = "hy_user_month_bill_table";
    public static final String CF_BILL = "bill";
    public static final String QL_ACCTNO = "acctNo";
    public static final String QL_TOTALFEE = "totalFee";
    public static final String QL_VOICEFEE = "voiceFee";
    public static final String QL_TOTALDUR = "totalDur";
    public static final String QL_LOCCALLCNT = "locCallCnt";
    public static final String QL_LOCCALLDUR = "locCallDur";
    public static final String QL_NATIONROAMCNT = "nationRoamCnt";
    public static final String QL_NATIONROAMDUR = "nationRoamDur";
    public static final String QL_INTERROAMCNT = "interRoamCnt";
    public static final String QL_INTERROAMDUR = "interRoamDur";
    public static final String QL_PHONEROAMCHARGEKB = "phoneRoamChargeKb";
    public static final String QL_PHONEINTERROAMCHARGEKB = "phoneInterRoamChargeKb";

    // 第三方映射层表
    public static final String USER_IDCARD_TABLE = "hy_user_idcard_table";
    public static final String CF_PHONE = "phone";
    public static final String QL_PHONE = "phone";

    private static final Map<String, byte[]> MAP;//Hold all columns' byte array

    /**
     * 获取指定键值的byte数组转换，若键值不在类预定义的常量范围内，则会返回null
     * 
     * @param key
     * @return
     */
    public static byte[] toBytes(String key) {
        return MAP.get(key);
    }

    static {
        Map<String, byte[]> tmp = new HashMap<String, byte[]>();
        Field[] fields = HBaseTableSchema.class.getDeclaredFields();
        for (Field f : fields) {
            try {
                Class<?> clazz = f.getType();
                if (clazz == String.class) {
                    String value = (String) f.get(null);
                    tmp.put(value, Bytes.toBytes(value));
                }
            } catch (Exception e) {
                throw new RuntimeException("Initial HBaseTableSchema error : " + e.getMessage());
            }
        }
        MAP = Collections.unmodifiableMap(tmp);
    }
}
