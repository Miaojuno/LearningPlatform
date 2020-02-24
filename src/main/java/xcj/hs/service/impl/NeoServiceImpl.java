package xcj.hs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xcj.hs.dao.NeoDao;
import xcj.hs.entity.Point;
import xcj.hs.entity.Question;
import xcj.hs.service.NeoService;
import xcj.hs.service.RecordService;
import xcj.hs.service.UserService;
import xcj.hs.vo.QuestionVo;

import java.util.List;

@Service
@Slf4j
public class NeoServiceImpl implements NeoService {
  @Autowired NeoDao neoDao;
  @Autowired UserService userService;
  @Autowired RecordService recordService;

  public String excelUpload(MultipartFile file) {
    return neoDao.excelUpload(file);
  }

  public QuestionVo getRandomQuestion(String userAccount, String pointId) {
    String userId = userService.findByUserAccount(userAccount).getUserId();
    Question question = neoDao.getRandomQuestion(pointId);
    // 只获取该用户没做过的题目
    int tryTimes = 0;
    while (question != null
        && recordService.findByUserIdAndQuestionId(userId, question.getQuestionId()) != null
        && tryTimes < 100) {
      question = neoDao.getRandomQuestion(pointId);
      tryTimes++;
    }
    if (tryTimes == 100 || question == null) return null;
    QuestionVo questionVo = new QuestionVo();
    BeanUtils.copyProperties(question, questionVo);
    //    BASE64Encoder encoder = new BASE64Encoder();
    //    if (question.getPic() != null) {
    //      questionVo.setPicStr(encoder.encode(question.getPic()));
    //    }
    return questionVo;
  }

  public QuestionVo findByQuestionId(String id) {
    Question question = neoDao.findByQuestionId(id);
    QuestionVo questionVo = new QuestionVo();
    BeanUtils.copyProperties(question, questionVo);
    return questionVo;
  }

  public QuestionVo findQuestionById(String id) {
    Question question = neoDao.findQuestionById(id);
    QuestionVo questionVo = new QuestionVo();
    BeanUtils.copyProperties(question, questionVo);
    return questionVo;
  }

  public Point findPointById(String id) {
    return neoDao.findPointById(id);
  }

  public List<Point> findPointByDetail(String pointDetail) {
    return neoDao.findPointByDetail(pointDetail);
  }

  public String addQuestion(Question question){
    return neoDao.addQuestion(question);
  }
}
