package xcj.hs.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xcj.hs.entity.Record;

public interface RecordDao extends JpaRepository<Record, String> {}
