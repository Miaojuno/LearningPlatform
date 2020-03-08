package xcj.hs.biz.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import xcj.hs.biz.UserManager;
import xcj.hs.entity.User;
import xcj.hs.service.RoleService;
import xcj.hs.service.UserService;
import xcj.hs.vo.UserVo;

import java.util.List;

@Component
@Slf4j
public class UserManagerImpl extends BaseManagerImpl<UserVo, User> implements UserManager {
  @Autowired UserService userService;

  @Autowired RoleService roleService;

  public boolean loginCheck(String account, String pwd) {
    return userService.loginCheck(account, pwd);
  }

  public boolean register(UserVo userVo) {
    User newUser = vo2po(userVo);
    newUser.setIsActive("1");
    if (StringUtils.isBlank(userVo.getRoleId())) {
      newUser.setRoleId("8a8181816e209f02016e209f20330001"); // 默认为"学生"
    }
    return userService.create(newUser);
  }

  public void modifyPic(String userAccount, byte[] pic) {
    userService.modifyPic(userAccount, pic);
  }

  public void modifyRole(String userAccount, String roleId) {
    userService.modifyRole(userAccount, roleId);
  }

  public List<UserVo> findAllActiveUser() {
    return po2vo(userService.findAllActiveUser());
  }

  @Override
  public UserVo po2vo(User user) {
    // 添加roleName字段
    UserVo userVo = super.po2vo(user);
    if (StringUtils.isNotBlank(user.getRoleId())) {
      String roleName = roleService.findRoleByRoleId(user.getRoleId()).getRoleName();
      if (StringUtils.isNotBlank(roleName)) {
        userVo.setRoleName(roleName);
      }
    }
    // 添加superiorName字段
    if (StringUtils.isNotBlank(user.getSuperiorId())) {
      String superiorName = userService.findById(user.getSuperiorId()).getUserName();
      if (StringUtils.isNotBlank(superiorName)) {
        userVo.setSuperiorName(superiorName);
      }
    }
    // 不把密码传输到前端
    userVo.setUserPwd("");

    //没有头像时默认为superadmin头像
    if (userVo.getPic() == null) {
      userVo.setPic(userService.findByUserAccount("superadmin").getPic());
    }

    return userVo;
  }

  public boolean updateSuperior(String subordinateId, String superiorId) {
    return userService.updateSuperior(subordinateId, superiorId);
  }

  public Page<UserVo> pageFind(UserVo userVo, Pageable pageable) {
    return po2vo(userService.pageFind(userVo, pageable));
  }

  public Page<UserVo> superiorPageFind(
      UserVo userVo, String subordinateId, String subordinateAccount, Pageable pageable) {
    String roleName;
    if (StringUtils.isNotBlank(subordinateId)) {
      roleName = findById(subordinateId).getRoleName();
    } else {
      roleName = po2vo(userService.findByUserAccount(subordinateAccount)).getRoleName();
    }
    if ("学生".equals(roleName)) {
      userVo.setRoleId(roleService.findRoleByRoleName("教师").getRoleId());
    } else if ("教师".equals(roleName) || "领导".equals(roleName)) {
      userVo.setRoleId(roleService.findRoleByRoleName("领导").getRoleId());
    }
    if (StringUtils.isBlank(userVo.getRoleId())) {
      return null;
    }
    return po2vo(userService.pageFind(userVo, pageable));
  }

  public void rePwd(String userId) {
    userService.rePwd(userId);
  }

  public void deleteUser(String userId) {
    userService.deleteUser(userId);
  }

  public int getActiveUserNumber() {
    return userService.getActiveUserNumber();
  }

  public void modify(UserVo userVo) {

    userService.update(vo2po(userVo));
  }

  public UserVo findById(String userId) {
    return po2vo(userService.findById(userId));
  }

  public UserVo findByUserAccount(String userAccount) {
    return po2vo(userService.findByUserAccount(userAccount));
  }

  public String getRoleName(String userAccount) {
    return roleService
        .findRoleByRoleId(userService.findByUserAccount(userAccount).getRoleId())
        .getRoleName();
  }
}
