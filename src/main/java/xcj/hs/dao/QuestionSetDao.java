package xcj.hs.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xcj.hs.entity.QuestionSet;

import java.util.List;

public interface QuestionSetDao extends JpaRepository<QuestionSet, String> {
  List<QuestionSet> findByUserIdsContaining(String userId);
}
