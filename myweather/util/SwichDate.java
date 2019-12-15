package cn.edu.lit.myweather.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 工具类将字符串转化为日期
 * String date 转化为 Date date
 */
public class SwichDate {
    public static String  switcher(String date){
        Date date1;
        try {
            date1 = new SimpleDateFormat("yyyyMMdd").parse(date);
            String date2 = new SimpleDateFormat("MM月dd日").format(date1);
            return date2;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
