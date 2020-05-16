package xcj.hs.service;

import org.springframework.web.multipart.MultipartFile;
import xcj.hs.entity.Point;
import xcj.hs.entity.Question;
import xcj.hs.vo.QuestionVo;

import java.util.List;

public interface NeoService {
  String excelUpload(MultipartFile file);

  QuestionVo getRandomQuestion(String userAccount, String pointId, String diff, String type);

  QuestionVo findByQuestionId(String id);

  QuestionVo findQuestionById(String id);

  Point findPointById(String id);

  List<Point> findPointByDetail(String pointDetail);

  /**
   * 成功返回id
   *
   * @param question
   * @return
   */
  String addQuestion(Question question,String pointId);

  List<Point> findPointByQuestionId(String questionId);

  Point findPointByPointId(String id);
}
