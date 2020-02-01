package xcj.hs.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xcj.hs.entity.Record;

import java.util.List;

public interface RecordDao extends JpaRepository<Record, String> {

  List<Record> findByUserIdContaining(String userId);

  Record findByUserIdAndQuestionId(String userId, String questionId);

  Record getFirstByReviewerIdContainingAndScoreIsNull(String reviewerId);

  List<Record> findByDateGreaterThanAndUserIdContaining(String date, String userId);
}
