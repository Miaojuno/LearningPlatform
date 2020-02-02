package xcj.hs.dao;

import org.springframework.web.multipart.MultipartFile;
import xcj.hs.entity.Point;
import xcj.hs.entity.Question;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NeoDao {

  String excelUpload(MultipartFile file);

  Question getRandomQuestion();

  Question findByQuestionId(String id);

  Question findQuestionById(String id);

  Point findPointById(String id);
}
