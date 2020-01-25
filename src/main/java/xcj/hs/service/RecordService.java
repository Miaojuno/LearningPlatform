package xcj.hs.service;

import xcj.hs.entity.Record;

public interface RecordService {
  void save(Record record);

  Record findByUserIdAndQuestionId(String userId,String questionId);

  /**
   * 根据userId获取一个当前用户需要审核的record;
   * @param userId
   * @return
   */
  Record getOneUnreviewed(String userId);
}
