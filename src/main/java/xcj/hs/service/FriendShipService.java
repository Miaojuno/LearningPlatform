package xcj.hs.service;

import xcj.hs.entity.FriendShip;

import java.util.List;

public interface FriendShipService {
  /**
   * user1向user2发起好友请求/user1接受user2发起的好友请求
   *
   * @param userId1
   * @param userId2
   */
  void activeShip(String userId1, String userId2);

  /**
   * 所有好友
   *
   * @param userId
   * @return
   */
  List<FriendShip> findFrinedsActive(String userId);

  /**
   * 所有正在请求中的好友
   *
   * @param userId
   * @return
   */
  List<FriendShip> findFrinedsOnRequest(String userId);
}
