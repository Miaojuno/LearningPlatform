package xcj.hs.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import xcj.hs.entity.Apply;

public interface ApplyDao extends JpaRepository<Apply, String> {

    Page<Apply> findByTypeContainingAndNewIdContainingAndStatusContaining(String Type, String newId, String status, Pageable pageable);
}
