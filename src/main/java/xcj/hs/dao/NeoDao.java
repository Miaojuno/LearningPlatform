package xcj.hs.dao;

import org.springframework.web.multipart.MultipartFile;
import xcj.hs.entity.Point;
import xcj.hs.entity.Question;

import java.util.List;

public interface NeoDao {

  String excelUpload(MultipartFile file);

  Question getRandomQuestion(String pointId,String diff,String type);

  Question findByQuestionId(String id);

  Question findQuestionById(String id);

  Point findPointById(String id);

  List<Point> findPointByDetail(String pointDetail);

  /**
   * 成功返回id
   *
   * @param question
   * @return
   */
  String addQuestion(Question question);
}
