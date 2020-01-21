package xcj.hs.biz.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xcj.hs.biz.ApplyManager;
import xcj.hs.entity.Apply;
import xcj.hs.service.ApplyService;
import xcj.hs.service.UserService;
import xcj.hs.vo.ApplyVo;

@Component
public class ApplyManagerImpl extends BaseManagerImpl<ApplyVo, Apply> implements ApplyManager {
  @Autowired ApplyService applyService;

  @Autowired UserService userService;

  public void modifyRoleApply(ApplyVo applyVo) {
    applyService.modifyRoleApply( applyVo);
  }

  public void modifySupeiorApply(ApplyVo applyVo) {
    applyService.modifySupeiorApply( applyVo);
  }
}
