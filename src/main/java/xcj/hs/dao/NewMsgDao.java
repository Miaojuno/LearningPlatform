package xcj.hs.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xcj.hs.entity.NewMsg;

public interface NewMsgDao extends JpaRepository<NewMsg, String> {
  NewMsg findByUserId(String userId);
}
