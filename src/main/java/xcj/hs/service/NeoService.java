package xcj.hs.service;

import org.springframework.web.multipart.MultipartFile;
import xcj.hs.entity.Point;
import xcj.hs.entity.Question;
import xcj.hs.vo.QuestionVo;

public interface NeoService {
  String excelUpload(MultipartFile file);

  QuestionVo getRandomQuestion(String userAccount);

  QuestionVo findByQuestionId(String id);

  QuestionVo findQuestionById(String id);

  Point findPointById(String id);
}
