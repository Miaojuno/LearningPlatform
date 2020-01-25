package xcj.hs.service;

import org.springframework.web.multipart.MultipartFile;
import xcj.hs.entity.Question;
import xcj.hs.vo.QuestionVo;

public interface NeoService {
  String excelUpload(MultipartFile file);

  QuestionVo getRandomQuestion(String userAccount);

  Question findQuestionById(String id);

  QuestionVo findQuestionVoById(String id);
}
