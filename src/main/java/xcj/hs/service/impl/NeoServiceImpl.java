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
import xcj.hs.vo.QuestionVo;

@Service
@Slf4j
public class NeoServiceImpl implements NeoService {
  @Autowired NeoDao neoDao;

  public String excelUpload(MultipartFile file) {
    return neoDao.excelUpload(file);
  }

  public QuestionVo getRandomQuestion() {
    Question question = neoDao.getRandomQuestion();
    QuestionVo questionVo = new QuestionVo();
    BeanUtils.copyProperties(question, questionVo);
    BASE64Encoder encoder = new BASE64Encoder();
    if (question.getPic() != null) {
      questionVo.setPicStr(encoder.encode(question.getPic()));
    }
    return questionVo;
  }

  public Question findById(String id) {
    return findById(id);
  }
}
