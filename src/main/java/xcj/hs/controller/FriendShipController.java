package xcj.hs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import xcj.hs.biz.FriendShipManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/friendShip")
public class FriendShipController {
  @Autowired FriendShipManager friendShipManager;

  /**
   * 好友关系主界面
   *
   * @param model
   * @return
   */
  @GetMapping("/main")
  public String mainPage(Model model) {
    return "friendShip/main";
  }

  /**
   * user1向user2发起好友请求/user1接受user2发起的好友请求
   *
   * @param userAccount1
   * @param userAccount2
   * @return
   */
  @PostMapping("/addOrAccept")
  @ResponseBody
  public Map<String, Object> addOrAccept(String userAccount1, String userAccount2) {
    Map<String, Object> map = new HashMap<String, Object>();
    friendShipManager.activeShip(userAccount1, userAccount2);
    map.put("success", true);
    return map;
  }

  /**
   * 获取当前用户的激活状态好友
   *
   * @param userAccount
   * @return
   */
  @PostMapping("/getActive")
  @ResponseBody
  public Map<String, Object> getActive(String userAccount) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("success", true);
    map.put("data", friendShipManager.findAllFriendActive(userAccount));
    return map;
  }

  /**
   * 获取当前用户的请求状态好友
   *
   * @param userAccount
   * @return
   */
  @PostMapping("/getOnRequest")
  @ResponseBody
  public Map<String, Object> getOnRequest(String userAccount) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("success", true);
    map.put("data", friendShipManager.findAllFriendOnRequest(userAccount));
    return map;
  }

  /**
   * addMsg
   *
   * @param userAccount
   * @param id
   * @param msgContent
   * @return
   */
  @PostMapping("/addMsg")
  @ResponseBody
  public Map<String, Object> addMsg(
      String userAccount, String id, String msgContent, MultipartFile file) throws IOException {
    Map<String, Object> map = new HashMap<String, Object>();
    if (file != null) {
      friendShipManager.addMsg(userAccount, id, msgContent, file.getBytes());
    } else {
      friendShipManager.addMsg(userAccount, id, msgContent, null);
    }

    map.put("success", true);
    return map;
  }

  /**
   * readMsg ，查看消息 用于未读数清空
   *
   * @param userAccount
   * @param fsId
   * @return
   */
  @PostMapping("/readMsg")
  @ResponseBody
  public Map<String, Object> readMsg(String userAccount, String fsId) {
    Map<String, Object> map = new HashMap<String, Object>();
    friendShipManager.readMsg(userAccount, fsId);
    map.put("success", true);
    return map;
  }

  /**
   * haveNewMsg ，查看是否有新消息
   *
   * @param userAccount
   * @return
   */
  @PostMapping("/haveNewMsg")
  @ResponseBody
  public Map<String, Object> haveNewMsg(String userAccount) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("success", true);
    map.put("data", friendShipManager.haveNewMsg(userAccount));
    return map;
  }

  /**
   * 根据图片id加载图片
   *
   * @param imgId
   * @return
   */
  @PostMapping("/getPic")
  @ResponseBody
  public Map<String, Object> getPic(String imgId) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("success", true);
    map.put("data", friendShipManager.getPic(imgId));
    return map;
  }
}
