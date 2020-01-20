package xcj.hs.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xcj.hs.entity.Apply;

public interface ApplyDao extends JpaRepository<Apply, String> {}
