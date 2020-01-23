package xcj.hs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;
import xcj.hs.dao.NeoDao;
import xcj.hs.entity.Question;
import xcj.hs.service.NeoService;
import xcj.hs.service.RecordService;
import xcj.hs.service.UserService;
import xcj.hs.vo.QuestionVo;

@Service
@Slf4j
public class NeoServiceImpl implements NeoService {
  @Autowired NeoDao neoDao;
  @Autowired UserService userService;
  @Autowired RecordService recordService;

  public String excelUpload(MultipartFile file) {
    return neoDao.excelUpload(file);
  }

  public QuestionVo getRandomQuestion(String userAccount) {
    String userId = userService.findByUserAccount(userAccount).getUserId();
    Question question;
    question = neoDao.getRandomQuestion();
    // 只获取该用户没做过的题目
    int tryTimes = 0;
    while (recordService.findByUserIdAndQuestionId(userId, question.getQuestionId()) != null
        && tryTimes < 100) {
      question = neoDao.getRandomQuestion();
      tryTimes++;
    }
    if (tryTimes == 100) return null;
    QuestionVo questionVo = new QuestionVo();
    BeanUtils.copyProperties(question, questionVo);
    BASE64Encoder encoder = new BASE64Encoder();
    if (question.getPic() != null) {
      questionVo.setPicStr(encoder.encode(question.getPic()));
    }
    return questionVo;
  }

  public Question findQuestionById(String id) {
    return findQuestionById(id);
  }
}
