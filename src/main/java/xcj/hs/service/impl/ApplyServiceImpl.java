package xcj.hs.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcj.hs.dao.ApplyDao;
import xcj.hs.entity.Apply;
import xcj.hs.entity.User;
import xcj.hs.service.ApplyService;
import xcj.hs.service.UserService;
import xcj.hs.vo.ApplyVo;

@Service
public class ApplyServiceImpl extends BaseServiceImpl<Apply> implements ApplyService {

  @Autowired ApplyDao applyDao;
  @Autowired
  UserService userService;

  public void modifyRoleApply(ApplyVo applyVo){
    User user=userService.findByUserAccount(applyVo.getUserAccount());
    Apply apply=new Apply();
    BeanUtils.copyProperties(applyVo,apply);
    apply.setStatus("申请中");
    apply.setType("角色变更");
    apply.setOldId(user.getRoleId());
    apply.setUserId(user.getUserId());
    applyDao.save(apply);
  }

  public void modifySupeiorApply(ApplyVo applyVo){
    User user=userService.findByUserAccount(applyVo.getUserAccount());
    Apply apply=new Apply();
    BeanUtils.copyProperties(applyVo,apply);
    apply.setStatus("申请中");
    apply.setType("上级变更");
    apply.setOldId(user.getSuperiorId());
    apply.setUserId(user.getUserId());
    applyDao.save(apply);
  }
}
