package xcj.hs.biz;

import xcj.hs.vo.RecordVo;

import java.util.List;
import java.util.Map;

public interface RecordManager {
  void save(RecordVo recordVo) throws Exception;

  /**
   *  根据userId获取一个当前用户需要审核的record;
   * @param userAccount
   * @return
   */
  RecordVo getOneUnreviewed(String userAccount);

  /**
   * 获取最近15日记录（前14天以及当天）
   * @param userAccount
   * @return
   */
  Map<String, Object> get15daysRecordData(String userAccount);
}
