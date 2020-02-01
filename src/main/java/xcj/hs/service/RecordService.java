package xcj.hs.service;

import xcj.hs.entity.Record;

import java.util.List;

public interface RecordService {
  void save(Record record);

  Record findByUserIdAndQuestionId(String userId,String questionId);

  /**
   * 根据userId获取一个当前用户需要审核的record;
   * @param userId
   * @return
   */
  Record getOneUnreviewed(String userId);

  /**
   * 获取最近15日记录（前14天以及当天）
   * @param userId
   * @return
   */
  List<Record> get15daysRecords(String userId);
}
