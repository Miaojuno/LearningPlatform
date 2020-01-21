package xcj.hs.biz.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import sun.java2d.pipe.AlphaPaintPipe;
import xcj.hs.biz.ApplyManager;
import xcj.hs.entity.Apply;
import xcj.hs.service.ApplyService;
import xcj.hs.service.RoleService;
import xcj.hs.service.UserService;
import xcj.hs.vo.ApplyVo;

@Component
public class ApplyManagerImpl extends BaseManagerImpl<ApplyVo, Apply> implements ApplyManager {
  @Autowired ApplyService applyService;

  @Autowired UserService userService;

  @Autowired
  RoleService roleService;

  public void modifyRoleApply(ApplyVo applyVo) {
    applyService.modifyRoleApply(applyVo);
  }

  public void modifySupeiorApply(ApplyVo applyVo) {
    applyService.modifySupeiorApply(applyVo);
  }

  public Page<ApplyVo> superiorApplyPageFind(String userAccount, String status, Pageable pageable) {
    return po2vo(
        applyService.superiorApplyPageFind(
            userService.findByUserAccount(userAccount).getUserId(), status, pageable));
  }









  @Override
  public ApplyVo po2vo(Apply apply) {
    ApplyVo applyVo = super.po2vo(apply);
    // 添加userName字段
    if (StringUtils.isNotBlank(apply.getUserId())) {
      String userName = userService.findById(apply.getUserId()).getUserName();
      if (StringUtils.isNotBlank(userName)) {
        applyVo.setUserName(userName);
      }
    }
    if ("上级变更".equals(applyVo.getType())){
      if (StringUtils.isNotBlank(apply.getNewId())) {
        String userName = userService.findById(apply.getNewId()).getUserName();
        if (StringUtils.isNotBlank(userName)) {
          applyVo.setNewName(userName);
        }
      }
      if (StringUtils.isNotBlank(apply.getOldId())) {
        String userName = userService.findById(apply.getOldId()).getUserName();
        if (StringUtils.isNotBlank(userName)) {
          applyVo.setOldName(userName);
        }
      }
    }
    else if ("角色变更".equals(applyVo.getType())){
      if (StringUtils.isNotBlank(apply.getNewId())){
        String roleName = roleService.findRoleByRoleId(apply.getNewId()).getRoleName();
        if (StringUtils.isNotBlank(roleName)) {
          applyVo.setNewName(roleName);
        }
      }
      if (StringUtils.isNotBlank(apply.getOldId())){
        String roleName = roleService.findRoleByRoleId(apply.getOldId()).getRoleName();
        if (StringUtils.isNotBlank(roleName)) {
          applyVo.setOldName(roleName);
        }
      }
    }

    return applyVo;
  }
}
