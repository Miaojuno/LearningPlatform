package xcj.hs.biz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xcj.hs.entity.Record;
import xcj.hs.vo.NameValueVo;
import xcj.hs.vo.RecordVo;

import java.util.List;
import java.util.Map;

public interface RecordManager {
  void save(RecordVo recordVo) throws Exception;

  /**
   * 根据userId获取一个当前用户需要审核的record;
   *
   * @param userAccount
   * @return
   */
  RecordVo getOneUnreviewed(String userAccount);

  /**
   * 获取最近15日记录（前14天以及当天）
   *
   * @param userAccount
   * @return
   */
  Map<String, Object> get15daysRecordData(String userAccount);

  List<NameValueVo> getErrorCountGroupByKind(String userAccount);

  List<Object> getSubordinateSituation (String userAccount);

  Map<String, Object> getRecordByDiff(String userAccount);

  RecordVo findByUserAccountAndQuestionId(String userAccount, String questionId);

  void updateScore(String recordId,String score);

  Page<RecordVo> pageFind(String userAccount, Pageable pageable);

  RecordVo findById(String id);

  List<Map<String, Object>>  getErrorPorint(String userAccount);
}
