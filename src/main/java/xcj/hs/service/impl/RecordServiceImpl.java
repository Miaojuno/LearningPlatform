package xcj.hs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcj.hs.dao.RecordDao;
import xcj.hs.entity.Record;
import xcj.hs.service.RecordService;
import xcj.hs.util.TimeUtil;

import java.util.List;

@Service
@Slf4j
public class RecordServiceImpl extends BaseServiceImpl<Record> implements RecordService {

  @Autowired RecordDao recordDao;

  public void save(Record record) {
    record.setDate(TimeUtil.getCurrectTimeStr(TimeUtil.TIMESTR));
    recordDao.save(record);
  }

  public Record findByUserIdAndQuestionId(String userId, String questionId) {
    return recordDao.findByUserIdAndQuestionId(userId, questionId);
  }

  public Record getOneUnreviewed(String userId) {
    return recordDao.getFirstByReviewerIdContainingAndScoreIsNull(userId);
  }

  public List<Record> get15daysRecords(String userId) {
    // 前15天以及当天
    return recordDao.findByDateGreaterThanAndUserIdContaining(
        TimeUtil.getSpecifiedTimeStr(TimeUtil.DATESTR, -14) + "000000", userId);
  }

  public List<Record> findByUserId(String userId) {
    return recordDao.findByUserIdContaining(userId);
  }
}
