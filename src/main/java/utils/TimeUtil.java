package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * unix时间戳转换工具类
 *
 * @author chenjunan
 */
public class TimeUtil {

    /**
     * 日期格式1
     */
    public static final String DETA_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式2
     */
    public static final String DETA_FORMAT_2 = "yyyy-MM-dd";

    /**
     * 日期格式3
     */
    public static final String DETA_FORMAT_3 = "yyyy年MM月dd日";

    /**
     * 日期格式2
     */
    public static final String DETA_FORMAT_4 = "yyyyMMdd";


    /**
     * 获取时间戳 单位秒
     * @return
     */
    public static long getTimeStamp(){
        return System.currentTimeMillis()/1000;
    }


    /**
     * 时间戳转日期
     * @param timeStamp
     * @param format
     * @return
     */
    public static String timeStampToDate(String timeStamp,String format){

        if(timeStamp.isEmpty() || timeStamp == null || timeStamp.equals("null"))
            return "";
        if(format == null || format.isEmpty())
            format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(new Date(Long.valueOf(timeStamp+"000")));

    }

    /**
     * 时间戳转日期
     * @param timeStamp
     * @param format
     * @return
     */
    public static String timeStampToSimpleDate(String timeStamp,String format){

        if(timeStamp.isEmpty() || timeStamp == null || timeStamp.equals("null"))
            return "";
        if(format == null || format.isEmpty())
            format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(new Date(Long.valueOf(timeStamp+"000")));

    }


    /**
     * 日期转时间戳
     * @param date
     * @param format
     * @return
     */
    public static String dateTotimeStamp(String date,String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date).getTime()/1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  "";
    }

    /**
     * 日期转时间戳
     * @param date
     * @return
     */
    public static String simpleDateTotimeStamp(String date){
        return dateTotimeStamp(date, DETA_FORMAT_2);
    }

    /**
     * 返回当前年月日（yyMMdd）
     *
     * @return
     */
    public static String getDate() {
        return timeStampToSimpleDate(String.valueOf(getTimeStamp()), DETA_FORMAT_4);
    }

}
