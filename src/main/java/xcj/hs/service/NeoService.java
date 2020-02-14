package xcj.hs.service;

import org.springframework.web.multipart.MultipartFile;
import xcj.hs.entity.Point;
import xcj.hs.vo.QuestionVo;

import java.util.List;

public interface NeoService {
  String excelUpload(MultipartFile file);

  QuestionVo getRandomQuestion(String userAccount, String pointId);

  QuestionVo findByQuestionId(String id);

  QuestionVo findQuestionById(String id);

  Point findPointById(String id);

  List<Point> findPointByDetail(String pointDetail);
}
