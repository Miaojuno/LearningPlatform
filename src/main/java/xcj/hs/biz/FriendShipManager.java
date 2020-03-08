package xcj.hs.biz;

import xcj.hs.vo.FriendShipVo;

import java.util.List;

public interface FriendShipManager {

  /**
   * user1向user2发起好友请求/user1接受user2发起的好友请求
   *
   * @param userAccount1
   * @param userAccount2
   */
  void activeShip(String userAccount1, String userAccount2);

  List<FriendShipVo> findAllFriendActive(String userAccount);

  List<FriendShipVo> findAllFriendOnRequest(String userAccount);
}
