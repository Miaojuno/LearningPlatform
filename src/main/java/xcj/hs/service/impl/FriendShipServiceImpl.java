package xcj.hs.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcj.hs.dao.FriendShipDao;
import xcj.hs.entity.FriendShip;
import xcj.hs.service.FriendShipService;
import xcj.hs.service.ImgService;
import xcj.hs.service.NewMsgService;
import xcj.hs.util.TimeUtil;
import xcj.hs.vo.FriendShipMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendShipServiceImpl extends BaseServiceImpl<FriendShip>
    implements FriendShipService {

  @Autowired FriendShipDao friendShipDao;

  @Autowired NewMsgService newMsgService;

  @Autowired ImgService imgService;

  public void activeShip(String userId1, String userId2) {
    FriendShip friendShip1 = friendShipDao.findByFsUser1AndFsUser2(userId1, userId2);
    FriendShip friendShip2 = friendShipDao.findByFsUser1AndFsUser2(userId2, userId1);
    // 好友关系已经存在，进行更新
    if (friendShip1 != null) {
      friendShip1.setFsUser1Active("1");
      friendShipDao.save(friendShip1);
    } else if (friendShip2 != null) {
      friendShip2.setFsUser2Active("1");
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
        .sorted((a, b) -> b.getFsLastTime().compareTo(a.getFsLastTime())) // 降序
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
        .sorted((a, b) -> b.getFsLastTime().compareTo(a.getFsLastTime())) // 降序
        .collect(Collectors.toList());
  }

  public void addMsgTo(String fromId, String toId, String msgContent, byte[] imgContent) {
    addMsg(
        fromId,
        friendShipDao.findByFsUser1AndFsUser2(fromId, toId) == null
            ? friendShipDao.findByFsUser1AndFsUser2(toId, fromId).getFsId()
            : friendShipDao.findByFsUser1AndFsUser2(fromId, toId).getFsId(),
        msgContent,
        imgContent);
  }

  public void addMsg(String userId, String id, String msgContent, byte[] imgContent) {
    newMsgService.addNumber(userId, 1);
    FriendShip friendShip = friendShipDao.findById(id).get();
    FriendShipMsg msg = new FriendShipMsg();
    msg.setMsgTime(TimeUtil.getCurrectTimeStr(TimeUtil.TIMESTR));
    msg.setMsgUser(userId);
    if (StringUtils.isNotBlank(msgContent)) {
      msg.setMsgContent(msgContent);
      msg.setMsgType("text");
    } else {
      msg.setMsgContent(imgService.save(imgContent));
      msg.setMsgType("img");
    }
    if (StringUtils.isBlank(friendShip.getFsMsgRecord())) { // 没记录
      List<FriendShipMsg> msgs = new ArrayList<>();
      msgs.add(msg);
      friendShip.setFsMsgRecord(JSONArray.toJSONString(msgs));
    } else { // 有记录
      List msgs = new ArrayList<>();
      JSONArray jsonArray = JSONArray.parseArray(friendShip.getFsMsgRecord());
      for (Object it : jsonArray) {
        msgs.add(it);
      }
      msgs.add(msg);
      friendShip.setFsMsgRecord(JSONArray.toJSONString(msgs));
    }
    // 对方未读数＋1
    if (friendShip.getFsUser1().equals(userId)) {
      friendShip.setFsUnread2(String.valueOf(Integer.parseInt(friendShip.getFsUnread2()) + 1));
    } else {
      friendShip.setFsUnread1(String.valueOf(Integer.parseInt(friendShip.getFsUnread1()) + 1));
    }
    // 更新时间
    friendShip.setFsLastTime(TimeUtil.getCurrectTimeStr(TimeUtil.TIMESTR));
    friendShipDao.save(friendShip);
  }

  public void readMsg(String userId, String fsId) {
    FriendShip friendShip = friendShipDao.findById(fsId).get();
    if (friendShip.getFsUser1().equals(userId)) {
      newMsgService.addNumber(userId, Integer.parseInt(friendShip.getFsUnread1()));
      friendShip.setFsUnread1("0");
    } else {
      newMsgService.addNumber(userId, Integer.parseInt(friendShip.getFsUnread2()));
      friendShip.setFsUnread2("0");
    }
    friendShipDao.save(friendShip);
  }

  public FriendShip findById(String fsId) {
    return friendShipDao.findById(fsId).get();
  }
}
