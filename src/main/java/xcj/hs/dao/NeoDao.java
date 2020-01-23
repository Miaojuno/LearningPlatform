package xcj.hs.dao;

import org.springframework.web.multipart.MultipartFile;
import xcj.hs.entity.Question;

public interface NeoDao {

  String excelUpload(MultipartFile file);

  Question getRandomQuestion();

  Question findById(String id);
}
