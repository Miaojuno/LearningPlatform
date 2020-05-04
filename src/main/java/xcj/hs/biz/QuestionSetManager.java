package xcj.hs.biz;

import xcj.hs.vo.QuestionSetVo;

import java.util.List;
import java.util.Map;

public interface QuestionSetManager {
  /**
   * 成功返回新增的id
   *
   * @param questionSetVo
   * @return
   */
  String add(QuestionSetVo questionSetVo);

  QuestionSetVo findById(String qsId);

  /**
   * 向题集内通过题目id添加题目，成功返回true，已存在返回false
   *
   * @param qsId
   * @param questionId
   * @return
   */
  boolean addQuestion(String qsId, String questionId);

  /**
   * 指定用户的题集
   *
   * @param userAccount
   * @return
   */
  List<QuestionSetVo> findByUserAccount(String userAccount);

  List<Map<String,Object>> findByOwner(String userAccount);

  List<QuestionSetVo> findAll();

  boolean addUser(String userAccount, String qsId);

  QuestionSetVo findQsRecordByUser(String userAccount, String qsId);
}
