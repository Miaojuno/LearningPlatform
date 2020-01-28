package xcj.hs.biz.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xcj.hs.biz.RecordManager;
import xcj.hs.entity.Record;
import xcj.hs.entity.User;
import xcj.hs.service.NeoService;
import xcj.hs.service.RecordService;
import xcj.hs.service.UserService;
import xcj.hs.vo.QuestionVo;
import xcj.hs.vo.RecordVo;

@Component
@Slf4j
public class RecordManagerImpl extends BaseManagerImpl<RecordVo, Record> implements RecordManager {
  @Autowired RecordService recordService;
  @Autowired UserService userService;
  @Autowired NeoService neoService;

  @Override
  public RecordVo po2vo(Record record) {
    RecordVo recordVo = super.po2vo(record);
    QuestionVo questionVo = neoService.findQuestionVoById(recordVo.getQuestionId());
    recordVo.setQuestionDetail(questionVo.getQuestionDetail());
    recordVo.setQuestionPic(questionVo.getPic());
    recordVo.setSolution(questionVo.getSolution());
    recordVo.setSolutionPic(questionVo.getSolutionPic());
    return recordVo;
  }

  public void save(RecordVo recordVo) throws Exception {
    Record record = vo2po(recordVo);
    // 当前做题用户
    User user = userService.findByUserAccount(recordVo.getUserAccount());
    record.setUserId(user.getUserId());
    // 设置审阅人为当前学生的上级老师
    if (StringUtils.isBlank(user.getSuperiorId())) {
      throw new Exception("请先选择上级教师再做题");
    }
    record.setReviewerId(user.getSuperiorId());
    // 选择题和客观题自动判题(忽略大小写)
    QuestionVo questionVo = neoService.findQuestionVoById(recordVo.getQuestionId());
    if ("选择题".equals(questionVo.getType()) || "客观题".equals(questionVo.getType())) {
      if (recordVo.getUserSolution().equalsIgnoreCase(questionVo.getSolution())) {
        record.setScore(questionVo.getScore());
      } else record.setScore("0");
    }
    recordService.save(record);
  }

  public RecordVo getOneUnreviewed(String userAccount) {
    Record record =
        recordService.getOneUnreviewed(userService.findByUserAccount(userAccount).getUserId());
    if (record != null) {
      return po2vo(record);
    }
    return null;
  }
}
