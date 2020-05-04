package xcj.hs.biz.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xcj.hs.biz.QuestionSetManager;
import xcj.hs.biz.UserManager;
import xcj.hs.entity.QuestionSet;
import xcj.hs.service.QuestionSetService;
import xcj.hs.service.RecordService;
import xcj.hs.service.UserService;
import xcj.hs.vo.QuestionSetVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class QuestionSetManagerImpl extends BaseManagerImpl<QuestionSetVo, QuestionSet>
    implements QuestionSetManager {
  @Autowired QuestionSetService questionSetService;
  @Autowired UserService userService;
  @Autowired UserManager userManager;
  @Autowired RecordService recordService;

  public String add(QuestionSetVo questionSetVo) {
    questionSetVo.setQsOwner(userService.findByUserAccount(questionSetVo.getQsOwner()).getUserId());
    return questionSetService.add(vo2po(questionSetVo));
  }

  public QuestionSetVo findById(String qsId) {
    return po2vo(questionSetService.findById(qsId));
  }

  public boolean addQuestion(String qsId, String questionId) {
    return questionSetService.addQuestion(qsId, questionId);
  }

  public List<QuestionSetVo> findByUserAccount(String userAccount) {
    String userId = userService.findByUserAccount(userAccount).getUserId();
    List<QuestionSetVo> questionSetVos = po2vo(questionSetService.findByUserId(userId));
    // 计算该用户各个题集的做题数
    for (QuestionSetVo questionSetVo : questionSetVos) {
      int finishedNumber = 0;
      if (StringUtils.isNotBlank(questionSetVo.getQuestionIds())) {
        for (String questionId : questionSetVo.getQuestionIds().split("、")) {
          if (recordService.findByUserIdAndQuestionId(userId, questionId) != null) {
            finishedNumber++;
          }
        }
      }
      questionSetVo.setFinishedNumber(String.valueOf(finishedNumber));
    }
    return questionSetVos;
  }

  public List<Map<String, Object>> findByOwner(String userAccount) {
    List<Map<String, Object>> result = new ArrayList<>();
    String owner = userService.findByUserAccount(userAccount).getUserId();
    List<QuestionSetVo> questionSetVos = po2vo(questionSetService.findByOwner(owner));

    for (QuestionSetVo questionSetVo : questionSetVos) { // 循环题集
      Map<String, Object> mapItem = new HashMap<>();
      mapItem.put("questionSet", questionSetVo);
      List<Map<String, Object>> userStatus = new ArrayList<>();
      // 计算该题集下每个学生的做题数
      if (StringUtils.isNotBlank(questionSetVo.getUserIds())) { // 循环题集对应用户
        for (String userId : questionSetVo.getUserIds().split("、")) {
          int finishedNumber = 0;
          for (String questionId : questionSetVo.getQuestionIds().split("、")) { // 循环题集下题目数
            if (recordService.findByUserIdAndQuestionId(userId, questionId) != null) {
              finishedNumber++;
            }
          }
          Map<String, Object> userNumMap = new HashMap<>();
          userNumMap.put("userVo", userManager.findById(userId));
          userNumMap.put("count", String.valueOf(finishedNumber));
          userStatus.add(userNumMap);
        }
      }
      mapItem.put("usersInfo", userStatus);
      result.add(mapItem);
    }
    return result;
  }

  public List<QuestionSetVo> findAll() {
    return po2vo(questionSetService.findAll());
  }

  public boolean addUser(String userAccount, String qsId) {
    return questionSetService.addUser(userService.findByUserAccount(userAccount).getUserId(), qsId);
  }

  public QuestionSetVo findQsRecordByUser(String userAccount, String qsId) {
    String userId = userService.findByUserAccount(userAccount).getUserId();
    QuestionSetVo questionSetVo = findById(qsId);
    int finishedNumber = 0;
    for (String questionId : questionSetVo.getQuestionIds().split("、")) {
      if (recordService.findByUserIdAndQuestionId(userId, questionId) != null) {
        finishedNumber++;
      } else {
        if (StringUtils.isBlank(questionSetVo.getUnfinishedQuestionIds())) {
          questionSetVo.setUnfinishedQuestionIds(questionId);
        } else {
          questionSetVo.setUnfinishedQuestionIds(
              questionSetVo.getUnfinishedQuestionIds() + "、" + questionId);
        }
      }
    }
    return questionSetVo;
  }

  @Override
  public QuestionSetVo po2vo(QuestionSet po) {
    QuestionSetVo vo = super.po2vo(po);
    // 添加拥有者姓名，题目数
    vo.setQsOwnerName(userService.findById(po.getQsOwner()).getUserName());
    vo.setQuestionNumber(
        StringUtils.isBlank(vo.getQuestionIds())
            ? "0"
            : String.valueOf(vo.getQuestionIds().split("、").length));
    return vo;
  }
}
