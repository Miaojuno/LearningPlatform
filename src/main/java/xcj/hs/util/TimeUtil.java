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

}
