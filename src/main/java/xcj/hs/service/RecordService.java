package xcj.hs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xcj.hs.entity.Record;
import xcj.hs.entity.User;

import java.util.List;

public interface RecordService {
  void save(Record record);

  Record findByUserIdAndQuestionId(String userId, String questionId);

  /**
   * 根据userId获取一个当前用户需要审核的record;
   *
   * @param userId
   * @return
   */
  Record getOneUnreviewed(String userId);

  /**
   * 获取最近15日记录（前14天以及当天）
   *
   * @param userId
   * @return
   */
  List<Record> get15daysRecords(String userId);

  List<Record> findByUserId(String userId);

  void updateScore(String recordId,String score);

  Page<Record> pageFind(String userId, Pageable pageable);

  Record findById(String id);
}
