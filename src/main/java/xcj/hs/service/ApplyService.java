package xcj.hs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xcj.hs.entity.Apply;
import xcj.hs.vo.ApplyVo;

public interface ApplyService {
  void modifyRoleApply(ApplyVo applyVo);

  void modifySupeiorApply(ApplyVo applyVo);

  Page<Apply> superiorApplyPageFind(String userId, String status, Pageable pageable);

  Page<Apply> roleApplyPageFind( String status, Pageable pageable);

  Apply findById(String id);

  void save(Apply apply);
}
