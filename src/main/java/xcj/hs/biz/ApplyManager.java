package xcj.hs.biz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xcj.hs.vo.ApplyVo;

public interface ApplyManager {

  void modifyRoleApply(ApplyVo applyVo);

  void modifySupeiorApply(ApplyVo applyVo);

  Page<ApplyVo> superiorApplyPageFind(String userAccount, String status, Pageable pageable);
}
