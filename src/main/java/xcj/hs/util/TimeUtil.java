package xcj.hs.util;

import java.text.SimpleDateFormat;
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
}
