package xcj.hs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xcj.hs.dao.UserDao;
import xcj.hs.entity.User;
import xcj.hs.service.FriendShipService;
import xcj.hs.service.RoleService;
import xcj.hs.service.UserService;
import xcj.hs.vo.UserVo;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

  @Autowired UserDao userDao;

  @Autowired RoleService roleService;

  @Autowired FriendShipService friendShipService;

  public boolean loginCheck(String account, String pwd) {
    User user = userDao.findByUserAccount(account);
    if (user != null && "1".equals(user.getIsActive())) {
      if (user.getUserPwd().equals(shaEncoding(pwd))) {
        return true;
      }
    }
    return false;
  }

  public boolean create(User newUser) {
    User user = userDao.findByUserAccount(newUser.getUserAccount());
    if (user == null) {
      newUser.setSuperiorId("");
      newUser.setUserPwd(shaEncoding(newUser.getUserPwd()));
      userDao.save(newUser);
      return true;
    }
    return false;
  }

  public void modifyPic(String userAccount, byte[] pic) {
    userDao.modifyPicByUserAccount(userAccount, pic);
  }

  public void modifyRole(String userAccount, String roleId) {
    userDao.modifyRoleIdByUserAccount(roleId, userAccount);
  }

  public List<User> findAllActiveUser() {
    return userDao.findByIsActiveEquals("1");
  }

  public int getActiveUserNumber() {
    return userDao.countByIsActiveEquals("1");
  }

  public Page<User> pageFind(UserVo userVo, Pageable pageable) {
    // 非管理员 只可查询本人的下一级
    if (StringUtils.isNotBlank(userVo.getSuperiorAccount())) {
      List<Object> superiorIds = new ArrayList<>();
      superiorIds.add(userDao.findByUserAccount(userVo.getSuperiorAccount()).getUserId());
      superiorIds.add("");
      superiorIds.add(null);
      return userDao.findByIsActiveAndUserNameContainingAndSuperiorIdInAndRoleIdContaining(
          "1",
          userVo.getUserName(),
          superiorIds,
          StringUtils.isNotBlank(userVo.getRoleId())
              ? userVo.getRoleId()
              : roleService.findRoleByRoleName(userVo.getRoleName()).getRoleId(),
          pageable);
    }
    // 管理员
    return userDao.findByIsActiveAndUserNameContainingAndRoleIdContaining(
        "1", userVo.getUserName(), userVo.getRoleId(), pageable);
  }

  public boolean updateSuperior(String subordinateId, String superiorId) {
    // 自动建立好友关系
    friendShipService.activeShip(subordinateId, superiorId);
    friendShipService.activeShip(superiorId, subordinateId);
    // 更新上级
    User user = findById(subordinateId);
    if (user != null) {
      user.setSuperiorId(superiorId);
      userDao.save(user);
      return true;
    } else {
      return false;
    }
  }

  public void rePwd(String userId) {
    userDao.modifyUserPwdById(shaEncoding("123456"), userId);
  }

  public void deleteUser(String userId) {
    userDao.modifyIsActiveById("0", userId);
  }

  public void update(User user) {
    if (StringUtils.isNotBlank(user.getUserPwd())) {
      user.setUserPwd(shaEncoding(user.getUserPwd()));
    }
    userDao.save(getNewEntity(findById(user.getUserId()), user));
  }

  public User findById(String userId) {
    if (userDao.findById(userId).isPresent()) {
      return userDao.findById(userId).get();
    }
    return null;
  }

  public User findByUserAccount(String userAccount) {
    return userDao.findByUserAccount(userAccount);
  }

  public List<User> findAllSubordinate(String account) {
    return userDao.findBySuperiorIdAndIsActive(findByUserAccount(account).getUserId(), "1");
  }

  private String shaEncoding(String pwd) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA");
      messageDigest.update(pwd.getBytes());
      return new BigInteger(messageDigest.digest()).toString(32);
    } catch (NoSuchAlgorithmException e) {
      log.error("SHA加密出错");
      return null;
    }
  }
}
