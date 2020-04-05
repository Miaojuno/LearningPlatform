package xcj.hs.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import xcj.hs.entity.Record;
import xcj.hs.entity.User;

import java.util.List;

public interface RecordDao extends JpaRepository<Record, String> {

  List<Record> findByUserIdContaining(String userId);

  Record findByUserIdAndQuestionId(String userId, String questionId);

  Record getFirstByReviewerIdContainingAndScoreIsNull(String reviewerId);

  List<Record> findByDateGreaterThanAndUserIdContaining(String date, String userId);

  Page<Record> findByUserId(String userId, Pageable pageable);
}
