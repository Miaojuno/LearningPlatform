package xcj.hs.biz.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xcj.hs.biz.FriendShipManager;
import xcj.hs.biz.UserManager;
import xcj.hs.entity.FriendShip;
import xcj.hs.service.FriendShipService;
import xcj.hs.service.UserService;
import xcj.hs.vo.FriendShipVo;

import java.util.List;

@Component
public class FriendShipManagerImpl extends BaseManagerImpl<FriendShipVo, FriendShip>
    implements FriendShipManager {
  @Autowired FriendShipService friendShipService;

  @Autowired UserManager userManager;

  @Autowired UserService userService;

  public void activeShip(String userAccount1, String userAccount2) {
    friendShipService.activeShip(
        userService.findByUserAccount(userAccount1).getUserId(),
        userService.findByUserAccount(userAccount2).getUserId());
  }

  public List<FriendShipVo> findAllFriendActive(String userAccount) {
    String userId = userService.findByUserAccount(userAccount).getUserId();
    List<FriendShipVo> result = po2vo(friendShipService.findFrinedsActive(userId));
    for (FriendShipVo friendShipVo : result) {
      if (friendShipVo.getFsUser1().equals(userId)) {
        friendShipVo.setUserVo(userManager.findById(friendShipVo.getFsUser2()));
      } else {
        friendShipVo.setUserVo(userManager.findById(friendShipVo.getFsUser1()));
      }
    }
    return result;
  }

  public List<FriendShipVo> findAllFriendOnRequest(String userAccount) {
    String userId = userService.findByUserAccount(userAccount).getUserId();
    List<FriendShipVo> result = po2vo(friendShipService.findFrinedsOnRequest(userId));
    for (FriendShipVo friendShipVo : result) {
      if (friendShipVo.getFsUser1().equals(userId)) {
        friendShipVo.setUserVo(userManager.findById(friendShipVo.getFsUser2()));
      } else {
        friendShipVo.setUserVo(userManager.findById(friendShipVo.getFsUser1()));
      }
    }
    return result;
  }
}
