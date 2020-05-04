package xcj.hs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcj.hs.dao.QuestionSetDao;
import xcj.hs.entity.QuestionSet;
import xcj.hs.service.QuestionSetService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionSetServiceImpl extends BaseServiceImpl<QuestionSet>
    implements QuestionSetService {

  @Autowired QuestionSetDao questionSetDao;

  public String add(QuestionSet questionSet) {
    return questionSetDao.saveAndFlush(questionSet).getQsId();
  }

  public QuestionSet findById(String qsId) {
    if (questionSetDao.findById(qsId).isPresent()) {
      return questionSetDao.findById(qsId).get();
    }
    return null;
  }

  public boolean addQuestion(String qsId, String questionId) {
    QuestionSet questionSet = findById(qsId);
    String ids =
        StringUtils.isBlank(questionSet.getQuestionIds()) ? "" : questionSet.getQuestionIds();
    if (ids.contains(questionId)) {
      return false;
    }
    if (StringUtils.isBlank(questionSet.getQuestionIds())) {
      questionSet.setQuestionIds(questionId);
    } else {
      questionSet.setQuestionIds(questionSet.getQuestionIds() + "、" + questionId);
    }
    questionSetDao.save(questionSet);
    return true;
  }

  public List<QuestionSet> findByUserId(String userId) {
    return questionSetDao.findByUserIdsContaining(userId);
  }

  public List<QuestionSet> findByOwner(String owner){
    return questionSetDao.findByQsOwnerContaining(owner);
  }

  public List<QuestionSet> findAll() {
    return questionSetDao.findAll();
  }

  public boolean addUser(String userId, String qsId) {
    QuestionSet questionSet = questionSetDao.findById(qsId).get();
    if (questionSet.getUserIds().contains(userId)) {
      return false;
    }
    questionSet.setUserIds(questionSet.getUserIds() + "、" + userId);
    questionSetDao.save(questionSet);
    return true;
  }
}
