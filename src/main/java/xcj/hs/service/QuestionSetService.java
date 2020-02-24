package xcj.hs.service;

import xcj.hs.entity.QuestionSet;

import java.util.List;

public interface QuestionSetService {
  /**
   * 成功返回新增的id
   *
   * @param questionSet
   * @return
   */
  String add(QuestionSet questionSet);

  QuestionSet findById(String qsId);

  /**
   * 向题集内通过题目id添加题目，成功返回true，已存在返回false
   *
   * @param qsId
   * @param questionId
   * @return
   */
  boolean addQuestion(String qsId, String questionId);

  /**
   * 题集的对应用户中包含指定用户
   *
   * @param userId
   * @return
   */
  List<QuestionSet> findByUserId(String userId);

  List<QuestionSet> findAll();

  boolean addUser(String userId, String qsId);
}
