package xcj.hs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcj.hs.dao.FriendShipDao;
import xcj.hs.entity.FriendShip;
import xcj.hs.service.FriendShipService;
import xcj.hs.util.TimeUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendShipServiceImpl extends BaseServiceImpl<FriendShip>
    implements FriendShipService {

  @Autowired FriendShipDao friendShipDao;

  public void activeShip(String userId1, String userId2) {
    FriendShip friendShip1 = friendShipDao.findByFsUser1AndFsUser2(userId1, userId2);
    FriendShip friendShip2 = friendShipDao.findByFsUser1AndFsUser2(userId2, userId1);
    // 好友关系已经存在，进行更新
    if (friendShip1 != null) {
      friendShip1.setFsUser1("1");
      friendShipDao.save(friendShip1);
    } else if (friendShip2 != null) {
      friendShip2.setFsUser2("1");
      friendShipDao.save(friendShip2);
    }
    // 好友关系不存在,新建好友关系
    else {
      FriendShip friendShip = new FriendShip();
      friendShip.setFsUser1(userId1);
      friendShip.setFsUser2(userId2);
      friendShip.setFsUnread1("0");
      friendShip.setFsUnread2("0");
      friendShip.setFsUser1Active("1");
      friendShip.setFsLastTime(TimeUtil.getCurrectTimeStr(TimeUtil.TIMESTR));
      friendShipDao.save(friendShip);
    }
  }

  public List<FriendShip> findFrinedsActive(String userId) {
    List<FriendShip> friendShips = friendShipDao.findByFsUser1OrFsUser2(userId, userId);
    return friendShips.stream()
        .filter(
            friendShip ->
                "1".equals(friendShip.getFsUser1Active())
                    && "1".equals(friendShip.getFsUser2Active()))
        .sorted((a, b) -> b.getFsLastTime().compareTo(a.getFsLastTime()))//降序
        .collect(Collectors.toList());
  }

  public List<FriendShip> findFrinedsOnRequest(String userId) {
    List<FriendShip> friendShips = friendShipDao.findByFsUser1OrFsUser2(userId, userId);
    return friendShips.stream()
        .filter( // 当前用户的active为null，对方的active为1
            friendShip ->
                (friendShip.getFsUser1().equals(userId)
                        && friendShip.getFsUser1Active() == null
                        && friendShip.getFsUser2Active() == "1")
                    || (friendShip.getFsUser2().equals(userId)
                        && friendShip.getFsUser2Active() == null
                        && friendShip.getFsUser1Active() == "1"))
        .sorted((a, b) -> b.getFsLastTime().compareTo(a.getFsLastTime()))//降序
        .collect(Collectors.toList());
  }
}
