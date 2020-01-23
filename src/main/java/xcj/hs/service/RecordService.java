package xcj.hs.service;

import xcj.hs.entity.Record;

public interface RecordService {
  void save(Record record);

  Record findByUserIdAndQuestionId(String userId,String questionId);
}
