package xcj.hs.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

  public static String TIMESTR = "yyyyMMddHHmmss";
  public static String DATESTR = "yyyyMMdd";

  /**
   * 以指定格式获取当前时间
   *
   * @param timeType
   * @return
   */
  public static String getCurrectTimeStr(String timeType) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeType); // 设置日期格式
    return simpleDateFormat.format(new Date());
  }

  /**
   * 以指定格式获取指定日期前（后）的时间
   *
   * @param timeType
   * @param day
   * @return
   */
  public static String getSpecifiedTimeStr(String timeType, int day) {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeType); // 设置日期格式
    calendar.add(calendar.DATE,day);
    return simpleDateFormat.format(calendar.getTime());
  }

  /**
   * 时间比较，time1在前则返回0，time一致返回1，time1在后返回2，时间格式不一致返回3
   * @param time1
   * @param time2
   * @return
   */
  public static String timeCompare(String time1, String time2) {
    if(time1.length()!=time2.length()) return "3";
    if(time1.compareTo(time2)<0) return "0";
    if(time1.compareTo(time2)==0)return "1";
    else return "2";
  }

}
